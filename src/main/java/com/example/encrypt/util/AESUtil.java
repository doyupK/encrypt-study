package com.example.encrypt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

@Slf4j
public class AESUtil {
    private final String ALGORITHM = "AES/GCM/NoPadding";
    private final String KEY = "abcdabcdabcdabcd";
    private String iv;

    private Key createKeySpec() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(KEY.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hashBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("createKeySpec fail : " + e.getMessage());
        }
    }

    private GCMParameterSpec createIvSpec() {
        try {
            String iv = StringUtil.randomStr(16);
//            String iv = "abcdabcdabcdabcd";
            this.iv = iv;
//            return new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            return new GCMParameterSpec(128, iv.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("createIvSpec fail : " + e.getMessage());
        }

    }


    public String encrypt(String data) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, createKeySpec(), createIvSpec());
            byte[] encryptData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return iv + Base64Utils.encodeToUrlSafeString(encryptData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException("encrypt fail : " + e.getMessage());
        }
    }
    public String decryptPayload(String data, String key) {
//        String content = data.substring(16);
        byte[] dataBytes = Base64Utils.decodeFromUrlSafeString(data);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, createKeySpec(), new GCMParameterSpec(128, key.getBytes()));
            byte[] original = cipher.doFinal(dataBytes);
            return new String(original, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("decrypt fail : " + e.getMessage());
        }
    }

    public String decryptParam(String data, String key) throws JsonProcessingException {
//        String content = data.substring(16);
//        byte[] dataBytes = Base64Utils.decodeFromString(data);
        byte[] dataBytes = Base64Utils.decodeFromUrlSafeString(data);

//        Base64.getDecoder().decode(data.getBytes()); ->  Base64Utils.decodeFromUrlSafeString(data);

        try {

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, createKeySpec(), new GCMParameterSpec(128, key.getBytes()));
            byte[] original = cipher.doFinal(dataBytes);
            return new String(original, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("decrypt fail : " + e.getMessage());
        }
    }

}
