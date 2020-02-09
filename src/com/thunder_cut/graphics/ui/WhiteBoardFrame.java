/*
 * WhiteBoardFrame.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui;

import com.thunder_cut.graphics.ui.drawing.DrawingPanel;

import javax.swing.*;
import java.awt.*;

public class WhiteBoardFrame {

    private static final Dimension frameSize = new Dimension(1600, 900);
    private static final double splitWeight = 0.8;
    private static final int scrollSpeed = 16;

    private JFrame mainFrame;

    private DrawingPanel drawingPanel;

    private JSplitPane split;

    private JScrollPane scrollPane;
    private ParticipantsPanel participantsPanel;

    public WhiteBoardFrame() {
        initializeComponents();

        addMenuBar();

        createView();
    }

    private void initializeComponents() {
        mainFrame = new JFrame("화이트 보드");
        mainFrame.setSize(frameSize);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingPanel = new DrawingPanel();

        split = new JSplitPane();

        participantsPanel = new ParticipantsPanel();
        scrollPane = new JScrollPane(participantsPanel.getParticipantsPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

    }

    private void createView() {

        split.setSize(frameSize);
        split.setDividerLocation(splitWeight);

        split.setLeftComponent(drawingPanel.getDrawingPanel());
        split.setRightComponent(scrollPane);

        mainFrame.add(split);

        mainFrame.setVisible(true);

        drawingPanel.createImageBuffer();
    }


    private void addMenuBar() {
        UIManager.put("MenuBar.background", Color.DARK_GRAY);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("MenuItem.background", Color.DARK_GRAY);
        UIManager.put("MenuItem.foreground", Color.WHITE);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("파일");

        JMenuItem makeNewFileMenuItem = new JMenuItem("새로 만들기");
        JMenuItem openFileMenuItem = new JMenuItem("열기");
        JMenuItem saveFileMenuItem = new JMenuItem("저장");
        JMenuItem saveOtherNameMenuItem = new JMenuItem("다른 이름으로 저장");
        JMenuItem exitMenuItem = new JMenuItem("끝내기");

        exitMenuItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(mainFrame, "종료하시겠습니까?",
                    mainFrame.getTitle(), JOptionPane.YES_NO_OPTION) == 0)
                System.exit(0);
        });

        fileMenu.add(makeNewFileMenuItem);
        fileMenu.add(openFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(saveOtherNameMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);


        JMenu editMenu = new JMenu("편집");
        JMenuItem undoMenuItem = new JMenuItem("Undo");
        JMenuItem redoMenuItem = new JMenuItem("Redo");

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        mainFrame.setJMenuBar(menuBar);
    }
}
