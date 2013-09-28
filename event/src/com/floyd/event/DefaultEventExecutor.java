package com.floyd.event;

import java.util.concurrent.ExecutorService;

import android.util.Log;

/**
 * 
 * @author zjutcxf128
 * 
 */
public class DefaultEventExecutor implements EventExecutor {

	private static final String TAG = DefaultEventExecutor.class
			.getSimpleName();

	private ExecutorService executorService;

	public DefaultEventExecutor(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	public void fireEvent(final EventObject<?> eo) {
		final Event e = eo.getEvent();
		if (e == null) {
			Log.e(TAG, "event not set,pls check");
			return;
		}
		
		final EventHandlerLink<?> ehl = e.getEventHandlerLink();
		if (ehl == null) {
			Log.e(TAG, "event not set event handler link for:" + e);
			return;
		}

		if (!executorService.isShutdown()) {
			executorService.submit(new Runnable() {

				@Override
				public void run() {
					EventContext<?> eventContext = new EventContext(eo);
					EventHandler eh = ehl.getEventHandler();
					if (eh == null) {
						return;
					}
					eh.handler(eventContext, eo.getArgs());
				}
			});
		}
	}

	@Override
	public void destroy() {
		executorService.shutdownNow();
	}

}
