/*
 * Sender.java
 * Author : 나상혁
 * Created Date : 2020-02-29
 */
package com.thunder_cut.netio;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class Sender {


    private Consumer<ByteBuffer> sender;
    private int ID;

    public Sender(int id){

        this.ID = id;

        DataType.IMAGE.addSender(this::sendImage);
        DataType.MESSAGE.addSender(this::sendString);
        DataType.COMMAND.addSender(this::sendCommand);

    }


    private void send(DataType type, ByteBuffer data){

        ByteBuffer packagedData = ByteBuffer.allocate(Character.BYTES + Integer.BYTES + data.limit());
        packagedData.putChar(type.code);
        packagedData.putInt(ID);
        packagedData.put(data);

        sender.accept(packagedData);
    }

    private void sendImage(byte[] img){
        send(DataType.IMAGE, ByteBuffer.wrap(img));
    }

    private void sendString(byte[] data){
        send(DataType.MESSAGE, ByteBuffer.wrap(data));
    }

    private void sendCommand(byte[] data){
        send(DataType.COMMAND, ByteBuffer.wrap(data));
    }

    public void addSender(Consumer<ByteBuffer> sender){
        this.sender = sender;

    }
}
