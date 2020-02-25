/*
 * DrawingPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui.drawing;

import com.thunder_cut.graphics.controller.DrawingModeHandler;
import com.thunder_cut.graphics.restorer.RestoreHandler;
import com.thunder_cut.graphics.restorer.WorkDataRecorder;
import com.thunder_cut.graphics.ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel {

    private static final int DEFAULT_GAP = 20;

    private JPanel drawingPanel;
    private ToolPanel toolPanel;
    private DrawingCanvas drawingCanvas;

    private DrawingModeHandler drawingModeHandler;

    private WorkDataRecorder workDataRecorder;
    private RestoreHandler restoreHandler;

    public DrawingPanel(){

        initializeComponents();
        createView();
    }

    private void initializeComponents(){
        drawingPanel = new JPanel();
        toolPanel = new ToolPanel();

        drawingCanvas = new DrawingCanvas();
        drawingModeHandler = new DrawingModeHandler();

        workDataRecorder = new WorkDataRecorder();
        restoreHandler = new RestoreHandler(drawingCanvas::drawCanvas);

        drawingPanel.setLayout(new BorderLayout(DEFAULT_GAP, DEFAULT_GAP));

        toolPanel.addDrawModeHandler(drawingModeHandler::drawingModeChanged);
        drawingCanvas.addMouseHandler(drawingModeHandler::handleMouseEvent);

        toolPanel.addRestoreHandler(restoreHandler::handleRestoreEvent);
        drawingCanvas.addWorkDataRecorder(workDataRecorder::handleRecordWorkData);

        restoreHandler.setWorkDataRecorder(workDataRecorder);
    }

    private void createView(){
        drawingPanel.setBackground(Theme.CURRENT.background);
        drawingPanel.add(toolPanel.getToolPanel(), BorderLayout.NORTH);
        drawingPanel.add(drawingCanvas.getCanvas(), BorderLayout.CENTER);

        drawingPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP));

    }

    public JPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void createImageBuffer(){
        drawingCanvas.createPixelInfo();
        workDataRecorder.setCanvasPixelInfo(drawingCanvas.getCanvasPixelInfo());
    }

    public void addEventListeners(){
        drawingCanvas.addEventListeners();
    }

    public void notifyFrameMoved(){
        drawingCanvas.notifyFrameMoved();
    }

}
