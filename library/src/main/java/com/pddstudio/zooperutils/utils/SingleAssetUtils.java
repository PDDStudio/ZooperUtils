package com.pddstudio.zooperutils.utils;

import android.content.Context;

import com.pddstudio.zooperutils.ZConf;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This Class was created by Patrick J
 * on 28.12.15. For more Details and Licensing
 * have a look at the README.md
 */
public class SingleAssetUtils {

    final Context context;
    final File cacheDir;

    public SingleAssetUtils(Context context) {
        this.context = context;
        this.cacheDir = new File(context.getCacheDir(), ZConf.ROOT_DIR);
    }

    public File extractPreviewImage(File widgetFile, File saveLocation) {
        String fileName = FilenameUtils.getBaseName(widgetFile.getName()) + ZConf.PREVIEW_IMAGE_EXT;
        File savedBitmap = new File(saveLocation, fileName);
        try {
            File cacheFile = new File(cacheDir, widgetFile.getName());
            InputStream inputStream = new FileInputStream(widgetFile);
            OutputStream cacheOutputStream = new FileOutputStream(cacheFile);
            IOUtils.copy(inputStream, cacheOutputStream);
            if(cacheFile.exists()) {
                String widgetName = FilenameUtils.getBaseName(cacheFile.getName());
                ZipFile zipFile = new ZipFile(cacheFile);
                zipFile.extractFile(ZConf.PREVIEW_IMAGE, saveLocation.getAbsolutePath());
                if(saveLocation.exists()) {
                    String imageName = FilenameUtils.getBaseName(saveLocation.getName());
                    File previewImage = new File(saveLocation, ZConf.PREVIEW_IMAGE);
                    if(!savedBitmap.exists()) FileUtils.moveFile(previewImage, savedBitmap);
                    if(savedBitmap.exists()) {
                        //Log.d("PreviewLoader", "Resource extracted to : " + finalImage.getAbsolutePath());
                        File renamedBitmap = new File(savedBitmap.getParent() + "/" + savedBitmap.getName().replace(" ","_"));
                        boolean rename = savedBitmap.renameTo(renamedBitmap);
                        if(rename) return renamedBitmap;
                        else return savedBitmap;
                    }
                }
            }
        } catch (IOException | ZipException io) {
            io.printStackTrace();
        }
        return null;
    }

}
