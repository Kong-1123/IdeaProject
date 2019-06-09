/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.entity;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 征信用户实体类
 * @author sjk
 * @date 2019年2月21日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="CQ_CREDIT_USER_TWO")
public class CeqCcUserView implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	/**
	 * 征信用户
	 */
	@Column(name="CREDIT_NAME",nullable=false, length=32)
	private String creditName;
	/**
	 * 征信用户账号
	 */
	@Column(name="CREDIT_USER",unique = true, nullable = false, length = 32)
	private String creditUser;
	/**
	 * 密码 加密
	 */
	@Column(name="CREDIT_PASSWORD",nullable=false, length=80)
	private String creditPassword;
	/**
	 * 征信用户所属机构
	 */
	@Column(name="CREDIT_ORG_CODE",nullable=false, length=14)
	private String creditOrgCode;
	/**
	 * 创建用户
	 */
	@Column(name="CREATE_USER", length=50)
	private String createUser;
	/**
	 * 创建用户所属机构
	 */
	@Column(name="DEPT_CODE", length=14)
	private String deptCode;
	/**
	 * 创建时间
	 */
	@Column(name="CREATE_DATE")
	private Date createDate;
	/**
	 * 更新用户
	 */
	@Column(name="UPDATE_USER")
	private String updateUser;
	/**
	 * 更新时间
	 */
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	/**
	 *  服务器网络配置
	 */
	@Column(name="MACHINE_NETWORK")
	private String machineNetwork;
	/**
	 * 服务器磁盘序号
	 */
	@Column(name="MACHINE_DISK")
	private String machineDisk;
	/**
	 * 服务器cpu
	 */
	@Column(name="MACHINE_CPU")
	private String machineCPU;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
