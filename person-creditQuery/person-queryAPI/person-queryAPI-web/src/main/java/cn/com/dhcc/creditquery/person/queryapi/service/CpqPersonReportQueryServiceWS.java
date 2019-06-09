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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 * @author Jerry.chen
 * @date 2019年2月15日
 */
@WebService
public interface CpqPersonReportQueryServiceWS {
	/**
	 * 个人单笔查询请求
	 * @param parXml
	 * @return
	 */
	@WebMethod
	String getCreditReport(@WebParam(name="parXml")String parXml);
	/**
	 * 异步获取结果
	 * @param reqID
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年1月17日
	 */
	@WebMethod
	String getSyncCreditReport(@WebParam(name="reqid")String reqID);
}
