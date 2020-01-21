/*
 * SelectArea.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class SelectArea implements DrawingFeature {
    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        startXPos = xPos;
        startYPos = yPos;
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        endXPos = xPos;
        endYPos = yPos;
    }
}
