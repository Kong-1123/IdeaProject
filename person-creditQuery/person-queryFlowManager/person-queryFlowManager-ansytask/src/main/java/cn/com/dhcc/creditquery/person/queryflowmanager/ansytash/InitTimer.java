/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.ansytash;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeManagerService;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.queryapicommon.util.ZipUtils;


/**
 * 初始化启动定时器
 *
 * @author guoshihu
 * @author yuzhao.xue
 * @date 2019年2月22日
 */
@Component
public class InitTimer implements InitializingBean {
    private static Logger log = LoggerFactory.getLogger(InitTimer.class);

    private static RedissonClient redisson = RedissonUtil.getLocalRedisson();

    /**
     * 审核通过
     */
    private static final String AUDIT_OK = "3";
    /**
     * 处理状态0：已完成
     */
    private static final String STATUS_OK = "0";

    /**
     * 批量标识：单笔
     */
	protected static final String SINGLEFLAG = "1";

	/**
	 * 信用报告版本---二代信用报告
	 */
	private static final String	REPORTVERSION="2.0.0";
	
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
	
	public static String CODE_SUSSCE = "000000";
	public static String MSG_SUSSCE = "查询成功";
	
	public static String CODE_SYS_EXCEPTION = "000005";
	public static String MSG_SYS_EXCEPTION = "系统异常";
	
	public static String CODE_LOCAL_NO_REPORT = "000021";
	public static String MSG_LOCAL_NO_REPORT = "本地无报告";
	/**
     * 请求访问来源-interface
     */
    static final String ACCESS_SOURCE_INTERFACE = "2";

