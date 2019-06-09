package cn.com.dhcc.creditquery.person.queryweb.init;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.querycontrol.bo.CpqExceptionRuleBo;
import cn.com.dhcc.creditquery.person.querycontrol.service.exceptionrule.CpqExceptionRuleService;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 初始化数据库中异常查询阻断规则到redis中
 * */
@Component
public class InitCpqExceptionRule implements InitializingBean {

	private static Logger log = LoggerFactory.getLogger(InitCpqExceptionRule.class);


	@Autowired
    private CpqExceptionRuleService cpqExceptionRuleService;

    private RedissonClient redis = RedissonUtil.getLocalRedisson();




	@Override
	public void afterPropertiesSet() throws Exception {


		try {
			log.info("cpqExceptionRule initialization start...");
			RMap<String, CpqExceptionRuleBo> map = redis.getMap(PersonCacheConstant.RULE_P_K);
			map.clear();
			List<CpqExceptionRuleBo> cpqExceptionRuleList = (List<CpqExceptionRuleBo>) cpqExceptionRuleService.findAll();
			if (null != cpqExceptionRuleList && cpqExceptionRuleList.size() != 0) {
				for (CpqExceptionRuleBo cpqExceptionRule : cpqExceptionRuleList) {
					map.put(cpqExceptionRule.getRuleCode()+cpqExceptionRule.getDeptCode(), cpqExceptionRule);
				}

			}

		} catch (Exception e) {
			log.error("cpqExceptionRule initialization failed!",e);
            throw e;
		}


	}

}
