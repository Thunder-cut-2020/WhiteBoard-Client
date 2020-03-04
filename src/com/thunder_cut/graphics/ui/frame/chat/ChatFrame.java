/*
 * ChatFrame.java
 * Author : 나상혁
 * Created Date : 2020-02-18
 */
package com.thunder_cut.graphics.ui.frame.chat;

import com.thunder_cut.graphics.ui.theme.Theme;
import com.thunder_cut.netio.DataType;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class ChatFrame {

    private JFrame frame;

    private JTextArea textArea;
    private JTextField textField;

    private JPanel panel;

    private JScrollPane scrollPane;

    public ChatFrame(int initXPos, int initYPos){

        frame = new JFrame("Chatting");
        frame.setBounds(initXPos,initYPos,360,720);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.CURRENT.background);

        initializeComponent();


        frame.getContentPane().add(panel);
    }

    private void initializeComponent(){

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Theme.CURRENT.secondary);
        textArea.setForeground(Theme.CURRENT.onSecondary);

        DataType.MESSAGE.addOnReceived((indexedName, data) -> {
            textArea.append(indexedName.name + " : " + new String(data,StandardCharsets.UTF_8) + "\n");

            textArea.setCaretPosition(textArea.getDocument().getLength());
        });

        textField = new JTextField();
        textField.setBackground(Theme.CURRENT.secondary);
        textField.setForeground(Theme.CURRENT.onSecondary);

        textField.addActionListener( _unused -> {

            String text = textField.getText();
            if(text.length() > 0){

                if(text.charAt(0) == '/'){
                    DataType.COMMAND.runSender(textField.getText().getBytes(StandardCharsets.UTF_8));
                }
                else{
                    DataType.MESSAGE.runSender(textField.getText().getBytes(StandardCharsets.UTF_8));
                }
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
            textField.setText("");
        });

        panel = new JPanel(new BorderLayout(5,5));
        panel.setBackground(Theme.CURRENT.background);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        scrollPane = new JScrollPane(textArea);

        scrollPane.setBackground(Theme.CURRENT.background);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(textField,BorderLayout.SOUTH);

    }

    public void setVisible(boolean visibility){
        frame.setVisible(visibility);
    }

}
