/*
 * WorkDataRecorder.java
 * Author : Cwhist
 * Created Date : 2020-02-01
 */
package com.thunder_cut.graphics.restorer;

import com.thunder_cut.graphics.controller.MouseStatus;
import com.thunder_cut.graphics.ui.drawing.CanvasPixelInfo;

import java.util.ArrayList;
import java.util.List;

public class WorkDataRecorder {
    private static final int DEFAULT_RECORDABLE_SIZE = 50;

    private List<WorkUnitData> workDataList;
    private CanvasPixelInfo canvasPixelInfo;

    private int recordableSize;
    private int[] prevPixels;
    private int presentIndex;

    public WorkDataRecorder() {
        recordableSize = DEFAULT_RECORDABLE_SIZE;
        workDataList = new ArrayList<>();
        presentIndex = -1;
    }

    public void handleRecordWorkData(MouseStatus mouseStatus) {
        if(mouseStatus == MouseStatus.PRESSED) {
            savePrevPixelInfo();
        }
        else if(mouseStatus == MouseStatus.RELEASED) {
            createWorkUnitData();
        }
    }

    private void savePrevPixelInfo() {
        prevPixels = new int[canvasPixelInfo.getWidth()*canvasPixelInfo.getHeight()];

        for(int i=0; i<canvasPixelInfo.getPixels().length; i++) {
            prevPixels[i] = canvasPixelInfo.getPixels()[i];
        }
    }

    private void createWorkUnitData() {
        WorkUnitData workUnitData = new WorkUnitData();
        boolean isChanged = false;

        for(int i=0; i<canvasPixelInfo.getPixels().length; i++) {
            if (prevPixels[i] != canvasPixelInfo.getPixels()[i]) {
                workUnitData.addData(new ChangedPixelUnitData
                        (i%canvasPixelInfo.getWidth(),i/canvasPixelInfo.getWidth(), prevPixels[i], canvasPixelInfo.getPixels()[i]));
                isChanged = true;
            }
        }

        if(isChanged) {
            addWorkUnitData(workUnitData);
        }
    }

    public void addWorkUnitData(WorkUnitData workUnitData) {
        if(presentIndex+1 == recordableSize) {
            workDataList.remove(0);
        }
        else {
            presentIndex++;
        }

        if(isRearWorkExist()) {
            for(int i=workDataList.size()-1; i>=presentIndex; i--) {
                workDataList.remove(i);
            }
        }

        workDataList.add(workUnitData);
    }

    public boolean isRearWorkExist() {
        return presentIndex <= (workDataList.size() - 1);
    }

    public void increasePresentIndex() {
        presentIndex += 1;
    }

    public void decreasePresentIndex() {
        presentIndex -= 1;
    }

    public void setCanvasPixelInfo(CanvasPixelInfo canvasPixelInfo) {
        this.canvasPixelInfo = canvasPixelInfo;
    }

    public CanvasPixelInfo getCanvasPixelInfo() {
        return canvasPixelInfo;
    }

    public WorkUnitData getCurrentWorkUnitData() {
        return workDataList.get(presentIndex);
    }

    public int getPresentIndex() {
        return presentIndex;
    }

}