/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.authorizemanager.service.impl;

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
import cn.com.dhcc.creditquery.person.authorizemanager.dao.CpqArchiveDao;
import cn.com.dhcc.creditquery.person.authorizemanager.entity.CpqArchiveAuthorizeManager;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeManagerService;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 授权管理服务-个人查询授权信息管理服务实现类
 * @author sjk
 * @date 2019年2月21日
 */
@Slf4j
@Service
@Transactional(value = "transactionManager")
public class CpqAuthorizeManagerServiceImpl implements CpqAuthorizeManagerService {

	private RedissonClient redis = RedissonUtil.getLocalRedisson();
	
	@Autowired
	private CpqArchiveDao cpqArchiveDao;
	
	@LogOperation("新增授权信息")
	@Override
	public CpqArchiveBo create(CpqArchiveBo cpqArchiveBo) {
		log.info("CpqAuthorizeManagerServiceImpl method : create request begin,cpqArchiveBo={}",cpqArchiveBo);
		CpqArchiveAuthorizeManager cpqArchiveAuthorizeManager = null;
		try {
			cpqArchiveAuthorizeManager = ClassCloneUtil.copyObject(cpqArchiveBo, CpqArchiveAuthorizeManager.class);
			cpqArchiveAuthorizeManager = cpqArchiveDao.save(cpqArchiveAuthorizeManager);
			cpqArchiveBo = ClassCloneUtil.copyObject(cpqArchiveAuthorizeManager, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("create Archive Failure,error={},cpqArchiveAuthorizeManager={}",e,cpqArchiveAuthorizeManager);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : create response end,cpqArchiveBo={}",cpqArchiveBo);
		return cpqArchiveBo;
	}

	@LogOperation("更新授权信息")
	@Override
	public int update(CpqArchiveBo cpqArchiveBo) {
		log.info("CpqAuthorizeManagerServiceImpl method : update request begin,cpqArchiveBo={}",cpqArchiveBo);
		CpqArchiveAuthorizeManager cpqArchiveAuthorizeManager = null;
		int successFlag = 0;
		try {
			cpqArchiveAuthorizeManager = ClassCloneUtil.copyObject(cpqArchiveBo, CpqArchiveAuthorizeManager.class);
			cpqArchiveDao.save(cpqArchiveAuthorizeManager);
		} catch (Exception e) {
			log.error("update Archive Failure,error={},cpqArchiveAuthorizeManager={}",e,cpqArchiveAuthorizeManager);
			successFlag = -1;
		}
		log.info("CpqAuthorizeManagerServiceImpl method : update response end,successFlag={}",successFlag);
		return successFlag;
	}

	@LogOperation("删除授权信息")
	@Override
	public int deleteById(String cpqArchiveId) {
		log.info("CpqAuthorizeManagerServiceImpl method : deleteById request begin,cpqArchiveId={}",cpqArchiveId);
		int successFlag = 0;
		try {
			cpqArchiveDao.delete(cpqArchiveId);
		} catch (Exception e) {
			log.error("deleteById Archive Failure,error={}",e);
		    successFlag = -1;
		}
		log.info("CpqAuthorizeManagerServiceImpl method : deleteById response end,successFlag={}",successFlag);
		return successFlag;
	}

	@LogOperation("批量删除授权信息")
	@Override
	public int deleteByIds(List<String> cpqArchiveIds) {
		log.info("CpqAuthorizeManagerServiceImpl method : deleteByIds request begin,cpqArchiveIds={}",cpqArchiveIds);
		int listSize = cpqArchiveIds.size();
		int toIndex = 1000;
		int successFlag = 0;
		try {
			for (int i = 0; i < cpqArchiveIds.size(); i += 1000) {
				//作用为toIndex最后没有1000条数据则剩余几条List中就装几条
				if(i + 1000 > listSize) {
					toIndex = listSize -1;
				}
				List<String> list = cpqArchiveIds.subList(i, i+toIndex);
				log.info("CpqAuthorizeManagerServiceImpl method : deleteByIds request begin,list={}",list);
				cpqArchiveDao.deleteIds(list);
			}
		} catch (Exception e) {
			log.error("deleteByIds Archive Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CpqAuthorizeManagerServiceImpl method : deleteById response end,successFlag={}",successFlag);
		return successFlag;
	}

	@Override
	public CpqArchiveBo findById(String archiveId) {
		log.info("CpqAuthorizeManagerServiceImpl method : findById request begin,archiveId={}",archiveId);
		CpqArchiveBo cpqArchiveBo = null;
		CpqArchiveAuthorizeManager cpqArchiveAuthorizeManager = null;
		try {
			cpqArchiveAuthorizeManager = cpqArchiveDao.findOne(archiveId);
			cpqArchiveBo = ClassCloneUtil.copyObject(cpqArchiveAuthorizeManager, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("findById Archive Failure,error={},cpqArchiveAuthorizeManager={}",e,cpqArchiveAuthorizeManager);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : findById response end,cpqArchiveBo={}",cpqArchiveBo);
		return cpqArchiveBo;
	}

	@Override
	public Page<CpqArchiveBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy) {
		log.info("CpqAuthorizeManagerServiceImpl method : getPage request begin,searchParams={},pageNumber={},"
				+ "pageSize={},direction={},orderBy={}",searchParams,pageNumber,pageSize,direction,orderBy);
		Page<CpqArchiveAuthorizeManager> cpqArchivePage = null;
		Page<CpqArchiveBo> cpqArchiveBoPage = null;
		try {
			cpqArchivePage = PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, cpqArchiveDao, 
					CpqArchiveAuthorizeManager.class);
			cpqArchiveBoPage = ClassCloneUtil.copyPage(cpqArchivePage, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("getPage Archive Failure,error={}",e);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : getPage response end,cpqArchiveBoPage={}",cpqArchiveBoPage);
		return cpqArchiveBoPage;
	}

	@Override
	public void updateStatus(String status, String id) {
		log.info("CpqAuthorizeManagerServiceImpl method : updateStatus request begin,status={},id={}",status,id);
		try {
			cpqArchiveDao.updateStatus(status, id);
		} catch (Exception e) {
			log.error("updateStatus Archive Failure,error={}",e);
		}
	}

	@Override
	public List<CpqArchiveBo> findByCustomerIdentify(String customerName, String certType, String certNo) {
		log.info("CpqAuthorizeManagerServiceImpl method : findByCustomerIdentify request begin,"
				+ "customerName={},certType={},certNo={}",customerName,certType,certNo);
		List<CpqArchiveAuthorizeManager> cpqArchiveList = null;
		List<CpqArchiveBo> cpqArchiveBoList = null;
		try {
			cpqArchiveList = cpqArchiveDao.findByThreeStandardization(customerName, certType, certNo);
			cpqArchiveBoList = ClassCloneUtil.copyIterableObject(cpqArchiveList, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("findByCustomerIdentify Archive Failure,error={}",e);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : findByCustomerIdentify response end cpqArchiveBoList={}",cpqArchiveBoList);
		return cpqArchiveBoList;
	}

	@Override
	public List<CpqArchiveBo> findAll(Map<String, Object> param) {
		log.info("CpqAuthorizeManagerServiceImpl method : findAll request begin param={}",param);
		List<CpqArchiveBo> cpqArchiveBoList = null;
		try {
			Specification<CpqArchiveAuthorizeManager> spec = PageUtil.buildSpecification(param, CpqArchiveAuthorizeManager.class);
			List<CpqArchiveAuthorizeManager> cpqArchivesList = cpqArchiveDao.findAll(spec);
			cpqArchiveBoList = ClassCloneUtil.copyIterableObject(cpqArchivesList, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("findAll Archive Failure,error={}",e);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : findAll response end cpqArchiveBoList={}",cpqArchiveBoList);
		return cpqArchiveBoList;
		
	}

	@Override
	public List<CpqArchiveBo> findByIds(List<String> ids) {
		log.info("CpqAuthorizeManagerServiceImpl method : findByIds request begin ids={}",ids);
		List<CpqArchiveAuthorizeManager> cpqArchvieList = null;
		List<CpqArchiveBo> cpqArchiveBoList = null;
		try {
			cpqArchvieList = cpqArchiveDao.findByIds(ids);
			cpqArchiveBoList = ClassCloneUtil.copyIterableObject(cpqArchvieList, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("findByIds Archive Failure,error={},cpqArchvieList={}",e,cpqArchvieList);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : findByIds response end cpqArchiveBoList={}",cpqArchiveBoList);
		return cpqArchiveBoList;
	}

	@Override
	public RLock getRlockForSavePaer(String userId) {
		log.info("CpqAuthorizeManagerServiceImpl method : getRlockForSavePaer request begin userId={}",userId);
		return redis.getLock(userId);
	}

	public RLock getRlockForUpdate() {
		return redis.getLock(PersonCacheConstant.UPDATE_QUERYNUM_REDISLOCK_KEY);
	}
	
	@Override
	public boolean updateQueryNumByRedis(String id) {
		log.info("CpqAuthorizeManagerServiceImpl method : updateQueryNumByRedis request begin id={}",id);
		RLock lock = redis.getLock(PersonCacheConstant.UPDATE_QUERYNUM_REDISLOCK_KEY);
		long nowQueryNum = 0;
		try {
			if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
				nowQueryNum = getQueryNumRAtomicLongByRedis(id).incrementAndGet();
				cpqArchiveDao.updateQueryNumById(nowQueryNum, id);
			}
		} catch (InterruptedException e) {
			log.error("updateQueryNumByRedis Archive Failure,error={},nowQueryNum={}",e,nowQueryNum);
			return false;
		} finally {
			lock.unlock();
		}
		log.info("CpqAuthorizeManagerServiceImpl method : updateQueryNumByRedis response end");
		return true;
	}

	private RAtomicLong getQueryNumRAtomicLongByRedis(String id) {
		RAtomicLong queryNumByRedis = null;
		CpqArchiveAuthorizeManager archive = null;
		try {
			queryNumByRedis = redis.getAtomicLong(id + PersonCacheConstant.QUERYNUM_REDIS_KEY);
			if (queryNumByRedis.get() == 0) {
				archive = cpqArchiveDao.findOne(id);
				queryNumByRedis.set(archive.getQueryNum());
			}
		} catch (Exception e) {
			log.error("getQueryNumRAtomicLongByRedis Archive Failure,error={},archive={}",e,archive);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : getQueryNumRAtomicLongByRedis response end");
		return queryNumByRedis;
	}

	@Override
	public long getQueryNumByRedis(String id) {
		log.info("CpqAuthorizeManagerServiceImpl method : getQueryNumByRedis request begin id={}",id);
		CpqArchiveAuthorizeManager archive = null;
		RAtomicLong queryNumByRedis = null;
		try {
			queryNumByRedis = redis.getAtomicLong(id + PersonCacheConstant.QUERYNUM_REDIS_KEY);
			if (queryNumByRedis.get() == 0) {
				archive = cpqArchiveDao.findOne(id);
				queryNumByRedis.set(archive.getQueryNum());
			}
		} catch (Exception e) {
			log.error("getQueryNumByRedis Archive Failure,error={},archive={}",e,archive);
		}
		log.info("CpqAuthorizeManagerServiceImpl method : getQueryNumByRedis response end");
		return queryNumByRedis.get();
	}
	
	/**
	 * 获取档案文件类型
	 */
	@Override
	public JSONObject getFileTypeForArchive(String topOrg) {
		log.info("getFileTypeForArchive begin,topOrg = {} ",topOrg);
		JSONArray json = new JSONArray();
		String num = ConfigUtil.getMaxArchiveFileNum(topOrg);
		String archiveType = ConfigUtil.getArchiveFileType(topOrg);
		String fileClassConfig = ConfigUtil.getArchiveFileClass(topOrg);
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
	public CpqArchiveBo findByReqId(String reqId) {
		log.info("CeqAuthorizeManagerServiceImpl method : findByReqId request begin reqId={}",reqId);
		CpqArchiveBo cpqArchiveBo = null;
		try {
			CpqArchiveAuthorizeManager cpqArchiveAuthorizeManager = cpqArchiveDao.findByReqId(reqId);
			cpqArchiveBo = ClassCloneUtil.copyObject(cpqArchiveAuthorizeManager, CpqArchiveBo.class);
		} catch (Exception e) {
			log.error("findByReqId CeqAuthorizeManagerBo Failure,error={}",e);
		}
		log.info("CeqAuthorizeManagerServiceImpl method : findByReqId response end");
		return cpqArchiveBo;
	}
}
