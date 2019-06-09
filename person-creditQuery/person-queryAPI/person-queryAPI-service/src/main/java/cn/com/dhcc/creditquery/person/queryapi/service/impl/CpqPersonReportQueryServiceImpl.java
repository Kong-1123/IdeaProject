/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.service.impl;

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
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqOrgAttrBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.person.queryapi.common.PersonConstant;
import cn.com.dhcc.creditquery.person.queryapi.dao.CpqSingleQueryDao;
import cn.com.dhcc.creditquery.person.queryapi.dao.CpqSingleResultDao;
import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleQuery;
import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleResult;
import cn.com.dhcc.creditquery.person.queryapi.service.CpqPersonReportQueryService;
import cn.com.dhcc.creditquery.person.queryapi.task.QuerySaveTask;
import cn.com.dhcc.creditquery.person.queryapi.util.GzipUtils;
import cn.com.dhcc.creditquery.person.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.person.queryapi.util.Validator;
import cn.com.dhcc.creditquery.person.querybo.queryapi.AuthorFileBo;
import cn.com.dhcc.creditquery.person.querybo.queryapi.SingleQueryBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqOrgAttrService;
import cn.com.dhcc.creditquery.person.queryflowmanager.QueryFlowMannerConstant;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;
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
public class CpqPersonReportQueryServiceImpl implements CpqPersonReportQueryService {

	/**
	 * 批量标识：批量
	 */
	private static final String BATCHEFLAG = "2";

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
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter";
	/**
	 * 批量文件名分隔符
	 */
	protected static final String SPLITSTR = "_";
	/**
	 * 结果文件公共父目录
	 */
	protected static final String REPORTRESULT = "ReportResult";
	/**
	 * 每行结尾换行字符
	 */
	protected static final String LINEEND = "\r\n";
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	@Autowired
	private CpqSingleResultDao cpqSingleResultDao;
	@Autowired
	private CpqSingleQueryDao cpqSingleQueryDao;
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
	/**
	 * 文件保存类型 1-String
	 */
	public static final String FILE_RETURN_TYPE_STRING = "1";

	/**
	 * 结果文件目录，个人标识。
	 */
	protected static final String PERSON_IDENTITY = "p";
	/**
	 * 报文文件行内字段分隔符
	 */
	protected static final String LINESPLIT = "|#";
	/**
	 * 结果文件名
	 */
	protected static final String RESULTFILENAME = "queryresult.txt";

