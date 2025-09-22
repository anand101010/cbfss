package com.incede.nbfc.core.monolith.customer.utils;


import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public final class KeyLoader {
    private KeyLoader(){}

    public static PublicKey loadPublicKeyFromCer(String cerPath) throws Exception {
        try (InputStream is = new FileInputStream(cerPath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            return cert.getPublicKey();
        }
    }

    public static KeyStore.PrivateKeyEntry loadPrivateKeyEntry(String pkcs12Path, String storePassword, String alias, String keyPassword) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream is = new FileInputStream(pkcs12Path)) {
            ks.load(is, storePassword.toCharArray());
        }
        KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(keyPassword.toCharArray());
        return (KeyStore.PrivateKeyEntry) ks.getEntry(alias, prot);
    }
}

