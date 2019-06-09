package cn.com.dhcc.creditquery.ent.queryweb.init;

import java.util.List;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.querycontrol.CeqUnauthorizedCustomerBo;
import cn.com.dhcc.creditquery.ent.querycontrol.service.authorizedcustomer.CeqUnauthorizedCustomerService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;

/**
 *
 * 加密末结清用户的三项标识+法人机构，并初始化到redis中
 *
 * @author pjy
 *
 */
@Component
public class InitUnauthorizedCustomer implements InitializingBean {

	private static Logger log = LoggerFactory.getLogger(InitUnauthorizedCustomer.class);

	@Autowired
	private CeqUnauthorizedCustomerService ceqCustomerService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void afterPropertiesSet() throws Exception {

		try {
			log.info("UnauthorizedCustomer initialization start...");

			RList<String> rList = redis.getList(EntCacheConstant.UnCUS_P_K);
			rList.clear();
			List<CeqUnauthorizedCustomerBo> list = ceqCustomerService.findAll();
			if (null != list) {
				for (CeqUnauthorizedCustomerBo ceqUnauthorizedCustomer : list) {
				
					String singCode = ceqUnauthorizedCustomer.getSignCode();
					String md5 = MD5.MD5(singCode);
					rList.add(md5);
				}
			}

		} catch (Exception e) {
			log.error("UnauthorizedCustomer initialization failed!", e.getMessage());
			throw e;
		}

	}

}
