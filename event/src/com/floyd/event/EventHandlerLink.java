package com.floyd.event;

/**
 * handler链
 * 
 * @author zjutcxf128
 * 
 * @param <T>
 * @param <E>
 */
public class EventHandlerLink<E> {

	private EventHandler<E> eventHandler;

	private EventHandlerLink<E> next;

	public EventHandler<E> getEventHandler() {
		return eventHandler;
	}

	public EventHandlerLink<E> getNextLink() {
		return next;
	}

	public EventHandlerLink<E> setNextHandler(EventHandler<E> eh) {
		if (this.eventHandler == null) {
			this.eventHandler = eh;
		} else {
			EventHandlerLink<E> thl = this;
			int i = 0;
			while (true) {
				if (i > 50) {
					throw new IllegalArgumentException(
							"set next handler over number 50, pls check");
				}

				if (thl.next == null) {
					EventHandlerLink<E> eee = new EventHandlerLink<E>();
					eee.eventHandler = eh;
					thl.next = eee;
					break;
				}
				thl = thl.next;
				i++;
			}
		}

		return this;
	}

}
