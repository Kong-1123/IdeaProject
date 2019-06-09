package cn.com.dhcc.creditquery.person.queryweb.init;

import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.querycontrol.CpqUnauthorizedCustomerBo;
import cn.com.dhcc.creditquery.person.querycontrol.service.authorizedcustomer.CpqUnauthorizedCustomerService;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
	private CpqUnauthorizedCustomerService cpqCustomerService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void afterPropertiesSet() throws Exception {

		try {
			log.info("UnauthorizedCustomer initialization start...");

			RList<String> rList = redis.getList(PersonCacheConstant.UnCUS_P_K);
			rList.clear();
			List<CpqUnauthorizedCustomerBo> list = cpqCustomerService.findAll();
			if (null != list) {
				for (CpqUnauthorizedCustomerBo cpqUnauthorizedCustomer : list) {
					String cusName = cpqUnauthorizedCustomer.getCusName();
					String cretType = cpqUnauthorizedCustomer.getCretType();
					String cretId = cpqUnauthorizedCustomer.getCretId();
					String deptCode = cpqUnauthorizedCustomer.getDeptCode();
					String md5 = MD5.MD5(cusName + cretType + cretId + deptCode);
					rList.add(md5);
				}
			}

		} catch (Exception e) {
			log.error("UnauthorizedCustomer initialization failed!", e.getMessage());
			throw e;
		}

	}

}
