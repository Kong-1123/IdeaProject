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

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeFileService;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeManagerService;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeFileBo;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqPbocUserBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqUserAttrBo;
import cn.com.dhcc.creditquery.ent.query.bo.querycontrol.CeqQueryControlBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqPageFlowControlInfo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqDicService;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqPbocUserService;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqUserAttrService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.CeqQueryControlValidateService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.alert.CeqAlertService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.QueryFlowMannerConstant;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqQueryRecordService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.queryreport.CeqQueryReportService;
import cn.com.dhcc.informationplatform.amqp.client.RabbitMQClient;
import cn.com.dhcc.platformmiddleware.constant.LoginVlidateConstants;
import cn.com.dhcc.platformmiddleware.login.result.LoginValidateResultBean;
import cn.com.dhcc.platformmiddleware.vo.SystemRole;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqPrivilegeType;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.ReportQueryStep;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.*;
import cn.com.dhcc.query.queryapicommon.rabbitmq.RabbitMqUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 查询流程控制服务类
 *
 * @Auther: liulekang
 * @Date: 2019/2/21
 */
@Slf4j
@Service
public class CeqFlowManageServiceImpl implements CeqFlowManageService {

	private static final String STATUS_VALID = "1";

	private static final String ARCHIVE_TYPE = "archiveidType_ceq";

	private static final String SUFFIX_NAME_SPLIT = ".";

	private static final String MSG_QUERYUSER_NON_EXISTENT = "查询用户不存在";

	private static final String MSG_QUERYUSER_IS_NULL_CODE = "查询用户不能为空";

	private static final String MSG_QUERYUSER_LOCKED_CODE = "查询用户被锁定";

	private static final String MSG_QUERYUSER_STOP_CODE = "查询用户被停用";
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	/**
	 * 是否为微服务版本
	 */
	private static final  String IS_MICRO_SERVICE_CONFIG_KEY = "isMicroService";

	/**
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter_e";
	/**
	 * 查询控制服务
	 */
	@Autowired
	private CeqQueryControlValidateService queryControlValidateService;

	/**
	 * 查询记录服务
	 */
	@Autowired
	private CeqQueryRecordService ceqQueryRecordService;

	/**
	 * 审批服务
	 */
	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;

	@Autowired
	private CeqAuthorizeManagerService authorizeManagerService;
	@Autowired
	private CeqQueryReportService ceqQueryReportService;
	@Autowired
	private CeqAuthorizeFileService ceqAuthorizeFileService;
	@Autowired
	private CeqUserAttrService ceqUserAttrService;
	@Autowired
	private CeqPbocUserService ceqPbocUserService;
	@Autowired
	private CeqAlertService ceqAlertService;
	@Autowired
	private CeqDicService ceqDicService;

