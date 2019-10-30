package com.zren.platform.bout.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程池工具
 * 
 * @author Hudson.D
 *
 */
@Slf4j
public class ExecutorUtil {

	private static ExecutorService es = Executors.newFixedThreadPool(64);

	private static ScheduledExecutorService scheduleExcutor = Executors.newScheduledThreadPool(64);

	public static Future<?> submit(Runnable task) {
		return es.submit(task);
	}

	public static void destory() {
		es.shutdown();
		scheduleExcutor.shutdown();
	}

	public static void execute(Runnable task) {
		es.execute(task);
	}

	/**
	 * 从当前时间开始每隔一定时间执行一次
	 * 
	 * @param task
	 * @param period
	 */
	public static void schedule(Runnable task, long period) {
		scheduleExcutor.scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * 延时执行
	 * 
	 * @param task
	 * @param delay
	 */
	public static void delaySchedule(Runnable task, long delay) {
		scheduleExcutor.schedule(task, delay, TimeUnit.MILLISECONDS);
	}

	public static void main(String[] msg) {
        Exception ex = new com.ecwid.consul.v1.OperationException(0, "aaaaabb", "aaaaaaaaaaaaaabbbbb");
        log.error("aaaa:{},{}","ss", ex);
	}

}
