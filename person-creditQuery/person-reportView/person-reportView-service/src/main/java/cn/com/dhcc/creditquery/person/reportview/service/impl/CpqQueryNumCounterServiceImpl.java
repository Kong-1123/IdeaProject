/**
 *  Copyright (c)  @date 2018年8月3日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.reportview.service.impl;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.reportview.service.CpqQueryNumCounterService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author chenting
 * @date 2018年8月3日
 */
@Service
@Slf4j
public class CpqQueryNumCounterServiceImpl implements CpqQueryNumCounterService {

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	/**
	 * 征信用户密码错误记录
	 */
	public void creditUserPasswordErrorRecord(String creditUser, boolean blean, String message) {
		try {
			log.info("CpqQueryNumCounter service creditUserPasswordErrorRecord ER_CREDITUSER", PersonCacheConstant.ER_CREDITUSER);
			RMap<String, String> cache = null;
			cache = redis.getMap(PersonCacheConstant.ER_CREDITUSER);
			if (blean) {// 如果为true记录，false 删除
				if (cache.get(creditUser) == null) {
					cache.put(creditUser, message);
				}
			} else {
				if (cache.get(creditUser) != null) {
					cache.remove(creditUser);
				}
			}
		} catch (Exception e) {
			log.error("CpqQueryNumCounterServiceImpl creditUserPasswordErrorRecord error", e);
		}
	}
}
