/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.service.queryreport.impl;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import cn.com.dhcc.query.creditquerycommon.ReportIdUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeManagerService;
import cn.com.dhcc.creditquery.ent.query.bo.queryCounter.CeqMonitorCounterBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqOrgAttrBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportStructuredStorageBo;
import cn.com.dhcc.creditquery.ent.query.bo.querypboc.InterfaceRequestBo;
import cn.com.dhcc.creditquery.ent.query.bo.querypboc.QueryRespBo;
import cn.com.dhcc.creditquery.ent.query.bo.querypboc.WebRequestBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqOrgAttrService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.CeqQueryControlValidateService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.greenchanne.CeqGreenChannelService;
import cn.com.dhcc.creditquery.ent.querycounter.service.CeqCounterControlService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqQueryRecordService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.queryreport.CeqQueryReportService;
import cn.com.dhcc.creditquery.ent.querypboc.service.CeqQueryPbocService;
import cn.com.dhcc.creditquery.ent.queryuserrouter.service.CeqQueryUserRouterService;
import cn.com.dhcc.creditquery.ent.queryuserrouter.service.bean.CeqQueryPbocUser;
import cn.com.dhcc.creditquery.ent.queryuserrouter.service.bean.CeqRouterUser;
import cn.com.dhcc.creditquery.ent.reportstructured.service.CeqReportanalysisService;
import cn.com.dhcc.informationplatform.amqp.client.RabbitMQClient;
import cn.com.dhcc.informationplatform.amqp.factory.RabbitMQFactory;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.info.StorageResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.cache.bean.SystemOrg;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.queryapicommon.rabbitmq.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 信用报告查询服务流程
 * 
 * @author sjk
 * @date 2019年2月26日
 */
@Slf4j
@Service
public class CeqQueryReportServiceImpl implements CeqQueryReportService {


	private static final String LOCAL_NO_REPORT = "5";

	private static final String QUERY_SUCEESS = "1";

	/**
	 * 用户路由服务
	 */
	@Autowired
	private CeqQueryUserRouterService queryUserRouterService;
	/**
	 * 中心查询服务
	 */
	@Autowired
	private CeqQueryPbocService ceqQueryPbocService;
	/**
	 * 文件存储服务
	 */
	@Autowired
	private FileStorageService fileStorageService;
	/**
	 * 查询流程控制
	 */
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	/**
	 * 查询记录服务
	 */
	@Autowired
	private CeqQueryRecordService ceqQueryRecordService;

	/**
	 * 计数器服务
	 */
	@Autowired
	private CeqCounterControlService ceqCounterControlService;
	/**
	 * 信用报告解析及转换服务
	 */
	@Autowired
	private CeqReportanalysisService ceqReportanalysisService;

	/**
	 * 授权档案服务
	 */
	@Autowired
	private CeqAuthorizeManagerService ceqAuthorizeManagerService;
	
	@Autowired
	private CeqGreenChannelService  ceqGreenChannelServiceImpl;
	
	@Autowired
	private CeqOrgAttrService ceqOrgAttrService;
	
	@Autowired
	private CeqQueryControlValidateService queryControlValidateService;

	/**
	 * 查询状态 0：查询异常 1： 查询成功 2： 查无此人 3： 查询失败 4:报告不完整
	 */
	private final static String QUERY_PBOC_STATUS_SUCESS = "1";
	/**
	 * 报文类型xml
	 */
	private final static String XMLTYPE = "xml";
	/**
	 * 报文类型html
	 */
	private final static String HTMLTYPE = "html";

	@Override
	public CeqQueryReportFlowBo creditReportQuery(CeqReportQueryBo ceqReportQueryBo) {

		log.info("CpqQueryReportServiceImpl creditReportQuery start,ceqReportQueryBo={}", ceqReportQueryBo);
		QueryRespBo queryRespBo = null;
//		CpqQueryRecordBo ceqQueryRecordBo = null;
		Integer queryType = ceqReportQueryBo.getQueryType();
		/**
		 * 信用报告复用策略 1、若该值为负数,则查询该值绝对值内的本地报告，不查询征信中心; 2、若该值为0，则强制查询征信中心；
		 * 3、若该值为正数，则查询该值内的本地报告，本地无报告则查询征信中心 单位：天
		 */
		CeqQueryReportFlowBo ceqQueryReportFlowBo = null;
		if (queryType < 0) {
			// 查询本地
			ceqQueryReportFlowBo = queryLocalReport(ceqReportQueryBo, queryType);
		} else if (queryType == 0) {
			// 查询人行
			ceqQueryReportFlowBo = coreQueryPbocFlow(ceqReportQueryBo, queryRespBo);
		} else {
			// 查询该值内的本地报告，本地无报告则查询征信中心
			ceqQueryReportFlowBo = queryLocalReport(ceqReportQueryBo, queryType);
			String resCode = ceqQueryReportFlowBo.getResCode();
			if (!StringUtils.equals(resCode, Constants.QUERY_SUCCESSCODE)) {
				// 查询本地失败，查询人行
				ceqQueryReportFlowBo = coreQueryPbocFlow(ceqReportQueryBo, queryRespBo);
			}
		}
		log.info("creditReportQuery end,result = ",ceqQueryReportFlowBo);
		//查询完成，判断是否为特权用户。若为特权用户，则特权查询次数加一
		String operator = ceqReportQueryBo.getOperator();
		ceqGreenChannelServiceImpl.updatePrivilegeUserQueryNum(operator);
		
		return ceqQueryReportFlowBo;
	}

	/**
	 * 查询本地信用报告
	 * 
	 * @param ceqReportQueryBo
	 * @param queryType
	 * @return
	 */
	private CeqQueryReportFlowBo queryLocalReport(CeqReportQueryBo ceqReportQueryBo, Integer queryType) {
		CeqQueryReportFlowBo ceqQueryReportFlowBo = new CeqQueryReportFlowBo();
		ceqQueryReportFlowBo.setBoCommonField(ceqReportQueryBo);
		// 获取可查询的征信用户
		CeqQueryPbocUser queryPbocUser = queryUserRouterService
				.routerPlatformUser(encapRouterUserRequest(ceqReportQueryBo));
		if (null == queryPbocUser) {
			ceqQueryReportFlowBo.setResCode(Constants.FAILURE_CODE);
			ceqQueryReportFlowBo.setResMsg(Constants.CCUSER_MAP_NOEXIST);
			return ceqQueryReportFlowBo;
		}
		//仅根据中征码进行本地查询
		String signCode = ceqReportQueryBo.getSignCode();
		String qryReason = ceqReportQueryBo.getQueryReasonId();
		String topOrgCode = ceqReportQueryBo.getTopOrgCode();
		int abs = Math.abs(queryType);
		CeqQueryReportFlowBo localReport = ceqQueryRecordService.getLocalReport(signCode,qryReason, topOrgCode, abs + "");
		if (localReport == null) {
			CeqQueryRecordBo queryBo = buildCeqQueryRecordBoByCeqReportQueryBo(ceqReportQueryBo, "1", queryPbocUser);
			ceqQueryReportFlowBo = processQueryResult(ceqQueryReportFlowBo, queryBo,queryType);
			ceqQueryReportFlowBo.setResCode(Constants.QUERY_LOCALNOTREPORTCODE);
			ceqQueryReportFlowBo.setResMsg(Constants.QUERY_LOCALNOTREPORTMSG);
			return ceqQueryReportFlowBo;
		}

		String resultType = getResultType(ceqReportQueryBo);

		// 转换信用报告
		CeqQueryRecordBo reportQueryBo = convertReport(ceqReportQueryBo, ceqQueryReportFlowBo, queryPbocUser,
				localReport, resultType);

		// 处理查询结果
		ceqQueryReportFlowBo = processQueryResult(ceqQueryReportFlowBo, reportQueryBo,queryType);
		
		return ceqQueryReportFlowBo;
	}

