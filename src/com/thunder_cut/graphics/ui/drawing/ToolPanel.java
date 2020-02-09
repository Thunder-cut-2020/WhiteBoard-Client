/*
 * ToolPanel.java
 * Author : 김태건
 * Created Date : 2020-01-08
 */
package com.thunder_cut.graphics.ui.drawing;

import com.thunder_cut.graphics.controller.DrawingMode;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.function.Consumer;

public class ToolPanel {
    private JPanel toolPanel;
    private Consumer<DrawingMode> drawHandler;

    private EnumMap<DrawingMode, JButton> buttons;

    public ToolPanel() {
        toolPanel = new JPanel();

        buttons = new EnumMap<>(DrawingMode.class);

        for (DrawingMode mode : DrawingMode.values()) {
            JButton button = new JButton(mode.DISPLAY_NAME);
            button.addActionListener(e -> drawHandler.accept(mode));
            buttons.put(mode, button);

            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
        }

        toolPanel.setPreferredSize(new Dimension(1280, 180));
        toolPanel.setBackground(Color.DARK_GRAY);

        buttons.forEach((mode, button) -> toolPanel.add(button));
    }

    public void addDrawModeHandler(Consumer<DrawingMode> handler) {
        drawHandler = handler;
    }

    public JPanel getToolPanel() {
        return toolPanel;
    }
}