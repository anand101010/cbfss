package com.incede.nbfc.notification.crypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.notification.common.CommonConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

@Component
public class OtpCrypto
{

	private final SecureRandom random = new SecureRandom();
	private final Argon2PasswordEncoder argon2;
	private final byte[] slotKeySecret;

	public OtpCrypto(
		@Value("${otp.hash.saltLength:16}") int saltLength,
		@Value("${otp.hash.hashLength:32}") int hashLength,
		@Value("${otp.hash.parallelism:1}") int parallelism,
		@Value("${otp.hash.memoryKb:65536}") int memoryKb,
		@Value("${otp.hash.iterations:3}") int iterations,
		@Value("${otp.slotKey.secret}") String slotKeySecret) {
		this.argon2 = new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryKb, iterations);
		this.slotKeySecret = slotKeySecret.getBytes(StandardCharsets.UTF_8);
	}

	public String secureDigits(int num)
    {
		StringBuilder sb = new StringBuilder(num);
		for (int i = 0; i < num; i++)
        {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public byte[] randomSalt(int num)
    {
		byte[] b = new byte[num];
		random.nextBytes(b);
		return b;
	}

	public String hashOtpCode(String secretKey, String code, byte[] salt) {
		String candidate = secretKey + ":" + code + ":" + Base64.getEncoder().encodeToString(salt);
		/// /argon is a hashing password encoding algorithm
        return argon2.encode(candidate);
	}

	public boolean verifyOtpCode(String storedHash, String secretKey, String code, byte[] salt) {
		String candidate = secretKey + ":" + code + ":" + Base64.getEncoder().encodeToString(salt);
		return argon2.matches(candidate, storedHash);
	}

    public String slotKey(Integer tenantId, String branchCode, String purpose, String normalizedTarget) {
        String msg = tenantId + "|" + branchCode + "|" + purpose + "|" + normalizedTarget;
        return hmacSha256Hex(slotKeySecret, msg);
    }

	public String normalizeTarget(String channel, String target)
    {
        if(target == null || target.trim().isEmpty())
        {
            throw new IllegalArgumentException("Target cannot be null or empty");
        }
		String normaliseTarget = target.trim();
		if ("EMAIL".equalsIgnoreCase(channel))
        {
			return normaliseTarget.toLowerCase();
		}
		String digits = normaliseTarget.replaceAll("[^0-9]", "");
		if (digits.length() == 10) return "+91" + digits;
		if (digits.startsWith("91") && digits.length() == 12) return "+" + digits;
		if (digits.startsWith("+")) return digits;
		return "+" + digits;
	}

	private static String hmacSha256Hex(byte[] secret, String msg)
    {
		try
        {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secret, "HmacSHA256"));
			byte[] out = mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(out.length * 2);
			for (byte b : out) sb.append(String.format("%02x", b));
			return sb.toString();
		}
        catch (Exception exception)
        {
			throw new IllegalStateException("HMAC failure", exception);
		}
	}
    public  String appendOtpMessage(String body,String otp)
    {
        return body.replace("$$OTP$$", otp);

    }




} 