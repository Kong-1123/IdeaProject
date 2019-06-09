package cn.com.dhcc.creditquery.person.queryweb.init;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqConfigBo;
import cn.com.dhcc.creditquery.person.queryconfig.entity.CpqConfig;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqConfigService;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigConstants;

/**
 *
 * @author lekang.liu
 * @date 2018年3月10日
 */
@Component
public class InitConfig implements InitializingBean{

    private static Logger log = LoggerFactory.getLogger(InitConfig.class);

    @Autowired
    private CpqConfigService cpqConfigService;

    private RedissonClient redis = RedissonUtil.getLocalRedisson();

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            log.info("config initialization start...");
            RMap<String, String> map = redis.getMap(PersonCacheConstant.CONFIG_KEY);
            map.clear();
            List<CpqConfigBo> configList = (List<CpqConfigBo>) cpqConfigService.findAll();
            if (null != configList && configList.size() != 0) {
                for (CpqConfigBo cpqConfig : configList) {
                	String redisKey = cpqConfig.getConfigName();
                	if(!ConfigConstants.SYSTEMURL.equals(cpqConfig.getConfigName())){
                		redisKey = redisKey+cpqConfig.getOrgId();
                    }
                    map.put(redisKey, cpqConfig.getConfigValue());
                }
                
            }
        }catch(Exception e){
            log.error("config initialization failed!",e.getMessage());
            throw e;
        }

            
    }

}
