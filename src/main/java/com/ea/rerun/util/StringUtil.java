package com.ea.rerun.util;

public class StringUtil {
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}

		return false;
	}
	
}
