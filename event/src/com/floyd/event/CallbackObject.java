package com.floyd.event;

/**
 * 回调对象
 * 
 * @author zjutcxf128
 * 
 */
public class CallbackObject {

	private Object data;
	private EventCallback<?> callback;

	public CallbackObject(Object data, EventCallback<?> callback) {
		this.data = data;
		this.callback = callback;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public EventCallback<?> getCallback() {
		return callback;
	}

	public void setCallback(EventCallback<?> callback) {
		this.callback = callback;
	}
}
