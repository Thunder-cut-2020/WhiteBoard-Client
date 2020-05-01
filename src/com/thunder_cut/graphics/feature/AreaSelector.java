/*
 * AreaSelector.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.clip.ClipBoard;
import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;
import com.thunder_cut.graphics.ui.keys.HotKey;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;


public class AreaSelector implements DrawingFeature {
    SelectedAreaEditor selectedAreaEditor;
    private CanvasPixelInfo lastPixelInfo;

    private boolean moveMode;

    public AreaSelector() {
        moveMode = false;

        selectedAreaEditor = new SelectedAreaEditor();
        selectedAreaEditor.initPosMove();

        //TODO Separate AreaSelector and SelectedAreaEditor
        HotKey.COPY_SELECTION.setAction(this::copyToClipboard);
        HotKey.PASTE.setAction(this::pasteFromClipboard);
        HotKey.SELECT_ALL.setAction(this::selectAll);
        HotKey.DELETE_SELECTION.setAction(this::deleteSelection);
        HotKey.CUT_SELECTION.setAction(this::cutSelection);
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
        lastPixelInfo = canvasPixelInfo;
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
            selectedAreaEditor.updateXY();
        }
        else {
            if (!isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight())) {
                selectedAreaEditor.setEndPos(xPos, yPos);
                selectedAreaEditor.updateXY();
                selectedAreaEditor.copySelectedArea(canvasPixelInfo);
            }
        }
        lastPixelInfo = canvasPixelInfo;
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


    public void copyToClipboard(){
        ClipBoard.copyImageToClipBoard(selectedAreaEditor.imageExtractor.image);
    }

    // For Ctrl+V
    public void pasteFromClipboard() {
        //TODO fix paste function
        BufferedImage bi = ClipBoard.getBufferedImageFromClipBoard();
        if(Objects.nonNull(bi)){
            selectedAreaEditor.pasteFromImage(CanvasPixelInfo.imageToPixelInfo(bi),lastPixelInfo);
            makeBorderEffect(lastPixelInfo);
            moveMode = true;
        }
    }

    public void selectAll(){
        moveMode = true;
        selectedAreaEditor.setStartPos(0, 0);
        selectedAreaEditor.setEndPos(lastPixelInfo.getWidth()-1, lastPixelInfo.getHeight()-1);
        selectedAreaEditor.copySelectedArea(lastPixelInfo);
        lastPixelInfo.initEffectPixels();
        makeBorderEffect(lastPixelInfo);
    }

    public void deleteSelection(){
        selectedAreaEditor.setPosMove(lastPixelInfo.getWidth(), lastPixelInfo.getHeight());
        selectedAreaEditor.moveSelectedArea(lastPixelInfo);
        selectedAreaEditor.setChangedAreaPos();
        selectedAreaEditor.pasteMovedSelectedArea(lastPixelInfo);
        selectedAreaEditor.initPosMove();
        lastPixelInfo.initEffectPixels();
        moveMode = false;
    }

    public void cutSelection(){
        copyToClipboard();
        deleteSelection();
    }

    public void setIsCtrlPressed(boolean value) {
        selectedAreaEditor.setIsCtrlPressed(value);
    }

}
