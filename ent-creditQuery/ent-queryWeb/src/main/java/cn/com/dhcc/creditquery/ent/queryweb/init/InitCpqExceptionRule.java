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
import cn.com.dhcc.creditquery.ent.querycontrol.bo.CeqExceptionRuleBo;
import cn.com.dhcc.creditquery.ent.querycontrol.service.exceptionrule.CeqExceptionRuleService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;


/**
 * 初始化数据库中异常查询阻断规则到redis中
 * */
@Component
public class InitCpqExceptionRule implements InitializingBean {

	private static Logger log = LoggerFactory.getLogger(InitCpqExceptionRule.class);


	@Autowired
    private CeqExceptionRuleService ceqExceptionRuleService;

    private RedissonClient redis = RedissonUtil.getLocalRedisson();




	@Override
	public void afterPropertiesSet() throws Exception {


		try {
			log.info("cpqExceptionRule initialization start...");
			RMap<String, CeqExceptionRuleBo> map = redis.getMap(EntCacheConstant.RULE_E_K);
			map.clear();
			List<CeqExceptionRuleBo> cpqExceptionRuleList = (List<CeqExceptionRuleBo>) ceqExceptionRuleService.findAll();
			if (null != cpqExceptionRuleList && cpqExceptionRuleList.size() != 0) {
				for (CeqExceptionRuleBo cpqExceptionRule : cpqExceptionRuleList) {
					map.put(cpqExceptionRule.getRuleCode()+cpqExceptionRule.getDeptCode(), cpqExceptionRule);
				}

			}

		} catch (Exception e) {
			log.error("cpqExceptionRule initialization failed!",e);
            throw e;
		}


	}

}
