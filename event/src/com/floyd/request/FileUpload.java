package com.floyd.request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传
 * 
 * @author cxf128
 * 
 */
public class FileUpload implements Request {

	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间

	private static final String CHARSET = "utf-8"; // 设置编码

	private String imageUrl;

	private File file;

	private Map<String, String> params;

	private HttpCallback callback;

	private byte[] response;

	private int timeCount = 0;
	
	private static final Map<String, String> contentTypeMap = new HashMap<String, String>();
	static {
		contentTypeMap.put(null, "image/jpep");
		contentTypeMap.put("jpg", "image/jpep");
		contentTypeMap.put("JPG", "image/jpep");
		contentTypeMap.put("jpeg", "image/jpep");
		contentTypeMap.put("JPEG", "image/jpep");
		contentTypeMap.put("png", "image/png");
		contentTypeMap.put("PNG", "image/png");
		contentTypeMap.put("amr", "application/octet-stream");
		contentTypeMap.put("AMR", "application/octet-stream");
		contentTypeMap.put("zip", "application/zip");
		contentTypeMap.put("ZIP", "application/zip");
		contentTypeMap.put("rar", "application/zip");
		contentTypeMap.put("RAR", "application/zip");
	}

	public FileUpload(String imageUrl, Map<String, String> params,
			HttpCallback callback) {
		this.imageUrl = imageUrl;
		this.params = params;
		this.callback = callback;
		if (params == null || params.get("file") == null) {
			throw new IllegalArgumentException("file path not include");
		}

		String filePath = params.get("file");
		file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("no file exists, pls check!");
		}
	}

	public byte[] request() {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;

		try {
			URL url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不允许使用缓冲
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary= " + BOUNDARY);
			conn.connect();
			os = conn.getOutputStream();
			StringBuffer sb = new StringBuffer();
			if (this.params != null && this.params.size() > 0) {
				for (Map.Entry<String, String> ent : this.params.entrySet()) {
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"")
					  .append(ent.getKey()).append("\"").append(LINE_END)
					  .append(LINE_END);
					sb.append(ent.getValue()).append(LINE_END);
				}
			}

			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"file\"; filename=\""
					+ file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:" + getContextType(file)).append(LINE_END);
			sb.append(LINE_END);

			os.write(sb.toString().getBytes());

			if (callback != null) {
				callback.onProgress(0);
			}

			long totalSize = file.length();

			InputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			int curLen = 0;
			while ((len = fis.read(bytes)) != -1) {
				os.write(bytes, 0, len);
				curLen += len;
				if (callback != null) {
					callback.onProgress((int) (curLen / totalSize));
				}
			}

			fis.close();

			os.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END + LINE_END).getBytes();
			os.write(end_data);
			os.flush();

			int res = conn.getResponseCode();
			if (res == 200) {
				is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] tmp = new byte[1024];
				int t = 0;
				while ((t = is.read(tmp)) != -1) {
					bos.write(tmp, 0, t);
				}
				response = bos.toByteArray();
				if (callback != null) {
					callback.onSuccess(response);
				}
			} else {
				is = conn.getErrorStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] tmp = new byte[1024];
				int t = 0;
				while ((t = is.read(tmp)) != -1) {
					bos.write(tmp, 0, t);
				}
				response = bos.toByteArray();
				if (callback != null) {
					callback.onError(res, new String(response));
				}
			}

			return response;
		} catch (SocketTimeoutException socketTimeoutException) {
			if (timeCount < 3) {
				this.request();
				timeCount++;
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
	
	protected String getContextType(File file) {
		String fileName = file.getName();
		int filePostfix = fileName.lastIndexOf(".");
		if (filePostfix == -1) {
			return contentTypeMap.get(null);
		}

		String postfix = fileName.substring(filePostfix + 1);
		return contentTypeMap.get(postfix);
	}
}
