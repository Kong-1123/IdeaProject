/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.ansytash;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;

/**
 * 初始化工具类：无法使用Spring注入使用此工具类
 * @author yuzhao.xue
 * @date 2019年1月18日
 */
@Component
public class InitUtil {
	private static InitUtil initBeanUtil;
	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
	@Autowired
	private CpqSingleQueryTaskDao cpqSingleQueryTaskDao;
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	
	@PostConstruct
	private void init(){
		initBeanUtil=this;
		initBeanUtil.cpqApproveFlowService=this.cpqApproveFlowService;
		initBeanUtil.cpqSingleQueryTaskDao=this.cpqSingleQueryTaskDao;
		initBeanUtil.cpqFlowManageService=this.cpqFlowManageService;
	}
	


	public static CpqApproveFlowService getCpqApproveFlowService() {
		return initBeanUtil.cpqApproveFlowService;
	}



	public static CpqSingleQueryTaskDao getCpqSingleQueryDao() {
		return initBeanUtil.cpqSingleQueryTaskDao;
	}



	
	public static CpqFlowManageService getCpqFlowManageService() {
		return initBeanUtil.cpqFlowManageService;
	}


}
