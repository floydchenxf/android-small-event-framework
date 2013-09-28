package com.floyd.event;

/**
 * 回调函数，如果是主UI线程回调将处理页面显示逻辑。如果非主UI线程处理。主要负责分支的调整处理
 * 
 * @author zjutcxf128
 * 
 * @param <T>
 */
public interface EventCallback<T> {

	/**
	 * 错误处理
	 * 
	 * @param code
	 * @param message
	 */
	void onError(int code, String message);

	/**
	 * 成功处理
	 * 
	 * @param t
	 */
	void onSuccess(T t);

	/**
	 * 进度处理
	 * 
	 * @param i
	 */
	void onProgress(int i);
}
