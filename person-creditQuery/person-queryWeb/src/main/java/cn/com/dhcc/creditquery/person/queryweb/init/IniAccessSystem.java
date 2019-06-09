/**
 *  Copyright (c)  @date 2018年11月5日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*

package cn.com.dhcc.creditquery.person.queryweb.init;

import java.util.Iterator;
import java.util.List;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryconfig.entity.CpqAccessSystem;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqAccessSystemService;

*/
/**
 * 将客户系统信息缓存至redis中
 * @author lekang.liu
 * 
 *//*

@Component
public class IniAccessSystem implements InitializingBean {

    private static Logger log = LoggerFactory.getLogger(IniAccessSystem.class);
    
    private RedissonClient redis = RedissonUtil.getLocalRedisson();
    
    private static final String REDIS_KEY = "ACCESSSYSTEM_KEY";
    
    
    @Autowired
    private CpqAccessSystemService  cpqAccessSystemService;
    
    */
/* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     *//*

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            log.info("init accessSystem start------------------");
            
            List<CpqAccessSystem> accessSystemList = cpqAccessSystemService.findAll();
            RList<Object> list = redis.getList(REDIS_KEY);
            list.clear();
            if(null != accessSystemList){
                for (Iterator<CpqAccessSystem> iterator = accessSystemList.iterator(); iterator.hasNext();) {
                    CpqAccessSystem cpqAccessSystem = (CpqAccessSystem) iterator.next();
                    list.add(cpqAccessSystem.getAccessSystemNo());
                }
            }
            log.info("init accessSystem stop------------------");
        } catch (Exception e) {
            log.error("accessSystem initialization failed!", e.getMessage());
            throw e;
        }

    }

}
*/
