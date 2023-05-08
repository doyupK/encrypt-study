package com.example.encrypt.config;

import com.example.encrypt.response.Code;
import com.example.encrypt.response.exception.CustomException;
import com.example.encrypt.service.RedisService;
import com.example.encrypt.util.AESUtil;
import com.example.encrypt.util.RSAUtil;
import org.springframework.util.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class PostRequestDecryptWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private String decodingBody;
    private byte[] rawData;

    public PostRequestDecryptWrapper(HttpServletRequest request, RedisService redisService) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        super(request);
        String charEncoding = request.getCharacterEncoding();

        this.encoding = ObjectUtils.isEmpty(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            InputStream inputStream = request.getInputStream();
            rawData = inputStream.readAllBytes();

            if (ObjectUtils.isEmpty(rawData)) {
                return;
            }
            String publicKey = request.getHeader("publicKey");
            String normalKey = request.getHeader("normalKey");
            String privateKeyBase64 = redisService.getStringValues(publicKey);
            PrivateKey privateKey = RSAUtil.getPrivateKeyFromBase64Encrypted(privateKeyBase64);

            String aesKey = RSAUtil.decryptRSA(normalKey,privateKey);

            AESUtil aesUtil = new AESUtil();

            this.decodingBody = aesUtil.decryptPayload(new String(rawData, StandardCharsets.UTF_8), aesKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            throw new CustomException(Code.NOT_FOUND_RSA_PRIVATE_KEY);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodingBody == null ? "".getBytes(encoding) : decodingBody.getBytes(encoding));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}