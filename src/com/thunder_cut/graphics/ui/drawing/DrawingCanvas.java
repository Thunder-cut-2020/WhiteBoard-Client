/*
 * DrawingCanvas.java
 * Author : susemeeee
 * Created Date : 2020-01-17
 */
package com.thunder_cut.graphics.ui.drawing;

import com.thunder_cut.graphics.controller.MouseData;
import com.thunder_cut.graphics.controller.MouseStatus;
import com.thunder_cut.graphics.controller.Resizer;
import com.thunder_cut.netio.DataType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DrawingCanvas {
    private Canvas canvas;
    private CanvasPixelInfo canvasPixelInfo;
    private BiConsumer<MouseData, CanvasPixelInfo> mouseHandler;
    private Consumer<MouseStatus> workDataRecorder;
    private Resizer resizer = new Resizer();

    private BufferStrategy canvasBuffer;

    private final int eventLimit = 10;
    private int eventCount = 0;

    public DrawingCanvas() {
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setBackground(Color.WHITE);
        canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void addEventListeners(){
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                workDataRecorder.accept(MouseStatus.PRESSED);
                mouseHandler.accept(new MouseData(MouseStatus.PRESSED, e.getX(), e.getY()), canvasPixelInfo);
                drawCanvas();
                sendImage();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseHandler.accept(new MouseData(MouseStatus.RELEASED, e.getX(), e.getY()), canvasPixelInfo);
                workDataRecorder.accept(MouseStatus.RELEASED);
                drawCanvas();
                sendImage();
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseHandler.accept(new MouseData(MouseStatus.DRAGGED, e.getX(), e.getY()), canvasPixelInfo);
                drawCanvas();
                ++eventCount;
                if(eventCount>eventLimit){
                    sendImage();
                    eventCount = 0;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseHandler.accept(new MouseData(MouseStatus.MOVED, e.getX(), e.getY()), canvasPixelInfo);
                drawCanvas();
                ++eventCount;
                if(eventCount>eventLimit){
                    sendImage();
                    eventCount = 0;
                }
            }
        });
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                //Here you can check the changing size
                try {
                    resizer.resize(canvas.getWidth(),canvas.getHeight(),canvasPixelInfo);
                }
                catch (NullPointerException e1) { }

                drawCanvas();
            }
        });
    }

    public void addMouseHandler(BiConsumer<MouseData, CanvasPixelInfo> mouseHandler) {
        this.mouseHandler = mouseHandler;
    }

    public void addWorkDataRecorder(Consumer<MouseStatus> workDataRecorder) {
        this.workDataRecorder = workDataRecorder;
    }

    public void createPixelInfo() {
        canvasPixelInfo = new CanvasPixelInfo(canvas.getWidth(), canvas.getHeight(), Color.WHITE);
    }

    public void drawCanvas() {
        if (canvasBuffer == null) {
            canvas.createBufferStrategy(2);
            canvasBuffer = canvas.getBufferStrategy();
            return;
        }

        BufferedImage image1 = canvasPixelInfo.toBufferedImage();
        BufferedImage image2 = canvasPixelInfo.toBufferedImageEffect();

        Graphics2D g = (Graphics2D) canvasBuffer.getDrawGraphics();
        g.drawImage(image1, 0, 0, canvas);
        g.drawImage(image2, 0, 0, canvas);
        g.dispose();
        canvasBuffer.show();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public CanvasPixelInfo getCanvasPixelInfo() {
        return canvasPixelInfo;
    }

    public void notifyFrameMoved(){
        drawCanvas();
    }

    private void sendImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(canvasPixelInfo.toBufferedImage(),"png",baos);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        DataType.IMAGE.runSender(baos.toByteArray());
    }
}
