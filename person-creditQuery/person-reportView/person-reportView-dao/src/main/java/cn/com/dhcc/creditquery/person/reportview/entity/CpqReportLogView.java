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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the CPQ_PRINTFLOW database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="CPQ_REPORTLOG")
public class CpqReportLogView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8700752344055150621L;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;//ID
	@Column(name="CERT_NO", nullable=false, length=18)
	private String certNo;//证件号码
	@Column(name="CERT_TYPE", nullable=false, length=1)
	private String certType;//证件类型
	@Column(name="CLIENT_NAME", nullable=false, length=128)
	private String clientName;//客户姓名
	@Column(name="OPERATE_DATE")
	private Date operateDate;//打印报告时间
	@Column(name="OPERATE_USER", length=50)
	private String operateUser;//打印报告用户
	@Column(name="OPERATE_DEPT", length=16)
	private String operateDept;//打印报告用户所属机构
	@Column(name="QUERY_FORMAT", length=1)
	private String queryFormat;//查询版式
	@Column(name="QUERY_USER", length=50)
	private String queryUser;//信用报告查询人
	@Column(name="QUERY_USER_DEPT", length=16)
	private String queryUserDept;//信用报告查询人所属机构
	@Column(name="RECHECK_DEPT", length=16)
	private String recheckDept;//审批机构
	@Column(name="RECHECK_TYPE", length=1)
	private String recheckType;//审批类型
	@Column(name="RECHECK_USER", length=50)
	private String recheckUser;//审批人
	@Column(name="VALIDATE_INFO", length=256)
	private String validateInfo;//验证信息
	@Column(name = "RECORD_ID", length = 32)
	private String recordId;
	@Column(name = "OPERATE_TYPE")
	private String operateType;
	
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