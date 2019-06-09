/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.service;

import java.util.List;

/**
 * 
 * @author rzd
 * @date 2019年2月26日
 */
public interface CeqReportViewQueryEncryptService {
	/**
	 * @Description: 列表脱敏
	 * 
	 */
	<T> void queryEncrypt(List<T> list);

	/*
	 * @Description: 明细脱敏
	 * 
	 */
	<T> void queryEncrypt(T t);

	/**
	 * @Description: 电话号码脱敏
	 * 
	 */
	String numberEncrypt(String phoneNumber);

}
