package com.example.encrypt.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusCode { // http 본문에 담아서 보내줄 형태

    private String code;
    private String message;


    public StatusCode(Code code){
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}