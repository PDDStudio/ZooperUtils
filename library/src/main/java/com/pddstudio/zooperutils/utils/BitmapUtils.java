package com.pddstudio.zooperutils.utils;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;

/**
 * This Class was created by Patrick J
 * on 05.02.16. For more Details and Licensing
 * have a look at the README.md
 */
public class BitmapUtils {

    public static Bitmap replaceBackgroundColor(Bitmap bitmapSrc, @ColorInt int originalColor, @ColorInt int replaceColor) {
        int width = bitmapSrc.getWidth();
        int height = bitmapSrc.getHeight();
        int[] pixels = new int[width*height];
        bitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);

        int minX = width;
        int minY = height;
        int maxX = -1;
        int maxY = -1;

        Bitmap bitmapDest = Bitmap.createBitmap(width, height, bitmapSrc.getConfig());
        int pixel;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int index = y * width + x;
                pixel = pixels[index];
                if(pixel == originalColor) {
                    pixels[index] = replaceColor;
                }
                if(pixels[index] != replaceColor) {
                    if(x < minX) minX = x;
                    if(x > maxX) maxX = x;
                    if(y < minY) minY = y;
                    if(y > maxY) maxY = y;
                }
            }
        }

        bitmapDest.setPixels(pixels, 0, width, 0, 0, width, height);
        return Bitmap.createBitmap(bitmapDest, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);

    }
}
