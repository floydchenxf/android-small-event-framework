 package com.floyd.event;

public class EventObject<T> implements FiredEvent {

	/**
	 * 事件发送器
	 */
	protected EventEmitter eventEmitter;

	protected Event event;

	/**
	 * 事件参数
	 */
	protected T args;
	
	/**
	 * 是否取消
	 */
	protected Boolean canceled = Boolean.FALSE;

	public EventObject(EventEmitter source, Event event, T args) {
		this.event = event;
		this.eventEmitter = source;
		this.args = args;
	}

	public EventEmitter getEventEmitter() {
		return eventEmitter;
	}

	public void setEventEmitter(EventEmitter eventEmitter) {
		this.eventEmitter = eventEmitter;
	}

	public T getArgs() {
		return args;
	}

	public void setArgs(T args) {
		this.args = args;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Boolean isCanceled() {
		return this.canceled;
	}
	
	public void cancel() {
		this.canceled = Boolean.TRUE;
	}

}
