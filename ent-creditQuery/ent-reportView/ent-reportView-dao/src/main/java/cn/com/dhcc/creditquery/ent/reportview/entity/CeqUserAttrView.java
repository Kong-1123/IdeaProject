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
 * 用户属性业务实体类
 * @author sjk
 * @date 2019年2月21日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="CEQ_USER_ATTR")
public class CeqUserAttrView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	
	/**
	 * 用户ID
	 */
	@Column(name="USER_NAME", length=50)
	private String userName;
	/**
	 * 是否限制查询时间
	 */
	@Column(name="TIME_LIMIT", length=2)
	private String timeLimit;
	/**
	 * 查询时间
	 */
	@Column(name="WORK_TIME", length=50)
	private String workTime;
	/**
	 * 是否限制查询量
	 */
	@Column(name="AMOUNT_LIMIT", length=2)
	private String amountLimit;
	/**
	 * 查询量
	 */
	@Column(name="AMOUNT_QUERY", length=50)
	private String amountQuery;
	/**
	 * 是否允许保存
	 */
	@Column(name="SAVE_PERMIT", length=2)
	private String savePermit;
	/**
	 * 是否允许打印
	 */
	@Column(name="PRINT_PERMIT", length=2)
	private String printPermit;
	/**
	 * 打印次数
	 */
	@Column(name="PRINT_AMOUNT", length=10)
	private String printAmount;
	/**
	 * 是否允许档案下载
	 */
	@Column(name="DOWNLOAD_PERMIT", length=2)
	private String downloadPermit;
	/**
	 * 是否绑定终端(绑定限制)
	 */
	@Column(name="BIND_PERMIT")
	private String bindPermit;
	/**
	 * 绑定终端数
	 */
	@Column(name="BIND_NUMBER")
	private String bindNumber;
	/**
	 * 监管业务用户锁定
	 */
	@Column(name="LOCK_STAT")
	private String lockStat;
	/**
	 * 创建时间
	 */
	@Column(name="CREATE_DATE")
	private Date createDate;
	/**
	 * 创建用户
	 */
	@Column(name="CREATE_USER", length=50)
	private String createUser;
	/**
	 * 更新时间
	 */
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	/**
	 * 征信用户
	 */
	@Column(name = "CREDIT_USER")
	private String creditUser;
	/**
	 * 二代征信用户
	 *  - 无用 后续去掉
	 */
	@Column(name = "SENIOR_CREDIT_USER")
	private String seniorCreditUser;
	
	
	@Column(name = "EXT1", length = 60)
	private String ext1;

	@Column(name = "EXT2", length = 60)
	private String ext2;

	@Column(name = "EXT3", length = 60)
	private String ext3;

	@Column(name = "EXT4", length = 60)
	private String ext4;

	@Column(name = "EXT5", length = 60)
	private String ext5;

	@Column(name = "EXT6", length = 60)
	private String ext6;

	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
