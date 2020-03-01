/*
 * Resizer.java
 * Author : Cwhist
 * Created Date : 2020-02-06
 */
package com.thunder_cut.graphics.controller;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class Resizer {
    public void resize(int width, int height, CanvasPixelInfo canvasPixelInfo) {
        if(width <= 0 || height <= 0) {
            return;
        }
        int[] newPixels = new int[width * height];

        for(int nowHeight=0; nowHeight<height; nowHeight++) {
            try {
                System.arraycopy(canvasPixelInfo.getPixels(), (nowHeight * canvasPixelInfo.getWidth()), newPixels, nowHeight * width, canvasPixelInfo.getWidth());
            }
            catch (Exception e) {
                // height expanded
                for(int i = 0; i<width; i++) {
                    newPixels[nowHeight * width + i] = Color.WHITE.getRGB();
                }
            }
            // width expanded
            for(int expandWidth = canvasPixelInfo.getWidth(); expandWidth<width; expandWidth++) {
                newPixels[nowHeight * width + expandWidth] = Color.WHITE.getRGB();
            }
        }

        canvasPixelInfo.setPixels(newPixels, width, height);
        canvasPixelInfo.initEffectPixels();
    }
}
