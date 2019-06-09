/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.service.impl;

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
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.queryconfig.dao.CeqConfigDao;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.dao.CeqResultinfoDao;
import cn.com.dhcc.creditquery.ent.queryflowmanager.entity.CeqResultinfoFlowManager;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqQueryRecordService;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportViewReadService;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: liulekang
 * @Date: 2019/2/27
 */
@Slf4j 
@Service
@Transactional(value = "transactionManager")
public class CeqQueryRecordServiceImpl implements CeqQueryRecordService {

    private RedissonClient redis = RedissonUtil.getLocalRedisson();

    @Autowired
    private CeqResultinfoDao ceqResultinfoDao;

    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private CeqReportViewReadService reportViewReadService;
    
    @Autowired
    private CeqConfigDao ceqConfigDao;

    @Override
    public CeqQueryRecordBo create(CeqQueryRecordBo ceqQueryRecordBo) {
        CeqResultinfoFlowManager ceqResultinfoFlowManager = ClassCloneUtil.copyObject(ceqQueryRecordBo, CeqResultinfoFlowManager.class);
        CeqResultinfoFlowManager save = ceqResultinfoDao.save(ceqResultinfoFlowManager);
        ClassCloneUtil.copyObject(save, ceqQueryRecordBo);
        return ceqQueryRecordBo;
    }

    @Override
    public CeqQueryRecordBo update(CeqQueryRecordBo ceqQueryRecordBo) {
        CeqResultinfoFlowManager ceqResultinfoFlowManager = ClassCloneUtil.copyObject(ceqQueryRecordBo, CeqResultinfoFlowManager.class);
        CeqResultinfoFlowManager save = ceqResultinfoDao.save(ceqResultinfoFlowManager);
        ClassCloneUtil.copyObject(save, ceqQueryRecordBo);
        return ceqQueryRecordBo;
    }

    @Override
    public CeqQueryRecordBo findById(String id) {
        CeqResultinfoFlowManager ceqResultinfoFlowManager = ceqResultinfoDao.findOne(id);
        CeqQueryRecordBo ceqQueryRecordBo = ClassCloneUtil.copyObject(ceqResultinfoFlowManager, CeqQueryRecordBo.class);
        return ceqQueryRecordBo;
    }

