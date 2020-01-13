package com.thunder_cut.c2s;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * author: pllapallpal (Hyeokwoo Kwon)
 * date: 2020-01-13
 *
 * used technology stack
 * - ServerSocket, Socket, InputStream
 * - Swing GUI
 * - Thread
 * - BufferedImage, ImageIcon
 * - ByteArrayInputStream
 */
public class Server {

    private Socket socket;
    private InputStream input;

    public Server(int port) {

        // accept socket from ServerSocket
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JFrame creation
        JFrame serverFrame = new JFrame("Server");
        JPanel mainPanel = new JPanel(new BorderLayout());
        serverFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        // start thread
        new Thread() {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    try {
                        byte[] sizeArray = new byte[4];
                        input.read(sizeArray);
                        int size = ByteBuffer.wrap(sizeArray).asIntBuffer().get();

                        byte[] data = new byte[size];
                        System.out.println(input.read(data));
                        ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
                        BufferedImage image = ImageIO.read(byteInput);
                        imageLabel.setIcon(new ImageIcon(image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();



        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setSize(800, 600);
        serverFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Server(5555);
    }
}
