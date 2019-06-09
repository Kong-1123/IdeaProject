package cn.com.dhcc.creditquery.ent.reportview.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.com.dhcc.creditquery.ent.query.bo.reportview.CeqReportLogBo;
import cn.com.dhcc.creditquery.ent.reportview.dao.CeqReportLogViewDao;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqReportLogView;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqResultinfoView;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportLogService;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqResultinfoService;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;


/**
 * 
 * @author wenjie·chu
 *
 * 2018年3月21日-下午1:32:17
 */


@Service
public class CeqReportLogServiceImpl implements CeqReportLogService {
	
	private static Logger log = LoggerFactory.getLogger(CeqReportLogServiceImpl.class);
	@Autowired
	CeqResultinfoService resultinfoService;
	@Autowired
	CeqReportLogViewDao reportLogDao;
	
	@Override
	public Page<CeqReportLogBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy) {
		Page<CeqReportLogView> page = PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, reportLogDao, CeqReportLogView.class);
		Page<CeqReportLogBo> pageResult = null;
		if(page != null) {
			pageResult = ClassCloneUtil.copyPage(page, CeqReportLogBo.class);
		}
		return pageResult;
	}


	@Override
	public CeqReportLogBo save(CeqReportLogView reportLog) {
		CeqReportLogView log = reportLogDao.save(reportLog);
		return ClassCloneUtil.copyObject(log, CeqReportLogBo.class);
	}


	@Override
	public CeqReportLogBo findById(String id) {
		log.info("CeqReportLogBo findById(String id) id{}", id);
		CeqReportLogView printflow = reportLogDao.findOne(id);
		log.info("CeqReportLogBo findById(String id) result{}" + printflow);
		return ClassCloneUtil.copyObject(printflow, CeqReportLogBo.class);
	}


	@Override
	public void save(String recordId, String userName, String orgCode,String logType) {
		CeqResultinfoView info = resultinfoService.findById(recordId);
		CeqReportLogView reportLog = new CeqReportLogView();
		dealReportLog(info,reportLog);
		reportLog.setOperateUser(userName);
		reportLog.setOperateDept(orgCode);
		reportLog.setRecordId(recordId);
		reportLog.setOperateType(logType);
		reportLogDao.save(reportLog);
	}
	
	private void dealReportLog(CeqResultinfoView info, CeqReportLogView reportLog) {
		reportLog.setSignCode(info.getSignCode());
		reportLog.setEnterpriseName(info.getEnterpriseName());
		reportLog.setOperateDate(new Date());
		reportLog.setQueryFormat(info.getQueryFormat());
		reportLog.setQueryUser(info.getOperator());
		reportLog.setQueryUserDept(info.getOperOrg());
		reportLog.setRecheckDept(info.getRekOrg());
		reportLog.setRecheckType(info.getCheckWay());
		reportLog.setRecheckUser(info.getRekUser());
	}
	
	
	@Override
	public long findPrintCount(String recordId) {
		long count = reportLogDao.findPrintCount(recordId);
		return count;
	}

	@Override
	public List<CeqReportLogBo> findAll(Map<String, Object> searchParams) {
		Specification<CeqReportLogView> spec = PageUtil.buildSpecification(searchParams, CeqReportLogView.class);
		List<CeqReportLogView> infoList = reportLogDao.findAll(spec);
		List<CeqReportLogBo> cpqReportLogBos = ClassCloneUtil.copyIterableObject(infoList, CeqReportLogBo.class);
		return cpqReportLogBos;
	}

	@Override
	public List<CeqReportLogBo> findCeqReportLogBosByIds(List<String> ceqReportLogIds) {
		log.info("CeqReportLogServiceImpl.findCeqReportLogBosByIds start,ceqReportLogIds ={}",ceqReportLogIds);
		List<CeqReportLogView> cpqReportLogViewList = new ArrayList();
		List<CeqReportLogBo> ceqReportLogBoList = new ArrayList();
		List<List<String>> splitIdList = Lists.partition(ceqReportLogIds, 900);
		try {
			for(int i=0;i<splitIdList.size();i++) {
				List<CeqReportLogView> splitCpqApproveList = reportLogDao.findCeqReportLogViewsByIds(ceqReportLogIds);
				cpqReportLogViewList.addAll(splitCpqApproveList);
			}
			ceqReportLogBoList = ClassCloneUtil.copyIterableObject(cpqReportLogViewList, CeqReportLogBo.class);
		}catch(Exception e) {
			log.error("CeqReportLogServiceImpl.findCeqReportLogBosByIds error={},ceqReportLogBoList={} ",e,cpqReportLogViewList);
		}
		log.info("CeqReportLogServiceImpl.findCeqReportLogBosByIds end,ceqReportLogBoList ={}",ceqReportLogBoList);
		return ceqReportLogBoList;
	}

}
