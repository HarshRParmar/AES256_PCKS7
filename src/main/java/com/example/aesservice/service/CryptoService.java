package com.example.aesservice.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String AES_CIPHER = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16; // 128 bits

    public String encrypt(String plainText, String keyString) {
        try {
            byte[] key = deriveKey(keyString);
            byte[] iv = new byte[IV_SIZE];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

            // Prepend IV to ciphertext
            byte[] ivAndCipher = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
            System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(ivAndCipher);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    public String decrypt(String base64IvAndCipher, String keyString) {
        try {
            byte[] ivAndCipher = Base64.getDecoder().decode(base64IvAndCipher);
            if (ivAndCipher.length < IV_SIZE) {
                throw new IllegalArgumentException("Invalid input data");
            }

            byte[] iv = Arrays.copyOfRange(ivAndCipher, 0, IV_SIZE);
            byte[] cipherBytes = Arrays.copyOfRange(ivAndCipher, IV_SIZE, ivAndCipher.length);

            byte[] key = deriveKey(keyString);

            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }

    private byte[] deriveKey(String keyString) throws Exception {
        // Derive a 256-bit key from the provided key string using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(keyString.getBytes("UTF-8"));
        // hash is 32 bytes = 256 bits
        return hash;
    }
}
