/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.util;

import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;

/**
 * @Description: 征信用户信息脱敏的具体实现
 * @author: wenchaobo
 * @date: 2018.10.24
 */
public class ViewEncryptUtil {
	/*
	 * @Description: 对身份证信息进行脱敏,脱年月日
	 * 
	 * @param id
	 */
	public static String idEncrypt(String id) {
		if (id.length() == 18) {
			int endIndex = id.length() - 4;
			int beginIndex = endIndex - 8;
			String temp = "********";
			String front = id.substring(0, beginIndex);
			String back = id.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return result;
		} else if (id.length() == 15) {
			int endIndex = id.length() - 3;
			int beginIndex = endIndex - 6;
			String temp = "******";
			String front = id.substring(0, beginIndex);
			String back = id.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return result;
		}
		return id;
	}

	/*
	 * @Description: 对非身份证信息进行脱敏,6位以上脱6位，6位及以下脱三位
	 * 
	 * @param id
	 */
	public static String elseidEncrypt(String id) {
		if (id.length() > 6) {
			int endIndex = id.length();
			int beginIndex = endIndex - 6;
			String temp = "******";
			String front = id.substring(0, beginIndex);
			String result = front.concat(temp);
			return result;
		} else {
			if (id.length() > 3) {
				int endIndex = id.length();
				int beginIndex = endIndex - 3;
				String temp = "***";
				String front = id.substring(0, beginIndex);
				String result = front.concat(temp);
				return result;
			} else {
				return "***";
			}
		}
	}

	/*
	 * @Description: 对手机电话号码脱敏，11位保留前三后四，其余后三位*号代替
	 * 
	 * @param id
	 */
	public static String phoneEncrypt(String number) {
		int first = 3;
		int two = 7;
		String temp = "****";
		String front = number.substring(0, first);
		String back = number.substring(two);
		String result = front.concat(temp).concat(back);
		return result;
	}

	public static String telEncrypt(String number) {
		int first = number.length() - 3;
		String temp = "***";
		String front = number.substring(0, first);
		String result = front.concat(temp);
		return result;
	}

	/*
	 * @Description: 对单位，家庭地址进行脱敏，地址折半，后半部分用6个*号代替
	 * 
	 * @param id
	 */
	public static String addressEncrypt(String address) {
		int first = address.length() / 2;
		String temp = "******";
		String front = address.substring(0, first);
		String result = front.concat(temp);
		return result;
	}

	/**
	 * @Description: 电话号码脱敏
	 * 
	 * 
	 */
	public static String numberEncrypt(String number) {
		if (number.length() == 11) {
			return QueryEncryptImplUtil.phoneEncrypt(number);
		} else {
			return QueryEncryptImplUtil.telEncrypt(number);
		}
	}
}
