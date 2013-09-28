package com.floyd.event;

import com.floyd.dispatch.DefaultEventDispatch;

/**
 * 注册事件对象
 * 
 * @author zjutcxf128
 * 
 */
public class Event {

	/**
	 * 事件处理器链
	 */
	private EventHandlerLink eventLink;

	/**
	 * 事件回调
	 */
	private EventCallback<?> eventCallback;

	/**
	 * 决定事件分发
	 */
	private EventDispatch eventDispatch = new DefaultEventDispatch();

	private Event() {

	}

	public Event setEventHandlerLink(EventHandlerLink<?> link) {
		this.eventLink = link;
		return this;
	}

	public static Event createInstance() {
		return new Event();
	}

	public Event setNextHandler(EventHandler<?> eventHandle) {
		if (eventLink == null) {
			eventLink = new EventHandlerLink<Object>();
		}

		eventLink.setNextHandler(eventHandle);
		return this;
	}

	public Event setEventCallback(EventCallback<?> eventCallback) {
		this.eventCallback = eventCallback;
		return this;
	}

	public Event setEventDispatch(EventDispatch dispatch) {
		this.eventDispatch = dispatch;
		return this;
	}

	public EventHandlerLink<?> getEventHandlerLink() {
		return eventLink;
	}

	public EventCallback<?> getEventCallback() {
		return eventCallback;
	}

	public EventDispatch getEventDispatch() {
		return eventDispatch;
	}

}
