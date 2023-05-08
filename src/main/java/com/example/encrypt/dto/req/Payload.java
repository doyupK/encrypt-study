package com.example.encrypt.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {
    private String keyword;
    private String publicKey;
}
