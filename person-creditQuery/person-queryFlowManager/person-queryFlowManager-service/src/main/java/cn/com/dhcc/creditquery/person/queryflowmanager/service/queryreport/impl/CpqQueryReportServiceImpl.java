/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.service.queryreport.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeManagerService;
import cn.com.dhcc.creditquery.person.query.bo.queryCounter.CpqMonitorCounterBo;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqOrgAttrBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.ReportStructuredStorageBo;
import cn.com.dhcc.creditquery.person.query.bo.querypboc.InterfaceRequestBo;
import cn.com.dhcc.creditquery.person.query.bo.querypboc.QueryRespBo;
import cn.com.dhcc.creditquery.person.query.bo.querypboc.WebRequestBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqOrgAttrService;
import cn.com.dhcc.creditquery.person.querycontrol.service.CpqQueryControlValidateService;
import cn.com.dhcc.creditquery.person.querycontrol.service.greenchanne.CpqGreenChannelService;
import cn.com.dhcc.creditquery.person.querycounter.service.CpqCounterControlService;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqQueryRecordService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.queryreport.CpqQueryReportService;
import cn.com.dhcc.creditquery.person.querypboc.service.CpqQueryPbocService;
import cn.com.dhcc.creditquery.person.queryuserrouter.service.QueryUserRouterService;
import cn.com.dhcc.creditquery.person.queryuserrouter.service.bean.QueryPbocUser;
import cn.com.dhcc.creditquery.person.queryuserrouter.service.bean.RouterUser;
import cn.com.dhcc.creditquery.person.reportstructured.service.CpqReportanalysisService;
import cn.com.dhcc.informationplatform.amqp.client.RabbitMQClient;
import cn.com.dhcc.informationplatform.amqp.factory.RabbitMQFactory;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.info.StorageResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.ReportIdUtil;
import cn.com.dhcc.query.creditquerycommon.cache.bean.SystemOrg;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.queryapicommon.rabbitmq.RabbitMqUtil;

/**
 * 信用报告查询服务流程
 * 
 * @author sjk
 * @date 2019年2月26日
 */
@Service
public class CpqQueryReportServiceImpl implements CpqQueryReportService {

	private static final Logger log = LoggerFactory.getLogger(CpqQueryReportServiceImpl.class);

	private static final String LOCAL_NO_REPORT = "5";

	private static final String QUERY_SUCEESS = "1";

	/**
	 * 用户路由服务
	 */
	@Autowired
	private QueryUserRouterService queryUserRouterService;
	/**
	 * 中心查询服务
	 */
	@Autowired
	private CpqQueryPbocService cpqQueryPbocService;
	/**
	 * 文件存储服务
	 */
	@Autowired
	private FileStorageService fileStorageService;
	/**
	 * 查询流程控制
	 */
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	/**
	 * 查询记录服务
	 */
	@Autowired
	private CpqQueryRecordService cpqQueryRecordService;

	/**
	 * 计数器服务
	 */
	@Autowired
	private CpqCounterControlService cpqCounterControlService;
	/**
	 * 信用报告解析及转换服务
	 */
	@Autowired
	private CpqReportanalysisService cpqReportanalysisService;

	/**
	 * 授权档案服务
	 */
	@Autowired
	private CpqAuthorizeManagerService cpqAuthorizeManagerService;

	@Autowired
	private CpqGreenChannelService cpqGreenChannelServiceImpl;
	
	@Autowired
	private CpqOrgAttrService cpqOrgAttrService;
	
	@Autowired
	private CpqQueryControlValidateService queryControlValidateService;
	
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
	public CpqQueryReportFlowBo creditReportQuery(CpqReportQueryBo cpqReportQueryBo) {
		long startTime = System.currentTimeMillis();
		log.info("CpqQueryReportServiceImpl creditReportQuery start,cpqReportQueryBo={}", cpqReportQueryBo);
		QueryRespBo queryRespBo = null;
//		CpqQueryRecordBo cpqQueryRecordBo = null;
		Integer queryType = cpqReportQueryBo.getQueryType();
		/**
		 * queryType:
		 * 信用报告复用策略 1、若该值为负数,则查询该值绝对值内的本地报告，不查询征信中心; 2、若该值为0，则强制查询征信中心；
		 * 3、若该值为正数，则查询该值内的本地报告，本地无报告则查询征信中心 单位：天
		 */
		CpqQueryReportFlowBo cpqQueryReportFlowBo = null;
		if (queryType < 0) {
			// 查询本地
			long queryLocalReportStartTime = System.currentTimeMillis();
			cpqQueryReportFlowBo = queryLocalReport(cpqReportQueryBo, queryType);
			log.info("queryLocalReport Query time consuming = {}ms",System.currentTimeMillis() - queryLocalReportStartTime);
		} else if (queryType == 0) {
			// 查询人行
			long coreQueryPbocFlowStartTime = System.currentTimeMillis();
			cpqQueryReportFlowBo = coreQueryPbocFlow(cpqReportQueryBo, queryRespBo);
			log.info("coreQueryPbocFlow Query time consuming = {}ms",System.currentTimeMillis() - coreQueryPbocFlowStartTime);
		} else {
			// 查询该值内的本地报告，本地无报告则查询征信中心
			cpqQueryReportFlowBo = queryLocalReport(cpqReportQueryBo, queryType);
			String resCode = cpqQueryReportFlowBo.getResCode();
			if (!StringUtils.equals(resCode, Constants.QUERY_SUCCESSCODE)) {
				// 查询本地失败，查询人行
				cpqQueryReportFlowBo = coreQueryPbocFlow(cpqReportQueryBo, queryRespBo);
			}
		}
		// 查询完成，判断是否为特权用户。若为特权用户，则特权查询次数加一
		String operator = cpqReportQueryBo.getOperator();
		cpqGreenChannelServiceImpl.updatePrivilegeUserQueryNum(operator);
		return cpqQueryReportFlowBo;
	}

