/*
 * DrawingModeHandler.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.controller;

import com.thunder_cut.graphics.feature.AreaSelector;
import com.thunder_cut.graphics.feature.Brush;
import com.thunder_cut.graphics.feature.DrawingFeature;
import com.thunder_cut.graphics.feature.Eraser;
import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.util.EnumMap;

import static java.util.Objects.nonNull;

public class DrawingModeHandler {
    private DrawingMode selectedDrawingMode;
    private EnumMap<DrawingMode, DrawingFeature> drawingFeatures;
    private Color color;

    public DrawingModeHandler() {
        selectedDrawingMode = DrawingMode.BRUSH;
        color = new Color(Color.BLACK.getRGB());

        drawingFeatures = new EnumMap<>(DrawingMode.class);
        drawingFeatures.put(DrawingMode.BRUSH, new Brush());
        drawingFeatures.put(DrawingMode.ERASER, new Eraser());
        drawingFeatures.put(DrawingMode.AREA_SELECTOR, new AreaSelector());
    }

    public void drawingModeChanged(DrawingMode mode) {
        if (mode == DrawingMode.COLOR_CHOOSER) {
            Color selectedColor = JColorChooser.showDialog(null, "Color", Color.GRAY);
            color = nonNull(selectedColor) ? selectedColor : color;
        } else if (mode == DrawingMode.SIZE_CHOOSER) {
            int size = Integer.parseInt(JOptionPane.showInputDialog("size"));
            ((Brush) drawingFeatures.get(DrawingMode.BRUSH)).setSize(size);
            ((Eraser) drawingFeatures.get(DrawingMode.ERASER)).setSize(size);
        } else {
            selectedDrawingMode = mode;
        }
    }

    public void handleMouseEvent(MouseData mouseData, CanvasPixelInfo canvasPixelInfo) {
        drawingFeatures.get(selectedDrawingMode).process(mouseData, canvasPixelInfo, color);
    }
}
