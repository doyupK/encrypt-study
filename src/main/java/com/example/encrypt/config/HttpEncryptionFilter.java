package com.example.encrypt.config;

import com.example.encrypt.response.ResponseDto;
import com.example.encrypt.response.exception.CustomException;
import com.example.encrypt.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter("/*")
@Component
@Slf4j
public class HttpEncryptionFilter implements Filter {

    private final RedisService redisService;

    @Autowired
    public HttpEncryptionFilter(RedisService redisService) {
        this.redisService = redisService;
    }


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        ResponseEncryptWrapper responseEncryptWrapper = new ResponseEncryptWrapper(httpServletResponse);
        String method = httpServletRequest.getMethod();

        try {
            if (httpServletRequest.getRequestURI().equals("/test/encryptPayload")) {
                chain.doFilter(httpServletRequest, responseEncryptWrapper);
                httpServletResponse.getOutputStream().write(responseEncryptWrapper.encryptResponse());
            }
            // 첫 key 생성 요청인 경우
            else if (httpServletRequest.getRequestURI().equals("/test2/create/key")){
                chain.doFilter(httpServletRequest, httpServletResponse);

            }
            // post 요청인 경우
            else if (method.equals("POST")){
                PostRequestDecryptWrapper postRequestDecryptWrapper = new PostRequestDecryptWrapper(httpServletRequest, redisService);
                chain.doFilter(postRequestDecryptWrapper, responseEncryptWrapper);
                httpServletResponse.getOutputStream().write(responseEncryptWrapper.encryptResponse());

            //Get 요청인 경우
            }else if (method.equals("GET")){
                request = new GetRequestDecryptWrapper(httpServletRequest, redisService);
                chain.doFilter(request, responseEncryptWrapper);
                httpServletResponse.getOutputStream().write(responseEncryptWrapper.encryptResponse());

            }else {
                chain.doFilter(request,responseEncryptWrapper);
                httpServletResponse.getOutputStream().write(responseEncryptWrapper.encryptResponse());
            }

        }catch (CustomException e){
            handleInvalidCorrelationId(responseEncryptWrapper, e);
        }






    }
    private void handleInvalidCorrelationId(ResponseEncryptWrapper response, CustomException e) throws IOException {
        ResponseDto<Object> responseDto = new ResponseDto<>(e.getCode(), e.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
        response.getOutputStream().write(response.encryptResponse());
    }

}
