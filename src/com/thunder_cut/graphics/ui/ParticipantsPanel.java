/*
 * ParticipantsPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantsPanel {
    private final int DEFAULT_GAP = 10;
    private JPanel participantsPanel;

    private List<JPanel> participants;

    public ParticipantsPanel() {
        participants = new ArrayList<>();

        participantsPanel = new JPanel();
        participantsPanel.setBackground(Color.BLACK);
        participantsPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP));
        participantsPanel.setLayout(new BoxLayout(participantsPanel, BoxLayout.Y_AXIS));

        makePanel();
    }

    private void makePanel() {
        JPanel newPanel = new JPanel();
        newPanel.setBackground(Color.DARK_GRAY);
        newPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, DEFAULT_GAP));

        newPanel.setMaximumSize(new Dimension(480, 270));
        newPanel.setPreferredSize(new Dimension(480, 270));

        participants.add(newPanel);

        participantsPanel.add(newPanel);
        participantsPanel.revalidate();
        participantsPanel.repaint();
    }

    public void drawImage(int srcID, byte[] imageData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        while (srcID > participants.size()) {
            makePanel();
        }
        try {
            BufferedImage bi = ImageIO.read(byteArrayInputStream);
            JPanel target = participants.get(srcID);
            target.getGraphics()
                    .drawImage(bi, 5, 5, target.getWidth() - DEFAULT_GAP, target.getHeight() - DEFAULT_GAP, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JPanel getParticipantsPanel() {
        return participantsPanel;
    }
}
