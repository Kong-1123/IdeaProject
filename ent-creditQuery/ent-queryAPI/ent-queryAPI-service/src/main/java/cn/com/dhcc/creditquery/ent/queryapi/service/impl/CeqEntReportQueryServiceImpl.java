/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.credit.platform.util.ExecutorUtil;
import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeFileBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.ent.queryapi.common.EntConstant;
import cn.com.dhcc.creditquery.ent.queryapi.dao.CeqSingleQueryDao;
import cn.com.dhcc.creditquery.ent.queryapi.dao.CeqSingleResultDao;
import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleQuery;
import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleResult;
import cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryService;
import cn.com.dhcc.creditquery.ent.queryapi.task.QuerySaveTask;
import cn.com.dhcc.creditquery.ent.queryapi.util.GzipUtils;
import cn.com.dhcc.creditquery.ent.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.ent.queryapi.util.Validator;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorFileBo;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.QueryFlowMannerConstant;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;
import cn.com.dhcc.query.creditquerycommon.util.CeqConstants;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.HtmlInWatermarkStyle;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.queryapicommon.rules.ValidatorBean;
import cn.com.dhcc.query.queryapicommon.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Jerry.chen
 * @date 2019年2月15日
 */
@Slf4j
@Component
public class CeqEntReportQueryServiceImpl implements CeqEntReportQueryService {

	/**
	 * 返回报告类型html
	 */
	private static final String REPORTTYPE_HTML = "H";
	/**
	 * 返回报告类型xml
	 */
	private static final String REPORTTYPE_XML = "X";
	/**
	 * 返回报告类型pdf
	 */
	private static final String REPORTTYPE_PDF = "P";
	/**
	 * 返回报告类型json
	 */
	private static final String REPORTTYPE_JSON = "J";

	/**
	 * 分隔符
	 */
	private static final String BLANK = "|";

	private static final CharSequence LENGTHBIGGER = "长度";

