package com.floyd.request;

import java.util.Map;

/**
 * request 工厂类
 * 
 * @author zjutcxf128
 * 
 */
public class RequestFactory {

	/**
	 * 产生request
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param callback
	 * @return
	 */
	public static Request getRequest(RequestMethod method, String url,
			Map<String, String> params, HttpCallback callback) {
		Request request = null;
		if (method == RequestMethod.GET) {
			request = new HttpRequestGet(url, params, callback);
		} else if (method == RequestMethod.POST) {
			request = new HttpRequestPost(url, params, callback);
		} else if (method == RequestMethod.UPLOAD) {
			request = new FileUpload(url, params, callback);
		} else {
			throw new IllegalArgumentException("not fit method");
		}
		return request;
	}

}
