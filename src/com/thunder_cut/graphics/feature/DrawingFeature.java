/*
 * DrawingFeature.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.controller.MouseData;
import com.thunder_cut.graphics.controller.MouseStatus;
import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public interface DrawingFeature {
    default void process(MouseData mouseData, CanvasPixelInfo canvasPixelInfo, Color color) {
        if(mouseData.status == MouseStatus.PRESSED) {
            pressed(mouseData.xPos, mouseData.yPos, canvasPixelInfo, color);
        }
        else if(mouseData.status == MouseStatus.DRAGGED) {
            dragged(mouseData.xPos, mouseData.yPos, canvasPixelInfo, color);
        }
        else if(mouseData.status == MouseStatus.RELEASED){
            released(mouseData.xPos, mouseData.yPos, canvasPixelInfo, color);
        }
        else {
            moved(mouseData.xPos, mouseData.yPos, canvasPixelInfo, color);
        }
    }

    default boolean isOverCanvas(int currentX, int currentY, int width, int height) {
        return ((currentX < 0) || (currentX >= width) || (currentY < 0) || (currentY >= height));
    }

    void pressed(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color);
    void dragged(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color);
    void released(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color);
    void moved(int xPos, int yPos, CanvasPixelInfo canvasPixelInfo, Color color);
}