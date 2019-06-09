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

import cn.com.dhcc.creditquery.person.reportview.util.ResultBeans;

/**
 * 
 * @author rzd
 * @date 2019年2月26日
 */
public interface CpqReportViewReadService {

	/**
	 * 读取信用报告使用记录，并展示报告
	 * 
	 * @param creditId
	 * @param recordId
	 * @param userName
	 * @param orgCode
	 * @return
	 * @author rzd
	 * @date 2019年2月27日
	 */
	String readReport(String recordId, String userName, String orgCode,String reportType);

	/**
	 * 保存信用报告打印记录
	 * 
	 * @param recordId
	 * @param userName
	 * @param orgCode
	 * @throws Exception
	 * @return String
	 */
	public ResultBeans savePrintLog(String recordId, String userName, String orgCode);
	/**判断是否允许打印
	 * 
	 * @param recordId
	 * @param userName
	 * @return
	 * @author rzd
	 * @date 2019年2月28日
	 */
	boolean isAllowdPrint(String recordId, String userName);
}
