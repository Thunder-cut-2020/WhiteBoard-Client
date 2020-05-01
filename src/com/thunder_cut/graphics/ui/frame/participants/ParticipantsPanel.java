/*
 * ParticipantsPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui.frame.participants;

import com.thunder_cut.graphics.ui.theme.Theme;
import com.thunder_cut.netio.IndexedName;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    }

    private void makePanel(){

        JPanel outer = new JPanel();
        outer.setBackground(Theme.CURRENT.background);
        outer.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Theme.CURRENT.secondary, DEFAULT_GAP),"Disconnected",
                        TitledBorder.LEFT, TitledBorder.TOP, Theme.CURRENT.font, Theme.CURRENT.onSecondary));

        JPanel newPanel = new JPanel();
        newPanel.setBackground(Theme.CURRENT.primary);

        newPanel.setMaximumSize(new Dimension(480, 270));
        newPanel.setPreferredSize(new Dimension(480, 270));
        newPanel.setIgnoreRepaint(true);

        outer.add(newPanel);

        participants.add(outer);

        participantsPanel.add(outer);
        participantsPanel.revalidate();
        participantsPanel.repaint();

    }

    public void drawImage(IndexedName indexedName, byte[] imageData){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
        int idx = indexedName.idx;
        while(idx>=participants.size()){
            makePanel();
        }
        try {
            BufferedImage bi =  ImageIO.read(byteArrayInputStream);
            if(Objects.nonNull(lastImages.get(idx))){
                lastImages.replace(idx,bi);
            }
            else{
                lastImages.put(idx,bi);
            }
            TitledBorder border = ((TitledBorder)participants.get(idx).getBorder());
            if(!border.getTitle().equals(indexedName.name)){
                SwingUtilities.invokeLater(()->{
                    border.setTitle(indexedName.name);
                    participants.get(idx).revalidate();
                    participants.get(idx).repaint();
                });
            }
            drawImage(idx);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawImage(int srcID){

        JPanel target = (JPanel) participants.get(srcID).getComponent(0);
        SwingUtilities.invokeLater(()->{
            target.getGraphics()
                    .drawImage(lastImages.get(srcID),0,0,
                            target.getWidth(),target.getHeight(),
                            null);
        });

    }

    public void redrawParticipantPanel(){
        for(int i = 0; i<participants.size(); ++i)
            drawImage(i);
    }

    public void resizeParticipantsPanel(){

        int width = participantsPanel.getWidth();
        int height = participantsPanel.getWidth() * 9 / 16;

        for (JPanel panel : participants) {
            JPanel drawingPanel = (JPanel) panel.getComponent(0);
            Dimension dimension = new Dimension(width - DEFAULT_GAP * 3, height);
            panel.setMaximumSize(dimension);
            panel.setPreferredSize(dimension);
            Dimension drawingDimension = new Dimension(width - DEFAULT_GAP * 6, height - DEFAULT_GAP * 4);
            drawingPanel.setMaximumSize(drawingDimension);
            drawingPanel.setPreferredSize(drawingDimension);
            panel.revalidate();
        }

        redrawParticipantPanel();
    }


    public JPanel getParticipantsPanel(){
        return participantsPanel;
    }
}
