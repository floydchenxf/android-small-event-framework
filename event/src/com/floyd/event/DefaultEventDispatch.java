package com.floyd.event;

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
