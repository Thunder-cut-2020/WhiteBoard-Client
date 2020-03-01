package com.thunder_cut.netio;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum DataType {

    IMAGE('I'),
    COMMAND('C'),
    MESSAGE('M'),
    LIST('L');

    public static DataType getType(char code){
        for(DataType type : DataType.values()){
            if(type.code == code){
                return type;
            }
        }
        return MESSAGE;
    }

    public final char code;

    private BiConsumer<IndexedName, byte[]> received;
    private Consumer<byte[]> sender;

    DataType(char code){
        this.code = code;
    }

    public void addOnReceived(BiConsumer<IndexedName, byte[]> receiver){
        this.received = receiver;

    }

    public void acceptReceiver(IndexedName name, byte[] data){
        received.accept(name,data);
    }

    public void addSender(Consumer<byte[]> sender){
        this.sender = sender;
    }

    public void runSender(byte[] data){
        if(Objects.nonNull(sender))
            sender.accept(data);
    }

}
