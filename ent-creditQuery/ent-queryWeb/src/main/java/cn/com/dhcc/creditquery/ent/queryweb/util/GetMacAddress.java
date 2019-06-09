package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lekang.liu
 * @date 2018年4月8日
 *
 */
public class GetMacAddress {
	
	/**
	 * 在Linux环境下执行相关命令，不对shell执行结果进行处理
	 * @param shell 需执行的shell命令
	 * @return shell执行后的所有结果信息
	 */
	private static String callCmd4Linux(String[] shell) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(shell);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *  在windows环境下执行相关命令，并从结果中处理获取MAC信息
	 * @param shell 需执行的shell命令
	 * @return 处理后的MAC地址信息
	 */
	private static String callCmd4Win(String[] shell) {
		String result = "";
		String line = "";
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(shell);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				if (line.contains("MAC")) {
					result = line.split("=")[1];
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param  ip 目标ip,一般在局域网内
	 * @param sourceString 命令处理的结果字符串
	 * @param macSeparator  mac分隔符号
	 * @return MAC地址，用上面的分隔符号表示
	 */
	private static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
		String result = "";
		String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(sourceString);
		while (matcher.find()) {
			result = matcher.group(1);
			if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
				break; // 如果有多个IP,只匹配本IP对应的Mac.
			}
		}
		return result;
	}

	
	/**
	 * 服务机位于windows类系统时，使用该方法获取目标机的MAC地址信息。
	 * 若服务机为Linux类系统，请调用{@link GetMacAddress#getMacInLinux(String)}
	 * @param 目标IP
	 * @return  MAC地址信息
	 */
	private static String getMacInWindows(final String ip) {
		String[] shell = { "cmd", "/c", "nbtstat -a " + ip };
		String cmdResult = callCmd4Win(shell);
		return cmdResult;
	}

	/**
	 * 服务机位于Linux类系统时，使用该方法获取目标机的MAC地址信息。
	 * 若服务机为windows类系统，请调用{@link GetMacAddress#getMacInWindows(String)}
	 * @param 目标IP
	 * @return  MAC地址信息
	 */
	private static String getMacInLinux(final String ip) {
		String result = "";
		String[] shell = { "/bin/sh", "-c", "arp  -a " };
		String shellResult = callCmd4Linux(shell);
		result = filterMacAddress(ip, shellResult, ":");
		return result;
	}

	/**
	 * 根据传入目标IP获取该机器的MAC地址
	 * @param 目标IP
	 * @return 返回MAC地址。若未获取到或传入参数为空，则返回为{@code null}
	 */
	public static String getMacAddress(String ip) {
		if(StringUtils.isBlank(ip)){
			return null;
		}
		String macAddress = "";
		String os = getOSName();
		if (!os.startsWith("windows")) {
			// 非windows系统 一般就是unix
			macAddress = getMacInLinux(ip);
		} else {
			macAddress = getMacInWindows(ip);
		}
		 macAddress = macAddress.replaceAll(" ", "");
		 if(StringUtils.isBlank(macAddress)){
			 return null;
		 }
		return macAddress;
	}

	/**
	 * 获取当前操作系统名称
	 * @return  操作系统名称 例如:windows 8,linux 等.
	 */
	private static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	// 做个测试
	public static void main(String[] args) {
		long time1 = new Date().getTime();
		System.out.println(new Date().getTime());
		System.out.println(getMacAddress("10.10.121.18"));
		long time2 = new Date().getTime();
		System.out.println(new Date().getTime());
		System.out.println((time2 - time1));

	}
}
