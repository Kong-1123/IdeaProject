/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.rules;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqConfigService;

/**
 * 初始化工具类：初始化CpqConfigService
 * @author yuzhao.xue
 * @date 2019年1月18日
 */
@Component
public class InitConfigServiceUtil {
	private static InitConfigServiceUtil initBeanUtil;
	@Autowired
    private CeqConfigService ceqConfigService;
	
	@PostConstruct
	private void init(){
		initBeanUtil=this;
		initBeanUtil.ceqConfigService=this.ceqConfigService;
	}
	
	public static CeqConfigService getCeqConfigService() {
		return initBeanUtil.ceqConfigService;
	}
	
}
