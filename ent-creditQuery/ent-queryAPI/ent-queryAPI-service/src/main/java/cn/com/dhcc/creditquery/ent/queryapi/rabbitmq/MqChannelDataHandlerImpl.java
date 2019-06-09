/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.rabbitmq;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;

import cn.com.dhcc.credit.platform.util.ExecutorUtil;
import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.queryapi.common.EntConstant;
import cn.com.dhcc.creditquery.ent.queryapi.dao.CeqSingleResultDao;
import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleResult;
import cn.com.dhcc.creditquery.ent.queryapi.task.CeqApproveSaveTask;
import cn.com.dhcc.creditquery.ent.queryapproveflow.entity.CeqApprove;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.informationplatform.amqp.consumer.handler.MQChannelDataHandler;
import cn.com.dhcc.query.creditquerycommon.cache.constant.EntCacheConstant;
import cn.com.dhcc.query.creditquerycommon.util.CeqConstants;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.HtmlInWatermarkStyle;
import cn.com.dhcc.query.queryapicommon.util.ZipUtils;

/**
 * mq数据处理类
 * @author yuzhao.xue
 * @date 2019年1月17日
 */
@Component
public class MqChannelDataHandlerImpl implements MQChannelDataHandler{
	/**
	 * 默认信用报告版式---授信机构版
	 */
	private static final String DEFAULT_QUERYFORMAT = "00";
	/**
	 * 异步需要审批
	 */
	private static final String ASYNCAUDIT = "0";
	/**
	 * 查询类型 0-非数字解读
	 */
	private static final String QUERYTYPE_NOTNUMBER="0";
	
	/**
	 * 同步异步标识：异步
	 */
	private static final String SYNCFLAGONE = "1";
	
	/**
	 * 审批状态--待审批
	 */
	private final static String PENDING_REVIEW = "1";
	
	/**
	 * 批量标识：单笔
	 */
	private static final String SINGLEFLAG = "1"; 
	
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
	
	/**
	 * 批量标识：批量
	 */
	private static final String BATCHEFLAG = "2"; 
	
