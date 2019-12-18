package com.newcoin.api.demo.utils;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    /***
     * HmacSha256签名,转换成字符串
     *
     * @param message  "verb + path + expires + data"
     * @param apiKey 秘钥
     * @return String
     */
    public static String getHmacHashForSignature(String apiKey, String message) throws Exception {
        String hash;
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec apiKeySpec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(apiKeySpec);
        hash = byteArrayToHexString(hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        return hash;
    }
    

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}
