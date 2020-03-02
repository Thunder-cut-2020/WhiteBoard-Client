/*
 * Connection.java
 * Author : 나상혁
 * Created Date : 2020-02-29
 */
package com.thunder_cut.netio;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.security.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;

public class Connection {

    private SocketChannel channel;

    private ExecutorService es;
    private FutureTask<?> task;

    private Receiver receiver;
    private Sender sender;

    private String nickName = "";

    private com.thunder_cut.netio.Cipher cipher;

    public Connection(){

        nickName = "user" + ThreadLocalRandom.current().nextInt(65535);
    }

    public void connect(String address, int port){
        if(Objects.isNull(channel) || !channel.isConnected()){

            new Thread(() -> {
                try {
                    channel = SocketChannel.open();

                    channel.connect(new InetSocketAddress(address, port));

                    initializeConnection();

                    receiver = new Receiver();
                    sender = new Sender(0);

                    receiver.addReader(cipher::read);
                    sender.addSender(cipher::write);

                    es = Executors.newSingleThreadExecutor();
                    task = new FutureTask<>(receiver::readData);
                    es.execute(task);

                }
                catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                }
            }).start();
        }
    }
    private void initializeConnection() {
        KeyPair keyPair = Objects.requireNonNull(generateKeyPair());
        sendPKey(keyPair.getPublic());
        receiveKey(keyPair);
        bindCommands();
//        sendNickname();
    }

    private void bindCommands() {

        Command.NAME.addAction((nickname)->{
            setNickName(nickName);
            sendNickname();
        });

    }

    private KeyPair generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendPKey(PublicKey pk){
        ByteBuffer buffer = ByteBuffer.allocate(pk.getEncoded().length + Integer.BYTES);
        buffer.putInt(pk.getEncoded().length);
        buffer.put(pk.getEncoded());
        buffer.flip();
        write(buffer);
    }

    private void receiveKey(KeyPair keyPair) {

        ByteBuffer data = read();

        byte[] bytes = data.array();
        byte[] decrypted = null;
        try {
            Cipher dec = Cipher.getInstance("RSA");
            dec.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            decrypted = dec.doFinal(bytes);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        cipher  = new com.thunder_cut.netio.Cipher(this,
                new SecretKeySpec(Objects.requireNonNull(decrypted),"AES"));
    }

    private void sendNickname(){

        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES + Command.NAME.toString().getBytes().length + nickName.getBytes().length);

        buffer.putChar(DataType.COMMAND.code);
        buffer.put(Command.NAME.toString().getBytes());
        buffer.put(nickName.getBytes());
        buffer.flip();
        cipher.write(buffer);
    }

    public void disconnect(){

        try {
            receiver.shutdown();
            task.cancel(true);
            es.shutdown();

            sender.addSender(this::closedWrite);
            channel.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read byte buffer from channel.
     * @return Data with Type, SRC_ID and DST_ID. Not included size.
     */
    ByteBuffer read(){

        ByteBuffer size = ByteBuffer.allocate(Integer.BYTES);
        ByteBuffer data = null;

        try {
            channel.read(size);
            size.flip();
            int sz = size.getInt();
            data = ByteBuffer.allocate(sz);
            while(data.hasRemaining()){
                channel.read(data);
            }
        }
        catch(ClosedByInterruptException ex){
            //Disconnected
        }
        catch (IOException e) {
            e.printStackTrace();
            disconnect();
        }
        Objects.requireNonNull(data).flip();
        return data;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    void write(ByteBuffer byteBuffer){
        try {
            if(channel.isConnected())
            channel.write(byteBuffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closedWrite(ByteBuffer byteBuffer){

        //Do Nothing.
    }

}
