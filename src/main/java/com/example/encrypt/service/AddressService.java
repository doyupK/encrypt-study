package com.example.encrypt.service;

import com.example.encrypt.dto.req.AddressCheckDto;
import com.example.encrypt.dto.req.DummyQuantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final RedisService redisService;
    @Transactional
    public void addDummy(DummyQuantity quantity) {

        for (int q=1;q<quantity.getQty();q++ ) {
            System.out.println(q);
            redisService.setHashAddress("80001", String.valueOf(UUID.randomUUID()),123L);
            System.out.println("ac");
        }

    }

    @Transactional(readOnly = true)
    public Long checkAddress(AddressCheckDto addressCheckDto) {
        return redisService.checkHashAddress(addressCheckDto.getNetwork(),addressCheckDto.getAddress());
    }
}
