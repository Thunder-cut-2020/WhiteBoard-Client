/*
 * CircleDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-21
 */
package com.thunder_cut.graphics.feature.shape;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public class CircleDrawer extends ShapeDrawer{
    @Override
    public void drawShape(CanvasPixelInfo canvasPixelInfo, Color color) {
        if(isShapedLine()) {
            return;
        }
        int longAxis = (startXPos < endXPos) ? (endXPos - startXPos) / 2 : (startXPos - endXPos) / 2;
        int shortAxis = (startYPos < endYPos) ? (endYPos - startYPos) / 2 : (startYPos - endYPos) / 2;
        Point centerPoint = new Point((startXPos + endXPos) / 2, (startYPos + endYPos) / 2);
        double degree = 0;

        while(degree <= 90) {
            currentX = (int)(centerPoint.x + longAxis * Math.cos(degree));
            currentY = (int)(centerPoint.y + shortAxis * Math.sin(degree));

            setPixels(canvasPixelInfo, currentX, currentY, color);

            degree += 0.0001;
        }
    }
}