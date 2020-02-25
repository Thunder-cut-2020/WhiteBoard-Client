/*
 * ParticipantsPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui.frame.participants;

import com.thunder_cut.graphics.ui.theme.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ParticipantsPanel {
    private final int DEFAULT_GAP = 10;
    private JPanel participantsPanel;

    private List<JPanel> participants;
    private Map<Integer,BufferedImage> lastImages;

    public ParticipantsPanel(){

        participants = new ArrayList<>();
        lastImages = new HashMap<>();

        participantsPanel = new JPanel();
        participantsPanel.setBackground(Theme.CURRENT.background);
        participantsPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP, DEFAULT_GAP));
        participantsPanel.setLayout(new BoxLayout(participantsPanel, BoxLayout.Y_AXIS));

        participantsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                redrawParticipantPanel();
            }

            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeParticipantsPanel();
            }
        });

        makePanel();
        makePanel();
    }

    private void makePanel(){
        JPanel newPanel = new JPanel();
        newPanel.setBackground(Theme.CURRENT.primary);
        newPanel.setBorder(BorderFactory.createLineBorder(Theme.CURRENT.background, DEFAULT_GAP));

        newPanel.setMaximumSize(new Dimension(480, 270));
        newPanel.setPreferredSize(new Dimension(480, 270));
        newPanel.setIgnoreRepaint(true);

        participants.add(newPanel);

        participantsPanel.add(newPanel);
        participantsPanel.revalidate();
        participantsPanel.repaint();

    }

    public void drawImage(int srcID, byte[] imageData){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        while(srcID>=participants.size()){
            makePanel();
        }
        try {
            BufferedImage bi =  ImageIO.read(byteArrayInputStream);
            if(Objects.nonNull(lastImages.get(srcID))){
                lastImages.replace(srcID,bi);
            }
            else{
                lastImages.put(srcID,bi);
            }
            drawImage(srcID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawImage(int srcID){

        JPanel target = participants.get(srcID);
        SwingUtilities.invokeLater(()->{
            target.getGraphics()
                    .drawImage(lastImages.get(srcID),DEFAULT_GAP,DEFAULT_GAP,target.getWidth()-DEFAULT_GAP*2,target.getHeight()-DEFAULT_GAP*2,null);
        });

    }

    public void redrawParticipantPanel(){
        for(int i = 0; i<participants.size(); ++i)
            drawImage(i);
    }

    public void resizeParticipantsPanel(){

        int width = participantsPanel.getWidth();
        int height = participantsPanel.getWidth() * 9 / 16;

        for(int i = 0; i < participants.size(); ++i){
            JPanel panel = participants.get(i);
            Dimension dimension = new Dimension(width - DEFAULT_GAP * 3, height - DEFAULT_GAP * 3);
            panel.setMaximumSize(dimension);
            panel.setPreferredSize(dimension);
            panel.revalidate();
        }

        redrawParticipantPanel();
    }


    public JPanel getParticipantsPanel(){
        return participantsPanel;
    }
}
