/*
 * AreaSelector.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class AreaSelector implements DrawingFeature {
    SelectedAreaMover areaMover;

    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;

    private boolean moveMode;

    public AreaSelector() {
        moveMode = false;

        areaMover = new SelectedAreaMover();
        areaMover.initPosMove();
    }

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(isInArea(xPos, yPos)) {
            moveMode = true;

            areaMover.setPrevPos(xPos, yPos);
            areaMover.copySelectedArea(canvasPixelInfo, startXPos, startYPos, endXPos, endYPos);
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
            areaMover.setPosMove(xPos, yPos);

            areaMover.moveSelectedArea(canvasPixelInfo, startXPos, startYPos, endXPos, endYPos);
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

            areaMover.pasteSelectedArea(canvasPixelInfo, startXPos, startYPos, endXPos, endYPos);

            startXPos += areaMover.getXPosMove();
            startYPos += areaMover.getYPosMove();
            endXPos += areaMover.getXPosMove();
            endYPos += areaMover.getYPosMove();
            areaMover.initPosMove();
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

    public void makeBorderEffect(CanvasPixelInfo canvasPixelInfo) {
        int i = startXPos;

        while(i != endXPos) {
            if (i % 12 > 5) {
                if(!isOverCanvas(i + areaMover.getXPosMove(), startYPos + areaMover.getYPosMove(),
                        canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight()))
                {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (startYPos + areaMover.getYPosMove()) + (i + areaMover.getXPosMove()),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (startYPos + areaMover.getYPosMove()) + (i + areaMover.getXPosMove())]));
                }
                if(!isOverCanvas(i + areaMover.getXPosMove(), endYPos + areaMover.getYPosMove(),
                        canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight()))
                {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (endYPos + areaMover.getYPosMove()) + (i + areaMover.getXPosMove()),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (endYPos + areaMover.getYPosMove()) + (i + areaMover.getXPosMove())]));
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
                if(!isOverCanvas(startXPos + areaMover.getXPosMove(), i + areaMover.getYPosMove(), canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (i + areaMover.getYPosMove()) + (startXPos + areaMover.getXPosMove()),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (i + areaMover.getYPosMove()) + (startXPos + areaMover.getXPosMove())]));
                }
                if(!isOverCanvas(endXPos + areaMover.getXPosMove(), i + areaMover.getYPosMove(), canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (i + areaMover.getYPosMove()) + (endXPos + areaMover.getXPosMove()),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (i + areaMover.getYPosMove()) + (endXPos + areaMover.getXPosMove())]));
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
        areaMover.pressCtrl(value);
    }

    private boolean isInArea(int xPos, int yPos) {
        return (xPos > startXPos && xPos < endXPos && yPos > startYPos && yPos < endYPos);
    }

}
