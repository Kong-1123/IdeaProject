/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC,
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqUserAttrService;
import cn.com.dhcc.creditquery.person.reportstructured.service.CpqReportanalysisService;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqResultinfoView;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportLogService;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewReadService;
import cn.com.dhcc.creditquery.person.reportview.service.CpqResultinfoService;
import cn.com.dhcc.creditquery.person.reportview.util.Constants;
import cn.com.dhcc.creditquery.person.reportview.util.ReportEncryptUtil;
import cn.com.dhcc.creditquery.person.reportview.util.ResultBeans;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.info.StorageResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author rzd
 * @date 2019年2月26日
 */
@Slf4j
@Service
public class CpqReportViewReadServiceImpl implements CpqReportViewReadService {
	static final String REPORT_FORMAT_XML = "XML";
	static final String REPORT_FORMAT_JSON = "JSON";
	static final String REPORT_FORMAT_HTML = "HTML";
	static final String REPORT_FORMAT_PDF = "PDF";
	@Autowired
    private CpqReportLogService reportLogService;
    @Autowired
    private CpqResultinfoService resultinfoService;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    CpqUserAttrService cpqUserAttrService;
    @Autowired
    CpqReportanalysisService cpqReportanalysisService;
    /*
     * (non-Javadoc)
     *
     * @see
     * cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewSaveService#
     * saveReport(java.lang.String, java.lang.String)
     */
    @Override
    public String readReport(String recordId, String userName, String orgCode,String reportType) {
        log.info("信用报告读取");
        reportLogService.save(recordId, userName, orgCode, Constant.view_log_type);
        String htmlPage = null;
        try {
            htmlPage = getV2ReportPage(recordId,reportType);
            if(!StringUtils.isBlank(htmlPage)) {
            	htmlPage = handleReportInfo(htmlPage);
            }
        } catch (FileNotFoundException fnfe) {
            log.error("读取信用报告recordId{},userName:{},orgCode:{} 报告文件不存在",
                    recordId, userName, orgCode);
            log.error(fnfe.getMessage());
        } catch (IOException ioe) {
            log.error("读取信用报告recordId{},userName:{},orgCode:{} 报告文件读取异常",
                    recordId, userName, orgCode);
            log.error(ioe.getMessage());
        }
        return htmlPage;
    }

    /**
     * 读取信用报告
     *
     * @param recordId
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @author rzd
     * @date 2019年2月27日
     */
    private String getV2ReportPage(String recordId,String reportType) throws FileNotFoundException, IOException {
        String htmlPage = null;
        String xmlPage = null;
        String reportFilePath = null;
        try {
            CpqResultinfoView findById = resultinfoService.findById(recordId);
            reportFilePath = findById.getHtmlPath();
            if(StringUtils.isBlank(reportFilePath)) {
            	reportFilePath = findById.getXmlPath();
            	//暂时只支持xml与html格式信用报告，在此就不做非空判断了。等添加别的格式信用报告后再做后续处理
            	xmlPage = readFile(xmlPage, reportFilePath);
                htmlPage = cpqReportanalysisService.reportConvert(xmlPage, REPORT_FORMAT_XML, REPORT_FORMAT_HTML);
                String operOrg = findById.getOperOrg();
                String rootDeptCode = UserUtilsForConfig.getRootDeptCode(operOrg);
              
				StorageResponse response = saveReport(htmlPage, "html", rootDeptCode);
				String htmlReportPath = response.getUri();
				findById.setHtmlPath(htmlReportPath);
				resultinfoService.create(findById);
            	return htmlPage;
            }
            htmlPage = readFile(htmlPage, reportFilePath);
        } catch (Exception e) {
            log.error("读取信用报告 错误，recordId:{},reportFilePath:{}", recordId, reportFilePath);
            log.error(e.getMessage());
        }
        return htmlPage;
    }

    
    /**
	 * 保存转换后的信用报告
	 * 
	 * @param htmlReport
	 * @param fileType
	 * @param topOrgCode2
	 */
	private StorageResponse saveReport(String htmlReport, String fileType, String topOrgCode2) {
		StorageRequest request = new StorageRequest();
		request.setContent(htmlReport);// 报告内容
		request.setFileType(fileType);// 后缀
		// 取得系统参数获取根路径
		String reportFilePath = CeqConfigUtil.getSystemWorkPath(topOrgCode2);
		if (StringUtils.isNotBlank(reportFilePath)) {
			request.setRootUri(reportFilePath);
		}
		request.setBussModelEN(Constants.BUSSMODELEN_FM);
		request.setSourceSystem(Constants.SOURCESYSTEM_QP);
		request.setCompression(true);
		request.setEncrypt(true);
		String systemStorageType = CeqConfigUtil.getSystemStorageType(topOrgCode2);
		if (StringUtils.isNotBlank(systemStorageType)) {
			request.setStorageType(systemStorageType);
		}
		StorageResponse saveFile = fileStorageService.saveFile(request);
		return saveFile;
	}
    /**
	 * 读取本地信用报告
	 * @param htmlPage
	 * @param reportFilePath
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年4月17日
	 */
	private String readFile(String htmlPage, String reportFilePath) {
		FileReadRequest readRequest = new FileReadRequest();
		String systemStorageType = ConfigUtil.getSystemStorageType();
		if (StringUtils.isNotBlank(systemStorageType)) {
			readRequest.setStorageType(systemStorageType);
		}
		readRequest.setZipFlag(true);
		readRequest.setDecrypt(true);
		readRequest.setResultType("0");
		readRequest.setUri(reportFilePath);
		FileReadResponse fileReadResponse = fileStorageService.readFileContent(readRequest);
		if (fileReadResponse != null) {
//             htmlPage = fileReadResponse.getContent();
			byte[] contentBytes = fileReadResponse.getContentBytes();
			htmlPage = new String(contentBytes);
		}
		return htmlPage;
	}
    
