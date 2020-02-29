/*
 * LineDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineDrawer extends ShapeDrawer{
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

        if((deltaX == 0) || (deltaY == 0)) {
            drawNotDiagonalLine(canvasPixelInfo, color);
            return;
        }

        Map<Integer, Integer> ratioMap = new HashMap<>();
        List<Integer> ratios = new ArrayList<>();
        makeRatioList(ratioMap, ratios);

        currentX = startXPos;
        currentY = startYPos;

        drawLine(ratioMap, ratios, canvasPixelInfo, color);
    }

    private void drawNotDiagonalLine(CanvasPixelInfo canvasPixelInfo, Color color) {
        while((currentX != endXPos) || (currentY != endYPos)) {
            setPixels(canvasPixelInfo, currentX, currentY, color);

            controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);
        }
    }

    protected void makeRatioList(Map<Integer, Integer> ratioMap, List<Integer> ratios) {
        int pixelMoveRatio = Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
        int count = 0;

        while((currentX != endXPos) && (currentY != endYPos)) {
            if(!ratioMap.containsKey(pixelMoveRatio)) {
                ratioMap.put(pixelMoveRatio, 1);
                ratios.add(pixelMoveRatio);
            }
            else {
                ratioMap.replace(pixelMoveRatio, ratioMap.get(pixelMoveRatio) + 1);
            }

            if((count >= pixelMoveRatio)) {
                controlPosition(!isMaxDeltaX, isMaxDeltaX ? isPlusY : isPlusX);
                count = 0;

                deltaX = Math.abs(endXPos - currentX);
                deltaY = Math.abs(endYPos - currentY);
                pixelMoveRatio =  Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
            }
            else {
                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);
                count++;
            }
        }
    }

    protected void drawLine(Map<Integer,Integer> ratioMap, List<Integer> ratios,
                          CanvasPixelInfo canvasPixelInfo, Color color) {
        int mapRatio;
        int count = 0;

        int pixelMoveRatio;
        if(ratios.size() == 1) {
            mapRatio = 0;
            pixelMoveRatio = ratioMap.get(ratios.get(0));
        }
        else {
            mapRatio = Math.max(ratioMap.get(ratios.get(0)), ratioMap.get(ratios.get(1))) /
                    Math.min(ratioMap.get(ratios.get(0)), ratioMap.get(ratios.get(1)));
            pixelMoveRatio = (Math.max(ratioMap.get(ratios.get(0)), ratioMap.get(ratios.get(1))) ==
                    ratioMap.get(ratios.get(0))) ? ratios.get(0) : ratios.get(1);
        }
        int ratioCount = 0;

        while((currentX != endXPos) && (currentY != endYPos)) {
            setPixels(canvasPixelInfo, currentX, currentY, color);

            if((count >= pixelMoveRatio)) {
                controlPosition(!isMaxDeltaX, isMaxDeltaX ? isPlusY : isPlusX);
                count = 0;
                ratioCount++;

                if(ratios.size() == 1) {
                    continue;
                }
                if((ratioCount >= mapRatio)) {
                    pixelMoveRatio = (pixelMoveRatio == ratios.get(0)) ? ratios.get(1) : ratios.get(0);
                    ratioCount = 0;
                }
                else if(pixelMoveRatio != Math.max(ratioMap.get(ratios.get(0)), ratioMap.get(ratios.get(1)))) {
                    pixelMoveRatio = (pixelMoveRatio == ratios.get(0)) ? ratios.get(1) : ratios.get(0);
                }
            }
            else {
                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);
                count++;
            }
        }
    }
}
