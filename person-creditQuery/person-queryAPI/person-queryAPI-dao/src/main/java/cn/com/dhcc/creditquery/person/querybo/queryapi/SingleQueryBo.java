/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.querybo.queryapi;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.com.dhcc.creditquery.person.query.bo.BaseBo;
import cn.com.dhcc.creditquery.person.rules.SingleQueryInfoRules;
import cn.com.dhcc.query.queryapicommon.rules.EnumJudgeRules;
import cn.com.dhcc.query.queryapicommon.rules.IpCheck;
import cn.com.dhcc.query.queryapicommon.rules.NumberCheck;

/**
 * <个人单笔查询请求参数>
 * 
 * @author guoshihu
 * @date 2019年1月19日
 */
@XStreamAlias("singleQuery")
@SingleQueryInfoRules
public class SingleQueryBo extends BaseBo {

	private static final long serialVersionUID = 6169927732875149421L;

	/**
	 * 被查询人姓名
	 */
	@XStreamAlias("querierName")
	@Length(max = 30, message = "querierName长度最大为30;")
	@NotBlank(message = "querierName不能为空;")
	private String querierName;
	/**
	 * 被查询人证件类型
	 */
	@XStreamAlias("querierCertype")
	@Length(max = 2, message = "querierCertype长度最大为2;")
	@NotBlank(message = "querierCertype不能为空;")
	@EnumJudgeRules(message = "querierCertype不在字典中", type = "idType", isPerson = true)
	private String querierCertype;
	/**
	 * 被查询人证件号码
	 */
	@XStreamAlias("querierCertno")
	@Length(max = 18, message = "querierCertno长度最大为18;")
	@NotBlank(message = "querierCertno不能为空;")
	private String querierCertno;
	/**
	 * 查询原因
	 */
	@XStreamAlias("queryReason")
	@NotBlank(message = "queryReason不能为空;")
	@EnumJudgeRules(message = "queryReason不在字典中", type = "qryReason", isPerson = true)
	@Length(max = 2, message = "queryReason长度最大为2;")
	private String queryReason;
	/**
	 * 信用报告版式
	 */
	@XStreamAlias("queryFormat")
	@NotBlank(message = "queryFormat不能为空;")
	@EnumJudgeRules(message = "queryFormat不在字典中", type = "qryFormat", isPerson = true)
	@Length(max = 2, message = "queryFormat长度最大为2;")
	private String queryFormat;
	/**
	 * 信用报告格式
	 */
	@XStreamAlias("reportType")
	@NotBlank(message = "reportType不能为空;")
	@EnumJudgeRules(message = "reportType不在字典中", type = "reportType", isPerson = true)
	@Length(max = 2, message = "reportType长度最大为2;")
	private String reportType;
	/**
	 * 信用报告复用策略
	 */
	@XStreamAlias("queryType")
	@NotNull(message = "queryType不能为空;")
	@Digits(fraction = 0, integer = 5, message = "长度不超过5位")
	private Integer queryType;
	/**
	 * 同步异步返回标识
	 */
	@XStreamAlias("syncFlag")
	@NotBlank(message = "syncFlag不能为空;")
	@EnumJudgeRules(message = "syncFlag不在字典中", type = "syncFlag", isPerson = true)
	@Length(max = 1, message = "syncFlag长度最大为1;")
	private String syncFlag;
	/**
	 * 异步调用查询流程配置
	 */
	@XStreamAlias("asyncQueryFlag")
	@EnumJudgeRules(message = "asyncQueryFlag不在字典中", type = "asyncQueryFlag", isPerson = true)
	@Length(max = 1, message = "asyncQueryFlag长度最大为1;")
	private String asyncQueryFlag;
	/**
	 * 业务线
	 */
	@XStreamAlias("businessLine")
	@NotBlank(message = "businessLine不能为空;")
	@Length(max = 2, message = "businessLine长度最大为2;")
	@NumberCheck(message = "businessLine只能是数字")
	private String businessLine;
	/**
	 * 系统标识
	 */
	@XStreamAlias("sysCode")
	@Length(max = 20, message = "sysCode长度最大为20;")
	@NotBlank(message = "sysCode不能为空;")
	private String sysCode;
	/**
	 * 查询用户
	 */
	@XStreamAlias("queryUser")
	@Length(max = 30, message = "queryUser长度最大为30;")
	@NotBlank(message = "queryUser不能为空;")
	private String queryUser;
	/**
	 * 发起用户
	 */
	@XStreamAlias("callSysUser")
	@Length(max = 30, message = "callSysUser长度最大为30;")
	@NotBlank(message = "callSysUser不能为空;")
	private String callSysUser;
	/**
	 * 关联业务号
	 */
	@XStreamAlias("bussId")
	@Length(max = 64, message = "bussid长度最大为64;")
	private String bussid;
	/**
	 * 接入系统审批员
	 */
	@XStreamAlias("recheckuser")
	@Length(max = 30, message = "recheckuser长度最大为30;")
	private String recheckUser;
	/**
	 * 终端IP地址
	 */
	@XStreamAlias("userIP")
	@Length(max = 15, message = "userIP长度最大为15;")
	@IpCheck
	private String userIp;
	/**
	 * 终端MAC地址
	 */
	@XStreamAlias("userMAC")
	@Length(max = 17, message = "userMAC长度最大为17;")
	private String userMac;
	/**
	 * 信用报告版本
	 */
	@XStreamAlias("reportVersion")
	@EnumJudgeRules(message = "reportVersion不在字典中", type = "reportVersion", isPerson = true)
	@NotBlank(message = "信用报告版本不能为空;")
	@Length(max = 5, message = "reportVersion长度最大为5;")
	private String reportVersion;
	/**
	 * 查询请求编号
	 */
	@NotBlank(message = "reqId不能为空;")
	@Length(max = 32, message = "reqId长度最大为32;")
	private String reqId;
	/**
	 * 批量查询批次号
	 */
	private String msgNo;

