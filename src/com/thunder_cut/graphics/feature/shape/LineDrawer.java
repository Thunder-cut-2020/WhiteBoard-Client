/*
 * LineDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-17
 */
package com.thunder_cut.graphics.feature.shape;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LineDrawer extends ShapeDrawer {
    protected double currentX;
    protected double currentY;
    protected boolean isPlusX;
    protected boolean isPlusY;
    protected int deltaX;
    protected int deltaY;
    protected boolean isMaxDeltaX;

    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        isPlusX = (startXPos < endXPos);
        isPlusY = (startYPos < endYPos);
        currentX = startXPos;
        currentY = startYPos;
        deltaX = Math.abs(endXPos - startXPos);
        deltaY = Math.abs(endYPos - startYPos);
        isMaxDeltaX = deltaX > deltaY;

        if ((deltaX == 0) || (deltaY == 0)) {
            drawNotDiagonalLine(canvasPixelInfo, color);
            return;
        }

        double ratio = (double) Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
        double count = 0;
        Map<Double, Double> correctPositions = new HashMap<>();

        while ((currentX != endXPos) && (currentY != endYPos)) {
            if (isMaxDeltaX) {
                correctPositions.put(currentX, currentY);
            } else {
                correctPositions.put(currentY, currentX);
            }

            if ((count >= ratio)) {
                controlPosition(!isMaxDeltaX, isMaxDeltaX ? isPlusY : isPlusX, 1);
                count = 0;
            } else {
                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY, 0.01);
                count += 0.01;
            }
        }

        for (Map.Entry<Double, Double> entry : correctPositions.entrySet()) {
            if (isMaxDeltaX) {
                currentX = entry.getKey();
                currentY = entry.getValue();
            } else {
                currentX = entry.getValue();
                currentY = entry.getKey();
            }
            setPixels(canvasPixelInfo, (int) currentX, (int) currentY, color);
        }
    }

    private void drawNotDiagonalLine(CanvasPixelInfo canvasPixelInfo, Color color) {
        while ((currentX != endXPos) || (currentY != endYPos)) {
            setPixels(canvasPixelInfo, (int) currentX, (int) currentY, color);

            controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY, 1);
        }
    }

    protected void controlPosition(boolean isMaxDeltaX, boolean isPlus, double moveDistance) {
        if (isMaxDeltaX) {
            currentX += isPlus ? moveDistance : moveDistance * -1;
            return;
        }

        currentY += isPlus ? moveDistance : moveDistance * -1;
    }
}