	/**
	 * 查询本地信用报告
	 * 
	 * @param cpqReportQueryBo
	 * @param queryType
	 * @return
	 */
	private CpqQueryReportFlowBo queryLocalReport(CpqReportQueryBo cpqReportQueryBo, Integer queryType) {
		CpqQueryReportFlowBo cpqQueryReportFlowBo = new CpqQueryReportFlowBo();
		cpqQueryReportFlowBo.setBoCommonField(cpqReportQueryBo);
		long startTime = System.currentTimeMillis();
		// 获取可查询的征信用户
		QueryPbocUser queryPbocUser = queryUserRouterService.routerPlatformUser(encapRouterUserRequest(cpqReportQueryBo));
		log.info("routerPlatformUser Query time consuming = {}ms",System.currentTimeMillis() - startTime);
		if (null == queryPbocUser) {
			cpqQueryReportFlowBo.setResCode(Constants.FAILURE_CODE);
			cpqQueryReportFlowBo.setResMsg(Constants.CCUSER_MAP_NOEXIST);
			return cpqQueryReportFlowBo;
		}
		String customerName = cpqReportQueryBo.getClientName();
		String certType = cpqReportQueryBo.getCertType();
		String certNo = cpqReportQueryBo.getCertNo();
		String qryReason = cpqReportQueryBo.getQueryReasonId();
		String topOrgCode = cpqReportQueryBo.getTopOrgCode();
		int abs = Math.abs(queryType);
		long getLocalReportStartTime = System.currentTimeMillis();
		CpqQueryReportFlowBo localReport = cpqQueryRecordService.getLocalReport(customerName, certType, certNo,
				qryReason, topOrgCode, abs + "");
		log.info("queryLocalReport  getLocalReport Query time consuming = {}ms",System.currentTimeMillis() - getLocalReportStartTime);
		if (localReport == null) {
			CpqQueryRecordBo queryBo = buildCpqQueryRecordBoByCpqReportQueryBo(cpqReportQueryBo, "1", queryPbocUser);
			cpqQueryReportFlowBo = processQueryResult(cpqQueryReportFlowBo, queryBo,queryType);
			cpqQueryReportFlowBo.setResCode(Constants.QUERY_LOCALNOTREPORTCODE);
			cpqQueryReportFlowBo.setResMsg(Constants.QUERY_LOCALNOTREPORTMSG);
			return cpqQueryReportFlowBo;
		}
		String resultType = getResultType(cpqReportQueryBo);
		long convertReportStartTime = System.currentTimeMillis();
		// 转换信用报告
		CpqQueryRecordBo reportQueryBo = convertReport(cpqReportQueryBo, cpqQueryReportFlowBo, queryPbocUser,
				localReport, resultType);
		log.info("queryLocalReport convertReport Query time consuming = {}ms",System.currentTimeMillis() - convertReportStartTime);
		long processQueryResultStartTime = System.currentTimeMillis();
		// 处理查询结果
		cpqQueryReportFlowBo = processQueryResult(cpqQueryReportFlowBo, reportQueryBo,queryType);
		log.info("queryLocalReport convertReport Query time consuming = {}ms",System.currentTimeMillis() - processQueryResultStartTime);
		log.info("queryLocalReport Query time consuming = {}ms",System.currentTimeMillis() - startTime);
		return cpqQueryReportFlowBo;
	}

	/**
	 * 本地查询结果的处理
	 * 
	 * @param cpqQueryReportFlowBo
	 * @param reportQueryBo
	 * @param queryType
	 * @return
	 */
	private CpqQueryReportFlowBo processQueryResult(CpqQueryReportFlowBo cpqQueryReportFlowBo,
			CpqQueryRecordBo reportQueryBo,Integer queryType) {
		String htmlReportPath = reportQueryBo.getHtmlPath();
		String xmlReportPath = reportQueryBo.getXmlPath();
		String jsonReportPath = reportQueryBo.getJsonPath();
		String pdfReportPath = reportQueryBo.getPdfPath();

		cpqQueryReportFlowBo.setHtmlReportPath(htmlReportPath);
		cpqQueryReportFlowBo.setXmlReportPath(xmlReportPath);
		cpqQueryReportFlowBo.setJsonReportPath(jsonReportPath);
		cpqQueryReportFlowBo.setPdfReportPath(pdfReportPath);

		boolean noReport = StringUtils.isBlank(htmlReportPath) && StringUtils.isBlank(xmlReportPath)
				&& StringUtils.isBlank(jsonReportPath) && StringUtils.isBlank(pdfReportPath);

		if (noReport) {
			reportQueryBo.setStatus(LOCAL_NO_REPORT);
			cpqQueryReportFlowBo.setResCode(Constants.QUERY_LOCALNOTREPORTCODE);
			cpqQueryReportFlowBo.setResMsg(Constants.QUERY_LOCALNOTREPORTMSG);
		} else {
			reportQueryBo.setStatus(QUERY_SUCEESS);
			cpqQueryReportFlowBo.setResCode(Constants.QUERY_SUCCESSCODE);
			cpqQueryReportFlowBo.setResMsg(Constants.QUERY_SUCCESSMSG);
		}

		if(queryType>0 && Constants.QUERY_LOCALNOTREPORTCODE.equals(cpqQueryReportFlowBo.getResCode())) {
			return cpqQueryReportFlowBo;
		}
		CpqQueryRecordBo cpqQueryRecordBo = cpqFlowManageService.create(reportQueryBo);
		String autharchiveId = cpqQueryRecordBo.getAutharchiveId();
		if (StringUtils.isNotBlank(autharchiveId)) {
			cpqAuthorizeManagerService.updateQueryNumByRedis(autharchiveId);
		}
		cpqQueryReportFlowBo.setQueryRecordId(cpqQueryRecordBo.getId());

		long updateCounterDataStartTime = System.currentTimeMillis();
		CpqMonitorCounterBo cpqMonitorCounterBo = buildMonitorCounterBo(cpqQueryReportFlowBo, reportQueryBo, "1");
		cpqCounterControlService.updateCounterData(cpqMonitorCounterBo);
		log.info("processQueryResult updateCounterData Query time consuming = {}ms",System.currentTimeMillis() - updateCounterDataStartTime);
		return cpqQueryReportFlowBo;
	}