	/**
	 * 本地查询结果的处理
	 * 
	 * @param ceqQueryReportFlowBo
	 * @param reportQueryBo
	 * @param queryType
	 * @return
	 */
	private CeqQueryReportFlowBo processQueryResult(CeqQueryReportFlowBo ceqQueryReportFlowBo,
			CeqQueryRecordBo reportQueryBo,Integer queryType) {
		String htmlReportPath = reportQueryBo.getHtmlPath();
		String xmlReportPath = reportQueryBo.getXmlPath();
		String jsonReportPath = reportQueryBo.getJsonPath();
		String pdfReportPath = reportQueryBo.getPdfPath();

		ceqQueryReportFlowBo.setHtmlReportPath(htmlReportPath);
		ceqQueryReportFlowBo.setXmlReportPath(xmlReportPath);
		ceqQueryReportFlowBo.setJsonReportPath(jsonReportPath);
		ceqQueryReportFlowBo.setPdfReportPath(pdfReportPath);

		boolean noReport = StringUtils.isBlank(htmlReportPath) && StringUtils.isBlank(xmlReportPath)
				&& StringUtils.isBlank(jsonReportPath) && StringUtils.isBlank(pdfReportPath);

		if (noReport) {
			reportQueryBo.setStatus(LOCAL_NO_REPORT);
			ceqQueryReportFlowBo.setResCode(Constants.QUERY_LOCALNOTREPORTCODE);
			ceqQueryReportFlowBo.setResMsg(Constants.QUERY_LOCALNOTREPORTMSG);
		} else {
			reportQueryBo.setStatus(QUERY_SUCEESS);
			ceqQueryReportFlowBo.setResCode(Constants.QUERY_SUCCESSCODE);
			ceqQueryReportFlowBo.setResMsg(Constants.QUERY_SUCCESSMSG);
		}

		if (queryType>0 &&Constants.QUERY_LOCALNOTREPORTCODE.equals(ceqQueryReportFlowBo.getResCode())) {
			return ceqQueryReportFlowBo;
		}
		CeqQueryRecordBo ceqQueryRecordBo = ceqFlowManageService.create(reportQueryBo);
		String autharchiveId = ceqQueryRecordBo.getAutharchiveId();
		if (StringUtils.isNotBlank(autharchiveId)) {
			ceqAuthorizeManagerService.updateQueryNumByRedis(autharchiveId);
		}
		ceqQueryReportFlowBo.setQueryRecordId(ceqQueryRecordBo.getId());
		CeqMonitorCounterBo ceqMonitorCounterBo = buildMonitorCounterBo(ceqQueryReportFlowBo, reportQueryBo, "1");
		ceqCounterControlService.updateCounterData(ceqMonitorCounterBo);
		return ceqQueryReportFlowBo;
	}

