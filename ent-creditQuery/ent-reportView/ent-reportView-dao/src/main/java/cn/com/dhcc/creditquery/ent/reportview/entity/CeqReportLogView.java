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
 * The persistent class for the CEQ_PRINTFLOW database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "CEQ_REPORTLOG")
public class CeqReportLogView implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;// ID
	
	@Column(name = "CLIENT_NAME", nullable = false, length = 300)
	private String enterpriseName;// 企业名称
	
	@Column(name = "ORG_CREDIT_CODE", nullable = false, length = 18)
	private String orgCreditCode;// 机构信用代码
	
	@Column(name = "LOANCARD_CODE", length = 60)
	private String signCode;// 中征码

	@Column(name = "US_CREDIT_CODE")
	private String uniformSocialCredCode;// 统一社会信用代码
	
	@Column(name = "COPR_CODE")
	private String orgInstCode;// 组织机构代码
	
	@Column(name = "OPERATE_DATE")
	private Date operateDate;// 操作时间
	
	@Column(name = "OPERATE_USER")
	private String operateUser;// 操作用户
	
	@Column(name = "OPERATE_DEPT")
	private String operateDept;// 操作用户所属机构
	
	@Column(name = "QUERY_FORMAT", length = 1)
	private String queryFormat;// 查询版式
	
	@Column(name = "QUERY_USER", length = 50)
	private String queryUser;// 信用报告查询人
	
	@Column(name = "QUERY_USER_DEPT", length = 16)
	private String queryUserDept;// 信用报告查询人所属机构
	
	@Column(name = "RECHECK_DEPT", length = 16)
	private String recheckDept;// 审批机构
	
	@Column(name = "RECHECK_TYPE", length = 1)
	private String recheckType;// 审批类型
	
	@Column(name = "RECHECK_USER", length = 50)
	private String recheckUser;// 审批人
	
	@Column(name = "VALIDATE_INFO", length = 256)
	private String validateInfo;// 验证信息
	
	@Column(name = "RECORD_ID", length = 32)
	private String recordId;// 查询记录id
	
	@Column(name = "OPERATE_TYPE")
	private String operateType;// 操作类型


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);

	}

}