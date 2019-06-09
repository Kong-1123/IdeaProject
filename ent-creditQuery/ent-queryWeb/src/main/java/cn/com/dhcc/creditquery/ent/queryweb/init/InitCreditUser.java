/**
 *  Copyright (c)  @date 2018年11月5日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.ent.queryweb.init;

import java.util.Iterator;
import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqPbocUserBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqPbocUserService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
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
    private CeqPbocUserService ceqPbocUserService;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            log.info("init creditUser start------------------");
            List<CeqPbocUserBo> ccuserList = ceqPbocUserService.findAll();
            RMap<Object, Object> creditUserMap = redis.getMap(EntCacheConstant.CREDITUSER_REDIS_KEY);
            creditUserMap.clear();
            if(null != ccuserList){
                for (Iterator<CeqPbocUserBo> iterator = ccuserList.iterator(); iterator.hasNext();) {
                    CeqPbocUserBo cpqCcUser = (CeqPbocUserBo) iterator.next();
                    creditUserMap.put(cpqCcUser.getCreditUser()+cpqCcUser.getDeptCode(), cpqCcUser);
                }
            }
            log.info("init creditUser stop------------------");
        } catch (Exception e) {
            log.error("creditUser initialization failed!", e.getMessage());
            throw e;
        }

    }

}
