package com.zren.platform.bout.common.util;


/**
 * @author gavin
 * @since 2018年12月12日 上午10:16:39
 */
public class PlayerUtil {
	/**
	 * 处理用户名
	 * 
	 * @param username
	 * @param brand
	 * @return
	 */
	public static String dealUserName(String username, String brand) {
		if (Strings.isBlank(username)) {
			return username;
		}
		if (Strings.isNotBlank(brand)) {
			if (username.startsWith(brand)) {
				username = username.replace(brand, "");
			}
		}
		if (username.startsWith("_")) {
			username = username.replace("_", "");
		}
		int start = username.length() / 3;
		int end = username.length() * 2 / 3;
		String head = username.substring(0, start);
		if (Strings.isBlank(head)) {
			head = username.substring(0, 1);
		}
		String tail = username.substring(end);
		String over = head + "****" + tail;
		return over;
	}
	
	public static String dealMyUserName(String username, String brand) {
		if (Strings.isBlank(username)) {
			return username;
		}
		if (Strings.isNotBlank(brand)) {
			if (username.startsWith(brand)) {
				username = username.replace(brand, "");
			}
		}
		if (username.startsWith("_")) {
			username = username.replace("_", "");
		}
		return username;
	}

}
