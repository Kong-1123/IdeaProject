package cn.com.dhcc.creditquery.ent.queryweb.util;

import javax.annotation.PostConstruct;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqPbocUserBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqUserAttrBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqPbocUserService;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqUserAttrService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;


@Component
public class UserAttrUtil {
	
	private static Logger log = LoggerFactory.getLogger(UserAttrUtil.class);
	
	private static RedissonClient redis = RedissonUtil.getLocalRedisson();
	
	@Autowired
	private CeqUserAttrService userAttrService;
	
	@Autowired
	private CeqPbocUserService pbocUserService;
	
	private static UserAttrUtil util;
	
	
	@PostConstruct
	public void initService(){
		util = this;
		util.userAttrService = this.userAttrService;
		util.pbocUserService = this.pbocUserService;
	}


	public static CeqUserAttrBo findCeqUserAttrBySystemUserId(String id){
		log.info("findCeqUserAttrBySystemUserId method begin,param is id = {}",id);
		RMap<String, CeqUserAttrBo> cache = redis.getMap(EntCacheConstant.USER_ATTR_CACHE);
		CeqUserAttrBo userAttr = cache.get(id);
		if(userAttr == null){
			log.info("findCeqUserAttrBySystemUserId method running,cache miss,find by DB,param is id = {}",id);
			userAttr = util.userAttrService.findByUserId(id);
			if(null != userAttr){
			    cache.put(userAttr.getUserName(), userAttr);
			}
		}
		log.info("findCeqUserAttrBySystemUserId method end,result is userAttr = {}",userAttr);
		return userAttr;
	}
	
	
}
