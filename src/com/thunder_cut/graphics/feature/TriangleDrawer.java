/*
 * TriangleDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-20
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class TriangleDrawer extends LineDrawer{
    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        if (isShapedLine()) {
            return;
        }

        int leftEndXPos = Math.min(startXPos, endXPos);
        int rightEndXPos = Math.max(startXPos, endXPos);
        int underYPos = Math.max(startYPos, endYPos);

        startXPos = (startXPos + endXPos) / 2;
        startYPos = Math.min(startYPos, endYPos);
        endXPos = leftEndXPos;
        endYPos = underYPos;
        super.drawShape(canvasPixelInfo, color);

        endXPos = rightEndXPos;
        super.drawShape(canvasPixelInfo, color);

        startYPos = underYPos;
        startXPos = leftEndXPos;
        endXPos = rightEndXPos;
        super.drawShape(canvasPixelInfo, color);
    }
}