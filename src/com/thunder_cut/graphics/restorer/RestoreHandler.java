/*
 * CanvasRestorer.java
 * Author : Cwhist
 * Created Date : 2020-01-30
 */
package com.thunder_cut.graphics.restorer;

import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;
import com.thunder_cut.graphics.ui.keys.HotKey;

import java.awt.*;

public class RestoreHandler {
    private WorkDataRecorder workDataRecorder;
    private Runnable drawCanvas;

    public RestoreHandler(Runnable drawCanvas) {
        this.drawCanvas = drawCanvas;
        HotKey.UNDO.setAction(()->{
            undo(workDataRecorder.getCanvasPixelInfo());
        });
        HotKey.REDO.setAction(()->{
            redo(workDataRecorder.getCanvasPixelInfo());
        });
    }

    public void handleRestoreEvent(RestoreMode mode) {
        if (mode == RestoreMode.UNDO) {
            undo(workDataRecorder.getCanvasPixelInfo());
        }
        else {
            redo(workDataRecorder.getCanvasPixelInfo());
        }
    }

    public void undo(CanvasPixelInfo canvasPixelInfo) {
        if(workDataRecorder.getPresentIndex() <= -1) {
            return;
        }

        WorkUnitData workUnitData = workDataRecorder.getCurrentWorkUnitData();

        canvasPixelInfo.initEffectPixels();
        for (int i = 0; i < workUnitData.getSize(); i++) {
            ChangedPixelUnitData pixelUnitData = workUnitData.getPixelUnitData(i);
            canvasPixelInfo.setPixel(canvasPixelInfo.getWidth() * pixelUnitData.yPos + pixelUnitData.xPos, new Color(pixelUnitData.prevColor));
        }

        workDataRecorder.decreasePresentIndex();

        drawCanvas.run();
    }

    public void redo(CanvasPixelInfo canvasPixelInfo) {
        workDataRecorder.increasePresentIndex();

        if(!workDataRecorder.isRearWorkExist()) {
            workDataRecorder.decreasePresentIndex();
            return;
        }
        WorkUnitData workUnitData = workDataRecorder.getCurrentWorkUnitData();

        canvasPixelInfo.initEffectPixels();
        for(int i=0; i<workUnitData.getSize(); i++) {
            ChangedPixelUnitData pixelUnitData = workUnitData.getPixelUnitData(i);
            canvasPixelInfo.setPixel(canvasPixelInfo.getWidth() * pixelUnitData.yPos + pixelUnitData.xPos, new Color(pixelUnitData.changedColor));
        }

        drawCanvas.run();

    }

    public void setWorkDataRecorder(WorkDataRecorder workDataRecorder) {
        this.workDataRecorder = workDataRecorder;
    }
}