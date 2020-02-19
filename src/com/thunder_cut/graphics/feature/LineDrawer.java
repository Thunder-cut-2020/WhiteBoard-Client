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
    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;

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

    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        boolean isPlusX = (startXPos < endXPos);
        boolean isPlusY = (startYPos < endYPos);
        currentX = startXPos;
        currentY = startYPos;
        int deltaX = Math.abs(endXPos - startXPos);
        int deltaY = Math.abs(endYPos - startYPos);
        boolean isMaxDeltaX = deltaX > deltaY;

        if((deltaX == 0) || (deltaY == 0)) {
            while((currentX != endXPos) || (currentY != endYPos)) {
                setPixels(canvasPixelInfo, currentX, currentY, color);

                controlPosition(isMaxDeltaX, isMaxDeltaX ? isPlusX : isPlusY);
            }

            return;
        }

        int pixelMoveRatio = Math.max(deltaX, deltaY) / Math.min(deltaX, deltaY);
        int count = 0;
        Map<Integer, Integer> ratioMap = new HashMap<>();
        List<Integer> ratios = new ArrayList<>();

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

        currentX = startXPos;
        currentY = startYPos;
        int mapRatio;

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
        double ratioCount = 0;

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

    @Override
    public void makeBorderEffect(CanvasPixelInfo canvasPixelInfo) {
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
}
