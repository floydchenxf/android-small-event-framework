package com.floyd.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事件发送器配置
 * 
 * @author zjutcxf128
 * 
 */
public final class EventEmitterConfiguration {

	/**
	 * 多线程执行器
	 */
	final EventExecutor multiExecutor;

	/**
	 * 事件任务管理器
	 */
	final EventTaskManager manager;

	/**
	 * 是否打印debug日志
	 */
	boolean showDebugLog = false;

	public EventEmitterConfiguration(final Builder builder) {
		multiExecutor = builder.multiExecutor;
		manager = EventTaskManager.getInstance();
		manager.initConfiguration(this);
		manager.start();
		showDebugLog = builder.showDebugLog;
	}

	public static class Builder {
		public static final int DEFAULT_THREAD_POOL_SIZE = 3;

		private EventExecutor multiExecutor = null;

		private boolean showDebugLog = false;

		public Builder taskExecutor(ExecutorService executorService) {
			multiExecutor = new DefaultEventExecutor(executorService);
			return this;
		}

		public Builder taskExecutor(int coreSize, int maxSize) {
			ExecutorService multiExecutorService = new ThreadPoolExecutor(
					coreSize, maxSize, 30l, TimeUnit.SECONDS,
					new SynchronousQueue<Runnable>());
			multiExecutor = new DefaultEventExecutor(multiExecutorService);
			return this;
		}

		public Builder showDebugLog(boolean isShow) {
			showDebugLog = isShow;
			return this;
		}

		public Builder build() {
			initDefaultBuilder();
			return this;
		}

		private void initDefaultBuilder() {
			if (multiExecutor == null) {
				ExecutorService multiExecutorService = Executors
						.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
				multiExecutor = new DefaultEventExecutor(multiExecutorService);
			}

			showDebugLog = false;
		}
	}
}
