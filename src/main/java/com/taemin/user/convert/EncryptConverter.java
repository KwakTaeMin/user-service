package com.taemin.user.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class EncryptConverter implements AttributeConverter<String, String> {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;  // 12 bytes for GCM
    private static final int TAG_SIZE = 128; // 16 bytes

    private final byte[] key;

    public EncryptConverter() {
        this.key = "MySuperSecretKey".getBytes();
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = generateIV();
            GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
            byte[] encryptedData = cipher.doFinal(attribute.getBytes());

            byte[] encryptedIvAndData = new byte[IV_SIZE + encryptedData.length];
            System.arraycopy(iv, 0, encryptedIvAndData, 0, IV_SIZE);
            System.arraycopy(encryptedData, 0, encryptedIvAndData, IV_SIZE, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedIvAndData);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            byte[] decodedData = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(decodedData, 0, iv, 0, IV_SIZE);

            GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] originalData = cipher.doFinal(decodedData, IV_SIZE, decodedData.length - IV_SIZE);
            return new String(originalData);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting", e);
        }
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
}
