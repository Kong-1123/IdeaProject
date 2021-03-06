/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.entity;

import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvExport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <查询结果实体类>
 * 
 * @author mingyu.Li @date：2018年3月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "CEQ_RESULTINFO")
public class CeqResultinfoFlowManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5840206694797642179L;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	/**
	 * 企业姓名
	 */
	@CsvExport(value = "企业名称")
	@Column(name = "CLIENT_NAME", nullable = false, length = 128)
	private String enterpriseName;
	/**
	 * 中征码
	 */
	@CsvExport(value = "中征码")
	@Column(name = "LOANCARD_CODE")
	private String signCode;
	/**
	 * 统一社会信用代码
	 */
	@CsvExport(value = "统一社会信用代码")
	@Column(name = "US_CREDIT_CODE")
	private String uniformSocialCredCode;
	/**
	 * 组织机构代码
	 */
	@CsvExport(value = "组织机构代码")
	@Column(name = "COPR_CODE")
	private String orgInstCode;
	/**
	 * 机构信用代码
	 */
	@CsvExport(value = "机构信用代码")
	@Column(name = "ORG_CREDIT_CODE")
	private String orgCreditCode;
	/**
	 * 纳税人识别号(国税)
	 */
	@CsvExport(value = "纳税人识别号(国税)")
	@Column(name = "GS_REGI_CODE")
	private String gsRegiNo;
	/**
	 * 纳税人识别号(地税)
	 */
	@CsvExport(value = "纳税人识别号(地税)")
	@Column(name = "DS_REGI_CODE")
	private String dsRegiNo;
	/**
	 * 登记注册类型
	 */
	@CsvExport(value = "登记注册类型")
	@Column(name = "REGI_TYPE_CODE")
	private String regiTypeCode;
	/**
	 * 登记注册号
	 */
	@CsvExport(value = "登记注册号")
	@Column(name = "FRGCORPNO")
	private String frgcorpno;
	/**
	 * 关联档案ID
	 */
	@CsvExport(value = "关联档案ID")
	@Column(name = "AUTHARCHIVE_ID", length = 32)
	private String autharchiveId;
	/**
	 * 查询原因
	 */
	@CsvExport(value = "查询原因", dicType = "qryReason")
	@Column(name = "QRY_REASON", length = 2)
	private String qryReason;
	/**
	 * 查询版式
	 */
	@CsvExport(value = "查询版式", dicType = "qryFormat")
	@Column(name = "QUERY_FORMAT", length = 1)
	private String queryFormat;
	/**
	 * 操作机构
	 */
	@CsvExport(value = "操作机构")
	@Column(name = "OPER_ORG", nullable = false, length = 14)
	private String operOrg;
	/**
	 * 操作用户
	 */
	@CsvExport(value = "操作用户")
	@Column(nullable = false, length = 50)
	private String operator;
	/**
	 * 征信用户
	 */
	@CsvExport(value = "征信用户")
	@Column(name = "CREDIT_USER", nullable = false, length = 45)
	private String creditUser;
	/**
	 * 金融机构代码
	 */
	@CsvExport(value = "金融机构代码")
	@Column(name = "QUERY_ORG", length = 14)
	private String queryOrg;
	/**
	 * 查询类型
	 */
	@CsvExport(value = "查询类型", dicType = "qryType")
	@Column(name = "QUERY_TYPE", length = 6)
	private String queryType;
	/**
	 * 查询要求时效
	 */
	@CsvExport(value = "查询要求时效")
	@Column(name = "QTIME_LIMIT", nullable = false, length = 16)
	private String qtimeLimit;
	/**
	 * 结果类型
	 */
	@CsvExport(value = "结果类型", dicType = "resultType")
	@Column(name = "RESULT_TYPE", length = 1)
	private String resultType;
	/**
	 * 查询人行通信模式
	 */
	@CsvExport(value = "查询人行通信模式", dicType = "queryMode")
	@Column(name = "QUERY_MODE", length = 1)
	private String queryMode;
	/**
	 * 数据状态
	 */
	@CsvExport(value = "数据状态", dicType = "qryStatus")
	@Column(name = "status", length = 1)
	private String status;
	/**
	 * 信用报告来源
	 * 
	 */
	@CsvExport(value = "信用报告来源", dicType = "reportSource")
	@Column(name = "source", length = 1)
	private String source;
	/**
	 * 人行登陆认证标识
	 */
	@CsvExport(value = "人行登录认证标识")
	@Column(name = "CERTIFICATION_MARK", length = 64)
	private String certificationMark;
	/**
	 * 批量标志
	 */
	@CsvExport(value = "批量标志", dicType = "bachType")
	@Column(name = "BATCH_FLAG", nullable = false, length = 1)
	private String batchFlag;
	/**
	 * 批量查询批次号
	 */
	@CsvExport(value = "批量查询批次号")
	@Column(name = "MSG_NO", length = 32)
	private String msgNo;
	/**
	 * 请求流水号
	 */
	@CsvExport(value = "请求流水号")
	@Column(name = "FLOW_ID", length = 32)
	private String flowId;
	/**
	 * 渠道编号
	 */
	@CsvExport(value = "渠道编号")
	@Column(name = "CHANNEL_ID", length = 2)
	private String channelId;
	/**
	 * 客户系统编号
	 */
	@CsvExport(value = "客户系统编号")
	@Column(name = "CSTMSYS_ID", length = 2)
	private String cstmsysId;
	/**
	 * 请求客户端IP
	 */
	@Column(name = "CLIENT_IP", length = 30)
	private String clientIp;
	/**
	 * 错误信息
	 */
	@CsvExport(value = "错误信息")
	@Column(name = "ERROR_INFO", length = 450)
	private String errorInfo;
	/**
	 * 查询时间
	 */
	@CsvExport(value = "查询时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "QUERY_TIME", nullable = false)
	private Date queryTime;
	/**
	 * 操作时间
	 */
	@CsvExport(value = "操作时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPDATE_TIME", nullable = false)
	private Date updateTime;
	/**
	 * 审批方式
	 */
	@CsvExport(value = "审批方式", dicType = "checkType")
	@Column(name = "CHECK_WAY", length = 2)
	private String checkWay;
	/**
	 * 相关联的业务数据
	 */
	@CsvExport(value = "备注")
	@Column(name = "ASSOCBSNSS_DATA", length = 50)
	private String assocbsnssData;
	/**
	 * 审批用户
	 */
	@CsvExport(value = "审批用户")
	@Column(name = "REK_USER", length = 50)
	private String rekUser;
	/**
	 * 审批机构
	 */
	@CsvExport(value = "审批机构")
	@Column(name = "REK_ORG", length = 14)
	private String rekOrg;
	/**
	 * 审批时间
	 */
	@CsvExport(value = "审批时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "REK_TIME")
	private Date rekTime;
	/**
	 * 审批请求ID
	 */
	@CsvExport(value = "审批请求ID")
	@Column(name = "CHECK_ID", length = 32)
	private String checkId;
	/**
	 * 信用报告id
	 */
	@Column(name = "CREDIT_ID", length = 32)
	private String creditId;
	/**
	 * 档案补录状态 1-无档案;2-有档案
	 */
	@Column(name = "ARCHIVE_REVISE", length = 1)
	private String archiveRevise;
	/**
	 * 查询用户密码
	 */
	@Column(name = "PASSWORD")
	private String PASSWORD;
	/**
	 * 信用报告版本
	 */
	@Column(name = "REPORT_VERSION", length = 32)
	private String reportVersion;
	/**
	 * 信用报告封装类型
	 */
	@Column(name = "REPORT_FORMAT", length = 32)
	private String reportFormat;
	/**
	 * 查询原因
	 */
	@Column(name = "QUERYREASON_ID", length = 32)
	private String queryReasonID;
	/**
	 * html信用报告存储路径
	 */
	@Column(name = "HTML_PATH", length = 200)
	private String htmlPath;
	/**
	 * xml信用报告存储路径
	 */
	@Column(name = "XML_PATH", length = 200)
	private String xmlPath;
	/**
	 * json信用报告存储路径
	 */
	@Column(name = "JSON_PATH", length = 200)
	private String jsonPath;
	/**
	 * pdf信用报告存储路径
	 */
	@Column(name = "PDF_PATH", length = 200)
	private String pdfPath;
	@Column(name = "CONTEXT_FILE_PATH", length = 200)
	private String contextFilePath;
	/**
	 * 接入系统请求发起用户
	 */
	@Column(name = "CALL_SYS_USER", length = 1)
	private String callSysUser;
	/**
	 * 接入系统审批员
	 */
	@Column(name = "RECHECK_USER_NAME", length = 1)
	private String recheckUserName;

	/**
	 * 征信中心查询耗时
	 */
	@Column(name = "USE_TIME", length = 5)
	private Integer useTime;

	@Column(name = "REPORT_ID", length = 32)
	private String reportId;
	
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

} 	