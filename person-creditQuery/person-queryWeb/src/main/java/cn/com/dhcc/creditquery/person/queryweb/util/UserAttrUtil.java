package cn.com.dhcc.creditquery.person.queryweb.util;

import java.util.List;

import javax.annotation.PostConstruct;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqPbocUserBo;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo;
import cn.com.dhcc.creditquery.person.queryconfig.entity.CpqUserAttr;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqPbocUserService;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqUserAttrService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;


@Component
public class UserAttrUtil {
	
	private static Logger log = LoggerFactory.getLogger(UserAttrUtil.class);
	
	private static RedissonClient redis = RedissonUtil.getLocalRedisson();
	
	@Autowired
	private CpqUserAttrService userAttrService;
	
	@Autowired
	private CpqPbocUserService pbocUserService;
	
	private static UserAttrUtil util;
	
	
	@PostConstruct
	public void initService(){
		util = this;
		util.userAttrService = this.userAttrService;
		util.pbocUserService = this.pbocUserService;
	}


	public static CpqUserAttrBo findCpqUserAttrBySystemUserId(String id){
		log.info("findCpqUserAttrBySystemUserId method begin,param is id = {}",id);
		RMap<String, CpqUserAttrBo> cache = redis.getMap(PersonCacheConstant.USER_ATTR_CACHE);
		CpqUserAttrBo userAttr = cache.get(id);
		if(userAttr == null){
			log.info("findCpqUserAttrBySystemUserId method running,cache miss,find by DB,param is id = {}",id);
			userAttr = util.userAttrService.findByUserId(id);
			if(null != userAttr){
			    cache.put(userAttr.getUserName(), userAttr);
			}
		}
		log.info("findCpqUserAttrBySystemUserId method end,result is userAttr = {}",userAttr);
		return userAttr;
	}
	/**
	 * redis中的用户属性信息以userName保存，请通过 {@link UserAttrUtil#deleteByUserName(List)}删除redis中的用户属性信息
	 * @param ids
	 * @return void
	 */
	@Deprecated
	public static void deleteById(List<String> ids){
		RMap<String, CpqUserAttr> cache = redis.getMap(PersonCacheConstant.USER_ATTR_CACHE);
		List<String> userNames = util.userAttrService.findUserNamesByIds(ids);
		for (String username : userNames) {
			cache.remove(username);
		}
	}
	
	public static void deleteByUserName(List<String> userNames){
		RMap<String, CpqUserAttr> cache = redis.getMap(PersonCacheConstant.USER_ATTR_CACHE);
		for (String userName : userNames) {
			cache.remove(userName);
		}
	}
	
	public static void saveOrUpdate(CpqUserAttr userAttr){
		RMap<String, CpqUserAttr> cache = redis.getMap(PersonCacheConstant.USER_ATTR_CACHE);
		cache.put(userAttr.getUserName(), userAttr);
	}
	
	
	/**
	 * 根据征信用户账号获取征信用户信息
	 * @param ccid
	 * @return
	 */
	public static CpqPbocUserBo getCreditUserByCcId(String creditUser) {
	    log.info("getCreditUserNameByCcId  start parm userName = {}",creditUser);
	    RMap<Object, Object> creditUserMap = redis.getMap(PersonCacheConstant.CREDITUSER_REDIS_KEY);
	    CpqPbocUserBo pbocUserBo = (CpqPbocUserBo) creditUserMap.get(creditUser);
	    if(null == pbocUserBo){
	    	pbocUserBo = util.pbocUserService.findByCreditUser(creditUser);
	        if(null != pbocUserBo){
	            creditUserMap.put(creditUser, pbocUserBo);
	        }
	    }
	    log.info("getCreditUserNameByCcId  end result cpqCcUser = {}",pbocUserBo);
	    return pbocUserBo;
    }
	
	
	
}
