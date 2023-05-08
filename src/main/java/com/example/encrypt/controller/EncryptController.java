package com.example.encrypt.controller;

import com.example.encrypt.dto.req.AddressCheckDto;
import com.example.encrypt.dto.req.DummyQuantity;
import com.example.encrypt.dto.req.Payload;
import com.example.encrypt.response.Code;
import com.example.encrypt.response.ResponseDto;
import com.example.encrypt.service.AddressService;
import com.example.encrypt.service.RedisService;
import com.example.encrypt.util.RSAUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EncryptController {
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;


    @GetMapping("/create/key")
    public String createKey(HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = RSAUtil.genRSAKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyPair.getPublic();

        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey,RSAPublicKeySpec.class);

        String publicKeyModulus = publicKeySpec.getModulus().toString(16);
        String publicKeyExponent = publicKeySpec.getPublicExponent().toString(16);

        response.setHeader("modulus", publicKeyModulus);
        response.setHeader("exponent", publicKeyExponent);

        redisService.setValues(
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    @GetMapping("/get/public-key")
    public String getPublicKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = RSAUtil.genRSAKeyPair();
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));




//        redisService.setValues(keyPair.getPublic(),keyPair.getPrivate().getEncoded().toString());
        return keyPair.getPublic().toString();
    }

    @GetMapping("/get/public-key/encrypt")
    public  Map<String, Object > getPublicEncrypt(@RequestParam String keyword) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        KeyPair keyPair = RSAUtil.genRSAKeyPair();


        redisService.setValues(
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

        System.out.println(keyPair.getPublic());

        String encyrptedString = RSAUtil.encryptRSA(keyword, keyPair.getPublic());

        byte[] publicKey = keyPair.getPublic().getEncoded();
        Map<String, Object > map = new HashMap<>();
        map.put("enc", encyrptedString);
        map.put("key", publicKey);
//        System.out.println(RSAUtil.getPublicKeyFromBase64Encrypted());
//        System.out.println(Arrays.toString (publicKey));
        return map;
    }


    @PostMapping("/get/public-key/decrypt")
    public String getPrivateDecrypt(@RequestBody Payload payload) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidKeyException, JsonProcessingException {
//        PublicKey p = RSAUtil.getPublicKeyFromBase64Encrypted(payload.getPublicKey());
        String a = redisService.getStringValues(payload.getPublicKey());

        PrivateKey privateKey = RSAUtil.getPrivateKeyFromBase64Encrypted(a);
        String decyrptedString = RSAUtil.decryptRSA(payload.getKeyword(),privateKey);
        return decyrptedString;
    }

    @GetMapping("getEncryptKey")
    public Map<String,String > testa(){
        Map<String,String > map = new HashMap<>();

        map.put("id","김도엽");
        map.put("address","지갑주소12312312");
        map.put("password", "Rlaehduq12#");
        map.put("phoneNumber","010000000000");
        map.put("username","01074123456");


        return map;
    }

    @GetMapping("abc")
    public ResponseEntity<?> testab(@RequestParam String id,
                                    @RequestParam String address,
                                    @RequestParam String password,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String username){
        log.info("parameter ID : {}", id);
        log.info("parameter address : {}", address);
        log.info("parameter password : {}", password);
        log.info("parameter phoneNumber : {}", phoneNumber);
        log.info("parameter username : {}", username);


        return null;
    }

    @PostMapping("/token/dummy")
    public ResponseEntity<?> addDummy(@RequestBody DummyQuantity quantity){
        addressService.addDummy(quantity);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/token/check")
    public ResponseEntity<?> tokenCheck(@RequestBody AddressCheckDto addressCheckDto){
        return ResponseEntity.ok().body(new ResponseDto<>(Code.SUCCESS,addressService.checkAddress(addressCheckDto)));
    }



}
