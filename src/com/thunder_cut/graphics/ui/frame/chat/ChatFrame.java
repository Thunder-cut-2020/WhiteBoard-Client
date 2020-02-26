/*
 * ChatFrame.java
 * Author : 나상혁
 * Created Date : 2020-02-18
 */
package com.thunder_cut.graphics.ui.frame.chat;

import com.thunder_cut.graphics.ui.theme.Theme;
import com.thunder_cut.netio.Connection;

import javax.swing.*;
import java.awt.*;

public class ChatFrame {

    private JFrame frame;

    private JTextArea textArea;
    private JTextField textField;

    private JPanel panel;

    public ChatFrame(int initXPos, int initYPos) {

        frame = new JFrame("Chatting");
        frame.setBounds(initXPos, initYPos, 360, 720);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.CURRENT.background);

        initializeComponent();

        frame.getContentPane().add(panel);
    }

    private void initializeComponent() {

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Theme.CURRENT.secondary);
        textArea.setForeground(Theme.CURRENT.onSecondary);
        Connection.addReceiveMessage((_unused, data) -> {
            textArea.append(new String(data));
        });

        textField = new JTextField();
        textField.setBackground(Theme.CURRENT.secondary);
        textField.setForeground(Theme.CURRENT.onSecondary);
        textField.addActionListener(_unused -> {
            Connection.send(textField.getText() + "\n");
            textField.setText("");
        });

        panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Theme.CURRENT.background);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(textField, BorderLayout.SOUTH);

    }

    public void setVisible(boolean visibility) {
        frame.setVisible(visibility);
    }

    public JPanel getPanel() {
        return panel;
    }
}
