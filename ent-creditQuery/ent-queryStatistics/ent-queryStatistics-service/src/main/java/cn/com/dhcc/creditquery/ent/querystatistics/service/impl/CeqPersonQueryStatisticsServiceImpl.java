/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.querystatistics.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import cn.com.dhcc.credit.platform.util.ArrayUtil;
import cn.com.dhcc.credit.platform.util.HttpClientUtil;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.businessmonitor.dao.CeqAlertDao;
import cn.com.dhcc.creditquery.ent.businessmonitor.entity.CeqAlert;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqAlertBo;
import cn.com.dhcc.creditquery.ent.query.bo.querystatistics.CeqQueryStatisticsBo;
import cn.com.dhcc.creditquery.ent.querystatistics.dao.CeqResultinfoStatisticsDao;
import cn.com.dhcc.creditquery.ent.querystatistics.entity.CeqResultinfoStatistics;
import cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.InConstant;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业征信查询统计服务实现
 * @author yuzhao.xue
 * @date 2019年2月21日
 */
@Slf4j
@Service
public class CeqPersonQueryStatisticsServiceImpl implements CeqPersonQueryStatisticsService {

	private  RedissonClient redis = RedissonUtil.getLocalRedisson();
	private final static String SYSTEMORGURL = "systemOrg/findDepartmentDirectlyUnder";
	@Autowired
	private CeqResultinfoStatisticsDao infoDao;
	@Autowired
	private CeqAlertDao alertDao;

	@Autowired
	private PlantFormInteractiveService plantFormInteractiveService;
	
	@Autowired(required = false)
	@Qualifier("entityManagerFactory")
	private LocalContainerEntityManagerFactoryBean ens;

