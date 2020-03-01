/*
 * ImageExtractor.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageExtractor {

    BufferedImage image;
    int[] extractedPixels;

    public void extract(int[] pixels, int startXPos, int startYPos,
                                 int endXPos, int endYPos, int canvasWidth) {

        int width = endXPos - startXPos + 1;
        int height = endYPos - startYPos + 1;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        extractedPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for(int nowHeight=0; nowHeight<height; nowHeight++) {
            System.arraycopy(pixels,((startYPos+nowHeight)*canvasWidth + startXPos), extractedPixels, nowHeight*width, width);
        }
    }

    public int[] getExtractedPixels() {
        return extractedPixels;
    }

    public BufferedImage getImage() {
        return image;
    }
}
