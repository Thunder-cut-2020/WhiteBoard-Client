/*
 * AreaSelector.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class AreaSelector implements DrawingFeature {
    ImageExtractor imageExtractor;
    int[] extractPixels;

    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;

    private int prevXPos;
    private int prevYPos;
    private int xPosMove;
    private int yPosMove;

    private boolean moveMode;
    private boolean isCtrlPressed;

    public AreaSelector() {
        imageExtractor = new ImageExtractor();
        moveMode = false;
        isCtrlPressed = false;

        xPosMove = 0;
        yPosMove = 0;
    }

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(isInArea(xPos, yPos)) {
            moveMode = true;

            prevXPos = xPos;
            prevYPos = yPos;

            copySelectedArea(canvasPixelInfo);
        }
        else {
            startXPos = xPos;
            startYPos = yPos;
        }
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        canvasPixelInfo.initEffectPixels();

        if(moveMode) {
            xPosMove = xPos - prevXPos;
            yPosMove = yPos - prevYPos;

            moveSelectedArea(canvasPixelInfo);
        }
        else {
            if (!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                endXPos = xPos;
                endYPos = yPos;
            }
        }

        makeBorderEffect(canvasPixelInfo);
    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(moveMode) {
            moveMode = false;

            pasteSelectedArea(canvasPixelInfo);

            startXPos += xPosMove;
            startYPos += yPosMove;
            endXPos += xPosMove;
            endYPos += yPosMove;
            xPosMove = 0;
            yPosMove = 0;
        }
        else {
            if (!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                endXPos = xPos;
                endYPos = yPos;
            }
            updateXY();
        }
        canvasPixelInfo.initEffectPixels();
        makeBorderEffect(canvasPixelInfo);
    }

    @Override
    public void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    public void copySelectedArea(CanvasPixelInfo canvasPixelInfo) {
        imageExtractor.extract(canvasPixelInfo.getPixels(), startXPos, startYPos, endXPos, endYPos, canvasPixelInfo.getWidth());
        extractPixels = imageExtractor.getExtractedPixels();
    }

    public void moveSelectedArea(CanvasPixelInfo canvasPixelInfo) {
        for(int nowHeight = startYPos; nowHeight <= endYPos; nowHeight++) {
            for (int nowWidth = startXPos; nowWidth <= endXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth + xPosMove, nowHeight + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel((nowHeight + yPosMove) * canvasPixelInfo.getWidth() + (nowWidth + xPosMove),
                            new Color(extractPixels[(nowHeight - startYPos) * (endXPos - startXPos + 1) + (nowWidth - startXPos)]));
                    // If Ctrl key is pressed, then don't erase previous. ( do copy and paste )
                    // If Ctrl key isn't pressed, then erase previous. ( do cut and paste )
                    // Key event isn't make yet.
                    if(!isCtrlPressed) {
                        canvasPixelInfo.setPixel(nowHeight * canvasPixelInfo.getWidth() + nowWidth, Color.WHITE);
                    }
                }
            }
        }
    }

    public void pasteSelectedArea(CanvasPixelInfo canvasPixelInfo) {
        for(int nowHeight = startYPos; nowHeight <= endYPos; nowHeight++) {
            for (int nowWidth = startXPos; nowWidth <= endXPos; nowWidth++) {
                if (!isOverCanvas(nowWidth + xPosMove, nowHeight + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setPixel((nowHeight + yPosMove) * canvasPixelInfo.getWidth() + (nowWidth + xPosMove),
                            new Color(extractPixels[(nowHeight - startYPos) * (endXPos - startXPos + 1) + (nowWidth - startXPos)]));
                }
            }
        }

    }

    public void makeBorderEffect(CanvasPixelInfo canvasPixelInfo) {
        int i = startXPos;

        while(i != endXPos) {
            if (i % 12 > 5) {
                if(!isOverCanvas(i + xPosMove, startYPos + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (startYPos + yPosMove) + (i + xPosMove),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (startYPos + yPosMove) + (i + xPosMove)]));
                }
                if(!isOverCanvas(i + xPosMove, endYPos + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (endYPos + yPosMove) + (i + xPosMove),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (endYPos + yPosMove) + (i + xPosMove)]));
                }
            }
            if(startXPos > endXPos)  {
                i--;
            }
            else {
                i++;
            }
        }

        i = startYPos;
        while(i != endYPos) {
            if(i % 12 > 5) {
                if(!isOverCanvas(startXPos + xPosMove, i + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (i + yPosMove) + (startXPos + xPosMove),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (i + yPosMove) + (startXPos + xPosMove)]));
                }
                if(!isOverCanvas(endXPos + xPosMove, i + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (i + yPosMove) + (endXPos + xPosMove),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (i + yPosMove) + (endXPos + xPosMove)]));
                }
            }
            if(startYPos > endYPos) {
                i--;
            }
            else {
                i++;
            }
        }

    }

    private Color toInvertColor(int data) {
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

    private void updateXY() {
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

    public void setIsCtrlPressed(boolean value) {
        isCtrlPressed = value;
    }

    private boolean isInArea(int xPos, int yPos) {
        return (xPos > startXPos && xPos < endXPos && yPos > startYPos && yPos < endYPos);
    }

}
