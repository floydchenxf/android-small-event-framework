package com.floyd.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpStatus;

public class HttpRequestPost implements Request {

	private String url;
	private Map<String, String> params;
	private HttpCallback callback;
	private int count = 0;

	public HttpRequestPost(String url, Map<String, String> params, HttpCallback callback) {
		this.url = url;
		this.callback = callback;
		this.params = params;
	}

	public byte[] request() {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("POST");
            conn.connect();
            
			if (params != null && params.size() > 0) {
				os = conn.getOutputStream();
				StringBuilder sb = new StringBuilder();
				int nn = 0;
				for (Map.Entry<String, String> ent : this.params.entrySet()) {
					if (++nn < params.size()) {
						sb.append(ent.getKey()).append("=").append(URLEncoder.encode(ent.getValue())).append("&");
					} else {
						sb.append(ent.getKey()).append("=").append(URLEncoder.encode(ent.getValue()));
					}
				}
				String output = sb.toString();
				os.write(output.getBytes(),0, output.length());
				os.flush();
			}
			
			int code = conn.getResponseCode();

			boolean isError = true;
			if (code == HttpStatus.SC_OK) {
				is = conn.getInputStream();
				isError = false;
			} else {
				is = conn.getErrorStream();
				isError = true;
			}

			int len = 0;
			byte[] tmp = new byte[1024];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			while ((len = is.read(tmp)) != -1) {
				byteArrayOutputStream.write(tmp, 0, len);
			}

			byte[] response = byteArrayOutputStream.toByteArray();

			if (isError) {
				if (callback != null) {
					callback.onError(code, new String(response));
				}
			} else {
				if (callback != null) {
					callback.onSuccess(response);
				}
			}

			return response;
		} catch (SocketTimeoutException socketTimeoutException) {
			if (count++ < 3) {
				request();
			} else {
				if (callback != null) {
					callback.onError(444, "重试失败");
				}
			}
			return null;
		} catch (Exception e) {
			if (callback != null) {
				callback.onError(999, e.toString());
			}
			return null;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
	}

}
