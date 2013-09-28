package com.floyd.dispatch;

import com.floyd.event.EventDispatch;
import com.floyd.event.EventEmitter;

public class DefaultEventDispatch implements EventDispatch {

	@Override
	public <T> boolean dispatchErrorEvent(EventEmitter eventEmitter, int code,
			T args) {
		return true;
	}

	@Override
	public <T> boolean dispatchSuccessEvent(EventEmitter eventEmitter, T e) {
		return true;
	}

	@Override
	public <T> boolean dispatchProgressEvent(EventEmitter eventEmitter, int i,
			T args) {
		return true;
	}

}
