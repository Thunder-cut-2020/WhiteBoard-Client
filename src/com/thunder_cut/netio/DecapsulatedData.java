/*
 * DecapsulatedData.java
 * Author : Hyeokwoo Kwon
 * Created Date : 2020-02-07
 */
package com.thunder_cut.netio;

import java.nio.ByteBuffer;

/**
 * Includes information of data that has come from server
 * <p>
 * Header of the data has a set of sequence
 * <pre>
 * - dataType
 * - srcID
 * - dstID
 * - dataSize
 * - actual data
 * </pre>
 */
public class DecapsulatedData {

    public final char dataType;
    public final int srcID;
    public final int dstID;
    public final int dataSize;
    public final ByteBuffer data;

    public DecapsulatedData(ByteBuffer data) {

        data.flip();

        dataType = data.getChar();
        srcID = data.getInt();
        dstID = data.getInt();
        dataSize = data.getInt();
        byte[] realData = new byte[data.limit() - 14];
        data.get(realData);
        this.data = ByteBuffer.wrap(realData);

        this.data.flip();
    }
}