    /**
     * 分布式锁 key
     */
	protected static final String ANSYTASK_LOCK = "p_ansytask_lock";
	@Autowired
	private CpqSingleResultTaskDao cpqSingleResultTaskDao;
	@Autowired
	private CpqAuthorizeManagerService cpqAuthorizeManagerService;
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            log.info("init Timer start------------------");
            TimerTask task = new TimerTask() {
				@Override
                public void run() {
                    log.info("timertask-----------------start");
                    RLock lock = redisson.getLock(ANSYTASK_LOCK);
					if(lock.isLocked()) {
						log.info("timertask---locked");
						return;
					}
                    try {
                    	lock.lock();
						RList<String> auditList = redisson.getList(PersonCacheConstant.AUDIT_WAITQUERY);
						if (CollectionUtils.isNotEmpty(auditList)) {
						    List<String> removeList = new ArrayList<>();
							for (String reqId : auditList) {
						        CpqApproveBo cpqApproveBo = InitUtil.getCpqApproveFlowService().findCpqApproveByReqId(reqId);
						        if (cpqApproveBo != null && AUDIT_OK.equals(cpqApproveBo.getStatus())) {
						            CpqSingleQueryTask cpqSingleQuery = InitUtil.getCpqSingleQueryDao().findByReqId(reqId);
						            cpqSingleQuery.setStatus(STATUS_OK);
						            CpqReportQueryBo cpqReportQueryBo = new CpqReportQueryBo();
						            cpqReportQueryBo.setCertNo(cpqSingleQuery.getQuerierCertno());
						            cpqReportQueryBo.setCertType(cpqSingleQuery.getQuerierCertype());
						            cpqReportQueryBo.setClientName(cpqSingleQuery.getQuerierName());
						            cpqReportQueryBo.setOperator(cpqSingleQuery.getQueryUser());
						            cpqReportQueryBo.setQueryReasonId(cpqSingleQuery.getQueryReason());
						            cpqReportQueryBo.setQueryType(cpqSingleQuery.getQueryType());
						            cpqReportQueryBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(getQueryUserOrg(cpqSingleQuery.getQueryUser())));
						            cpqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
						            InterfaceQueryParams InterfaceQueryParams = cpqReportQueryBo.new InterfaceQueryParams();
						            InterfaceQueryParams.setBusinessLine(cpqSingleQuery.getBusinessLine());
						            InterfaceQueryParams.setSysCode(cpqSingleQuery.getSysCode());
						            InterfaceQueryParams.setClientIp(cpqSingleQuery.getUserIp());//TODO 请求客户端IP 取得什么ip待确认。
						            InterfaceQueryParams.setCallSysUser(cpqSingleQuery.getCallSysUser());
						            InterfaceQueryParams.setRecheckUser(cpqSingleQuery.getRecheckUser());
						            InterfaceQueryParams.setUserIp(cpqSingleQuery.getUserIp());
						            InterfaceQueryParams.setUserMac(cpqSingleQuery.getUserMac());
						            InterfaceQueryParams.setReqId(cpqSingleQuery.getReqId());
						            InterfaceQueryParams.setSyncFlag(cpqSingleQuery.getSyncFlag());
						            InterfaceQueryParams.setAsyncQueryFlag(cpqSingleQuery.getSyncFlag());
						            InterfaceQueryParams.setApproveId(cpqApproveBo.getId());
						            InterfaceQueryParams.setApproveWay(cpqApproveBo.getRekType());
						            InterfaceQueryParams.setApproveOrg(cpqApproveBo.getRekOrg());
						            InterfaceQueryParams.setApproveTime(cpqApproveBo.getRekTime());
						            InterfaceQueryParams.setApproveUser(cpqApproveBo.getRekUser());
						            InterfaceQueryParams.setBatchFlag(SINGLEFLAG);
						            InterfaceQueryParams.setReportType(cpqSingleQuery.getReportType());
						            InterfaceQueryParams.setQueryOrg(getQueryUserOrg(cpqSingleQuery.getQueryUser()));
						            InterfaceQueryParams.setRecheckUser(cpqSingleQuery.getRecheckUser());
						            InterfaceQueryParams.setQueryFormat(cpqSingleQuery.getQueryFormat());
						            CpqArchiveBo findByReqId = cpqAuthorizeManagerService.findByReqId(reqId);
						            if(findByReqId!=null) {
						            	InterfaceQueryParams.setAuthArchiveId(findByReqId.getId());
						            }
						            cpqReportQueryBo.setInterfaceQueryParamsBo(InterfaceQueryParams);
						            CpqQueryReportFlowBo queryReportStr = InitUtil.getCpqFlowManageService().queryReportStr(cpqReportQueryBo);
						            CpqSingleResultTask singleResult = new CpqSingleResultTask();
									
						            if (!Constants.QUERY_SUCCESSCODE.equals(queryReportStr.getResCode())) {
										singleResult.setReqId(reqId);
										singleResult.setReportVersion(REPORTVERSION);
										if(Constants.QUERY_LOCALNOTREPORTCODE.equals(queryReportStr.getResCode())) {
											singleResult.setResCode(CODE_LOCAL_NO_REPORT);
											singleResult.setResMsg(MSG_LOCAL_NO_REPORT);
										}else {
											singleResult.setResCode(queryReportStr.getResCode());
											singleResult.setResMsg(queryReportStr.getResMsg());
										}
										singleResult.setCreditreportNo(queryReportStr.getQueryRecordId());
										singleResult.setReportSource(queryReportStr.getReportSource());
										singleResult.setUseTime(queryReportStr.getUseTime());
										singleResult.setMsgNo(InterfaceQueryParams.getMsgNo());
									} else {
										singleResult.setReqId(reqId);
										singleResult.setReportVersion(REPORTVERSION);
										singleResult.setResCode(CODE_SUSSCE);
										singleResult.setResMsg(MSG_SUSSCE);
										singleResult.setCreditreportNo(queryReportStr.getQueryRecordId());
										singleResult.setReportSource(queryReportStr.getReportSource());
										singleResult.setUseTime(queryReportStr.getUseTime());
										singleResult.setMsgNo(InterfaceQueryParams.getMsgNo());
									}
									changeReport(singleResult, cpqSingleQuery.getReportType(), queryReportStr);
									saveReport(singleResult, cpqSingleQuery.getReportType(), queryReportStr);
						            InitUtil.getCpqSingleQueryDao().save(cpqSingleQuery);
						            removeList.add(reqId);
						        }
						    }
							auditList.removeAll(removeList);
						} else {
						    long resetFlag = redisson.getAtomicLong(PersonCacheConstant.ISRESET).get();
						    //如果值为0，则说明redis重启过，需要重新查询CpqSingleQuery表中未审核通过的记录；值不为0时不进行操作
						    if (resetFlag == 0) {
						        List<CpqSingleQueryTask> auditingData = InitUtil.getCpqSingleQueryDao().findAuditingData();
						        for (CpqSingleQueryTask singleQuery : auditingData) {
						            auditList.add(singleQuery.getReqId());
						        }
						        redisson.getAtomicLong(PersonCacheConstant.ISRESET).incrementAndGet();
						    }
						}
					} catch (Exception e) {
						log.error("timertask exception:{}",e);
					}finally {
						lock.unlock();
					}
                }
            };
            Timer timer = new Timer();
            long delay = 2 * 60 * 1000;                    //延迟0秒后执行
            long intevalPeriod = 5 * 60 * 1000;  //5分钟执行一次

            timer.scheduleAtFixedRate(task, delay, intevalPeriod);
            log.info("init Timer stop------------------");
        } catch (Exception e) {
            log.error("定时器启动异常", e);
        }
    }
    
    /**
	 * 根据请求中的格式要求转换信用报告
	 * 
	 * @param singleResult
	 * @param reportType
	 * @param reportbo
	 * @author guoshihu
	 * @date 2019年1月22日
	 */
	public void changeReport(CpqSingleResultTask singleResult, String reportType, CpqQueryReportFlowBo cpqQueryReportFlowBo) {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		try {
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
				message=cpqQueryReportFlowBo.getXmlCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setXmlStr(zipMessage);
					singleResult.setXmlMd5(md5Message);
				}
			}
			if (reportType.contains(REPORTTYPE_PDF)) {
				message=cpqQueryReportFlowBo.getPdfCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setPdfStr(zipMessage);
					singleResult.setPdfMd5(md5Message);
				}
			}
			if (reportType.contains(REPORTTYPE_JSON)) {
				message=cpqQueryReportFlowBo.getJsonCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setJsonStr(zipMessage);
					singleResult.setJsonMd5(md5Message);
				}
			}
		} catch (Exception e) {
			log.error("changeReport(CpqSingleResult singleResult, String reportType, CpqQueryReportFlowBo cpqQueryReportFlowBo{})exception{}",cpqQueryReportFlowBo,e);
			singleResult.setResCode(CODE_SYS_EXCEPTION);
			singleResult.setResMsg(MSG_SYS_EXCEPTION);
		}
	}
	/**
	 * 保存请求结果
	 * @param singleResult
	 * @param reportType
	 * @param cpqQueryReportFlowBo
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
	@Transactional
	public void saveReport(CpqSingleResultTask singleResult, String reportType, CpqQueryReportFlowBo cpqQueryReportFlowBo) {
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
		cpqSingleResultTaskDao.save(singleResult);
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
}
