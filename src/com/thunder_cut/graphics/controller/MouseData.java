/*
 * MouseData.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.controller;

public class MouseData {
    public final MouseStatus status;
    public final int xPos;
    public final int yPos;

    public MouseData(MouseStatus status, int xPos, int yPos) {
        this.status = status;
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
