package com.floyd.event;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author zjutcxf128
 * 
 */
public class TaskEventEmitter implements EventEmitter {

	private static final String TAG = TaskEventEmitter.class.getSimpleName();

	private EventEmitterConfiguration eventEmitterConfiguration;
	
	private Map<String, Event> handlers = new HashMap<String, Event>();

	protected TaskEventEmitter() {

	}

	public boolean isInited() {
		return eventEmitterConfiguration != null;
	}

	public <T> void fireEvent(Event event, T args) {
		EventObject<T> eventObject = new EventObject<T>(this, event, args);
		eventEmitterConfiguration.manager.addTask(eventObject);
		
		if (eventEmitterConfiguration.showDebugLog) {
			eventEmitterConfiguration.manager.printQueueObjects();
		}
	}
	
	public <T> void fireEvent(String eventName, T args) {
		Event event = handlers.get(eventName);
		if (event == null) {
			throw new IllegalArgumentException("event not publish" + eventName);
		}
		
		fireEvent(event, args);
	}

	public void initEventEmitter(EventEmitterConfiguration configuration) {
		eventEmitterConfiguration = configuration;
	}

	public void destory() {
		handlers.clear();
		initEventEmitter(null);
	}


	@Override
	public void regEvent(String eventName, Event event) {
		handlers.put(eventName, event);
	}
}
