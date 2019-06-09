package cn.com.dhcc.creditquery.person.reportview.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.com.dhcc.creditquery.person.query.bo.reportview.CpqReportLogBo;
import cn.com.dhcc.creditquery.person.reportview.dao.CpqReportLogViewDao;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqReportLogView;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqResultinfoView;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportLogService;
import cn.com.dhcc.creditquery.person.reportview.service.CpqResultinfoService;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;


/**
 * 
 * @author wenjie·chu
 *
 * 2018年3月21日-下午1:32:17
 */


@Service
public class CpqReportLogServiceImpl implements CpqReportLogService {
	
	private static Logger log = LoggerFactory.getLogger(CpqReportLogServiceImpl.class);
	@Autowired
	CpqResultinfoService resultinfoService;
	@Autowired
	CpqReportLogViewDao reportLogDao;
	
	@Override
	public Page<CpqReportLogBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy) {
		Page<CpqReportLogView> page = PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, reportLogDao, CpqReportLogView.class);
		Page<CpqReportLogBo> pageResult = null;
		if(page != null) {
			pageResult = ClassCloneUtil.copyPage(page, CpqReportLogBo.class);
		}
		return pageResult;
	}


	@Override
	public CpqReportLogBo save(CpqReportLogView reportLog) {
		CpqReportLogView log = reportLogDao.save(reportLog);
		return ClassCloneUtil.copyObject(log, CpqReportLogBo.class);
	}


	@Override
	public CpqReportLogBo findById(String id) {
		log.info("CpqReportLogBo findById(String id) id{}", id);
		CpqReportLogView printflow = reportLogDao.findOne(id);
		log.info("CpqReportLogBo findById(String id) result{}" + printflow);
		return ClassCloneUtil.copyObject(printflow, CpqReportLogBo.class);
	}


	@Override
	public void save(String recordId, String userName, String orgCode,String logType) {
		CpqResultinfoView info = resultinfoService.findById(recordId);
		CpqReportLogView reportLog = new CpqReportLogView();
		dealReportLog(info,reportLog);
		reportLog.setOperateUser(userName);
		reportLog.setOperateDept(orgCode);
		reportLog.setRecordId(recordId);
		reportLog.setOperateType(logType);
		reportLogDao.save(reportLog);
	}
	
	private void dealReportLog(CpqResultinfoView info, CpqReportLogView reportLog) {
		reportLog.setCertNo(info.getCertNo());
		reportLog.setCertType(info.getCertType());
		reportLog.setClientName(info.getCustomerName());
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
	public List<CpqReportLogBo> findAll(Map<String, Object> searchParams) {
		Specification<CpqReportLogView> spec = PageUtil.buildSpecification(searchParams, CpqReportLogView.class);
		List<CpqReportLogView> infoList = reportLogDao.findAll(spec);
		List<CpqReportLogBo> cpqReportLogBos = ClassCloneUtil.copyIterableObject(infoList, CpqReportLogBo.class);
		return cpqReportLogBos;
	}

	@Override
	public List<CpqReportLogBo> findCpqReportLogBosByIds(List<String> cpqReportLogIds) {
		log.info("CpqReportLogServiceImpl.findCpqReportLogBosByIds start,cpqReportLogIds ={}",cpqReportLogIds);
		List<CpqReportLogView> cpqReportLogViewList = new ArrayList();
		List<CpqReportLogBo> cpqReportLogBoList = new ArrayList();
		List<List<String>> splitIdList = Lists.partition(cpqReportLogIds, 900);
		try {
			for(int i=0;i<splitIdList.size();i++) {
				List<CpqReportLogView> splitCpqApproveList = reportLogDao.findCpqReportLogViewsByIds(cpqReportLogIds);
				cpqReportLogViewList.addAll(splitCpqApproveList);
			}
			cpqReportLogBoList = ClassCloneUtil.copyIterableObject(cpqReportLogViewList, CpqReportLogBo.class);
		}catch(Exception e) {
			log.error("CpqReportLogServiceImpl.findCpqReportLogBosByIds error={},cpqReportLogBoList={} ",e,cpqReportLogViewList);
		}
		log.info("CpqReportLogServiceImpl.findCpqReportLogBosByIds end,cpqReportLogBoList ={}",cpqReportLogBoList);
		return cpqReportLogBoList;
	}

}
