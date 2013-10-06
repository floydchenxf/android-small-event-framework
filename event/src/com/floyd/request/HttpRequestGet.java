package com.floyd.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpStatus;


public class HttpRequestGet implements Request {

	private int count;
	private String url;
	private Map<String, String> params;
	private HttpCallback callback;

	public HttpRequestGet(String url, Map<String, String> params, HttpCallback callback) {
		this.url = url;
		this.params = params;
		this.callback = callback;
	}

	public byte[] request() {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			StringBuilder totalUrl = new StringBuilder(url);
			if (params != null && params.size() > 0) {
				int inn = url.indexOf("\\?");
				if (inn == -1) {
					totalUrl.append("?");
				} else {
					totalUrl.append("&");
				}

				int size = params.size();
				int i = 0;
				for (Map.Entry<String, String> ent : this.params.entrySet()) {
					if (++i < size) {
						totalUrl.append(ent.getKey()).append("=").append(ent.getValue()).append("&");
					} else {
						totalUrl.append(ent.getKey()).append("=").append(ent.getValue());
					}
				}
			}

			URL httpUrl = new URL(totalUrl.toString());
			conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestMethod("GET");
			conn.connect();

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
					callback.onError(HttpErrorInfo.SOCKET_TIMEOUT_ERROR.code, HttpErrorInfo.SOCKET_TIMEOUT_ERROR.message);
				}
			}
			return null;
		} catch (Exception e) {
			if (callback != null) {
				callback.onError(HttpErrorInfo.UNKNOW_ERROR.code, HttpErrorInfo.UNKNOW_ERROR.message);
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
