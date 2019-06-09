/**
 *  Copyright (c)  @date 2018年9月13日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*

package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.task;

import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.ResourceManager;
import cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean.BatchRequestQueryResultVo;
import cn.com.dhcc.creditquery.person.queryweb.util.WebApplicationContextUtil;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;
import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
import cn.com.dhcc.query.creditpersonquerydao.entity.userarr.CpqUserAttr;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.http.InquireService;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.info.ResponseInfo;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.CpqBatchquerydetailService;
import cn.com.dhcc.query.creditpersonqueryservice.ccuser.service.CpqCcUserService;
import cn.com.dhcc.query.creditpersonqueryservice.policy.legalcheck.CpqLegalCheckService;
import cn.com.dhcc.query.creditpersonqueryservice.policy.utils.InConstant;
import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.ExceptionRuleValidateVo;
import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.LegalReview;
import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.QueryReq;
import cn.com.dhcc.query.creditpersonqueryservice.userattr.service.util.UserAttrUtil;
import cn.com.dhcc.query.creditquerycommon.Constant;

*/
/**
 * 一代批量请求信用报告查询工作线程
 * @author lekang.liu
 * @date 2018年9月13日
 *//*

public class GenerationBatchRequestQueryTask implements Callable<BatchRequestQueryResultVo>{
	
	private static Logger log = LoggerFactory.getLogger(GenerationBatchFilePretreatmentTask.class);
	
	private CpqBatchquerydetail detail;
	
	private InquireService inquireService;
	
	private CpqBatchquerydetailService detailService;
	
	private CpqCcUserService  ccUserService;
	
	private CpqLegalCheckService cpqLegalCheckService;
	
	private String resultType;
	
	private int size = 0;
	
	private static final String TRUE = "true";
	private static final String ERROR = "error";
	
	public GenerationBatchRequestQueryTask(CpqBatchquerydetail detail,String resultType,int size) {
		this.resultType = resultType;
		this.detail = detail;
		this.size = size;
	}

	@Override
	public BatchRequestQueryResultVo call() throws Exception {
		
		try {
			log.info("GenerationBatchRequestQueryTask is run detail = ", detail);
			init();

			ResponseInfo inquireCreditReport;
			//  将detail转换为queryReq
			QueryReq queryReq = detailToQueryReq(detail);
			
			queryReq.setResultType(resultType);
			queryReq.setBatchFlag(InConstant.BATCH_FLAG);
			
			//获取征信用户
			CpqUserAttr userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(detail.getOperator());
			if (null == userAttr */
/*用户未配置属性*//*
) {
				detail.setStatus("3");
				detail.setErrorCode("3");
				detail.setErrorMsg("用户未设置用户属性");
				detailService.update(detail);
				BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("3").batchQueryDetailId(detail.getId()).build();
				return queryResultVo;
			}
			
			
			String ccid = userAttr.getCreditUser();
			
			if (StringUtils.isBlank(ccid)*/
/*用户未配置征信用户*//*
) {
				detail.setStatus("4");
				detail.setErrorCode("4");
				detail.setErrorMsg("用户未配置征信用户");
				detailService.update(detail);
				BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("4").batchQueryDetailId(detail.getId()).build();
				return queryResultVo;
			}
			
			CpqCcUser ccuser = ccUserService.findByCcId(ccid);
			
			if(ccuser == null||StringUtils.isBlank(ccuser.getCcuser())*/
/*未配置征信用户*//*
){
				detail.setStatus("6");
				detail.setErrorCode("6");
				detail.setErrorMsg("未配置征信用户");
				detailService.update(detail);
				BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("4").batchQueryDetailId(detail.getId()).build();
				return queryResultVo;
			}
			String creditUserName = ccuser.getCcuser();
			queryReq.setCreditUser(creditUserName);
			
			// 进行异常阻断
			//接口批量查询是否进行异常规则管控   true:是   false:否
			String isInterfaceBatchQueryControlled = ResourceManager.getInstance().getValue("exceptionRule.isInterfaceBatchQueryControlled");
			if(StringUtils.equals(TRUE, isInterfaceBatchQueryControlled)){
			    ExceptionRuleValidateVo exceptionRuleValidateVo = new ExceptionRuleValidateVo();
			    exceptionRuleValidateVo.setClientName(detail.getName());
			    exceptionRuleValidateVo.setCertType(detail.getCerttype());
			    exceptionRuleValidateVo.setCertNo(detail.getCertno());
			    exceptionRuleValidateVo.setCreditUserName(creditUserName);
			    exceptionRuleValidateVo.setIsInterface("0");
			    exceptionRuleValidateVo.setIsPreValidate("1");
			    exceptionRuleValidateVo.setOrgCode(detail.getOperorg());
			    exceptionRuleValidateVo.setQueryReson(detail.getQryreason());
			    exceptionRuleValidateVo.setQueryUserName(detail.getOperator());
//			    exceptionRuleValidateVo.setSize(size);
			    
		    LegalReview legalReview = cpqLegalCheckService.exceptionRuleValidate(exceptionRuleValidateVo);
		    if(StringUtils.equals(Constant.CHECK_USER_VIOLATE, legalReview.getResultCode())*/
