package com.floyd.event;

/**
 * 决定事件流程
 * 
 * @author cxf128
 * 
 */
public interface EventDispatch {

	/**
	 * 决定错误是否分发
	 * 
	 * @param code 错误code
	 * @param T 执行时的参数
	 * @return true 分发给callback执行onError, false不执行callback
	 */
	<T> boolean dispatchErrorEvent(EventEmitter eventEmitter, int code, T args);

	/**
	 * 决定成功后是否分发
	 * 
	 * @param eventEmitter 事件触发器
	 * @param e 成功后的对象
	 * @return true 分发给callback执行onSuccess, false不执行callback
	 */
	<T> boolean dispatchSuccessEvent(EventEmitter eventEmitter, T e);

	/**
	 * 决定进度事件是否分发
	 * 
	 * @param eventEmitter
	 * @param i
	 * @param args 执行进度时的参数
	 * @return true 分发给callback执行onProcess, false不执行callback
	 */
	<T> boolean dispatchProgressEvent(EventEmitter eventEmitter, int i, T args);

}
