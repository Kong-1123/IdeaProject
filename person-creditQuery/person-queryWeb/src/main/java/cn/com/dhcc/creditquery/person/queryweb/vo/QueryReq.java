package cn.com.dhcc.creditquery.person.queryweb.vo;

import cn.com.dhcc.creditquery.person.queryweb.rules.QueryReqValidator;

import javax.validation.constraints.NotNull;
import java.util.Date;

@QueryReqValidator
public class QueryReq {

	private String id;
	// 相关联的业务数据
	private String assocbsnssData;
	// 关联档案ID
	private String autharchiveId;
	// 批量标志
	private String batchFlag;
	// 证件号码
	@NotNull
	private String certNo;
	// 证件类型
	@NotNull
	private String certType;
	// 人行登陆认证标识
	private String certificationMark;
	// 渠道编号
	private String channelId;
	// 审批请求ID
	private String checkId;
	// 审批方式
	private String checkWay;
	// 请求客户端IP
	private String clientIp;
	// 客户姓名
	@NotNull
	private String clientName;
	// 征信用户
	private String creditUser;
	//征信用户密码
	private String creditPassWord;
	
	private String resulttype;
	// 错误信息
	private String errorInfo;
	// 请求流水号
	private String flowId;
	// 批量查询批次号
	private String msgNo;
	// 操作机构
	private String operOrg;
	// 操作用户
	private String operator;
	// 查询原因
	private String qryReason;
	// 查询要求时效
	private String qtimeLimit;
	// 查询版式
	private String queryFormat;
	// 查询人行通信模式
	private String queryMode;
	// 金融机构代码
	private String queryOrg;
	// 查询时间
	private Date queryTime;
	// 查询类型
	private String queryType;
	// 审批机构
	private String rekOrg;
	// 审批时间
	private Date rekTime;
	// 审批用户
	private String rekUser;
	// 结果类型
	private String resultType;
	// 信用报告来源
	private String source;
	// 数据状态
	private String status;
	// 操作时间
	private Date updateTime;
	// 信用报告id
	private String creditId;
	// html格式信用报告
	private String html;
	// xml格式信用报告
	private String xml;
	// pdf格式信用报告
	private String pdf;
	// 审批用户密码
	private String rekPasword;
	
	//进行查询的法人机构
	private String topOrgCode;
	
	/*========二代相关信息==========*/
	//信用报告版本
	private String reportVersion;
	//信用报告封装类型
	private String reportFormat;
	//查询原因
	private String queryReasonID;
	
	private String creditUserTow;
	private String queryOrgTow;
	// 客户系统编号
	private String cstmsysId;
	//接口发起用户
	private String callSysUser;
	//接入系统审批员
	private String recheckUserName;
	// 档案补录状态
	// 1-非贷后无资料;2-贷后无资料,3-贷后无借据编号,4-贷后两者皆无
	private String archiveRevise;
	/**
	 * 顶级机构
	 */
	private String topOrg;
	
	public QueryReq() {
	}


	public void setTopOrg(String topOrg) {
		this.topOrg = topOrg;
	}


	
	public String getQueryOrgTow() {
		return queryOrgTow;
	}



	public void setQueryOrgTow(String queryOrgTow) {
		this.queryOrgTow = queryOrgTow;
	}

	public String getCreditPassWord() {
		return creditPassWord;
	}



	public void setCreditPassWord(String creditPassWord) {
		this.creditPassWord = creditPassWord;
	}



	public String getTopOrgCode() {
		return topOrgCode;
	}



	public void setTopOrgCode(String topOrgCode) {
		this.topOrgCode = topOrgCode;
	}



