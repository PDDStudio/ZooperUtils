package com.pddstudio.zooperutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.pddstudio.zooperutils.adapters.RecyclerAdapter;
import com.pddstudio.zooperutils.adapters.RecyclerImageAdapter;
import com.pddstudio.zooperutils.utils.AssetListener;
import com.pddstudio.zooperutils.utils.AssetUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This Class was created by Patrick J
 * on 20.12.15. For more Details and Licensing
 * have a look at the README.md
 */
public final class ZooperUtils implements AssetUtils.AsyncAssetCallback {

    private static ZooperUtils zooperUtils;

    final Context context;
    final AssetUtils assetUtils;
    final List<File> extractedImages;
    final List<SimpleWidgetCallback> simpleWidgetCallbacks;
    RecyclerAdapter recyclerAdapter;

    private ZooperUtils(Context context) throws IOException {
        this.context = context;
        this.assetUtils = new AssetUtils(context);
        this.extractedImages = new LinkedList<>();
        this.simpleWidgetCallbacks = new LinkedList<>();
        assetUtils.prepareWithCallback(this);
    }

    public static void init(Context context) throws IOException {
        zooperUtils = new ZooperUtils(context);
    }

    public static ZooperUtils getInstance() {
        return zooperUtils;
    }

    public static boolean instanceExist() {
        return zooperUtils != null;
    }

    public void registerSimpleWidgetCallback(SimpleWidgetCallback simpleWidgetCallback) {
        this.simpleWidgetCallbacks.add(simpleWidgetCallback);
    }

    public RecyclerAdapter getSimpleWidgetAdapter() {
        return recyclerAdapter;
    }

    public RecyclerImageAdapter createSimpleWidgetAdapter(List<Bitmap> images) {
        return new RecyclerImageAdapter(images);
    }

    public List<File> getExtractedImages() {
        return extractedImages;
    }

    public List<Bitmap> getExtractedBitmaps() {
        List<Bitmap> bitmapList = new ArrayList<>();
        for(File file : extractedImages) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if(bitmap != null) bitmapList.add(bitmap);
        }
        return bitmapList;
    }

    @Override
    public void onPrepare() {}

    @Override
    public void onImageExtracted(File imageFile) {
        this.extractedImages.add(imageFile);
    }

    @Override
    public void onFailed() {
        try {
            FileUtils.cleanDirectory(assetUtils.getCacheDir());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void onFinished(List<File> imageList) {
        try {
            FileUtils.cleanDirectory(assetUtils.getCacheDir());
            for(File file : assetUtils.getFileDir().listFiles()) {
                if (file.isDirectory()) FileUtils.deleteDirectory(file);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        this.recyclerAdapter = new RecyclerAdapter(imageList);
        for(SimpleWidgetCallback callback : simpleWidgetCallbacks) {
            callback.onFinishedLoading(getExtractedBitmaps());
        }
    }

}
