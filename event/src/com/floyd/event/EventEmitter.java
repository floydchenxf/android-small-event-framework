package com.floyd.event;

public interface EventEmitter {
	
	/**
	 * 注册事件
	 * @param event
	 */
	void regEvent(String eventName, Event event);

	/**
	 * 带注册的触发事件
	 * 
	 * @param args
	 */
	<T> FiredEvent fireEvent(String eventName, T args);

	/**
	 * 不需要注册直接触发事件
	 * 
	 * @param eventName
	 * @param args
	 */
	<T> FiredEvent fireEvent(Event event, T args);
	
	/**
	 * 为保持eventobject唯一(能够取消事件),特意加的触发事件
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
