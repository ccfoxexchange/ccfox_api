package com.newcoin.api.demo.utils;

import java.net.URLEncoder;

public class URLEncoderUtil {
	
	public static String urlDecoderEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException(e);
		}
	}
}
