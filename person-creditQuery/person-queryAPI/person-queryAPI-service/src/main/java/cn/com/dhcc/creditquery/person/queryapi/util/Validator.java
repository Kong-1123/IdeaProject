package cn.com.dhcc.creditquery.person.queryapi.util;

import java.util.List;

public class Validator {
	private static final String IP_SEP = "\\.";

	private static boolean matchUserIp(String fromIp, String allowIp) {
		String[] fromIps = fromIp.split(IP_SEP);
		String[] allowIps = allowIp.split(IP_SEP);
		int index = 0;
		while (index < fromIps.length) {
			if ("*".equals(allowIps[index])) {
				index++;
				continue;
			} else {
				if (!fromIps[index].equals(allowIps[index])) {
					return false;
				}
			}
			index++;
		}
		return true;
	}

	public static boolean verifyUserIp(String userIp, List<String> allowIps) {
		boolean existed = false;
		for (String ip : allowIps) {
			if (matchUserIp(userIp, ip)) {
				existed = true;
				break;
			}
		}
		return existed;
	}

}
