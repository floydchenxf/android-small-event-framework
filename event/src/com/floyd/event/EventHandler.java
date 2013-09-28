package com.floyd.event;

/**
 * 事件处理类
 * 
 * @author zjutcxf128
 * 
 * @param <T>
 */
public interface EventHandler<E> {

	/**
	 * 处理事件
	 * 
	 * @param eventObject
	 * @return
	 */
	void handler(EventContext<E> eventContext, E args);

}
