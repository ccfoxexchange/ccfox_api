package com.newcoin.api.demo.service.impl;

import com.newcoin.api.demo.utils.EncryptUtil;

/**
 * 
 * @ClassName: SignatureServiceImpl
 * @date: 2019年12月18日
 * @author shen
 * 
 * @desc 签名接口实现
 */
public class SignatureServiceImpl {

	 /**
     * 券商签名验证
     * 检查签名，如合法，则返回userApiKeyPo
     * @param apiKey: 秘钥
     * @param expires: 过期时间戳o
     * @param path 路径
     * @param data "{'s': 123}"
     * @param verb GET|POST|DELETE|PUT
     */
	public String signature(String apiKey, String verb, String path, long expires, String data) {
		try {
			return EncryptUtil.getHmacHashForSignature(apiKey, verb + path + expires + data);
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException(e);
		}
	}

}
