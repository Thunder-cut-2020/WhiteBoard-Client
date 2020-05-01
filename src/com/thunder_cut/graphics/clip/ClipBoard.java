/*
 * ClipBoard.java
 * Author : 나상혁
 * Created Date : 2020-03-02
 */
package com.thunder_cut.graphics.clip;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipBoard implements ClipboardOwner {

    private static ClipBoard clipBoard = new ClipBoard();

    public static void copyImageToClipBoard(BufferedImage bi){
        TransferableImage is = new TransferableImage(bi);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(is,clipBoard);
    }

    public static BufferedImage getBufferedImageFromClipBoard(){
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(clipBoard);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
            try
            {
                Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D bgr = bi.createGraphics();
                bgr.drawImage(img,0,0, null);
                bgr.dispose();
                return bi;
            }
            catch (UnsupportedFlavorException | IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
