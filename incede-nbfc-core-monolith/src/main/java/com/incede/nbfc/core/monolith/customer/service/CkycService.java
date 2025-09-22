package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.ckyc.PidData;
import com.incede.nbfc.core.monolith.customer.dto.CkycRequest;
import com.incede.nbfc.core.monolith.customer.dto.CkycRequestDto;
import com.incede.nbfc.core.monolith.customer.utils.CryptoUtils;
import com.incede.nbfc.core.monolith.customer.utils.KeyUtil;
import com.incede.nbfc.core.monolith.customer.utils.JaxbUtil;
import com.incede.nbfc.core.monolith.customer.utils.XmlSigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
@Service
public class CkycService {

    /**
     * Build, encrypt, and sign a CKYC request XML.
     */
    public String buildSignedRequest(CkycRequestDto dto) {
        try {
            // 1. Generate PID_DATA XML
            PidData pidData = new PidData(dto.getDateTime(), dto.getIdNo(), dto.getIdType());
            String pidXml = JaxbUtil.toXml(pidData);
            log.debug("Generated PID_DATA XML: {}", pidXml);

            // 2. Generate AES session key
            SecretKey sessionKey = CryptoUtils.generateAesKey256();

            // 3. Encrypt PID_DATA with AES session key
            String encryptedPid = CryptoUtils.encryptAesWithIvPrefix(sessionKey, pidXml);
            log.debug("Encrypted PID_DATA (Base64): {}", encryptedPid);

            // 4. Load CKYC public key and encrypt session key
            PublicKey ckycPublicKey = KeyUtil.loadPublicKey(Path.of("src/main/resources/keys/ckyc_pub.cer"));
            String encryptedSessionKey = CryptoUtils.encryptSessionKeyWithRsaOaep(ckycPublicKey, sessionKey);
            log.debug("Encrypted Session Key (Base64): {}", encryptedSessionKey);

            // 5. Build CKYC request DTO and marshal to XML
            CkycRequest ckycRequest = new CkycRequest("IN1895", "02", encryptedPid, encryptedSessionKey);
            String requestXml = JaxbUtil.toXml(ckycRequest);
            log.debug("Unsigned CKYC Request XML: {}", requestXml);

            // 6. Load FI private key from keystore and sign XML
            PrivateKey privateKey = KeyUtil.loadPrivateKeyFromJks(
                    String.valueOf(Path.of("src/main/resources/keys/fi_keystore.jks")),
                    "changeit", // keystore password
                    "fi-key"    // key alias
            );
            String signedXml = XmlSigner.signXml(requestXml, privateKey, null); // pass certificate if required

            log.info("Successfully built and signed CKYC request");
            return signedXml;

        } catch (Exception e) {
            log.error("Failed to build CKYC request", e);
            throw new RuntimeException("Failed to build CKYC request", e);
        }
    }
}
