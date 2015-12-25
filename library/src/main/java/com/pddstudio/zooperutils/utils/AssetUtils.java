package com.pddstudio.zooperutils.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.pddstudio.zooperutils.ZConf;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * This Class was created by Patrick J
 * on 24.12.15. For more Details and Licensing
 * have a look at the README.md
 */
public class AssetUtils {

    public interface AsyncAssetCallback {
        void onPrepare();
        void onImageExtracted(File imageFile);
        void onFailed();
        void onFinished(List<File> imageList);
    }

    final Context context;
    final AssetManager assetManager;
    final String[] assetWidgetList;

    //WORKING DIRECTORIES
    final File cacheDir;
    final File fileDir;

    //List with images
    final List<File> imageList = new LinkedList<>();

    public AssetUtils(Context context) throws IOException {
        this.context = context;
        this.assetManager = context.getAssets();
        this.assetWidgetList = assetManager.list(ZConf.ASSETS_TEMPLATE_DIR);
        this.cacheDir = new File(context.getCacheDir(), ZConf.ROOT_DIR);
        this.fileDir = new File(context.getFilesDir(), ZConf.ROOT_DIR);
        if(!cacheDir.exists() || !cacheDir.isDirectory()) FileUtils.forceMkdir(cacheDir);
        if(!fileDir.exists() || !fileDir.isDirectory()) FileUtils.forceMkdir(fileDir);
        FileUtils.cleanDirectory(cacheDir);
    }

    public void prepare() {
        new PreviewLoader().execute();
    }

    public void prepareWithCallback(AsyncAssetCallback asyncAssetCallback) {
        new PreviewLoader(asyncAssetCallback).execute();
    }

    public List<File> getImageList() {
        return imageList;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public File getFileDir() {
        return fileDir;
    }

    private class PreviewLoader extends AsyncTask<Void, Void, Void> {

        final AsyncAssetCallback asyncAssetCallback;

        PreviewLoader() {
            this.asyncAssetCallback = null;
        }

        PreviewLoader(AsyncAssetCallback asyncAssetCallback) {
            this.asyncAssetCallback = asyncAssetCallback;
        }

        @Override
        public void onPreExecute() {
            if(asyncAssetCallback != null) asyncAssetCallback.onPrepare();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for(String file : assetWidgetList) {
                    File cacheFile = new File(cacheDir, file);
                    InputStream assetInputStream = assetManager.open(ZConf.ASSETS_TEMPLATE_DIR + "/" + file);
                    OutputStream cacheOutputStream = new FileOutputStream(cacheFile);
                    IOUtils.copy(assetInputStream, cacheOutputStream);
                    if(cacheFile.exists()) {
                        String widgetName = FilenameUtils.getBaseName(cacheFile.getName());
                        File localFile = new File(fileDir, widgetName);
                        ZipFile zipFile = new ZipFile(cacheFile);
                        zipFile.extractFile(ZConf.PREVIEW_IMAGE, localFile.getAbsolutePath());
                        if(localFile.exists()) {
                            String imageName = FilenameUtils.getBaseName(localFile.getName());
                            File previewImage = new File(localFile, ZConf.PREVIEW_IMAGE);
                            File finalImage = new File(fileDir, imageName + ZConf.PREVIEW_IMAGE_EXT);
                            if(!finalImage.exists()) FileUtils.moveFile(previewImage, finalImage);
                            if(finalImage.exists()) {
                                //Log.d("PreviewLoader", "Resource extracted to : " + finalImage.getAbsolutePath());
                                imageList.add(finalImage);
                                if(asyncAssetCallback != null) asyncAssetCallback.onImageExtracted(finalImage);
                            }
                        }
                    }
                }
            } catch (IOException | ZipException io) {
                io.printStackTrace();
                if(asyncAssetCallback != null) asyncAssetCallback.onFailed();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            if(asyncAssetCallback != null) asyncAssetCallback.onFailed();
        }

        @Override
        public void onPostExecute(Void v) {
            if(asyncAssetCallback != null) asyncAssetCallback.onFinished(imageList);
        }

    }

}
