/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <个人证件号码校验>
 * @author guoshihu
 * @date 2019年2月14日
 */
public class IdCardUtil {
	private static Logger log = LoggerFactory.getLogger(IdCardUtil.class);

	/**
	 * 居民身份证及其他以公民身份证号为标 识的证件
	 */
	public static final String ID_CARD = "10";

	/**
	 * String idpass = "1234567890(11)"; “台湾居民往来大陆通行证”的证件号码应符合如下规则：
	 * 
	 * @param idpass
	 * @return
	 * @author tanshengrui
	 * @date 2017年4月25日
	 */
	public static boolean taiwanIdpassValidator(String idpass) {
		try {
			// 前 10 位必须为数字
			String idpass10 = null;
			// 后三位或者后四位变量
			String idpassEnd = null;
			// 字母的变量
			String alphabet = null;
			// 英文正则
			String alphabetRegular = "^[A-Za-z]+$";
			// 两位数字的正则表达式
			String numberRegular = "^\\d{2}$";
			// 数字的变量
			String number = null;
			// 8 位证件必须为 8 位数字
			if (idpass.length() == 8) {
				if (StringUtils.isNumeric(idpass)) {
					return true;
				}
				return false;
			}
			// 10-14 位证件
			if (10 <= idpass.length() && idpass.length() <= 14) {
				idpass10 = StringUtils.substring(idpass, 0, 10);
				if (StringUtils.isNumeric(idpass10)) {
					// 10-14 位证件，前 10 位必须为数字
					if (StringUtils.isNumeric(idpass10)) {
						// 取出10位之后的数字
						idpassEnd = StringUtils.substring(idpass, 10, idpass.length());
						if (idpassEnd.length() == 3) {
							// 是否含有（）
							if (idpassEnd.contains("(") && idpassEnd.contains(")")) {
								alphabet = StringUtils.substring(idpassEnd, 11, idpassEnd.length() - 1);
								Pattern pattern = Pattern.compile(alphabetRegular);
								// 判断是否是英文
								if (pattern.matcher(alphabet).matches()) {
									return true;
								}
								return false;
							}
							return false;
						}
						if (idpassEnd.length() == 4) {
							if (idpassEnd.contains("(") && idpassEnd.contains(")")) {
								number = StringUtils.substring(idpass, 11, idpass.length() - 1);
								Pattern pattern = Pattern.compile(numberRegular);
								// 判断2位的数字
								if (pattern.matcher(number).matches()) {
									return true;
								}
								return false;
							}
						}
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 港澳居民往来大陆通行证”的证件号码应符合如下规则：  长度必须为 9 位或 11 位；  第一位为字母“H”或“M”，后 8 位或 10
	 * 位必须为数字。
	 * 
	 * @param idpass
	 * @return
	 * @author tanshengrui
	 * @date 2017年4月25日
	 */
	public static boolean hkmacidpassValidator(String idpass) {
		try {
			if (idpass.length() == 9 || idpass.length() == 11) {
				if (idpass.startsWith("H") || idpass.startsWith("M")) {
					String idpassAfter = StringUtils.substring(idpass, 1);
					if (StringUtils.isNumeric(idpassAfter)) {
						return true;
					}
					return false;
				}
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <护照的校验> 校验规则 ： 最多 12 位；  前 3 位为国籍代码
	 * 
	 * @param passport
	 * @return
	 * @author tanshengrui
	 * @param keySet
	 * @date 2017年4月25日
	 */
	public static boolean passportValidator(String passport, Set<String> keySet) {
		if (StringUtils.isNotBlank(passport) && CollectionUtils.isNotEmpty(keySet)) {
			if (passport.length() > 3 && passport.length() <= 12) {
				String nation = StringUtils.substring(passport, 0, 3);
				if (keySet.contains(nation)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * convertIdCardNO 身份证15位转18位
	 * 
	 * @param idCardNO
	 * @author tanshengrui
	 * @date 2017年3月28日
	 */
	public static String convertIdCardNO15To18(String idCardNO) {
		int len = 0;
		len = idCardNO.length();
		if (len == 15) {
			// 15位转18位：
			// 先将15位的号码转换成17位
			String idCardNO18 = "";
			String idCardNO17 = idCardNO.substring(0, 6) + "19" + idCardNO.substring(6);
			int N = 0;
			int R = -1;
			char T = 0;// 储存最后一个数字
			int j = 0;
			List<Integer> v = Arrays.asList(2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7);
			String vs = "10X98765432";
			// 计算出第18位
			for (int i = 16; i >= 0; i--) {
				N += Integer.parseInt(idCardNO17.substring(i, i + 1)) * v.get(j).intValue();
				j++;
			}
			R = N % 11;
			T = vs.charAt(R);
			idCardNO18 = idCardNO17 + String.valueOf(T);
			return idCardNO18;
		} else {
			return idCardNO;
		}
	}

	/**
	 * 身份证15位转18位
	 * 
	 * @param idCardNO
	 * @return idType
	 * @author tanshengrui
	 * @date 2017年9月23日
	 */
	public static String convertIdCardNO15To18(String idCardNO, String idType){
		log.info("convertIdCardNO15To18(String idCardNO:{}, String idType:{})",idCardNO,idType);
		try {
			if (StringUtils.isNotBlank(idCardNO) && StringUtils.isNotBlank(idType)) {
				if (idType.equals(ID_CARD)) {
					String idCardNO18 = null;
					idCardNO18 = convertIdCardNO15To18(idCardNO);
					return idCardNO18;
				} else {
					return idCardNO;
				}
			} else {
				return idCardNO;
			}
		} catch (Exception e) {
			log.error("convertIdCardNO15To18()",e);
		}
		return idCardNO;
	}
	
//	public static void main(String[] args) {
//		String a = "15282319821206003X";
//		System.err.println(a.length());
//		System.err.println(checkCardStrict("15282319821206003X"));;
//	}
	/**
	 * <将字符串转成数组>
	 * 
	 * @param idcard
	 * @param idcard_array
	 * @author tanshengrui
	 * @date 2017年4月24日
	 */
	private static void idcard_array(String idcard, String[] idcard_array) {
		for (int i = 0; i < idcard.length(); i++) {
			char item = idcard.charAt(i);
			idcard_array[i] = String.valueOf(item);
		}
	}

	/**
	 * isDate getMaxDay isNumber 身份证校验用到的三个函数 isDate 判断生日的合法性 getMaxDay 取得天的最大数
	 * 包括平年和闰年 isNumber 校验整数
	 */
	private static boolean isDate(String date) {
		String fmt = "yyyyMMdd";
		int yIndex = fmt.indexOf("yyyy");
		if (yIndex == -1){
			return false;
		}
		String year = date.substring(yIndex, yIndex + 4);
		int mIndex = fmt.indexOf("MM");
		if (mIndex == -1){
			return false;
		}
		String month = date.substring(mIndex, mIndex + 2);
		int dIndex = fmt.indexOf("dd");
		if (dIndex == -1){
			return false;
		}
		String day = date.substring(dIndex, dIndex + 2);
		if (!StringUtils.isNumeric(year) || Integer.parseInt(year) > 2100 || Integer.parseInt(year) < 1900){
			return false;
		}
		if (!StringUtils.isNumeric(month) || Integer.parseInt(month) > 12 || Integer.parseInt(month) < 01){
			return false;
		}
		if (Integer.parseInt(day) > getMaxDay(Integer.parseInt(year), Integer.parseInt(month)) || Integer.parseInt(day) < 01){
			return false;
		}
		return true;

	}

	/**
	 * 根据年和月计算最大的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @author tanshengrui
	 * @date 2017年4月24日
	 */
	private static int getMaxDay(int year, int month) {
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2){
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
				return 29;
			}else{
				return 28;
			}
		}
		return 31;

	}

	/**
	 * checkCard 校验身份证
	 * 
	 * @param {}
	 *            idcard
	 * @return {Boolean} 根据大系统的身份证校验代码判断 15位身份证只校验长度 每一位必须为整数 18位弱校验
	 */
	public static boolean checkCard(String idcard) {
		Pattern re = Pattern.compile("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)");
		if (idcard.length() > 0 && idcard != null) {
			// 先判断15或18位身份证的基本校验 如长度 每一位是否为整数 以及18位最后一位是否为X
			if (!re.matcher(idcard).matches()) {
				return false;
			}
			return true;
		}
		return false;
	};

	/**
	 * checkCard 校验身份证（强校验）
	 * 
	 * @param {}
	 *            idcard
	 * @return {Boolean}
	 *         根据大系统的18位身份证校验规则：18位身份证需校验前17位是否为数字，出生日期须合法，且最后一位校验位需符合算法。
	 */
	public static boolean checkCardStrict(String idcard) {
		// 先判断18位身份证是否通过基本校验： 如长度 ，每一位是否为整数 以及18位最后一位是否为"X"
		if (idcard.length() == 15) {
			idcard = convertIdCardNO15To18(idcard);
		}
		Pattern re = Pattern.compile("^\\d{17}([0-9]|X)$");
		String[] idcard_array = new String[idcard.length()];
		int S, Y;
		String M;
		String JYM;
		idcard_array(idcard, idcard_array);
		if (idcard.length() > 0 && idcard != null) {
			// 先判断18位身份证是否通过基本校验： 如长度 ，每一位是否为整数 以及18位最后一位是否为"X"
			if (!re.matcher(idcard).matches()) {
				return false;
			}
			// 再判断18身份证的出生日期与校验位
			// 判断出生日期是否合法
			String date = idcard.substring(6, 10) + idcard.substring(10, 12) + idcard.substring(12, 14);
			if (isDate(date)) {
				S = (Integer.parseInt(idcard_array[0]) + Integer.parseInt(idcard_array[10])) * 7 + (Integer.parseInt(idcard_array[1]) + Integer.parseInt(idcard_array[11])) * 9 + (Integer.parseInt(idcard_array[2]) + Integer.parseInt(idcard_array[12])) * 10 + (Integer.parseInt(idcard_array[3]) + Integer.parseInt(idcard_array[13])) * 5
						+ (Integer.parseInt(idcard_array[4]) + Integer.parseInt(idcard_array[14])) * 8 + (Integer.parseInt(idcard_array[5]) + Integer.parseInt(idcard_array[15])) * 4 + (Integer.parseInt(idcard_array[6]) + Integer.parseInt(idcard_array[16])) * 2 + Integer.parseInt(idcard_array[7]) * 1 + Integer.parseInt(idcard_array[8]) * 6 + Integer.parseInt(idcard_array[9]) * 3;
				// System.out.println("sadfsadfasd"+S);
				Y = S % 11;
				// System.out.println("Y"+Y);
				M = "F";
				JYM = "10X98765432";
				// 判断校验位是否合法
				M = JYM.substring(Y, Y + 1);
				/*
				 * System.out.println("mm" + M);
				 * System.out.println("idcard_array[17]" + idcard_array[17]);
				 */
				if (M.toString().equals(idcard_array[17].toString())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

}
