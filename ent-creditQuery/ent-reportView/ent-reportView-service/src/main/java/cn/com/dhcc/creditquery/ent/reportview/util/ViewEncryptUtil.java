/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.util;

import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 征信用户信息脱敏的具体实现
 * @author: wenchaobo
 * @date: 2018.10.24
 */
@Slf4j
public class ViewEncryptUtil {
	private static final String FOURSTAR = "****";
	private static final String SIXSTAR = "******";
	private static final String SEVENSTAR = "********";
	private static final String EIGHTSTAR = "********";
	private static final String TENSTAR = "**********";
	/*
	 * @Description: 对中征码信息进行脱敏,共16位，保留前四后四，存在空格占两位
	 * @param id
	 */
	public static String loancardCodeEncrypt(String loancardCode){
		if(null != loancardCode && loancardCode.length() == 16){
			int endIndex = loancardCode.length()-4;
			int beginIndex=4;
			String temp = EIGHTSTAR;
			String front = loancardCode.substring(0, beginIndex);
			String back = loancardCode.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return 	result;
		}else if(null != loancardCode && loancardCode.length() == 18){
			int endIndex = loancardCode.length()-4;
			int beginIndex=6;
			String temp = EIGHTSTAR;
			String front = loancardCode.substring(0, beginIndex);
			String back = loancardCode.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return 	result;
		}
		return loancardCode;
	} 
	
	/*
	 * @Description: 对统一社会信用代码信息进行脱敏,有15位和18位，保留前四后四
	 * @param id
	 */
	public static String usCreditCodeEncrypt(String usCreditCode){
		if(null != usCreditCode && usCreditCode.length() == 18){
			int endIndex = usCreditCode.length()-4;
			int beginIndex=4;
			String temp = TENSTAR;
			String front = usCreditCode.substring(0, beginIndex);
			String back = usCreditCode.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return 	result;
		}else if(null != usCreditCode && usCreditCode.length() == 15){
			int endIndex = usCreditCode.length()-4;
			int beginIndex=4;
			String temp = SEVENSTAR;
			String front = usCreditCode.substring(0, beginIndex);
			String back = usCreditCode.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return 	result;
		}
		return usCreditCode;
	} 
	/*
	 * @Description: 对组织机构代码脱敏，保留5位和最后1位
	 * @param id
	 */
	public static String corpNoEncrypt(String number){
		
		if(null != number && number.length()>=9){
			if(number.length()==9) {
				String front = number.substring(0,5);
				String back = number.substring(8);
				String temp = starMaker(3).concat(back);
				String result = front.concat(temp);
				return result;
			} else {
				String front = number.substring(0,5);
				String back = number.substring(8);
				String temp = starMaker(3).concat(back);
				String result = front.concat(temp);
				return result;
			}
		}
		return number;
	} 
	/*
	 * @Description: 对机构信用代码脱敏，共18位，保留前四后四
	 * @param id
	 */
	public static String creditCodeEncrypt(String creditCode){
		if(null != creditCode && creditCode.length() == 18){
			int endIndex = creditCode.length()-4;
			int beginIndex=4;
			String temp = TENSTAR;
			String front = creditCode.substring(0, beginIndex);
			String back = creditCode.substring(endIndex);
			String result = front.concat(temp).concat(back);
			return 	result;
		}
		return creditCode;
	} 
	/*
	 * @Description: 对国税、地税登记号脱敏，保留前六，空格占两格
	 * @param id
	 */
	public static String taxEncrypt(String tax){
		if(null != tax && tax.length() >6){
			int beginIndex=6+2;
			int star = tax.length()-beginIndex;
			String temp = starMaker(star);
			String front = tax.substring(0, beginIndex);
			String result = front.concat(temp);
			return 	result;
		}
		return tax;
	} 
	/*
	 * @Description: 对注册登记号脱敏，脱一半
	 * @param id
	 */
	public static String regNumEncrypt(String regNum){
		if(null != regNum){
			int beginIndex=regNum.length()/2;
			int star = regNum.length()-beginIndex;
			String temp = starMaker(star);
			String front = regNum.substring(0, beginIndex);
			String result = front.concat(temp);
			return 	result;
		}
		return regNum;
	} 
	/*
	 * @Description: 对查询操作员脱敏，脱征信用户
	 * @param id
	 */
	public static String opertorEncrypt(String opertor){
		if(null != opertor){
			int beginIndex=opertor.indexOf("/")+1;
			int star = opertor.length()-beginIndex;
			String temp = starMaker(star);
			String front = opertor.substring(0, beginIndex);
			String result = front.concat(temp);
			return 	result;
		}
		return opertor;
	} 
	
	/*
	 * @Description: 对注册地址进行脱敏，地址折半，后半部分用6个*号代替
	 * @param id
	 */
	public static String addressEncrypt(String address){
		if(null !=address && address.length()>2 ){
			String addressTrim = address.substring(2);
			int first =addressTrim.length()/2+2;
			String temp = SIXSTAR;
			String front = address.substring(0, first);
			String result = front.concat(temp);
			return result;
		}
		return address;
	} 
	
	public static String starMaker(int number){
		String result="";
		for(int i=0;i<number;i++){
			result +="*";
		}
		return result;
	}
	

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
