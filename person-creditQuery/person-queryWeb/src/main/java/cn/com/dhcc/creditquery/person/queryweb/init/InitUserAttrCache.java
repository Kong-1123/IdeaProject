package cn.com.dhcc.creditquery.person.queryweb.init;

import java.util.List;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqUserAttrService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitUserAttrCache implements InitializingBean {
	
	@Autowired
	private CpqUserAttrService attrService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init userAttrCache begin...");
		RMap<String, CpqUserAttrBo> cache = redis.getMap(PersonCacheConstant.USER_ATTR_CACHE);
		log.debug("search CpqUserAttrBo data by DB begin");
		List<CpqUserAttrBo> attrList = attrService.findAll();
		log.debug("search CpqUserAttrBo data by DB end, data sum = {}",attrList.size());
		for (CpqUserAttrBo cpqUserAttrBo : attrList) {
			cache.put(cpqUserAttrBo.getUserName(), cpqUserAttrBo);
		}
		log.info("init userAttrCache end...");
	}

}
