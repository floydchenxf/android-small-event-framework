package com.floyd.request;

public interface HttpCallback {

	/**
	 * 出错
	 * 
	 * @param code
	 * @param message
	 */
	void onError(int code, String message);

	/**
	 * 成功
	 * 
	 * @param obj
	 */
	void onSuccess(Object obj);

	/**
	 * 进度
	 * 
	 * @param i
	 */
	void onProgress(int i);

}
