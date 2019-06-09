/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.authorizemanager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.authorizemanager.dao.CeqAuthorizeManagerDao;
import cn.com.dhcc.creditquery.ent.authorizemanager.entity.CeqAuthorizeManager;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeManagerService;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 授权管理服务-企业查询授权信息管理服务实现
 * 
 * @author sjk
 * @date 2019年3月19日
 */
@Slf4j
@Service
@Transactional(value = "transactionManager")
public class CeqAuthorizeManagerServiceImpl implements CeqAuthorizeManagerService {

	private RedissonClient redis = RedissonUtil.getLocalRedisson();
	@Autowired
	private CeqAuthorizeManagerDao ceqAuthorizeManagerDao;

	@LogOperation("新增企业授权信息")
	@Override
	public CeqAuthorizeManagerBo create(CeqAuthorizeManagerBo ceqAuthorizeManagerBo) {
		log.info("CeqAuthorizeManagerServiceImpl method : create request begin,ceqAuthorizeManagerBo={}",ceqAuthorizeManagerBo);
		CeqAuthorizeManager ceqAuthorizeManager = null;
		try {
			ceqAuthorizeManager = ClassCloneUtil.copyObject(ceqAuthorizeManagerBo,CeqAuthorizeManager.class);
			ceqAuthorizeManager = ceqAuthorizeManagerDao.save(ceqAuthorizeManager);
			ceqAuthorizeManagerBo = ClassCloneUtil.copyObject(ceqAuthorizeManager,CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("create Authorize Failure,error={},ceqAuthorizeManager={}", e, ceqAuthorizeManager);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : create response end,ceqAuthorizeManagerBo={}",ceqAuthorizeManagerBo);
		return ceqAuthorizeManagerBo;
	}

	@LogOperation("更新授权信息")
	@Override
	public int update(CeqAuthorizeManagerBo ceqAuthorizeManagerBo) {
		log.info("CeqAuthorizeManagerServiceImpl method : update request begin,ceqAuthorizeManagerBo={}",ceqAuthorizeManagerBo);
		CeqAuthorizeManager ceqAuthorizeManager = null;
		int successFlag = 0;
		try {
			ceqAuthorizeManager = ClassCloneUtil.copyObject(ceqAuthorizeManagerBo, CeqAuthorizeManager.class);
			ceqAuthorizeManagerDao.save(ceqAuthorizeManager);
		} catch (Exception e) {
			log.error("update Authorize Failure,error={},ceqAuthorizeManager={}",e,ceqAuthorizeManager);
			successFlag = -1;
		}
		log.info("CeqAuthorizeManagerServiceImpl method : update response end,successFlag={}",successFlag);
		return successFlag;
	}

	@LogOperation("删除授权信息")
	@Override
	public int deleteById(String ceqAuthorizeId) {
		log.info("CeqAuthorizeManagerServiceImpl method : deleteById request begin,ceqAuthorizeId={}",ceqAuthorizeId);
		int successFlag = 0;
		try {
			ceqAuthorizeManagerDao.delete(ceqAuthorizeId);
		} catch (Exception e) {
			log.error("deleteById Authorize Failure,error={}",e);
		    successFlag = -1;
		}
		log.info("CeqAuthorizeManagerServiceImpl method : deleteById response end,successFlag={}",successFlag);
		return successFlag;
	}

	@LogOperation("批量删除授权信息")
	@Override
	public int deleteByIds(List<String> ceqAuthorizeIds) {
		log.info("CeqAuthorizeManagerServiceImpl method : deleteByIds request begin,ceqAuthorizeIds={}",ceqAuthorizeIds);
		int listSize = ceqAuthorizeIds.size();
		int toIndex = 1000;
		int successFlag = 0;
		try {
			for (int i = 0; i < ceqAuthorizeIds.size(); i += 1000) {
				//作用为toIndex最后没有1000条数据则剩余几条List中就装几条
				if(i + 1000 > listSize) {
					toIndex = listSize -1;
				}
				List<String> list = ceqAuthorizeIds.subList(i, i+toIndex);
				log.info("CeqAuthorizeManagerServiceImpl method : deleteByIds request begin,list={}",list);
				ceqAuthorizeManagerDao.deleteIds(list);
			}
		} catch (Exception e) {
			log.error("deleteByIds Authorize Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CeqAuthorizeManagerServiceImpl method : deleteById response end,successFlag={}",successFlag);
		return successFlag;
	}

	@Override
	public CeqAuthorizeManagerBo findById(String authorizeId) {
		log.info("CeqAuthorizeManagerServiceImpl method : findById request begin,authorizeId={}",authorizeId);
		CeqAuthorizeManagerBo ceqAuthorizeManagerBo = null;
		CeqAuthorizeManager ceqAuthorizeManager = null;
		try {
			ceqAuthorizeManager = ceqAuthorizeManagerDao.findOne(authorizeId);
			ceqAuthorizeManagerBo = ClassCloneUtil.copyObject(ceqAuthorizeManager, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("findById Authorize Failure,error={},ceqAuthorizeManager={}",e,ceqAuthorizeManager);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findById response end,ceqAuthorizeManagerBo={}",ceqAuthorizeManagerBo);
		return ceqAuthorizeManagerBo;
	}

	@Override
	public Page<CeqAuthorizeManagerBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy) {
		log.info("CeqAuthorizeManagerServiceImpl method : getPage request begin,searchParams={},pageNumber={},"
				+ "pageSize={},direction={},orderBy={}",searchParams,pageNumber,pageSize,direction,orderBy);
		Page<CeqAuthorizeManager> ceqAuthorizePage = null;
		Page<CeqAuthorizeManagerBo> ceqAuthorizeBoPage = null;
		try {
			ceqAuthorizePage = PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, ceqAuthorizeManagerDao, 
					CeqAuthorizeManager.class);
			ceqAuthorizeBoPage = ClassCloneUtil.copyPage(ceqAuthorizePage, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("getPage Authorize Failure,error={}",e);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : getPage response end,ceqAuthorizeBoPage={}",ceqAuthorizeBoPage);
		return ceqAuthorizeBoPage;
	}

	@Override
	public void updateStatus(String status, String authorizeId) {
		log.info("CeqAuthorizeManagerServiceImpl method : updateStatus request begin,status={},authorizeId={}",status,authorizeId);
		try {
			ceqAuthorizeManagerDao.updateStatus(status, authorizeId);
		} catch (Exception e) {
			log.error("updateStatus Authorize Failure,error={}",e);
		}
	}

	@Override
	public List<CeqAuthorizeManagerBo> findBySignCode(String signCode) {
		log.info("CeqAuthorizeManagerServiceImpl method : findByCustomerIdentify request begin signCode={}",signCode);
		List<CeqAuthorizeManager> ceqAuthorizeList = null;
		List<CeqAuthorizeManagerBo> ceqAuthorizeBoList = null;
		try {
			ceqAuthorizeList = ceqAuthorizeManagerDao.findByThreeStandardization(signCode);
			ceqAuthorizeBoList = ClassCloneUtil.copyIterableObject(ceqAuthorizeList, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("findByCustomerIdentify Authorize Failure,error={}",e);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findByCustomerIdentify response end ceqAuthorizeBoList={}",ceqAuthorizeBoList);
		return ceqAuthorizeBoList;
	}

	@Override
	public List<CeqAuthorizeManagerBo> findAll(Map<String, Object> param) {
		log.info("CeqAuthorizeManagerServiceImpl method : findAll request begin param={}",param);
		List<CeqAuthorizeManagerBo> ceqAuthorizeBoList = null;
		try {
			Specification<CeqAuthorizeManager> spec = PageUtil.buildSpecification(param, CeqAuthorizeManager.class);
			List<CeqAuthorizeManager> ceqAuthorizeList = ceqAuthorizeManagerDao.findAll(spec);
			ceqAuthorizeBoList = ClassCloneUtil.copyIterableObject(ceqAuthorizeList, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("findAll Authorize Failure,error={}",e);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findAll response end ceqAuthorizeBoList={}",ceqAuthorizeBoList);
		return ceqAuthorizeBoList;
		
	}

	@Override
	public List<CeqAuthorizeManagerBo> findByIds(List<String> authorizeIdList) {
		log.info("CepqAuthorizeManagerServiceImpl method : findByIds request begin authorizeIdList={}",authorizeIdList);
		List<CeqAuthorizeManager> ceqAuthorizeList = null;
		List<CeqAuthorizeManagerBo> ceqAuthorizeBoList = null;
		try {
			ceqAuthorizeList = ceqAuthorizeManagerDao.findByIds(authorizeIdList);
			ceqAuthorizeBoList = ClassCloneUtil.copyIterableObject(ceqAuthorizeList, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("findByIds Authorize Failure,error={},ceqArchvieList={}",e,ceqAuthorizeList);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findByIds response end ceqAuthorizeBoList={}",ceqAuthorizeBoList);
		return ceqAuthorizeBoList;
	}

	@Override
	public long getQueryNumByRedis(String authorizeId) {
		log.info("CeqAuthorizeManagerServiceImpl method : getQueryNumByRedis request begin authorizeId={}",authorizeId);
		CeqAuthorizeManager authorize = null;
		RAtomicLong queryNumByRedis = null;
		try {
			queryNumByRedis = redis.getAtomicLong(authorizeId + EntCacheConstant.QUERYNUM_REDIS_KEY);
			if (queryNumByRedis.get() == 0) {
				authorize = ceqAuthorizeManagerDao.findOne(authorizeId);
				queryNumByRedis.set(authorize.getQueryNum());
			}
		} catch (Exception e) {
			log.error("getQueryNumByRedis Authorize Failure,error={},authorize={}",e,authorize);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : getQueryNumByRedis response end");
		return queryNumByRedis.get();
	}

	@Override
	public RLock getRlockForSavePaer(String userId) {
		log.info("CeqAuthorizeManagerServiceImpl method : getRlockForSavePaer request begin userId={}",userId);
		return redis.getLock(userId);
	}

	@Override
	public RLock getRlockForUpdate() {
		return redis.getLock(EntCacheConstant.UPDATE_QUERYNUM_REDISLOCK_KEY);
	}

	@Override
	public boolean updateQueryNumByRedis(String authorizeId) {
		log.info("CeqAuthorizeManagerServiceImpl method : updateQueryNumByRedis request begin authorizeId={}",authorizeId);
		RLock lock = redis.getLock(EntCacheConstant.UPDATE_QUERYNUM_REDISLOCK_KEY);
		long nowQueryNum = 0;
		try {
			if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
				nowQueryNum = getQueryNumRAtomicLongByRedis(authorizeId).incrementAndGet();
				ceqAuthorizeManagerDao.updateQueryNumById(nowQueryNum, authorizeId);
			}
		} catch (InterruptedException e) {
			log.error("updateQueryNumByRedis Authorize Failure,error={},nowQueryNum={}",e,nowQueryNum);
			return false;
		} finally {
			lock.unlock();
		}
		log.info("CeqAuthorizeManagerServiceImpl method : updateQueryNumByRedis response end");
		return true;
	}

	private RAtomicLong getQueryNumRAtomicLongByRedis(String id) {
		RAtomicLong queryNumByRedis = null;
		CeqAuthorizeManager authorize = null;
		try {
			queryNumByRedis = redis.getAtomicLong(id + EntCacheConstant.QUERYNUM_REDIS_KEY);
			if (queryNumByRedis.get() == 0) {
				authorize = ceqAuthorizeManagerDao.findOne(id);
				queryNumByRedis.set(authorize.getQueryNum());
			}
		} catch (Exception e) {
			log.error("getQueryNumRAtomicLongByRedis Authorize Failure,error={},authorize={}",e,authorize);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : getQueryNumRAtomicLongByRedis response end");
		return queryNumByRedis;
	}

	@Override
	public JSONObject getFileTypeForArchive(String topOrg) {
		log.info("getFileTypeForArchive begin,topOrg = {} ",topOrg);
		JSONArray json = new JSONArray();
		String num = CeqConfigUtil.getMaxArchiveFileNum(topOrg);
		String archiveType = CeqConfigUtil.getArchiveFileType(topOrg);
		String fileClassConfig = CeqConfigUtil.getArchiveFileClass(topOrg);
		String[] fileClass = fileClassConfig.split(",");
		for (String string : fileClass) {
			json.add(string);
		}
		JSONObject jo = new JSONObject();
		jo.put("Type", json);
		jo.put("Num", num);
		jo.put("archiveType", archiveType);
		log.info("getFileTypeForArchive begin,jo = {} ",jo.toString());
		return jo;
	}

	@Override
	public CeqAuthorizeManagerBo findByReqId(String reqId) {
		log.info("CeqAuthorizeManagerServiceImpl method : findByReqId request begin reqId={}",reqId);
		CeqAuthorizeManagerBo ceqAuthorizeManagerBo = null;
		try {
			CeqAuthorizeManager ceqAuthorizeManager = ceqAuthorizeManagerDao.findByReqId(reqId);
			ceqAuthorizeManagerBo = ClassCloneUtil.copyObject(ceqAuthorizeManager, CeqAuthorizeManagerBo.class);
		} catch (Exception e) {
			log.error("findByReqId CeqAuthorizeManagerBo Failure,error={}",e);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findByReqId response end");
		return ceqAuthorizeManagerBo;
	}
	
}
