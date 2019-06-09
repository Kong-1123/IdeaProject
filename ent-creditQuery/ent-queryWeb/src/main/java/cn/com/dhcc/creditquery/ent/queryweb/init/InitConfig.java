package cn.com.dhcc.creditquery.ent.queryweb.init;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqConfigBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqConfigService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
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
    private CeqConfigService ceqConfigService;

    private RedissonClient redis = RedissonUtil.getLocalRedisson();

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            log.info("config initialization start...");
            RMap<String, String> map = redis.getMap(EntCacheConstant.CONFIG_KEY);
            map.clear();
            List<CeqConfigBo> configList = (List<CeqConfigBo>) ceqConfigService.findAll();
            if (null != configList && configList.size() != 0) {
                for (CeqConfigBo ceqconfig : configList) {
                	String redisKey = ceqconfig.getConfigName();
                	if(!ConfigConstants.SYSTEMURL.equals(ceqconfig.getConfigName())){
                		redisKey = redisKey+ceqconfig.getOrgId();
                    }
                    map.put(redisKey, ceqconfig.getConfigValue());
                }
                
            }
        }catch(Exception e){
            log.error("config initialization failed!",e.getMessage());
            throw e;
        }

            
    }

}
