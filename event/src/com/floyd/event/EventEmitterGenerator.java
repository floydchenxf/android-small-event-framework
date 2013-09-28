package com.floyd.event;

/**
 * 事件发送器生成类
 * 
 * 使用方法 Builder builder = new Builder().taskExecutor(5);
 * EventEmitterConfiguration eventEmitterConfiguration = new EventEmitterConfiguration(builder); 
 * EventEmitterGenerator generator = EventEmitterGenerator.getInstance();
 * generator.initEventEmitter(eventEmitterConfiguration);
 * EventEmitter eventEmitter = generator.generateEmitter();
 * eventEmitter.regEvent("hello2", new Hello2EventHandler());
 * eventEmitter.fireEvent("hello1", new Object[] { "wuliao" }, true);
 * 
 * @author zjutcxf128
 * 
 */
public class EventEmitterGenerator {

	private static final String ERROR_NOT_INIT = "eventEmitter must be init with configuration before using";
	private static final String ERROR_NOT_INIT_MUTLI_EXECUTOR_SERVICE = "eventEmitter must be init with multi executorService before using";

	private EventEmitterConfiguration eventEmitterConfiguration;

	private static EventEmitterGenerator generator = new EventEmitterGenerator();

	private EventEmitterGenerator() {

	}

	public static EventEmitterGenerator getInstance() {
		return generator;
	}

	private void checkConfiguration() {
		if (eventEmitterConfiguration == null) {
			throw new IllegalStateException(ERROR_NOT_INIT);
		}

		if (eventEmitterConfiguration.multiExecutor == null) {
			throw new IllegalStateException(ERROR_NOT_INIT_MUTLI_EXECUTOR_SERVICE);
		}
	}

	public void initEventEmitter(EventEmitterConfiguration configuration) {
		eventEmitterConfiguration = configuration;
	}

	public EventEmitter generateEmitter() {
		EventEmitter eventEmitter = new TaskEventEmitter();
		checkConfiguration();
		eventEmitter.initEventEmitter(eventEmitterConfiguration);
		return eventEmitter;
	}

	public void destory() {
		if (eventEmitterConfiguration != null) {
			eventEmitterConfiguration.manager.destory();
			eventEmitterConfiguration.multiExecutor.destroy();
			eventEmitterConfiguration = null;
		}
	}

}