	private EntityManager getEntityManager() {
		return ens.getNativeEntityManagerFactory().createEntityManager();
	}
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findQueryStatisticsById(java.lang.String)
	 */
	@Override
	public CeqQueryStatisticsBo findQueryStatisticsById(String queryStatisticId) {
		log.info("QueryStatisticsBo findQueryStatisticsById(String id) id{}" + queryStatisticId);
		Optional<CeqResultinfoStatistics> info = infoDao.findById(queryStatisticId);
		log.info("QueryStatisticsBo findQueryStatisticsById(String id) " + info);
		CeqQueryStatisticsBo queryStatisticsBo = ClassCloneUtil.copyObject(info, CeqQueryStatisticsBo.class);
		return queryStatisticsBo;
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#getQueryStatisticPage(java.util.Map, int, int, java.lang.String, java.lang.String[])
	 */
	@Override
	public Page<CeqQueryStatisticsBo> getQueryStatisticPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String... orderBy) {
		log.info("CeqPersonQueryStatisticsServiceImpl---getQueryStatisticPage(Map<String, Object> searchParams,int pageNumber, int pageSize, String direction, String orderBy)---begin");
		PageRequest pageRequest = PageUtil.buildPageRequest(pageNumber, pageSize, direction, orderBy);
		Specification<CeqResultinfoStatistics> spec = PageUtil.buildSpecification(searchParams, CeqResultinfoStatistics.class);
		Page<CeqResultinfoStatistics> page = infoDao.findAll(spec, pageRequest);
		Page<CeqQueryStatisticsBo> boPage = ClassCloneUtil.copyPage(page, CeqQueryStatisticsBo.class);
		log.info("CeqPersonQueryStatisticsServiceImpl---getQueryStatisticPage(Map<String, Object> searchParams,int pageNumber, int pageSize, String direction, String orderBy)---end");
		return boPage;
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#queryStatisticsListGroupByUser(java.util.Map, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> queryStatisticsListGroupByUser(Map<String, Object> searchParams,
			String queryStatisticType) {
		log.info("queryStatisticsListGroupByUser(searchParams{},queryStatisticType{})",searchParams,queryStatisticType);
		List<Map<String, Object>> queryForList = null;
		if(QUERYSTATISTICTYPE_ORG.equals(queryStatisticType)){
			queryForList = statisticsResultByOrg(searchParams, queryForList);
		}else if(QUERYSTATISTICTYPE_USER.equals(queryStatisticType)){
			queryForList= fingBySumUser(searchParams);
		}
		return queryForList;
	}

	
	

	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findCeqAlertById(java.lang.String)
	 */
	@Override
	public CeqAlertBo findCeqAlertById(String alertId) {
		log.info("CeqAlertStatistics findCeqAlertById(String alertId) alertId{}", alertId);
		Optional<CeqAlert> alert = alertDao.findById(alertId);
		log.info("CeqAlertStatistics findCpqAlertById(String alertId) result{}" + alert);
		CeqAlertBo alterBo = ClassCloneUtil.copyObject(alert, CeqAlertBo.class);
		return alterBo;
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#getCeqAlertPage(java.util.Map, int, int, java.lang.String, java.lang.String[])
	 */
	@Override
	public Page<CeqAlertBo> getCeqAlertPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String... orderBy) {
		log.info("getCeqAlertPage() searchParams={},pageNumber={},pageSize={},direction={}", searchParams,
		pageNumber, pageSize, direction);
		PageRequest pageRequest = PageUtil.buildPageRequest(pageNumber, pageSize, direction, orderBy);
		Specification<CeqAlert> spec = PageUtil.buildSpecification(searchParams, CeqAlert.class);
		Page<CeqAlert> page = alertDao.findAll(spec, pageRequest);
		Page<CeqAlertBo> boPage = ClassCloneUtil.copyPage(page, CeqAlertBo.class);
		return boPage;
	}

	
	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findCeqAlertsByParams(java.util.Map)
	 */
	@Override
	public List<CeqAlertBo> findCeqAlertsByParams(Map<String, Object> searchParams) {
		log.info("findCpqAlertsByParams(searchParams{})",searchParams);
		Specification<CeqAlert> spec = PageUtil.buildSpecification(searchParams, CeqAlert.class);
		List<CeqAlert> all = alertDao.findAll(spec);
		List<CeqAlertBo> boList = ClassCloneUtil.copyIterableObject(all, CeqAlertBo.class);
		return boList;
	}

	

	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findCeqAlertGroupbyAleratReason(java.lang.String, java.util.List)
	 */
	@Override
	public Integer findCeqAlertGroupbyAleratReason(String AlertReason,List<String> deptCodes) {
		log.info("findCeqAlertGroupbyAleratReason( AlertReason{}, deptCodes{})",AlertReason,deptCodes);
		return alertDao.countByAleratReason(AlertReason,deptCodes).intValue();
	}



	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findCeqAlerts(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public List<CeqAlertBo> findCeqAlerts(String queryOrgScope, String deptCode, Date alertBeginDate, Date alertEndDate) {
		log.info("findCeqAlerts(String queryOrgScope{}, String deptCode{}, Date alertBeginDate{}, Date alertEndDate{})",queryOrgScope,deptCode,alertBeginDate,alertEndDate);
		List<CeqAlert> returnList = new ArrayList<>();
		try {
			if(QUERYORGSCOPE_WITHIN.equals(queryOrgScope)){
				List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(deptCode);
				List<List<String>> split = ArrayUtil.split(deptCodes, 1000);
				for (List<String> deptList : split) {
					List<CeqAlert>  AlertList= alertDao.findByReasonAndAlertDate(alertBeginDate, alertEndDate,deptList);
					returnList.addAll(AlertList);
				}
			}else if(QUERYORGSCOPE_WITHIN.equals(queryOrgScope)){
				CeqAlert Alert= alertDao.findByReasonAndAlertDate(alertBeginDate, alertEndDate,deptCode);
				returnList.add(Alert);
			}else if(QUERYORGSCOPE_WITHIN_DIRECT.equals(queryOrgScope)){

				String orgCode = plantFormInteractiveService.findDepartmentDirectlyUnder(deptCode);
				String[] orgId = StringUtils.strip(orgCode.toString(),"[]").split(",");
				ArrayList<String> arrayList = new ArrayList<>();
				for(String org:orgId){
					arrayList.add(org);
				}
				returnList= alertDao.findByReasonAndAlertDate(alertBeginDate, alertEndDate,arrayList);
			}
			List<CeqAlertBo> copyPageObject = ClassCloneUtil.copyIterableObject(returnList, CeqAlertBo.class);
			return copyPageObject;
		} catch (Exception e) {
			log.info("findCeqAlerts(String queryOrgScope{}, String deptCode{}, Date alertBeginDate{}, Date alertEndDate{})",queryOrgScope,deptCode,alertBeginDate,alertEndDate);
			log.error("findCeqAlerts出现异常{}",e);
		} 
		return null;
	}
	
	/**
	 * <获取到查询条件，把全部的给默认值>
	 * 
	 * @param searchParams
	 */
	private void getAllAttributes(Map<String, Object> searchParams) {
		String batchFlag = (String) searchParams.get("EQ_batchFlag");// 批量标识
		if (StringUtils.isBlank(batchFlag)) {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add("1");
			arrayList.add("2");
			searchParams.put("EQ_batchFlag", arrayList);
		} else {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(batchFlag);
			searchParams.put("EQ_batchFlag", arrayList);
		}
		String source = (String) searchParams.get("EQ_source");// 信用报告来源
		if (StringUtils.isBlank(source)) {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add("1");
			arrayList.add("2");
			searchParams.put("EQ_source", arrayList);
		} else {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(source);
			searchParams.put("EQ_source", arrayList);
		}
		
		String status = (String) searchParams.get("EQ_status");// 查询结果
		if (StringUtils.isBlank(status)) {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add("1");
			arrayList.add("2");
			arrayList.add("3");
			arrayList.add("4");
			arrayList.add("5");
			searchParams.put("EQ_status", arrayList);
		} else {
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(status);
			searchParams.put("EQ_status", arrayList);
		}
		String queryType = (String) searchParams.get("EQ_queryType");// 查询类型
		if (StringUtils.isBlank(queryType)) {
			ArrayList<Object> arrayList = new ArrayList<>();
			arrayList.add("0");
			arrayList.add("2");
			searchParams.put("EQ_queryType", arrayList);
		} else {
			ArrayList<Object> arrayList = new ArrayList<>();
			arrayList.add(queryType);
			searchParams.put("EQ_queryType", arrayList);
		}
	}

	
	/**
	 * <按机构统计查询>
	 * @param searchParams
	 * @param queryForList
	 * @return
	 * @throws Exception
	 * @author yuzhao.xue
	 * @date 2019年2月21日
	 */
	private List<Map<String, Object>> statisticsResultByOrg(Map<String, Object> searchParams,
			List<Map<String, Object>> queryForList) {
		try {
			log.info("statisticsResultByOrg(Map<String, Object> searchParams)------start searchParams={}", searchParams);

			// 判断是否包含档案
			String autharchive = " ";
			if(Objects.equals("0", searchParams.get("EQ_autharchiveId"))){
				autharchive = "  AND  AUTHARCHIVE_ID IS NOT NULL";
			}else if(Objects.equals("1", searchParams.get("EQ_autharchiveId"))){
				autharchive = "  AND  AUTHARCHIVE_ID IS  NULL";
			}

			String sql = "SELECT OPER_ORG org, " + searchParams.get("FUNTION") + " queryTime,QRY_REASON queryReason,count(*) count  from ceq_resultinfo " + " where 1=1 and "
			        + searchParams.get("FUNTION") + " >= :startDate " + "and " + searchParams.get("FUNTION") + " <= :endDate  and  BATCH_FLAG in(:EQ_batchFlag) "
			        + " and SOURCE in (:EQ_source) and STATUS in (:EQ_status)  and OPER_ORG in (:operOrg)  " +autharchive+ "  GROUP BY OPER_ORG, " + searchParams.get("FUNTION")
			        + ",QRY_REASON order by 1,3,2";


			getAllAttributes(searchParams);

			queryForList = namedParameterJdbcTemplate.queryForList(sql, searchParams);

		} catch (Exception e) {
			log.error("fingBySumUser error e={}", e);
		}
		return queryForList;
	}

	/**
	 * <按用户统计，进行查询>
	 * @param searchParams
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月21日
	 */
	public List<Map<String, Object>> fingBySumUser(Map<String, Object> searchParams) {
		List<Map<String, Object>> queryForList = null;
		try {
			log.info("fingBySumUser(Map<String, Object> searchParams)------start searchParams={}", searchParams);

			String userType = "";
			userType = Objects.equals(searchParams.get("EQ_userType"), "2") ? "CREDIT_USER" : "OPERATOR";
			
			String autharchive = " ";
			if(Objects.equals("0", searchParams.get("EQ_autharchiveId"))){
				autharchive = "  AND  AUTHARCHIVE_ID IS  NOT NULL";
			}else if(Objects.equals("1", searchParams.get("EQ_autharchiveId"))){
				autharchive = "  AND  AUTHARCHIVE_ID IS  NULL";
			}

			String sql = "SELECT " + userType + " username, " + searchParams.get("FUNTION") + " queryTime,QRY_REASON queryReason,count(*) count  from ceq_resultinfo " + " where 1=1 and "
			        + searchParams.get("FUNTION") + " >= :startDate " + "and " + searchParams.get("FUNTION") + " <= :endDate  and  BATCH_FLAG in(:EQ_batchFlag) "
			        + " and SOURCE in (:EQ_source) and STATUS in (:EQ_status) and OPER_ORG in (:operOrg) " + autharchive + " GROUP BY  " + userType + " , " + searchParams.get("FUNTION")
			        + ",QRY_REASON order by 1,3,2";

			getAllAttributes(searchParams);

			queryForList = namedParameterJdbcTemplate.queryForList(sql, searchParams);

		} catch (Exception e) {
			log.error("fingBySumUser error e={}", e);
		}
		return queryForList;
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#updateAlert(cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqAlertBo)
	 */
	@Override
	public void updateAlert(CeqAlertBo alter) {
		log.info("CeqAlertStatistics update(CeqAlertStatistics alter)--alter{}", alter);
		CeqAlert copyObject = ClassCloneUtil.copyObject(alter, CeqAlert.class);
		CeqAlert save = alertDao.save(copyObject);
		String userName = alter.getUserName();
		if(InConstant.ALERT_QUANTITY.equals(alter.getAleratReason())){
			redis.getAtomicLong(userName + EntCacheConstant.ALERT_QUANTITY_KEY_M).delete();
		}
		if(InConstant.ALERT_TIME.equals(alter.getAleratReason())){
			redis.getAtomicLong(userName + EntCacheConstant.ALERT_TIME_KEY_M).delete();
		}
		log.info("CeqAlert update(CpqAlertStatistics alter)--result{}", save);
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#unlockUser(cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqAlertBo)
	 */
	@Override
	public void unlockUser(CeqAlertBo alter) {
		log.info("unlockUser alter = {}",alter);
		String userName  = alter.getUserName();
		String status = alter.getUserStatus();
		alertDao.changeUserStatus(userName, status);
		log.info("unlockUser alter = {}",alter);
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#clearAlertCount(java.lang.String)
	 */
	@Override
	public void clearAlertCount(String userName) {
		log.info("clearAlertCount start,userName={}", userName);
		RMap<String, Integer> quantityCache = redis.getMap(EntCacheConstant.ALERT_QUANTITY_KEY_M);
		RMap<String, Integer> timeCache = redis.getMap(EntCacheConstant.ALERT_TIME_KEY_M);
		quantityCache.put(userName, 0);
		timeCache.put(userName, 0);
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#lockedUser(cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqAlertBo)
	 */
	@Override
	public void lockedUser(CeqAlertBo alter) {
		log.info("lockedUser alter = {}",alter);
		String userName  = alter.getUserName();
		String status = alter.getUserStatus();
		alertDao.changeUserStatus(userName, status);
		log.info("lockedUser result = {}",alter);
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findByIds(java.util.List)
	 */
	@Override
	public List<CeqAlertBo> findByIds(List<String> ids) {
		List<CeqAlert> alters = alertDao.findByids(ids);
		List<CeqAlertBo> copyIterableObject = ClassCloneUtil.copyIterableObject(alters, CeqAlertBo.class);
		return copyIterableObject;
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findByAllStat(java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> findByAllStat(List<String> deptCode, String alertDate, String bussDate,
			String alertDateFuntion, String bussDateFuntion) {
		EntityManager entityManager = getEntityManager();
		EntityTransaction transcation = entityManager.getTransaction();
		transcation.begin();
		try {
			String mysql_sql="select org_code,"
			        + "sum(CASE a.ALERAT_REASON  WHEN  'C0000' THEN 1 ELSE 0  END)   as status_0,"
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0001' THEN 1 ELSE 0  END)   as status_1,"
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0002' THEN 1 ELSE 0  END)   as status_2,"
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0003' THEN 1 ELSE 0  END)   as status_3,"
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0004' THEN 1 ELSE 0  END)   as status_4, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0005' THEN 1 ELSE 0  END)   as status_5, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0006' THEN 1 ELSE 0  END)   as status_6, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0007' THEN 1 ELSE 0  END)   as status_7, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0008' THEN 1 ELSE 0  END)   as status_8, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0009' THEN 1 ELSE 0  END)   as status_9, "
					+ "sum(CASE a.ALERAT_REASON  WHEN  'C0010' THEN 1 ELSE 0  END)   as status_10 "
					+ "from CEQ_ALERT a "
					+ "where 1 = 1 and  ORG_CODE in(:DEPTCODE) "
					+ "AND  :ALERTDATE  <=  " +alertDateFuntion+ " AND  :BUSSDATE  >=  " +bussDateFuntion+ " GROUP BY org_code";
			
			Query query = entityManager.createNativeQuery(mysql_sql);
			query.setParameter("DEPTCODE", deptCode);
			query.setParameter("ALERTDATE", alertDate);
			query.setParameter("BUSSDATE", bussDate);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		} finally {
			transcation.commit();
			entityManager.close();
		}
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findByTotal(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> findByTotal(String deptCode, String alertDate, String bussDate, String alertDateFuntion,
			String bussDateFuntion) {
		EntityManager entityManager = getEntityManager();
		EntityTransaction transcation = entityManager.getTransaction();
		transcation.begin();
		try {
			String mysql_sql="select org_code,"
			        + "sum(CASE a.ALERAT_REASON  WHEN  'C0000' THEN 1 ELSE 0  END)   as status_0,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0001' THEN 1 ELSE 0  END)   as status_1,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0002' THEN 1 ELSE 0  END)   as status_2,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0003' THEN 1 ELSE 0  END)   as status_3,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0004' THEN 1 ELSE 0  END)   as status_4, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0005' THEN 1 ELSE 0  END)   as status_5, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0006' THEN 1 ELSE 0  END)   as status_6, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0007' THEN 1 ELSE 0  END)   as status_7, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0008' THEN 1 ELSE 0  END)   as status_8, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0009' THEN 1 ELSE 0  END)   as status_9, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0010' THEN 1 ELSE 0  END)   as status_10 "
					+ "from CEQ_ALERT a "
					+ "where 1 = 1 and  ORG_CODE in(select slave_Id from Sys_Org_Relation where parent_Id = :DEPTCODE) "
					+ "AND  :ALERTDATE  <=  " +alertDateFuntion+ " AND  :BUSSDATE  >=  " +bussDateFuntion+ " GROUP BY org_code";
					
			
			Query query = entityManager.createNativeQuery(mysql_sql);
			query.setParameter("DEPTCODE", deptCode);
			query.setParameter("ALERTDATE", alertDate);
			query.setParameter("BUSSDATE", bussDate);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		} finally {
			transcation.commit();
			entityManager.close();
		}
	}


	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.querystatistics.service.CeqPersonQueryStatisticsService#findByTotal4Agency(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> findByTotal4Agency(String deptCode, String alertDate, String bussDate,
			String alertDateFuntion, String bussDateFuntion) {
		EntityManager entityManager = getEntityManager();
		EntityTransaction transcation = entityManager.getTransaction();
		transcation.begin();
		try {
			String mysql_sql="select org_code,"
			        + "sum(CASE a.ALERAT_REASON  WHEN  'C0000' THEN 1 ELSE 0  END)   as status_0,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0001' THEN 1 ELSE 0  END)   as status_1,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0002' THEN 1 ELSE 0  END)   as status_2,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0003' THEN 1 ELSE 0  END)   as status_3,"
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0004' THEN 1 ELSE 0  END)   as status_4, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0005' THEN 1 ELSE 0  END)   as status_5, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0006' THEN 1 ELSE 0  END)   as status_6, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0007' THEN 1 ELSE 0  END)   as status_7, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0008' THEN 1 ELSE 0  END)   as status_8, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0009' THEN 1 ELSE 0  END)   as status_9, "
                    + "sum(CASE a.ALERAT_REASON  WHEN  'C0010' THEN 1 ELSE 0  END)   as status_10 "
					+ "from CEQ_ALERT a "
					+ "where 1 = 1 and  ORG_CODE = :DEPTCODE "
					+ "AND  :ALERTDATE  <=  " +alertDateFuntion+ " AND  :BUSSDATE  >=  " +bussDateFuntion+ " GROUP BY org_code";
			
			Query query = entityManager.createNativeQuery(mysql_sql);
			query.setParameter("DEPTCODE", deptCode);
			query.setParameter("ALERTDATE", alertDate);
			query.setParameter("BUSSDATE", bussDate);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		} finally {
			transcation.commit();
			entityManager.close();
		}
	}
}
