package com.example.encrypt.response.exception;

import com.example.encrypt.response.Code;
import com.example.encrypt.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

import static com.example.encrypt.response.Code.TEMPORARY_SERVER_ERROR;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value =  Exception.class )
    public ResponseEntity<?> handleApiRequestException(Exception ex) {
        ResponseDto<?> responseDto = new ResponseDto<>(TEMPORARY_SERVER_ERROR, null);
        responseDto.getStatus().setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(responseDto);
    }

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        if (e.getData()==null){
            ResponseDto<Object> responseDto = new ResponseDto<>(e.getCode(), null);

            return ResponseEntity
                    .status(e.getCode().getHttpStatus())
                    .body(responseDto);
        }else {
            ResponseDto<Object> responseDto = new ResponseDto<>(e.getCode(), e.getData());

            return ResponseEntity
                    .status(e.getCode().getHttpStatus())
                    .body(responseDto);
        }


    }
    @ExceptionHandler(value = CustomNonRollbackException.class)
    public ResponseEntity<?> handleCustomNonRollbackException(CustomNonRollbackException e) {
        if (e.getData()==null){
            ResponseDto<Object> responseDto = new ResponseDto<>(e.getCode(), null);

            return ResponseEntity
                    .status(e.getCode().getHttpStatus())
                    .body(responseDto);
        }else {
            ResponseDto<Object> responseDto = new ResponseDto<>(e.getCode(), e.getData());

            return ResponseEntity
                    .status(e.getCode().getHttpStatus())
                    .body(responseDto);
        }


    }




    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        ResponseDto<?> responseDto = new ResponseDto<>(Code.VALIDATION_ERROR, null);
        responseDto.getStatus().setMessage(Objects.requireNonNull(e.getFieldError()).getField()+"은(는) "+e.getFieldError().getDefaultMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MethodNotAllowedException.class})
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(Exception e) {
        ResponseDto<?> responseDto = new ResponseDto<>(Code.BAD_REQUEST, null);
        responseDto.getStatus().setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    //없는 API 호출
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException e) {
        ResponseDto<?> responseDto = new ResponseDto<>(Code.NOT_FOUND_API, null);
        responseDto.getStatus().setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }
}