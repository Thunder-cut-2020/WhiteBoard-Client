/*
 * ChangedPixelUnitData.java
 * Author : Cwhist
 * Created Date : 2020-01-30
 */
package com.thunder_cut.graphics.restorer;

public class ChangedPixelUnitData {
    public final int xPos;
    public final int yPos;
    public final int prevColor;
    public final int changedColor;

    public ChangedPixelUnitData(int xPos, int yPos, int prevColor, int changedColor) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.prevColor = prevColor;
        this.changedColor = changedColor;
    }
}
