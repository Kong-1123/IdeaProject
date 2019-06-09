package cn.com.dhcc.creditquery.person.querystatistics.entity;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


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
@Table(name = "CPQ_RESULTINFO")
public class CpqResultinfoStatistics implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5151800334115830620L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	// 客户姓名
	@CsvExport(value = "客户姓名")
	@Column(name = "CLIENT_NAME", nullable = false, length = 128)
	private String customerName;

	// 证件类型
	@CsvExport(value = "证件类型", dicType = "idType")
	@Column(name = "CERT_TYPE", nullable = false, length = 1)
	private String certType;

	// 证件号码
	@CsvExport(value = "证件号码")
	@Column(name = "CERT_NO", nullable = false, length = 18)
	private String certNo;

	// 查询原因
	@CsvExport(value = "查询原因", dicType = "qryReason")
	@Column(name = "QRY_REASON", length = 2)
	private String qryReason;

	// 查询类型
	@CsvExport(value = "查询类型", dicType = "qryType")
	@Column(name = "QUERY_TYPE", length = 6)
	private String queryType;

	// 查询版式
	@CsvExport(value = "查询版式", dicType = "qryFormat")
	@Column(name = "QUERY_FORMAT", length = 1)
	private String queryFormat;
	// 结果类型
	@CsvExport(value = "结果类型", dicType = "resultType")
	@Column(name = "RESULT_TYPE", length = 1)
	private String resultType;

	/* =============二代查询信息==================== */
	// 信用报告版本
	@Column(name = "REPORT_VERSION", length = 32)
	private String reportVersion;
	// 信用报告封装类型
	@Column(name = "REPORT_FORMAT", length = 32)
	private String reportFormat;
	// 查询原因
	@Column(name = "QUERYREASON_ID", length = 32)
	private String queryReasonID;

	// 征信用户
	@CsvExport(value = "征信用户")
	@Column(name = "CREDIT_USER", nullable = false, length = 45)
	private String creditUser;
	// 金融机构代码
	@CsvExport(value = "金融机构代码")
	@Column(name = "QUERY_ORG", length = 14)
	private String queryOrg;

	// 操作机构
	@CsvExport(value = "操作机构")
	@Column(name = "OPER_ORG", nullable = false, length = 14)
	private String operOrg;
	// 操作用户
	@CsvExport(value = "操作用户")
	@Column(nullable = false, length = 50)
	private String operator;

	// 请求客户端IP
	@Column(name = "CLIENT_IP", length = 30)
	private String clientIp;

	// 请求流水号
	@CsvExport(value = "请求流水号")
	@Column(name = "FLOW_ID", length = 32)
	private String flowId;

	// 查询要求时效
	@CsvExport(value = "查询要求时效")
	@Column(name = "QTIME_LIMIT", nullable = false, length = 16)
	private String qtimeLimit;

	// 查询时间
	@CsvExport(value = "查询时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "QUERY_TIME", nullable = false)
	private Date queryTime;

	// 相关联的业务数据
	@CsvExport(value = "备注")
	@Column(name = "ASSOCBSNSS_DATA", length = 50)
	private String assocbsnssData;
	// 关联档案ID
	@CsvExport(value = "关联档案ID")
	@Column(name = "AUTHARCHIVE_ID", length = 32)
	private String autharchiveId;

	// 批量标志
	@CsvExport(value = "批量标志", dicType = "bachType")
	@Column(name = "BATCH_FLAG", nullable = false, length = 1)
	private String batchFlag;

	// 批量查询批次号
	@CsvExport(value = "批量查询批次号")
	@Column(name = "MSG_NO", length = 32)
	private String msgNo;

	// 人行登陆认证标识
	@CsvExport(value = "人行登录认证标识")
	@Column(name = "CERTIFICATION_MARK", length = 64)
	private String certificationMark;

	// 查询人行通信模式
	@CsvExport(value = "查询人行通信模式", dicType = "queryMode")
	@Column(name = "QUERY_MODE", length = 1)
	private String queryMode;

	// 信用报告来源
	@CsvExport(value = "信用报告来源", dicType = "reportSource")
	@Column(name = "source", length = 1)
	private String source;

	// 渠道编号
	@CsvExport(value = "渠道编号")
	@Column(name = "CHANNEL_ID", length = 2)
	private String channelId;

	// 客户系统编号
	@CsvExport(value = "客户系统编号")
	@Column(name = "CSTMSYS_ID", length = 2)
	private String cstmsysId;

	// 数据状态
	@CsvExport(value = "数据状态", dicType = "qryStatus")
	@Column(name = "status", length = 1)
	private String status;
	// 错误信息
	@CsvExport(value = "错误信息")
	@Column(name = "ERROR_INFO", length = 450)
	private String errorInfo;
	// 操作时间
	@CsvExport(value = "操作时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPDATE_TIME", nullable = false)
	private Date updateTime;

	// 审批请求ID
	@CsvExport(value = "审批请求ID")
	@Column(name = "CHECK_ID", length = 32)
	private String checkId;
	// 审批方式
	@CsvExport(value = "审批方式", dicType = "checkType")
	@Column(name = "CHECK_WAY", length = 2)
	private String checkWay;

	// 审批机构
	@CsvExport(value = "审批机构")
	@Column(name = "REK_ORG", length = 14)
	private String rekOrg;
	// 审批时间
	@CsvExport(value = "审批时间", dateFormatter = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "REK_TIME")
	private Date rekTime;
	// 审批用户
	@CsvExport(value = "审批用户")
	@Column(name = "REK_USER", length = 50)
	private String rekUser;

	// 信用报告id
	@Column(name = "CREDIT_ID", length = 32)
	private String creditId;

	
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

	// 档案补录状态
	// 1-无档案;2-有档案
	@Column(name = "ARCHIVE_REVISE", length = 1)
	private String archiveRevise;

	/**
	 * 接入系统请求发起用户
	 */
	@Column(name = "CALL_SYS_USER", length = 1)
	private String callSysUser;
	/**
	 * 征信中心查询耗时
	 */
	@Column(name = "USE_TIME", length = 5)
	private Integer useTime;
	
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