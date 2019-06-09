/*
package cn.com.dhcc.creditquery.person.queryweb.webservice;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuBatchRequest;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuBatchResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuGetResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuSingleRequest;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuSingleResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.service.QueryMiddleService;
import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;

@Component
public class CreditReportService {
	private static Logger log = LoggerFactory.getLogger(CreditReportService.class);
	
	
	@Autowired
	QueryMiddleService queryMiddleService;
	
	*/
/**
	 * webservice单笔请求
	 *//*

	public CuResult sendCuRequest(CuSingleRequest cuSingleRequest) {
		log.info("调用接口方法cuSingleRequest");
		CuResult cuResult=new CuResult();
		//将CuSingleRequest 转换为  XML
		String singleRequestXml = XmlUtil.Obj2Xml(cuSingleRequest);
		//将XML 使用webservice客户端调用东华监管的接口
		//将返回的XML转换为CuResult 返回给下游系统
		cuResult = queryMiddleService.getResultXml(singleRequestXml);
		return cuResult;
	}

	
	*/
/**
	 * webservice单笔结果获取
	 *//*

	public CuSingleResult getCuResult(CuGetResult cuGetResult) {
		CuSingleResult cuSingleResult=new CuSingleResult();
		cuSingleResult = queryMiddleService.getCuSingleResult(cuGetResult);
		return cuSingleResult;
	}
	
	*/
/**
	 * 个人批量查询webservice
	 * @param cuBatchRequest
	 * @return
	 *//*

	public CuBatchResult sendCuRequests(CuBatchRequest cuBatchRequest){
		CuBatchResult cuBatchResult = new CuBatchResult();
		String batchQueryVoXml = XmlUtil.Obj2Xml(cuBatchRequest);
		cuBatchResult = queryMiddleService.sendCusRequests(batchQueryVoXml);
		return cuBatchResult;
	}
	
	*/
/**
	 * 个人批量结果获取webservice
	 * @param getresult
	 * @return
	 *//*

	public CuBatchResult getCuResults(CuGetResult getResult){
		CuBatchResult cuBatchResult = new CuBatchResult();
		cuBatchResult = queryMiddleService.getCuResults(getResult);
		return cuBatchResult;
	}

}
*/