	/**
	 * 审核数据状态--复核通过
	 */
	private static final String DATASTATUS_CHECKPASS = "3";
	/**
	 * 审核数据状态--复核拒绝
	 */
	private static final String DATASTATUS_CHECKREFUSE = "4";
	/**
	 * 文件保存类型 1-String
	 */
	public static final String FILE_RETURN_TYPE_STRING = "1";
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	@Autowired
	private CeqSingleResultDao ceqSingleResultDao;
	@Autowired
	private CeqSingleQueryDao ceqSingleQueryDao;
	@Autowired
	private FileStorageService fileStorageService;
	/**
	 * 信用报告版本---二代信用报告
	 */
	private static final String REPORTVERSION = "2.0.0";
	/**
	 * 请求访问来源-interface
	 */
	static final String ACCESS_SOURCE_INTERFACE = "2";
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	/**
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter_e";
	 /**
     * 批量文件名分隔符
     */
	protected static final String SPLITSTR = "_";
	/**
	 * 结果文件公共父目录
	 */
	protected static final String REPORTRESULT = "ReportResult";
	/**
	 * 	结果文件目录，企业标识。
	 */
	protected static final String PERSON_IDENTITY = "E";
	/**
	 * 结果文件名
	 */
	protected static final String RESULTFILENAME = "queryresult.txt";
	/**
	 * 报文文件行内字段分隔符
	 */
	protected static final String LINESPLIT = "|#";
	/**
	 * 每行结尾换行字符
	 */
	protected static final String LINEEND = "\r\n";
	/*
	 * (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryService#getCreditReport(cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo)
	 */
	@Override
	public String getCreditReport(SingleQueryBo queryBo) {
		String returnStr = null;
		log.info("getCreditReport begin, queryBo bussid= ", queryBo.getBussid());
		// 参数校验
		returnStr = fieldValidate(returnStr, queryBo);
		if (StringUtils.isNotBlank(returnStr)) {
			if(queryBo !=null && StringUtils.isNotBlank(queryBo.getMsgNo())) {
				String msgNo = queryBo.getMsgNo();
				log.info("msgNo{}",msgNo);
				redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
			}
			if (returnStr.contains(LENGTHBIGGER) || returnStr.contains(EntConstant.MSG_SYS_EXCEPTION)) {
				if (StringUtils.isNotBlank(queryBo.getMsgNo())) {
					writeTxtFile(queryBo, returnStr);
				}
				return returnStr;
			}
			// 请求参数存入cpqsinglequery表中
			QuerySaveTask task = new QuerySaveTask();
			task.setSingleQueryBo(queryBo);
			ExecutorUtil.executor.submit(task);
			return returnStr;
		}
		// 请求参数存入cpqsinglequery表中
		QuerySaveTask task = new QuerySaveTask();
		task.setSingleQueryBo(queryBo);
		ExecutorUtil.executor.submit(task);
		if (StringUtils.isBlank(queryBo.getMsgNo())) {//批量不需要ip校验
			// 合法性校验
			returnStr = ipValidate(returnStr, queryBo);
		}
		if (StringUtils.isNotBlank(returnStr)) {
			if(queryBo !=null && StringUtils.isNotBlank(queryBo.getMsgNo())) {
				String msgNo = queryBo.getMsgNo();
				log.info("msgNo{}",msgNo);
				redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
			}
			return returnStr;
		}

		CeqReportQueryBo ceqReportQueryBo = buildCeqReportQueryBo(queryBo);
		CeqQueryReportFlowBo interfaceQueryFlowManager = ceqFlowManageService.interfaceQueryFlowManager(ceqReportQueryBo);
		if (!Constants.QUERY_SUCCESSCODE.equals(interfaceQueryFlowManager.getResCode())) {
			if(Constants.QUERY_LOCALNOTREPORTCODE.equals(interfaceQueryFlowManager.getResCode())) {
				CeqSingleResult singleResult = new CeqSingleResult();
				singleResult.setReqId(queryBo.getReqId());
				singleResult.setReportVersion(REPORTVERSION);
				singleResult.setResCode(EntConstant.CODE_LOCAL_NO_REPORT);
				singleResult.setResMsg(EntConstant.MSG_LOCAL_NO_REPORT);
				singleResult.setCreditreportNo(interfaceQueryFlowManager.getQueryRecordId());
				singleResult.setReportSource(interfaceQueryFlowManager.getReportSource());
				singleResult.setUseTime(interfaceQueryFlowManager.getUseTime());
				singleResult.setMsgNo(queryBo.getMsgNo());
				ceqSingleResultDao.save(singleResult);
				returnStr = XmlUtil.Obj2Xml(singleResult);
			}else {
				if (QueryFlowMannerConstant.CODE_WAIT_QUERY.equals(interfaceQueryFlowManager.getResCode())) {
					returnStr = getErrBoStringSync(interfaceQueryFlowManager.getResCode(), interfaceQueryFlowManager.getResMsg(),
							queryBo.getReqId());
				}else {
					returnStr = getErrBoString(interfaceQueryFlowManager.getResCode(), interfaceQueryFlowManager.getResMsg(),
							queryBo.getReqId(),queryBo.getMsgNo());
				}
			}
		} else {
			CeqSingleResult singleResult = new CeqSingleResult();
			singleResult.setReqId(queryBo.getReqId());
			singleResult.setReportVersion(REPORTVERSION);
			singleResult.setResCode(EntConstant.CODE_SUSSCE);
			singleResult.setResMsg(EntConstant.MSG_SUSSCE);
			singleResult.setCreditreportNo(interfaceQueryFlowManager.getQueryRecordId());
			singleResult.setReportSource(interfaceQueryFlowManager.getReportSource());
			singleResult.setUseTime(interfaceQueryFlowManager.getUseTime());
			singleResult.setMsgNo(queryBo.getMsgNo());
			try {
				String html = interfaceQueryFlowManager.getHtmlCreditReport();
				//组装html，添加样式和水印
				html = HtmlInWatermarkStyle.ceqHtmlInWatermarkStyle(html, ceqReportQueryBo.getOperator(), 
						ceqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg(), CeqConstants.CEQ_WATERMARK_STYLE);
				
				interfaceQueryFlowManager.setHtmlCreditReport(html);
				changeReport(singleResult, queryBo.getReportType(), interfaceQueryFlowManager);
			} catch (IOException e) {
				log.error("changeReport(singleResult, queryBo.getReportType(), interfaceQueryFlowManager{})出现异常{}",
						interfaceQueryFlowManager, e);
				returnStr = getErrBoString(EntConstant.CODE_SYS_EXCEPTION, EntConstant.MSG_SYS_EXCEPTION,
						queryBo.getReqId(),queryBo.getMsgNo());
				return returnStr;
			}
			returnStr = XmlUtil.Obj2Xml(singleResult);
			saveReport(singleResult, queryBo.getReportType(), interfaceQueryFlowManager);
		}
		log.info("getCreditReport sucess ! queryBo bussid= ", queryBo.getBussid());
		return returnStr;
	}
	/**
	 * 批量请求校验出错，请求参数无法入库时直接写txt文件
	 * @param queryBo
	 * @param returnStr
	 * @author yuzhao.xue
	 * @date 2019年4月3日
	 */
	private void writeTxtFile(SingleQueryBo queryBo, String returnStr) {
		String[] split = StringUtils.splitByWholeSeparator(queryBo.getMsgNo(), SPLITSTR);
		String orgEntity = split[0];//法人实体
		String sysCode = split[1];//系统标识
		String queryDate = split[2];//查询日期
		String serialNum = split[3];//批量查询序号
		String batchQueryFilePath = CeqConfigUtil.getBatchQueryFilePath();
		String filePath = batchQueryFilePath + REPORTRESULT + File.separator + orgEntity + File.separator
				+ PERSON_IDENTITY + File.separator + queryDate.substring(0, 6) + File.separator + orgEntity+sysCode
				+ queryDate + serialNum + File.separator;
		File file = new File(filePath+RESULTFILENAME);
		FileOutputStream out=null;
		PrintStream ps=null;
		try {
			if(!file.exists()) {
				FileUtils.touch(file);
			}
			out = new FileOutputStream(file,true);
			ps = new PrintStream(out);
			ps.append(queryBo.getQuerierName());//被查询人姓名
		    ps.append(LINESPLIT);
		    ps.append(queryBo.getQuerierCertype());//被查询人证件类型
		    ps.append(LINESPLIT);
		    ps.append(queryBo.getQuerierCertno());//被查询人证件号码
		    ps.append(LINESPLIT);
		    ps.append(queryBo.getQueryReason());
		    ps.append(LINESPLIT);
		    CeqSingleResult ceqSingleResult = XmlUtil.parse4Xml(CeqSingleResult.class, returnStr);
		    ps.append(ceqSingleResult.getResCode());
		    ps.append(LINESPLIT);
		    ps.append(LINESPLIT);
		    ps.append(LINEEND);
		} catch (Exception e) {
			log.error("write Result error:{}",e);;
		}finally {
			if (ps !=null) {
				ps.close();
			}
		}
	}
	/**
	 * @param queryBo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年3月7日
	 */
	private CeqReportQueryBo buildCeqReportQueryBo(SingleQueryBo queryBo) {
		CeqReportQueryBo ceqReportQueryBo = new CeqReportQueryBo();
		
		switch (queryBo.getQuerierCertype()) {
		case "10"://中征码
			ceqReportQueryBo.setSignCode(queryBo.getQuerierCertno());
			break;
		case "20"://统一社会信用代码
			ceqReportQueryBo.setUniformSocialCredCode(queryBo.getQuerierCertno());
			break;
		case "30"://组织机构代码
			ceqReportQueryBo.setOrgInstCode(queryBo.getQuerierCertno().replace("-", ""));
			break;
		case "41"://纳税人识别号（国税）
			ceqReportQueryBo.setGsRegiNo(queryBo.getQuerierCertno());
			break;
		case "42"://纳税人识别号（地税）
			ceqReportQueryBo.setDsRegiNo(queryBo.getQuerierCertno());
			break;

		default:
			break;
		}
		ceqReportQueryBo.setEnterpriseName(queryBo.getQuerierName());
		ceqReportQueryBo.setOperator(queryBo.getQueryUser());
		ceqReportQueryBo.setQueryReasonId(queryBo.getQueryReason());
		ceqReportQueryBo.setQueryType(queryBo.getQueryType());
		String queryUserOrg = getQueryUserOrg(queryBo.getQueryUser());
		if (StringUtils.isNotBlank(queryUserOrg)) {
			ceqReportQueryBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(queryUserOrg));
		}
		ceqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
		InterfaceQueryParams interfaceQueryParams = ceqReportQueryBo.new InterfaceQueryParams();
		interfaceQueryParams.setBusinessLine(queryBo.getBusinessLine());
		interfaceQueryParams.setSysCode(queryBo.getSysCode());
		// InterfaceQueryParams.setClientIp(queryBo.getUserIp());//TODO 请求客户端IP
		// 取得什么ip待确认。
		interfaceQueryParams.setCallSysUser(queryBo.getCallSysUser());
		interfaceQueryParams.setRecheckUser(queryBo.getRecheckUser());
		interfaceQueryParams.setUserIp(queryBo.getUserIp());
		interfaceQueryParams.setUserMac(queryBo.getUserMac());
		interfaceQueryParams.setReqId(queryBo.getReqId());
		interfaceQueryParams.setSyncFlag(queryBo.getSyncFlag());
		interfaceQueryParams.setAsyncQueryFlag(queryBo.getAsyncQueryFlag());
		interfaceQueryParams.setQueryOrg(queryUserOrg);
		interfaceQueryParams.setReportType(queryBo.getReportType());
		interfaceQueryParams.setQueryFormat(queryBo.getQueryFormat());
		interfaceQueryParams.setReportVersion(queryBo.getReportVersion());
		interfaceQueryParams.setClientIp(queryBo.getUserIp());
		if(StringUtils.isBlank(queryBo.getMsgNo())) {
			interfaceQueryParams.setBatchFlag("1");
		}else {
			interfaceQueryParams.setBatchFlag("2");
			interfaceQueryParams.setMsgNo(queryBo.getMsgNo());
		}
		if (queryBo.getAuthorizedBo() != null) {
			interfaceQueryParams.setAuthorType(queryBo.getAuthorizedBo().getAuthorType());
			interfaceQueryParams.setAuthorNum(queryBo.getAuthorizedBo().getAuthorNum());
			interfaceQueryParams.setAuthorizedURL(queryBo.getAuthorizedBo().getAuthorizedURL());
			interfaceQueryParams.setAuthorBeginDate(queryBo.getAuthorizedBo().getAuthorBeginDate());
			interfaceQueryParams.setAuthorEndDate(queryBo.getAuthorizedBo().getAuthorEndDate());
		}
		ceqReportQueryBo.setInterfaceQueryParamsBo(interfaceQueryParams);
		List<CeqAuthorizeFileBo> fileBoList = new ArrayList<>();
		if (queryBo.getAuthorizedBo() != null) {
			List<AuthorFileBo> authorfile=null;
			if(queryBo.getAuthorizedBo().getAuthorizedFileBo() != null) {
				authorfile = queryBo.getAuthorizedBo().getAuthorizedFileBo().getAuthorfile();
			}
			if (CollectionUtils.isNotEmpty(authorfile)) {
				for (AuthorFileBo authorFileBo : authorfile) {
					
					CeqAuthorizeFileBo ceqArchivefileBo = new CeqAuthorizeFileBo();
					String fileContent = authorFileBo.getFileContent();
					if(StringUtils.isNotBlank(fileContent) || StringUtils.isNotBlank(authorFileBo.getUrl())) {
						StorageRequest storageRequest = new StorageRequest();
						try {
							byte[] gunzip = GzipUtils.gunzip(fileContent);
							if(null != gunzip && gunzip.length != 0) {
								storageRequest.setContentBytes(gunzip);
							}
						} catch (Exception e) {
							log.error("authorFileBo.getFileContent().getBytes(\"utf-8\")exception:{}",e);
						}
						String topOrgCode = ceqReportQueryBo.getTopOrgCode();
						String storageType = CeqConfigUtil.getSystemStorageType(topOrgCode);
						if(StringUtils.isNotBlank(storageType)) {
							storageRequest.setStorageType(storageType);
						}
						storageRequest.setFileType(StringUtils.trim(authorFileBo.getFileFormat()));
						storageRequest.setBussModelEN(Constants.BUSSMODELEN_QA);
						storageRequest.setSourceSystem(Constants.SOURCESYSTEM_QP);
						String systemWorkPath = CeqConfigUtil.getSystemWorkPath(topOrgCode);
						storageRequest.setRootUri(systemWorkPath);
						storageRequest.setUrl(authorFileBo.getUrl());
						ceqArchivefileBo.setStorageRequest(storageRequest);
					}
					ceqArchivefileBo.setFileType(authorFileBo.getFileType());
					ceqArchivefileBo.setCreateTime(SysDate.getSysTime());
					ceqArchivefileBo.setOperator(ceqReportQueryBo.getOperator());
					ceqArchivefileBo.setOperOrg(getQueryUserOrg(ceqReportQueryBo.getOperator()));
					ceqArchivefileBo.setOperTime(SysDate.getSysTime());
					ceqArchivefileBo.setFileFoamt(authorFileBo.getFileFormat());
					String url = authorFileBo.getUrl();
					if(StringUtils.isBlank(fileContent)&&StringUtils.isNotBlank(url)) {
						ceqArchivefileBo.setFileType(ARCHIVE_TYPE_EXTERNAL_IMAGE);
						ceqArchivefileBo.setImageSysUrl(url);
					}else {
						ceqArchivefileBo.setFileType(authorFileBo.getFileType());
						ceqArchivefileBo.setFilePath(url);
					}
					if(StringUtils.isNotBlank(fileContent) || StringUtils.isNotBlank(authorFileBo.getUrl())) {
						fileBoList.add(ceqArchivefileBo);
					}
				}
			}
			String authorNum = ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorNum();
			if (StringUtils.isNotBlank(authorNum)) {
				CeqAuthorizeFileBo ceqArchivefileBo = new CeqAuthorizeFileBo();
				//纸质文件不存储文件类型
//				cpqArchivefileBo.setFileType(ARCHIVE_TYPE_PAPER);
				ceqArchivefileBo.setCreateTime(SysDate.getSysTime());
				ceqArchivefileBo.setOperator(ceqReportQueryBo.getOperator());
				ceqArchivefileBo.setOperOrg(getQueryUserOrg(ceqReportQueryBo.getOperator()));
				ceqArchivefileBo.setOperTime(SysDate.getSysTime());
				ceqArchivefileBo.setPaperFileId(authorNum);
				fileBoList.add(ceqArchivefileBo);
			}
			String authorizedURL = ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorizedURL();
			if(StringUtils.isNotBlank(authorizedURL)) {
				CeqAuthorizeFileBo ceqArchivefileBo = new CeqAuthorizeFileBo();
				ceqArchivefileBo.setFileType(ARCHIVE_TYPE_EXTERNAL_IMAGE);
				ceqArchivefileBo.setCreateTime(SysDate.getSysTime());
				ceqArchivefileBo.setOperator(ceqReportQueryBo.getOperator());
				ceqArchivefileBo.setOperOrg(getQueryUserOrg(ceqReportQueryBo.getOperator()));
				ceqArchivefileBo.setOperTime(SysDate.getSysTime());
				ceqArchivefileBo.setImageSysUrl(authorizedURL);
				fileBoList.add(ceqArchivefileBo);
			}
		}
		interfaceQueryParams.setCeqArchivefileBoList(fileBoList);
		ceqReportQueryBo.setInterfaceQueryParamsBo(interfaceQueryParams);
		return ceqReportQueryBo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.com.dhcc.creditquery.person.queryapi.service.PersonCreditReportQuery#
	 * getSyncCreditReport(java.lang.String)
	 */
	@Override
	public String getSyncCreditReport(String reqID) {
		log.info("进入方法CreditPersonReportWebServiceImpl---getSyncCreditReport(reqID {})", reqID);
		String returnStr = null;
		try {
			CeqSingleQuery ceqSingleQuery = ceqSingleQueryDao.findByReqId(reqID);
			if(ceqSingleQuery==null) {
				returnStr = getErrBoStringSync(EntConstant.CODE_WAIT_QUERY,
						EntConstant.MSG_WAIT_QUERY, reqID);
			}
			CeqSingleResult ceqSingleResult = ceqSingleResultDao.findByReqId(reqID);
			log.debug("方法CreditPersonReportWebServiceImpl---getSyncCreditReport通过reqID查询出结果对象为：{}", ceqSingleResult);
			if (ceqSingleResult == null) {
				CeqApproveBo checkInfo = InitBeanUtil.getCeqApproveFlowService().findCeqApproveByReqId(reqID);
				if (null != checkInfo) {
					String status = checkInfo.getStatus();
					if (DATASTATUS_CHECKREFUSE.equals(status)) {
						returnStr = getErrBoStringSync(EntConstant.CODE_EXAMINE_RESUSE,
								EntConstant.MSG_EXAMINE_RESUSE, reqID);
					} else if (!DATASTATUS_CHECKPASS.equals(status)) {
						returnStr = getErrBoStringSync(EntConstant.CODE_WAIT_EXAMINE, EntConstant.MSG_WAIT_EXAMINE,
								reqID);
					} else {
						returnStr = getErrBoStringSync(EntConstant.CODE_WAIT_QUERY, EntConstant.MSG_WAIT_QUERY,
								reqID);
					}
				} else {
					returnStr = getErrBoStringSync(EntConstant.CODE_WAIT_QUERY, EntConstant.MSG_WAIT_QUERY, reqID);
				}
				return returnStr;
			}
			String queryUserOrg = getQueryUserOrg(ceqSingleQuery.getQueryUser());
			String rootDeptCode = UserUtilsForConfig.getRootDeptCode(queryUserOrg);
			String systemStorageType = CeqConfigUtil.getSystemStorageType(rootDeptCode);
			handleHtmlReport(ceqSingleResult,systemStorageType);
			handleXmlReport(ceqSingleResult,systemStorageType);
			handleJsonReport(ceqSingleResult,systemStorageType);
			handlePdfReport(ceqSingleResult,systemStorageType);
			returnStr = XmlUtil.Obj2Xml(ceqSingleResult);
		} catch (Exception e) {
			log.error("getSyncCreditReport异常:{}", e);
		}

		return returnStr;
	}

