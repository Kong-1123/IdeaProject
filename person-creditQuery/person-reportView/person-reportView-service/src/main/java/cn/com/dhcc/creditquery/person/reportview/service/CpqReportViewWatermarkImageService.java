/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.service;

/** 个人信用报告展示水印相关服务
 * 
 * @author rzd
 * @date 2019年2月26日
 */
public interface CpqReportViewWatermarkImageService {

	/**
	 * 
	 * @param username 查询员
	 * @param deptCode
	 * @return
	 * @author rzd
	 * @date 2019年2月26日
	 */
	String getImage(String username, String deptCode);
}
