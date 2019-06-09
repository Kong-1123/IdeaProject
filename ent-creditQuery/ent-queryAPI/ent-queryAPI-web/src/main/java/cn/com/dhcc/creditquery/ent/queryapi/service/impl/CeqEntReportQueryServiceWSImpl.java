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

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import cn.com.dhcc.credit.platform.util.NetworkUtil;
import cn.com.dhcc.credit.platform.util.UUIDHexGenerator;
import cn.com.dhcc.creditquery.ent.queryapi.common.EntConstant;
import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleResult;
import cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryService;
import cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryServiceWS;
import cn.com.dhcc.creditquery.ent.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorFileBo;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorizedBo;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorizedFileBo;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo;
import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Jerry.chen
 * @date 2019年2月15日
 */
@Slf4j
@WebService(endpointInterface="cn.com.dhcc.creditquery.ent.queryapi.service.CeqEntReportQueryServiceWS")
public class CeqEntReportQueryServiceWSImpl implements CeqEntReportQueryServiceWS {
	/**
	 * 特殊字符&
	 */
	private static final String SPECIALAND = "&";
	
	/**
	 * 特殊字符&转义后的字符串
	 */
	private static final CharSequence SPECIALANDTR = "&amp;";
	
	/**
	 * queryType标签
	 */
	private static final String QUERYTYPE_TAG = "queryType"; 
	
	/**
	 * 只能是正整数或负整数的正则
	 */
	private static final String NUMBER_REGEX = "(\\-|\\+?)\\d+";
	
	private static final String MSG_QUERYTYPE_ERROR = "queryType只能是整数"; 
	private static final String MSG_QUERYTYPE_ERRORISNOTNULL = "queryType不能为空"; 
	
	/**
	 * authorBeginDate标签xpath表达式
	 */
	private static final String AUTHORBEGINDATE_TAG = "/singleQuery/authorized/authorBeginDate"; 
	/**
	 * authorEndDate标签xpath表达式
	 */
	private static final String AUTHORENDDATE_TAG = "/singleQuery/authorized/authorEndDate";
	
	 /**
     * 只含有年月日的日期正则
     */
	 private static final String DATEREGEX = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$";
//	 /**
//	  * yyyy-mm-ddhh24:MM:ss 的正则
//	  */
//	 private static final String DATEREGEX_YYYYMMDDHHMMSS = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
	 /**
	  * authorBeginDate
	  */
	 private static final String AUTHORBEGINDATE_ERROR = "authorBeginDate必须符合日期格式yyyy-mm-dd hh24:MM:ss或yyyy-mm-dd";
	 /**
	  * authorEndDate
	  */
	 private static final String AUTHORENDDATE_ERROR = "authorEndDate必须符合日期格式yyyy-mm-dd hh24:MM:ss或yyyy-mm-dd";
	
	 /**
	  * 只含有年月日的日期格式
	  */
	 private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	 /**
	  * 含有时分秒的日期格式
	  */
	 private static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	/**
	 * webservice入口
	 * 请求地址：http://IP:PORT/entqueryapiweb/webservice/ReportQuery?wsdl
	 * cxf url : /webservice/* 
	 * address : ReportQuery
	 */
	 @Override
	public String getCreditReport(String reqXml) {
		String reqid = UUIDHexGenerator.getInstance().generate();;
		reqXml=reqXml.replace(SPECIALAND, SPECIALANDTR);
		log.info("getCreditReport---start,reqid={}",reqid);
		String returnStr=null;
		SingleQueryBo queryBo=null;
		try {
			returnStr = preHandleXml(reqXml, reqid, returnStr);
			log.debug("preHandleXml返回值为：{}",returnStr);
			if(StringUtils.isNotBlank(returnStr)){
				return returnStr;	
			}
			queryBo = XmlUtil.parse4Xml(SingleQueryBo.class, reqXml);
			if(queryBo==null){
				returnStr=getErrBoString(EntConstant.CODE_ERROR,EntConstant.MSG_ERROR,reqid);
				log.error("将请求参数转换为对象:转换出的对象为空");
				return returnStr;
			}
			Message message =  PhaseInterceptorChain.getCurrentMessage();
			HttpServletRequest httprequest = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
			String ip = NetworkUtil.getIpAddress(httprequest);
			log.debug("请求ip为：{}",ip);
			queryBo.setUserIp(ip);
			queryBo.setReqId(reqid);
			cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo copyObject = singleQueryBoCopy(queryBo);
			CeqEntReportQueryService entReportQueryService = InitBeanUtil.getCeqEntReportQueryService();
			returnStr = entReportQueryService.getCreditReport(copyObject);



		} catch (Exception e) {
			log.error("getCreditReport(String reqXml{})出现异常{}",reqXml,e);
			returnStr=getErrBoString(EntConstant.CODE_SYS_EXCEPTION,EntConstant.MSG_SYS_EXCEPTION,reqid);
			return returnStr;
		}
		return returnStr;
	}

