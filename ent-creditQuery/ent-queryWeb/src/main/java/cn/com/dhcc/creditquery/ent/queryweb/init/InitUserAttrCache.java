package cn.com.dhcc.creditquery.ent.queryweb.init;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqUserAttrBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqUserAttrService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitUserAttrCache implements InitializingBean {
	
	@Autowired
	private CeqUserAttrService attrService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init userAttrCache begin...");
		RMap<String, CeqUserAttrBo> cache = redis.getMap(EntCacheConstant.USER_ATTR_CACHE);
		log.debug("search CpqUserAttrBo data by DB begin");
		List<CeqUserAttrBo> attrList = attrService.findAll();
		log.debug("search CpqUserAttrBo data by DB end, data sum = {}",attrList.size());
		for (CeqUserAttrBo cpqUserAttrBo : attrList) {
			cache.put(cpqUserAttrBo.getUserName(), cpqUserAttrBo);
		}
		log.info("init userAttrCache end...");
	}

}
