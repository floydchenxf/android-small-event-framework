package com.trade.goods;

import android.app.Application;

import com.floyd.event.EventEmitterConfiguration;
import com.floyd.event.EventEmitterConfiguration.Builder;
import com.floyd.event.EventEmitterGenerator;

public class EventApplication extends Application {

	private EventEmitterGenerator generator = EventEmitterGenerator.getInstance();

	@Override
	public void onCreate() {  
		super.onCreate();
		Builder builder = new Builder().taskExecutor(3, 100).showDebugLog(true);
		EventEmitterConfiguration eventEmitterConfiguration = new EventEmitterConfiguration(builder);
		generator.initEventEmitter(eventEmitterConfiguration);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		generator.destory();
	}

}
