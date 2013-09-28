package com.floyd.handler;

import com.floyd.event.EventContext;
import com.floyd.event.EventHandler;

public abstract class AbstractConvertObjEventHandler<T, E> implements
		EventHandler<E> {

	@Override
	public void handler(EventContext<E> eventContext, E e) {
		T t = convertObj(eventContext, e);
		if (t != null) {
			eventContext.invokeNext(t);
		}
	}

	public abstract T convertObj(EventContext<E> eventContext, E args);
}
