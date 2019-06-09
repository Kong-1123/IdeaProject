/*
package cn.com.dhcc.creditquery.person.queryweb.webservice;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.dhcc.query.creditpersonquerymiddle.webservice.CommonMethod;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuBatchRequest;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuBatchResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuGetResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuResult;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuSingleRequest;
import cn.com.dhcc.query.creditpersonquerymiddle.webservice.bean.CuSingleResult;

@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService(targetNamespace = "http://webservice.icrqs.cfcc.com/", serviceName = "CreditReportService", portName = "CreditReportServicePortDelegate")
public class CreditReportServiceDelegate {

	@Resource
	private WebServiceContext webContext;

	public CuResult sendCuRequest(CuSingleRequest cuSingleRequest) {
		ServletContext sc = (ServletContext) webContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		CreditReportService creditReportService = (CreditReportService) WebApplicationContextUtils.getWebApplicationContext(sc).getBean("creditReportService");
		if (org.springframework.util.StringUtils.isEmpty(cuSingleRequest.getClientip()) || !CommonMethod.isIp(cuSingleRequest.getClientip())) {
			MessageContext mc = webContext.getMessageContext();

			HttpServletRequest request = (HttpServletRequest) (mc.get(MessageContext.SERVLET_REQUEST));
			cuSingleRequest.setClientip(request.getRemoteAddr());
		}

		return creditReportService.sendCuRequest(cuSingleRequest);
	}

	public CuSingleResult getCuResult(CuGetResult cuGetResult) {
		ServletContext sc = (ServletContext) webContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		CreditReportService creditReportService = (CreditReportService) WebApplicationContextUtils.getWebApplicationContext(sc).getBean("creditReportService");
		return creditReportService.getCuResult(cuGetResult);
	}

	public CuBatchResult sendCuRequests(CuBatchRequest cuBatchRequest) {
		ServletContext sc = (ServletContext) webContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		CreditReportService creditReportService = (CreditReportService) WebApplicationContextUtils.getWebApplicationContext(sc).getBean("creditReportService");
		if (org.springframework.util.StringUtils.isEmpty(cuBatchRequest.getClientIp()) || !CommonMethod.isIp(cuBatchRequest.getClientIp())) {
			MessageContext mc = webContext.getMessageContext();

			HttpServletRequest request = (HttpServletRequest) (mc.get(MessageContext.SERVLET_REQUEST));
			cuBatchRequest.setClientIp(request.getRemoteAddr());
		}
		return creditReportService.sendCuRequests(cuBatchRequest);
	}

	public CuBatchResult getCuResults(CuGetResult getResult) {
		ServletContext sc = (ServletContext) webContext.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		CreditReportService creditReportService = (CreditReportService) WebApplicationContextUtils.getWebApplicationContext(sc).getBean("creditReportService");
		return creditReportService.getCuResults(getResult);
	}

}*/
