/*
 * ShapeDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public abstract class ShapeDrawer extends Brush {
    protected int startXPos;
    protected int startYPos;
    protected int endXPos;
    protected int endYPos;

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
            startXPos = xPos;
            startYPos = yPos;
        }
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
            endXPos = xPos;
            endYPos = yPos;
        }
        canvasPixelInfo.initEffectPixels();
        makeBorderEffect(canvasPixelInfo);
    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
            endXPos = xPos;
            endYPos = yPos;
        }
        canvasPixelInfo.initEffectPixels();
        makeBorderEffect(canvasPixelInfo);

        drawShape(canvasPixelInfo, color);
    }

    public abstract void drawShape(CanvasPixelInfo canvasPixelInfo, Color color);

    protected void makeBorderEffect(CanvasPixelInfo canvasPixelInfo) {
        int lowX;
        int lowY;
        int highX;
        int highY;

        if (startXPos > endXPos) {
            lowX = endXPos;
            highX = startXPos;
        }
        else {
            lowX = startXPos;
            highX = endXPos;
        }
        if (startYPos > endYPos) {
            lowY = endYPos;
            highY = startYPos;
        }
        else {
            lowY = startYPos;
            highY = endYPos;
        }

        for (int i = lowX; i < highX; i++) {
            if (i % 12 > 5) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * lowY + i,
                        toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * lowY + i]));
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * highY + i,
                        toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * highY + i]));
            }
        }

        for(int i = lowY; i < highY; i++) {
            if(i % 12 > 5) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * i + lowX,
                        toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * i + lowX]));
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * i + highX,
                        toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * i + highX]));
            }
        }
    }

    protected Color toInvertColor(int data) {
        Color color = new Color(data);
        int red, green, blue;

        if(color.getRed() > 100 && color.getRed() < 128)
            red = 156;
        else if(color.getRed() >= 128 && color.getRed() < 156)
            red = 100;
        else
            red = 255 - color.getRed();

        if(color.getGreen() > 100 && color.getGreen() < 128)
            green = 156;
        else if(color.getGreen() >= 128 && color.getGreen() < 156)
            green = 100;
        else
            green = 255 - color.getGreen();

        if(color.getBlue() > 100 && color.getBlue() < 128)
            blue = 156;
        else if(color.getBlue() >= 128 && color.getBlue() < 156)
            blue = 100;
        else
            blue = 255 - color.getBlue();

        return new Color(red, green, blue);
    }
}
