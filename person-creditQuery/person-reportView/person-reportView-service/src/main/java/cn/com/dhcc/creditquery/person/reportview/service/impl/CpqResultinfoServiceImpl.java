package cn.com.dhcc.creditquery.person.reportview.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo.Menu;
import cn.com.dhcc.creditquery.person.reportview.dao.CpqResultinfoViewDao;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqResultinfoView;
import cn.com.dhcc.creditquery.person.reportview.service.CpqResultinfoService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.InConstant;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;
import cn.com.dhcc.query.creditquerycommon.util.TimeTransferUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(value = "transactionManager")
public class CpqResultinfoServiceImpl implements CpqResultinfoService {
	
	private RedissonClient redis = RedissonUtil.getLocalRedisson();
	@Autowired
	CpqResultinfoViewDao resultinfoDao;

	@Override
	public CpqResultinfoView create(CpqResultinfoView resultinfo) {
		return resultinfoDao.save(resultinfo);
	}

	@Override
	public CpqResultinfoView update(CpqResultinfoView resultinfo) {
		return resultinfoDao.save(resultinfo);
	}

	@Override
	public CpqResultinfoView findById(String id) {
		return resultinfoDao.findOne(id);
	}

	@Override
	public Page<CpqResultinfoView> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy) {
		return PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, resultinfoDao,
				CpqResultinfoView.class);
	}

	public CpqResultinfoView updateRedisByCpqResultinfo(CpqResultinfoView resultinfo) {
		RLock lock = redis.getLock(PersonCacheConstant.USER_QUERY_NUN_LOCK);
		try {
			if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
				if (InConstant.RECHECK_SYNC.equals(resultinfo.getCheckWay())) {
					setUserQueryNumbyRedis(resultinfo.getOperator());
				}
				resultinfo = resultinfoDao.save(resultinfo);
			}
		} catch (InterruptedException e) {
			log.error("updateRedisByCpqResultinfo error :", e);
			resultinfo.setErrorInfo(e.getMessage());
			return resultinfo;
		} finally {
			lock.unlock();
		}
		return resultinfo;
	}

	public long getCurrentUserQueryNum(String username) {
		log.info("-getCurrentUserQueryNum username = {}", username);
		RMap<String, Long> cache = redis.getMap(PersonCacheConstant.USER_QUERY_NUN);
		if (null == cache || cache.size() == 0) {
			long expireTime = TimeTransferUtil.getExpireTimeAtMiliSecond(InConstant.HOUR_CLEAR_IN_REDISH,
					InConstant.MIN_CLEAR_IN_REDISH, InConstant.SECOND_CLEAR_IN_REDISH);
			cache.put(username,0L);
			cache.expireAt(expireTime);
		}
		Long currentUserQueryNum = cache.get(username);
		currentUserQueryNum = (null ==currentUserQueryNum?0:currentUserQueryNum);
		log.info("getCurrentUserQueryNum currentUserQueryNum = {}", currentUserQueryNum);
		return currentUserQueryNum;
	}

	/**
	 * set expire
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public void setUserQueryNumbyRedis(String username) {
		RMap<String, Long> cache = redis.getMap(PersonCacheConstant.USER_QUERY_NUN);
		long currentNum = getCurrentUserQueryNum(username) + 1;
		cache.put(username, currentNum);
	}

	/**
	 * 0-授权书 1-关联业务数据 2-全部 3-两者之一
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param direction
	 * @param orderBy
	 * @return
	 */
	public Page<CpqResultinfoView> getRelevancePage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy) {
		String statusRevise = ConfigUtil.getAfterloanArchiveAmended();
		String revise = "";
		if (0 == Integer.parseInt(statusRevise)) {
			revise = "1,2,3,4";
			searchParams.put("EQ_archiveRevise", revise);
		}
		if (1 == Integer.parseInt(statusRevise) || 2 == Integer.parseInt(statusRevise)) {
			revise = "3,4";
			searchParams.put("IN_archiveRevise", revise);
		}
		if (3 == Integer.parseInt(statusRevise)) {
			revise = "4";
			searchParams.put("EQ_archiveRevise", revise);
		}
		return PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, resultinfoDao,
				CpqResultinfoView.class);
	}

	public Page<CpqResultinfoView> getArchiveRevisePage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy) {
		String statusRevise = ConfigUtil.getAfterloanArchiveAmended();
		String revise = "";
		//archiveRevise 档案补录类型（1-非贷后无资料，2-贷后无资料，3-贷后无借据编号，4-贷后两者皆无）
		if (0 == Integer.parseInt(statusRevise)) {
			revise = "1,2,4";
			searchParams.put("IN_archiveRevise", revise);
		}
		if (1 == Integer.parseInt(statusRevise)) {
			revise = "1";
			searchParams.put("EQ_archiveRevise", revise);
		}
		if (2 == Integer.parseInt(statusRevise)) {
			revise = "1,2,4";
			searchParams.put("IN_archiveRevise", revise);
		}
		if (3 == Integer.parseInt(statusRevise)) {
			revise = "1,4";
			searchParams.put("IN_archiveRevise", revise);
		}
		return PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, resultinfoDao,
				CpqResultinfoView.class);
	}

	@Override
	public void updateAssociateById(String id, String associate, String archiveRevise) {
		resultinfoDao.updateAssociationById(id, associate, archiveRevise);
	}

	@Override
	public void updateArchiveIdById(String id, String archiveId) {
		resultinfoDao.updateArchiveIdById(id, archiveId);
	}

	@Override
	public Date getLastQueryTimeByArchiveId(String archiveId) {
		return resultinfoDao.getLastQueryTimeByArchiveId(archiveId);
	}

	@Override
	public CpqResultinfoView findByCreditId(String creditId) {
		List<CpqResultinfoView> resultInfoList = resultinfoDao.findbyCreditId(creditId);
		return resultInfoList.get(0);
	}

	@Override
	public List<CpqResultinfoView> findByCcuser(String ccuser) {
		return resultinfoDao.findByCcuser(ccuser);
	}

	/* 
	 * @see cn.com.dhcc.query.creditpersonqueryservice.queryreq.service.CpqResultinfoService#findByArchiveCount(java.util.Date, java.util.List)
	 */
	@Override
	public List<CpqResultinfoView> findByArchiveCount(String qryReason, List<String> deptCodes) {
		List<CpqResultinfoView> count = resultinfoDao.findByArchiveCount(qryReason,deptCodes);
		return count;
	}

	//档案补录menuId
	private static final String ACHIVEMENUID ="20010103";
	//用户管理ID
//	private static final String USERMENUID ="10020000";

	private static final String NAME="档案补录";

	private static final String MENULINK="/creditpersonqueryweb/archive/archiverevise";

	private static final String TITLE="授权补录";


	@Deprecated
	@Override
	public CpqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds) {
		List<CpqResultinfoView> resultList=null;
//		Date date = getToday();
		CpqShortcutBo cutVo = new CpqShortcutBo();
		List<Menu> menusList = new ArrayList<>();
		String qryReason="01";//贷后管理
		if(menuIds.contains(ACHIVEMENUID)){
			resultList = this.findByArchiveCount(qryReason,deptCodes);
			Menu me = new Menu();
			me.setName(NAME);
			me.setLink(MENULINK);
			me.setCount(resultList.size());
			menusList.add(me);
		}else{
			resultList = Collections.emptyList();
		}
		cutVo.setMenus(menusList);
		cutVo.setTitle(TITLE);
		return cutVo;
	}
	/**
	 * 去除时分秒后的当前日期
	 * @return
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private Date getToday(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
