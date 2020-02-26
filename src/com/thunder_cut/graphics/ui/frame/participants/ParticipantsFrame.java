/*
 * ParticipantsFrame.java
 * Author : 나상혁
 * Created Date : 2020-02-18
 */
package com.thunder_cut.graphics.ui.frame.participants;

import com.thunder_cut.netio.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ParticipantsFrame {

    private static final int scrollSpeed = 16;

    private static final Dimension FRAME_SIZE = new Dimension(530, 720);

    private JScrollPane scrollPane;
    private ParticipantsPanel participantsPanel;

    private JFrame frame;

    private int framePrevX;
    private int framePrevY;

    public ParticipantsFrame(int initXPos, int initYPos) {

        frame = new JFrame("Participants");
        frame.setSize(FRAME_SIZE);
        frame.setLocation(initXPos, initYPos);

        participantsPanel = new ParticipantsPanel();

        scrollPane = new JScrollPane(participantsPanel.getParticipantsPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        frame.add(scrollPane);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                participantsPanel.getParticipantsPanel().repaint();
                participantsPanel.redrawParticipantPanel();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                if (!(framePrevX == e.getComponent().getX() && framePrevY == e.getComponent().getY())) {
                    framePrevX = e.getComponent().getX();
                    framePrevY = e.getComponent().getY();
                    participantsPanel.redrawParticipantPanel();
                }
            }
        });

        Connection.addDrawImage(participantsPanel::drawImage);
    }

    public void setVisible(boolean visibility) {
        frame.setVisible(visibility);
    }

    public ParticipantsPanel getParticipantsPanel() {
        return participantsPanel;
    }
}
