/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.service;

import cn.com.dhcc.creditquery.person.querybo.queryapi.SingleQueryBo;

/**
 * 个人信用报告单笔查询接口：
 * 	提供单笔同步 和 单笔异步两种服务
 * @author Jerry.chen
 * @date 2019年2月15日
 */
public interface CpqPersonReportQueryService {
	
	static final String ARCHIVE_TYPE_PAPER = "1";
	static final String ARCHIVE_TYPE_ELECTRON = "2";
	static final String ARCHIVE_TYPE_EXTERNAL_IMAGE = "3";
	/**
	 * 个人单笔查询请求
	 * @param reqXml 请求参数XMl
	 * @return 获取结果报文
	 */
	String getCreditReport(SingleQueryBo reqXml);
	/**
	 * 异步获取结果
	 * @param reqID 根据请求ID获取异步查询结果
	 * @return 结果报文
	 * @author  
	 * @date 2019年1月17日
	 */
	String getSyncCreditReport(String reqID);
}
