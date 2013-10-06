package com.floyd.request;

public class HttpErrorInfo {

	public int code;
	public String message;

	private HttpErrorInfo(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 网络不可用
	 */
	public static final HttpErrorInfo NETWORK_INVALIDABLE_ERROR = new HttpErrorInfo(999, "亲,网络不给力!");


	/**
	 * 连接超时
	 */
	public static final HttpErrorInfo SOCKET_TIMEOUT_ERROR = new HttpErrorInfo(998, "亲,连接超时了!");

	/**
	 * 读取超时
	 */
	public static final HttpErrorInfo READ_IMEOUT_ERROR = new HttpErrorInfo(997, "亲,读取内容超时了!");

	/**
	 * 未知错误
	 */
	public static final HttpErrorInfo UNKNOW_ERROR = new HttpErrorInfo(1000, "亲,前面出现点麻烦!");
	
	/**
	 * 获取内容出错
	 */
	public static final HttpErrorInfo PARSE_CONTENT_ERROR = new HttpErrorInfo(996, "亲, 内容获取出错了!");

}
