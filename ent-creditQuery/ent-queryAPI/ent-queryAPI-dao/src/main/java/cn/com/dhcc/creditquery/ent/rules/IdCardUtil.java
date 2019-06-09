/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.rules;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <企业证件号码校验>
 * @author guoshihu
 * @date 2019年2月14日
 */
public class IdCardUtil {

	private static Logger log = LoggerFactory.getLogger(IdCardUtil.class);

	/**
	 * <组织机构代码应符合如下校验>
	 * 
	 * @param idpass
	 * @return
	 * @author Mingyu.li
	 * @date 2017年6月15日
	 */
	public static final boolean isValidEntpCode(String code) {
		if(StringUtils.isEmpty(code)){
			return false;
		}
		if(code.length() != 10){
			return false;
		}
		log.info("orgCode(String code)------start" + code);
		int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String regex = "^([0-9A-Z]){8}-[0-9|X]$";
		if (!code.matches(regex)) {
			return false;
		}
		int sum = 0;
		for (int i = 0; i < 8; i++) {
			sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
		}
		int c9 = 11 - (sum % 11);
		String sc9 = String.valueOf(c9);
		if (11 == c9) {
			sc9 = "0";
		} else if (10 == c9) {
			sc9 = "X";
		}
		return sc9.equals(String.valueOf(code.charAt(9)));
	}

	public static void main1(String[] args) {
		String code = "89783874-X";
		System.out.println(code);
	}
	
	/**
	 * <中征码校验规则如下：长度为16位，且必须是数字0-9>
	 * 
	 * @param loanCardNo
	 * @return
	 * @author Mingyu.li
	 * @date 2017年6月15日
	 */
	public static boolean isLoanCard(String loanCardNo) {
		if (StringUtils.isEmpty(loanCardNo)) {
			return false;
		}
		if (loanCardNo.length() != 16) {
			return false;
		}
		String subLoanCardNo_14 = loanCardNo.substring(0, 14);
		String subLoanCardNo_2 = loanCardNo.substring(14, 16);
		return subLoanCardNo_2.equals(createLoanCardCheckCode(subLoanCardNo_14));
	}

	// 中佂码则：
	// 境内机构：地区编码（6位）＋8位顺序号 ＋ 2位校验码
	// 境外机构：国别编码（3位）＋ 11位顺序号 ＋ 2位校验码
	public static String createLoanCardCheckCode(String loanCardNo) {
		int[] weightValue = new int[14];
		int[] checkValue = new int[14];
		int totalValue = 0;
		int c = 0;

		if ((loanCardNo == null) || (loanCardNo.length() != 14) || (loanCardNo.trim().length() != 14)) {
			return null;
		}
		String checkCode = loanCardNo.trim();

		weightValue[0] = 1;
		weightValue[1] = 3;
		weightValue[2] = 5;
		weightValue[3] = 7;
		weightValue[4] = 11;
		weightValue[5] = 2;
		weightValue[6] = 13;
		weightValue[7] = 1;
		weightValue[8] = 1;
		weightValue[9] = 17;
		weightValue[10] = 19;
		weightValue[11] = 97;
		weightValue[12] = 23;
		weightValue[13] = 29;

		for (int j = 0; j < 14; j++) {
			checkValue[j] = Character.getNumericValue(checkCode.charAt(j));
			totalValue += weightValue[j] * checkValue[j];
		}

		c = 1 + totalValue % 97;
		String str1 = new Integer(c).toString();

		if (str1.length() == 1) {
			str1 = '0' + str1;
		}

		return str1;
	}

}
