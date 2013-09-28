package com.floyd.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.floyd.handler.ErrorObject;

public class EventContext<T> extends EventObject<T> {

	private static final int CODE_ERROR = 0;
	private static final int CODE_SUCCESS = 1;
	private static final int CODE_PROGRESS = 2;

	private static final String TAG = EventContext.class.getSimpleName();

	private static final Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			int type = msg.what;
			CallbackObject callbackObject = (CallbackObject) msg.obj;
			if (callbackObject == null) {
				Log.e(TAG, "msg:" + msg + " callbackobject is null,pls check");
				return;
			}

			EventCallback callback = callbackObject.getCallback();

			if (callback == null) {
				Log.e(TAG, "msg:" + msg + " event callback is null,pls check");
				return;
			}

			Object o = callbackObject.getData();
			if (o == null) {
				Log.e(TAG, "msg:" + msg + " data is null,pls check");
				return;
			}

			if (CODE_ERROR == type) {
				ErrorObject errorObject = (ErrorObject) o;
				callback.onError(errorObject.code, errorObject.message);
			} else if (CODE_SUCCESS == type) {
				callback.onSuccess(o);
			} else if (CODE_PROGRESS == type) {
				callback.onProgress((Integer) o);
			} else {
				Log.e(TAG, "handler type is not match!");
			}
		}

	};
	
	EventContext(EventObject<T> eo) {
		super(eo.eventEmitter, eo.event, eo.args);
	}

	EventContext(EventEmitter ee, Event event, T args) {
		super(ee, event, args);
	}

	public void invokeError(int code, String message) {
		EventDispatch eventDispatch = event.getEventDispatch();
		boolean continueEx = true;
		if (eventDispatch != null) {
			continueEx = eventDispatch.dispatchErrorEvent(eventEmitter, code, args);
		}

		if (continueEx) {
			EventCallback<?> eventCallback = event.getEventCallback();
			if (eventCallback != null) {
				ErrorObject errorObject = new ErrorObject();
				errorObject.code = code;
				errorObject.message = message;
				CallbackObject callbackObject = new CallbackObject(errorObject,
						eventCallback);
				Message msg = new Message();
				msg.what = CODE_ERROR;
				msg.obj = callbackObject;
				handler.sendMessage(msg);
			}
		}
	}

	private void invokeSuccess(Object o) {
		EventDispatch eventDispatch = event.getEventDispatch();
		boolean continueEx = true;
		if (eventDispatch != null) {
			continueEx = eventDispatch.dispatchSuccessEvent(eventEmitter, o);
		}

		if (continueEx) {
			EventCallback<?> eventCallback = event.getEventCallback();
			if (eventCallback != null) {
				CallbackObject callbackObject = new CallbackObject(o,
						eventCallback);
				Message msg = new Message();
				msg.what = CODE_SUCCESS;
				msg.obj = callbackObject;
				handler.sendMessage(msg);
			}
		}

	}

	public void invokeProgress(int progress) {
		EventDispatch eventDispatch = event.getEventDispatch();
		boolean continueEx = true;
		if (eventDispatch != null) {
			continueEx = eventDispatch.dispatchProgressEvent(eventEmitter,progress, args);
		}

		if (continueEx) {
			EventCallback<?> eventCallback = event.getEventCallback();
			if (eventCallback != null) {
				CallbackObject callbackObject = new CallbackObject(progress,
						eventCallback);
				Message msg = new Message();
				msg.what = CODE_PROGRESS;
				msg.obj = callbackObject;
				handler.sendMessage(msg);
			}
		}
	}

	public void invokeNext(Object args) {
		EventHandlerLink<?> ehl = event.getEventHandlerLink();
		if (ehl == null) {
			Log.e(TAG, "event handle link is null, pls check");
		}

		EventHandlerLink<?> ttt = ehl.getNextLink();
		if (ttt == null) {
			invokeSuccess(args);
			return;
		}

		EventCallback<?> eventCallback = event.getEventCallback();
		EventDispatch eventDispatch = event.getEventDispatch();
		Event event = Event.createInstance();
		event.setEventHandlerLink(ttt).setEventCallback(eventCallback)
				.setEventDispatch(eventDispatch);
		eventEmitter.fireEvent(event, args);
	}
}
