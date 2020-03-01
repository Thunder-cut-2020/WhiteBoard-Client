/*
 * Eraser.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class Eraser extends Brush {
    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        super.pressed(xPos, yPos, canvasPixelInfo, Color.WHITE);
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        canvasPixelInfo.initEffectPixels();
        super.dragged(xPos, yPos, canvasPixelInfo, Color.WHITE);
        this.moved(xPos, yPos, canvasPixelInfo, Color.WHITE);
        makeBorderEffect(xPos, yPos, canvasPixelInfo);
    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        super.released(xPos, yPos, canvasPixelInfo, Color.WHITE);
    }

    @Override
    public void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        super.moved(xPos, yPos, canvasPixelInfo, Color.WHITE);
        makeBorderEffect(xPos, yPos, canvasPixelInfo);
    }

    public void makeBorderEffect(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo) {
        xPos -= (super.getSize() / 2);
        yPos -= (super.getSize() / 2);
        for(int i=0; i<super.getSize(); i++) {
            if(!isOverCanvas(xPos + i, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * yPos + (xPos + i), Color.BLACK);
            }
            if(!isOverCanvas(xPos, yPos + i, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (yPos + i) + xPos, Color.BLACK);
            }
            if(!isOverCanvas(xPos + i, yPos + super.getSize(), canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (yPos + super.getSize()) + (xPos + i), Color.BLACK);
            }
            if(!isOverCanvas(xPos + super.getSize(), yPos + i, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (yPos + i ) + (xPos + super.getSize()), Color.BLACK);
            }
        }
    }
}