	private String getResultType(CpqReportQueryBo cpqReportQueryBo) {
		// 根据结果类型返回相应的报告，若无相应格式的报告，则进行转换
		String accessSource = cpqReportQueryBo.getAccessSource();
		String resultType = "";
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			InterfaceQueryParams interfaceQueryParamsBo = cpqReportQueryBo.getInterfaceQueryParamsBo();
			resultType = interfaceQueryParamsBo.getReportType();
		} else {
			WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
			CpqReportResultType reportResultType = webQueryBo.getReportResultType();
			resultType = reportResultType.getCode();
		}
		return resultType;
	}

	/**
	 * 根据结果类型和已有的信用报告进行转换
	 * 
	 * @param cpqReportQueryBo
	 * @param cpqQueryReportFlowBo
	 * @param queryPbocUser
	 * @param localReport
	 * @param resultType
	 * @return
	 */
	private CpqQueryRecordBo convertReport(CpqReportQueryBo cpqReportQueryBo, CpqQueryReportFlowBo cpqQueryReportFlowBo,
			QueryPbocUser queryPbocUser, CpqQueryReportFlowBo localReport, String resultType) {
		String htmlReportPath = localReport.getHtmlReportPath();
		String xmlReportPath = localReport.getXmlReportPath();
		String jsonReportPath = localReport.getJsonReportPath();
		String pdfReportPath = localReport.getPdfReportPath();

		if (StringUtils.contains(resultType, "H")) {
			if (StringUtils.isNotBlank(htmlReportPath)) {
				String htmlCreditReport = localReport.getHtmlCreditReport();
				cpqQueryReportFlowBo.setHtmlCreditReport(htmlCreditReport);
			} else {
				// 将xml转换为html
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String htmlReport = cpqReportanalysisService.reportConvert(xmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_XML, CpqReportanalysisService.REPORT_FORMAT_HTML);
					cpqQueryReportFlowBo.setHtmlCreditReport(htmlReport);
					String fileType = "html";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(htmlReport, fileType, topOrgCode2);
					htmlReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "X")) {
			if (StringUtils.isNotBlank(xmlReportPath)) {
				String xmlCreditReport = localReport.getXmlCreditReport();
				cpqQueryReportFlowBo.setXmlCreditReport(xmlCreditReport);
			} else {
				// 将html转换为xml
				if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String xmlReport = cpqReportanalysisService.reportConvert(htmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_HTML, CpqReportanalysisService.REPORT_FORMAT_XML);
					cpqQueryReportFlowBo.setXmlCreditReport(xmlReport);
					String fileType = "xml";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(xmlReport, fileType, topOrgCode2);
					xmlReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "J")) {
			if (StringUtils.isNotBlank(jsonReportPath)) {
				String jsonCreditReport = localReport.getJsonCreditReport();
				cpqQueryReportFlowBo.setJsonCreditReport(jsonCreditReport);
			} else {
				// 将报告转换为json
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String jsonReport = cpqReportanalysisService.reportConvert(xmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_XML, CpqReportanalysisService.REPORT_FORMAT_JSON);
					cpqQueryReportFlowBo.setJsonCreditReport(jsonReport);
					String fileType = "json";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(jsonReport, fileType, topOrgCode2);
					jsonReportPath = response.getUri();
				} else if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String jsonReport = cpqReportanalysisService.reportConvert(htmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_HTML, CpqReportanalysisService.REPORT_FORMAT_JSON);
					cpqQueryReportFlowBo.setJsonCreditReport(jsonReport);
					String fileType = "json";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(jsonReport, fileType, topOrgCode2);
					jsonReportPath = response.getUri();
				}
			}
		}
		if (StringUtils.contains(resultType, "P")) {
			if (StringUtils.isNotBlank(pdfReportPath)) {
				String pdfCreditReport = localReport.getPdfCreditReport();
				cpqQueryReportFlowBo.setPdfCreditReport(pdfCreditReport);
			} else {
				// 将报告转换为pdf
				if (StringUtils.isNotBlank(xmlReportPath)) {
					String xmlCreditReport = localReport.getXmlCreditReport();
					String pdfReport = cpqReportanalysisService.reportConvert(xmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_XML, CpqReportanalysisService.REPORT_FORMAT_PDF);
					cpqQueryReportFlowBo.setPdfCreditReport(pdfReport);
					String fileType = "pdf";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(pdfReport, fileType, topOrgCode2);
					pdfReportPath = response.getUri();
				} else if (StringUtils.isNotBlank(htmlReportPath)) {
					String htmlCreditReport = localReport.getHtmlCreditReport();
					String pdfReport = cpqReportanalysisService.reportConvert(htmlCreditReport,
							CpqReportanalysisService.REPORT_FORMAT_HTML, CpqReportanalysisService.REPORT_FORMAT_PDF);
					cpqQueryReportFlowBo.setPdfCreditReport(pdfReport);
					String fileType = "pdf";
					String topOrgCode2 = cpqReportQueryBo.getTopOrgCode();
					StorageResponse response = saveReport(pdfReport, fileType, topOrgCode2);
					pdfReportPath = response.getUri();
				}
			}
		}

		CpqQueryRecordBo reportQueryBo = buildCpqQueryRecordBoByCpqReportQueryBo(cpqReportQueryBo, "1", queryPbocUser);
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
		String reportFilePath = ConfigUtil.getSystemWorkPath(topOrgCode2);
		if (StringUtils.isNotBlank(reportFilePath)) {
			request.setRootUri(reportFilePath);
		}
		request.setBussModelEN(Constants.BUSSMODELEN_FM);
		request.setSourceSystem(Constants.SOURCESYSTEM_QP);
		request.setCompression(true);
		request.setEncrypt(true);
		String systemStorageType = ConfigUtil.getSystemStorageType(topOrgCode2);
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
	 * @param cpqReportQueryBo
	 * @param queryRespBo
	 * @author Jerry.chen
	 * @date 2019年3月5日
	 */
	private CpqQueryReportFlowBo coreQueryPbocFlow(CpqReportQueryBo cpqReportQueryBo, QueryRespBo queryRespBo) {
		CpqQueryRecordBo cpqQueryRecordBo;
		CpqQueryReportFlowBo cpqQueryReportFlowBo = new CpqQueryReportFlowBo();
		cpqQueryReportFlowBo.setBoCommonField(cpqReportQueryBo);
		try {
			String topOrgCode = cpqReportQueryBo.getTopOrgCode();
			RouterUser routerUser = encapRouterUserRequest(cpqReportQueryBo);
			long routerUserStartTime = System.currentTimeMillis();
			// 获取可查询的征信用户
			QueryPbocUser queryPbocUser = queryUserRouterService.routerPlatformUser(routerUser);
			log.info("coreQueryPbocFlow routerPlatformUser Query time consuming = {}ms",System.currentTimeMillis() - routerUserStartTime);
			if (null == queryPbocUser) {
				cpqQueryReportFlowBo.setResCode(Constants.FAILURE_CODE);
				cpqQueryReportFlowBo.setResMsg(Constants.CCUSER_MAP_NOEXIST);
				return cpqQueryReportFlowBo;
			}
			String resultType = "";
			boolean isWEBQuery = true;
			// 通过系统参数判断查询web和接口的优先级， 1：接口 2：web 3：接口、web、
			String accessSource = cpqReportQueryBo.getAccessSource();
			String queryPbocStrategy = "";
			long getQueryPbocStrategyStartTime = System.currentTimeMillis();
			queryPbocStrategy = ConfigUtil.getQueryPbocStrategy(topOrgCode);
			log.info("coreQueryPbocFlow getQueryPbocStrategy Query time consuming = {}ms",System.currentTimeMillis() - getQueryPbocStrategyStartTime);
			if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
				InterfaceQueryParams interfaceQueryParamsBo = cpqReportQueryBo.getInterfaceQueryParamsBo();
				resultType = interfaceQueryParamsBo.getReportType();
			} else {
				WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
				CpqReportResultType reportResultType = webQueryBo.getReportResultType();
				resultType = reportResultType.getCode();
			}
			/*if (StringUtils.equals(accessSource, ACCESS_SOURCE_WEB)
					&& StringUtils.equals(queryPbocStrategy, Constants.CREDITREPORT_REQUEST_STRATEGY_INTERFACE)) {
				// 页面来源查询中心接口，暂时不支持。
				cpqQueryReportFlowBo.setResCode(Constants.QUERY_PBOC_STRATEGY_ERROR_CODE);
				cpqQueryReportFlowBo.setResMsg(Constants.QUERY_PBOC_STRATEGY_ERROR_MSG);
				return cpqQueryReportFlowBo;
			}*/
			if (queryPbocStrategy.equals(Constants.CREDITREPORT_REQUEST_STRATEGY_INTERFACE)) {// 接口请求
				InterfaceRequestBo integerQueryRequest = encapAPIQueryRequest(cpqReportQueryBo, queryPbocUser);
				long interfaceQueryStartTime = System.currentTimeMillis();
				queryRespBo = cpqQueryPbocService.interfaceQuery(integerQueryRequest);
				log.info("coreQueryPbocFlow interfaceQuery Query time consuming = {}ms",System.currentTimeMillis() - interfaceQueryStartTime);
				isWEBQuery = false;
			} else if (queryPbocStrategy.equals(Constants.CREDITREPORT_REQUEST_STRATEGY_WEB)) {// web请求
				WebRequestBo webQueryRequest = encapWebQueryRequest(cpqReportQueryBo, queryPbocUser);
				long webQueryStartTime = System.currentTimeMillis();
				queryRespBo = cpqQueryPbocService.webQuery(webQueryRequest);
				log.info("coreQueryPbocFlow webQuery Query time consuming = {}ms",System.currentTimeMillis() - webQueryStartTime);
			}
			// 构建查询记录对象
			long encapQueryResultToRecordStartTime = System.currentTimeMillis();
			cpqQueryRecordBo = encapQueryResultToRecord(queryRespBo, cpqReportQueryBo);
			log.info("coreQueryPbocFlow encapQueryResultToRecord Query time consuming = {}ms",System.currentTimeMillis() - encapQueryResultToRecordStartTime);
			String autharchiveId = cpqQueryRecordBo.getAutharchiveId();
			if (StringUtils.isNotBlank(autharchiveId)) {
				cpqAuthorizeManagerService.updateQueryNumByRedis(autharchiveId);
			}

			// 查询状态 0：查询异常 1： 查询成功 2： 查无此人 3： 查询失败 4:报告不完整
			String status = queryRespBo.getStatus();
			StorageResponse storageResponse = null;
			if (QUERY_PBOC_STATUS_SUCESS.equals(status)) {
				// 查询记录存储
				cpqQueryReportFlowBo.setResCode(Constants.QUERY_SUCCESSCODE);
				cpqQueryReportFlowBo.setResMsg(Constants.QUERY_SUCCESSMSG);

				// 查询回来的报告
				String sourceReport = queryRespBo.getReportMsg();
				// 存储查询回来的信用报告
				String fileType = isWEBQuery ? "html" : "xml";
				StorageRequest reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, fileType, sourceReport);
				// 报告存储
				long saveFileStartTime = System.currentTimeMillis();
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				log.info("coreQueryPbocFlow saveFile Query time consuming = {}ms",System.currentTimeMillis() - encapQueryResultToRecordStartTime);
				if (isWEBQuery) {
					String uri = storageResponse.getUri();
					cpqQueryRecordBo.setHtmlPath(uri);
					cpqQueryReportFlowBo.setHtmlCreditReport(sourceReport);
					cpqQueryReportFlowBo.setHtmlReportPath(uri);
				} else {
					String uri = storageResponse.getUri();
					cpqQueryRecordBo.setXmlPath(uri);
					cpqQueryReportFlowBo.setXmlCreditReport(sourceReport);
					cpqQueryReportFlowBo.setXmlReportPath(uri);
				}
				// 根据报告结果类型进行报告转换
				long convertReportToResultTypeStartTime = System.currentTimeMillis();
				convertReportToResultType(queryRespBo, cpqQueryReportFlowBo, cpqQueryRecordBo, resultType, isWEBQuery,
						sourceReport);
				log.info("coreQueryPbocFlow convertReportToResultType Query time consuming = {}ms",System.currentTimeMillis() - convertReportToResultTypeStartTime);
				long reportStructuredStorageStartTime = System.currentTimeMillis();
				// 结构化入库
				reportStructuredStorage(cpqReportQueryBo, cpqQueryReportFlowBo);
				log.info("coreQueryPbocFlow reportStructuredStorage Query time consuming = {}ms",System.currentTimeMillis() - reportStructuredStorageStartTime);
				//获取信用报告编号
				long getReportIdStartTime = System.currentTimeMillis();
				String reportId = getReportId(cpqQueryReportFlowBo, topOrgCode);
				log.info("coreQueryPbocFlow getReportId Query time consuming = {}ms",System.currentTimeMillis() - reportStructuredStorageStartTime);
				cpqQueryRecordBo.setReportId(reportId);
			}else {
				//其他情况，赋予从人行返回的结果代码
				cpqQueryReportFlowBo.setResCode(queryRespBo.getResCode());
				cpqQueryReportFlowBo.setResMsg(queryRespBo.getMsg());
			}
			//保存查询记录
			long createQueryRecordStartTime = System.currentTimeMillis();
			cpqQueryRecordBo = cpqFlowManageService.create(cpqQueryRecordBo);
			log.info("coreQueryPbocFlow cpqFlowManageService.create Query time consuming = {}ms",System.currentTimeMillis() - createQueryRecordStartTime);
			cpqQueryReportFlowBo.setQueryRecordId(cpqQueryRecordBo.getId());
			cpqQueryReportFlowBo.setUseTime(queryRespBo.getUseTime());
			cpqQueryReportFlowBo.setReportSource(cpqQueryRecordBo.getSource());
			// 构建计数器业务对象
			CpqMonitorCounterBo cpqMonitorCounterBo = buildMonitorCounterBo(cpqQueryReportFlowBo, cpqQueryRecordBo,
					"2");
			long updateCounterDataStartTime = System.currentTimeMillis();
			cpqCounterControlService.updateCounterData(cpqMonitorCounterBo);
			log.info("coreQueryPbocFlow updateCounterData Query time consuming = {}ms",System.currentTimeMillis() - updateCounterDataStartTime);

		} catch (Exception e) {
			log.error("CpqQueryReportServiceImpl creditReportQuery end...,error={}", e);
			cpqQueryReportFlowBo.setResCode(Constants.QUERY_EXCEPTIONCODE);
			cpqQueryReportFlowBo.setResMsg(Constants.QUERY_EXCEPTIONMSG);
		}
		return cpqQueryReportFlowBo;
	}

	/**
	 * 获取信用报告编号
	 * @param cpqQueryReportFlowBo
	 * @param topOrgCode
	 * @return
	 */
	private String getReportId(CpqQueryReportFlowBo cpqQueryReportFlowBo, String topOrgCode) {
		// 如果存在xml信用报告，则直接从xml中获取信用报告编号；若不存在，则通过html进行获取
		String xmlCreditReport = cpqQueryReportFlowBo.getXmlCreditReport();
		String reportId = "";
		if (StringUtils.isNotBlank(xmlCreditReport)) {
			reportId = ReportIdUtil.getReportIdFromXml(topOrgCode, xmlCreditReport, ReportIdUtil.PERSON_BUSSLINE);
		}else {
			String htmlCreditReport = cpqQueryReportFlowBo.getHtmlCreditReport();
			if (StringUtils.isNotBlank(htmlCreditReport)) {
				reportId = ReportIdUtil.getReportIdFromHtml(htmlCreditReport,ReportIdUtil.PERSON_BUSSLINE);
			}
		}
		return reportId;
	}

	private CpqMonitorCounterBo buildMonitorCounterBo(CpqQueryReportFlowBo cpqQueryReportFlowBo,
			CpqQueryRecordBo cpqQueryRecordBo, String source) {
		CpqMonitorCounterBo cpqMonitorCounterBo = new CpqMonitorCounterBo();
		cpqMonitorCounterBo.setBoCommonField(cpqQueryReportFlowBo);
		cpqMonitorCounterBo.setCreditUser(cpqQueryRecordBo.getCreditUser());
		cpqMonitorCounterBo.setDeptCode(cpqQueryRecordBo.getOperOrg());
		cpqMonitorCounterBo.setNum(1);
		cpqMonitorCounterBo.setSource(source);
		Integer statusInt = Integer.valueOf(cpqQueryRecordBo.getStatus());
		cpqMonitorCounterBo.setStatus(statusInt);
		cpqMonitorCounterBo.setUserName(cpqQueryRecordBo.getOperator());
		cpqMonitorCounterBo.setBatchFlag(false);
		return cpqMonitorCounterBo;
	}

	private void reportStructuredStorage(CpqReportQueryBo cpqReportQueryBo, CpqQueryReportFlowBo cpqQueryReportFlowBo) {
		String topOrgCode = cpqReportQueryBo.getTopOrgCode();
		log.debug("get isStructuredStorage flag param topOrgCode = ", topOrgCode);
		boolean isStructuredStorage = ConfigUtil.isStructuredStorage(topOrgCode);
		log.debug("get isStructuredStorage flag  = ", isStructuredStorage);
		if (isStructuredStorage) {
			String xmlReportPath = cpqQueryReportFlowBo.getXmlReportPath();
			String htmlReportPath = cpqQueryReportFlowBo.getHtmlReportPath();
			log.debug("StructuredStorage xmlReportPath  = ", xmlReportPath);

			ReportStructuredStorageBo structuredStorage = ReportStructuredStorageBo
					.buildReportStructuredStorage(xmlReportPath, htmlReportPath);
			if (null != structuredStorage) {
				// 向MQ中发送消息
				RabbitMQFactory rabbitMQFactory = RabbitMqUtil.getRabbitMQFactory();
				RabbitMQClient bindingqueue = rabbitMQFactory.topicExchangeBindingqueue(
						RabbitMqUtil.PSERSON_STRUCTURED_STORAGE_EXCHANGENAME,
						RabbitMqUtil.PSERSON_STRUCTURED_STORAGE_QUEUENAME,
						RabbitMqUtil.PSERSON_STRUCTURED_STORAGE_TOPIC_ROUTINGKEY);
				String message = JsonUtil.toJSONString(structuredStorage);
				bindingqueue.send(RabbitMqUtil.PSERSON_STRUCTURED_STORAGE_SEND_ROUTINGKEY, message);
				log.info("reportStructuredStorage send message to MQ message = ", message);
			}
		}
	}

	/**
	 * 将查得得信用报告根据resultType进行转换，并将报告存储路径等构建至查询记录中
	 * 
	 * @param queryRespBo
	 * @param cpqQueryReportFlowBo
	 * @param cpqQueryRecordBo
	 * @param resultType
	 * @param isWEBQuery
	 * @param sourceReport
	 */
	private void convertReportToResultType(QueryRespBo queryRespBo, CpqQueryReportFlowBo cpqQueryReportFlowBo,
			CpqQueryRecordBo cpqQueryRecordBo, String resultType, boolean isWEBQuery, String sourceReport) {
		StorageResponse storageResponse;
		StorageRequest reportSaveRequest;
		if (resultType.contains("H")) {
			String sourceType = isWEBQuery ? CpqReportanalysisService.REPORT_FORMAT_HTML
					: CpqReportanalysisService.REPORT_FORMAT_XML;
			long reportConvertStartTime = System.currentTimeMillis();
			String reportConvert = cpqReportanalysisService.reportConvert(sourceReport, sourceType,
					CpqReportanalysisService.REPORT_FORMAT_HTML);
			log.info("convertReportToResultType reportConvert To HTML sourceType = {}  Query time consuming = {}ms",sourceType, System.currentTimeMillis() - reportConvertStartTime);
			if (!StringUtils.equals(sourceType, CpqReportanalysisService.REPORT_FORMAT_HTML) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "html", reportConvert);
				// html报告存储
				long saveFileConvertHtmlReportStartTime = System.currentTimeMillis();
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				log.info("convertReportToResultType saveFileConvertHtmlReport Query time consuming = {}ms",sourceType, System.currentTimeMillis() - saveFileConvertHtmlReportStartTime);
				String uri = storageResponse.getUri();
				cpqQueryRecordBo.setHtmlPath(uri);
				cpqQueryReportFlowBo.setHtmlReportPath(uri);
				cpqQueryReportFlowBo.setHtmlCreditReport(reportConvert);
			}
		}
		if (resultType.contains("X")) {
			String sourceType = isWEBQuery ? CpqReportanalysisService.REPORT_FORMAT_HTML
					: CpqReportanalysisService.REPORT_FORMAT_XML;
			long reportConvertStartTime = System.currentTimeMillis();
			String reportConvert = cpqReportanalysisService.reportConvert(sourceReport, sourceType,
					CpqReportanalysisService.REPORT_FORMAT_XML);
			log.info("convertReportToResultType reportConvert To XML sourceType = {}  Query time consuming = {}ms",sourceType, System.currentTimeMillis() - reportConvertStartTime);
			if (!StringUtils.equals(sourceType, CpqReportanalysisService.REPORT_FORMAT_XML) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "xml", reportConvert);
				// xml报告存储
				long saveFileConvertXmlReportStartTime = System.currentTimeMillis();
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				log.info("convertReportToResultType saveFileConvertXmlReport Query time consuming = {}ms",sourceType, System.currentTimeMillis() - saveFileConvertXmlReportStartTime);
				String uri = storageResponse.getUri();
				cpqQueryRecordBo.setXmlPath(uri);
				cpqQueryReportFlowBo.setXmlReportPath(uri);
				cpqQueryReportFlowBo.setXmlCreditReport(reportConvert);
			}
		}
		if (resultType.contains("J")) {
			String sourceType = isWEBQuery ? CpqReportanalysisService.REPORT_FORMAT_HTML
					: CpqReportanalysisService.REPORT_FORMAT_XML;
			long reportConvertStartTime = System.currentTimeMillis();
			String reportConvert = cpqReportanalysisService.reportConvert(sourceReport, sourceType,
					CpqReportanalysisService.REPORT_FORMAT_JSON);
			log.info("convertReportToResultType reportConvert To JSON sourceType = {}  Query time consuming = {}ms",sourceType, System.currentTimeMillis() - reportConvertStartTime);
			if (!StringUtils.equals(sourceType, CpqReportanalysisService.REPORT_FORMAT_JSON) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "json", reportConvert);
				// json报告存储
				long saveFileConvertJsonReportStartTime = System.currentTimeMillis();
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				log.info("convertReportToResultType saveFileConvertJsonReport Query time consuming = {}ms",sourceType, System.currentTimeMillis() - saveFileConvertJsonReportStartTime);
				String uri = storageResponse.getUri();
				cpqQueryRecordBo.setJsonPath(uri);
				cpqQueryReportFlowBo.setJsonCreditReport(reportConvert);
				cpqQueryReportFlowBo.setJsonReportPath(uri);
			}
		}
		if (resultType.contains("P")) {
			String sourceType = isWEBQuery ? CpqReportanalysisService.REPORT_FORMAT_HTML
					: CpqReportanalysisService.REPORT_FORMAT_XML;
			long reportConvertStartTime = System.currentTimeMillis();
			String reportConvert = cpqReportanalysisService.reportConvert(sourceReport, sourceType,
					CpqReportanalysisService.REPORT_FORMAT_PDF);
			log.info("convertReportToResultType reportConvert To PDF sourceType = {}  Query time consuming = {}ms",sourceType, System.currentTimeMillis() - reportConvertStartTime);
			if (!StringUtils.equals(sourceType, CpqReportanalysisService.REPORT_FORMAT_PDF) && null != reportConvert) {
				reportSaveRequest = encapCreditReportSaveRequest(queryRespBo, "pdf", reportConvert);
				// pdf报告存储
				long saveFileConvertPDFReportStartTime = System.currentTimeMillis();
				storageResponse = fileStorageService.saveFile(reportSaveRequest);
				log.info("convertReportToResultType saveFileConvertPDFReport Query time consuming = {}ms",sourceType, System.currentTimeMillis() - saveFileConvertPDFReportStartTime);
				String uri = storageResponse.getUri();
				cpqQueryRecordBo.setPdfPath(uri);
				cpqQueryReportFlowBo.setPdfCreditReport(reportConvert);
				cpqQueryReportFlowBo.setPdfReportPath(uri);
			}
		}
	}

	/**
	 * 封装获取征信用户的路由请求信息
	 * 
	 * @param cpqReportQueryBo
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private RouterUser encapRouterUserRequest(CpqReportQueryBo cpqReportQueryBo) {
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest start...cpqReportQueryBo={}",cpqReportQueryBo);
		String queryUser = null;
		SystemOrg systemOrg = null;
		//获取业务属性查询员或者审批员参数
		String queryUserType = ConfigUtil.getQueryUser(cpqReportQueryBo.getTopOrgCode());
		// 查询原因是否需要进行审批
		boolean isReasonNeedApprove = queryControlValidateService.isReasonNeedApprove(cpqReportQueryBo.getQueryReasonId(), cpqReportQueryBo.getTopOrgCode());
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest queryUserType={},isReasonNeedApprove={}",queryUserType,isReasonNeedApprove);
		//查询员、接口不进行管控，默认查询员、查询原因是否需要审批
		if(queryUserType.equals("1") || cpqReportQueryBo.getAccessSource().equals("2") || !isReasonNeedApprove) {
			queryUser = cpqReportQueryBo.getOperator();
		}else {//审批员
			queryUser = cpqReportQueryBo.getWebQueryBo().getApproveUser();
		}
		systemOrg = UserUtilsForConfig.getOrgCache(queryUser);// 根据前置用户获取机构码
		log.info("CpqQueryReportServiceImpl encapRouterUserRequest systemOrg={}",systemOrg);
		RouterUser routerUser = new RouterUser(queryUser, systemOrg.getOrgId(), null);
		return routerUser;
	}

	/**
	 * 封装向征信中心服务请求的信息 web
	 * 
	 * @param cpqReportQueryBo
	 * @param queryPbocUser
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private WebRequestBo encapWebQueryRequest(CpqReportQueryBo cpqReportQueryBo, QueryPbocUser queryPbocUser) {
		WebRequestBo webRequestBo = new WebRequestBo();
		log.info("CpqQueryReportServiceImpl encapWebRequestBoRequest start,cpqReportQueryBo = {}", cpqReportQueryBo);
		/**
		 * 查询要素
		 */
		webRequestBo.setCertName(cpqReportQueryBo.getClientName());// 三项标识
		webRequestBo.setCertTypeID(cpqReportQueryBo.getCertType());
		webRequestBo.setCertNum(cpqReportQueryBo.getCertNo());
		webRequestBo.setQueryReasonID(cpqReportQueryBo.getQueryReasonId());// 查询原因

		String accessSource = cpqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			webRequestBo.setReportVersion(cpqReportQueryBo.getInterfaceQueryParamsBo().getQueryFormat());// 信用报告版本
			webRequestBo.setReportFormat(cpqReportQueryBo.getInterfaceQueryParamsBo().getReportType());// 信用报告封装格式
			/**
			 * 记录要素
			 */
			CpqReportQueryBo.InterfaceQueryParams interfaceQueryParams = cpqReportQueryBo.getInterfaceQueryParamsBo();
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
			webRequestBo.setReportVersion(cpqReportQueryBo.getWebQueryBo().getReportVersion());// 信用报告版本
			webRequestBo.setReportFormat(cpqReportQueryBo.getWebQueryBo().getReportResultType().getCode());// 信用报告封装格式
			/**
			 * 记录要素
			 */
			webRequestBo.setOperOrg(cpqReportQueryBo.getWebQueryBo().getQueryOrg());// 查询机构
			webRequestBo.setCheckId(cpqReportQueryBo.getWebQueryBo().getApproveId());// 审批请求ID
			webRequestBo.setCheckWay(cpqReportQueryBo.getWebQueryBo().getApproveWay());// 审批方式
			webRequestBo.setRekOrg(cpqReportQueryBo.getWebQueryBo().getApproveOrg());// 审批机构
			webRequestBo.setRekTime(cpqReportQueryBo.getWebQueryBo().getApproveTime());// 审批时间
			webRequestBo.setRekUser(cpqReportQueryBo.getWebQueryBo().getApproveUser());// 审批用户
			webRequestBo.setBatchFlag(cpqReportQueryBo.getWebQueryBo().getBatchFlag());// 批量标志
			webRequestBo.setMsgNo(cpqReportQueryBo.getWebQueryBo().getMsgNo());// 批量查询批次号
			webRequestBo.setAutharchiveId(cpqReportQueryBo.getWebQueryBo().getAuthArchiveId());// 关联档案ID
			webRequestBo.setAssocbsnssData(cpqReportQueryBo.getWebQueryBo().getRelationBaseData());// 相关联的业务数据
		}
		webRequestBo.setCreditUser(queryPbocUser.getCenterUserName());// 征信用户
		webRequestBo.setCreditPassWord(queryPbocUser.getCenterUserPWD());// 征信用户密码
		webRequestBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());// 征信用户金融机构代码
		/**
		 * 记录要素
		 */
		webRequestBo.setOperator(cpqReportQueryBo.getOperator());// 前置查询用户
		log.info("CpqQueryReportServiceImpl encapWebRequestBoRequest end,result = {}", webRequestBo);
		return webRequestBo;
	}

	/**
	 * 封装向征信中心服务请求的信息 api
	 * 
	 * @param cpqReportQueryBo
	 * @param queryPbocUser
	 * @return
	 * @author sjk
	 * @date 2019年2月27日
	 */
	private InterfaceRequestBo encapAPIQueryRequest(CpqReportQueryBo cpqReportQueryBo, QueryPbocUser queryPbocUser) {
		log.info("CpqQueryReportServiceImpl encapInterfaceRequestBoRequest start,cpqReportQueryBo = {}",
				cpqReportQueryBo);
		InterfaceRequestBo interfaceRequestBo = new InterfaceRequestBo();

		// ==============================用于查询的参数信息==================================================
		interfaceRequestBo.setOperator(cpqReportQueryBo.getOperator());// 前置查询用户
		interfaceRequestBo.setInstLoginName(queryPbocUser.getCenterUserName());// 征信用户
		interfaceRequestBo.setInstLoginPassword(queryPbocUser.getCenterUserPWD());// 征信用户密码
		interfaceRequestBo.setInstCode(queryPbocUser.getCenterUserOrgId());// 查询机构代码-征信用户金融机构代码
		interfaceRequestBo.setName(cpqReportQueryBo.getClientName());// 三项标识
		interfaceRequestBo.setIdType(cpqReportQueryBo.getCertType());
		interfaceRequestBo.setIdNum(cpqReportQueryBo.getCertNo());
		interfaceRequestBo.setQueryReason(cpqReportQueryBo.getQueryReasonId());// 查询原因
		interfaceRequestBo.setOperator(cpqReportQueryBo.getOperator());// 查询用户
		String serviceCode = ConfigUtil.getServiceCode(cpqReportQueryBo.getTopOrgCode());
		interfaceRequestBo.setServiceCode(serviceCode);
		SystemOrg systemOrg = UserUtilsForConfig.getOrgCache(cpqReportQueryBo.getOperator());// 根据前置用户获取机构码
		interfaceRequestBo.setOperOrg(systemOrg.getOrgId());// 机构
		/**
		 * 根据机构代码获取发起机构代码
		 */
		CpqOrgAttrBo orgAttrBo = cpqOrgAttrService.findByOrgId(systemOrg.getOrgId());
		interfaceRequestBo.setPbocOrgCode(orgAttrBo.getPbocOrgCode());
		if(StringUtils.isBlank(orgAttrBo.getPbocOrgCode())) {
			interfaceRequestBo.setPbocOrgCode(queryPbocUser.getCenterUserOrgId());
		}
		
		interfaceRequestBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(systemOrg.getOrgId()));

		// ==============================用于记录的参数信息==================================================
		interfaceRequestBo.setCertName(cpqReportQueryBo.getClientName());// 三项标识
		interfaceRequestBo.setCertTypeID(cpqReportQueryBo.getCertType());
		interfaceRequestBo.setCertNum(cpqReportQueryBo.getCertNo());
		interfaceRequestBo.setQueryReasonID(cpqReportQueryBo.getQueryReasonId());// 查询原因
		interfaceRequestBo.setCreditUser(queryPbocUser.getCenterUserName());// 征信用户
		interfaceRequestBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());// 征信用户金融机构代码
		String accessSource = cpqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			interfaceRequestBo.setReportVersion(cpqReportQueryBo.getInterfaceQueryParamsBo().getQueryFormat());// 信用报告版本
			interfaceRequestBo.setReportFormat(cpqReportQueryBo.getInterfaceQueryParamsBo().getReportType());// 信用报告封装格式
			CpqReportQueryBo.InterfaceQueryParams interfaceQueryParams = cpqReportQueryBo.getInterfaceQueryParamsBo();
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
			interfaceRequestBo.setReportVersion(cpqReportQueryBo.getWebQueryBo().getReportVersion());// 信用报告版本
			interfaceRequestBo.setReportFormat(cpqReportQueryBo.getWebQueryBo().getReportResultType().getCode());// 信用报告封装格式
			interfaceRequestBo.setOperOrg(cpqReportQueryBo.getWebQueryBo().getQueryOrg());// 查询机构
			interfaceRequestBo.setCheckId(cpqReportQueryBo.getWebQueryBo().getApproveId());// 审批请求ID
			interfaceRequestBo.setCheckWay(cpqReportQueryBo.getWebQueryBo().getApproveWay());// 审批方式
			interfaceRequestBo.setRekOrg(cpqReportQueryBo.getWebQueryBo().getApproveOrg());// 审批机构
			interfaceRequestBo.setRekTime(cpqReportQueryBo.getWebQueryBo().getApproveTime());// 审批时间
			interfaceRequestBo.setRekUser(cpqReportQueryBo.getWebQueryBo().getApproveUser());// 审批用户
			interfaceRequestBo.setBatchFlag(cpqReportQueryBo.getWebQueryBo().getBatchFlag());// 批量标志
			interfaceRequestBo.setMsgNo(cpqReportQueryBo.getWebQueryBo().getMsgNo());// 批量查询批次号
			interfaceRequestBo.setAutharchiveId(cpqReportQueryBo.getWebQueryBo().getAuthArchiveId());// 关联档案ID
			interfaceRequestBo.setAssocbsnssData(cpqReportQueryBo.getWebQueryBo().getRelationBaseData());// 相关联的业务数据
		}
		log.info("CpqQueryReportServiceImpl encapInterfaceRequestBoRequest end,interfaceRequestBo = {}",
				interfaceRequestBo);
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
		String reportFilePath = ConfigUtil.getSystemWorkPath(rootDeptCode);
		if (StringUtils.isNotBlank(reportFilePath)) {
			request.setRootUri(reportFilePath);
		}
		request.setBussModelEN(Constants.BUSSMODELEN_FM);
		request.setSourceSystem(Constants.SOURCESYSTEM_QP);
		request.setCompression(true);
		request.setEncrypt(true);
		String operOrg = queryRespBo.getOperOrg();
		String topOrg = UserUtilsForConfig.getRootDeptCode(operOrg);
		String systemStorageType = ConfigUtil.getSystemStorageType(topOrg);
		if (StringUtils.isNotBlank(systemStorageType)) {
			request.setStorageType(systemStorageType);
		}
		return request;
	}

	/**
	 * 封装查询结果到查询记录bo
	 * 
	 * @param queryRespBo
	 * @return
	 * @author DHC-S
	 * @date 2019年2月28日
	 */
	private CpqQueryRecordBo encapQueryResultToRecord(QueryRespBo queryRespBo, CpqReportQueryBo cpqReportQueryBo) {
		CpqQueryRecordBo cpqQueryRecordBo = new CpqQueryRecordBo();
		cpqQueryRecordBo.setCustomerName(queryRespBo.getCertName());
		cpqQueryRecordBo.setCertType(queryRespBo.getCertTypeID());
		cpqQueryRecordBo.setCertNo(queryRespBo.getCertNum());
		cpqQueryRecordBo.setQueryType("0");
		cpqQueryRecordBo.setQueryFormat(queryRespBo.getReportVersion());
		cpqQueryRecordBo.setResultType(queryRespBo.getReportFormat());
		cpqQueryRecordBo.setQueryReasonID(queryRespBo.getQueryReasonID());
		cpqQueryRecordBo.setQryReason(queryRespBo.getQueryReasonID());
		cpqQueryRecordBo.setReportVersion("2.0.0");
		cpqQueryRecordBo.setReportFormat(queryRespBo.getReportFormat());
		cpqQueryRecordBo.setCreditUser(queryRespBo.getCreditUser());
		cpqQueryRecordBo.setQueryOrg(queryRespBo.getQueryOrg());
		cpqQueryRecordBo.setOperator(queryRespBo.getOperator());
		cpqQueryRecordBo.setOperOrg(queryRespBo.getOperOrg());
		cpqQueryRecordBo.setQueryTime(new Date());
		cpqQueryRecordBo.setAssocbsnssData(queryRespBo.getAssocbsnssData());
		cpqQueryRecordBo.setAutharchiveId(queryRespBo.getAutharchiveId());
		cpqQueryRecordBo.setBatchFlag("1");
		cpqQueryRecordBo.setMsgNo(queryRespBo.getMsgNo());
		cpqQueryRecordBo.getQueryMode();
		cpqQueryRecordBo.setSource("2");
		cpqQueryRecordBo.setStatus(queryRespBo.getStatus());
		cpqQueryRecordBo.setUpdateTime(new Date());
		cpqQueryRecordBo.setCheckId(queryRespBo.getCheckId());
		cpqQueryRecordBo.setCheckWay(queryRespBo.getCheckWay());
		cpqQueryRecordBo.setRekOrg(queryRespBo.getRekOrg());
		cpqQueryRecordBo.setRekTime(queryRespBo.getRekTime());
		cpqQueryRecordBo.setRekUser(queryRespBo.getRekUser());
		if(StringUtils.isNotBlank(queryRespBo.getUseTime())) {
			cpqQueryRecordBo.setUseTime(Integer.valueOf(queryRespBo.getUseTime()));
		}
		String accessSource = cpqReportQueryBo.getAccessSource();
		if (StringUtils.equals(accessSource, ACCESS_SOURCE_INTERFACE)) {
			InterfaceQueryParams interfaceQueryParamsBo = cpqReportQueryBo.getInterfaceQueryParamsBo();
			cpqQueryRecordBo.setCallSysUser(cpqReportQueryBo.getInterfaceQueryParamsBo().getCallSysUser());
			cpqQueryRecordBo.setRecheckUserName(cpqReportQueryBo.getInterfaceQueryParamsBo().getRecheckUser());
			cpqQueryRecordBo.setClientIp(interfaceQueryParamsBo.getClientIp());
			cpqQueryRecordBo.setChannelId(interfaceQueryParamsBo.getBusinessLine());
			cpqQueryRecordBo.setCstmsysId(interfaceQueryParamsBo.getSysCode());
			cpqQueryRecordBo.setBatchFlag(interfaceQueryParamsBo.getBatchFlag());
			cpqQueryRecordBo.setMsgNo(interfaceQueryParamsBo.getMsgNo());
		} else {
			WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
			cpqQueryRecordBo.setClientIp(webQueryBo.getClientIp());
		}
		cpqQueryRecordBo.setResCode(queryRespBo.getCode());
		cpqQueryRecordBo.setResMsg(queryRespBo.getMsg());
		cpqQueryRecordBo.setErrorInfo(queryRespBo.getMsg());
		return cpqQueryRecordBo;
	}

	/**
	 * 使用查询请求信息业务对象{@link CpqReportQueryBo }构造一个查询记录业务对象{@link CpqQueryRecordBo }
	 * 
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqQueryRecordBo buildCpqQueryRecordBoByCpqReportQueryBo(CpqReportQueryBo cpqReportQueryBo,
			String reportSource, QueryPbocUser queryPbocUser) {
		CpqQueryRecordBo cpqQueryRecordBo = new CpqQueryRecordBo();
		cpqQueryRecordBo.setCustomerName(cpqReportQueryBo.getClientName());
		cpqQueryRecordBo.setCertType(cpqReportQueryBo.getCertType());
		cpqQueryRecordBo.setCertNo(cpqReportQueryBo.getCertNo());
		cpqQueryRecordBo.setOperator(cpqReportQueryBo.getOperator());
		cpqQueryRecordBo.setQryReason(cpqReportQueryBo.getQueryReasonId());
		cpqQueryRecordBo.setQueryReasonID(cpqReportQueryBo.getQueryReasonId());
		cpqQueryRecordBo.setSource(reportSource);
		cpqQueryRecordBo.setCreditUser(queryPbocUser.getCenterUserName());
		cpqQueryRecordBo.setQueryOrg(queryPbocUser.getCenterUserOrgId());
		cpqQueryRecordBo.setQueryType("0");
		cpqQueryRecordBo.setQueryTime(new Date());
		cpqQueryRecordBo.setUpdateTime(new Date());
		String accessSource = cpqReportQueryBo.getAccessSource();
		if (StringUtils.equals(ACCESS_SOURCE_INTERFACE, accessSource)) {
			InterfaceQueryParams interfaceQueryParamsBo = cpqReportQueryBo.getInterfaceQueryParamsBo();
			cpqQueryRecordBo.setBatchFlag(interfaceQueryParamsBo.getBatchFlag());
			cpqQueryRecordBo.setReportVersion(interfaceQueryParamsBo.getReportVersion());
			cpqQueryRecordBo.setChannelId(interfaceQueryParamsBo.getBusinessLine());
			cpqQueryRecordBo.setCstmsysId(interfaceQueryParamsBo.getSysCode());
			cpqQueryRecordBo.setClientIp(interfaceQueryParamsBo.getUserIp());
			cpqQueryRecordBo.setCallSysUser(interfaceQueryParamsBo.getCallSysUser());
			cpqQueryRecordBo.setRecheckUserName(interfaceQueryParamsBo.getRecheckUser());
			cpqQueryRecordBo.setAssocbsnssData(interfaceQueryParamsBo.getBussid());
			cpqQueryRecordBo.setQueryFormat(interfaceQueryParamsBo.getQueryFormat());
			cpqQueryRecordBo.setResultType(interfaceQueryParamsBo.getReportType());
			cpqQueryRecordBo.setAutharchiveId(interfaceQueryParamsBo.getAuthArchiveId());
			cpqQueryRecordBo.setAssocbsnssData(interfaceQueryParamsBo.getAuthorNum());
			cpqQueryRecordBo.setOperOrg(interfaceQueryParamsBo.getQueryOrg());
			cpqQueryRecordBo.setReportVersion("2.0.0");
			cpqQueryRecordBo.setReportFormat(interfaceQueryParamsBo.getReportType());
			cpqQueryRecordBo.setMsgNo(interfaceQueryParamsBo.getMsgNo());
		} else if (StringUtils.equals(ACCESS_SOURCE_WEB, accessSource)) {
			WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
			cpqQueryRecordBo.setAutharchiveId(webQueryBo.getAuthArchiveId());
			cpqQueryRecordBo.setAssocbsnssData(webQueryBo.getRelationBaseData());
			cpqQueryRecordBo.setCheckId(webQueryBo.getApproveId());
			cpqQueryRecordBo.setCheckWay(webQueryBo.getApproveWay());
			cpqQueryRecordBo.setRekOrg(webQueryBo.getApproveOrg());
			cpqQueryRecordBo.setRekTime(webQueryBo.getApproveTime());
			cpqQueryRecordBo.setRekUser(webQueryBo.getApproveUser());
			cpqQueryRecordBo.setMsgNo(webQueryBo.getMsgNo());
			cpqQueryRecordBo.setBatchFlag(webQueryBo.getBatchFlag());
			cpqQueryRecordBo.setErrorInfo(webQueryBo.getErrorInfo());
			cpqQueryRecordBo.setOperOrg(webQueryBo.getQueryOrg());
			cpqQueryRecordBo.setClientIp(webQueryBo.getClientIp());
			cpqQueryRecordBo.setReportVersion("2.0.0");
			cpqQueryRecordBo.setReportFormat(webQueryBo.getReportResultType().getCode());
			cpqQueryRecordBo.setResultType(webQueryBo.getReportResultType().getCode());
			String queryFormat = webQueryBo.getReportVersion();
			cpqQueryRecordBo.setQueryFormat(queryFormat);

		}
		return cpqQueryRecordBo;
	}

}
