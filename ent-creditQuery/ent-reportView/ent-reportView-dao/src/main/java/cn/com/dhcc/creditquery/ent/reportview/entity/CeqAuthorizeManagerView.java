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
 * 授权信息记录业务实体类
 * @author sjk
 * @date 2019年2月21日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "CEQ_ARCHIVE")
public class CeqAuthorizeManagerView implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;

	/**
	 * 中征码
	 */
	@Column(name = "LOANCARD_CODE")
	private String signCode;
	/**
	 * 机构信用码
	 */
	@Column(name = "ORG_CREDIT_CODE",nullable = false,length = 18)
	private String orgCreditCode;
	/**
	 * 企业名称
	 */
	@Column(name = "CLIENT_NAME")
	private String enterpriseName;
	/**
	 * 	组织机构代码
	 */
	@Column(name = "COPR_CODE")
	private String orgInstCode;
	/**
	 * 	统一社会信用代码
	 */
	@Column(name = "US_CREDIT_CODE")
	private String uniformSocialCredCode;
	
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
	 * 	登记注册号
	 */
	@Column(name = "FRGCORPNO")
	private String frgCorpNo;
	
	/**
	 * 	登记注册类型
	 */
	@Column(name = "REGI_TYPE_CODE")
	private String regiTypeCode;
	
	/**
	 * 档案类型(1-电子档案，2-纸质档案，3-影像档案)
	 */
	@Column(name = "ARCHIVE_TYPE")
	private String archiveType;
	/**
	 * 授权起始日
	 */
	@Column(name = "START_DATE")
	private Date startDate;
	/**
	 * 授权到期日
	 */
	@Column(name = "EXPIRE_DATE")
	private Date expireDate;
	/**
	 * 展期到期日
	 */
	@Column(name = "EXTEND_DATE")
	private Date extendDate;
	/**
	 * 档案状态 (1-有效，2-无资料)
	 */
	@Column(name = "STATUS")
	private String status;
	/**
	 * 创建人
	 */
	@Column(name = "CREATOR")
	private String creator;
	/**
	 * 所属机构
	 */
	@Column(name = "OWN_ORG")
	private String ownorg;
	/**
	 * 创建时间
	 */
	@Column(name = "CREAT_TIME")
	private Date creatTime;
	/**
	 * 操作用户
	 */
	@Column(name = "OPERATOR")
	private String operator;
	/**
	 * 操作机构
	 */
	@Column(name = "OPER_ORG")
	private String operorg;
	/**
	 * 操作时间
	 */
	@Column(name = "OPER_TIME")
	private Date operTime;
	/**
	 * 资料数量
	 */
	@Column(name = "QUANTITY")
	private Long quantity;
	/**
	 * 报告数量
	 */
	@Column(name = "QUERYNUM")
	private Long queryNum;
	/**
	 * 备注
	 */
	@Column(name = "REMARKS")
	private String remarks;

	/**
	 * 组织机构代码
	 */
	@Column(name = "CORPNO", length = 18)
	private String corpNo;
	
	/**
	 * 请求编号
	 */
	@Column(name = "REQ_ID")
	private String  reqId;
	
	/**
	 * EXT1 - EXT6 
	 * *预留字段
	 */
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