	/**
	 * SingleQueryBo复制
	 * @param queryBo
	 * @return
	 * @author yuzhao.xue
	 * @throws Exception 
	 * @date 2019年3月4日
	 */
	private cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo singleQueryBoCopy(SingleQueryBo queryBo) throws Exception {
		cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo singleQueryBoCopy = ClassCloneUtil.copyObject(queryBo, cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo.class);
		CopyUtil.trim(singleQueryBoCopy);
		if(queryBo.getAuthorizedBo()!=null) {
			AuthorizedBo authorizedBoCopy = ClassCloneUtil.copyObject(queryBo.getAuthorizedBo(), cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorizedBo.class);
			CopyUtil.trim(authorizedBoCopy);
			singleQueryBoCopy.setAuthorizedBo(authorizedBoCopy);
			if(queryBo.getAuthorizedBo().getAuthorizedFileBo()!=null) {
				AuthorizedFileBo authorizedFileCopy = ClassCloneUtil.copyObject(queryBo.getAuthorizedBo().getAuthorizedFileBo(), cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorizedFileBo.class);
				authorizedBoCopy.setAuthorizedFileBo(authorizedFileCopy);
				if(queryBo.getAuthorizedBo().getAuthorizedFileBo().getAuthorfile() !=null) {
					List<AuthorFileBo> authorFileListcopy = ClassCloneUtil.copyIterableObject(queryBo.getAuthorizedBo().getAuthorizedFileBo().getAuthorfile(), cn.com.dhcc.creditquery.ent.querybo.queryapi.AuthorFileBo.class);
					for (AuthorFileBo authorFileBo : authorFileListcopy) {
						CopyUtil.trim(authorFileBo);
					}
					authorizedFileCopy.setAuthorfile(authorFileListcopy);
				}
			}
		}
		return singleQueryBoCopy;
	}
	
	

	@Override
	public String getSyncCreditReport(String reqID) {
		return InitBeanUtil.getCeqEntReportQueryService().getSyncCreditReport(reqID);
	}

	/**
	 * 对日期数字类型的标签内容提前处理防止转对象的时候报错
	 * @param parXml
	 * @param reqid
	 * @param returnStr
	 * @return
	 * @throws DocumentException
	 * @author yuzhao.xue
	 * @throws UnsupportedEncodingException 
	 * @date 2019年2月18日
	 */
	private String preHandleXml(String parXml, String reqid, String returnStr) throws Exception{
		log.info("preHandleXml( parXml {},  reqid {})---start",parXml,reqid);
		Document document = DocumentHelper.parseText(parXml);
		Element rootElement = document.getRootElement();
		String queryTypeVal = rootElement.elementText(QUERYTYPE_TAG);
		log.debug("queryTypeVal为:{}",queryTypeVal);
		if(StringUtils.isNotBlank(queryTypeVal)) {
			if(!queryTypeVal.matches(NUMBER_REGEX)){
				returnStr=getErrBoString(EntConstant.CODE_ERROR,MSG_QUERYTYPE_ERROR,reqid);
				return returnStr;
			}
		}else {
			returnStr=getErrBoString(EntConstant.CODE_ERROR,MSG_QUERYTYPE_ERRORISNOTNULL,reqid);
			return returnStr;
		}
		Node authorBeginDateNode = rootElement.selectSingleNode(AUTHORBEGINDATE_TAG);
		if(authorBeginDateNode==null || StringUtils.isEmpty(authorBeginDateNode.getText())){
			return returnStr;
		}
		String authorBeginDateVal = authorBeginDateNode.getText();
		log.debug("authorBeginDateVal为:{}",authorBeginDateVal);
		try {
			formatDate(authorBeginDateVal);
		} catch (Exception e) {
			log.error("authorBeginDate日期转换出现异常{}",e);
			returnStr=getErrBoString(EntConstant.CODE_ERROR,AUTHORBEGINDATE_ERROR,reqid);
			return returnStr;
		}
		
		Node authorEndDateNode = rootElement.selectSingleNode(AUTHORENDDATE_TAG);
		if(authorEndDateNode==null || StringUtils.isEmpty(authorEndDateNode.getText())){
			return returnStr;
		}
		String authorEndDateVal = authorEndDateNode.getText();
		log.debug("authorEndDateVal为:{}",authorEndDateVal);
		try {
			formatDate(authorEndDateVal);
		} catch (Exception e) {
			log.error("authorEndDate日期转换出现异常{}",e);
			returnStr=getErrBoString(EntConstant.CODE_ERROR,AUTHORENDDATE_ERROR,reqid);
			return returnStr;
		}
		return returnStr;
	}

	private void formatDate(String dateStr) throws ParseException {
		if (dateStr.matches(DATEREGEX)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.parse(dateStr);
			dateFormat = null;
		} else {
			// 这里将字符串转换成日期
			DateFormat defaultDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			defaultDateformat.parse(dateStr);
			defaultDateformat = null;
		}
	}

	/**
	 * 获取错误结果
	 * @param resCode
	 * @param resMsg
	 * @param reqID 
	 * @return
	 */
	private String getErrBoString(String resCode, String resMsg, String reqID) {
		log.info("进入方法getErrBoString(String resCode, String resMsg, String {})",reqID);
		CeqSingleResult rv = new CeqSingleResult();
		rv.setResCode(resCode);
		rv.setResMsg(resMsg);
		rv.setReqId(reqID);
		String obj2Xml = XmlUtil.Obj2Xml(rv);
		return obj2Xml;
	}
	
}
