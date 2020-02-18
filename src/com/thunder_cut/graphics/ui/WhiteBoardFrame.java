/*
 * WhiteBoardFrame.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui;

import com.thunder_cut.graphics.ui.drawing.DrawingPanel;
import com.thunder_cut.graphics.ui.keys.HotKeyExecutor;
import com.thunder_cut.netio.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class WhiteBoardFrame {

    private static final Dimension frameSize = new Dimension(1600, 900);
    private static final double splitWeight = 0.8;
    private static final int scrollSpeed = 16;

    private JFrame mainFrame;

    private DrawingPanel drawingPanel;

    private JSplitPane split;

    private JScrollPane scrollPane;
    private ParticipantsPanel participantsPanel;

    public WhiteBoardFrame(){
        initializeComponents();

        addMenuBar();

        createView();

        HotKeyExecutor.initialize();

    }

    private void initializeComponents(){
        mainFrame = new JFrame("화이트 보드");
        mainFrame.setSize(frameSize);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingPanel = new DrawingPanel();

        split = new JSplitPane();

        participantsPanel = new ParticipantsPanel();
        scrollPane = new JScrollPane(participantsPanel.getParticipantsPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        Connection.addDrawImage(participantsPanel::drawImage);

        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                drawingPanel.notifyFrameMoved();
            }
        });
    }

    private void createView(){

        split.setSize(frameSize);
        split.setDividerLocation(splitWeight);

        split.setLeftComponent(drawingPanel.getDrawingPanel());
        split.setRightComponent(scrollPane);

        mainFrame.add(split);

        mainFrame.setVisible(true);

        drawingPanel.createImageBuffer();

    }


    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("파일");

        JMenuItem makeNewFileMenuItem = new JMenuItem("새로 만들기");
        JMenuItem openFileMenuItem = new JMenuItem("열기");
        JMenuItem saveFileMenuItem = new JMenuItem("저장");
        JMenuItem saveOtherNameMenuItem = new JMenuItem("다른 이름으로 저장");
        JMenuItem exitMenuItem = new JMenuItem("끝내기");

        exitMenuItem.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(mainFrame, "종료하시겠습니까?",
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

        JMenu connectMenu = new JMenu("연결");
        JMenuItem createConnectionMenuItem = new JMenuItem("연결");

        createConnectionMenuItem.addActionListener((actionEvent)->{
            showConnectionDialog();
        });

        JMenuItem nicknameMenuItem = new JMenuItem("닉네임 설정");
        nicknameMenuItem.addActionListener(e -> {
            String nickname = JOptionPane.showInputDialog(mainFrame,"Nickname : ","Nickname",JOptionPane.PLAIN_MESSAGE);
            if(!(Objects.isNull(nickname) || nickname.equals(""))){
                Connection.setNickname(nickname);
            }
        });

        connectMenu.add(createConnectionMenuItem);
        connectMenu.add(nicknameMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(connectMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    private void showConnectionDialog(){

        String serverIP = "";
        int serverPort = 0;

        JTextField IPInput = new JTextField();
        JTextField portInput = new JTextField();

        JComponent[] components = new JComponent[] {

                new JLabel("IP : "), IPInput,
                new JLabel("Port : "), portInput
        };

        int result = JOptionPane.showConfirmDialog(mainFrame,components, "연결 설정", JOptionPane.OK_CANCEL_OPTION);

        if(result == JOptionPane.OK_OPTION){

            serverIP = IPInput.getText();
            if(serverIP.equals("") || portInput.getText().equals("")){
                JOptionPane.showMessageDialog(mainFrame,"올바르지 않은 입력입니다.","Error!",JOptionPane.WARNING_MESSAGE);
                return;
            }
            try{
                serverPort = Integer.parseInt(portInput.getText());
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(mainFrame,"올바르지 않은 입력입니다.","Error!",JOptionPane.WARNING_MESSAGE);
                return;
            }

            Connection.createConnection(serverIP,serverPort);
        }

    }
}
