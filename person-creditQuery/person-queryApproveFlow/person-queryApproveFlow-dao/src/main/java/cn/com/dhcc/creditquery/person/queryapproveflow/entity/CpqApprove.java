/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapproveflow.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;


/**
 * 个人授权审批业务实体类
 * @author yahongcai
 * @date 2019年2月23日
 */
@Entity
@Table(name="CPQ_CHECKINFO")
public class CpqApprove implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@Column(name="ARCHIVE_ID", length=32)
	private String archiveId;

	@Column(name="ASSOCBSNSS_DATA", length=300)
	private String assocbsnssData;

	@Column(name="CERT_NO", length=18)
	private String certNo;

	@Column(name="CERT_TYPE", length=128)
	private String certType;

	@Column(name="CLIENT_IP", length=30)
	private String clientIp;

	@Column(name="CLIENT_NAME", length=128)
	private String clientName;

	@Column(name="CREDIT_USER", length=50)
	private String creditUser;

	@Column(name = "EXT1",length=60)
	private String ext1;

	@Column(name = "EXT2",length=60)
	private String ext2;

	@Column(name = "EXT3",length=60)
	private String ext3;

	@Column(name = "EXT4",length=60)
	private String ext4;

	@Column(name = "EXT5",length=60)
	private String ext5;

	@Column(name = "EXT6",length=60)
	private String ext6;

	@Column(name="OPER_ORG", nullable=false, length=16)
	private String operOrg;

	@Column(name="OPER_TIME", nullable=false)
	private Date operTime;

	@Column(name = "OPERATOR",nullable=false, length=50)
	private String operator;

	@Column(name="QRY_REASON", nullable=false, length=2)
	private String qryReason;

	@Column(name="QUERY_FORMAT", nullable=false, length=2)
	private String queryFormat;

	@Column(name="QUERY_TIME", nullable=false)
	private Date queryTime;

	@Column(name="QUERY_TYPE", nullable=false, length=6)
	private String queryType;

	@Column(name="REFUSE_REASONS", length=450)
	private String refuseReasons;

	@Column(name="REK_ORG", length=16)
	private String rekOrg;

	@Column(name="REK_TIME")
	private Date rekTime;

	@Column(name="REK_TYPE", length=1)
	private String rekType;

	@Column(name="REK_USER", length=50)
	private String rekUser;

	@Column(name="REQ_ID", length=32)
	private String reqId;

	@Column(name="RESULT_TYPE", length=1)
	private String resultType;

	@Column(name = "source",length=1)
	private String source;

	@Column(name = "STATUS",nullable=false, length=1)
	private String status;

	public CpqApprove() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArchiveId() {
		return this.archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getAssocbsnssData() {
		return this.assocbsnssData;
	}

	public void setAssocbsnssData(String assocbsnssData) {
		this.assocbsnssData = assocbsnssData;
	}

	public String getCertNo() {
		return this.certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCertType() {
		return this.certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getClientIp() {
		return this.clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCreditUser() {
		return this.creditUser;
	}

	public void setCreditUser(String creditUser) {
		this.creditUser = creditUser;
	}

	public String getExt1() {
		return this.ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return this.ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return this.ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return this.ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return this.ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return this.ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getOperOrg() {
		return this.operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getQryReason() {
		return this.qryReason;
	}

	public void setQryReason(String qryReason) {
		this.qryReason = qryReason;
	}

	public String getQueryFormat() {
		return this.queryFormat;
	}

	public void setQueryFormat(String queryFormat) {
		this.queryFormat = queryFormat;
	}

	public Date getQueryTime() {
		return this.queryTime;
	}

	public void setQueryTime(Date queryTime) {
		this.queryTime = queryTime;
	}

	public String getQueryType() {
		return this.queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getRefuseReasons() {
		return this.refuseReasons;
	}

	public void setRefuseReasons(String refuseReasons) {
		this.refuseReasons = refuseReasons;
	}

	public String getRekOrg() {
		return this.rekOrg;
	}

	public void setRekOrg(String rekOrg) {
		this.rekOrg = rekOrg;
	}

	public Date getRekTime() {
		return this.rekTime;
	}

	public void setRekTime(Date rekTime) {
		this.rekTime = rekTime;
	}

	public String getRekType() {
		return this.rekType;
	}

	public void setRekType(String rekType) {
		this.rekType = rekType;
	}

	public String getRekUser() {
		return this.rekUser;
	}

	public void setRekUser(String rekUser) {
		this.rekUser = rekUser;
	}

	public String getReqId() {
		return this.reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getResultType() {
		return this.resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}