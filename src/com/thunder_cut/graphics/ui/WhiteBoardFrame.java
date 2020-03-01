/*
 * WhiteBoardFrame.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui;

import com.thunder_cut.graphics.ui.drawing.DrawingPanel;
import com.thunder_cut.graphics.ui.frame.chat.ChatFrame;
import com.thunder_cut.graphics.ui.frame.participants.ParticipantsFrame;
import com.thunder_cut.graphics.ui.keys.HotKeyExecutor;
import com.thunder_cut.netio.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class WhiteBoardFrame {

    private static final Dimension MAIN_FRAME_SIZE = new Dimension(960, 720);
    private static final int MAIN_FRAME_X_POS = 360;

    private static final int FRAME_GAP = -10;

    private Connection connection;

    private JFrame mainFrame;

    private ParticipantsFrame participantsFrame;
    private ChatFrame chatFrame;

    private DrawingPanel drawingPanel;

    private int framePrevX;
    private int framePrevY;

    public WhiteBoardFrame() {
        initializeComponents();

        addMenuBar();

        createView();

        HotKeyExecutor.initialize();

    }

    private void initializeComponents() {
        connection = new Connection();
        mainFrame = new JFrame("화이트 보드");
        mainFrame.setSize(MAIN_FRAME_SIZE);
        mainFrame.setLocation(MAIN_FRAME_X_POS + FRAME_GAP,0);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatFrame = new ChatFrame(0,0);
        participantsFrame = new ParticipantsFrame(MAIN_FRAME_SIZE.width+ MAIN_FRAME_X_POS + FRAME_GAP*2,0);

        drawingPanel = new DrawingPanel();

    }

    private void createView() {

        mainFrame.add(drawingPanel.getDrawingPanel());

        mainFrame.setVisible(true);

        participantsFrame.setVisible(true);
        chatFrame.setVisible(true);

        drawingPanel.createImageBuffer();

        drawingPanel.addEventListeners();

        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                if(!(framePrevX == e.getComponent().getX() && framePrevY == e.getComponent().getY())){
                    framePrevX = e.getComponent().getX();
                    framePrevY = e.getComponent().getY();
                    drawingPanel.notifyFrameMoved();
                }

            }
        });
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
                    mainFrame.getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
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

        createConnectionMenuItem.addActionListener((actionEvent) -> {
            showConnectionDialog();
        });

        JMenuItem destroyConnectionMenuItem = new JMenuItem("연결 해제");
        destroyConnectionMenuItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(mainFrame, "연결을 해제하시겠습니까?", mainFrame.getTitle(), JOptionPane.YES_NO_OPTION) == 0) {
                connection.disconnect();
            }
        });

        JMenuItem nicknameMenuItem = new JMenuItem("닉네임 설정");
        nicknameMenuItem.addActionListener(e -> {
            String nickname = JOptionPane.showInputDialog(mainFrame,"Nickname : ",
                    "Nickname",JOptionPane.PLAIN_MESSAGE);
            if(!(Objects.isNull(nickname) || nickname.equals(""))) {
                connection.setNickName(nickname);
            }
        });

        connectMenu.add(createConnectionMenuItem);
        connectMenu.add(destroyConnectionMenuItem);
        connectMenu.add(nicknameMenuItem);

        JMenu windowMenu = new JMenu("창");
        JMenuItem participantsWindowMenuItem = new JMenuItem("Participants");

        participantsWindowMenuItem.addActionListener(e -> {
            participantsFrame.setVisible(true);
        });

        JMenuItem chatWindowMenuItem = new JMenuItem("Chatting");
        chatWindowMenuItem.addActionListener(e -> {
            chatFrame.setVisible(true);
        });

        windowMenu.add(participantsWindowMenuItem);
        windowMenu.add(chatWindowMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(connectMenu);
        menuBar.add(windowMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    private void showConnectionDialog() {

        String serverAddress = "";
        int serverPort = 0;

        JTextField IPInput = new JTextField();
        JTextField portInput = new JTextField();

        JComponent[] components = new JComponent[]{

                new JLabel("Address : "), IPInput,
                new JLabel("Port : "), portInput
        };

        int result = JOptionPane.showConfirmDialog(mainFrame, components, "연결 설정", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            serverAddress = IPInput.getText();
            if (serverAddress.equals("") || portInput.getText().equals("")) {
                JOptionPane.showMessageDialog(mainFrame, "올바르지 않은 입력입니다.", "Error!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                serverPort = Integer.parseInt(portInput.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "올바르지 않은 입력입니다.", "Error!", JOptionPane.WARNING_MESSAGE);
                return;
            }

            connection.connect(serverAddress,serverPort);
        }

    }
}
