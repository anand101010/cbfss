package com.incede.nbfc.core.monolith.customer.utils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

public final class CryptoUtils {

    private CryptoUtils() {
        // Utility class
    }

    /** Generate 256-bit AES key */
    public static SecretKey generateAesKey256() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        return kg.generateKey();
    }

    /** Encrypt plaintext using AES/CBC/PKCS5Padding with IV prefix */
    public static String encryptAesWithIvPrefix(SecretKey key, String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] ivAndCipher = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivAndCipher);
    }

    /** Encrypt AES session key using CKYC public key with RSA/OAEP */
    public static String encryptSessionKeyWithRsaOaep(PublicKey pub, SecretKey aesKey) throws Exception {
        Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        rsa.init(Cipher.ENCRYPT_MODE, pub);
        byte[] enc = rsa.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(enc);
    }

}

