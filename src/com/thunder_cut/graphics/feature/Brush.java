/*
 * Brush.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class Brush implements DrawingFeature {
    public static final int DEFAULT_SIZE = 1;
    protected static int size;
    private int prevXPos;
    private int prevYPos;
    protected int currentX;
    protected int currentY;

    public Brush() {
        size = DEFAULT_SIZE;
    }

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        prevXPos = xPos;
        prevYPos = yPos;
        setPixels(canvasPixelInfo, xPos, yPos, color);
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        boolean isPlusX = (prevXPos < xPos);
        boolean isPlusY = (prevYPos < yPos);
        currentX = prevXPos;
        currentY = prevYPos;
        int deltaX = Math.abs(xPos - prevXPos);
        int deltaY = Math.abs(yPos - prevYPos);
        boolean isMaxDeltaX = deltaX > deltaY;

        if((deltaX == 0) || (deltaY == 0)) {
            while((currentX != xPos) || (currentY != yPos)) {
                setPixels(canvasPixelInfo, currentX, currentY, color);

                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);
            }

            return;
        }

        int ratio = Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
        int count = 0;

        while((currentX != xPos) && (currentY != yPos)) {
            setPixels(canvasPixelInfo, currentX, currentY, color);

            if((count >= ratio)) {
                controlPosition(!isMaxDeltaX, isMaxDeltaX ? isPlusY : isPlusX);

                count = 0;
                deltaX = Math.abs(xPos - currentX);
                deltaY = Math.abs(yPos - currentY);
                ratio =  Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
            }
            else {
                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);

                count++;
            }
        }

        prevXPos = xPos;
        prevYPos = yPos;
    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    @Override
    public void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        canvasPixelInfo.initEffectPixels();
        setEffectPixels(canvasPixelInfo, xPos, yPos, color);
    }

    protected void setPixels(CanvasPixelInfo canvasPixelInfo, int xPos, int yPos, Color color) {
        xPos -= (size / 2);
        yPos -= (size / 2);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel(canvasPixelInfo.getWidth() * yPos + xPos, color);
                }
                xPos++;
            }

            xPos -= size;
            yPos++;
        }
    }

    private void setEffectPixels(CanvasPixelInfo canvasPixelInfo, int xPos, int yPos, Color color) {
        xPos -= (size / 2);
        yPos -= (size / 2);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * yPos + xPos, color);
                }
                xPos++;
            }

            xPos -= size;
            yPos++;
        }
    }

    protected void controlPosition(boolean isMaxDeltaX, boolean isPlus) {
        if (isMaxDeltaX) {
            currentX += (int) Math.pow(-1, isPlus ? 0 : 1);
            return;
        }
        currentY += (int) Math.pow(-1, isPlus ? 0 : 1);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