	@Autowired
	private CpqOrgAttrService cpqOrgAttrService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.com.dhcc.creditquery.person.queryapi.service.CpqPersonReportQueryService#
	 * getCreditReport(cn.com.dhcc.creditquery.person.querybo.queryapi.
	 * SingleQueryBo)
	 */
	@Override
	public String getCreditReport(SingleQueryBo queryBo) {
		String returnStr = null;
		log.info("getCreditReport begin, queryBo bussid= ", queryBo.getBussid());
		// 参数校验
		returnStr = fieldValidate(returnStr, queryBo);
		if (StringUtils.isNotBlank(returnStr)) {
			if (queryBo != null && StringUtils.isNotBlank(queryBo.getMsgNo())) {
				String msgNo = queryBo.getMsgNo();
				log.info("msgNo{}", msgNo);
				redisson.getAtomicLong(msgNo + COUNTERSUFFIX).incrementAndGet();
			}
			if (returnStr.contains(LENGTHBIGGER) || returnStr.contains(PersonConstant.MSG_SYS_EXCEPTION)) {
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
		if (StringUtils.isBlank(queryBo.getMsgNo())) {// 批量不需要做ip校验
			// 合法性校验
			returnStr = ipValidate(returnStr, queryBo);
		}
		if (StringUtils.isNotBlank(returnStr)) {
			if (queryBo != null && StringUtils.isNotBlank(queryBo.getMsgNo())) {
				String msgNo = queryBo.getMsgNo();
				log.info("msgNo{}", msgNo);
				redisson.getAtomicLong(msgNo + COUNTERSUFFIX).incrementAndGet();
			}
			return returnStr;
		}

		CpqReportQueryBo cpqReportQueryBo = buildCpqReportQueryBo(queryBo);
		CpqQueryReportFlowBo interfaceQueryFlowManager = cpqFlowManageService
				.interfaceQueryFlowManager(cpqReportQueryBo);
		if (!Constants.QUERY_SUCCESSCODE.equals(interfaceQueryFlowManager.getResCode())) {
			if (Constants.QUERY_LOCALNOTREPORTCODE.equals(interfaceQueryFlowManager.getResCode())) {
				CpqSingleResult singleResult = new CpqSingleResult();
				singleResult.setReqId(queryBo.getReqId());
				singleResult.setReportVersion(REPORTVERSION);
				singleResult.setResCode(PersonConstant.CODE_LOCAL_NO_REPORT);
				singleResult.setResMsg(PersonConstant.MSG_LOCAL_NO_REPORT);
				singleResult.setCreditreportNo(interfaceQueryFlowManager.getQueryRecordId());
				singleResult.setReportSource(interfaceQueryFlowManager.getReportSource());
				singleResult.setUseTime(interfaceQueryFlowManager.getUseTime());
				singleResult.setMsgNo(queryBo.getMsgNo());
				cpqSingleResultDao.save(singleResult);
				returnStr = XmlUtil.Obj2Xml(singleResult);
			} else {
				if (QueryFlowMannerConstant.CODE_WAIT_QUERY.equals(interfaceQueryFlowManager.getResCode())) {
					returnStr = getErrBoStringSync(interfaceQueryFlowManager.getResCode(),
							interfaceQueryFlowManager.getResMsg(), queryBo.getReqId());
				} else {
					returnStr = getErrBoString(interfaceQueryFlowManager.getResCode(),
							interfaceQueryFlowManager.getResMsg(), queryBo.getReqId(), queryBo.getMsgNo());
				}
			}
		} else {
			CpqSingleResult singleResult = new CpqSingleResult();
			singleResult.setReqId(queryBo.getReqId());
			singleResult.setReportVersion(REPORTVERSION);
			singleResult.setResCode(PersonConstant.CODE_SUSSCE);
			singleResult.setResMsg(PersonConstant.MSG_SUSSCE);
			singleResult.setCreditreportNo(interfaceQueryFlowManager.getQueryRecordId());
			singleResult.setReportSource(interfaceQueryFlowManager.getReportSource());
			singleResult.setUseTime(interfaceQueryFlowManager.getUseTime());
			singleResult.setMsgNo(queryBo.getMsgNo());
			try {
				String html = interfaceQueryFlowManager.getHtmlCreditReport();
				//组装html，添加样式和水印
				html = HtmlInWatermarkStyle.cpqHtmlInWatermarkStyle(html, cpqReportQueryBo.getOperator(),
						cpqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg(), Constants.CPQ_WATERMARK_STYLE);
				
				interfaceQueryFlowManager.setHtmlCreditReport(html);
				changeReport(singleResult, queryBo.getReportType(), interfaceQueryFlowManager);
			} catch (IOException e) {
				log.error("changeReport(singleResult, queryBo.getReportType(), interfaceQueryFlowManager{})出现异常{}",
						interfaceQueryFlowManager, e);
				returnStr = getErrBoString(PersonConstant.CODE_SYS_EXCEPTION, PersonConstant.MSG_SYS_EXCEPTION,
						queryBo.getReqId(), queryBo.getMsgNo());
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
	 * 
	 * @param queryBo
	 * @param returnStr
	 * @author yuzhao.xue
	 * @date 2019年4月3日
	 */
	private void writeTxtFile(SingleQueryBo queryBo, String returnStr) {
		String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(queryBo.getMsgNo(), SPLITSTR);
		String orgEntity = split[0];// 法人实体
		String sysCode = split[1];// 系统标识
		String queryDate = split[2];// 查询日期
		String serialNum = split[3];// 批量查询序号
		String batchQueryFilePath = ConfigUtil.getBatchQueryFilePath();
		String filePath = batchQueryFilePath + REPORTRESULT + File.separator + orgEntity + File.separator
				+ PERSON_IDENTITY + File.separator + queryDate.substring(0, 6) + File.separator + orgEntity + sysCode
				+ queryDate + serialNum + File.separator;
		File file = new File(filePath + RESULTFILENAME);
		FileOutputStream out = null;
		PrintStream ps = null;
		try {
			if (!file.exists()) {
				FileUtils.touch(file);
			}
			out = new FileOutputStream(file, true);
			ps = new PrintStream(out);
			ps.append(queryBo.getQuerierName());// 被查询人姓名
			ps.append(LINESPLIT);
			ps.append(queryBo.getQuerierCertype());// 被查询人证件类型
			ps.append(LINESPLIT);
			ps.append(queryBo.getQuerierCertno());// 被查询人证件号码
			ps.append(LINESPLIT);
			ps.append(queryBo.getQueryReason());
			ps.append(LINESPLIT);
			CpqSingleResult cpqSingleResult = XmlUtil.parse4Xml(CpqSingleResult.class, returnStr);
			ps.append(cpqSingleResult.getResCode());
			ps.append(LINESPLIT);
			ps.append(LINESPLIT);
			ps.append(LINEEND);
		} catch (Exception e) {
			log.error("write Result error:{}", e);
		} finally {
			if (ps != null) {
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
	private CpqReportQueryBo buildCpqReportQueryBo(SingleQueryBo queryBo) {
		CpqReportQueryBo cpqReportQueryBo = new CpqReportQueryBo();
		cpqReportQueryBo.setCertNo(queryBo.getQuerierCertno());
		cpqReportQueryBo.setCertType(queryBo.getQuerierCertype());
		cpqReportQueryBo.setClientName(queryBo.getQuerierName());
		cpqReportQueryBo.setOperator(queryBo.getQueryUser());
		cpqReportQueryBo.setQueryReasonId(queryBo.getQueryReason());
		cpqReportQueryBo.setQueryType(queryBo.getQueryType());
		cpqReportQueryBo.setCallSysOrg(queryBo.getCallSysOrg());
		String queryUserOrg = getQueryUserOrg(queryBo.getQueryUser());
		if (StringUtils.isNotBlank(queryUserOrg)) {
			cpqReportQueryBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(queryUserOrg));
		}
		cpqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
		InterfaceQueryParams interfaceQueryParams = cpqReportQueryBo.new InterfaceQueryParams();
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
		if (StringUtils.isBlank(queryBo.getMsgNo())) {
			interfaceQueryParams.setBatchFlag("1");
		} else {
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
		cpqReportQueryBo.setInterfaceQueryParamsBo(interfaceQueryParams);
		List<CpqArchivefileBo> fileBoList = new ArrayList<>();
		if (queryBo.getAuthorizedBo() != null) {
			List<AuthorFileBo> authorfile = null;
			if (queryBo.getAuthorizedBo().getAuthorizedFileBo() != null) {
				authorfile = queryBo.getAuthorizedBo().getAuthorizedFileBo().getAuthorfile();
			}
			if (CollectionUtils.isNotEmpty(authorfile)) {
				for (AuthorFileBo authorFileBo : authorfile) {
					CpqArchivefileBo cpqArchivefileBo = new CpqArchivefileBo();
					String fileContent = authorFileBo.getFileContent();
					if (StringUtils.isNotBlank(fileContent) || StringUtils.isNotBlank(authorFileBo.getUrl())) {
						StorageRequest storageRequest = new StorageRequest();
						try {
							byte[] gunzip = GzipUtils.gunzip(fileContent);
							if (null != gunzip && gunzip.length != 0) {
								storageRequest.setContentBytes(gunzip);
							}
						} catch (Exception e) {
							log.error("authorFileBo.getFileContent().getBytes(\"utf-8\")exception:{}", e);
						}
						String topOrgCode = cpqReportQueryBo.getTopOrgCode();
						String storageType = ConfigUtil.getSystemStorageType(topOrgCode);
						if (StringUtils.isNotBlank(storageType)) {
							storageRequest.setStorageType(storageType);
						}
						storageRequest.setFileType(StringUtils.trim(authorFileBo.getFileFormat()));
						storageRequest.setBussModelEN(Constants.BUSSMODELEN_QA);
						storageRequest.setSourceSystem(Constants.SOURCESYSTEM_QP);
						String systemWorkPath = ConfigUtil.getSystemWorkPath(topOrgCode);
						storageRequest.setRootUri(systemWorkPath);
						storageRequest.setUrl(authorFileBo.getUrl());
						cpqArchivefileBo.setStorageRequest(storageRequest);
					}
					cpqArchivefileBo.setFileType(authorFileBo.getFileType());
					cpqArchivefileBo.setCreatetime(SysDate.getSysTime());
					cpqArchivefileBo.setOperator(cpqReportQueryBo.getOperator());
					cpqArchivefileBo.setOperorg(getQueryUserOrg(cpqReportQueryBo.getOperator()));
					cpqArchivefileBo.setOpertime(SysDate.getSysTime());
					// cpqArchivefileBo.setPaperfileid(cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorNum());
					String url = authorFileBo.getUrl();
					if (StringUtils.isBlank(fileContent) && StringUtils.isNotBlank(url)) {
						cpqArchivefileBo.setFileType(ARCHIVE_TYPE_EXTERNAL_IMAGE);
						cpqArchivefileBo.setImageSysUrl(url);
					} else {
						cpqArchivefileBo.setFileType(authorFileBo.getFileType());
						cpqArchivefileBo.setFilepath(url);
					}
					cpqArchivefileBo.setFileFoamt(authorFileBo.getFileFormat());
					if (StringUtils.isNotBlank(fileContent) || StringUtils.isNotBlank(authorFileBo.getUrl())) {
						fileBoList.add(cpqArchivefileBo);
					}
				}
			}
			String authorNum = cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorNum();
			if (StringUtils.isNotBlank(authorNum)) {
				CpqArchivefileBo cpqArchivefileBo = new CpqArchivefileBo();
				// 纸质文件不存储文件类型
				// cpqArchivefileBo.setFileType(ARCHIVE_TYPE_PAPER);
				cpqArchivefileBo.setCreatetime(SysDate.getSysTime());
				cpqArchivefileBo.setOperator(cpqReportQueryBo.getOperator());
				cpqArchivefileBo.setOperorg(getQueryUserOrg(cpqReportQueryBo.getOperator()));
				cpqArchivefileBo.setOpertime(SysDate.getSysTime());
				cpqArchivefileBo.setPaperfileid(authorNum);
				fileBoList.add(cpqArchivefileBo);
			}
			String authorizedURL = cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorizedURL();
			if (StringUtils.isNotBlank(authorizedURL)) {
				CpqArchivefileBo cpqArchivefileBo = new CpqArchivefileBo();
				cpqArchivefileBo.setFileType(ARCHIVE_TYPE_EXTERNAL_IMAGE);
				cpqArchivefileBo.setCreatetime(SysDate.getSysTime());
				cpqArchivefileBo.setOperator(cpqReportQueryBo.getOperator());
				cpqArchivefileBo.setOperorg(getQueryUserOrg(cpqReportQueryBo.getOperator()));
				cpqArchivefileBo.setOpertime(SysDate.getSysTime());
				cpqArchivefileBo.setImageSysUrl(authorizedURL);
				fileBoList.add(cpqArchivefileBo);
			}
		}
		interfaceQueryParams.setCpqArchivefileBoList(fileBoList);
		cpqReportQueryBo.setInterfaceQueryParamsBo(interfaceQueryParams);
		return cpqReportQueryBo;
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
			CpqSingleQuery cpqSingleQuery = cpqSingleQueryDao.findByReqId(reqID);
			log.debug("方法CreditPersonReportWebServiceImpl---getSyncCreditReport通过reqID查询出请求对象为：{}", cpqSingleQuery);
			if (cpqSingleQuery == null) {
				returnStr = getErrBoStringSync(PersonConstant.CODE_WAIT_QUERY, PersonConstant.MSG_WAIT_QUERY, reqID);
			}
			CpqSingleResult cpqSingleResult = cpqSingleResultDao.findByReqId(reqID);
			log.debug("方法CreditPersonReportWebServiceImpl---getSyncCreditReport通过reqID查询出结果对象为：{}", cpqSingleResult);
			if (cpqSingleResult == null) {
				CpqApproveBo checkInfo = InitBeanUtil.getCpqApproveFlowService().findCpqApproveByReqId(reqID);
				if (null != checkInfo) {
					String status = checkInfo.getStatus();
					if (DATASTATUS_CHECKREFUSE.equals(status)) {
						returnStr = getErrBoStringSync(PersonConstant.CODE_EXAMINE_RESUSE,
								PersonConstant.MSG_EXAMINE_RESUSE, reqID);
					} else if (!DATASTATUS_CHECKPASS.equals(status)) {
						returnStr = getErrBoStringSync(PersonConstant.CODE_WAIT_EXAMINE,
								PersonConstant.MSG_WAIT_EXAMINE, reqID);
					} else {
						returnStr = getErrBoStringSync(PersonConstant.CODE_WAIT_QUERY, PersonConstant.MSG_WAIT_QUERY,
								reqID);
					}
				} else {
					returnStr = getErrBoStringSync(PersonConstant.CODE_WAIT_QUERY, PersonConstant.MSG_WAIT_QUERY,
							reqID);
				}
				return returnStr;
			}
			String queryUserOrg = getQueryUserOrg(cpqSingleQuery.getQueryUser());
			String rootDeptCode = UserUtilsForConfig.getRootDeptCode(queryUserOrg);
			String systemStorageType = ConfigUtil.getSystemStorageType(rootDeptCode);
			handleHtmlReport(cpqSingleResult, systemStorageType);
			handleXmlReport(cpqSingleResult, systemStorageType);
			handleJsonReport(cpqSingleResult, systemStorageType);
			handlePdfReport(cpqSingleResult, systemStorageType);
			returnStr = XmlUtil.Obj2Xml(cpqSingleResult);
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
	private String getErrBoString(String resCode, String resMsg, String reqID, String msgNo) {
		log.info("进入方法getErrBoString(String resCode, String resMsg, String {})", reqID);
		CpqSingleResult rv = new CpqSingleResult();
		rv.setResCode(resCode);
		rv.setResMsg(resMsg);
		rv.setReqId(reqID);
		rv.setMsgNo(msgNo);
		cpqSingleResultDao.save(rv);
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
		CpqSingleResult rv = new CpqSingleResult();
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
		String errBoString = getErrBoString(PersonConstant.CODE_ERROR, sb.toString(), parse4Xml.getReqId(),
				parse4Xml.getMsgNo());
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
						PersonConstant.STOP_FLAG);
				log.debug("从ip配置表查询出ipList为{}", ipList);
				if (CollectionUtils.isEmpty(ipList)) {
					returnStr = getErrBoString(PersonConstant.CODE_REFUSE, PersonConstant.MSG_REFUSE,
							singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
					return returnStr;
				}
				if (!Validator.verifyUserIp(singleQueryBo.getUserIp(), ipList)) {
					returnStr = getErrBoString(PersonConstant.CODE_REFUSE, PersonConstant.MSG_REFUSE,
							singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
					return returnStr;
				}
			} else {
				returnStr = getErrBoString(PersonConstant.CODE_REFUSE, PersonConstant.MSG_REFUSE,
						singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
				return returnStr;
			}
		} catch (Exception e) {
			log.error("合法性校验出现异常：{}", e);
			returnStr = getErrBoString(PersonConstant.CODE_SYS_EXCEPTION, PersonConstant.MSG_SYS_EXCEPTION,
					singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
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
	public void changeReport(CpqSingleResult singleResult, String reportType, CpqQueryReportFlowBo cpqQueryReportFlowBo)
			throws IOException {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		if (reportType.contains(REPORTTYPE_HTML)) {
			message = cpqQueryReportFlowBo.getHtmlCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setHtmlStr(zipMessage);
				singleResult.setHtmlMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_XML)) {
			message = cpqQueryReportFlowBo.getXmlCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setXmlStr(zipMessage);
				singleResult.setXmlMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_PDF)) {
			message = cpqQueryReportFlowBo.getPdfCreditReport();
			if (StringUtils.isNotBlank(message)) {
				zipMessage = ZipUtils.gzip(message);
				md5Message = MD5.getMd5String(zipMessage);
				singleResult.setPdfStr(zipMessage);
				singleResult.setPdfMd5(md5Message);
			}
		}
		if (reportType.contains(REPORTTYPE_JSON)) {
			message = cpqQueryReportFlowBo.getJsonCreditReport();
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
	 * @param cpqQueryReportFlowBo
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
	@Transactional
	public void saveReport(CpqSingleResult singleResult, String reportType, CpqQueryReportFlowBo cpqQueryReportFlowBo) {
		if (reportType.contains(REPORTTYPE_HTML)) {
			singleResult.setHtmlStr(cpqQueryReportFlowBo.getHtmlReportPath());
		}

		if (reportType.contains(REPORTTYPE_XML)) {
			singleResult.setXmlStr(cpqQueryReportFlowBo.getXmlReportPath());
		}

		if (reportType.contains(REPORTTYPE_PDF)) {
			singleResult.setPdfStr(cpqQueryReportFlowBo.getPdfReportPath());
		}

		if (reportType.contains(REPORTTYPE_JSON)) {
			singleResult.setJsonStr(cpqQueryReportFlowBo.getJsonReportPath());
		}
		cpqSingleResultDao.save(singleResult);
	}
	
	private boolean checkOrgNoExisted(String callSysOrg) {
		if (StringUtils.isEmpty(callSysOrg)) {
			return false;
		}
		CpqOrgAttrBo org = cpqOrgAttrService.findByOrgId(callSysOrg);
		if (org == null || StringUtils.isEmpty(org.getOrgId())) {
			return false;
		}
		return true;
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
			//批量请求
			if (StringUtils.isNotBlank(singleQueryBo.getMsgNo()) ) {
				if (!this.checkOrgNoExisted(singleQueryBo.getCallSysOrg())) {
					returnStr = getErrBoString(PersonConstant.MSG_ERROR, PersonConstant.MSG_ERROR,
							singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
					return returnStr;
				} 
			}
		} catch (Exception e) {
			log.error("方法：CreditPersonReportWebServiceImpl---fieldValidate出现异常{}", e);
			returnStr = getErrBoString(PersonConstant.CODE_SYS_EXCEPTION, PersonConstant.MSG_SYS_EXCEPTION,
					singleQueryBo.getReqId(), singleQueryBo.getMsgNo());
		}
		return returnStr;
	}

	/**
	 * 处理html报文
	 * 
	 * @param cpqSingleResult
	 * @param systemStorageType
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handleHtmlReport(CpqSingleResult cpqSingleResult, String systemStorageType) throws IOException {
		log.info("进入方法：handleHtmlReport(CpqSingleResult {})", cpqSingleResult);
		String htmlStr = cpqSingleResult.getHtmlStr();
		log.debug("方法handleHtmlReport---htmlStr:{}", htmlStr);
		if (StringUtils.isNotBlank(htmlStr)) {
			String readFileToString = readFileContent(systemStorageType, htmlStr);
			log.debug("方法handleHtmlReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			cpqSingleResult.setHtmlStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleHtmlReport---md5String:{}", md5String);
			cpqSingleResult.setHtmlMd5(md5String);
		}
	}

	/**
	 * 读取报文文件内容
	 * 
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
	private void handleXmlReport(CpqSingleResult cpqSingleResult, String systemStorageType) throws IOException {
		log.info("进入方法handleXmlReport(CpqSingleResult {})", cpqSingleResult);
		String xmlStr = cpqSingleResult.getXmlStr();
		log.debug("方法handleXmlReport---xmlStr:{}", xmlStr);
		if (StringUtils.isNotBlank(xmlStr)) {
			String readFileToString = readFileContent(systemStorageType, xmlStr);
			log.debug("方法handleXmlReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			cpqSingleResult.setXmlStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleXmlReport---md5String:{}", md5String);
			cpqSingleResult.setXmlMd5(md5String);
		}
	}

	/**
	 * 处理json报文
	 * 
	 * @param cpqSingleResult
	 * @param systemStorageType
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handleJsonReport(CpqSingleResult cpqSingleResult, String systemStorageType) throws IOException {
		log.info("进入方法handleJsonReport(CpqSingleResult {})", cpqSingleResult);
		String jsonStr = cpqSingleResult.getJsonStr();
		log.debug("方法handleJsonReport---jsonStr:{}", jsonStr);
		if (StringUtils.isNotBlank(jsonStr)) {
			String readFileToString = readFileContent(systemStorageType, jsonStr);
			log.debug("方法handleJsonReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			cpqSingleResult.setJsonStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handleJsonReport---md5String:{}", md5String);
			cpqSingleResult.setJsonMd5(md5String);
		}
	}

	/**
	 * 处理pdf报文
	 * 
	 * @param cpqSingleResult
	 * @throws IOException
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	private void handlePdfReport(CpqSingleResult cpqSingleResult, String systemStorageType) throws IOException {
		log.info("进入方法handlePdfReport(CpqSingleResult {})", cpqSingleResult);
		String pdfStr = cpqSingleResult.getPdfStr();
		log.debug("方法handlePdfReport---jsonStr:{}", pdfStr);
		if (StringUtils.isNotBlank(pdfStr)) {
			String readFileToString = readFileContent(systemStorageType, pdfStr);
			log.debug("方法handlePdfReport---readFileToString:{}", readFileToString);
			String gzip = ZipUtils.gzip(readFileToString);
			cpqSingleResult.setPdfStr(gzip);
			String md5String = MD5.getMd5String(gzip);
			log.debug("方法handlePdfReport---md5String:{}", md5String);
			cpqSingleResult.setPdfMd5(md5String);
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
