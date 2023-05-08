package com.example.encrypt.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum Code {

    //2xx

    SUCCESS                     (HttpStatus.OK, "E20000","Success"),
    CREATED                     (HttpStatus.CREATED, "E20001","Create Success"),
    SUCCESS_BUT_NO_DATA         (HttpStatus.NO_CONTENT,"E20002", "Success, But no data"),
    LOGIN_SUCCESS               (HttpStatus.OK,"E20003","Access Token 발행"),

    // 3xx Redirection
    MOVED_TEMPORARILY           (HttpStatus.FOUND, "E30201","마이페이지로 이동해주세요"),
    // 400 Bad Request
    BAD_REQUEST                 (HttpStatus.BAD_REQUEST, "E40000","올바른 요청이 아닙니다."),
    WRONG_USER                  (HttpStatus.BAD_REQUEST, "E40001", "올바른 유저가 아닙니다."),
    NULL_CONTENT                (HttpStatus.BAD_REQUEST, "E40002","내용을 입력해 주세요"),
    TEMPORARY_SERVER_ERROR      (HttpStatus.BAD_REQUEST, "E40003", "잘못된 요청입니다."),
    EXIST_USER                 (HttpStatus.BAD_REQUEST, "E40004", "이미 존재하는 회원입니다."),
    VALIDATION_ERROR            (HttpStatus.BAD_REQUEST, "E40005", "올바른 형식을 입력해주세요."),
    PASSWORD_UNMATCHED          (HttpStatus.BAD_REQUEST, "E40006", "비밀번호가 틀렸습니다."),
    NOT_LOGIN                   (HttpStatus.BAD_REQUEST, "E40007", "로그인이 필요한 서비스입니다."),
    NOT_EXISTS_USERNAME         (HttpStatus.BAD_REQUEST, "E40008", "존재하지 않는 아이디입니다."),
    PASSWORD_VALID_ERROR        (HttpStatus.BAD_REQUEST, "E40009", "비밀번호와 비밀번호 확인이 맞지 않습니다."),


    //401 Authentication
    EXPIRED_ACCESS_JWT          (HttpStatus.BAD_REQUEST,"E40101","JWT ACCESS 시간이 만료되었습니다."),
    AUTHENTICATION_FAILURE_JWT  (HttpStatus.BAD_REQUEST,"E40102","올바른 JWT 정보가 아닙니다."),
    EXPIRED_REFRESH_JWT         (HttpStatus.BAD_REQUEST,"E40103","JWT REFRESH 시간이 만료되었습니다."),

    //402 Payment Required (결제 시스템에 사용하기 위해 만들어졌지만 지금은 사용되고 있지 않다.)

    //403 Authorization
    PENALTY_USER                (HttpStatus.BAD_REQUEST,"E40301","패널티 유저입니다."),
    NO_AUTHORIZATION            (HttpStatus.BAD_REQUEST, "E40302", "권한이 없습니다"),
    NO_AUTHENTICATION            (HttpStatus.BAD_REQUEST, "E40303", "인증 실패"),
    BLOCKED_USER                (HttpStatus.BAD_REQUEST,"E40304","차단된 유저입니다."),

    // 404 Not Found
    NOT_FOUND_USER              (HttpStatus.BAD_REQUEST, "E40404", "유저를 찾지 못했습니다."),
    NOT_FOUND_TOKEN             (HttpStatus.BAD_REQUEST, "E40405","토큰을 찾지 못했습니다."),
    NOT_FOUND_API               (HttpStatus.BAD_REQUEST,"E40406","없는 API 입니다."),
    NOT_FOUND_RSA_PRIVATE_KEY   (HttpStatus.BAD_REQUEST,"E40407","PRIVATE KEY NOT FOUND"),
    // 415 Media file
    WRONG_FILE_TYPE             (HttpStatus.BAD_REQUEST,"E41501", "잘못된 형식의 파일입니다."),
    FAIL_FILE_UPLOAD            (HttpStatus.BAD_REQUEST,"E41502", "파일 업로드에 실패하였습니다.")
//    FAIL_FILE_UPLOAD            (HttpStatus.BAD_REQUEST,"E41502", "파일 업로드에 실패하였습니다.")


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}