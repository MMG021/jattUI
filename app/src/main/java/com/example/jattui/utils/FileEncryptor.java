package com.example.jattui.utils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor {
    //Arbitrarily selected 8-byte salt sequence:
    private static final byte[] salt = {
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
    };
    private static final byte[] IV_DATA = {
            (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
            (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B,
            (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
    };

    private static Cipher makeCipher(Boolean decryptMode) throws GeneralSecurityException {
        SecretKeySpec skeySpec = new SecretKeySpec(Base64.getDecoder().decode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        if (decryptMode)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        else
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher;
    }

    public static File encryptFile(File inFile)
            throws IOException, GeneralSecurityException {
        return Encryption.encryptFile(inFile);
    }

    public static File decryptFile(File inFile, String ext)
            throws IOException, GeneralSecurityException {
        return Encryption.decryptFile(inFile,ext);
    }
}