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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.com.dhcc.query.creditquerycommon.util.KeyProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeFileService;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeManagerService;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqPbocUserBo;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo;
import cn.com.dhcc.creditquery.person.query.bo.querycontrol.CpqQueryControlBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqPageFlowControlInfo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqDicService;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqPbocUserService;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqUserAttrService;
import cn.com.dhcc.creditquery.person.querycontrol.service.CpqQueryControlValidateService;
import cn.com.dhcc.creditquery.person.querycontrol.service.alert.CpqAlertService;
import cn.com.dhcc.creditquery.person.queryflowmanager.QueryFlowMannerConstant;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqQueryRecordService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.queryreport.CpqQueryReportService;
import cn.com.dhcc.informationplatform.amqp.client.RabbitMQClient;
import cn.com.dhcc.platformmiddleware.constant.LoginVlidateConstants;
import cn.com.dhcc.platformmiddleware.login.result.LoginValidateResultBean;
import cn.com.dhcc.platformmiddleware.vo.SystemRole;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqPrivilegeType;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.ReportQueryStep;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.queryapicommon.rabbitmq.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 查询流程控制服务类
 *
 * @Auther: liulekang
 * @Date: 2019/2/21
 */
@Slf4j
@Service
public class CpqFlowManageServiceImpl implements CpqFlowManageService {

	private static final String STATUS_VALID = "1";

	private static final String ARCHIVE_TYPE = "archiveType";

	private static final String SUFFIX_NAME_SPLIT = ".";
	
	private static final String MSG_QUERYUSER_NON_EXISTENT = "查询用户不存在";

	private static final String MSG_QUERYUSER_IS_NULL_CODE = "查询用户不能为空";

	private static final String MSG_QUERYUSER_LOCKED_CODE = "查询用户被锁定";

	private static final String MSG_QUERYUSER_STOP_CODE = "查询用户被停用";
	/**
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter";
	/**
	 * 是否为微服务版本
	 */
	private static final  String IS_MICRO_SERVICE_CONFIG_KEY = "isMicroService";
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	/**
	 * 查询控制服务
	 */
	@Autowired
	private CpqQueryControlValidateService queryControlValidateService;

	/**
	 * 查询记录服务
	 */
	@Autowired
	private CpqQueryRecordService cpqQueryRecordService;

	/**
	 * 审批服务
	 */
	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;

	@Autowired
	private CpqAuthorizeManagerService authorizeManagerService;
	@Autowired
	private CpqQueryReportService cpqQueryReportService;
	@Autowired
	private CpqAuthorizeFileService cpqAuthorizeFileService;
	@Autowired
	private CpqUserAttrService cpqUserAttrService;
	@Autowired
	private CpqPbocUserService cpqPbocUserService;
	@Autowired
	private CpqAlertService cpqAlertService;
	@Autowired
	private CpqDicService cpqDicService;