	/**
	 * 获取错误结果
	 * 
	 * @param resCode
	 * @param resMsg
	 * @param reqID
	 * @return
	 */
	@Transactional
	private String getErrBoString(String resCode, String resMsg, String reqID,String msgNo) {
		log.info("进入方法getErrBoString(String resCode, String resMsg, String {})", reqID);
		CeqSingleResult rv = new CeqSingleResult();
		rv.setResCode(resCode);
		rv.setResMsg(resMsg);
		rv.setReqId(reqID);
		rv.setMsgNo(msgNo);
		ceqSingleResultDao.save(rv);
		String obj2Xml = XmlUtil.Obj2Xml(rv);
		return obj2Xml;
	}
	/**
	 * 获取错误结果
	 * 
	 * @param resCode
	 * @param resMsg
	 * @param reqID
	 * @return
	 */
	private String getErrBoStringSync(String resCode, String resMsg, String reqID) {
		log.info("进入方法getErrBoString(String resCode, String resMsg, String {})", reqID);
		CeqSingleResult rv = new CeqSingleResult();
		rv.setResCode(resCode);
		rv.setResMsg(resMsg);
		rv.setReqId(reqID);
		String obj2Xml = XmlUtil.Obj2Xml(rv);
		return obj2Xml;
	}

	/**
	 * 数据合法性校验校验失败返回错误报文
	 * 
	 * @param validator
	 * @return
	 */
	private String getErrResult(Set<ConstraintViolation<Object>> validator, SingleQueryBo parse4Xml) {
		log.info("进入方法CreditPersonReportWebServiceImpl---getErrResult(SingleQueryBo {})", parse4Xml);
		StringBuffer sb = new StringBuffer();
		for (ConstraintViolation<Object> constraintViolation : validator) {
			sb.append(constraintViolation.getMessage());
			sb.append(BLANK);
		}
		String errBoString = getErrBoString(EntConstant.CODE_ERROR, sb.toString(), parse4Xml.getReqId(),parse4Xml.getMsgNo());
		return errBoString;
	}

