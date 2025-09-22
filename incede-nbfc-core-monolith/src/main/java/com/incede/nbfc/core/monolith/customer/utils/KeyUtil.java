package com.incede.nbfc.core.monolith.customer.utils;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class KeyUtil {

    private KeyUtil() {}

    /**
     * Load an RSA public key from a PEM file
     * @param path Path to the .cer/.pem public key file
     * @return PublicKey instance
     * @throws Exception if any error occurs
     */
    public static PublicKey loadPublicKey(Path path) throws Exception {
        String key = Files.readString(path)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    /**
     * Load an RSA private key from a PEM file
     * @param path Path to the .pem private key file
     * @return PrivateKey instance
     * @throws Exception if any error occurs
     */
    public static PrivateKey loadPrivateKey(Path path) throws Exception {
        String key = Files.readString(path)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PrivateKey loadPrivateKeyFromJks(String keystorePath, String keystorePassword, String keyAlias) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            ks.load(fis, keystorePassword.toCharArray());
        }
        return (PrivateKey) ks.getKey(keyAlias, keystorePassword.toCharArray());
    }
}
