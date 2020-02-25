/*
 * PaintFiller.java
 * Author : susemeeee
 * Created Date : 2020-02-25
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;
import java.util.*;

public class PaintFiller implements DrawingFeature{
    private int originalColor;

    @Override
    public void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        originalColor = canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * yPos + xPos];
        fill(xPos, yPos, canvasPixelInfo, color);
    }

    @Override
    public void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    @Override
    public void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    @Override
    public void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {

    }

    private void fill(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color) {
        Queue<Point> selectedPositions = new LinkedList<>();
        selectedPositions.offer(new Point(xPos, yPos));

        while(selectedPositions.size() != 0) {
            Point currentPosition = selectedPositions.poll();
            canvasPixelInfo.setPixel(canvasPixelInfo.getWidth() * currentPosition.y + currentPosition.x, color);

            if(isCorrectPosition(currentPosition.x, currentPosition.y - 1, canvasPixelInfo, color, selectedPositions)) {
                selectedPositions.offer(new Point(currentPosition.x, currentPosition.y - 1));
            }
            if(isCorrectPosition(currentPosition.x - 1, currentPosition.y, canvasPixelInfo, color, selectedPositions)) {
                selectedPositions.offer(new Point(currentPosition.x - 1, currentPosition.y));
            }
            if(isCorrectPosition(currentPosition.x + 1, currentPosition.y, canvasPixelInfo, color, selectedPositions)) {
                selectedPositions.offer(new Point(currentPosition.x + 1, currentPosition.y));
            }
            if(isCorrectPosition(currentPosition.x, currentPosition.y + 1, canvasPixelInfo, color, selectedPositions)) {
                selectedPositions.offer(new Point(currentPosition.x, currentPosition.y + 1));
            }
        }
    }

    private boolean isCorrectPosition(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo,
                                      Color color, Queue<Point> selectedPositions) {
        return !isOverCanvas(xPos, yPos, canvasPixelInfo.getWidth(), canvasPixelInfo.getHeight()) &&
                (canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * yPos + xPos] != color.getRGB()) &&
                (canvasPixelInfo.getPixels()[canvasPixelInfo.getWidth() * yPos + xPos] == originalColor) &&
                !selectedPositions.contains(new Point(xPos, yPos));
    }
}