	@XStreamAlias("authorized")
	@Valid
	private AuthorizedBo authorized;

	/**
	 * 发起机构
	 */

	@Length(max = 20, message = "发起机构长度最大为20;")
	private String callSysOrg;

	public String getCallSysOrg() {
		return callSysOrg;
	}

	public void setCallSysOrg(String callSysOrg) {
		this.callSysOrg = callSysOrg;
	}

	public SingleQueryBo() {
	}

	public SingleQueryBo(String querierName, String querierCertype, String querierCertno, String queryReason,
			String queryFormat, String reportType, Integer queryType, String syncFlag, String asyncQueryFlag,
			String businessLine, String sysCode, String queryUser, String callSysUser, String bussid,
			String recheckUser, String userIp, String userMac, String reportVersion, String reqId,
			AuthorizedBo authorized) {
		super();
		this.querierName = querierName;
		this.querierCertype = querierCertype;
		this.querierCertno = querierCertno;
		this.queryReason = queryReason;
		this.queryFormat = queryFormat;
		this.reportType = reportType;
		this.queryType = queryType;
		this.syncFlag = syncFlag;
		this.asyncQueryFlag = asyncQueryFlag;
		this.businessLine = businessLine;
		this.sysCode = sysCode;
		this.queryUser = queryUser;
		this.callSysUser = callSysUser;
		this.bussid = bussid;
		this.recheckUser = recheckUser;
		this.userIp = userIp;
		this.userMac = userMac;
		this.reportVersion = reportVersion;
		this.reqId = reqId;
		this.authorized = authorized;
	}

	public String getQuerierName() {
		return querierName;
	}

	public void setQuerierName(String querierName) {
		this.querierName = querierName;
	}

	public String getQuerierCertype() {
		return querierCertype;
	}

	public void setQuerierCertype(String querierCertype) {
		this.querierCertype = querierCertype;
	}

	public String getQuerierCertno() {
		return querierCertno;
	}

	public void setQuerierCertno(String querierCertno) {
		this.querierCertno = querierCertno;
	}

	public String getQueryReason() {
		return queryReason;
	}

	public void setQueryReason(String queryReason) {
		this.queryReason = queryReason;
	}

	public String getQueryFormat() {
		return queryFormat;
	}

	public void setQueryFormat(String queryFormat) {
		this.queryFormat = queryFormat;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getAsyncQueryFlag() {
		return asyncQueryFlag;
	}

	public void setAsyncQueryFlag(String asyncQueryFlag) {
		this.asyncQueryFlag = asyncQueryFlag;
	}

	public String getBusinessLine() {
		return businessLine;
	}

	public void setBusinessLine(String businessLine) {
		this.businessLine = businessLine;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getQueryUser() {
		return queryUser;
	}

	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}

	public String getCallSysUser() {
		return callSysUser;
	}

	public void setCallSysUser(String callSysUser) {
		this.callSysUser = callSysUser;
	}

	public String getBussid() {
		return bussid;
	}

	public void setBussid(String bussid) {
		this.bussid = bussid;
	}

	public String getRecheckUser() {
		return recheckUser;
	}

	public void setRecheckUser(String recheckUser) {
		this.recheckUser = recheckUser;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserMac() {
		return userMac;
	}

	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}

	public String getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public AuthorizedBo getAuthorizedBo() {
		return authorized;
	}

	public void setAuthorizedBo(AuthorizedBo authorized) {
		this.authorized = authorized;
	}

	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SingleQueryBo [querierName=" + querierName + ", querierCertype=" + querierCertype + ", querierCertno="
				+ querierCertno + ", queryReason=" + queryReason + ", queryFormat=" + queryFormat + ", reportType="
				+ reportType + ", queryType=" + queryType + ", syncFlag=" + syncFlag + ", asyncQueryFlag="
				+ asyncQueryFlag + ", businessLine=" + businessLine + ", sysCode=" + sysCode + ", queryUser="
				+ queryUser + ", callSysUser=" + callSysUser + ", bussid=" + bussid + ", recheckUser=" + recheckUser
				+ ", userIp=" + userIp + ", userMac=" + userMac + ", reportVersion=" + reportVersion + ", reqId="
				+ reqId + ", msgNo=" + msgNo + ", authorized=" + authorized + "]";
	}

}
