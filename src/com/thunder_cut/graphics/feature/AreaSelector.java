/*
 * AreaSelector.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class AreaSelector implements DrawingFeature {
    SelectedAreaEditor selectedAreaEditor;

    private boolean moveMode;

    public AreaSelector() {
        moveMode = false;

        selectedAreaEditor = new SelectedAreaEditor();
        selectedAreaEditor.initPosMove();
    }

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(selectedAreaEditor.isInArea(xPos, yPos)) {
            moveMode = true;

            selectedAreaEditor.setPrevMousePos(xPos, yPos);
            selectedAreaEditor.copySelectedArea(canvasPixelInfo);
        }
        else {
            selectedAreaEditor.setStartPos(xPos, yPos);
        }
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        canvasPixelInfo.initEffectPixels();

        if(moveMode) {
            selectedAreaEditor.setPosMove(xPos, yPos);
            selectedAreaEditor.moveSelectedArea(canvasPixelInfo);
        }
        else {
            if (!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                selectedAreaEditor.setEndPos(xPos, yPos);
            }
            makeBorderEffect(canvasPixelInfo);
        }

    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(moveMode) {
            moveMode = false;

            selectedAreaEditor.setChangedAreaPos();
            selectedAreaEditor.pasteMovedSelectedArea(canvasPixelInfo);
            selectedAreaEditor.initPosMove();
        }
        else {
            if (!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                selectedAreaEditor.setEndPos(xPos, yPos);
            }
            selectedAreaEditor.updateXY();
        }
        canvasPixelInfo.initEffectPixels();
        makeBorderEffect(canvasPixelInfo);
    }

    @Override
    public void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    public void makeBorderEffect(CanvasPixelInfo canvasPixelInfo) {
        int startXPos = selectedAreaEditor.getStartXPos();
        int startYPos = selectedAreaEditor.getStartYPos();
        int endXPos = selectedAreaEditor.getEndXPos();
        int endYPos = selectedAreaEditor.getEndYPos();

        int xPosMove = selectedAreaEditor.getXPosMove();
        int yPosMove = selectedAreaEditor.getYPosMove();

        int i = startXPos;

        while(i != endXPos) {
            if (i % 12 > 5) {
                if(!isOverCanvas(i + xPosMove, startYPos + yPosMove, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                    canvasPixelInfo.setEffectPixel(canvasPixelInfo.getWidth() * (startYPos + yPosMove) + (i + xPosMove),
                            toInvertColor(canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * (startYPos+ yPosMove) + (i +xPosMove)]));
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

    // For Ctrl+C
    public void copy(CanvasPixelInfo canvasPixelInfo) {
        selectedAreaEditor.copySelectedArea(canvasPixelInfo);
    }

    // For Ctrl+V
    public void paste(CanvasPixelInfo canvasPixelInfo) {
        selectedAreaEditor.pasteSelectedArea(canvasPixelInfo);
    }

    public void setIsCtrlPressed(boolean value) {
        selectedAreaEditor.setIsCtrlPressed(value);
    }

}
