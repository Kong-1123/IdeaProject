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
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqDicBo;
import cn.com.dhcc.creditquery.person.queryconfig.entity.CpqDic;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqDicService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

@Component
public class InitDic implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(InitDic.class);

	@Autowired
	private CpqDicService cpqDicService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			log.info("init dic start------------------");
			List<String> allType = cpqDicService.findAllDicType();
			for (String type : allType) {
				RMap<String, String> mapType = redis.getMap(type + PersonCacheConstant.DIC_P_K);
				mapType.clear();
				List<CpqDicBo> dicList = cpqDicService.findByType(type);
				for (CpqDicBo cpqDic : dicList) {
					mapType.put(cpqDic.getCode(), cpqDic.getValue());
				}
			}
			log.info("init dic stop------------------");
		} catch (Exception e) {
			log.error("dic initialization failed!", e.getMessage());
			throw e;
		}

	}

}