	public String getReportVersion() {
		return reportVersion;
	}



	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}



	public String getReportFormat() {
		return reportFormat;
	}



	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}



	public String getQueryReasonID() {
		return queryReasonID;
	}



	public void setQueryReasonID(String queryReasonID) {
		this.queryReasonID = queryReasonID;
	}



	public String getTopOrg() {
		return topOrg;
	}



	public QueryReq(String id, String assocbsnssData, String autharchiveId, String batchFlag, String certNo,
			String certType, String certificationMark, String channelId, String checkId, String checkWay,
			String clientIp, String clientName, String creditUser, String creditPassWord, String resulttype,
			String errorInfo, String flowId, String msgNo, String operOrg, String operator, String qryReason,
			String qtimeLimit, String queryFormat, String queryMode, String queryOrg, Date queryTime, String queryType,
			String rekOrg, Date rekTime, String rekUser, String resultType2, String source, String status,
			Date updateTime, String creditId, String html, String xml, String pdf, String rekPasword, String qryReason2,
			String queryFormat2, String queryType2, String resultType22, String topOrgCode, String reportVersion,
			String reportFormat, String queryReasonID, String archiveRevise, String topOrg) {
		super();
		this.id = id;
		this.assocbsnssData = assocbsnssData;
		this.autharchiveId = autharchiveId;
		this.batchFlag = batchFlag;
		this.certNo = certNo;
		this.certType = certType;
		this.certificationMark = certificationMark;
		this.channelId = channelId;
		this.checkId = checkId;
		this.checkWay = checkWay;
		this.clientIp = clientIp;
		this.clientName = clientName;
		this.creditUser = creditUser;
		this.creditPassWord = creditPassWord;
		this.resulttype = resulttype;
		this.errorInfo = errorInfo;
		this.flowId = flowId;
		this.msgNo = msgNo;
		this.operOrg = operOrg;
		this.operator = operator;
		this.qryReason = qryReason;
		this.qtimeLimit = qtimeLimit;
		this.queryFormat = queryFormat;
		this.queryMode = queryMode;
		this.queryOrg = queryOrg;
		this.queryTime = queryTime;
		this.queryType = queryType;
		this.rekOrg = rekOrg;
		this.rekTime = rekTime;
		this.rekUser = rekUser;
		resultType = resultType2;
		this.source = source;
		this.status = status;
		this.updateTime = updateTime;
		this.creditId = creditId;
		this.html = html;
		this.xml = xml;
		this.pdf = pdf;
		this.rekPasword = rekPasword;
		resultType2 = resultType22;
		this.topOrgCode = topOrgCode;
		this.reportVersion = reportVersion;
		this.reportFormat = reportFormat;
		this.queryReasonID = queryReasonID;
		this.archiveRevise = archiveRevise;
		this.topOrg = topOrg;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssocbsnssData() {
		return assocbsnssData;
	}

	public void setAssocbsnssData(String assocbsnssData) {
		this.assocbsnssData = assocbsnssData;
	}

	public String getAutharchiveId() {
		return autharchiveId;
	}

	public void setAutharchiveId(String autharchiveId) {
		this.autharchiveId = autharchiveId;
	}

	public String getBatchFlag() {
		return batchFlag;
	}

	public void setBatchFlag(String batchFlag) {
		this.batchFlag = batchFlag;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertificationMark() {
		return certificationMark;
	}

	public void setCertificationMark(String certificationMark) {
		this.certificationMark = certificationMark;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public String getCheckWay() {
		return checkWay;
	}

	public void setCheckWay(String checkWay) {
		this.checkWay = checkWay;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCreditUser() {
		return creditUser;
	}

	public void setCreditUser(String creditUser) {
		this.creditUser = creditUser;
	}

	public String getResulttype() {
		return resulttype;
	}

	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	public String getOperOrg() {
		return operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getQryReason() {
		return qryReason;
	}

	public void setQryReason(String qryReason) {
		this.qryReason = qryReason;
	}

	public String getQtimeLimit() {
		return qtimeLimit;
	}

	public void setQtimeLimit(String qtimeLimit) {
		this.qtimeLimit = qtimeLimit;
	}

	public String getQueryFormat() {
		return queryFormat;
	}

	public void setQueryFormat(String queryFormat) {
		this.queryFormat = queryFormat;
	}

	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}

	public String getQueryOrg() {
		return queryOrg;
	}

	public void setQueryOrg(String queryOrg) {
		this.queryOrg = queryOrg;
	}

	public Date getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getRekOrg() {
		return rekOrg;
	}

	public void setRekOrg(String rekOrg) {
		this.rekOrg = rekOrg;
	}

	public Date getRekTime() {
		return rekTime;
	}

	public void setRekTime(Date rekTime) {
		this.rekTime = rekTime;
	}

	public String getRekUser() {
		return rekUser;
	}

	public void setRekUser(String rekUser) {
		this.rekUser = rekUser;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getArchiveRevise() {
		return archiveRevise;
	}

	public void setArchiveRevise(String archiveRevise) {
		this.archiveRevise = archiveRevise;
	}

	public String getRekPasword() {
		return rekPasword;
	}

	public void setRekPasword(String rekPasword) {
		this.rekPasword = rekPasword;
	}
	
	public String getCreditUserTow() {
		return creditUserTow;
	}

	public void setCreditUserTow(String creditUserTow) {
		this.creditUserTow = creditUserTow;
	}


	public String getCstmsysId() {
		return cstmsysId;
	}


	public void setCstmsysId(String cstmsysId) {
		this.cstmsysId = cstmsysId;
	}


	public String getCallSysUser() {
		return callSysUser;
	}


	public void setCallSysUser(String callSysUser) {
		this.callSysUser = callSysUser;
	}


	public String getRecheckUserName() {
		return recheckUserName;
	}


	public void setRecheckUserName(String recheckUserName) {
		this.recheckUserName = recheckUserName;
	}


	@Override
	public String toString() {
		return "QueryReq [id=" + id + ", assocbsnssData=" + assocbsnssData + ", autharchiveId=" + autharchiveId
				+ ", batchFlag=" + batchFlag + ", certNo=" + certNo + ", certType=" + certType + ", certificationMark="
				+ certificationMark + ", channelId=" + channelId + ", checkId=" + checkId + ", checkWay=" + checkWay
				+ ", clientIp=" + clientIp + ", clientName=" + clientName + ", creditUser=" + creditUser
				+ ", creditPassWord=" + creditPassWord + ", resulttype=" + resulttype + ", errorInfo=" + errorInfo
				+ ", flowId=" + flowId + ", msgNo=" + msgNo + ", operOrg=" + operOrg + ", operator=" + operator
				+ ", qryReason=" + qryReason + ", qtimeLimit=" + qtimeLimit + ", queryFormat=" + queryFormat
				+ ", queryMode=" + queryMode + ", queryOrg=" + queryOrg + ", queryTime=" + queryTime + ", queryType="
				+ queryType + ", rekOrg=" + rekOrg + ", rekTime=" + rekTime + ", rekUser=" + rekUser + ", resultType="
				+ resultType + ", source=" + source + ", status=" + status + ", updateTime=" + updateTime
				+ ", creditId=" + creditId + ", html=" + html + ", xml=" + xml + ", pdf=" + pdf + ", rekPasword="
				+ rekPasword + ", topOrgCode=" + topOrgCode + ", reportVersion="
				+ reportVersion + ", reportFormat=" + reportFormat + ", queryReasonID=" + queryReasonID
				+ ", archiveRevise=" + archiveRevise + ", topOrg=" + topOrg + ", getId()=" + getId()
				+ ", getAssocbsnssData()=" + getAssocbsnssData() + ", getAutharchiveId()=" + getAutharchiveId()
				+ ", getBatchFlag()=" + getBatchFlag() + ", getCertNo()=" + getCertNo() + ", getCertType()="
				+ getCertType() + ", getCertificationMark()=" + getCertificationMark() + ", getChannelId()="
				+ getChannelId() + ", getCheckId()=" + getCheckId() + ", getCheckWay()=" + getCheckWay()
				+ ", getClientIp()=" + getClientIp() + ", getClientName()=" + getClientName() + ", getCreditUser()="
				+ getCreditUser() + ", getResulttype()=" + getResulttype() + ", getErrorInfo()=" + getErrorInfo()
				+ ", getFlowId()=" + getFlowId() + ", getMsgNo()=" + getMsgNo() + ", getOperOrg()=" + getOperOrg()
				+ ", getOperator()=" + getOperator() + ", getQryReason()=" + getQryReason() + ", getQtimeLimit()="
				+ getQtimeLimit() + ", getQueryFormat()=" + getQueryFormat() + ", getQueryMode()=" + getQueryMode()
				+ ", getQueryOrg()=" + getQueryOrg() + ", getQueryTime()=" + getQueryTime() + ", getQueryType()="
				+ getQueryType() + ", getRekOrg()=" + getRekOrg() + ", getRekTime()=" + getRekTime() + ", getRekUser()="
				+ getRekUser() + ", getResultType()=" + getResultType() + ", getSource()=" + getSource()
				+ ", getStatus()=" + getStatus() + ", getUpdateTime()=" + getUpdateTime() + ", getCreditId()="
				+ getCreditId() + ", getHtml()=" + getHtml() + ", getXml()=" + getXml() + ", getPdf()=" + getPdf()
				+ ", getArchiveRevise()=" + getArchiveRevise() + ", getRekPasword()=" + getRekPasword()
				+ ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	

	
	
}
