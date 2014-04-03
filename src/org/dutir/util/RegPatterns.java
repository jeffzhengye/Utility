package org.dutir.util;

public class RegPatterns {

	public static String test ="^[a-z0-9_-]+";
	
	public static String ipPattern = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	
	public static String urlPattern = "^((https?|ftp|news)://)?" +
			"([a-z]([a-z0-9-]*\\.)+" +
			"([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)" +
			"|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
			"([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(/[a-z0-9_-\\.~]+)*(/([a-z0-9_-\\.]*)(\\?[a-z0-9+_-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$";
	
	public static String emailPattern = "^[a-z0-9_-]+(\\.[_a-z0-9-]+)*@([_a-z0-9-]+\\.)+" +
			"([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)$";
}
