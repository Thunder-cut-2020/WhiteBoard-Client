/*
 * Receiver.java
 * Author : 나상혁
 * Created Date : 2020-02-29
 */
package com.thunder_cut.netio;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Receiver {

    private Supplier<ByteBuffer> reader;

    private boolean isRunning = false;

    private Map<Integer, String> names;

    public Receiver(){

        names = new ConcurrentHashMap<>();
        DataType.LIST.addOnReceived(this::updateNickname);
    }

    boolean readData(){
        isRunning = true;

        while(true){

            synchronized (this){
                if(!isRunning){
                    return false;
                }
            }

            ByteBuffer buffer = reader.get();
            char type = buffer.getChar();


            int src = buffer.getInt();

            byte[] data = new byte[buffer.limit() - Character.BYTES - Integer.BYTES];

            buffer.get(data);


            int idx = 0;
            for (int i : names.keySet()) {
                if (i == src) {
                    break;
                }
                ++idx;
            }

            DataType.getType(type).acceptReceiver(new IndexedName(idx,names.get(src)),data);

        }
    }

    private void updateNickname(IndexedName _unused, byte[] data){
        String[] value = new String(data).split("/");

        names.clear();

        for(int i = 0; i<value.length; i += 2){
            names.putIfAbsent(Integer.parseInt(value[i]),value[i+1]);
        }

    }

    void shutdown(){
        synchronized (this){
            isRunning = false;
        }
    }

    void addReader(Supplier<ByteBuffer> reader){
        this.reader = reader;
    }

}