	@Override
	public CeqPageFlowControlInfo webQueryFlowManager(CeqReportQueryBo CeqReportQueryBo) {
		try {
			log.info("webQueryFlowManager param Info CeqReportQueryBo = {}", CeqReportQueryBo);
			CeqReportQueryBo.initSerialNumber();
			CeqReportQueryBo.WebQueryBo webQueryBo = CeqReportQueryBo.getWebQueryBo();
			String approveId = webQueryBo.getApproveId();
			if (StringUtils.isNotBlank(approveId)) {
				CeqApproveBo approveBo = ceqApproveFlowService.findCeqApproveById(approveId);
				webQueryBo.setApproveTime(approveBo.getRekTime());
				webQueryBo.setApproveOrg(approveBo.getRekOrg());
			}
			ReportQueryStep queryNextStep = webQueryBo.getQueryNextStep();
			if (queryNextStep == ReportQueryStep.PRECHECK) {
				// 进行规则预校验
				CeqQueryControlBo CeqQueryControlBo = buildCeqQueryControlBo(CeqReportQueryBo);
				CeqQueryControlBo.setInterface(false);
				CeqQueryControlBo.setPreValidate(true);
				CeqQueryControlBo queryControlBo = queryControlValidateService
						.queryExceptionRuleValidate(CeqQueryControlBo);
				CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(queryControlBo);
				log.info("webQueryFlowManager result CeqPageFlowControlInfo = {}", pageFlowControlInfo);
				return pageFlowControlInfo;
			} else if (queryNextStep == ReportQueryStep.AUTHORIZE) {
				// 授权前步骤
				CeqPageFlowControlInfo pageFlowControlInfo = authorizedStepHandle(CeqReportQueryBo);
				log.info("webQueryFlowManager result CeqPageFlowControlInfo = {}", pageFlowControlInfo);
				return pageFlowControlInfo;
			} else if (queryNextStep == ReportQueryStep.CHECKINFO) {
				// 审批前步骤
				CeqPageFlowControlInfo pageFlowControlInfo = precheckStepHandle(CeqReportQueryBo);
				log.info("webQueryFlowManager result CeqPageFlowControlInfo = {}", pageFlowControlInfo);
				return pageFlowControlInfo;
			} else if (queryNextStep == ReportQueryStep.INQUIRE) {
				// 进行查询前审批
				// 是否指纹审批
				CeqApproveBo CeqApproveBo1 = null;
				CeqPageFlowControlInfo pageFlowControlInfo = null;
				boolean sysncApproveFlag = false;
				boolean needCheck = webQueryBo.isNeedCheck();
				log.info("webQueryFlowManager isNeedCheck  = {}", needCheck);
				if (needCheck) {
					// 需要进行审批
					boolean fingerApproveFlag = webQueryBo.isFingerApproveFlag();
					if (fingerApproveFlag) {
						// 指纹审批，指纹审批流程由页面进行了处理;在此时一定是审批通过的,直接存储审批记录即可
						CeqApproveBo CeqApproveBo = buildCeqApproveBo(CeqReportQueryBo);
						CeqApproveBo.setStatus(APPROVED_REVIEW);
						CeqApproveBo.setRekTime(new Date());
						CeqApproveBo1 = ceqApproveFlowService.create(CeqApproveBo);
					} else {
						// 调用审批服务进行普通审批
						String approveWay = webQueryBo.getApproveWay();
						log.info("webQueryFlowManager approveWay  = ", approveWay);
						if (StringUtils.equals(approveWay, SYNC_RECHECK)) {
							// 同步审批
							log.info("webQueryFlowManager sysncApprove params = ",CeqReportQueryBo);
							pageFlowControlInfo = ceqApproveFlowService.sysncApprove(CeqReportQueryBo);
							log.info("webQueryFlowManager sysncApprove result  = {} ", pageFlowControlInfo);
							if (!StringUtils.equals(pageFlowControlInfo.getResCode(), Constant.REVIEW_SYN_OK)) {
								// 审批不通过，返回相应信息
								return pageFlowControlInfo;
							}
							sysncApproveFlag = true;
						} else {
							// 异步审批，新增一条审批记录
							CeqApproveBo CeqApproveBo = buildCeqApproveBo(CeqReportQueryBo);
							CeqApproveBo.setStatus(PENDING_REVIEW);
							CeqApproveBo1 = ceqApproveFlowService.create(CeqApproveBo);
							pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqApproveBo1);
							if (null == CeqApproveBo1) {
								pageFlowControlInfo.setResCode(Constant.POLICY_RECHECK_E);
								pageFlowControlInfo.setResMsg("系统处理出现问题。请联系管理员");
								return pageFlowControlInfo;
							}
							pageFlowControlInfo.setApproveRecordId(CeqApproveBo1.getId());
							pageFlowControlInfo.setResCode(Constant.REVIEW_ASYN_OK);
							pageFlowControlInfo.setResMsg("异步审批提交成功！");
							log.info("webQueryFlowManager  result  = {} ", pageFlowControlInfo);
							return pageFlowControlInfo;
						}
					}
				}
				CeqPageFlowControlInfo haveLocalReport = isHaveLocalReport(CeqReportQueryBo, webQueryBo);
				if (null != CeqApproveBo1) {
					haveLocalReport.setApproveRecordId(CeqApproveBo1.getId());
				}
				if (sysncApproveFlag) {
					haveLocalReport.setApproveRecordId(pageFlowControlInfo.getApproveRecordId());
				}
				log.info("webQueryFlowManager  result  = {} ", haveLocalReport);
				return haveLocalReport;
			} else if (queryNextStep == ReportQueryStep.QUERYREPORT) {
				// 查询报告
				CeqReportQueryBo.setAccessSource("1");
				CeqReportQueryBo.setQueryType(0);
				CeqPageFlowControlInfo queryReportStepHandle = queryReportStepHandle(CeqReportQueryBo);
				log.info("webQueryFlowManager  result  = {} ", queryReportStepHandle);
				return queryReportStepHandle;
			} else if (queryNextStep == ReportQueryStep.QUERYLOCALREPORT) {
				// 查询本地报告
				CeqReportQueryBo.setAccessSource("1");
				// 获取查询策略
				String loacalDayStr = CeqConfigUtil.getLocalReportValidity();
				Integer loacalDay = Integer.valueOf(loacalDayStr);
				CeqReportQueryBo.setQueryType(0 - loacalDay);
				return queryReportStepHandle(CeqReportQueryBo);
			}
		} catch (Exception e) {
			log.error("webQueryFlowManager is error e = ", e);
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
			pageFlowControlInfo.setResCode(Constant.POLICY_RECHECK_E);
			pageFlowControlInfo.setResMsg("系统处理出现问题。请联系管理员");
			return pageFlowControlInfo;
		}
		return null;
	}

	/**
	 * 查询信用报告的处理
	 *
	 * @param CeqReportQueryBo
	 * @return
	 */
	private CeqPageFlowControlInfo queryReportStepHandle(CeqReportQueryBo CeqReportQueryBo) {
		// 查询前规则验证
		CeqQueryControlBo CeqQueryControlBo = buildCeqQueryControlBo(CeqReportQueryBo);
		CeqQueryControlBo.setInterface(false);
		CeqQueryControlBo.setPreValidate(false);
		CeqQueryControlBo queryControlBo = queryControlValidateService.queryExceptionRuleValidate(CeqQueryControlBo);
		String resCode = queryControlBo.getResCode();
		if (!StringUtils.equals(Constant.CHECK_SUCCESS, resCode)) {
			// 验证未通过,不进行查询.返回相应信息
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(queryControlBo);
			return pageFlowControlInfo;
		}
		// 验证通过调用查询服务进行查询
		CeqReportQueryBo.setAccessSource(ACCESS_SOURCE_WEB);
		CeqQueryReportFlowBo ceqQueryReportFlowBo = ceqQueryReportService.creditReportQuery(CeqReportQueryBo);
		
		/**
		 * 查询不成功的情况下，页面代码返回10999,弹出提示窗口。
		 */
		if(!Objects.equals(Constants.QUERY_SUCCESSCODE, ceqQueryReportFlowBo.getResCode())) {
			ceqQueryReportFlowBo.setResCode("10999");
		}
		CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqQueryReportFlowBo);
		String queryRecordId = ceqQueryReportFlowBo.getQueryRecordId();
		pageFlowControlInfo.setQueryRecordId(queryRecordId);
		return pageFlowControlInfo;
	}

	/**
	 * 判断本地是否存在历史报告，并进行相关信息处理
	 *
	 * @param CeqReportQueryBo
	 * @param webQueryBo
	 * @return
	 */
	private CeqPageFlowControlInfo isHaveLocalReport(CeqReportQueryBo CeqReportQueryBo,
													 CeqReportQueryBo.WebQueryBo webQueryBo) {
		String localDayStr = CeqConfigUtil.getLocalReportValidity();
		boolean haveLocalReport =  ceqQueryRecordService.isHaveLocalReport(CeqReportQueryBo.getSignCode(), CeqReportQueryBo.getQueryReasonId(),
				webQueryBo.getTopOrg(), localDayStr);
		if (haveLocalReport) {
			// 本地存在报告
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
			pageFlowControlInfo.setResCode(Constant.LOCAL_HAS_REPORT);
			pageFlowControlInfo.setResMsg("本地存在信用报告，是否进行展示?");
			return pageFlowControlInfo;
		}
		// 本地没有报告
		CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
		pageFlowControlInfo.setResCode(Constant.LOCAL_NO_REPORT);
		pageFlowControlInfo.setResMsg("本地无报告，进行查询。");
		return pageFlowControlInfo;
	}

	/**
	 * precheck步骤的处理
	 *
	 * @param CeqReportQueryBo
	 * @return
	 */
	private CeqPageFlowControlInfo precheckStepHandle(CeqReportQueryBo CeqReportQueryBo) {
		// 查询原因是否需要进行审批
		boolean isReasonNeedApprove = queryControlValidateService
				.isReasonNeedApprove(CeqReportQueryBo.getQueryReasonId(), CeqReportQueryBo.getTopOrgCode());
		if (isReasonNeedApprove) {
			// 需要审批
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
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
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
			pageFlowControlInfo.setNextStepPageUrl(DISPATCHER_URL);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.POLICY_RECHECK_N_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.POLICY_RECHECK_N_MSG);
			return pageFlowControlInfo;
		}
	}

	/**
	 * authorized步骤的相关处理 进行理，下一步进行授权认证或审批页面
	 *
	 * @param CeqReportQueryBo
	 * @return
	 */
	private CeqPageFlowControlInfo authorizedStepHandle(CeqReportQueryBo CeqReportQueryBo) {
		WebQueryBo webQueryBo = CeqReportQueryBo.getWebQueryBo();
		// 判断用户有无查询原因使用权限
		boolean isUserHaveQueryReasonPermission = queryControlValidateService
				.isUserHaveQueryReasonPermission(webQueryBo.getUserRoles(), CeqReportQueryBo.getQueryReasonId());
		if (!isUserHaveQueryReasonPermission) {
			// 用户无查询原因权限，返回相应提示信息
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.HAVE_NORIGHT_INQUIRE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.HAVE_NORIGHT_MSG);
			return pageFlowControlInfo;
		}
		// 根据用户名,查询原因判断是否有不使用授权档案及审批的绿色通道特权特权
		boolean isAuthorizePrivilegeUserForReason = queryControlValidateService.isPrivilegeUserForReason(
				CeqReportQueryBo.getOperator(), CpqPrivilegeType.AUTHORIZEPPROVAL, CeqReportQueryBo.getQueryReasonId());
		if (isAuthorizePrivilegeUserForReason) {
			// 特权用户，不需进行授权步骤。返回相应信息
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
			pageFlowControlInfo.setResCode(QueryFlowMannerConstant.GREEN_PASS_CODE);
			pageFlowControlInfo.setResMsg(QueryFlowMannerConstant.GREEN_PASS_MSG);
			return pageFlowControlInfo;
		}

		// 查询原因是否需要授权
		boolean isReasonNeedAuthorize = queryControlValidateService
				.isReasonNeedAuthorize(CeqReportQueryBo.getQueryReasonId(), CeqReportQueryBo.getTopOrgCode());
		if (isReasonNeedAuthorize) {
			// 需要授权
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(CeqReportQueryBo);
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
			return precheckStepHandle(CeqReportQueryBo);
		}
	}

	@Override
	public CeqQueryReportFlowBo interfaceQueryFlowManager(CeqReportQueryBo ceqReportQueryBo) {
		CeqQueryReportFlowBo CeqQueryReportFlowBo = new CeqQueryReportFlowBo();
		try {
			// 前置系统用户校验
			Map<String, String> validateQueryUser = validateQueryUser(ceqReportQueryBo.getOperator());
			log.debug("用户可用性校验结果为CeqSingleResult:{}", validateQueryUser);
			if (MapUtils.isNotEmpty(validateQueryUser)) {
				if(StringUtils.isNotBlank(ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				String resCode = (String) validateQueryUser.keySet().toArray()[0];
				CeqQueryReportFlowBo.setResCode(resCode);
				CeqQueryReportFlowBo.setResMsg(validateQueryUser.get(resCode));
				return CeqQueryReportFlowBo;
			}
			boolean lockedUser = ceqAlertService.isLockedUser(ceqReportQueryBo.getOperator());
			if (!lockedUser) {
				if(StringUtils.isNotBlank(ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				CeqQueryReportFlowBo.setResCode(CODE_USER_IS_LOCKED_BUSINESS);
				CeqQueryReportFlowBo.setResMsg(MSG_USER_IS_LOCKED_BUSINESS);
				return CeqQueryReportFlowBo;
			}

			CeqQueryControlBo queryExceptionRuleValidate = validateCanQuery(ceqReportQueryBo);
			// 进行规则阻断验证
			log.debug("interfaceQueryFlowManager validateCanQuery result queryExceptionRuleValidate = ",
					queryExceptionRuleValidate);
			if (!StringUtils.equals(Constant.CHECK_SUCCESS, queryExceptionRuleValidate.getResCode())) {
				if(StringUtils.isNotBlank(ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
					String msgNo = ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
					log.info("msgNo{}",msgNo);
					redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
				}
				// 验证未通过,不进行查询.返回相应信息
				CeqQueryReportFlowBo.setBoCommonField(queryExceptionRuleValidate);
				return CeqQueryReportFlowBo;
			}
			if (StringUtils.isNotEmpty(ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorType())) {
				CeqAuthorizeManagerBo ceqArchiveBo = new CeqAuthorizeManagerBo();
				ceqArchiveBo.setSignCode(ceqReportQueryBo.getSignCode());
				ceqArchiveBo.setOrgCreditCode(ceqReportQueryBo.getOrgCreditCode());
				ceqArchiveBo.setEnterpriseName(ceqReportQueryBo.getEnterpriseName());
				ceqArchiveBo.setOrgInstCode(ceqReportQueryBo.getOrgInstCode());

				ceqArchiveBo.setUniformSocialCredCode(ceqReportQueryBo.getUniformSocialCredCode());
				ceqArchiveBo.setGsRegiNo(ceqReportQueryBo.getGsRegiNo());
				ceqArchiveBo.setDsRegiNo(ceqReportQueryBo.getDsRegiNo());
				ceqArchiveBo.setFrgCorpNo(ceqReportQueryBo.getFrgCorpNo());
				ceqArchiveBo.setRegiTypeCode(ceqReportQueryBo.getRegiTypeCode());

				ceqArchiveBo.setCreatTime(new Date());
				ceqArchiveBo.setCreator(ceqReportQueryBo.getOperator());
				ceqArchiveBo.setExpireDate(ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorEndDate());
				ceqArchiveBo.setStartDate(ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorBeginDate());
				ceqArchiveBo.setStatus(STATUS_VALID);
				ceqArchiveBo.setOwnOrg(ceqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg());
				ceqArchiveBo.setOperOrg(ceqReportQueryBo.getInterfaceQueryParamsBo().getQueryOrg());
				ceqArchiveBo.setOperTime(new Date());
				ceqArchiveBo.setOperator(ceqReportQueryBo.getOperator());
				//授权类型转换
				String authorizeType = ceqReportQueryBo.getInterfaceQueryParamsBo().getAuthorType();
				ceqArchiveBo.setArchiveType(authorizeType);
				ceqArchiveBo.setReqId(ceqReportQueryBo.getInterfaceQueryParamsBo().getReqId());
				List<CeqAuthorizeFileBo> ceqArchivefileBoList = ceqReportQueryBo.getInterfaceQueryParamsBo()
						.getCeqArchivefileBoList();
				Long quantity = 0L;
				if (CollectionUtils.isNotEmpty(ceqArchivefileBoList)) {
					quantity = (long) ceqArchivefileBoList.size();
				}
				ceqArchiveBo.setQuantity(quantity);
				CeqAuthorizeManagerBo create = authorizeManagerService.create(ceqArchiveBo);
				String id = create.getId();
				// 将档案ID传入查询参数中，用于记录查询关联档案
				InterfaceQueryParams interfaceQueryParamsBo = ceqReportQueryBo.getInterfaceQueryParamsBo();
				interfaceQueryParamsBo.setAuthArchiveId(id);
				// 保存档案文件
				for (CeqAuthorizeFileBo CeqArchivefileBo : ceqArchivefileBoList) {
					CeqArchivefileBo.setArchiveId(id);
					CeqArchivefileBo.setAuthorizeFileMode(Constants.AUTHORIZEFILE_MODE_1);
					String fileType = CeqArchivefileBo.getFileType();
					fileType = fileTypeConversion(fileType);
					if (StringUtils.isNotBlank(fileType)) {
						String dicName = ceqDicService.getDicValue(ARCHIVE_TYPE, fileType);
						String suffixName = CeqArchivefileBo.getFileFoamt();
						if(StringUtils.isNotBlank(suffixName)) {
							dicName += SUFFIX_NAME_SPLIT + suffixName;
							CeqArchivefileBo.setFileName(dicName);
						}
					}

				}
				ceqAuthorizeFileService.authorizeFileInterface(ceqArchivefileBoList);
			}
			log.debug("创建电子档案end");

			String syncFlag = ceqReportQueryBo.getInterfaceQueryParamsBo().getSyncFlag();
			if (SYNCFLAGZERO.equals(syncFlag)) {// 同步
				ceqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
				CeqQueryReportFlowBo = queryReportStr(ceqReportQueryBo);
				return CeqQueryReportFlowBo;
			} else if (SYNCFLAGONE.equals(syncFlag)) {// 异步
				RabbitMQClient client = RabbitMqUtil.getClientByType(QUERY_TYPE_ENT);
				client.send(JSON.toJSONString(ceqReportQueryBo));
				CeqQueryReportFlowBo.setResCode(QueryFlowMannerConstant.CODE_WAIT_QUERY);
				CeqQueryReportFlowBo.setResMsg(QueryFlowMannerConstant.MSG_WAIT_QUERY);
				return CeqQueryReportFlowBo;
			}
		} catch (Exception e) {
			log.error("interfaceQueryFlowManager(CeqReportQueryBo CeqReportQueryBo{})出现异常{}", ceqReportQueryBo, e);
			if(StringUtils.isNotBlank(ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo())) {
				String msgNo = ceqReportQueryBo.getInterfaceQueryParamsBo().getMsgNo();
				log.info("msgNo{}",msgNo);
				redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
			}
			CeqQueryReportFlowBo.setResCode(QueryFlowMannerConstant.CODE_SYS_EXCEPTION);
			CeqQueryReportFlowBo.setResMsg(e.getLocalizedMessage());
			return CeqQueryReportFlowBo;

		}
		return CeqQueryReportFlowBo;
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
			if (fileType.equals(CeqConstants.FILE_TYPE_1)) {
				return CeqConstants.FILE_TYPE_101;
			} else if (fileType.equals(CeqConstants.FILE_TYPE_2)) {
				return CeqConstants.FILE_TYPE_102;
			} else if (fileType.equals(CeqConstants.FILE_TYPE_3)) {
				return CeqConstants.FILE_TYPE_103;
			} else if (fileType.equals(CeqConstants.FILE_TYPE_4)) {
				return CeqConstants.FILE_TYPE_105;
			} else if (fileType.equals(CeqConstants.FILE_TYPE_9)) {
				return CeqConstants.FILE_TYPE_200;
			}
		}
		return fileType;
	}




	@Override
	public CeqQueryReportFlowBo queryReportStr(CeqReportQueryBo CeqReportQueryBo) {
		log.info("queryReportStr param CeqReportQueryBo = ", CeqReportQueryBo);
		CeqQueryReportFlowBo ceqQueryReportFlowBo = ceqQueryReportService.creditReportQuery(CeqReportQueryBo);
		log.info("queryReportStr end,result = {}",ceqQueryReportFlowBo);
		return ceqQueryReportFlowBo;
	}

	@Override
	public CeqQueryRecordBo create(CeqQueryRecordBo CeqQueryRecordBo) {
		return ceqQueryRecordService.create(CeqQueryRecordBo);
	}

	@Override
	public CeqQueryRecordBo update(CeqQueryRecordBo CeqQueryRecordBo) {
		return ceqQueryRecordService.update(CeqQueryRecordBo);
	}

	@Override
	public CeqQueryRecordBo findCeqQueryRecordBoById(String id) {
		return ceqQueryRecordService.findById(id);
	}

	@Override
	public List<CeqQueryRecordBo> findAll(Map<String, Object> searchParams) {
		return ceqQueryRecordService.findAll(searchParams);
	}

	@Override
	public List<CeqQueryRecordBo> findByIds(List<String> ids) {
		return ceqQueryRecordService.findByIds(ids);
	}

	@Override
	public Page<CeqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
										  String direction, String orderBy) {
		return ceqQueryRecordService.getPage(searchParams, pageNumber, pageSize, direction, orderBy);
	}

	@Override
	public CeqQueryReportFlowBo getLocalReport(String signCode,String qryReason, String topOrgCode,String localDay) {
		CeqQueryReportFlowBo localReport = ceqQueryRecordService.getLocalReport(signCode, qryReason, topOrgCode, localDay);
		return localReport;
	}

	@Override
	public boolean isHaveLocalReport(String signCode,String qryReason, String topOrgCode,String localDay) {
		return ceqQueryRecordService.isHaveLocalReport(signCode, qryReason, topOrgCode, localDay);
	}

	@Override
	public String getReportById(String id, String reportType) {
		return ceqQueryRecordService.getReportById(id, reportType);
	}

	@Override
	public List<String> batchQueryReport(String batchFilePath) {
		return null;
	}

	@Override
	public CeqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds) {
		return ceqQueryRecordService.getArchiveShortcut(userName, deptCodes, menuIds);
	}

	@Override
	public void updateArchiveIdById(String id, String archiveId) {
		ceqQueryRecordService.updateArchiveIdById(id, archiveId);
	}

	@Override
	public boolean isQueriedCreditUser(String creditUser) {
		return ceqQueryRecordService.isQueriedCreditUser(creditUser);
	}

	/**
	 * 构建查询管控服务BO
	 *
	 * @param ceqReportQueryBo
	 * @return
	 */
	private CeqQueryControlBo buildCeqQueryControlBo(CeqReportQueryBo ceqReportQueryBo) {
		CeqQueryControlBo ceqQueryControlBo = new CeqQueryControlBo();
		CeqReportQueryBo.WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
		ClassCloneUtil.copyObject(ceqReportQueryBo,ceqQueryControlBo);
		ceqQueryControlBo.setQueryUserName(ceqReportQueryBo.getOperator());
		ceqQueryControlBo.setOrgCode(webQueryBo.getQueryOrg());
		ceqQueryControlBo.setQueryReason(ceqReportQueryBo.getQueryReasonId());
		ceqQueryControlBo.setBoCommonField(ceqReportQueryBo);
		return ceqQueryControlBo;
	}

	/**
	 * 构造审批业务对象
	 *
	 * @param ceqReportQueryBo
	 * @return
	 */
	private CeqApproveBo buildCeqApproveBo(CeqReportQueryBo ceqReportQueryBo) {
		CeqReportQueryBo.WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();

		CeqApproveBo approveBo = new CeqApproveBo();
		ClassCloneUtil.copyObject(ceqReportQueryBo,approveBo);
		approveBo.setArchiveId(webQueryBo.getAuthArchiveId());
		approveBo.setAssocbsnssData(webQueryBo.getRelationBaseData());

		approveBo.setCreditUser(webQueryBo.getCreditUser());
		approveBo.setOperator(ceqReportQueryBo.getOperator());
		approveBo.setOperOrg(webQueryBo.getQueryOrg());
		approveBo.setOperTime(new Date());
		approveBo.setQryReason(ceqReportQueryBo.getQueryReasonId());
		approveBo.setQueryFormat(webQueryBo.getReportVersion());
		approveBo.setQueryTime(new Date());
		approveBo.setRekType(webQueryBo.getApproveWay());
		approveBo.setRekUser(webQueryBo.getApproveUser());
		approveBo.setRekOrg(webQueryBo.getApproveOrg());
		CpqReportResultType reportResultType = webQueryBo.getReportResultType();
		approveBo.setResultType(reportResultType.getCode());
		approveBo.setQueryType("0");
		approveBo.setRekTime(new Date());
		approveBo.setBoCommonField(ceqReportQueryBo);
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
		Map<String, String> returnMap = new HashMap<>();
		try {
			// 进行查询员有效性校验
			returnMap = validateSystemUser(queryUser, returnMap);
			if(MapUtils.isNotEmpty(returnMap)){
				return returnMap;
			}
			// 获取征信用户
			CeqUserAttrBo userAttr = ceqUserAttrService.findByUserId(queryUser);
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

			CeqPbocUserBo creditUser = ceqPbocUserService.findByCreditName(creditName);
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
			returnMap.put(QueryFlowMannerConstant.CODE_NO_ROLE,
					QueryFlowMannerConstant.MSG_NO_ROLE);
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
	private CeqQueryControlBo validateCanQuery(CeqReportQueryBo queryBo) {
		log.info("validateCanQuery param queryBo = ", queryBo);
		CeqQueryControlBo ruleValidateBo = new CeqQueryControlBo();
		ClassCloneUtil.copyObject(queryBo,ruleValidateBo);
		ruleValidateBo.setQueryUserName(queryBo.getOperator());
		ruleValidateBo.setPreValidate(false);
		ruleValidateBo.setInterface(true);
		ruleValidateBo.setOrgCode(queryBo.getInterfaceQueryParamsBo().getQueryOrg());
		ruleValidateBo.setQueryReason(queryBo.getQueryReasonId());
		CeqQueryControlBo queryExceptionRuleValidate = queryControlValidateService
				.queryExceptionRuleValidate(ruleValidateBo);
		return queryExceptionRuleValidate;
	}

	@Override
	public String getReportById(String recordId, String userName, String orgCode, String reportType) {
		return ceqQueryRecordService.readReportById(recordId, userName, orgCode, reportType);
	}
}
