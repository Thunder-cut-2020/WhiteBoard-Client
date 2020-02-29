/*
 * CanvasPixelInfo.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.ui.drawing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class CanvasPixelInfo {
    private int[] pixels;
    private int[] effectPixels;
    private int width;
    private int height;

    public CanvasPixelInfo(int width, int height, Color backgroundColor) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        effectPixels = new int[width * height];

        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = backgroundColor.getRGB();
            effectPixels[i] = new Color(255,255,255,0).getRGB();
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < imagePixels.length; i++) {
            imagePixels[i] = pixels[i];
        }

        return image;
    }

    public BufferedImage toBufferedImageEffect() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < imagePixels.length; i++) {
            imagePixels[i] = effectPixels[i];
        }

        return image;
    }

    public void initEffectPixels() {
        effectPixels = new int[width * height];
        for(int i = 0; i < pixels.length; i++) {
            effectPixels[i] = new Color(255,255,255,0).getRGB();
        }
    }

    public static int[] convertImageToPixels(BufferedImage image) {
        int[] srcPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        int[] outPixels = new int[image.getWidth() * image.getHeight()];

        System.arraycopy(srcPixels, 0, outPixels, 0, srcPixels.length);

        return outPixels;
    }

    public void setPixel(int index, Color color) {
        pixels[index] = color.getRGB();
    }

    public void setEffectPixel(int index, Color color) {
        effectPixels[index] = color.getRGB();
    }

    public void setPixels(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
