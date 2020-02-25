/*
 * MainClass.java
 * Author : 나상혁
 * Created Date : 2020-01-07
 */
package com.thunder_cut;

import com.thunder_cut.graphics.ui.WhiteBoardFrame;
import com.thunder_cut.netio.Connection;

public class MainClass {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        //TODO : Prepare connection
        //TODO : Create UI Component
        Connection.initialize();
        new WhiteBoardFrame();
    }
}
