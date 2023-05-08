package com.example.encrypt.response.exception;

import com.example.encrypt.response.Code;
import lombok.Getter;
import reactor.util.annotation.Nullable;

@Getter
public class CustomNonRollbackException extends RuntimeException{
    private final Code code;
    @Nullable
    private final Object data;

    public CustomNonRollbackException(Code code) {
        super(code.getMessage());
        this.code = code;
        this.data = null;
    }

    public CustomNonRollbackException(Code code, Object data) {
        super(code.getMessage());
        this.code = code;
        this.data = data;
    }


}
