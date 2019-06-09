/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.ansytash;

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
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.InterfaceQueryParams;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
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
	protected static final String ANSYTASK_LOCK = "e_ansytask_lock";
	@Autowired
	private CeqSingleResultTaskDao ceqSingleResultTaskDao;
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
						RList<String> auditList = redisson.getList(EntCacheConstant.AUDIT_WAITQUERY);
						if (CollectionUtils.isNotEmpty(auditList)) {
						    List<String> removeList = new ArrayList<>();
							for (String reqId : auditList) {
						        CeqApproveBo ceqApproveBo = InitUtil.getCeqApproveFlowService().findCeqApproveByReqId(reqId);
						        if (ceqApproveBo != null && AUDIT_OK.equals(ceqApproveBo.getStatus())) {
						            CeqSingleQueryTask ceqSingleQuery = InitUtil.getCeqSingleQueryDao().findByReqId(reqId);
						            ceqSingleQuery.setStatus(STATUS_OK);
						            CeqReportQueryBo ceqReportQueryBo = new CeqReportQueryBo();
						    		//中征码
									ceqReportQueryBo.setSignCode(ceqSingleQuery.getSignCode());
						    		//统一社会信用代码
									ceqReportQueryBo.setUniformSocialCredCode(ceqSingleQuery.getUniformSocialCredCode());
						    		//组织机构代码
									ceqReportQueryBo.setOrgInstCode(ceqSingleQuery.getOrgInstCode());
						    		//纳税人识别号（国税）
									ceqReportQueryBo.setGsRegiNo(ceqSingleQuery.getGsRegiNo());
						    		//纳税人识别号（地税）
									ceqReportQueryBo.setDsRegiNo(ceqSingleQuery.getDsRegiNo());
						            
						            ceqReportQueryBo.setEnterpriseName(ceqSingleQuery.getEnterpriseName());
						            ceqReportQueryBo.setOperator(ceqSingleQuery.getQueryUser());
						            ceqReportQueryBo.setQueryReasonId(ceqSingleQuery.getQueryReason());
						            ceqReportQueryBo.setQueryType(ceqSingleQuery.getQueryType());
						            ceqReportQueryBo.setTopOrgCode(UserUtilsForConfig.getRootDeptCode(getQueryUserOrg(ceqSingleQuery.getQueryUser())));
						            ceqReportQueryBo.setAccessSource(ACCESS_SOURCE_INTERFACE);
						            InterfaceQueryParams InterfaceQueryParams = ceqReportQueryBo.new InterfaceQueryParams();
						            InterfaceQueryParams.setBusinessLine(ceqSingleQuery.getBusinessLine());
						            InterfaceQueryParams.setSysCode(ceqSingleQuery.getSysCode());
						            InterfaceQueryParams.setClientIp(ceqSingleQuery.getUserIp());//TODO 请求客户端IP 取得什么ip待确认。
						            InterfaceQueryParams.setCallSysUser(ceqSingleQuery.getCallSysUser());
						            InterfaceQueryParams.setRecheckUser(ceqSingleQuery.getRecheckUser());
						            InterfaceQueryParams.setUserIp(ceqSingleQuery.getUserIp());
						            InterfaceQueryParams.setUserMac(ceqSingleQuery.getUserMac());
						            InterfaceQueryParams.setReqId(ceqSingleQuery.getReqId());
						            InterfaceQueryParams.setSyncFlag(ceqSingleQuery.getSyncFlag());
						            InterfaceQueryParams.setAsyncQueryFlag(ceqSingleQuery.getSyncFlag());
						            InterfaceQueryParams.setApproveId(ceqApproveBo.getId());
						            InterfaceQueryParams.setApproveWay(ceqApproveBo.getRekType());
						            InterfaceQueryParams.setApproveOrg(ceqApproveBo.getRekOrg());
						            InterfaceQueryParams.setApproveTime(ceqApproveBo.getRekTime());
						            InterfaceQueryParams.setApproveUser(ceqApproveBo.getRekUser());
						            InterfaceQueryParams.setBatchFlag(SINGLEFLAG);
						            InterfaceQueryParams.setReportType(ceqSingleQuery.getReportType());
						            InterfaceQueryParams.setQueryOrg(getQueryUserOrg(ceqSingleQuery.getQueryUser()));
						            InterfaceQueryParams.setRecheckUser(ceqSingleQuery.getRecheckUser());
						            InterfaceQueryParams.setQueryFormat(ceqSingleQuery.getQueryFormat());
						            InterfaceQueryParams.setAuthArchiveId(ceqApproveBo.getArchiveId());
						            
						            ceqReportQueryBo.setInterfaceQueryParamsBo(InterfaceQueryParams);
						            CeqQueryReportFlowBo queryReportStr = InitUtil.getCeqFlowManageService().queryReportStr(ceqReportQueryBo);
						            CeqSingleResultTask singleResult = new CeqSingleResultTask();
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
									changeReport(singleResult, ceqSingleQuery.getReportType(), queryReportStr);
									saveReport(singleResult, ceqSingleQuery.getReportType(), queryReportStr);
						            InitUtil.getCeqSingleQueryDao().save(ceqSingleQuery);
						            removeList.add(reqId);
						        }
						    }
							auditList.removeAll(removeList);
						} else {
						    long resetFlag = redisson.getAtomicLong(EntCacheConstant.ISRESET).get();
						    //如果值为0，则说明redis重启过，需要重新查询CpqSingleQuery表中未审核通过的记录；值不为0时不进行操作
						    if (resetFlag == 0) {
						        List<CeqSingleQueryTask> auditingData = InitUtil.getCeqSingleQueryDao().findAuditingData();
						        for (CeqSingleQueryTask singleQuery : auditingData) {
						            auditList.add(singleQuery.getReqId());
						        }
						        redisson.getAtomicLong(EntCacheConstant.ISRESET).incrementAndGet();
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
	public void changeReport(CeqSingleResultTask singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo) {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		try {
			if (reportType.contains(REPORTTYPE_HTML)) {
				message=ceqQueryReportFlowBo.getHtmlCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setHtmlStr(zipMessage);
					singleResult.setHtmlMd5(md5Message);
				}
			}
			if (reportType.contains(REPORTTYPE_XML)) {
				message=ceqQueryReportFlowBo.getXmlCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setXmlStr(zipMessage);
					singleResult.setXmlMd5(md5Message);
				}
			}
			if (reportType.contains(REPORTTYPE_PDF)) {
				message=ceqQueryReportFlowBo.getPdfCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setPdfStr(zipMessage);
					singleResult.setPdfMd5(md5Message);
				}
			}
			if (reportType.contains(REPORTTYPE_JSON)) {
				message=ceqQueryReportFlowBo.getJsonCreditReport();
				if (StringUtils.isNotBlank(message)) {
					zipMessage = ZipUtils.gzip(message);
					md5Message = MD5.getMd5String(zipMessage);
					singleResult.setJsonStr(zipMessage);
					singleResult.setJsonMd5(md5Message);
				}
			}
		} catch (Exception e) {
			log.error("changeReport(CpqSingleResult singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo{})exception{}",ceqQueryReportFlowBo,e);
			singleResult.setResCode(CODE_SYS_EXCEPTION);
			singleResult.setResMsg(MSG_SYS_EXCEPTION);
		}
	}
	/**
	 * 保存请求结果
	 * @param singleResult
	 * @param reportType
	 * @param ceqQueryReportFlowBo
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
	@Transactional
	public void saveReport(CeqSingleResultTask singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo) {
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
		ceqSingleResultTaskDao.save(singleResult);
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
