/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapproveflow.entity;

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
 * 	企业授权审批业务实体类
 * @author yahongcai
 * @date 2019年2月23日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="CEQ_CHECKINFO")
public class CeqApprove implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	// 企业名称
	@Column(name = "CLIENT_NAME", length = 240)
	private String enterpriseName;
	// 中征码
	@Column(name = "LOANCARD_CODE", length = 16)
	private String signCode;
	// 统一社会信用代码
	@Column(name = "US_CREDIT_CODE", length = 18)
	private String uniformSocialCredCode;
	// 组织机构代码
	@Column(name = "COPR_CODE", length = 10)
	private String orgInstCode;
	// 机构信用代码
	@Column(name = "ORG_CREDIT_CODE", length = 18)
	private String orgCreditCode;
	/**
	 * 	纳税人识别号(国税)
	 */
	@Column(name = "GS_REGI_CODE")
	private String gsRegiNo;
	/**
	 * 	纳税人识别号(地税)
	 */
	@Column(name = "DS_REGI_CODE")
	private String dsRegiNo;
	/**
	 * 	登记注册类型
	 */
	@Column(name = "REGI_TYPE_CODE")
	private String regiTypeCode;
	
	/**
	 * 	登记注册号
	 */
	@Column(name = "FRGCORPNO")
	private String frgCorpNo;
	
	// 关联档案id
	@Column(name = "ARCHIVE_ID", length = 32)
	private String archiveId;
	// 关联数据业务号
	@Column(name = "ASSOCBSNSS_DATA", length = 300)
	private String assocbsnssData;

	// 征信用户
	@Column(name = "CREDIT_USER", length = 50)
	private String creditUser;
	// 复合请求机构
	@Column(name = "OPER_ORG", nullable = false, length = 16)
	private String operOrg;
	// 操作时间
	@Column(name = "OPER_TIME", nullable = false)
	private Date operTime;
	// 审批请求人
	@Column(nullable = false, length = 50)
	private String operator;
	// 查询原因
	@Column(name = "QRY_REASON", nullable = false, length = 2)
	private String qryReason;
	// 查询板式
	@Column(name = "QUERY_FORMAT", nullable = false, length = 2)
	private String queryFormat;
	// 查询时间
	@Column(name = "QUERY_TIME", nullable = false)
	private Date queryTime;
	// 查询类型
	@Column(name = "QUERY_TYPE", nullable = false, length = 6)
	private String queryType;
	// 拒绝原因
	@Column(name = "REFUSE_REASONS", length = 450)
	private String refuseReasons;
	// 审批机构
	@Column(name = "REK_ORG", length = 16)
	private String rekOrg;
	// 审批时间
	@Column(name = "REK_TIME")
	private Date rekTime;
	// 审批类型
	@Column(name = "REK_TYPE", length = 1)
	private String rekType;
	// 审批用户
	@Column(name = "REK_USER", length = 50)
	private String rekUser;
	// 审批状态
	@Column(nullable = false, length = 1)
	private String status;
	// 请求客户端IP
	@Column(name = "CLIENT_IP", length = 30)
	private String clientIp;
	// 查询策略1-本地;2-人行
	@Column(length = 1)
	private String source;
	
	
	/**
	 * 结果类型
	 */
	@Column(name = "RESULT_TYPE")
	private String resultType;
	/**
	 * 请求编号
	 */
	@Column(name = "REQ_ID")
	private String  reqId;
	

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

	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}