	/**
	 * 合法性校验
	 * 
	 * @param regid
	 * @param returnStr
	 * @param singleQueryBo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年1月17日
	 */
	private String ipValidate(String returnStr, SingleQueryBo singleQueryBo) {
		log.info("进入方法CreditPersonReportWebServiceImpl---ipValidate(SingleQueryBo {})", singleQueryBo);
		try {
			String ip = singleQueryBo.getUserIp();
			if (StringUtils.isNotBlank(ip)) {
				List<String> ipList = InitBeanUtil.findIpBySysCode(singleQueryBo.getSysCode(),
						EntConstant.STOP_FLAG);
				log.debug("从ip配置表查询出ipList为{}", ipList);
				if (CollectionUtils.isEmpty(ipList)) {
					returnStr = getErrBoString(EntConstant.CODE_REFUSE, EntConstant.MSG_REFUSE,
							singleQueryBo.getReqId(),singleQueryBo.getMsgNo());
					return returnStr;
				}else {
					if (!Validator.verifyUserIp(singleQueryBo.getUserIp(), ipList)) {
						returnStr = getErrBoString(EntConstant.CODE_REFUSE, EntConstant.MSG_REFUSE,
								singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
						return returnStr;
					}
				}
				
			} else {
				returnStr = getErrBoString(EntConstant.CODE_REFUSE, EntConstant.MSG_REFUSE,
						singleQueryBo.getReqId(),singleQueryBo.getMsgNo());
				return returnStr;
			}
		} catch (Exception e) {
			log.error("合法性校验出现异常：{}", e);
			returnStr = getErrBoString(EntConstant.CODE_SYS_EXCEPTION, EntConstant.MSG_SYS_EXCEPTION,
					singleQueryBo.getReqId(),singleQueryBo.getMsgNo());
		}
		return returnStr;
	}

	/**
	 * 根据请求中的格式要求转换信用报告
	 * 
	 * @param singleResult
	 * @param reportType
	 * @param reportbo
	 * @author guoshihu
	 * @throws IOException
	 * @date 2019年1月22日
	 */
	public void changeReport(CeqSingleResult singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo)
			throws IOException {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		if (reportType.contains(REPORTTYPE_HTML)) {
			message = ceqQueryReportFlowBo.getHtmlCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setHtmlStr(zipMessage);
				singleResult.setHtmlMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_XML)) {
			message = ceqQueryReportFlowBo.getXmlCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setXmlStr(zipMessage);
				singleResult.setXmlMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_PDF)) {
			message = ceqQueryReportFlowBo.getPdfCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setPdfStr(zipMessage);
				singleResult.setPdfMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_JSON)) {
			message = ceqQueryReportFlowBo.getJsonCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setJsonStr(zipMessage);
				singleResult.setJsonMd5(md5Message);
			}
		}
	}

	/**
	 * 保存请求结果
	 * 
	 * @param singleResult
	 * @param reportType
	 * @param ceqQueryReportFlowBo
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
	@Transactional
	public void saveReport(CeqSingleResult singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo) {
		if (reportType.contains(REPORTTYPE_HTML)) {
			singleResult.setHtmlStr(ceqQueryReportFlowBo.getHtmlReportPath());
		}

		if (reportType.contains(REPORTTYPE_XML)) {
			singleResult.setXmlStr(ceqQueryReportFlowBo.getXmlReportPath());
		}

		if (reportType.contains(REPORTTYPE_PDF)) {
			singleResult.setPdfStr(ceqQueryReportFlowBo.getPdfReportPath());
		}

		if (reportType.contains(REPORTTYPE_JSON)) {
			singleResult.setJsonStr(ceqQueryReportFlowBo.getJsonReportPath());
		}
		ceqSingleResultDao.save(singleResult);
	}

	/**
	 * 参数校验
	 * 
	 * @param returnStr
	 * @param singleQueryBo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年1月17日
	 */
	private String fieldValidate(String returnStr, SingleQueryBo singleQueryBo) {
		log.info("进入方法：CreditPersonReportWebServiceImpl---fieldValidate(SingleQueryBo {})", singleQueryBo);
		try {
			Set<ConstraintViolation<Object>> validator = ValidatorBean.Validator(singleQueryBo);
			if (CollectionUtils.isNotEmpty(validator)) {
				returnStr = getErrResult(validator, singleQueryBo);
				return returnStr;
			}
		} catch (Exception e) {
			log.error("方法：CreditPersonReportWebServiceImpl---fieldValidate出现异常{}", e);
			returnStr = getErrBoString(EntConstant.CODE_SYS_EXCEPTION, EntConstant.MSG_SYS_EXCEPTION,
					singleQueryBo.getReqId(),singleQueryBo.getMsgNo());
		}
		return returnStr;
	}

	/**
	 * 处理html报文
	 * 
	 * @param ceqSingleResult
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handleHtmlReport(CeqSingleResult ceqSingleResult,String systemStorageType) throws IOException {
		log.info("进入方法：handleHtmlReport(CeqSingleResult {})", ceqSingleResult);
		String htmlStr = ceqSingleResult.getHtmlStr();
		log.debug("方法handleHtmlReport---htmlStr:{}", htmlStr);
		if (StringUtils.isNotBlank(htmlStr)) {
			String readFileToString = readFileContent(systemStorageType, htmlStr);
			log.debug("方法handleHtmlReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			ceqSingleResult.setHtmlStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleHtmlReport---md5String:{}", md5String);
			ceqSingleResult.setHtmlMd5(md5String);
		}
	}
	/**
	 * 读取报文文件内容
	 * @param systemStorageType
	 * @param htmlStr
	 * @author yuzhao.xue
	 * @date 2019年3月20日
	 */
	private String readFileContent(String systemStorageType, String htmlStr) {
		FileReadRequest readRequest = new FileReadRequest();
		readRequest.setUri(htmlStr);
		if (StringUtils.isNotBlank(systemStorageType)) {
			readRequest.setStorageType(systemStorageType);
		}
		readRequest.setResultType(FILE_RETURN_TYPE_STRING);
		readRequest.setZipFlag(true);
		readRequest.setDecrypt(true);
		FileReadResponse readFileContent = fileStorageService.readFileContent(readRequest);
		return readFileContent.getContent();
	}
	/**
	 * 处理xml报文
	 * 
	 * @param cpqSingleResult
	 * @param systemStorageType
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handleXmlReport(CeqSingleResult ceqSingleResult,String systemStorageType) throws IOException {
		log.info("进入方法handleXmlReport(CpqSingleResult {})", ceqSingleResult);
		String xmlStr = ceqSingleResult.getXmlStr();
		log.debug("方法handleXmlReport---xmlStr:{}", xmlStr);
		if (StringUtils.isNotBlank(xmlStr)) {
			String readFileToString = readFileContent(systemStorageType, xmlStr);
			log.debug("方法handleXmlReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			ceqSingleResult.setXmlStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleXmlReport---md5String:{}", md5String);
			ceqSingleResult.setXmlMd5(md5String);
		}
	}

	/**
	 * 处理json报文
	 * 
	 * @param ceqSingleResult
	 * @param systemStorageType
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handleJsonReport(CeqSingleResult ceqSingleResult,String systemStorageType) throws IOException {
		log.info("进入方法handleJsonReport(CeqSingleResult {})", ceqSingleResult);
		String jsonStr = ceqSingleResult.getJsonStr();
		log.debug("方法handleJsonReport---jsonStr:{}", jsonStr);
		if (StringUtils.isNotBlank(jsonStr)) {
			String readFileToString = readFileContent(systemStorageType, jsonStr);
			log.debug("方法handleJsonReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			ceqSingleResult.setJsonStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleJsonReport---md5String:{}", md5String);
			ceqSingleResult.setJsonMd5(md5String);
		}
	}

	/**
	 * 处理pdf报文
	 * 
	 * @param ceqSingleResult
	 * @param systemStorageType
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handlePdfReport(CeqSingleResult ceqSingleResult,String systemStorageType) throws IOException {
		log.info("进入方法handlePdfReport(CpqSingleResult {})", ceqSingleResult);
		String pdfStr = ceqSingleResult.getPdfStr();
		log.debug("方法handlePdfReport---jsonStr:{}", pdfStr);
		if (StringUtils.isNotBlank(pdfStr)) {
			String readFileToString = readFileContent(systemStorageType, pdfStr);
			log.debug("方法handlePdfReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			ceqSingleResult.setPdfStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handlePdfReport---md5String:{}", md5String);
			ceqSingleResult.setPdfMd5(md5String);
		}
	}

	/**
	 * 返回用户所属机构
	 *
	 * @param userName
	 * @return
	 * @author guoshihu
	 * @date 2019年1月16日
	 */
	private String getQueryUserOrg(String userName) {
		log.info("getQueryUserOrg userName={}", userName);
		String userDeptcode = "";
		try {
			SystemUser user = LoginValidateUtil.findUserByUserName(userName);
			if (user != null) {
				userDeptcode = user.getOrgId();
			}
		} catch (Exception e) {
			log.error("getQueryUserOrg error:", e);
		}
		log.info("getQueryUserOrg result={}", userDeptcode);
		return userDeptcode;
	}
	
}
