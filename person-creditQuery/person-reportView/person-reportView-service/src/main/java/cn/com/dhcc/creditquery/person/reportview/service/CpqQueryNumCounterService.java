/**
 *  Copyright (c)  @date 2018年8月2日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.reportview.service;

/**
 * 
 * @author chenting
 * @date 2018年8月2日
 */
public interface CpqQueryNumCounterService {
	
	/**
	 * 征信用户密码错误记录
	 * @param creditUser
	 */
	void creditUserPasswordErrorRecord(String creditUser, boolean blean,String message);
}
	