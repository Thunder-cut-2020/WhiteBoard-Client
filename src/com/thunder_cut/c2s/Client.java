package com.thunder_cut.c2s;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * author: pllapallpal (Hyeokwoo Kwon)
 * date: 2020-01-13
 *
 * used technology stack
 * - Socket, OutputStream
 * - Swing GUI
 * - BufferedImage, ImageIcon
 * - ByteArrayOutputStream
 */
public class Client {

    private Socket socket;
    private OutputStream output;

    public Client(String address, int port) {


        // allocation of socket
        try {
            socket = new Socket(address, port);
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JFrame creation
        JFrame clientFrame = new JFrame("Client");
        JPanel mainPanel = new JPanel(new BorderLayout());
        clientFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        JButton sendButton = new JButton("Send");
        mainPanel.add(sendButton, BorderLayout.SOUTH);

        // add ActionListener to button
        sendButton.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            int value = fileChooser.showOpenDialog(null);
            if (value == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                    imageLabel.setIcon(new ImageIcon(image));
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", byteOut);
                    byte[] size = ByteBuffer.allocate(4).putInt(byteOut.size()).array();
                    byte[] data = byteOut.toByteArray();
                    byteOut = null;
                    output.write(size);
                    output.write(data);
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setSize(800, 600);
        clientFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Client("127.0.0.1", 5555);
    }
}
