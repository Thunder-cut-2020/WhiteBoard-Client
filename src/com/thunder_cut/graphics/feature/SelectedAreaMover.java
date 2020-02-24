/*
 * SelectedAreaMover.java
 * Author : Cwhist
 * Created Date : 2020-02-23
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class SelectedAreaMover {
    ImageExtractor imageExtractor;
    int[] extractPixels;

    private int prevXPos;
    private int prevYPos;
    private int xPosMove;
    private int yPosMove;

    private boolean isCtrlPressed;

    public SelectedAreaMover() {
        isCtrlPressed = false;

        imageExtractor = new ImageExtractor();

        initPosMove();
    }

    public void copySelectedArea(CanvasPixelInfo canvasPixelInfo, int startXPos, int startYPos, int endXPos, int endYPos) {
        imageExtractor.extract(canvasPixelInfo.getPixels(), startXPos, startYPos, endXPos, endYPos, canvasPixelInfo.getWidth());
        extractPixels = imageExtractor.getExtractedPixels();
    }

    public void moveSelectedArea(CanvasPixelInfo canvasPixelInfo, int startXPos, int startYPos, int endXPos, int endYPos) {
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

    public void pasteSelectedArea(CanvasPixelInfo canvasPixelInfo, int startXPos, int startYPos, int endXPos, int endYPos) {
        for(int nowHeight = startYPos; nowHeight <= endYPos; nowHeight++) {
            for (int nowWidth = startXPos; nowWidth <= endXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth + xPosMove, nowHeight + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel((nowHeight + yPosMove) * canvasPixelInfo.getWidth() + (nowWidth + xPosMove),
                            new Color(extractPixels[(nowHeight - startYPos) * (endXPos - startXPos + 1) + (nowWidth - startXPos)]));
                }
            }
        }

    }

    public void setPrevPos(int xPos, int yPos) {
        prevXPos = xPos;
        prevYPos = yPos;
    }

    public void setPosMove(int xPos, int yPos) {
        this.xPosMove = xPos - prevXPos;
        this.yPosMove = yPos - prevYPos;
    }

    public void initPosMove() {
        xPosMove = 0;
        yPosMove = 0;
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

    private boolean isOverCanvas(int currentX, int currentY, int width, int height) {
        return ((currentX < 0) || (currentX >= width) || (currentY < 0) || (currentY >= height));
    }

}