    @Override
    public Page<CeqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction, String orderBy) {
        return PageUtil.getPageByJPA(searchParams, pageNumber, pageSize, direction, orderBy, ceqResultinfoDao,
                CeqResultinfoFlowManager.class, CeqQueryRecordBo.class);
    }


 @Override
    public CeqQueryReportFlowBo getLocalReport(String signCode,String queryReason, String topOrgCode,String localDay) {
        try {
            List<CeqResultinfoFlowManager> list = getCeqResultinfos(signCode, queryReason, topOrgCode,localDay);
            if (Collections3.isEmpty(list)) {
                return null;
            } else {
                CeqResultinfoFlowManager ceqResultinfoFlowManager = list.get(0);
                CeqQueryReportFlowBo ceqQueryReportFlowBo = new CeqQueryReportFlowBo();
                String htmlReportFilePath = ceqResultinfoFlowManager.getHtmlPath();
                if(StringUtils.isNotBlank(htmlReportFilePath)) {
                	String readReport = readReport(htmlReportFilePath,topOrgCode);
                	ceqQueryReportFlowBo.setHtmlReportPath(htmlReportFilePath);
                	ceqQueryReportFlowBo.setHtmlCreditReport(readReport);
                }
                String xmlPath = ceqResultinfoFlowManager.getXmlPath();
                if(StringUtils.isNotBlank(xmlPath)) {
                	String readReport = readReport(xmlPath,topOrgCode);
                	ceqQueryReportFlowBo.setXmlReportPath(xmlPath);
                	ceqQueryReportFlowBo.setXmlCreditReport(readReport);
                }
                String jsonPath = ceqResultinfoFlowManager.getJsonPath();
                if(StringUtils.isNotBlank(jsonPath)) {
                	String readReport = readReport(jsonPath,topOrgCode);
                	ceqQueryReportFlowBo.setJsonReportPath(jsonPath);
                	ceqQueryReportFlowBo.setJsonCreditReport(readReport);
                }
                String pdfPath = ceqResultinfoFlowManager.getPdfPath();
                if(StringUtils.isNotBlank(pdfPath)) {
                	String readReport = readReport(pdfPath,topOrgCode);
                	ceqQueryReportFlowBo.setPdfReportPath(pdfPath);
                	ceqQueryReportFlowBo.setPdfCreditReport(readReport);
                }
                return ceqQueryReportFlowBo;
            }
        } catch (Exception e) {
            log.error("getLocalReport ", e);
        }
        return null;
    }

	private String readReport(String htmlReportFilePath,String topOrgCode) {
		FileReadRequest fileReadRequest = new FileReadRequest();
		fileReadRequest.setUri(htmlReportFilePath);
		String systemStorageType = CeqConfigUtil.getSystemStorageType(topOrgCode);
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
     * @param signCode
     * @param queryReason
     * @param topOrgCode
     * @return
     * @throws Exception
     */
    private List<CeqResultinfoFlowManager> getCeqResultinfos(String signCode, String queryReason, String topOrgCode,String localDay) throws Exception {
        String queryReasonLable = CeqConfigUtil.getLocalQueryIncludeReason(topOrgCode);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - Integer.parseInt(localDay));
        Date validateDate = cal.getTime();
        //添加机构代码
        List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(topOrgCode);
        String deptCode = StringUtils.join(deptCodes, ",");
        JPAParamGroup param = new JPAParamGroup();
        JPAParamGroup orgId = new JPAParamGroup("IN_operOrg", deptCode);
        JPAParamGroup signCodeGroup = new JPAParamGroup("EQ_signCode", signCode);
        JPAParamGroup source = new JPAParamGroup("EQ_source", REMOTE_SOURCE);
        JPAParamGroup status = new JPAParamGroup("EQ_status", InConstant.QUERY_SUCCESS);
        JPAParamGroup times = new JPAParamGroup("GT_queryTime", validateDate);
        param = new JPAParamGroup(orgId, signCodeGroup, source, status, times);
        if (ENABLE.equals(queryReasonLable)) {
            JPAParamGroup qryreason = new JPAParamGroup("EQ_qryReason", queryReason);
            param = new JPAParamGroup(orgId, signCodeGroup, source, status, times, qryreason);
        }
        Specification<CeqResultinfoFlowManager> spec = PageUtil.buildSpecification(param, CeqResultinfoFlowManager.class);
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        return ceqResultinfoDao.findAll(spec, sort);
    }

    @Override
    public boolean isHaveLocalReport(String signCode, String queryReason, String topOrgCode,String localDay) {
        boolean flag = false;
        try {
            log.info("isHaveLocalReport param signCode = {} , queryReason = {} , topOrgCode = {}", signCode, queryReason, topOrgCode);
            List<CeqResultinfoFlowManager> list = getCeqResultinfos(signCode, queryReason, topOrgCode, localDay);
            log.debug("isHaveLocalReport getCeqResultinfos result = ", list);
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
            CeqResultinfoFlowManager ceqResultinfoFlowManager = ceqResultinfoDao.findOne(id);
            log.debug("getReportById findOne ceqResultinfo = ", ceqResultinfoFlowManager);
            if (null != ceqResultinfoFlowManager) {
            	String reportFilePath = "";
            	if(StringUtils.equals(REPORT_FORMAT_HTML, reportType)) {
            		reportFilePath = ceqResultinfoFlowManager.getHtmlPath();
            	}else if(StringUtils.equals(REPORT_FORMAT_XML, reportType)) {
            		reportFilePath = ceqResultinfoFlowManager.getXmlPath();
            	}else if(StringUtils.equals(REPORT_FORMAT_JSON, reportType)) {
            		reportFilePath = ceqResultinfoFlowManager.getJsonPath();
            	}else{
            		reportFilePath = ceqResultinfoFlowManager.getPdfPath();
            	}
                FileReadRequest fileReadRequest = new FileReadRequest();
                fileReadRequest.setUri(reportFilePath);
                String systemStorageType = CeqConfigUtil.getSystemStorageType();
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
    public CeqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds) {
        List<CeqResultinfoFlowManager> resultList=null;
        CeqShortcutBo cutVo = new CeqShortcutBo();
        List<CeqShortcutBo.Menu> menusList = new ArrayList<>();
        String qryReason="01";//贷后管理
        if(menuIds.contains(ACHIVEMENUID)){
            resultList = ceqResultinfoDao.findByArchiveCount(qryReason,deptCodes);
            CeqShortcutBo.Menu me = new CeqShortcutBo.Menu();
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
        ceqResultinfoDao.updateArchiveIdById(id, archiveId);
    }

    @Override
    public boolean isQueriedCreditUser(String creditUser) {
        log.info("isQueriedCreditUser param creditUser = ",creditUser);
        List<CeqResultinfoFlowManager> ceqResultInfos = ceqResultinfoDao.findByCcuser(creditUser);
        if(Collections3.isEmpty(ceqResultInfos)){
            return true;
        }
        return false;
    }

    @Override
    public long getUserQueryNum(String username) {
        log.info("getUserQueryNum  param username = ", username);
        RMap<String, Long> cache = redis.getMap(EntCacheConstant.USER_QUERY_NUN);
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
    public List<CeqQueryRecordBo> findAll(Map<String, Object> searchParams) {
        Specification<CeqResultinfoFlowManager> spec = PageUtil.buildSpecification(searchParams, CeqResultinfoFlowManager.class);
        List<CeqResultinfoFlowManager> infoList = ceqResultinfoDao.findAll(spec);
        List<CeqQueryRecordBo> ceqQueryRecordBos = ClassCloneUtil.copyIterableObject(infoList, CeqQueryRecordBo.class);
        return ceqQueryRecordBos;
    }

    @Override
    public List<CeqQueryRecordBo> findByIds(List<String> ids) {
        List<CeqResultinfoFlowManager> infoList = ceqResultinfoDao.findByIdIn(ids);
        List<CeqQueryRecordBo> ceqQueryRecordBos = ClassCloneUtil.copyIterableObject(infoList, CeqQueryRecordBo.class);
        return ceqQueryRecordBos;
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
