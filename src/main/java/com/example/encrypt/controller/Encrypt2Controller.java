package com.example.encrypt.controller;

import com.example.encrypt.dto.req.PayloadTest2;
import com.example.encrypt.service.AddressService;
import com.example.encrypt.service.RedisService;
import com.example.encrypt.util.RSAUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Encrypt2Controller {
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;


    @GetMapping("/test2/create/key")
    public String createKey2(HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = RSAUtil.genRSAKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyPair.getPublic();


        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

        String publicKeyModulus = publicKeySpec.getModulus().toString(16);
        String publicKeyExponent = publicKeySpec.getPublicExponent().toString(16);
        String timestamp = String.valueOf(LocalDateTime.now(ZoneId.systemDefault()).toInstant(ZoneOffset.UTC).toEpochMilli());
        response.setHeader("modulus", publicKeyModulus);
        response.setHeader("exponent", publicKeyExponent);
        response.setHeader("keyEpochTime", timestamp);

        redisService.setValuesAndDuration(timestamp,
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));


        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    @GetMapping("/test/encryptPayload")
    public PayloadTest2 testEncryptPayload()  {
//        PublicKey p = RSAUtil.getPublicKeyFromBase64Encrypted(payload.getPublicKey());
//        String a = redisService.getStringValues(request.getHeader("keyEpochTime"));

        PayloadTest2 payloadTest2 = new PayloadTest2();
        payloadTest2.setData("abc");
        return payloadTest2;
    }



    @PostMapping("/test/decrypt")
    public void testDecrypt(HttpServletRequest request, @RequestBody PayloadTest2 payload)  {
//        PublicKey p = RSAUtil.getPublicKeyFromBase64Encrypted(payload.getPublicKey());
//        String a = redisService.getStringValues(request.getHeader("keyEpochTime"));

        log.info(payload.getData());
//        PrivateKey privateKey = RSAUtil.getPrivateKeyFromBase64Encrypted(a);
//        String decyrptedString = RSAUtil.decryptRSA(payload.getData(),privateKey);
//        return decyrptedString;
    }



}
