package com.zren.platform.bout.common.util;

public final class ThreadUtil {

	public static void sleep(long millis) {
		try {
			Thread.currentThread();
			Thread.sleep(millis);
		} catch (Exception localException) {
		}
	}
	private ThreadUtil() {
	}
}