    /*
     * (non-Javadoc)
     *
     * @see
     * cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewReadService#
     * savePrintLog(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ResultBeans savePrintLog(String recordId, String userName, String orgCode) {
		log.info("保存打印报告日志-savePrintLog:recordId:{},userName:{},orgCode:{}",recordId,userName,orgCode);
    	ResultBeans rs = null;
        try {
            boolean flag = isAllowdPrint(recordId, userName);
            if (flag) {
                reportLogService.save(recordId, userName, orgCode, Constant.print_log_type);
                rs = new ResultBeans(Constants.SUCCESSCODE, "保存成功！");
            } else {
                rs = new ResultBeans(Constants.ERRORCODE, "超出用户或系统最大打印次数,请联系系统管理员!!!");
            }
        } catch (Exception e) {
            rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
            log.error("保存打印报告日志-savePrintLog error:", e);
        }
        return rs;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewReadService#
     * isAllowdPrint(String recordId, String userName)
     */
    @Override
    public boolean isAllowdPrint(String recordId, String userName) {
        try {
            log.info("isAllowdPrint recordId = {},userName = {}", recordId, userName);
            boolean flag = false;
            long count = reportLogService.findPrintCount(recordId);
            String systemDefaultNum = ConfigUtil.getMaxPrintAmount();
            long maxPrintNum = Long.parseLong(systemDefaultNum);
//            CpqUserAttrBo cpqUserAttrBo = UserAttrUtil.findCpqUserAttrBySystemUserId(userName);
            CpqUserAttrBo cpqUserAttrBo = cpqUserAttrService.findByUserId(userName);
            long userPrintAmount = maxPrintNum;
            if (null != cpqUserAttrBo) {
                String countStr = cpqUserAttrBo.getPrintAmount();
                if (StringUtils.isNotBlank(countStr)) {
                    userPrintAmount = Long.parseLong(countStr);
                }
            }
            long maxCount = (maxPrintNum > userPrintAmount ? userPrintAmount : maxPrintNum);
            if (count < maxCount) {
                flag = true;
            }
            log.info("isAllowdPrint result = {}", flag);
            return flag;
        } catch (Exception e) {
            log.error("isAllowdPrint error:", e);
            throw new RuntimeException("系统或用户打印参数设置错误,请联系系统管理员!!!");
        }
    }
    
     /**
     * 对获取的信用报告进行脱敏
     * @param content
     * @return
     */
    private String handleReportInfo(String content) {
    	log.info("handleReportInfo  begin!");
    	try {
    		content = ReportEncryptUtil.idEncrypt(content);
    		content = ReportEncryptUtil.numberEncrypt(content);
    		content = ReportEncryptUtil.addressEncrypt(content);
		} catch (Exception e) {
			log.error("handleReportInfo error = {}",e);
		}
    	log.info("handleReportInfo end!");
		return content;
    }
}
