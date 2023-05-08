package com.example.encrypt.config;

import com.example.encrypt.response.Code;
import com.example.encrypt.response.exception.CustomException;
import com.example.encrypt.service.RedisService;
import com.example.encrypt.util.AESUtil;
import com.example.encrypt.util.RSAUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GetRequestDecryptWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private Map<String, String[]> params = new HashMap<>();
//    private byte[] rawData;

    public GetRequestDecryptWrapper(HttpServletRequest request, RedisService redisService) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        super(request);
        String charEncoding = request.getCharacterEncoding();

        this.encoding = ObjectUtils.isEmpty(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);

        try {
            if (ObjectUtils.isEmpty(request.getParameterMap())) {
                return;
            }
            String encryptParams = request.getParameterNames().nextElement();
            String publicKey = request.getHeader("publicKey");
            String normalKey = request.getHeader("normalKey");
            String privateKeyBase64 = redisService.getStringValues(publicKey);
            PrivateKey privateKey = RSAUtil.getPrivateKeyFromBase64Encrypted(privateKeyBase64);


            String aesKey = RSAUtil.decryptRSA(normalKey, privateKey);

            AESUtil aesUtil = new AESUtil();


            String descryptParamString = aesUtil.decryptParam(new String(encryptParams.getBytes(), StandardCharsets.UTF_8), aesKey);
            ObjectMapper mapper = new ObjectMapper();
            Object params = mapper.readValue(descryptParamString, Object.class);
            Map<String, Object> map = mapper.convertValue(params, Map.class);

            map.forEach((s, v) -> {
                String[] c = {(String) v};
                this.params.put(s, c);
            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            throw new CustomException(Code.NOT_FOUND_RSA_PRIVATE_KEY);
        }
    }

    //    @Override
//    public ServletInputStream getInputStream() {
//        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodingParam == null ? "".getBytes(encoding) : decodingParam.getBytes(encoding));
//        return new ServletInputStream() {
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//
//            @Override
//            public void setReadListener(ReadListener listener) {
//            }
//
//            @Override
//            public int read() {
//                return byteArrayInputStream.read();
//            }
//        };
//    }
    public String getParameter(String name) {
        String returnValue = null;
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            returnValue = paramArray[0];
        }
        return returnValue;
    }

    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] temp = (String[]) params.get(name);
        if (temp != null) {
            result = new String[temp.length];
            System.arraycopy(temp, 0, result, 0, temp.length);
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public Map getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    @SuppressWarnings("unchecked")
    public Enumeration getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    public Map<String, String[]> getParams() {
        return this.params;
    }

    public void setParameter(String name, String value) {
        String[] oneParam = {value};
        setParameter(name, oneParam);
    }

    public void setParameter(String name, String[] value) {
        params.put(name, value);
    }
}