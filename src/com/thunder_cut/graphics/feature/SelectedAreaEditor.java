/*
 * SelectedAreaMover.java
 * Author : Cwhist
 * Created Date : 2020-02-23
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class SelectedAreaEditor {
    ImageExtractor imageExtractor;
    int[] extractPixels;

    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;

    private int prevMouseXPos;
    private int prevMouseYPos;
    private int xPosMove;
    private int yPosMove;

    private boolean isCtrlPressed;

    public SelectedAreaEditor() {
        isCtrlPressed = false;

        imageExtractor = new ImageExtractor();

        initPosMove();
    }

    public void copySelectedArea(CanvasPixelInfo canvasPixelInfo) {
        if(!isOverCanvas(startXPos, startYPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())
                && !isOverCanvas(endXPos, endYPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
            imageExtractor.extract(canvasPixelInfo.getPixels(), startXPos, startYPos, endXPos, endYPos, canvasPixelInfo.getWidth());
            extractPixels = imageExtractor.getExtractedPixels();
        }
    }

    public void moveSelectedArea(CanvasPixelInfo canvasPixelInfo) {
        for(int nowHeight = startYPos; nowHeight <= endYPos; nowHeight++) {
            for (int nowWidth = startXPos; nowWidth <= endXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth + xPosMove, nowHeight + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel((nowHeight + yPosMove) * canvasPixelInfo.getWidth() + (nowWidth + xPosMove),
                            new Color(extractPixels[(nowHeight - startYPos) * (endXPos - startXPos + 1) + (nowWidth - startXPos)]));
                }
                // If Ctrl key is pressed, then don't erase previous. ( do copy and paste )
                // If Ctrl key isn't pressed, then erase previous. ( do cut and paste )
                // Key event isn't make yet.
                if(!isCtrlPressed && !isOverCanvas(nowWidth, nowHeight, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel(nowHeight * canvasPixelInfo.getWidth() + nowWidth, Color.WHITE);
                }
            }
        }

    }

    public void pasteSelectedArea(CanvasPixelInfo canvasPixelInfo) {
        for(int nowHeight = 0; nowHeight <= endYPos - startYPos; nowHeight++) {
            for (int nowWidth = 0; nowWidth <= endXPos - startXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth, nowHeight, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel(nowHeight * canvasPixelInfo.getWidth() + nowWidth,
                            new Color(extractPixels[nowHeight * (endXPos - startXPos + 1) + nowWidth]));
                }
            }
        }
        setEndPos(endXPos-startXPos, endYPos-startYPos);
        setStartPos(0, 0);
    }

    public void pasteMovedSelectedArea(CanvasPixelInfo canvasPixelInfo) {
        for(int nowHeight = startYPos; nowHeight <= endYPos; nowHeight++) {
            for (int nowWidth = startXPos; nowWidth <= endXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth, nowHeight, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel(nowHeight * canvasPixelInfo.getWidth() + nowWidth,
                            new Color(extractPixels[(nowHeight - startYPos) * (endXPos - startXPos + 1) + (nowWidth - startXPos)]));
                }
            }
        }

    }

    public void updateXY() {
        int temp;

        if (startXPos > endXPos) {
            temp = startXPos;
            startXPos = endXPos;
            endXPos = temp;
        }

        if (startYPos > endYPos) {
            temp = startYPos;
            startYPos = endYPos;
            endYPos = temp;
        }
    }

    public void setStartPos(int xPos, int yPos) {
        startXPos = xPos;
        startYPos = yPos;
    }

    public void setEndPos(int xPos, int yPos) {
        endXPos = xPos;
        endYPos = yPos;
    }

    public void setPrevMousePos(int xPos, int yPos) {
        prevMouseXPos = xPos;
        prevMouseYPos = yPos;
    }

    public void setChangedAreaPos() {
        startXPos += xPosMove;
        startYPos += yPosMove;
        endXPos += xPosMove;
        endYPos += yPosMove;
    }

    public void setPosMove(int xPos, int yPos) {
        this.xPosMove = xPos - prevMouseXPos;
        this.yPosMove = yPos - prevMouseYPos;
    }

    public void initPosMove() {
        xPosMove = 0;
        yPosMove = 0;
    }

    public int getStartXPos() {
        return startXPos;
    }

    public int getStartYPos() {
        return startYPos;
    }

    public int getEndXPos() {
        return endXPos;
    }

    public int getEndYPos() {
        return endYPos;
    }

    public int getXPosMove() {
        return xPosMove;
    }

    public int getYPosMove() {
        return yPosMove;
    }

    public void setIsCtrlPressed(boolean value) {
        isCtrlPressed = value;
    }

    public boolean isInArea(int xPos, int yPos) {
        return (xPos > startXPos && xPos < endXPos && yPos > startYPos && yPos < endYPos);
    }

    private boolean isOverCanvas(int currentX, int currentY, int width, int height) {
        return ((currentX < 0) || (currentX >= width) || (currentY < 0) || (currentY >= height));
    }

}
