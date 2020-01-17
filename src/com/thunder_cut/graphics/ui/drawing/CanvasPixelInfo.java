package com.thunder_cut.graphics.ui.drawing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class CanvasPixelInfo {
    private int[] pixels;
    private int width;
    private int height;

    public CanvasPixelInfo(int width, int height, Color backgroundColor) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = backgroundColor.getRGB();
        }
    }

    public void toBufferedImage(BufferedImage image) {
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < imagePixels.length; i++) {
            imagePixels[i] = pixels[i];
        }
    }

    public void setPixel(int index, Color color) {
        pixels[index] = color.getRGB();
    }

    public int[] getPixels() {
        return pixels;
    }
}