/*
 * WorkUnitData.java
 * Author : Cwhist
 * Created Date : 2020-01-30
 */
package com.thunder_cut.graphics.restorer;

import java.util.ArrayList;

public class WorkUnitData {
    private ArrayList<ChangedPixelUnitData> workUnitData;

    public WorkUnitData() {
        workUnitData = new ArrayList<>();
    }

    public void addData(ChangedPixelUnitData changedPixelUnitData) {
        workUnitData.add(changedPixelUnitData);
    }

    public int getSize() {
        return workUnitData.size();
    }

    public ChangedPixelUnitData getPixelUnitData(int index) {
        return workUnitData.get(index);
    }
}
