/*
 * CommunicationPanel.java
 * Author: Seokjin Yoon
 * Created Date: 2020-02-26
 */

package com.thunder_cut.graphics.ui.frame;

import javax.swing.*;
import java.awt.*;

public class CommunicationPanel {
    private JPanel panel;

    private JScrollPane participantsPanel;
    private JPanel chatPanel;

    public CommunicationPanel(JScrollPane participantsPanel, JPanel chatPanel) {
        panel = new JPanel();
        this.participantsPanel = participantsPanel;
        this.chatPanel = chatPanel;

        panel.setLayout(new BorderLayout());
        panel.add(participantsPanel, BorderLayout.CENTER);
        panel.add(chatPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
