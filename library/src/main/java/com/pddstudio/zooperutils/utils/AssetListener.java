package com.pddstudio.zooperutils.utils;

import java.io.File;
import java.util.List;

/**
 * This Class was created by Patrick J
 * on 25.12.15. For more Details and Licensing
 * have a look at the README.md
 */
public abstract class AssetListener implements AssetUtils.AsyncAssetCallback {
    public void onPrepare() {}
    public void onImageExtracted(File imageFile) {}
    public void onFailed() {}
    public abstract void onFinished(List<File> imageList);
}
