/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.rabbitmq;

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
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.queryapi.common.PersonConstant;
import cn.com.dhcc.creditquery.person.queryapi.dao.CpqSingleResultDao;
import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleResult;
import cn.com.dhcc.creditquery.person.queryapi.task.CpqApproveSaveTask;
import cn.com.dhcc.creditquery.person.queryapproveflow.entity.CpqApprove;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.informationplatform.amqp.consumer.handler.MQChannelDataHandler;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
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
	 * 批量标识：批量
	 */
	private static final String BATCHEFLAG = "2"; 
	
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
	 * 计数器后缀
	 */
	private static final String COUNTERSUFFIX = "_counter";
	
	private static RedissonClient redisson = RedissonUtil.getLocalRedisson();
	
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	
	@Autowired
	private CpqSingleResultDao cpqSingleResultDao;
	
	private static Logger log = LoggerFactory.getLogger(MqChannelDataHandlerImpl.class);
	/* (non-Javadoc)
	 * @see cn.com.dhcc.informationplatform.amqp.consumer.handler.MQChannelDataHandler#processForBasicAck(java.lang.String, com.rabbitmq.client.Channel, long)
	 */
	@Override
	public void processForBasicAck(String message, Channel channel, long deliveryTag) throws Exception {
		log.info("MqChannelDataHandlerImpl---processForBasicAck param deliveryTag = ", deliveryTag);
		CpqReportQueryBo queryBo=null;
		try {
			queryBo = JSON.parseObject(message, CpqReportQueryBo.class);
			if(ASYNCAUDIT.equals(queryBo.getInterfaceQueryParamsBo().getAsyncQueryFlag())){
				RList<String> auditList = redisson.getList(PersonCacheConstant.AUDIT_WAITQUERY);
				String reqId=queryBo.getInterfaceQueryParamsBo().getReqId();
				auditList.add(reqId);
				CpqApprove cpqApprove=getCpqApprove(queryBo);
				CpqApproveSaveTask cpqApproveSaveTask = new CpqApproveSaveTask();
				cpqApproveSaveTask.setCpqApprove(cpqApprove);
				log.info("MqChannelDataHandlerImpl---processForBasicAck executor.submit CpqApproveSaveTask deliveryTag = ", deliveryTag);
				ExecutorUtil.executor.submit(cpqApproveSaveTask);
			}else{
				CpqQueryReportFlowBo queryReportStr = cpqFlowManageService.queryReportStr(queryBo);
				CpqSingleResult singleResult = new CpqSingleResult();
				if (!Constants.QUERY_SUCCESSCODE.equals(queryReportStr.getResCode())) {
					singleResult.setReqId(queryBo.getInterfaceQueryParamsBo().getReqId());
					singleResult.setReportVersion(REPORTVERSION);
					if(Constants.QUERY_LOCALNOTREPORTCODE.equals(queryReportStr.getResCode())) {
						singleResult.setResCode(PersonConstant.CODE_LOCAL_NO_REPORT);
						singleResult.setResMsg(PersonConstant.MSG_LOCAL_NO_REPORT);
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
					singleResult.setResCode(PersonConstant.CODE_SUSSCE);
					singleResult.setResMsg(PersonConstant.MSG_SUSSCE);
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
			log.info("MqChannelDataHandlerImpl---processForBasicAck  basicAck message deliveryTag = ", deliveryTag);
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
	private CpqApprove getCpqApprove(CpqReportQueryBo queryBo) {
		CpqApprove cpqApprove = new CpqApprove(); 
		
		cpqApprove.setReqId(queryBo.getInterfaceQueryParamsBo().getReqId());
		cpqApprove.setArchiveId(queryBo.getInterfaceQueryParamsBo().getAuthArchiveId());
		cpqApprove.setAssocbsnssData(queryBo.getInterfaceQueryParamsBo().getBussid());
		cpqApprove.setClientName(queryBo.getClientName());
		cpqApprove.setCertNo(queryBo.getCertNo());
		cpqApprove.setCertType(queryBo.getCertType());
//		cpqApprove.setCreditUser(queryReq.getCreditUser());
		cpqApprove.setOperator(queryBo.getOperator());
		cpqApprove.setOperOrg(queryBo.getInterfaceQueryParamsBo().getQueryOrg());
		cpqApprove.setOperTime(new Date());
		cpqApprove.setQryReason(queryBo.getQueryReasonId());
		cpqApprove.setQueryFormat(StringUtils.isBlank(queryBo.getInterfaceQueryParamsBo().getQueryFormat()) ? DEFAULT_QUERYFORMAT : queryBo.getInterfaceQueryParamsBo().getQueryFormat());
		cpqApprove.setQueryTime(new Date());
		cpqApprove.setQueryType(QUERYTYPE_NOTNUMBER);
		cpqApprove.setRekType(SYNCFLAGONE);
		String recheckUser = queryBo.getInterfaceQueryParamsBo().getRecheckUser();
		cpqApprove.setRekUser(recheckUser);
//		cpqApprove.setRekOrg(getQueryUserOrg(recheckUser));
		cpqApprove.setRekTime(new Date());
		cpqApprove.setClientIp(queryBo.getInterfaceQueryParamsBo().getClientIp());
		cpqApprove.setStatus(PENDING_REVIEW);
//		cpqApprove.setSource(queryReq.getSource());
		cpqApprove.setResultType(queryBo.getInterfaceQueryParamsBo().getReportType());
		return cpqApprove;
	}

	/**
	 * 根据请求中的格式要求转换信用报告
	 * 
	 * @param singleResult
	 * @param reportType
	 * @param cpqQueryReportFlowBo
	 * @author guoshihu
	 * @throws IOException 
	 * @date 2019年1月22日
	 */
	public void changeReport(CpqSingleResult singleResult, CpqReportQueryBo queryBo, CpqQueryReportFlowBo cpqQueryReportFlowBo) {
		String message = "";
		String zipMessage = "";
		String md5Message = "";
		String reportType = queryBo.getInterfaceQueryParamsBo().getReportType();
		try {
			if (reportType.contains(REPORTTYPE_HTML)) {
				message=cpqQueryReportFlowBo.getHtmlCreditReport();
				//组装html，添加样式和水印
				message = HtmlInWatermarkStyle.cpqHtmlInWatermarkStyle(message, queryBo.getOperator(), 
						queryBo.getInterfaceQueryParamsBo().getQueryOrg(), Constants.CPQ_WATERMARK_STYLE);
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
			singleResult.setResCode(PersonConstant.CODE_SYS_EXCEPTION);
			singleResult.setResMsg(PersonConstant.MSG_SYS_EXCEPTION);
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
}
