/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.creditquery.person.queryapi.dao.CpqSingleQueryDao;
import cn.com.dhcc.creditquery.person.queryapi.dao.CqSysIpDao;
import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleQuery;
import cn.com.dhcc.creditquery.person.queryapi.service.CpqPersonReportQueryService;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import lombok.extern.slf4j.Slf4j;

/**
 * 初始化工具类：无法使用Spring注入使用此工具类
 * @author yuzhao.xue
 * @date 2019年1月18日
 */
@Slf4j
@Component
public class InitBeanUtil {
	private static InitBeanUtil initBeanUtil;
	@Autowired
	private CpqPersonReportQueryService queryService;
	@Autowired
	private CqSysIpDao cqSysIpDao;
	@Autowired
	private CpqSingleQueryDao cpqSingleQueryDao;
	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
	
	@PostConstruct
	private void init(){
		initBeanUtil=this;
		initBeanUtil.queryService=this.queryService;
		initBeanUtil.cqSysIpDao=this.cqSysIpDao;
		initBeanUtil.cpqSingleQueryDao=this.cpqSingleQueryDao;
		initBeanUtil.cpqApproveFlowService=this.cpqApproveFlowService;
	}
	
	/**
	 * @return the dataProcessService
	 */
	public static CpqPersonReportQueryService getCpqPersonReportQueryService() {
		return initBeanUtil.queryService;
	}


	@Transactional
	public static void saveSingleQuery(CpqSingleQuery cpqSingleQuery){
		log.info("进入方法InitBeanUtil---saveSingleQuery(CpqSingleQuery {})",cpqSingleQuery);
		initBeanUtil.cpqSingleQueryDao.save(cpqSingleQuery);
	}
	@Transactional
	public static List<String> findIpBySysCode(String sysCode,String stopFlag){
		log.info("进入方法InitBeanUtil---findIpBySysCode(String sysCode,String stopFlag)",sysCode,stopFlag);
		List<String> findIpBySysCode = initBeanUtil.cqSysIpDao.findIpBySysCode(sysCode,stopFlag);
		return findIpBySysCode;
	}
	

	public static CpqApproveFlowService getCpqApproveFlowService() {
		return initBeanUtil.cpqApproveFlowService;
	}
	
}
