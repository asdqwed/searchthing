package com.seths.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientUtil {
	
	private static OkHttpClient client = new OkHttpClient();
	public static final MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");
	private static final MediaType MEDIA_TYPE_URLENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
	private static Logger log = LoggerFactory.getLogger(OkHttpClientUtil.class);
	
	public static String doGet(String url) {
		try {
			Request request = new Request.Builder().url(url).build();
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}
	
	public static String doGetUTF(String url) {
		try {
			Request request = new Request.Builder().url(url).build();
			try (Response response = client.newCall(request).execute()) {
				String str = new String(response.body().bytes(),"UTF-8");
				return str;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	public static String doGetPic(String url) {
		try {
			Request request = new Request.Builder().url(url).build();
			try (Response response = client.newCall(request).execute()) {
				byte[] bytes = response.body().bytes();
				File file = new File("1.jpg");
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(bytes);
				fops.flush();
				fops.close();
				String str = new String(response.body().bytes(), "UTF-8");
				return str;
			}
		} catch (Exception e) {

			return "";
		}
	}
	
	public static String doPost(String url, Map<String, String> params) throws Exception {
		StringBuilder tempParams = new StringBuilder();
		int pos = 0;
		for (String key : params.keySet()) {
			if (pos > 0) {
				tempParams.append("&");
			}
			tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
			pos++;
		}
		RequestBody body = RequestBody.create(MEDIA_TYPE_URLENCODED, tempParams.toString());

		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();

	}
}
