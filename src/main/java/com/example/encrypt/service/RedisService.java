package com.example.encrypt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String ,String> redisTemplate;
    private final RedisTemplate<String ,Object> redisHashTemplate;



    public void setValues(String key, String count) {
        ValueOperations<String , String > values = redisTemplate.opsForValue();
        values.set(key, count);
    }
    public void setValuesAndDuration(String key, String count) {
        ValueOperations<String , String > values = redisTemplate.opsForValue();
        values.set(key, count, Duration.ofMinutes(10L));
    }

    //String 타입 value Get
    public String getStringValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void setAddress(String address){
        SetOperations<String , String > values = redisTemplate.opsForSet();
        values.add("address",address);
    }
    public boolean checkAddress(String address){
        SetOperations<String , String > values = redisTemplate.opsForSet();
        return Boolean.TRUE.equals(values.isMember("address", address));
    }


    public void setHashAddress(String network, String address, Long userId){
        HashOperations<String , String, Long> values = redisHashTemplate.opsForHash();
        values.put("network:"+network,"address:"+address, userId);
    }
    public Long checkHashAddress(String network, String address){
        HashOperations<String , String, Long> values = redisHashTemplate.opsForHash();
        return values.get("network:"+network, "address:"+address);
    }

//    public void setRSAKey(String timestamp, String aesKey, String rsaKey){
//        HashOperations<String , String, String> values = redisHashTemplate.opsForHash();
//        values.put("secretKey:"+timestamp,"aes", aesKey);
//        values.put("secretKey:"+timestamp,"rsa", rsaKey);
//    }


}

