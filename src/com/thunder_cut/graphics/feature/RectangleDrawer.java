/*
 * RectangleDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-19
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class RectangleDrawer extends ShapeDrawer{
    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        if(isShapedLine()) {
            return;
        }

        boolean isPlusX = (startXPos < endXPos);
        boolean isPlusY = (startYPos < endYPos);
        int controlEndXPos = isPlusX ? 1 : -1;
        int controlEndYPos = isPlusY ? 1 : -1;
        currentX = startXPos;
        currentY = startYPos;

        while(currentX != endXPos + controlEndXPos) {
            while(currentY != endYPos + controlEndYPos) {
                if((currentX == startXPos) || (currentX == endXPos) ||
                        (currentY == startYPos) || (currentY == endYPos)) {
                    setPixels(canvasPixelInfo, currentX, currentY, color);
                }

                controlPosition(false, isPlusY);
            }
            currentY = startYPos;
            controlPosition(true, isPlusX);
        }
    }
}
