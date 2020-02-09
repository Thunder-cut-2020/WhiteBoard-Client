/*
 * DrawingPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui.drawing;

import com.thunder_cut.graphics.controller.DrawingModeHandler;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel {

    private static final int DEFAULT_GAP = 20;

    private JPanel drawingPanel;
    private ToolPanel toolPanel;
    private DrawingCanvas drawingCanvas;

    private DrawingModeHandler drawingModeHandler;

    public DrawingPanel() {

        initializeComponents();
        createView();
    }

    private void initializeComponents() {
        drawingPanel = new JPanel();
        toolPanel = new ToolPanel();
        drawingCanvas = new DrawingCanvas();

        drawingModeHandler = new DrawingModeHandler();

        drawingPanel.setLayout(new BorderLayout(DEFAULT_GAP, DEFAULT_GAP));
    }

    private void createView() {
        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.add(toolPanel.getToolPanel(), BorderLayout.NORTH);
        drawingPanel.add(drawingCanvas.getCanvas(), BorderLayout.CENTER);

        drawingPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP));

        drawingCanvas.addMouseHandler(drawingModeHandler::handleMouseEvent);
        toolPanel.addDrawModeHandler(drawingModeHandler::drawingModeChanged);
    }

    public JPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void createImageBuffer() {
        drawingCanvas.createPixelInfo();
    }

}
