/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.creditquery.ent.queryapi.dao.CeqSingleQueryDao;
import cn.com.dhcc.creditquery.ent.queryapi.dao.CqSysIpDao;
import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleQuery;
import cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryService;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
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
	private CeqEntReportQueryService queryService;
	@Autowired
	private CqSysIpDao cqSysIpDao;
	@Autowired
	private CeqSingleQueryDao ceqSingleQueryDao;
	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;
	
	@PostConstruct
	private void init(){
		initBeanUtil=this;
		initBeanUtil.queryService=this.queryService;
		initBeanUtil.cqSysIpDao=this.cqSysIpDao;
		initBeanUtil.ceqSingleQueryDao=this.ceqSingleQueryDao;
		initBeanUtil.ceqApproveFlowService=this.ceqApproveFlowService;
	}
	
	/**
	 * @return the dataProcessService
	 */
	public static CeqEntReportQueryService getCeqEntReportQueryService() {
		return initBeanUtil.queryService;
	}


	@Transactional
	public static void saveSingleQuery(CeqSingleQuery ceqSingleQuery){
		log.info("进入方法InitBeanUtil---saveSingleQuery(CeqSingleQuery {})",ceqSingleQuery);
		initBeanUtil.ceqSingleQueryDao.save(ceqSingleQuery);
	}
	@Transactional
	public static List<String> findIpBySysCode(String sysCode, String stopFlag){
		log.info("进入方法InitBeanUtil---findIpBySysCode(String sysCode{}, String stopFlag{})",sysCode,stopFlag);
		List<String> findIpBySysCode = initBeanUtil.cqSysIpDao.findIpBySysCode(sysCode,stopFlag);
		log.info("List<String> findIpBySysCode{}",findIpBySysCode);
		return findIpBySysCode;
	}
	

	public static CeqApproveFlowService getCeqApproveFlowService() {
		return initBeanUtil.ceqApproveFlowService;
	}
	
}
