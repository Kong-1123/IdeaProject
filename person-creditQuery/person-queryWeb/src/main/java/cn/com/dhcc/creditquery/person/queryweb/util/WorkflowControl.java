package cn.com.dhcc.creditquery.person.queryweb.util;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cn.com.dhcc.creditquery.person.queryweb.vo.QueryReq;

public class WorkflowControl {
	private List<String> flowName;
	private String modulaName;
	private String resultCode;
	private String resultMsg;
	private String url;
	private String submitUrl;
	private String currentPage;
	private String totalPage;

	private QueryReq queryReq;

	public WorkflowControl() {
	}

	public WorkflowControl(List<String> flowName, String modulaName, String resultCode, String resultMsg, String url,
			String submitUrl, String currentPage, String totalPage, QueryReq queryReq) {
		super();
		this.flowName = flowName;
		this.modulaName = modulaName;
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.url = url;
		this.submitUrl = submitUrl;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.queryReq = queryReq;
	}

	public WorkflowControl(String resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public List<String> getFlowName() {
		return flowName;
	}

	public void setFlowName(List<String> flowName) {
		this.flowName = flowName;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public QueryReq getQueryReq() {
		return queryReq;
	}

	public void setQueryReq(QueryReq queryReq) {
		this.queryReq = queryReq;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getModulaName() {
		return modulaName;
	}

	public void setModulaName(String modulaName) {
		this.modulaName = modulaName;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
