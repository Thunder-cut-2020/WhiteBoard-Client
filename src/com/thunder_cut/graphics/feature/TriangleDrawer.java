/*
 * TriangleDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-20
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TriangleDrawer extends LineDrawer{
    private int leftEndXPos;
    private int rightEndXPos;
    private int underYPos;

    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        if (isShapedLine()) {
            return;
        }

        leftEndXPos = Math.min(startXPos, endXPos);
        rightEndXPos = Math.max(startXPos, endXPos);
        underYPos = Math.max(startYPos, endYPos);

        startXPos = (startXPos + endXPos) / 2;
        startYPos = Math.min(startYPos, endYPos);
        endXPos = leftEndXPos;
        endYPos = underYPos;
        currentX = startXPos;
        currentY = startYPos;
        drawLeftLine(canvasPixelInfo, color);

        endXPos = rightEndXPos;
        currentX = startXPos;
        currentY = startYPos;
        drawRightLine(canvasPixelInfo, color);

        currentX = leftEndXPos;
        currentY = underYPos;
        endXPos = rightEndXPos;
        drawUnderLine(canvasPixelInfo, color);
    }

    private void drawUnderLine(CanvasPixelInfo canvasPixelInfo, Color color) {
        while(currentX != endXPos) {
            setPixels(canvasPixelInfo, currentX, currentY, color);
            controlPosition(true, true);
        }
    }

    private void drawLeftLine(CanvasPixelInfo canvasPixelInfo, Color color) {
        isPlusX = (startXPos < endXPos);
        isPlusY = (startYPos < endYPos);
        deltaX = Math.abs(endXPos - startXPos);
        deltaY = Math.abs(endYPos - startYPos);
        isMaxDeltaX = deltaX > deltaY;

        Map<Integer, Integer> ratioMap = new HashMap<>();
        List<Integer> ratios = new ArrayList<>();
        makeRatioList(ratioMap, ratios);

        currentX = startXPos;
        currentY = startYPos;

        drawLine(ratioMap, ratios, canvasPixelInfo, color);

        leftEndXPos = currentX;
        underYPos = currentY;
    }

    private void drawRightLine(CanvasPixelInfo canvasPixelInfo, Color color) {
        isPlusX = (startXPos < endXPos);
        isPlusY = (startYPos < endYPos);
        deltaX = Math.abs(endXPos - startXPos);
        deltaY = Math.abs(endYPos - startYPos);
        isMaxDeltaX = deltaX > deltaY;

        Map<Integer, Integer> ratioMap = new HashMap<>();
        List<Integer> ratios = new ArrayList<>();
        makeRatioList(ratioMap, ratios);

        currentX = startXPos;
        currentY = startYPos;

        drawLine(ratioMap, ratios, canvasPixelInfo, color);

        rightEndXPos = currentX;
    }
}
