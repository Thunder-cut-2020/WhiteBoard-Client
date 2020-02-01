/*
 * CanvasRestorer.java
 * Author : Cwhist
 * Created Date : 2020-01-30
 */
package com.thunder_cut.graphics.controller;

import com.thunder_cut.graphics.feature.Restorer;

public class RestoreHandler {
    private WorkDataRecorder workDataRecorder;
    private Restorer restorer = new Restorer();

    public void handleRestoreEvent(String mode) {
        if (mode == "UNDO") {
            restorer.undo(workDataRecorder.getWorkDataList(), workDataRecorder.getCanvasPixelInfo());
        }
        else {
            restorer.redo(workDataRecorder.getWorkDataList(), workDataRecorder.getCanvasPixelInfo());
        }
    }

    public void setWorkDataRecorder(WorkDataRecorder workDataRecorder) {
        this.workDataRecorder = workDataRecorder;
    }
}