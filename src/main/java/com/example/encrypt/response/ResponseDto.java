package com.example.encrypt.response;

import lombok.Getter;
import lombok.Setter;
import reactor.util.annotation.Nullable;


@Getter
@Setter
public class ResponseDto<T> {
    StatusCode status;
    @Nullable
    T data;

    public ResponseDto(Code code, T data) {
        this.status = new StatusCode(code);
        this.data = data;
    }
    public ResponseDto(Code code){
        this.status = new StatusCode(code);
    }

}