	@Override
	public CpqPageFlowControlInfo webQueryFlowManager(CpqReportQueryBo cpqReportQueryBo) {
		try {
			log.info("webQueryFlowManager param Info cpqReportQueryBo = {}", cpqReportQueryBo);
			cpqReportQueryBo.initSerialNumber();
			CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
			String approveId = webQueryBo.getApproveId();
			if (StringUtils.isNotBlank(approveId)) {
				CpqApproveBo approveBo = cpqApproveFlowService.findCpqApproveById(approveId);
				webQueryBo.setApproveTime(approveBo.getRekTime());
				webQueryBo.setApproveOrg(approveBo.getRekOrg());
			}
			ReportQueryStep queryNextStep = webQueryBo.getQueryNextStep();
			if (queryNextStep == ReportQueryStep.PRECHECK) {
				// 进行规则预校验
				CpqQueryControlBo cpqQueryControlBo = buildCpqQueryControlBo(cpqReportQueryBo);
				cpqQueryControlBo.setInterface(false);
				cpqQueryControlBo.setPreValidate(true);
				CpqQueryControlBo queryControlBo = queryControlValidateService
						.queryExceptionRuleValidate(cpqQueryControlBo);
				CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(queryControlBo);
				return pageFlowControlInfo;
			} else if (queryNextStep == ReportQueryStep.AUTHORIZE) {
				// 授权前步骤
				return authorizedStepHandle(cpqReportQueryBo);
			} else if (queryNextStep == ReportQueryStep.CHECKINFO) {
				// 审批前步骤
				return precheckStepHandle(cpqReportQueryBo);
			} else if (queryNextStep == ReportQueryStep.INQUIRE) {
				// 进行查询前审批
				// 是否指纹审批
				CpqApproveBo cpqApproveBo1 = null;
				CpqPageFlowControlInfo pageFlowControlInfo = null;
				boolean sysncApproveFlag = false;
				boolean needCheck = webQueryBo.isNeedCheck();
				if (needCheck) {
					// 需要进行审批
					boolean fingerApproveFlag = webQueryBo.isFingerApproveFlag();
					if (fingerApproveFlag) {
						// 指纹审批，指纹审批流程由页面进行了处理;在此时一定是审批通过的,直接存储审批记录即可
						CpqApproveBo cpqApproveBo = buildCpqApproveBo(cpqReportQueryBo);
						cpqApproveBo.setStatus(APPROVED_REVIEW);
						cpqApproveBo.setRekTime(new Date());
						cpqApproveBo1 = cpqApproveFlowService.create(cpqApproveBo);
					} else {
						// 调用审批服务进行普通审批
						String approveWay = webQueryBo.getApproveWay();
						if (StringUtils.equals(approveWay, SYNC_RECHECK)) {
							// 同步审批
							pageFlowControlInfo = cpqApproveFlowService.sysncApprove(cpqReportQueryBo);
							if (!StringUtils.equals(pageFlowControlInfo.getResCode(), Constant.REVIEW_SYN_OK)) {
								// 审批不通过，返回相应信息
								return pageFlowControlInfo;
							}
							sysncApproveFlag = true;
						} else {
							// 异步审批，新增一条审批记录
							CpqApproveBo cpqApproveBo = buildCpqApproveBo(cpqReportQueryBo);
							cpqApproveBo.setStatus(PENDING_REVIEW);
							cpqApproveBo1 = cpqApproveFlowService.create(cpqApproveBo);
							pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqApproveBo1);
							if (null == cpqApproveBo1) {
								pageFlowControlInfo.setResCode(Constant.POLICY_RECHECK_E);
								pageFlowControlInfo.setResMsg("系统处理出现问题。请联系管理员");
								return pageFlowControlInfo;
							}
							pageFlowControlInfo.setApproveRecordId(cpqApproveBo1.getId());
							pageFlowControlInfo.setResCode(Constant.REVIEW_ASYN_OK);
							pageFlowControlInfo.setResMsg("异步审批提交成功！");
							return pageFlowControlInfo;
						}
					}
				}
				//判断是否存在本地报告
				CpqPageFlowControlInfo haveLocalReport = isHaveLocalReport(cpqReportQueryBo, webQueryBo);
				if (null != cpqApproveBo1) {
					haveLocalReport.setApproveRecordId(cpqApproveBo1.getId());
				}
				if (sysncApproveFlag) {
					haveLocalReport.setApproveRecordId(pageFlowControlInfo.getApproveRecordId());
				}
				return haveLocalReport;
			} else if (queryNextStep == ReportQueryStep.QUERYREPORT) {
				// 查询人行报告
				cpqReportQueryBo.setAccessSource("1");
				cpqReportQueryBo.setQueryType(0);
				return queryReportStepHandle(cpqReportQueryBo);
			} else if (queryNextStep == ReportQueryStep.QUERYLOCALREPORT) {
				// 查询本地报告
				cpqReportQueryBo.setAccessSource("1");
				// 获取查询策略
				String loacalDayStr = ConfigUtil.getLocalReportValidity();
				Integer loacalDay = Integer.valueOf(loacalDayStr);
				cpqReportQueryBo.setQueryType(0 - loacalDay);
				return queryReportStepHandle(cpqReportQueryBo);
			}
		} catch (Exception e) {
			log.error("webQueryFlowManager is error e = ", e);
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResCode(Constant.POLICY_RECHECK_E);
			pageFlowControlInfo.setResMsg("系统处理出现问题。请联系管理员");
			return pageFlowControlInfo;
		}
		return null;
	}

	/**
	 * 查询信用报告的处理
	 * 
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqPageFlowControlInfo queryReportStepHandle(CpqReportQueryBo cpqReportQueryBo) {
		// 查询前规则验证
		CpqQueryControlBo cpqQueryControlBo = buildCpqQueryControlBo(cpqReportQueryBo);
		cpqQueryControlBo.setInterface(false);
		cpqQueryControlBo.setPreValidate(false);
		CpqQueryControlBo queryControlBo = queryControlValidateService.queryExceptionRuleValidate(cpqQueryControlBo);
		String resCode = queryControlBo.getResCode();
		if (!StringUtils.equals(Constant.CHECK_SUCCESS, resCode)) {
			// 验证未通过,不进行查询.返回相应信息
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(queryControlBo);
			return pageFlowControlInfo;
		}
		// 验证通过调用查询服务进行查询
		cpqReportQueryBo.setAccessSource(ACCESS_SOURCE_WEB);
		CpqQueryReportFlowBo cpqQueryReportFlowBo = cpqQueryReportService.creditReportQuery(cpqReportQueryBo);
		
		/**
		 * 查询不成功的情况下，页面代码返回10999,弹出提示窗口。
		 */
		if(!Objects.equals(Constants.QUERY_SUCCESSCODE, cpqQueryReportFlowBo.getResCode())) {
			cpqQueryReportFlowBo.setResCode("10999");
		}
		CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqQueryReportFlowBo);
		String queryRecordId = cpqQueryReportFlowBo.getQueryRecordId();
		pageFlowControlInfo.setQueryRecordId(queryRecordId);
		return pageFlowControlInfo;
	}

	/**
	 * 判断本地是否存在历史报告，并进行相关信息处理
	 *
	 * @param cpqReportQueryBo
	 * @param webQueryBo
	 * @return
	 */
	private CpqPageFlowControlInfo isHaveLocalReport(CpqReportQueryBo cpqReportQueryBo,
			CpqReportQueryBo.WebQueryBo webQueryBo) {
		String localDayStr = ConfigUtil.getLocalReportValidity();
		boolean haveLocalReport = cpqQueryRecordService.isHaveLocalReport(cpqReportQueryBo.getClientName(),
				cpqReportQueryBo.getCertType(), cpqReportQueryBo.getCertNo(), cpqReportQueryBo.getQueryReasonId(),
				webQueryBo.getTopOrg(), localDayStr);
		if (haveLocalReport) {
			// 本地存在报告
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResCode(Constant.LOCAL_HAS_REPORT);
			pageFlowControlInfo.setResMsg("本地存在信用报告，是否进行展示?");
			return pageFlowControlInfo;
		}
		// 本地没有报告
		CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
		pageFlowControlInfo.setResCode(Constant.LOCAL_NO_REPORT);
		pageFlowControlInfo.setResMsg("本地无报告，进行查询。");
		return pageFlowControlInfo;
	}

	/**
	 * precheck步骤的处理
	 *
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqPageFlowControlInfo precheckStepHandle(CpqReportQueryBo cpqReportQueryBo) {
		// 查询原因是否需要进行审批
		boolean isReasonNeedApprove = queryControlValidateService.isReasonNeedApprove(cpqReportQueryBo.getQueryReasonId(), cpqReportQueryBo.getTopOrgCode());
		if (isReasonNeedApprove) {
			// 需要审批
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
			String url = CHECKINFO_URL;
			if(Boolean.parseBoolean(property)){
				url = CHECKINFO_URL_MICRO;
			}
			pageFlowControlInfo.setNextStepPageUrl(url);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.POLICY_RECHECK_Y_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.POLICY_RECHECK_Y_MSG);
			return pageFlowControlInfo;
		} else {
			// 不需要审批，直接进入查询步骤
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setNextStepPageUrl(DISPATCHER_URL);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.POLICY_RECHECK_N_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.POLICY_RECHECK_N_MSG);
			return pageFlowControlInfo;
		}
	}

	/**
	 * authorized步骤的相关处理 进行理，下一步进行授权认证或审批页面
	 *
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqPageFlowControlInfo authorizedStepHandle(CpqReportQueryBo cpqReportQueryBo) {
		WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		// 判断用户有无查询原因使用权限
		boolean isUserHaveQueryReasonPermission = queryControlValidateService
				.isUserHaveQueryReasonPermission(webQueryBo.getUserRoles(), cpqReportQueryBo.getQueryReasonId());
		if (!isUserHaveQueryReasonPermission) {
			// 用户无查询原因权限，返回相应提示信息
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.HAVE_NORIGHT_INQUIRE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.HAVE_NORIGHT_MSG);
			return pageFlowControlInfo;
		}
		// 根据用户名,查询原因判断是否有不使用授权档案及审批的绿色通道特权特权
		boolean isAuthorizePrivilegeUserForReason = queryControlValidateService.isPrivilegeUserForReason(
				cpqReportQueryBo.getOperator(), CpqPrivilegeType.AUTHORIZEPPROVAL, cpqReportQueryBo.getQueryReasonId());
		if (isAuthorizePrivilegeUserForReason) {
			// 特权用户，不需进行授权步骤。返回相应信息
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.GREEN_PASS_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.GREEN_PASS_MSG);
			return pageFlowControlInfo;
		}

		// 查询原因是否需要授权
		boolean isReasonNeedAuthorize = queryControlValidateService.isReasonNeedAuthorize(cpqReportQueryBo.getQueryReasonId(), cpqReportQueryBo.getTopOrgCode());
		if (isReasonNeedAuthorize) {
			// 需要授权
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
			String url = ARCHIVE_URL;
			if(Boolean.parseBoolean(property)){
				url = ARCHIVE_URL_MICRO;
			}
			pageFlowControlInfo.setNextStepPageUrl(url);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.POLICY_DELEGATION_Y_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.POLICY_DELEGATION_Y_MSG);
			return pageFlowControlInfo;
		} else {
			// 不需要授权，判断是否需要进入审批步骤
			return precheckStepHandle(cpqReportQueryBo);
		}
	}

	@Override
	public CpqQueryReportFlowBo interfaceQueryFlowManager(CpqReportQueryBo cpqReportQueryBo) {
		CpqQueryReportFlowBo cpqQueryReportFlowBo = new CpqQueryReportFlowBo();
		try {
			// 前置系统用户校验
			Map<String, String> validateQueryUser = validateQueryUser(cpqReportQueryBo.getOperator());
			log.debug("用户可用性校验结果为CpqSingleResult:{}", validateQueryUser);
			if (MapUtils.isNotEmpty(validateQueryUser)) {
				if(StringUtils.isNotBlank(cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				String resCode = (String) validateQueryUser.keySet().toArray()[0];
				cpqQueryReportFlowBo.setResCode(resCode);
				cpqQueryReportFlowBo.setResMsg(validateQueryUser.get(resCode));
				return cpqQueryReportFlowBo;
			}
			boolean lockedUser = cpqAlertService.isLockedUser(cpqReportQueryBo.getOperator());
			if (!lockedUser) {
				if(StringUtils.isNotBlank(cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				cpqQueryReportFlowBo.setResCode(CODE_USER_IS_LOCKED_BUSINESS);
				cpqQueryReportFlowBo.setResMsg(MSG_USER_IS_LOCKED_BUSINESS);
				return cpqQueryReportFlowBo;
			}

			CpqQueryControlBo queryExceptionRuleValidate = validateCanQuery(cpqReportQueryBo);
			// 进行规则阻断验证
			log.debug("interfaceQueryFlowManager validateCanQuery result queryExceptionRuleValidate = ",
					queryExceptionRuleValidate);
			if (!StringUtils.equals(Constant.CHECK_SUCCESS, queryExceptionRuleValidate.getResCode())) {
				if(StringUtils.isNotBlank(cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				// 验证未通过,不进行查询.返回相应信息
				cpqQueryReportFlowBo.setBoCommonField(queryExceptionRuleValidate);
				return cpqQueryReportFlowBo;
			}
			if (StringUtils.isNotEmpty(cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorType())) {
				CpqArchiveBo cpqArchiveBo = new CpqArchiveBo();
				cpqArchiveBo.setClientName(cpqReportQueryBo.getClientName());
				cpqArchiveBo.setCretType(cpqReportQueryBo.getCertType());
				cpqArchiveBo.setCretNo(cpqReportQueryBo.getCertNo());
				cpqArchiveBo.setCreatTime(new Date());
				cpqArchiveBo.setCreator(cpqReportQueryBo.getOperator());
				cpqArchiveBo.setExpireDate(cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorEndDate());
				cpqArchiveBo.setStartDate(cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorBeginDate());
				cpqArchiveBo.setStatus(STATUS_VALID);
				cpqArchiveBo.setOwnorg(cpqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg());
				cpqArchiveBo.setOperorg(cpqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg());
				cpqArchiveBo.setOperTime(new Date());
				cpqArchiveBo.setOperator(cpqReportQueryBo.getOperator());
				cpqArchiveBo.setArchiveType(cpqReportQueryBo.getInterfaceQueryParamsBo().getAuthorType());
				cpqArchiveBo.setReqId(cpqReportQueryBo.getInterfaceQueryParamsBo().getReqId());
				List<CpqArchivefileBo> cpqArchivefileBoList = cpqReportQueryBo.getInterfaceQueryParamsBo()
						.getCpqArchivefileBoList();
				Long quantity = 0L;
				if (CollectionUtils.isNotEmpty(cpqArchivefileBoList)) {
					quantity = (long) cpqArchivefileBoList.size();
				}
				cpqArchiveBo.setQuantity(quantity);
				CpqArchiveBo create = authorizeManagerService.create(cpqArchiveBo);
				String id = create.getId();
				// 将档案ID传入查询参数中，用于记录查询关联档案
				InterfaceQueryParams interfaceQueryParamsBo = cpqReportQueryBo.getInterfaceQueryParamsBo();
				interfaceQueryParamsBo.setAuthArchiveId(id);
				// 保存档案文件
				for (CpqArchivefileBo cpqArchivefileBo : cpqArchivefileBoList) {
					cpqArchivefileBo.setArchiveid(id);
					cpqArchivefileBo.setAuthorizeFileMode(Constants.AUTHORIZEFILE_MODE_1);
					String fileType = cpqArchivefileBo.getFileType();
					fileType = fileTypeConversion(fileType);
					if (StringUtils.isNotBlank(fileType)) {
						String dicName = cpqDicService.getDicValue(ARCHIVE_TYPE, fileType);
						String suffixName = cpqArchivefileBo.getFileFoamt();
						if(StringUtils.isNotBlank(suffixName)) {
							dicName += SUFFIX_NAME_SPLIT + suffixName;
							cpqArchivefileBo.setFilename(dicName);
						}
					}

				}
				cpqAuthorizeFileService.createList(cpqArchivefileBoList);
			}
			log.debug("创建电子档案end");

			String syncFlag = cpqReportQueryBo.getInterfaceQueryParamsBo().getSyncFlag();
			if (SYNCFLAGZERO.equals(syncFlag)) {// 同步
				cpqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
				cpqQueryReportFlowBo = queryReportStr(cpqReportQueryBo);
				return cpqQueryReportFlowBo;
			} else if (SYNCFLAGONE.equals(syncFlag)) {// 异步
				RabbitMQClient client = RabbitMqUtil.getClientByType(QUERY_TYPE_PERSON);
				client.send(JSON.toJSONString(cpqReportQueryBo));
				cpqQueryReportFlowBo.setResCode(QueryFlowMannerConstant.CODE_WAIT_QUERY);
				cpqQueryReportFlowBo.setResMsg(QueryFlowMannerConstant.MSG_WAIT_QUERY);
				return cpqQueryReportFlowBo;
			}
		} catch (Exception e) {
			log.error("interfaceQueryFlowManager(CpqReportQueryBo cpqReportQueryBo{})出现异常{}", cpqReportQueryBo, e);
			if(StringUtils.isNotBlank(cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
				String msgNo = cpqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
				log.info("msgNo{}",msgNo);
				redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
			}
			cpqQueryReportFlowBo.setResCode(QueryFlowMannerConstant.CODE_SYS_EXCEPTION);
			cpqQueryReportFlowBo.setResMsg(e.getLocalizedMessage());
			return cpqQueryReportFlowBo;

		}
		return cpqQueryReportFlowBo;
	}

	/**
	 * 文件类型转换
	 * 
	 * @param fileType
	 * @return
	 * @author DHC-S
	 * @date 2019年3月14日
	 */
	private String fileTypeConversion(String fileType) {
		if (StringUtils.isNotBlank(fileType)) {
			if (fileType.equals(Constants.FILE_TYPE_1)) {
				return Constants.FILE_TYPE_101;
			} else if (fileType.equals(Constants.FILE_TYPE_2)) {
				return Constants.FILE_TYPE_102;
			} else if (fileType.equals(Constants.FILE_TYPE_3)) {
				return Constants.FILE_TYPE_103;
			} else if (fileType.equals(Constants.FILE_TYPE_9)) {
				return Constants.FILE_TYPE_200;
			}
		}
		return fileType;
	}

	@Override
	public CpqQueryReportFlowBo queryReportStr(CpqReportQueryBo cpqReportQueryBo) {
		log.info("queryReportStr param cpqReportQueryBo = ", cpqReportQueryBo);
		CpqQueryReportFlowBo cpqQueryReportFlowBo = cpqQueryReportService.creditReportQuery(cpqReportQueryBo);
		return cpqQueryReportFlowBo;
	}

	@Override
	public CpqQueryRecordBo create(CpqQueryRecordBo cpqQueryRecordBo) {
		return cpqQueryRecordService.create(cpqQueryRecordBo);
	}

	@Override
	public CpqQueryRecordBo update(CpqQueryRecordBo cpqQueryRecordBo) {
		return cpqQueryRecordService.update(cpqQueryRecordBo);
	}

	@Override
	public CpqQueryRecordBo findCpqQueryRecordBoById(String id) {
		return cpqQueryRecordService.findById(id);
	}

	@Override
	public List<CpqQueryRecordBo> findAll(Map<String, Object> searchParams) {
		return cpqQueryRecordService.findAll(searchParams);
	}

	@Override
	public List<CpqQueryRecordBo> findByIds(List<String> ids) {
		return cpqQueryRecordService.findByIds(ids);
	}

	@Override
	public Page<CpqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy) {
		return cpqQueryRecordService.getPage(searchParams, pageNumber, pageSize, direction, orderBy);
	}

	@Override
	public CpqQueryReportFlowBo getLocalReport(String customerName, String certType, String certNo, String qryReason,
			String topOrgCode, String localDay) {
		CpqQueryReportFlowBo localReport = cpqQueryRecordService.getLocalReport(customerName, certType, certNo,
				qryReason, topOrgCode, localDay);
		return localReport;
	}

	@Override
	public boolean isHaveLocalReport(String customerName, String certType, String certNo, String qryReason,
			String topOrgCode, String localDay) {
		return cpqQueryRecordService.isHaveLocalReport(customerName, certType, certNo, qryReason, topOrgCode, localDay);
	}

	@Override
	public String getReportById(String id, String reportType) {
		return cpqQueryRecordService.getReportById(id, reportType);
	}

	@Override
	public List<String> batchQueryReport(String batchFilePath) {
		return null;
	}

	@Override
	public CpqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds) {
		return cpqQueryRecordService.getArchiveShortcut(userName, deptCodes, menuIds);
	}

	@Override
	public void updateArchiveIdById(String id, String archiveId) {
		cpqQueryRecordService.updateArchiveIdById(id, archiveId);
	}

	@Override
	public boolean isQueriedCreditUser(String creditUser) {
		return cpqQueryRecordService.isQueriedCreditUser(creditUser);
	}

	/**
	 * 构建查询管控服务BO
	 *
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqQueryControlBo buildCpqQueryControlBo(CpqReportQueryBo cpqReportQueryBo) {
		CpqQueryControlBo cpqQueryControlBo = new CpqQueryControlBo();
		CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		cpqQueryControlBo.setQueryUserName(cpqReportQueryBo.getOperator());
		cpqQueryControlBo.setOrgCode(webQueryBo.getQueryOrg());
		cpqQueryControlBo.setClientName(cpqReportQueryBo.getClientName());
		cpqQueryControlBo.setCertNo(cpqReportQueryBo.getCertNo());
		cpqQueryControlBo.setCertType(cpqReportQueryBo.getCertType());
		cpqQueryControlBo.setQueryReason(cpqReportQueryBo.getQueryReasonId());
		cpqQueryControlBo.setBoCommonField(cpqReportQueryBo);
		return cpqQueryControlBo;
	}

	/**
	 * 构造审批业务对象
	 *
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqApproveBo buildCpqApproveBo(CpqReportQueryBo cpqReportQueryBo) {
		CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		CpqApproveBo approveBo = new CpqApproveBo();
		approveBo.setArchiveId(webQueryBo.getAuthArchiveId());
		approveBo.setAssocbsnssData(webQueryBo.getRelationBaseData());
		approveBo.setClientName(cpqReportQueryBo.getClientName());
		approveBo.setCertNo(cpqReportQueryBo.getCertNo());
		approveBo.setCertType(cpqReportQueryBo.getCertType());
		approveBo.setCreditUser(webQueryBo.getCreditUser());
		approveBo.setOperator(cpqReportQueryBo.getOperator());
		approveBo.setOperOrg(webQueryBo.getQueryOrg());
		approveBo.setOperTime(new Date());
		approveBo.setQryReason(cpqReportQueryBo.getQueryReasonId());
		approveBo.setQueryFormat(webQueryBo.getReportVersion());
		approveBo.setQueryTime(new Date());
		approveBo.setRekType(webQueryBo.getApproveWay());
		approveBo.setRekUser(webQueryBo.getApproveUser());
		approveBo.setRekOrg(webQueryBo.getApproveOrg());
		CpqReportResultType reportResultType = webQueryBo.getReportResultType();
		approveBo.setResultType(reportResultType.getCode());
		approveBo.setQueryType("0");
		approveBo.setRekTime(new Date());
		approveBo.setBoCommonField(cpqReportQueryBo);
		return approveBo;
	}

	/**
	 * 校验前置系统用户
	 *
	 * @param queryUser
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月22日
	 */
	private Map<String, String> validateQueryUser(String queryUser) {
		String json = "";
		Map<String, String> returnMap = new HashMap<>();
		try {
			// 进行查询员有效性校验
			Map<String, String> validateSystemUser = validateSystemUser(queryUser, returnMap);
			if(MapUtils.isNotEmpty(validateSystemUser)){
				return validateSystemUser;
			}
			// 获取征信用户
			CpqUserAttrBo userAttr = cpqUserAttrService.findByUserId(queryUser);
			log.debug("validateQueryUser-------userAttr:" + userAttr);
			if (null == userAttr) {
				returnMap.put(QueryFlowMannerConstant.CODE_NO_MAPPING_CREDITUSER, QueryFlowMannerConstant.MSG_NO_MAPPING_CREDITUSER);
				return returnMap;
			}
			String creditName = userAttr.getCreditUser();// 征信用户别名
			if (StringUtils.isBlank(creditName)) {
				returnMap.put(QueryFlowMannerConstant.CODE_NO_MAPPING_CREDITUSER,
						QueryFlowMannerConstant.MSG_NO_MAPPING_CREDITUSER);
				return returnMap;
			}

			CpqPbocUserBo creditUser = cpqPbocUserService.findByCreditName(creditName);
			log.debug("validateQueryUser-------creditUser:" + creditUser);
			if (null == creditUser) {
				returnMap.put(QueryFlowMannerConstant.CODE_NO_CREDITUSER, QueryFlowMannerConstant.MSG_NO_CREDITUSER);
				return returnMap;
			}

			return returnMap;
		} catch (Exception e) {
			log.error("webservice validateQueryUser ---{}", e);
			returnMap.put(QueryFlowMannerConstant.CODE_SYS_EXCEPTION, QueryFlowMannerConstant.MSG_SYS_EXCEPTION);
		}
		return returnMap;

	}
	
	
	/**
	 * 校验前置系统用户
	 * @param queryUser
	 * @param returnMap
	 * @return
	 * @throws Exception
	 * @author yuzhao.xue
	 * @date 2019年3月30日
	 */
	private Map<String, String> validateSystemUser(String queryUser, Map<String, String> returnMap) throws Exception {
		String json;
		json = LoginValidateUtil.validateUser(queryUser);
		
		LoginValidateResultBean bean = null;
		try {
			bean = JSON.parseObject(json, LoginValidateResultBean.class);
		} catch (Exception e) {
			log.error("analysis error:", e);
			returnMap.put(QueryFlowMannerConstant.CODE_SYS_EXCEPTION, QueryFlowMannerConstant.MSG_SYS_EXCEPTION);
			return returnMap;
		}
		String beanCode = bean.getCode();
		switch (beanCode) {
		case LoginVlidateConstants.NON_EXISTENT:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					MSG_QUERYUSER_NON_EXISTENT);
			return returnMap;
		case LoginVlidateConstants.IS_NULL_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					MSG_QUERYUSER_IS_NULL_CODE);
			return returnMap;
		case LoginVlidateConstants.LOCKED_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					MSG_QUERYUSER_LOCKED_CODE);
			return returnMap;
		case LoginVlidateConstants.STOP_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					MSG_QUERYUSER_STOP_CODE);
			return returnMap;
		}
		List<SystemRole> list = bean.getData();
		if(CollectionUtils.isEmpty(list)) {
			returnMap.put(QueryFlowMannerConstant.CODE_REFUSE,
					QueryFlowMannerConstant.MSG_REFUSE);
		}else {
			String rootDeptCode = UserConfigUtils.getRootDeptCode(getQueryUserOrg(queryUser));
			for (SystemRole systemRole : list) {
				if (Objects.equals(systemRole.getId(), WEBSERVICE_QUERY_ROLE + rootDeptCode)) {
					return returnMap;
				}
			}
			returnMap.put(QueryFlowMannerConstant.CODE_REFUSE, QueryFlowMannerConstant.MSG_REFUSE);
		}
		return returnMap;
	}

	/**
	 * 解析用户验证信息
	 *
	 * @param json
	 * @return Map<String, String>
	 * @author guoshihu
	 * @date 2019年1月16日
	 */
	private Map<String, String> analysis(String json) {
		LoginValidateResultBean bean = null;
		Map<String, String> returnMap = new HashMap<>();
		try {
			bean = JSON.parseObject(json, LoginValidateResultBean.class);
		} catch (Exception e) {
			log.error("analysis error:", e.getMessage());
			returnMap.put(QueryFlowMannerConstant.CODE_SYS_EXCEPTION, QueryFlowMannerConstant.MSG_SYS_EXCEPTION);
			return returnMap;
		}
		String beanCode = bean.getCode();
		switch (beanCode) {
		case LoginVlidateConstants.NON_EXISTENT:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					QueryFlowMannerConstant.MSG_QUERYUSER_VALI_ERROR);
			break;
		case LoginVlidateConstants.IS_NULL_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					QueryFlowMannerConstant.MSG_QUERYUSER_VALI_ERROR);
			break;
		case LoginVlidateConstants.LOCKED_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					QueryFlowMannerConstant.MSG_QUERYUSER_VALI_ERROR);
			break;
		case LoginVlidateConstants.STOP_CODE:
			returnMap.put(QueryFlowMannerConstant.CODE_QUERYUSER_VALI_ERROR,
					QueryFlowMannerConstant.MSG_QUERYUSER_VALI_ERROR);
			break;
		default:
			returnMap.put(QueryFlowMannerConstant.CODE_SYS_EXCEPTION, QueryFlowMannerConstant.MSG_SYS_EXCEPTION);
		}
		return returnMap;
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
			userDeptcode = user.getOrgId();
		} catch (Exception e) {
			log.error("getQueryUserOrg error:", e);
		}
		log.info("getQueryUserOrg result={}", userDeptcode);
		return userDeptcode;
	}

	/**
	 * 阻断校验
	 *
	 * @param queryBo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月23日
	 */
	private CpqQueryControlBo validateCanQuery(CpqReportQueryBo queryBo) {
		log.info("validateCanQuery param queryBo = ", queryBo);
		CpqQueryControlBo ruleValidateBo = new CpqQueryControlBo();
		ruleValidateBo.setQueryUserName(queryBo.getOperator());
		ruleValidateBo.setPreValidate(false);
		ruleValidateBo.setInterface(true);
		ruleValidateBo.setClientName(queryBo.getClientName());
		ruleValidateBo.setCertNo(queryBo.getCertNo());
		ruleValidateBo.setCertType(queryBo.getCertType());
		ruleValidateBo.setOrgCode(queryBo.getInterfaceQueryParamsBo().getQueryOrg());
		ruleValidateBo.setQueryReason(queryBo.getQueryReasonId());
		CpqQueryControlBo queryExceptionRuleValidate = queryControlValidateService
				.queryExceptionRuleValidate(ruleValidateBo);
		return queryExceptionRuleValidate;
	}

	@Override
	public String getReportById(String recordId, String userName, String orgCode, String reportType) {
		return cpqQueryRecordService.readReportById(recordId, userName, orgCode, reportType);
	}
}
