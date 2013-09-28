package com.floyd.event;

import java.util.LinkedList;

import android.util.Log;

/**
 * 事件分配管理器，主要负责分配事件给线程池执行
 * 
 * @author zjutcxf128
 * 
 */
public class EventTaskManager extends Thread {

	private static final String TAG = EventTaskManager.class.getSimpleName();

	private static EventTaskManager instance = new EventTaskManager();

	private EventEmitterConfiguration configuration;

	private boolean stop;

	private EventTaskManager() {
	}

	public void initConfiguration(EventEmitterConfiguration configuration) {
		this.configuration = configuration;
	}

	private static final LinkedList<EventObject> eventObjectQueue = new LinkedList<EventObject>();

	public static EventTaskManager getInstance() {
		return instance;
	}

	public void addTask(EventObject e) {
		if (!stop) {
			synchronized (eventObjectQueue) {
				eventObjectQueue.add(e);
				eventObjectQueue.notify();
			}
		}
	}

	@Override
	public void run() {
		while (!this.isInterrupted() && !stop) {
			synchronized (eventObjectQueue) {
				if (eventObjectQueue.isEmpty()) {
					try {
						eventObjectQueue.wait();
					} catch (InterruptedException e) {
						Log.e(TAG, e.toString());
					}
				}

				EventObject<Object> eventObject = eventObjectQueue.poll();
				if (eventObject == null) {
					continue;
				}

				if (configuration.multiExecutor != null) {
					configuration.multiExecutor.fireEvent(eventObject);
				}

			}
		}
	}

	public void start() {
		new Thread(this).start();
	}

	public void cancel() {
		stop = true;
		synchronized (eventObjectQueue) {
			eventObjectQueue.notify();
		}
		this.interrupt();
	}

	public void destory() {
		this.cancel();
		instance = null;
	}

	public void printQueueObjects() {
		synchronized (eventObjectQueue) {
			if (!eventObjectQueue.isEmpty()) {
				Log.d(TAG, "event queue has size:" + eventObjectQueue.size());
				for (EventObject eventObject : eventObjectQueue) {
					StringBuffer sb = new StringBuffer();
					sb.append("event handler:");
					sb.append(eventObject.getEvent().getEventHandlerLink()
							.getEventHandler());
					sb.append(" args:");
					Object args = eventObject.getArgs();
					if (args != null) {
						sb.append(args);
					}
					Log.d(TAG, sb.toString());
				}
			} else {
				Log.d(TAG, "event queue is empty!");
			}
		}
	}

}
