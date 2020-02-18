/*
 * EncapsulatedData.java
 * Author : Hyeokwoo Kwon
 * Created Date : 2020-02-07
 */
package com.thunder_cut.netio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Includes information of data that'll be sent to the server
 * <p>
 * Header of the data has a set of sequence
 * <pre>
 * - dataType
 * - dataSize
 * - actual data
 * </pre>
 */
public class EncapsulatedData {

    public final ByteBuffer encapsulatedData;

    public EncapsulatedData(BufferedImage image) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer data = ByteBuffer.wrap(bos.toByteArray());

        int dataSize = data.limit();
        encapsulatedData = ByteBuffer.allocate(dataSize + 6);
        encapsulatedData.putChar(DataType.IMG.type);
        encapsulatedData.putInt(dataSize);
        encapsulatedData.put(data);

        encapsulatedData.flip();
    }

    public EncapsulatedData(String message) {

        ByteBuffer data = ByteBuffer.wrap(message.getBytes());
        int dataSize = data.limit();

        encapsulatedData = ByteBuffer.allocate(dataSize + 6);
        encapsulatedData.putChar(DataType.MSG.type);
        encapsulatedData.putInt(dataSize);
        encapsulatedData.put(data);

        encapsulatedData.flip();
    }

    public EncapsulatedData(ByteBuffer data, DataType dataType) {

        int dataSize = data.limit();
        encapsulatedData = ByteBuffer.allocate(dataSize + 6);
        encapsulatedData.putChar(dataType.type);
        encapsulatedData.putInt(dataSize);
        encapsulatedData.put(data);

        encapsulatedData.flip();
    }
}