	/**
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter_e";
	
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	
	@Autowired
	private CeqSingleResultDao ceqSingleResultDao;
	
	private static Logger log = LoggerFactory.getLogger(MqChannelDataHandlerImpl.class);
	/* (non-Javadoc)
	 * @see cn.com.dhcc.informationplatform.amqp.consumer.handler.MQChannelDataHandler#processForBasicAck(java.lang.String, com.rabbitmq.client.Channel, long)
	 */
	@Override
	public void processForBasicAck(String message, Channel channel, long deliveryTag) throws Exception {
		log.info("MqChannelDataHandlerImpl---processForBasicAck param deliveryTag = {}", deliveryTag);
		CeqReportQueryBo queryBo=null;
		try {
			queryBo = JSON.parseObject(message, CeqReportQueryBo.class);
			if(ASYNCAUDIT.equals(queryBo.getInterfaceQueryParamsBo().getAsyncQueryFlag())){
				RList<String> auditList = redisson.getList(EntCacheConstant.AUDIT_WAITQUERY);
				String reqId=queryBo.getInterfaceQueryParamsBo().getReqId();
				auditList.add(reqId);
				CeqApprove cpqApprove=getCeqApprove(queryBo);
				CeqApproveSaveTask cpqApproveSaveTask = new CeqApproveSaveTask();
				cpqApproveSaveTask.setCeqApprove(cpqApprove);
				log.info("MqChannelDataHandlerImpl---processForBasicAck executor.submit CpqApproveSaveTask deliveryTag = {}", deliveryTag);
				ExecutorUtil.executor.submit(cpqApproveSaveTask);
			}else{
				CeqQueryReportFlowBo queryReportStr = ceqFlowManageService.queryReportStr(queryBo);
				
				CeqSingleResult singleResult = new CeqSingleResult();
				if (!Constants.QUERY_SUCCESSCODE.equals(queryReportStr.getResCode())) {
					singleResult.setReqId(queryBo.getInterfaceQueryParamsBo().getReqId());
					singleResult.setReportVersion(REPORTVERSION);
					if(Constants.QUERY_LOCALNOTREPORTCODE.equals(queryReportStr.getResCode())) {
						singleResult.setResCode(EntConstant.CODE_LOCAL_NO_REPORT);
						singleResult.setResMsg(EntConstant.MSG_LOCAL_NO_REPORT);
					}else {
						singleResult.setResCode(queryReportStr.getResCode());
						singleResult.setResMsg(queryReportStr.getResMsg());
						
					}
					singleResult.setCreditreportNo(queryReportStr.getQueryRecordId());
					singleResult.setReportSource(queryReportStr.getReportSource());
					singleResult.setUseTime(queryReportStr.getUseTime());
					singleResult.setMsgNo(queryBo.getInterfaceQueryParamsBo().getMsgNo());
				} else {
					singleResult.setReqId(queryBo.getInterfaceQueryParamsBo().getReqId());
					singleResult.setReportVersion(REPORTVERSION);
					singleResult.setResCode(EntConstant.CODE_SUSSCE);
					singleResult.setResMsg(EntConstant.MSG_SUSSCE);
					singleResult.setCreditreportNo(queryReportStr.getQueryRecordId());
					singleResult.setReportSource(queryReportStr.getReportSource());
					singleResult.setUseTime(queryReportStr.getUseTime());
					singleResult.setMsgNo(queryBo.getInterfaceQueryParamsBo().getMsgNo());
				}
				changeReport(singleResult, queryBo, queryReportStr);
				saveReport(singleResult, queryBo.getInterfaceQueryParamsBo().getReportType(), queryReportStr);
			}
		} catch (Exception e) {
			log.error("MqChannelDataHandlerImpl---processForBasicAck(String {}, Channel channel, long deliveryTag = {} )异常:{}",message,deliveryTag,e);
		}finally{
			log.info("MqChannelDataHandlerImpl---processForBasicAck  basicAck message deliveryTag = {}", deliveryTag);
			if(queryBo !=null &&BATCHEFLAG.equals(queryBo.getInterfaceQueryParamsBo().getBatchFlag())) {
				String msgNo = queryBo.getInterfaceQueryParamsBo().getMsgNo();
				log.info("msgNo{}",msgNo);
				redisson.getAtomicLong(msgNo+COUNTERSUFFIX).incrementAndGet();
			}
			channel.basicAck(deliveryTag, false);
		}
	}
	/**
	 * @param queryBo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月27日
	 */
	private CeqApprove getCeqApprove(CeqReportQueryBo queryBo) {
		CeqApprove ceqApprove = new CeqApprove(); 
		
		ceqApprove.setReqId(queryBo.getInterfaceQueryParamsBo().getReqId());
		ceqApprove.setArchiveId(queryBo.getInterfaceQueryParamsBo().getAuthArchiveId());
		ceqApprove.setAssocbsnssData(queryBo.getInterfaceQueryParamsBo().getBussid());
		ceqApprove.setSignCode(queryBo.getSignCode());
		ceqApprove.setOrgCreditCode(queryBo.getOrgCreditCode());
		ceqApprove.setEnterpriseName(queryBo.getEnterpriseName());
		ceqApprove.setOrgInstCode(queryBo.getOrgInstCode());
		ceqApprove.setUniformSocialCredCode(queryBo.getUniformSocialCredCode());
		ceqApprove.setGsRegiNo(queryBo.getGsRegiNo());
		ceqApprove.setDsRegiNo(queryBo.getDsRegiNo());
		ceqApprove.setFrgCorpNo(queryBo.getFrgCorpNo());
		ceqApprove.setRegiTypeCode(queryBo.getRegiTypeCode());
//		cpqApprove.setCreditUser(queryReq.getCreditUser());
		ceqApprove.setOperator(queryBo.getOperator());
		ceqApprove.setOperOrg(queryBo.getInterfaceQueryParamsBo().getQueryOrg());
		ceqApprove.setOperTime(new Date());
		ceqApprove.setQryReason(queryBo.getQueryReasonId());
		ceqApprove.setQueryFormat(StringUtils.isBlank(queryBo.getInterfaceQueryParamsBo().getQueryFormat()) ? DEFAULT_QUERYFORMAT : queryBo.getInterfaceQueryParamsBo().getQueryFormat());
		ceqApprove.setQueryTime(new Date());
		ceqApprove.setQueryType(QUERYTYPE_NOTNUMBER);
		ceqApprove.setRekType(SYNCFLAGONE);
		String recheckUser = queryBo.getInterfaceQueryParamsBo().getRecheckUser();
		ceqApprove.setRekUser(recheckUser);
//		cpqApprove.setRekOrg(getQueryUserOrg(recheckUser));
		ceqApprove.setRekTime(new Date());
		ceqApprove.setClientIp(queryBo.getInterfaceQueryParamsBo().getClientIp());
		ceqApprove.setStatus(PENDING_REVIEW);
//		cpqApprove.setSource(queryReq.getSource());
		ceqApprove.setResultType(queryBo.getInterfaceQueryParamsBo().getReportType());
		return ceqApprove;
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
	public void changeReport(CeqSingleResult singleResult, CeqReportQueryBo queryBo, CeqQueryReportFlowBo ceqQueryReportFlowBo) {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		String reportType = queryBo.getInterfaceQueryParamsBo().getReportType();
		try {
			if (reportType.contains(REPORTTYPE_HTML)) {
				message=ceqQueryReportFlowBo.getHtmlCreditReport();
				
				//组装html，添加样式和水印
				message = HtmlInWatermarkStyle.ceqHtmlInWatermarkStyle(message, queryBo.getOperator(), 
						queryBo.getInterfaceQueryParamsBo().getQueryOrg(), CeqConstants.CEQ_WATERMARK_STYLE);
				
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
			log.error("changeReport(CeqSingleResult singleResult, String reportType, CeqQueryReportFlowBo ceqQueryReportFlowBo{})exception{}",ceqQueryReportFlowBo,e);
			singleResult.setResCode(EntConstant.CODE_SYS_EXCEPTION);
			singleResult.setResMsg(EntConstant.MSG_SYS_EXCEPTION);
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
}
