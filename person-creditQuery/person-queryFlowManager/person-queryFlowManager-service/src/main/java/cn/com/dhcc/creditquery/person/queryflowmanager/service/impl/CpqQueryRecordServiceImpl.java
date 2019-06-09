/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.dhcc.query.creditquerycommon.util.*;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.credit.platform.util.Collections3;
import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.queryconfig.dao.CpqConfigDao;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.dao.CpqResultinfoDao;
import cn.com.dhcc.creditquery.person.queryflowmanager.entity.CpqResultinfoFlowManager;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqQueryRecordService;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewReadService;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: liulekang
 * @Date: 2019/2/27
 */
@Slf4j
@Service
@Transactional(value = "transactionManager")
public class CpqQueryRecordServiceImpl implements CpqQueryRecordService {

    private RedissonClient redis = RedissonUtil.getLocalRedisson();



    @Autowired
    private CpqResultinfoDao cpqResultinfoDao;

    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    CpqReportViewReadService reportViewReadService;

    @Override
    public CpqQueryRecordBo create(CpqQueryRecordBo cpqQueryRecordBo) {
        CpqResultinfoFlowManager cpqResultinfoFlowManager = ClassCloneUtil.copyObject(cpqQueryRecordBo, CpqResultinfoFlowManager.class);
        CpqResultinfoFlowManager save = cpqResultinfoDao.save(cpqResultinfoFlowManager);
        ClassCloneUtil.copyObject(save, cpqQueryRecordBo);
        return cpqQueryRecordBo;
    }

    @Override
    public CpqQueryRecordBo update(CpqQueryRecordBo cpqQueryRecordBo) {
        CpqResultinfoFlowManager cpqResultinfoFlowManager = ClassCloneUtil.copyObject(cpqQueryRecordBo, CpqResultinfoFlowManager.class);
        CpqResultinfoFlowManager save = cpqResultinfoDao.save(cpqResultinfoFlowManager);
        ClassCloneUtil.copyObject(save, cpqQueryRecordBo);
        return cpqQueryRecordBo;
    }

    @Override
    public CpqQueryRecordBo findById(String id) {
        CpqResultinfoFlowManager cpqResultinfoFlowManager = cpqResultinfoDao.findOne(id);
        CpqQueryRecordBo cpqQueryRecordBo = ClassCloneUtil.copyObject(cpqResultinfoFlowManager, CpqQueryRecordBo.class);
        return cpqQueryRecordBo;
    }

