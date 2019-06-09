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
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqDicBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqDicService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;

@Component
public class InitDic implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(InitDic.class);

	@Autowired
	private CeqDicService ceqDicService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			log.info("init dic start------------------");
			List<String> allType = ceqDicService.findAllDicType();
			for (String type : allType) {
				RMap<String, String> mapType = redis.getMap(type + EntCacheConstant.DIC_E_K);
				mapType.clear();
				List<CeqDicBo> dicList = ceqDicService.findByType(type);
				for (CeqDicBo cpqDic : dicList) {
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
