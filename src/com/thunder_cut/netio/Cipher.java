/*
 * Cryptor.java
 * Author : 나상혁
 * Created Date : 2020-02-29
 */
package com.thunder_cut.netio;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

//AES256
//RSA2048
public class Cipher {

    private Connection connection;

    private javax.crypto.Cipher encryption;
    private javax.crypto.Cipher decryption;

    public Cipher(Connection connection, Key key){

        this.connection = connection;
        try {
            encryption = javax.crypto.Cipher.getInstance("AES");
            encryption.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            decryption = javax.crypto.Cipher.getInstance("AES");
            decryption.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public ByteBuffer read(){
        ByteBuffer buffer = connection.read();
        ByteBuffer data = null;
        try {
            data = ByteBuffer.wrap(decryption.doFinal(buffer.array()));
        }
        catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void write(ByteBuffer data){
        try {

            ByteBuffer encrypted = ByteBuffer.wrap(encryption.doFinal(data.array()));
            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + encrypted.limit());

            buffer.putInt(encrypted.limit());
            buffer.put(encrypted);
            buffer.flip();

            connection.write(buffer);
        }
        catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