    @Override
    public Page<CpqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction, String orderBy) {
        return PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, cpqResultinfoDao,
                CpqResultinfoFlowManager.class, CpqQueryRecordBo.class);
    }


 @Override
    public CpqQueryReportFlowBo getLocalReport(String customerName, String certType, String certNo, String queryReason, String topOrgCode,String localDay) {
        try {
            List<CpqResultinfoFlowManager> list = getCpqResultinfos(customerName, certType, certNo, queryReason, topOrgCode,localDay);
            if (Collections3.isEmpty(list)) {
                return null;
            } else {
                CpqResultinfoFlowManager cpqResultinfoFlowManager = list.get(0);
                CpqQueryReportFlowBo cpqQueryReportFlowBo = new CpqQueryReportFlowBo();
                String htmlReportFilePath = cpqResultinfoFlowManager.getHtmlPath();
                if(StringUtils.isNotBlank(htmlReportFilePath)) {
                	String readReport = readReport(htmlReportFilePath,topOrgCode);
                	cpqQueryReportFlowBo.setHtmlReportPath(htmlReportFilePath);
                	cpqQueryReportFlowBo.setHtmlCreditReport(readReport);
                }
                String xmlPath = cpqResultinfoFlowManager.getXmlPath();
                if(StringUtils.isNotBlank(xmlPath)) {
                	String readReport = readReport(xmlPath,topOrgCode);
                	cpqQueryReportFlowBo.setXmlReportPath(xmlPath);
                	cpqQueryReportFlowBo.setXmlCreditReport(readReport);
                }
                String jsonPath = cpqResultinfoFlowManager.getJsonPath();
                if(StringUtils.isNotBlank(jsonPath)) {
                	String readReport = readReport(jsonPath,topOrgCode);
                	cpqQueryReportFlowBo.setJsonReportPath(jsonPath);
                	cpqQueryReportFlowBo.setJsonCreditReport(readReport);
                }
                String pdfPath = cpqResultinfoFlowManager.getPdfPath();
                if(StringUtils.isNotBlank(pdfPath)) {
                	String readReport = readReport(pdfPath,topOrgCode);
                	cpqQueryReportFlowBo.setPdfReportPath(pdfPath);
                	cpqQueryReportFlowBo.setPdfCreditReport(readReport);
                }
                return cpqQueryReportFlowBo;
            }
        } catch (Exception e) {
            log.error("getLocalReport ", e);
        }
        return null;
    }

	private String readReport(String htmlReportFilePath,String topOrgCode) {
		FileReadRequest fileReadRequest = new FileReadRequest();
		fileReadRequest.setUri(htmlReportFilePath);
		String systemStorageType = ConfigUtil.getSystemStorageType(topOrgCode);
		if(StringUtils.isNotBlank(systemStorageType)) {
			fileReadRequest.setStorageType(systemStorageType);
		}
		fileReadRequest.setDecrypt(true);
		fileReadRequest.setZipFlag(true);
		FileReadResponse fileReadResponse = fileStorageService.readFileContent(fileReadRequest);
		String report = fileReadResponse.getContent();
		return report;
	}


    /**
     * 获取相应的查询记录
     *
     * @param customerName
     * @param certType
     * @param certNo
     * @param queryReason
     * @param topOrgCode
     * @return
     * @throws Exception
     */
    private List<CpqResultinfoFlowManager> getCpqResultinfos(String customerName, String certType, String certNo, String queryReason, String topOrgCode,String localDay) throws Exception {
        String queryReasonLable = ConfigUtil.getLocalQueryIncludeReason(topOrgCode);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - Integer.parseInt(localDay));
        Date validateDate = cal.getTime();
        //添加机构代码
        List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(topOrgCode);
        String deptCode = StringUtils.join(deptCodes, ",");
        JPAParamGroup param = new JPAParamGroup();
        JPAParamGroup orgId = new JPAParamGroup("IN_operOrg", deptCode);
        JPAParamGroup clientNameGroup = new JPAParamGroup("EQ_customerName", customerName);
        JPAParamGroup certNoGroup = new JPAParamGroup("EQ_certNo", certNo);
        JPAParamGroup certTypeGroup = new JPAParamGroup("EQ_certType", certType);
        JPAParamGroup source = new JPAParamGroup("EQ_source", REMOTE_SOURCE);
        JPAParamGroup status = new JPAParamGroup("EQ_status", InConstant.QUERY_SUCCESS);
        JPAParamGroup times = new JPAParamGroup("GT_queryTime", validateDate);
        param = new JPAParamGroup(orgId, clientNameGroup, certNoGroup, certTypeGroup, source, status, times);
        if (ENABLE.equals(queryReasonLable)) {
            JPAParamGroup qryreason = new JPAParamGroup("EQ_qryReason", queryReason);
            param = new JPAParamGroup(orgId, clientNameGroup, certNoGroup, certTypeGroup, source, status, times, qryreason);
        }
        Specification<CpqResultinfoFlowManager> spec = PageUtil.buildSpecification(param, CpqResultinfoFlowManager.class);
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        return cpqResultinfoDao.findAll(spec, sort);
    }

    @Override
    public boolean isHaveLocalReport(String customerName, String certType, String certNo, String queryReason, String topOrgCode,String localDay) {
        boolean flag = false;
        try {
            log.info("isHaveLocalReport param customerName = {} , certType = {} , certNo = {} , queryReason = {} , topOrgCode = {}", customerName, certType, certNo, queryReason, topOrgCode);
            List<CpqResultinfoFlowManager> list = getCpqResultinfos(customerName, certType, certNo, queryReason, topOrgCode, localDay);
            log.debug("isHaveLocalReport getCpqResultinfos result = ", list);
            if (Collections3.isNotEmpty(list)) {
                flag = true;
            }
        } catch (Exception e) {
            log.info("isHaveLocalReport error e = ", e);
        }
        return flag;
    }

    @Override
    public String getReportById(String id,String reportType) {
        try {
            log.info("getReportById param id = ", id);
            CpqResultinfoFlowManager cpqResultinfoFlowManager = cpqResultinfoDao.findOne(id);
            log.debug("getReportById findOne cpqResultinfo = ", cpqResultinfoFlowManager);
            if (null != cpqResultinfoFlowManager) {
            	String reportFilePath = "";
            	if(StringUtils.equals(REPORT_FORMAT_HTML, reportType)) {
            		reportFilePath = cpqResultinfoFlowManager.getHtmlPath();
            	}else if(StringUtils.equals(REPORT_FORMAT_XML, reportType)) {
            		reportFilePath = cpqResultinfoFlowManager.getXmlPath();
            	}else if(StringUtils.equals(REPORT_FORMAT_JSON, reportType)) {
            		reportFilePath = cpqResultinfoFlowManager.getJsonPath();
            	}else{
            		reportFilePath = cpqResultinfoFlowManager.getPdfPath();
            	}
                FileReadRequest fileReadRequest = new FileReadRequest();
                fileReadRequest.setUri(reportFilePath);
                String systemStorageType = ConfigUtil.getSystemStorageType();
        		if(StringUtils.isNotBlank(systemStorageType)) {
        			fileReadRequest.setStorageType(systemStorageType);
        		}
                fileReadRequest.setZipFlag(true);
                fileReadRequest.setDecrypt(true);
                FileReadResponse fileReadResponse = fileStorageService.readFileContent(fileReadRequest);
                String report = fileReadResponse.getContent();
                return report;
            }
        } catch (Exception e) {
            log.info("getReportById error e = ", e);
        }
        return null;
    }

    @Override
    public CpqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds) {
        List<CpqResultinfoFlowManager> resultList=null;
        CpqShortcutBo cutVo = new CpqShortcutBo();
        List<CpqShortcutBo.Menu> menusList = new ArrayList<>();
        String qryReason="01";//贷后管理
        if(menuIds.contains(ACHIVEMENUID)){
            resultList = cpqResultinfoDao.findByArchiveCount(qryReason,deptCodes);
            CpqShortcutBo.Menu me = new CpqShortcutBo.Menu();
            me.setName(NAME);
            String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
            String url = MENULINK;
            if(Boolean.parseBoolean(property)){
                url = MENULINK_MICRO;
            }
            me.setLink(url);
            me.setCount(resultList.size());
            menusList.add(me);
        }else{
            resultList = Collections.emptyList();
        }
        cutVo.setMenus(menusList);
        cutVo.setTitle(TITLE);
        return cutVo;
    }

    @Override
    public void updateArchiveIdById(String id, String archiveId) {
        log.info("updateArchiveIdById param id = {} , archiveId = {}",id ,archiveId);
        cpqResultinfoDao.updateArchiveIdById(id, archiveId);
    }

    @Override
    public boolean isQueriedCreditUser(String creditUser) {
        log.info("isQueriedCreditUser param creditUser = ",creditUser);
        List<CpqResultinfoFlowManager> cpqResultInfos = cpqResultinfoDao.findByCcuser(creditUser);
        if(Collections3.isEmpty(cpqResultInfos)){
            return true;
        }
        return false;
    }

    @Override
    public long getUserQueryNum(String username) {
        log.info("getUserQueryNum  param username = ", username);
        RMap<String, Long> cache = redis.getMap(PersonCacheConstant.USER_QUERY_NUN);
        if (null == cache || cache.size() == 0) {
            long expireTime = TimeTransferUtil.getExpireTimeAtMiliSecond(InConstant.HOUR_CLEAR_IN_REDISH,
                    InConstant.MIN_CLEAR_IN_REDISH, InConstant.SECOND_CLEAR_IN_REDISH);
            cache.put(username, 0L);
            cache.expireAt(expireTime);
        }
        Long currentUserQueryNum = cache.get(username);
        currentUserQueryNum = (null == currentUserQueryNum ? 0 : currentUserQueryNum);
        log.info("getCurrentUserQueryNum currentUserQueryNum = {}", currentUserQueryNum);
        return currentUserQueryNum;
    }

    @Override
    public List<CpqQueryRecordBo> findAll(Map<String, Object> searchParams) {
        Specification<CpqResultinfoFlowManager> spec = PageUtil.buildSpecification(searchParams, CpqResultinfoFlowManager.class);
        List<CpqResultinfoFlowManager> infoList = cpqResultinfoDao.findAll(spec);
        List<CpqQueryRecordBo> cpqQueryRecordBos = ClassCloneUtil.copyIterableObject(infoList, CpqQueryRecordBo.class);
        return cpqQueryRecordBos;
    }

    @Override
    public List<CpqQueryRecordBo> findByIds(List<String> ids) {
        List<CpqResultinfoFlowManager> infoList = cpqResultinfoDao.findByIdIn(ids);
        List<CpqQueryRecordBo> cpqQueryRecordBos = ClassCloneUtil.copyIterableObject(infoList, CpqQueryRecordBo.class);
        return cpqQueryRecordBos;
    }

	@Override
	public String readReportById(String recordId, String userName, String orgCode,String reportType) {
		log.info("read report begin,recordId={}", recordId);
		String content = null;
		try {
			content = reportViewReadService.readReport(recordId, userName, orgCode,reportType);
			return content;
		} catch (Exception e) {
			log.error("read report error", e);
		}
		log.info("read report end");
		return null;
	}


}
