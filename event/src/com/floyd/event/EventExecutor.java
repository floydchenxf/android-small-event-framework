package com.floyd.event;

public interface EventExecutor {

	/**
	 * 触发事件
	 * 
	 * @param e
	 */
	void fireEvent(EventObject<?> e);

	/**
	 * 销毁
	 */
	void destroy();

}
