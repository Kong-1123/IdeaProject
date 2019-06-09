/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvExport;

/**
 * <保存授权档案信息>
 * 
 * @author Tianyu.Li
 * @date 2018年03月07日
 */
@Entity
@Table(name = "CPQ_ARCHIVE")
public class CpqArchiveView implements Serializable {

	private static final long serialVersionUID = 3145013004547716385L;
	// 主键
	private String id;
	// 客户名称
	@CsvExport(value = "客户名称")
	private String clientName;
	// 证件类型
	@CsvExport(value = "证件类型", dicType = "idType")
	private String cretType;
	// 证件号码
	@CsvExport("证件号码")
	private String cretNo;
	// 档案类型(1-电子档案，2-纸质档案，3-影像档案)
	@CsvExport(value = "档案类型", dicType = "archiveType")
	private String archiveType;
	// 授权起始日
	@CsvExport(value = "授权起始日", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	// 授权到期日
	@CsvExport(value = "授权到期日", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	private Date expireDate;
	// 展期到期日
	@CsvExport(value = "展期到期日", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	private Date extendDate;
	// 档案状态 (1-有效，2-无资料)
	@CsvExport(value = "档案状态", dicType = "archiveStatus")
	private String status;
	// 创建人
	private String creator;
	// 所属机构
	private String ownorg;
	// 创建时间
	private Date creatTime;
	// 操作用户
	private String operator;
	// 操作机构
	private String operorg;
	// 操作时间
	private Date operTime;
	// 资料数量
	@CsvExport(value = "资料数量")
	private Long quantity;
	// 报告数量
	@CsvExport(value = "报告数量")
	private Long queryNum;
	//备注
	@CsvExport(value = "备注")
	private String remarks;

	public CpqArchiveView() {
	}

	public CpqArchiveView(String id, String clientName, String cretType, String cretNo, String archiveType, Date startDate, Date expireDate, Date extendDate, String status, String creator, String ownorg, Date creatTime, String operator, String operorg, Date operTime, Long quantity, Long queryNum, String remarks) {
		this.id = id;
		this.clientName = clientName;
		this.cretType = cretType;
		this.cretNo = cretNo;
		this.archiveType = archiveType;
		this.startDate = startDate;
		this.expireDate = expireDate;
		this.extendDate = extendDate;
		this.status = status;
		this.creator = creator;
		this.ownorg = ownorg;
		this.creatTime = creatTime;
		this.operator = operator;
		this.operorg = operorg;
		this.operTime = operTime;
		this.quantity = quantity;
		this.queryNum = queryNum;
		this.remarks = remarks;
	}



	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CLIENT_NAME")
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Column(name = "CERT_TYPE")
	public String getCretType() {
		return cretType;
	}

	public void setCretType(String cretType) {
		this.cretType = cretType;
	}

	@Column(name = "CERT_NO")
	public String getCretNo() {
		return cretNo;
	}

	public void setCretNo(String cretNo) {
		this.cretNo = cretNo;
	}

	@Column(name = "ARCHIVE_TYPE")
	public String getArchiveType() {
		return archiveType;
	}

	public void setArchiveType(String type) {
		this.archiveType = type;
	}

	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "EXPIRE_DATE")
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "EXTEND_DATE")
	public Date getExtendDate() {
		return extendDate;
	}

	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "OWN_ORG")
	public String getOwnorg() {
		return ownorg;
	}

	public void setOwnorg(String ownorg) {
		this.ownorg = ownorg;
	}

	@Column(name = "CREAT_TIME")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	@Column(name = "OPERATOR")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPER_ORG")
	public String getOperorg() {
		return operorg;
	}

	public void setOperorg(String operorg) {
		this.operorg = operorg;
	}

	@Column(name = "OPER_TIME")
	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	@Column(name = "QUANTITY")
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Column(name = "QUERYNUM")
	public Long getQueryNum() {
		return queryNum;
	}

	public void setQueryNum(Long queryNum) {
		this.queryNum = queryNum;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);

	}

}
