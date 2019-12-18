package com.newcoin.api.demo.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;


public class HttpUtil {

	public static String sendPost(String url, String requestBody, String signature, String apiKey, Long apiExpires) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// 添加请求头
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept-Charset", "charset=utf-8");
			con.setRequestProperty("signature", signature);
			con.setRequestProperty("apiKey", apiKey);
			con.setRequestProperty("apiExpires", apiExpires.toString());
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// 发送Post请求
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(requestBody);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

			String inputLine = "";
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 */
	public static String get(String url, String signature, String apiKey, Long apiExpires) {
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("signature", signature);
			connection.setRequestProperty("apiKey", apiKey);
			connection.setRequestProperty("apiExpires", apiExpires.toString());
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream(), StandardCharsets.UTF_8 ));
			return IOUtils.toString( in );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
