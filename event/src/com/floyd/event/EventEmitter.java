package com.floyd.event;

public interface EventEmitter {
	
	/**
	 * 注册事件
	 * @param event
	 */
	void regEvent(String eventName, Event event);

	/**
	 * 触发事件
	 * 
	 * @param args
	 */
	<T> FiredEvent fireEvent(String eventName, T args);

	/**
	 * 触发事件
	 * 
	 * @param eventName
	 * @param args
	 */
	<T> FiredEvent fireEvent(Event event, T args);
	
	/**
	 * 触发事件
	 * @param eventObject
	 * @return
	 */
	<T> FiredEvent fireEvent(EventObject<T> eventObject);

	/**
	 * 初始化事件发送器
	 * 
	 * @param configuration
	 */
	void initEventEmitter(EventEmitterConfiguration configuration);

	/**
	 * 回收相应资源
	 */
	void destory();
}