/*异常阻断验证未通过*//*

		    		||StringUtils.equals(Constant.CHECK_ERROR, legalReview.getResultCode())*/
/*锁定用户*//*
){
		        detail.setStatus("7");
                detail.setErrorCode("7");
                detail.setErrorMsg(legalReview.getResultMsg());
                detailService.update(detail);
                BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("4").batchQueryDetailId(detail.getId()).build();
                return queryResultVo;
		    }
			    
			}

			//判断查询时效
			if (0 == detail.getTimeBound() */
/*只查询人行*//*
) {
				inquireCreditReport = inquireService.inquireCreditReport(queryReq);
			} else {
				inquireCreditReport = inquireService.inquireLocalCreditReport(queryReq);
			}

			//  判断是否查询成功条件需要修改为正确的
			if (ERROR.equals(inquireCreditReport.getCode()) */
/*查询未成功*//*
) {
				//判断查询时效是否为空
				if (0 < detail.getTimeBound()*/
/* 为正数,则代表时间段内本地没有，则去查询人行*//*
) {
					inquireCreditReport = inquireService.inquireCreditReport(queryReq);
				} else if (0 > detail.getTimeBound()*/
/*为负数,本地无报告，不查询人行*//*
) {
					//  本地无报告应该按失败请求进行处理
					detail.setStatus("1");
					detail.setErrorCode("1");
					detail.setErrorMsg("本地无报告");
					detailService.update(detail);
					BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("1").batchQueryDetailId(detail.getId()).build();
					return queryResultVo;
				}
			}

			if (ERROR.equals(inquireCreditReport.getCode()) */
/*仍然查询未成功*//*
) {
				//查询人行失败
				detail.setStatus("2");
				detail.setErrorCode("2");
				detail.setErrorMsg("查询人行失败");
				detailService.update(detail);
				BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("2").batchQueryDetailId(detail.getId()).build();
				return queryResultVo;
			}

			//更新请求状态为处理完毕，已查得报告
			detail.setStatus("0");
			String resultId = inquireCreditReport.getQueryReq().getId();
			detail.setResultId(resultId);
			detailService.update(detail);

			//已查得报告，进行返回
			BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("0").batchQueryDetailId(detail.getId()).build();
			return queryResultVo;
		} catch (Exception e) {
			log.error("GenerationBatchRequestQueryTask is error detail = {} , e={}", detail,e);
			//处理异常
			detail.setStatus("5");
			detail.setErrorCode("5");
			detail.setErrorMsg("处理异常");
			detailService.update(detail);
			BatchRequestQueryResultVo queryResultVo = new BatchRequestQueryResultVo.Builder().status("5").batchQueryDetailId(detail.getId()).build();
			return queryResultVo;
			
		}
	}
	
	*/
/**
	 * 
	 * @return void
	 *//*

	private void init() {
		inquireService = (InquireService) WebApplicationContextUtil.getBean("inquireServiceImpl");
		detailService = (CpqBatchquerydetailService) WebApplicationContextUtil.getBean("cpqBatchquerydetailServiceImpl");
		ccUserService = (CpqCcUserService) WebApplicationContextUtil.getBean("cpqCcUserServiceImpl");
		cpqLegalCheckService = (CpqLegalCheckService) WebApplicationContextUtil.getBean("cpqLegalCheckServiceImpl");
	}
	
	private QueryReq detailToQueryReq(CpqBatchquerydetail detail) {
		QueryReq queryReq = new QueryReq();
		queryReq.setCertNo(detail.getCertno());
		queryReq.setCertType(detail.getCerttype());
		queryReq.setClientName(detail.getName());
		queryReq.setQryReason(detail.getQryreason());
		queryReq.setQueryFormat(detail.getQueryFormat());
		queryReq.setQueryTime(new Date());
		queryReq.setQueryType(detail.getQrytype());
		queryReq.setAutharchiveId(detail.getAutharchiveid());
		queryReq.setAssocbsnssData(detail.getAutharchivedata());
		queryReq.setCheckId(detail.getId());
		queryReq.setOperOrg(detail.getOperorg());
		queryReq.setOperator(detail.getOperator());
		return queryReq;
	}

	
}
*/
