package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.Gdx;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class Encrypter {

    private IvParameterSpec iv;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    public Encrypter(String ivParam, String keyParam) {
        try {
            iv = new IvParameterSpec(ivParam.getBytes("UTF-8"));
            secretKeySpec = new SecretKeySpec(keyParam.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            Gdx.app.error("Encryptor", e.toString());
        }
    }

    public Encrypter(byte[] ivParam, byte[] keyParam) {
        try {
            iv = new IvParameterSpec(ivParam);
            secretKeySpec = new SecretKeySpec(keyParam, "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            Gdx.app.error("Encryptor", e.toString());
        }
    }

    public Encryption encrypt(String input) {
        try {
            return encrypt(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        }
        return null;
    }

    public Encryption encrypt(byte[] input) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
            int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            return new Encryption(encrypted, enc_len);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }

    public byte[] decrypt(Encryption encryption) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] decrypted = new byte[cipher.getOutputSize(encryption.length)];
            int dec_len = cipher.update(encryption.encrypted, 0, encryption.length, decrypted, 0);
            cipher.doFinal(decrypted, dec_len);
            return decrypted;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }

    public String decryptString(Encryption encryption) {
        byte[] output = decrypt(encryption);
        if(output != null)
            return new String(output);
        return null;
    }

    public class Encryption {

        public byte[] encrypted;
        public int length;

        public Encryption(byte[] encrypted, int length) {
            this.encrypted = encrypted;
            this.length = length;
        }

        @Override
        public String toString() {
            try {
                return "Encrypted:( " + new String(encrypted, "UTF-8") + ": " + length + ")";
            } catch (Exception e) {
                Gdx.app.error("EncryptionToString", e.toString());
            }
            return super.toString();
        }
    }
}
