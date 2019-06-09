/**
 *  Copyright (c)  @date 2018年11月5日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.init;

import java.util.Iterator;
import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqPbocUserBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqPbocUserService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import lombok.extern.slf4j.Slf4j;


/**
 * 将征信用户信息缓存至redis中
 * @author lekang.liu
 * 
 */
@Slf4j
@Component
public class InitCreditUser implements InitializingBean {
    
    private RedissonClient redis = RedissonUtil.getLocalRedisson();
    
    @Autowired
    private CpqPbocUserService ccuserService;
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            log.info("init creditUser start------------------");
            
            List<CpqPbocUserBo> ccuserList = ccuserService.findAll();
            RMap<Object, Object> creditUserMap = redis.getMap(PersonCacheConstant.CREDITUSER_REDIS_KEY);
            creditUserMap.clear();
            if(null != ccuserList){
                for (Iterator<CpqPbocUserBo> iterator = ccuserList.iterator(); iterator.hasNext();) {
                    CpqPbocUserBo cpqCcUser = (CpqPbocUserBo) iterator.next();
                    creditUserMap.put(cpqCcUser.getCreditUser(), cpqCcUser);
                }
            }
            log.info("init creditUser stop------------------");
        } catch (Exception e) {
            log.error("creditUser initialization failed!", e.getMessage());
            throw e;
        }

    }

}
