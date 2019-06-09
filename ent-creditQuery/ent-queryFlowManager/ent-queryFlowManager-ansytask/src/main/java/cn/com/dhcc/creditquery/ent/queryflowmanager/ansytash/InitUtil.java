/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.ansytash;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;

/**
 * 初始化工具类：无法使用Spring注入使用此工具类
 * @author yuzhao.xue
 * @date 2019年1月18日
 */
@Component
public class InitUtil {
	private static InitUtil initBeanUtil;
	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;
	@Autowired
	private CeqSingleQueryTaskDao ceqSingleQueryTaskDao;
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	
	@PostConstruct
	private void init(){
		initBeanUtil=this;
		initBeanUtil.ceqApproveFlowService=this.ceqApproveFlowService;
		initBeanUtil.ceqSingleQueryTaskDao=this.ceqSingleQueryTaskDao;
		initBeanUtil.ceqFlowManageService=this.ceqFlowManageService;
	}
	


	public static CeqApproveFlowService getCeqApproveFlowService() {
		return initBeanUtil.ceqApproveFlowService;
	}



	public static CeqSingleQueryTaskDao getCeqSingleQueryDao() {
		return initBeanUtil.ceqSingleQueryTaskDao;
	}



	
	public static CeqFlowManageService getCeqFlowManageService() {
		return initBeanUtil.ceqFlowManageService;
	}


}
