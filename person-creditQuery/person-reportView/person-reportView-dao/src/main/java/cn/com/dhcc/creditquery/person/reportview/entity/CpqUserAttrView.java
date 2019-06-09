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

/**
 * The persistent class for the CPQ_USER_ATTR database table.
 * 
 */
@Entity
@Table(name="CPQ_USER_ATTR")
public class CpqUserAttrView implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	// 是否限制查询量
	private String amountLimit;
	// 查询量
	private String amountQuery;
	// 创建时间
	private Date createDate;
	// 创建用户
	private String createUser;
	// 是否允许档案下载
	private String downloadPermit;
	// 打印次数
	private String printAmount;
	// 是否允许打印
	private String printPermit;
	// 是否允许保存
	private String savePermit;
	// 是否限制查询时间
	private String timeLimit;
	// 更新时间
	private Date updateDate;
	// 用户ID
	private String userName;
	// 查询时间
	private String workTime;
	// 是否绑定终端
	private String bindPermit;
	// 绑定终端数
	private String bindNumber;
	// 监管业务用户锁定
	private String lockStat;
	//征信用户
	private String creditUser;
	//二代征信用户
	private String seniorCreditUser;
	//扩展字段1
	private String userAttrDesc1;
	//扩展字段2
	private String userAttrDesc2;
	//扩展字段3
	private String userAttrDesc3;
	//扩展字段4
	private String userAttrDesc4;
	
	public CpqUserAttrView() {
	}

	public CpqUserAttrView(String id, String amountLimit, String amountQuery, Date createDate, String createUser,
			String downloadPermit, String printAmount, String printPermit, String savePermit, String timeLimit,
			Date updateDate, String userName, String workTime, String bindPermit, String bindNumber, String lockStat,
			String creditUser, String seniorCreditUser, String userAttrDesc1, String userAttrDesc2,
			String userAttrDesc3, String userAttrDesc4) {
		super();
		this.id = id;
		this.amountLimit = amountLimit;
		this.amountQuery = amountQuery;
		this.createDate = createDate;
		this.createUser = createUser;
		this.downloadPermit = downloadPermit;
		this.printAmount = printAmount;
		this.printPermit = printPermit;
		this.savePermit = savePermit;
		this.timeLimit = timeLimit;
		this.updateDate = updateDate;
		this.userName = userName;
		this.workTime = workTime;
		this.bindPermit = bindPermit;
		this.bindNumber = bindNumber;
		this.lockStat = lockStat;
		this.creditUser = creditUser;
		this.seniorCreditUser = seniorCreditUser;
		this.userAttrDesc1 = userAttrDesc1;
		this.userAttrDesc2 = userAttrDesc2;
		this.userAttrDesc3 = userAttrDesc3;
		this.userAttrDesc4 = userAttrDesc4;
	}

	@Column(name="BIND_PERMIT")
	public String getBindPermit() {
		return bindPermit;
	}


	public void setBindPermit(String bindPermit) {
		this.bindPermit = bindPermit;
	}

	@Column(name="BIND_NUMBER")
	public String getBindNumber() {
		return bindNumber;
	}


	public void setBindNumber(String bindNumber) {
		this.bindNumber = bindNumber;
	}

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="AMOUNT_LIMIT", length=2)
	public String getAmountLimit() {
		return this.amountLimit;
	}

	public void setAmountLimit(String amountLimit) {
		this.amountLimit = amountLimit;
	}

	@Column(name="AMOUNT_QUERY", length=50)
	public String getAmountQuery() {
		return this.amountQuery;
	}

	public void setAmountQuery(String amountQuery) {
		this.amountQuery = amountQuery;
	}


	@Column(name="CREATE_DATE")
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	@Column(name="CREATE_USER", length=50)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	@Column(name="DOWNLOAD_PERMIT", length=2)
	public String getDownloadPermit() {
		return this.downloadPermit;
	}

	public void setDownloadPermit(String downloadPermit) {
		this.downloadPermit = downloadPermit;
	}


	@Column(name="PRINT_AMOUNT", length=10)
	public String getPrintAmount() {
		return this.printAmount;
	}

	public void setPrintAmount(String printAmount) {
		this.printAmount = printAmount;
	}


	@Column(name="PRINT_PERMIT", length=2)
	public String getPrintPermit() {
		return this.printPermit;
	}

	public void setPrintPermit(String printPermit) {
		this.printPermit = printPermit;
	}


	@Column(name="SAVE_PERMIT", length=2)
	public String getSavePermit() {
		return this.savePermit;
	}

	public void setSavePermit(String savePermit) {
		this.savePermit = savePermit;
	}


	@Column(name="TIME_LIMIT", length=2)
	public String getTimeLimit() {
		return this.timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}


	@Column(name="UPDATE_DATE")
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Column(name="USER_NAME", length=50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Column(name="WORK_TIME", length=50)
	public String getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	
	@Column(name="LOCK_STAT")
	public String getLockStat() {
		return this.lockStat;
	}

	public void setLockStat(String lockStat) {
		this.lockStat = lockStat;
	}
	
    @Column(name = "CREDIT_USER")
	public String getCreditUser() {
		return creditUser;
	}

	public void setCreditUser(String creditUser) {
		this.creditUser = creditUser;
	}

	@Column(name = "SENIOR_CREDIT_USER")
	public String getSeniorCreditUser() {
		return seniorCreditUser;
	}

	public void setSeniorCreditUser(String seniorCreditUser) {
		this.seniorCreditUser = seniorCreditUser;
	}

	@Column(name = "USER_ATTR_DESC1")
	public String getUserAttrDesc1() {
		return userAttrDesc1;
	}

	public void setUserAttrDesc1(String userAttrDesc1) {
		this.userAttrDesc1 = userAttrDesc1;
	}

	@Column(name = "USER_ATTR_DESC2")
	public String getUserAttrDesc2() {
		return userAttrDesc2;
	}

	public void setUserAttrDesc2(String userAttrDesc2) {
		this.userAttrDesc2 = userAttrDesc2;
	}

	@Column(name = "USER_ATTR_DESC3")
	public String getUserAttrDesc3() {
		return userAttrDesc3;
	}

	public void setUserAttrDesc3(String userAttrDesc3) {
		this.userAttrDesc3 = userAttrDesc3;
	}

	@Column(name = "USER_ATTR_DESC4")
	public String getUserAttrDesc4() {
		return userAttrDesc4;
	}

	public void setUserAttrDesc4(String userAttrDesc4) {
		this.userAttrDesc4 = userAttrDesc4;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
		
	}

}