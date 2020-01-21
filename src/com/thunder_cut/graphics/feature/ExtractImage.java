/*
 * ExtractImage.java
 * Author : Cwhist
 * Created Date : 2020-01-21
 */
package com.thunder_cut.graphics.feature;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ExtractImage {

    public BufferedImage extract(int[] pixels, int startXPos, int startYPos,
                                 int endXPos, int endYPos, int canvasWidth) {
        int lowX;
        int lowY;
        int highX;
        int highY;

        if(startXPos > endXPos) {
            lowX = endXPos;
            highX = startXPos;
        }
        else {
            lowX = startXPos;
            highX = endXPos;
        }
        if(startYPos > endYPos) {
            lowY = endYPos;
            highY = startYPos;
        }
        else {
            lowY = startYPos;
            highY = endYPos;
        }

        int width = highX-lowX + 1;
        int height = highY-lowY + 1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] extractedPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for(int nowHeight=0; nowHeight<height; nowHeight++) {
            for(int nowWidth = 0; nowWidth<width; nowWidth++) {
                extractedPixels[nowHeight*width + nowWidth] = pixels[(lowY+nowHeight)*canvasWidth + (lowX+nowWidth)];
            }
        }

        return image;
    }
}