	private String getResultType(CeqReportQueryBo ceqReportQueryBo) {
		// 根据结果类型返回相应的报告，若无相应格式的报告，则进行转换
		String accessSource = ceqReportQueryBo.getAccessSource();
		String resultType = "";
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			InterfaceQueryParams interfaceQueryParamsBo = ceqReportQueryBo.getInterfaceQueryParamsBo();
			resultType = interfaceQueryParamsBo.getReportType();
		} else {
			WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
			CpqReportResultType reportResultType = webQueryBo.getReportResultType();
			resultType = reportResultType.getCode();
		}
		return resultType;
	}

	/**
	 * 根据结果类型和已有的信用报告进行转换
	 * 
	 * @param ceqReportQueryBo
	 * @param ceqQueryReportFlowBo
	 * @param queryPbocUser
	 * @param localReport
	 * @param resultType
	 * @return
	 */
	private CeqQueryRecordBo convertReport(CeqReportQueryBo ceqReportQueryBo, CeqQueryReportFlowBo ceqQueryReportFlowBo,
			CeqQueryPbocUser queryPbocUser, CeqQueryReportFlowBo localReport, String resultType) {
		String htmlReportPath = localReport.getHtmlReportPath();
		String xmlReportPath = localReport.getXmlReportPath();
		String jsonReportPath = localReport.getJsonReportPath();
		String pdfReportPath = localReport.getPdfReportPath();

		if (StringUtils.contains(resultType, "H")) {
			if (StringUtils.isNotBlank(htmlReportPath)) {
				String htmlCreditReport = localReport.getHtmlCreditReport();
				ceqQueryReportFlowBo.setHtmlCreditReport(htmlCreditReport);
			} else {
				// 将xml转换为html
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String htmlReport = ceqReportanalysisService.reportConvert(xmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_XML, CeqReportanalysisService.REPORT_FORMAT_HTML);
					ceqQueryReportFlowBo.setHtmlCreditReport(htmlReport);
					String fileType = "html";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(htmlReport, fileType, topOrgCode2);
					htmlReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "X")) {
			if (StringUtils.isNotBlank(xmlReportPath)) {
				String xmlCreditReport = localReport.getXmlCreditReport();
				ceqQueryReportFlowBo.setXmlCreditReport(xmlCreditReport);
			} else {
				// 将html转换为xml
				if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String xmlReport = ceqReportanalysisService.reportConvert(htmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_HTML, CeqReportanalysisService.REPORT_FORMAT_XML);
					ceqQueryReportFlowBo.setXmlCreditReport(xmlReport);
					String fileType = "xml";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(xmlReport, fileType, topOrgCode2);
					xmlReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "J")) {
			if (StringUtils.isNotBlank(jsonReportPath)) {
				String jsonCreditReport = localReport.getJsonCreditReport();
				ceqQueryReportFlowBo.setJsonCreditReport(jsonCreditReport);
			} else {
				// 将报告转换为json
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String jsonReport = ceqReportanalysisService.reportConvert(xmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_XML, CeqReportanalysisService.REPORT_FORMAT_JSON);
					ceqQueryReportFlowBo.setJsonCreditReport(jsonReport);
					String fileType = "json";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(jsonReport, fileType, topOrgCode2);
					jsonReportPath = response.getUri();
				} else if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String jsonReport = ceqReportanalysisService.reportConvert(htmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_HTML, CeqReportanalysisService.REPORT_FORMAT_JSON);
					ceqQueryReportFlowBo.setJsonCreditReport(jsonReport);
					String fileType = "json";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(jsonReport, fileType, topOrgCode2);
					jsonReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "P")) {
			if (StringUtils.isNotBlank(pdfReportPath)) {
				String pdfCreditReport = localReport.getPdfCreditReport();
				ceqQueryReportFlowBo.setPdfCreditReport(pdfCreditReport);
			} else {
				// 将报告转换为pdf
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String pdfReport = ceqReportanalysisService.reportConvert(xmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_XML, CeqReportanalysisService.REPORT_FORMAT_PDF);
					ceqQueryReportFlowBo.setPdfCreditReport(pdfReport);
					String fileType = "pdf";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(pdfReport, fileType, topOrgCode2);
					pdfReportPath = response.getUri();
				} else if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String pdfReport = ceqReportanalysisService.reportConvert(htmlCreditReport,
							CeqReportanalysisService.REPORT_FORMAT_HTML, CeqReportanalysisService.REPORT_FORMAT_PDF);
					ceqQueryReportFlowBo.setPdfCreditReport(pdfReport);
					String fileType = "pdf";
					String topOrgCode2 = ceqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(pdfReport, fileType, topOrgCode2);
					pdfReportPath = response.getUri();
				}
			}
		}

		CeqQueryRecordBo reportQueryBo = buildCeqQueryRecordBoByCeqReportQueryBo(ceqReportQueryBo, "1", queryPbocUser);
		reportQueryBo.setHtmlPath(htmlReportPath);
		reportQueryBo.setXmlPath(xmlReportPath);
		reportQueryBo.setJsonPath(jsonReportPath);
		reportQueryBo.setPdfPath(pdfReportPath);
		return reportQueryBo;
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
	 * 内部核心查询流程-->直接查中心并且记录查询日志。 1、获取可查询的征信用户。 2、查中心，判断是通过接口还是Web进行查询。
	 * 3、根据查询中心返回的结果，如果成功查询到报告，则写文件。 4、将查询记录写入查询记录表 5、返回响应报文。
	 * 
	 * @param ceqReportQueryBo
	 * @param queryRespBo
	 * @author Jerry.chen
	 * @date 2019年3月5日
	 */
	private CeqQueryReportFlowBo coreQueryPbocFlow(CeqReportQueryBo ceqReportQueryBo, QueryRespBo queryRespBo) {
		log.info("coreQueryPbocFlow start,ceqReportQueryBo = {},queryRespBo={}",ceqReportQueryBo,queryRespBo);
		CeqQueryRecordBo ceqQueryRecordBo;
		CeqQueryReportFlowBo ceqQueryReportFlowBo = new CeqQueryReportFlowBo();
		ceqQueryReportFlowBo.setBoCommonField(ceqReportQueryBo);
		try {
			String topOrgCode = ceqReportQueryBo.getTopOrgCode();
			CeqRouterUser routerUser = encapRouterUserRequest(ceqReportQueryBo);
			log.info("coreQueryPbocFlow routerPlatformUser params routerUser = ",routerUser);
			// 获取可查询的征信用户
			CeqQueryPbocUser queryPbocUser = queryUserRouterService.routerPlatformUser(routerUser);
			log.info("coreQueryPbocFlow routerPlatformUser result queryPbocUser = ",queryPbocUser);
			if (null == queryPbocUser) {
				ceqQueryReportFlowBo.setResCode(Constants.FAILURE_CODE);
				ceqQueryReportFlowBo.setResMsg(Constants.CCUSER_MAP_NOEXIST);
				return ceqQueryReportFlowBo;
			}
			String resultType = "";
			boolean isWEBQuery = true;
			String accessSource = ceqReportQueryBo.getAccessSource();
			// 通过系统参数判断查询web和接口的优先级， 1：接口 2：web 3：接口、web、
			String queryPbocStrategy = "";
			if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
				queryPbocStrategy = CeqConfigUtil.getQueryPbocStrategy(topOrgCode);
				InterfaceQueryParams interfaceQueryParamsBo = ceqReportQueryBo.getInterfaceQueryParamsBo();
				resultType = interfaceQueryParamsBo.getReportType();
			} else {
				queryPbocStrategy = CeqConfigUtil.getQueryPbocStrategy();
				WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
				CpqReportResultType reportResultType = webQueryBo.getReportResultType();
				resultType = reportResultType.getCode();
			}
			log.info("coreQueryPbocFlow queryPbocStrategy = "+ queryPbocStrategy);
			/*if (StringUtils.equals(accessSource, ACCESS_SOURCE_WEB)
					&& StringUtils.equals(queryPbocStrategy, Constants.CREDITREPORT_REQUEST_STRATEGY_INTERFACE)) {
				// 页面来源查询中心接口，暂时不支持。
				ceqQueryReportFlowBo.setResCode(Constants.QUERY_PBOC_STRATEGY_ERROR_CODE);
				ceqQueryReportFlowBo.setResMsg(Constants.QUERY_PBOC_STRATEGY_ERROR_MSG);
				return ceqQueryReportFlowBo;
			}*/
			if (queryPbocStrategy.equals(Constants.CREDITREPORT_REQUEST_STRATEGY_INTERFACE)) {// 接口请求
				InterfaceRequestBo integerQueryRequest = encapAPIQueryRequest(ceqReportQueryBo, queryPbocUser);
				queryRespBo = ceqQueryPbocService.interfaceQuery(integerQueryRequest);
				isWEBQuery = false;
			} else if (queryPbocStrategy.equals(Constants.CREDITREPORT_REQUEST_STRATEGY_WEB)) {// web请求
				WebRequestBo webQueryRequest = encapWebQueryRequest(ceqReportQueryBo, queryPbocUser);
				queryRespBo = ceqQueryPbocService.webQuery(webQueryRequest);
			}
			// 构建查询记录对象
			ceqQueryRecordBo = encapQueryResultToRecord(queryRespBo, ceqReportQueryBo);
			String autharchiveId = ceqQueryRecordBo.getAutharchiveId();
			if (StringUtils.isNotBlank(autharchiveId)) {
				ceqAuthorizeManagerService.updateQueryNumByRedis(autharchiveId);
			}

			// 查询状态 0：查询异常 1： 查询成功 2： 查无此人 3： 查询失败 4:报告不完整
			String status = queryRespBo.getStatus();
			StorageResponse storageResponse = null;
			if (QUERY_PBOC_STATUS_SUCESS.equals(status)) {
				// 查询记录存储
				ceqQueryReportFlowBo.setResCode(Constants.QUERY_SUCCESSCODE);
				ceqQueryReportFlowBo.setResMsg(Constants.QUERY_SUCCESSMSG);

				// 查询回来的报告
				String sourceReport = queryRespBo.getReportMsg();
				// 存储查询回来的信用报告
				String fileType = isWEBQuery ? "html" : "xml";
				StorageRequest reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, fileType, sourceReport);
				// html报告存储
				storageResponse = fileStorageService.saveFile(reportSaveRequest);

				if (isWEBQuery) {
					String uri = storageResponse.getUri();
					ceqQueryRecordBo.setHtmlPath(uri);
					ceqQueryReportFlowBo.setHtmlCreditReport(sourceReport);
					ceqQueryReportFlowBo.setHtmlReportPath(uri);
				} else {
					String uri = storageResponse.getUri();
					ceqQueryRecordBo.setXmlPath(uri);
					ceqQueryReportFlowBo.setXmlCreditReport(sourceReport);
					ceqQueryReportFlowBo.setXmlReportPath(uri);
				}

				// 根据报告结果类型进行报告转换
				convertReportToResultType(queryRespBo, ceqQueryReportFlowBo, ceqQueryRecordBo, resultType, isWEBQuery,
						sourceReport);
				// 结构化入库
				reportStructuredStorage(ceqReportQueryBo, ceqQueryReportFlowBo);
				//获取信用报告编号
				String reportId = getReportId(ceqQueryReportFlowBo, topOrgCode);
				ceqQueryRecordBo.setReportId(reportId);

			}else {
				//其他情况，弹出提示信息
				ceqQueryReportFlowBo.setResCode("10999");
				ceqQueryReportFlowBo.setResMsg(queryRespBo.getMsg());
			}

			ceqQueryRecordBo = ceqFlowManageService.create(ceqQueryRecordBo);
			ceqQueryReportFlowBo.setQueryRecordId(ceqQueryRecordBo.getId());
			ceqQueryReportFlowBo.setUseTime(queryRespBo.getUseTime());
			ceqQueryReportFlowBo.setReportSource(ceqQueryRecordBo.getSource());
			// 构建计数器业务对象
			CeqMonitorCounterBo ceqMonitorCounterBo = buildMonitorCounterBo(ceqQueryReportFlowBo, ceqQueryRecordBo,
					"2");
			ceqCounterControlService.updateCounterData(ceqMonitorCounterBo);

		} catch (Exception e) {
			log.error("CeqQueryReportServiceImpl creditReportQuery end...,error={}", e);
			ceqQueryReportFlowBo.setResCode(Constants.QUERY_EXCEPTIONCODE);
			ceqQueryReportFlowBo.setResMsg(Constants.QUERY_EXCEPTIONMSG);
		}
		log.info("coreQueryPbocFlow end,result = {}",ceqQueryReportFlowBo);
		return ceqQueryReportFlowBo;
	}

	/**
	 *　获取信用报告编号
	 * @param ceqQueryReportFlowBo
	 * @param topOrgCode
	 * @return
	 */
	private String getReportId(CeqQueryReportFlowBo ceqQueryReportFlowBo, String topOrgCode) {
		// 如果存在xml信用报告，则直接从xml中获取信用报告编号；若不存在，则通过html进行获取
		String xmlCreditReport = ceqQueryReportFlowBo.getXmlCreditReport();
		String reportId = "";
		if (StringUtils.isNotBlank(xmlCreditReport)) {
			reportId = ReportIdUtil.getReportIdFromXml(topOrgCode, xmlCreditReport, ReportIdUtil.ENTERPRISE_BUSSLINE);
		}else {
			String htmlCreditReport = ceqQueryReportFlowBo.getHtmlCreditReport();
			if (StringUtils.isNotBlank(htmlCreditReport)) {
				reportId = ReportIdUtil.getReportIdFromHtml(htmlCreditReport,ReportIdUtil.ENTERPRISE_BUSSLINE);
			}
		}
		return reportId;
	}

	private CeqMonitorCounterBo buildMonitorCounterBo(CeqQueryReportFlowBo ceqQueryReportFlowBo,
			CeqQueryRecordBo ceqQueryRecordBo, String source) {
		CeqMonitorCounterBo ceqMonitorCounterBo = new CeqMonitorCounterBo();
		ceqMonitorCounterBo.setBoCommonField(ceqQueryReportFlowBo);
		ceqMonitorCounterBo.setCreditUser(ceqQueryRecordBo.getCreditUser());
		ceqMonitorCounterBo.setDeptCode(ceqQueryRecordBo.getOperOrg());
		ceqMonitorCounterBo.setNum(1);
		ceqMonitorCounterBo.setSource(source);
		Integer statusInt = Integer.valueOf(ceqQueryRecordBo.getStatus());
		ceqMonitorCounterBo.setStatus(statusInt);
		ceqMonitorCounterBo.setUserName(ceqQueryRecordBo.getOperator());
		ceqMonitorCounterBo.setBatchFlag(false);
		return ceqMonitorCounterBo;
	}

	private void reportStructuredStorage(CeqReportQueryBo ceqReportQueryBo, CeqQueryReportFlowBo ceqQueryReportFlowBo) {
		String topOrgCode = ceqReportQueryBo.getTopOrgCode();
		log.debug("get isStructuredStorage flag param topOrgCode = ", topOrgCode);
		boolean isStructuredStorage = CeqConfigUtil.isStructuredStorage(topOrgCode);
		log.debug("get isStructuredStorage flag  = ", isStructuredStorage);
		if (isStructuredStorage) {
			String xmlReportPath = ceqQueryReportFlowBo.getXmlReportPath();
			String htmlReportPath = ceqQueryReportFlowBo.getHtmlReportPath();
			log.debug("StructuredStorage xmlReportPath  = ", xmlReportPath);

			CeqReportStructuredStorageBo structuredStorage = CeqReportStructuredStorageBo
					.buildReportStructuredStorage(xmlReportPath, htmlReportPath);
			if (null != structuredStorage) {
				// 向MQ中发送消息
				RabbitMQFactory rabbitMQFactory = RabbitMqUtil.getRabbitMQFactory();
				RabbitMQClient bindingqueue = rabbitMQFactory.topicExchangeBindingqueue(
						RabbitMqUtil.ENT_STRUCTURED_STORAGE_EXCHANGENAME,
						RabbitMqUtil.ENT_STRUCTURED_STORAGE_QUEUENAME,
						RabbitMqUtil.ENT_STRUCTURED_STORAGE_TOPIC_ROUTINGKEY);
				String message = JsonUtil.toJSONString(structuredStorage);
				bindingqueue.send(RabbitMqUtil.ENT_STRUCTURED_STORAGE_SEND_ROUTINGKEY, message);
				log.info("reportStructuredStorage send message to MQ message = ", message);
			}
		}
	}

	/**
	 * 将查得得信用报告根据resultType进行转换，并将报告存储路径等构建至查询记录中
	 * 
	 * @param queryRespBo
	 * @param ceqQueryReportFlowBo
	 * @param ceqQueryRecordBo
	 * @param resultType
	 * @param isWEBQuery
	 * @param sourceReport
	 */
	private void convertReportToResultType(QueryRespBo queryRespBo, CeqQueryReportFlowBo ceqQueryReportFlowBo,
			CeqQueryRecordBo ceqQueryRecordBo, String resultType, boolean isWEBQuery, String sourceReport) {
		StorageResponse storageResponse;
		StorageRequest reportSaveRequest;
		if (resultType.contains("H")) {
			String sourceType = isWEBQuery ? CeqReportanalysisService.REPORT_FORMAT_HTML
					: CeqReportanalysisService.REPORT_FORMAT_XML;
			String reportConvert = ceqReportanalysisService.reportConvert(sourceReport, sourceType,
					CeqReportanalysisService.REPORT_FORMAT_HTML);
			if (!StringUtils.equals(sourceType, CeqReportanalysisService.REPORT_FORMAT_HTML) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "html", reportConvert);
				// html报告存储
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				String uri = storageResponse.getUri();
				ceqQueryRecordBo.setHtmlPath(uri);
				ceqQueryReportFlowBo.setHtmlReportPath(uri);
				ceqQueryReportFlowBo.setHtmlCreditReport(reportConvert);
			}
		}
		if (resultType.contains("X")) {
			String sourceType = isWEBQuery ? CeqReportanalysisService.REPORT_FORMAT_HTML
					: CeqReportanalysisService.REPORT_FORMAT_XML;
			String reportConvert = ceqReportanalysisService.reportConvert(sourceReport, sourceType,
					CeqReportanalysisService.REPORT_FORMAT_XML);
			if (!StringUtils.equals(sourceType, CeqReportanalysisService.REPORT_FORMAT_XML) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "xml", reportConvert);
				// xml报告存储
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				String uri = storageResponse.getUri();
				ceqQueryRecordBo.setXmlPath(uri);
				ceqQueryReportFlowBo.setXmlReportPath(uri);
				ceqQueryReportFlowBo.setXmlCreditReport(reportConvert);
			}
		}
		if (resultType.contains("J")) {
			String sourceType = isWEBQuery ? CeqReportanalysisService.REPORT_FORMAT_HTML
					: CeqReportanalysisService.REPORT_FORMAT_XML;
			String reportConvert = ceqReportanalysisService.reportConvert(sourceReport, sourceType,
					CeqReportanalysisService.REPORT_FORMAT_JSON);
			if (!StringUtils.equals(sourceType, CeqReportanalysisService.REPORT_FORMAT_JSON) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "json", reportConvert);
				// json报告存储
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				String uri = storageResponse.getUri();
				ceqQueryRecordBo.setJsonPath(uri);
				ceqQueryReportFlowBo.setJsonCreditReport(reportConvert);
				ceqQueryReportFlowBo.setJsonReportPath(uri);
			}
		}
		if (resultType.contains("P")) {
			String sourceType = isWEBQuery ? CeqReportanalysisService.REPORT_FORMAT_HTML
					: CeqReportanalysisService.REPORT_FORMAT_XML;
			String reportConvert = ceqReportanalysisService.reportConvert(sourceReport, sourceType,
					CeqReportanalysisService.REPORT_FORMAT_PDF);
			if (!StringUtils.equals(sourceType, CeqReportanalysisService.REPORT_FORMAT_PDF) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "pdf", reportConvert);
				// pdf报告存储
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				String uri = storageResponse.getUri();
				ceqQueryRecordBo.setPdfPath(uri);
				ceqQueryReportFlowBo.setPdfCreditReport(reportConvert);
				ceqQueryReportFlowBo.setPdfReportPath(uri);
			}
		}
	}

	/**
	 * 封装获取征信用户的路由请求信息
	 * 
	 * @param ceqReportQueryBo
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private CeqRouterUser encapRouterUserRequest(CeqReportQueryBo ceqReportQueryBo) {
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest start...ceqReportQueryBo={}",ceqReportQueryBo);
		String queryUser = null;
		SystemOrg systemOrg = null;
		//获取业务属性查询员或者审批员参数
		String queryUserType = CeqConfigUtil.getQueryUser(ceqReportQueryBo.getTopOrgCode());
		// 查询原因是否需要进行审批
		boolean isReasonNeedApprove = queryControlValidateService.isReasonNeedApprove(ceqReportQueryBo.getQueryReasonId(), ceqReportQueryBo.getTopOrgCode());
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest queryUserType={}",queryUserType);
		if(queryUserType.equals("1") || ceqReportQueryBo.getAccessSource().equals("2") || !isReasonNeedApprove) {//查询员、接口不进行管控，默认查询员、、查询原因是否需要审批
			queryUser = ceqReportQueryBo.getOperator();
		}else {//审批员
			queryUser = ceqReportQueryBo.getWebQueryBo().getApproveUser();
		}
		systemOrg = UserUtilsForConfig.getOrgCache(queryUser);// 根据前置用户获取机构码
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest systemOrg={}",systemOrg);
		CeqRouterUser routerUser = new CeqRouterUser(queryUser, systemOrg.getOrgId(), null);
		return routerUser;
	}

	/**
	 * 封装向征信中心服务请求的信息 web
	 * 
	 * @param ceqReportQueryBo
	 * @param queryPbocUser
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private WebRequestBo encapWebQueryRequest(CeqReportQueryBo ceqReportQueryBo, CeqQueryPbocUser queryPbocUser) {
		WebRequestBo webRequestBo = new WebRequestBo();
		log.info("CpqQueryReportServiceImpl encapWebRequestBoRequest start...");
		/**
		 * 查询要素
		 */
		ClassCloneUtil.copyObject(ceqReportQueryBo, webRequestBo);
		webRequestBo.setSignCode(ceqReportQueryBo.getSignCode());// 三项标识
		webRequestBo.setOrgInstCode(ceqReportQueryBo.getOrgInstCode());
		webRequestBo.setUniformSocialCredCode(ceqReportQueryBo.getUniformSocialCredCode());
		//TODO  将机构信用码等查询要素转换为otherIdentityCode与otherIdentityCode
		webRequestBo.setQueryReasonID(ceqReportQueryBo.getQueryReasonId());// 查询原因
		String accessSource = ceqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			webRequestBo.setReportVersion(ceqReportQueryBo.getInterfaceQueryParamsBo().getQueryFormat());// 信用报告版本
			webRequestBo.setReportFormat(ceqReportQueryBo.getInterfaceQueryParamsBo().getReportType());// 信用报告封装格式
			/**
			 * 记录要素
			 */
			CeqReportQueryBo.InterfaceQueryParams interfaceQueryParams = ceqReportQueryBo.getInterfaceQueryParamsBo();
			webRequestBo.setOperOrg(interfaceQueryParams.getQueryOrg());// 查询机构
			webRequestBo.setCheckId(interfaceQueryParams.getApproveId());// 审批请求ID
			webRequestBo.setCheckWay(interfaceQueryParams.getApproveWay());// 审批方式
			webRequestBo.setRekOrg(interfaceQueryParams.getApproveOrg());// 审批机构
			webRequestBo.setRekTime(interfaceQueryParams.getApproveTime());// 审批时间
			webRequestBo.setRekUser(interfaceQueryParams.getApproveUser());// 审批用户
			webRequestBo.setBatchFlag(interfaceQueryParams.getBatchFlag());// 批量标志
			webRequestBo.setMsgNo(interfaceQueryParams.getMsgNo());// 批量查询批次号
			webRequestBo.setAutharchiveId(interfaceQueryParams.getAuthArchiveId());// 关联档案ID
			webRequestBo.setAssocbsnssData(interfaceQueryParams.getRelationBaseData());// 相关联的业务数据
		} else {
			webRequestBo.setReportVersion(ceqReportQueryBo.getWebQueryBo().getReportVersion());// 信用报告版本
			webRequestBo.setReportFormat(ceqReportQueryBo.getWebQueryBo().getReportResultType().getCode());// 信用报告封装格式
			/**
			 * 记录要素
			 */
			webRequestBo.setOperOrg(ceqReportQueryBo.getWebQueryBo().getQueryOrg());// 查询机构
			webRequestBo.setCheckId(ceqReportQueryBo.getWebQueryBo().getApproveId());// 审批请求ID
			webRequestBo.setCheckWay(ceqReportQueryBo.getWebQueryBo().getApproveWay());// 审批方式
			webRequestBo.setRekOrg(ceqReportQueryBo.getWebQueryBo().getApproveOrg());// 审批机构
			webRequestBo.setRekTime(ceqReportQueryBo.getWebQueryBo().getApproveTime());// 审批时间
			webRequestBo.setRekUser(ceqReportQueryBo.getWebQueryBo().getApproveUser());// 审批用户
			webRequestBo.setBatchFlag(ceqReportQueryBo.getWebQueryBo().getBatchFlag());// 批量标志
			webRequestBo.setMsgNo(ceqReportQueryBo.getWebQueryBo().getMsgNo());// 批量查询批次号
			webRequestBo.setAutharchiveId(ceqReportQueryBo.getWebQueryBo().getAuthArchiveId());// 关联档案ID
			webRequestBo.setAssocbsnssData(ceqReportQueryBo.getWebQueryBo().getRelationBaseData());// 相关联的业务数据
		}
		webRequestBo.setCreditUser(queryPbocUser.getCenterUserName());// 征信用户
		webRequestBo.setCreditPassWord(queryPbocUser.getCenterUserPWD());// 征信用户密码
		webRequestBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());// 征信用户金融机构代码
		/**
		 * 记录要素
		 */
		webRequestBo.setOperator(ceqReportQueryBo.getOperator());// 前置查询用户
		return webRequestBo;
	}

	/**
	 * 封装向征信中心服务请求的信息 api
	 * 
	 * @param ceqReportQueryBo
	 * @param queryPbocUser
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private InterfaceRequestBo encapAPIQueryRequest(CeqReportQueryBo ceqReportQueryBo, CeqQueryPbocUser queryPbocUser) {
		log.info("CpqQueryReportServiceImpl encapInterfaceRequestBoRequest start...");
		InterfaceRequestBo interfaceRequestBo = new InterfaceRequestBo();

		// ==============================用于查询的参数信息==================================================
		interfaceRequestBo.setOperator(ceqReportQueryBo.getOperator());// 前置查询用户
		interfaceRequestBo.setInstLoginName(queryPbocUser.getCenterUserName());// 征信用户
		interfaceRequestBo.setInstLoginPassword(queryPbocUser.getCenterUserPWD());// 征信用户密码
		interfaceRequestBo.setInstCode(queryPbocUser.getCenterUserOrgId());// 查询机构代码-征信用户金融机构代码
		interfaceRequestBo.setEntName(ceqReportQueryBo.getEnterpriseName());
		//中征码
		String signCode = ceqReportQueryBo.getSignCode();
		//组织机构代码
		String orgInstCode = ceqReportQueryBo.getOrgInstCode();
		//统一社会信用代码
		String uniformSocialCredCode = ceqReportQueryBo.getUniformSocialCredCode();
		//根据查询要素信息进行code值转换
		if (StringUtils.isNotBlank(signCode)) {
			//set企业身份标识类型-中征码
			interfaceRequestBo.setEntCertType(QUERY_ENT_CERTTYPE_SIGNCODE);
			//set企业身份标识代码
			interfaceRequestBo.setEntCertNum(signCode);
		}else if (StringUtils.isNotBlank(orgInstCode)) {
//set企业身份标识类型-中征码
			interfaceRequestBo.setEntCertType(QUERY_ENT_CERTTYPE_ORGINSTCODE);
			//set企业身份标识代码
			interfaceRequestBo.setEntCertNum(orgInstCode);
		}else if (StringUtils.isNotBlank(uniformSocialCredCode)) {
//set企业身份标识类型-中征码
			interfaceRequestBo.setEntCertType(QUERY_ENT_CERTTYPE_UNIFORMSOCIALCREDCODE);
			//set企业身份标识代码
			interfaceRequestBo.setEntCertNum(uniformSocialCredCode);
		}

		interfaceRequestBo.setQueryReason(ceqReportQueryBo.getQueryReasonId());// 查询原因
		interfaceRequestBo.setOperator(ceqReportQueryBo.getOperator());// 查询用户
		String serviceCode = CeqConfigUtil.getServiceCode(ceqReportQueryBo.getTopOrgCode());
		interfaceRequestBo.setServiceCode(serviceCode);
		SystemOrg systemOrg = UserUtilsForConfig.getOrgCache(ceqReportQueryBo.getOperator());// 根据前置用户获取机构码
		interfaceRequestBo.setOperOrg(systemOrg.getOrgId());// 机构
		interfaceRequestBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(systemOrg.getOrgId()));
		
		/**
		 * 根据机构代码获取发起机构代码
		 */
		CeqOrgAttrBo orgAttrBo = ceqOrgAttrService.findByOrgId(systemOrg.getOrgId());
		interfaceRequestBo.setPbocOrgCode(orgAttrBo.getPbocOrgCode());
		if(StringUtils.isBlank(orgAttrBo.getPbocOrgCode())) {
			interfaceRequestBo.setPbocOrgCode(queryPbocUser.getCenterUserOrgId());
		}
		
		// ==============================用于记录的参数信息==================================================
		ClassCloneUtil.copyObject(ceqReportQueryBo, interfaceRequestBo);
		interfaceRequestBo.setSignCode(signCode);
		interfaceRequestBo.setOrgInstCode(orgInstCode);
		interfaceRequestBo.setUniformSocialCredCode(uniformSocialCredCode);
		//TODO  将机构信用码等查询要素转换为otherIdentityCode与otherIdentityCode

		interfaceRequestBo.setQueryReasonID(ceqReportQueryBo.getQueryReasonId());// 查询原因
		interfaceRequestBo.setCreditUser(queryPbocUser.getCenterUserName());// 征信用户
		interfaceRequestBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());// 征信用户金融机构代码
		String accessSource = ceqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			interfaceRequestBo.setReportVersion(ceqReportQueryBo.getInterfaceQueryParamsBo().getQueryFormat());// 信用报告版本
			interfaceRequestBo.setReportFormat(ceqReportQueryBo.getInterfaceQueryParamsBo().getReportType());// 信用报告封装格式
			CeqReportQueryBo.InterfaceQueryParams interfaceQueryParams = ceqReportQueryBo.getInterfaceQueryParamsBo();
			interfaceRequestBo.setOperOrg(interfaceQueryParams.getQueryOrg());// 查询机构
			interfaceRequestBo.setCheckId(interfaceQueryParams.getApproveId());// 审批请求ID
			interfaceRequestBo.setCheckWay(interfaceQueryParams.getApproveWay());// 审批方式
			interfaceRequestBo.setRekOrg(interfaceQueryParams.getApproveOrg());// 审批机构
			interfaceRequestBo.setRekTime(interfaceQueryParams.getApproveTime());// 审批时间
			interfaceRequestBo.setRekUser(interfaceQueryParams.getApproveUser());// 审批用户
			interfaceRequestBo.setBatchFlag(interfaceQueryParams.getBatchFlag());// 批量标志
			interfaceRequestBo.setMsgNo(interfaceQueryParams.getMsgNo());// 批量查询批次号
			interfaceRequestBo.setAutharchiveId(interfaceQueryParams.getAuthArchiveId());// 关联档案ID
			interfaceRequestBo.setAssocbsnssData(interfaceQueryParams.getRelationBaseData());// 相关联的业务数据
		} else {
			interfaceRequestBo.setReportVersion(ceqReportQueryBo.getWebQueryBo().getReportVersion());// 信用报告版本
			interfaceRequestBo.setReportFormat(ceqReportQueryBo.getWebQueryBo().getReportResultType().getCode());// 信用报告封装格式
			interfaceRequestBo.setOperOrg(ceqReportQueryBo.getWebQueryBo().getQueryOrg());// 查询机构
			interfaceRequestBo.setCheckId(ceqReportQueryBo.getWebQueryBo().getApproveId());// 审批请求ID
			interfaceRequestBo.setCheckWay(ceqReportQueryBo.getWebQueryBo().getApproveWay());// 审批方式
			interfaceRequestBo.setRekOrg(ceqReportQueryBo.getWebQueryBo().getApproveOrg());// 审批机构
			interfaceRequestBo.setRekTime(ceqReportQueryBo.getWebQueryBo().getApproveTime());// 审批时间
			interfaceRequestBo.setRekUser(ceqReportQueryBo.getWebQueryBo().getApproveUser());// 审批用户
			interfaceRequestBo.setBatchFlag(ceqReportQueryBo.getWebQueryBo().getBatchFlag());// 批量标志
			interfaceRequestBo.setMsgNo(ceqReportQueryBo.getWebQueryBo().getMsgNo());// 批量查询批次号
			interfaceRequestBo.setAutharchiveId(ceqReportQueryBo.getWebQueryBo().getAuthArchiveId());// 关联档案ID
			interfaceRequestBo.setAssocbsnssData(ceqReportQueryBo.getWebQueryBo().getRelationBaseData());// 相关联的业务数据
		}

		return interfaceRequestBo;
	}

	/**
	 * 封装向文件存储服务保存信用报告的请求信息
	 * 
	 * @param queryRespBo
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private StorageRequest encapCreditReportSaveRequest(QueryRespBo queryRespBo, String fileType, String reportMsg) {
		StorageRequest request = new StorageRequest();
		request.setSerialNumber(queryRespBo.getSerialNumber());
		request.setContent(reportMsg);// 报告内容
		request.setFileType(fileType);// 后缀
		// 取得系统参数获取根路径
		String rootDeptCode = UserUtilsForConfig.getRootDeptCode(queryRespBo.getOperOrg());
		String reportFilePath = CeqConfigUtil.getSystemWorkPath(rootDeptCode);
		if (StringUtils.isNotBlank(reportFilePath)) {
			request.setRootUri(reportFilePath);
		}
		request.setBussModelEN(Constants.BUSSMODELEN_FM);
		request.setSourceSystem(Constants.SOURCESYSTEM_QE);
		request.setCompression(true);
		request.setEncrypt(true);
		String operOrg = queryRespBo.getOperOrg();
		String topOrg = UserUtilsForConfig.getRootDeptCode(operOrg);
		String systemStorageType = CeqConfigUtil.getSystemStorageType(topOrg);
		if (StringUtils.isNotBlank(systemStorageType)) {
			request.setStorageType(systemStorageType);
		}
		return request;
	}

	/**
	 * 封装查询结果到查询记录bo
	 * 
	 * @param queryRespBo
	 * @param ceqReportQueryBo
	 * @return
	 * @author DHC-S
	 * @date 2019年2月28日
	 */
	private CeqQueryRecordBo encapQueryResultToRecord(QueryRespBo queryRespBo, CeqReportQueryBo ceqReportQueryBo) {
		CeqQueryRecordBo ceqQueryRecordBo = new CeqQueryRecordBo();
		ClassCloneUtil.copyObject(queryRespBo,ceqQueryRecordBo);

		ceqQueryRecordBo.setQueryType("0");
		ceqQueryRecordBo.setQueryFormat(queryRespBo.getReportVersion());
		ceqQueryRecordBo.setResultType(queryRespBo.getReportFormat());
		ceqQueryRecordBo.setQueryReasonID(queryRespBo.getQueryReasonID());
		ceqQueryRecordBo.setQryReason(queryRespBo.getQueryReasonID());
		ceqQueryRecordBo.setReportVersion("2.0.0");
		ceqQueryRecordBo.setReportFormat(queryRespBo.getReportFormat());
		ceqQueryRecordBo.setCreditUser(queryRespBo.getCreditUser());
		ceqQueryRecordBo.setQueryOrg(queryRespBo.getQueryOrg());
		ceqQueryRecordBo.setOperator(queryRespBo.getOperator());
		ceqQueryRecordBo.setOperOrg(queryRespBo.getOperOrg());
		ceqQueryRecordBo.setQueryTime(new Date());
		ceqQueryRecordBo.setAssocbsnssData(queryRespBo.getAssocbsnssData());
		ceqQueryRecordBo.setAutharchiveId(queryRespBo.getAutharchiveId());
		ceqQueryRecordBo.setBatchFlag("1");
		ceqQueryRecordBo.setMsgNo(queryRespBo.getMsgNo());
		ceqQueryRecordBo.getQueryMode();
		ceqQueryRecordBo.setSource("2");
		ceqQueryRecordBo.setStatus(queryRespBo.getStatus());
		ceqQueryRecordBo.setUpdateTime(new Date());
		ceqQueryRecordBo.setCheckId(queryRespBo.getCheckId());
		ceqQueryRecordBo.setCheckWay(queryRespBo.getCheckWay());
		ceqQueryRecordBo.setRekOrg(queryRespBo.getRekOrg());
		ceqQueryRecordBo.setRekTime(queryRespBo.getRekTime());
		ceqQueryRecordBo.setRekUser(queryRespBo.getRekUser());
		String useTime = queryRespBo.getUseTime();
		if(StringUtils.isNotBlank(useTime)) {
			ceqQueryRecordBo.setUseTime(Integer.valueOf(useTime));
		}
		String accessSource = ceqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			InterfaceQueryParams interfaceQueryParamsBo = ceqReportQueryBo.getInterfaceQueryParamsBo();
			ceqQueryRecordBo.setCallSysUser(ceqReportQueryBo.getInterfaceQueryParamsBo().getCallSysUser());
			ceqQueryRecordBo.setRecheckUserName(ceqReportQueryBo.getInterfaceQueryParamsBo().getRecheckUser());
			ceqQueryRecordBo.setClientIp(interfaceQueryParamsBo.getClientIp());
			ceqQueryRecordBo.setChannelId(interfaceQueryParamsBo.getBusinessLine());
			ceqQueryRecordBo.setCstmsysId(interfaceQueryParamsBo.getSysCode());
			ceqQueryRecordBo.setBatchFlag(interfaceQueryParamsBo.getBatchFlag());
			ceqQueryRecordBo.setMsgNo(interfaceQueryParamsBo.getMsgNo());
		} else {
			WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
			ceqQueryRecordBo.setClientIp(webQueryBo.getClientIp());
		}
		return ceqQueryRecordBo;
	}

	/**
	 * 使用查询请求信息业务对象{@link CeqReportQueryBo }构造一个查询记录业务对象{@link CeqQueryRecordBo }
	 * 
	 * @param ceqReportQueryBo
	 * @return
	 */
	private CeqQueryRecordBo buildCeqQueryRecordBoByCeqReportQueryBo(CeqReportQueryBo ceqReportQueryBo,
			String reportSource, CeqQueryPbocUser queryPbocUser) {
		CeqQueryRecordBo ceqQueryRecordBo = new CeqQueryRecordBo();
		ClassCloneUtil.copyObject(ceqReportQueryBo,ceqQueryRecordBo);

		ceqQueryRecordBo.setOperator(ceqReportQueryBo.getOperator());
		ceqQueryRecordBo.setQryReason(ceqReportQueryBo.getQueryReasonId());
		ceqQueryRecordBo.setQueryReasonID(ceqReportQueryBo.getQueryReasonId());
		ceqQueryRecordBo.setSource(reportSource);
		ceqQueryRecordBo.setCreditUser(queryPbocUser.getCenterUserName());
		ceqQueryRecordBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());
		ceqQueryRecordBo.setQueryType("0");
		ceqQueryRecordBo.setQueryTime(new Date());
		ceqQueryRecordBo.setUpdateTime(new Date());
		String accessSource = ceqReportQueryBo.getAccessSource();
		if (StringUtils.equals(ACCESS_SOURCE_INTERFACE, accessSource)) {
			InterfaceQueryParams interfaceQueryParamsBo = ceqReportQueryBo.getInterfaceQueryParamsBo();
			ceqQueryRecordBo.setBatchFlag(interfaceQueryParamsBo.getBatchFlag());
			ceqQueryRecordBo.setReportVersion(interfaceQueryParamsBo.getReportVersion());
			ceqQueryRecordBo.setChannelId(interfaceQueryParamsBo.getBusinessLine());
			ceqQueryRecordBo.setCstmsysId(interfaceQueryParamsBo.getSysCode());
			ceqQueryRecordBo.setClientIp(interfaceQueryParamsBo.getUserIp());
			ceqQueryRecordBo.setCallSysUser(interfaceQueryParamsBo.getCallSysUser());
			ceqQueryRecordBo.setRecheckUserName(interfaceQueryParamsBo.getRecheckUser());
			ceqQueryRecordBo.setAssocbsnssData(interfaceQueryParamsBo.getBussid());
			ceqQueryRecordBo.setQueryFormat(interfaceQueryParamsBo.getQueryFormat());
			ceqQueryRecordBo.setResultType(interfaceQueryParamsBo.getReportType());
			ceqQueryRecordBo.setAutharchiveId(interfaceQueryParamsBo.getAuthArchiveId());
			ceqQueryRecordBo.setAssocbsnssData(interfaceQueryParamsBo.getAuthorNum());
			ceqQueryRecordBo.setOperOrg(interfaceQueryParamsBo.getQueryOrg());
			ceqQueryRecordBo.setReportVersion("2.0.0");
			ceqQueryRecordBo.setReportFormat(interfaceQueryParamsBo.getReportType());
			ceqQueryRecordBo.setMsgNo(interfaceQueryParamsBo.getMsgNo());
		} else if (StringUtils.equals(ACCESS_SOURCE_WEB, accessSource)) {
			WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
			ceqQueryRecordBo.setAutharchiveId(webQueryBo.getAuthArchiveId());
			ceqQueryRecordBo.setAssocbsnssData(webQueryBo.getRelationBaseData());
			ceqQueryRecordBo.setCheckId(webQueryBo.getApproveId());
			ceqQueryRecordBo.setCheckWay(webQueryBo.getApproveWay());
			ceqQueryRecordBo.setRekOrg(webQueryBo.getApproveOrg());
			ceqQueryRecordBo.setRekTime(webQueryBo.getApproveTime());
			ceqQueryRecordBo.setRekUser(webQueryBo.getApproveUser());
			ceqQueryRecordBo.setMsgNo(webQueryBo.getMsgNo());
			ceqQueryRecordBo.setBatchFlag(webQueryBo.getBatchFlag());
			ceqQueryRecordBo.setErrorInfo(webQueryBo.getErrorInfo());
			ceqQueryRecordBo.setOperOrg(webQueryBo.getQueryOrg());
			ceqQueryRecordBo.setClientIp(webQueryBo.getClientIp());
			ceqQueryRecordBo.setReportVersion("2.0.0");
			ceqQueryRecordBo.setReportFormat(webQueryBo.getReportResultType().getCode());
			ceqQueryRecordBo.setResultType(webQueryBo.getReportResultType().getCode());
			String queryFormat = webQueryBo.getReportVersion();
			ceqQueryRecordBo.setQueryFormat(queryFormat);

		}
		return ceqQueryRecordBo;
	}

}
