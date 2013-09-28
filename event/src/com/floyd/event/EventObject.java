 package com.floyd.event;

public class EventObject<T> {

	/**
	 * 事件发送器
	 */
	protected EventEmitter eventEmitter;

	protected Event event;

	/**
	 * 事件参数
	 */
	protected T args;

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

}
