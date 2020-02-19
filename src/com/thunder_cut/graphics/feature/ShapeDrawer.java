/*
 * ShapeDrawer.java
 * Author : susemeeee
 * Created Date : 2020-02-17
 */
package com.thunder_cut.graphics.feature;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.awt.*;

public abstract class ShapeDrawer extends Brush {
    public abstract void drawShape(CanvasPixelInfo canvasPixelInfo, Color color);
    public abstract void makeBorderEffect(CanvasPixelInfo canvasPixelInfo);

    protected Color toInvertColor(int data) {
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
}
