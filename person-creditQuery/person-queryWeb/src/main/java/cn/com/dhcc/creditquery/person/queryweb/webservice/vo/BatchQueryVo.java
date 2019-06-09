package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.SR0101001OfWebservice;

/**
 * 
 * @author lekang.liu
 * @date 2018年7月26日
 */
@SR0101001OfWebservice
@XStreamAlias("BatchQuery")
public class BatchQueryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 批量请求文件方式
	@XStreamAlias("BatchQueryFileMode")
	@Length(max = 6, message = "BatchQueryFileMode最大长度为6；")
	@NotNull(message = "BatchQueryFileMode字段不可为空；")
	@NotBlank(message = "BatchQueryFileMode字段不可为空；")
	private String batchQueryFileMode;

	// 批量请求文件路径
	@XStreamAlias("BatchQueryFilePath")
	@Length(max = 100, message = "BatchQueryFilePath最大长度为100；")
	@NotNull(message = "BatchQueryFilePath字段不可为空；")
	@NotBlank(message = "BatchQueryFilePath字段不可为空；")
	private String batchQueryFilePath;

	// 批量请求文件名称
	@XStreamAlias("BatchQueryFileName")
	@Length(max = 60, message = "BatchQueryFileName最大长度为60；")
	@NotNull(message = "BatchQueryFileName字段不可为空；")
	@NotBlank(message = "BatchQueryFileName字段不可为空；")
	private String batchQueryFileName;

	// 批量请求文件格式
	@XStreamAlias("BatchQueryFileFormat")
	@Length(max = 6, message = "BatchQueryFileFormat最大长度为6；")
	@NotNull(message = "BatchQueryFileFormat字段不可为空；")
	@NotBlank(message = "BatchQueryFileFormat字段不可为空；")
	private String batchQueryFileFormat;

	// 查询员
	@XStreamAlias("QueryUser")
	@NotNull(message = "QueryUser字段不可为空；")
	@NotBlank(message = "QueryUser字段不可为空；")
	private String queryUser;

	// 系统标识
	@XStreamAlias("SystemLabel")
	@NotBlank(message = "SystemLabel不能为空;")
	@NotNull(message = "SystemLabel不能为空;")
	private String systemLabel;

	// 报文格式
	@XStreamAlias("ReportType")
	@NotBlank(message = "ReportType不能为空;")
	@NotNull(message = "ReportType不能为空;")
	private String reportType;

	// 查询时限
	@XStreamAlias("TimeBound")
	@NumberFormat
	private String timeBound;

	public BatchQueryVo() {
	}

	public BatchQueryVo(String batchQueryFileMode, String batchQueryFilePath, String batchQueryFileName, String batchQueryFileFormat, String queryUser, String systemLabel, String reportType,
	        String timeBound) {
		this.batchQueryFileMode = batchQueryFileMode;
		this.batchQueryFilePath = batchQueryFilePath;
		this.batchQueryFileName = batchQueryFileName;
		this.batchQueryFileFormat = batchQueryFileFormat;
		this.queryUser = queryUser;
		this.systemLabel = systemLabel;
		this.reportType = reportType;
		this.timeBound = timeBound;
	}

	public String getBatchQueryFileMode() {
		return batchQueryFileMode;
	}

	public void setBatchQueryFileMode(String batchQueryFileMode) {
		this.batchQueryFileMode = batchQueryFileMode;
	}

	public String getBatchQueryFilePath() {
		return batchQueryFilePath;
	}

	public void setBatchQueryFilePath(String batchQueryFilePath) {
		this.batchQueryFilePath = batchQueryFilePath;
	}

	public String getBatchQueryFileName() {
		return batchQueryFileName;
	}

	public void setBatchQueryFileName(String batchQueryFileName) {
		this.batchQueryFileName = batchQueryFileName;
	}

	public String getBatchQueryFileFormat() {
		return batchQueryFileFormat;
	}

	public void setBatchQueryFileFormat(String batchQueryFileFormat) {
		this.batchQueryFileFormat = batchQueryFileFormat;
	}

	public String getQueryUser() {
		return queryUser;
	}

	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}

	public String getSystemLabel() {
		return systemLabel;
	}

	public void setSystemLabel(String systemLabel) {
		this.systemLabel = systemLabel;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getTimeBound() {
		return timeBound;
	}

	public void setTimeBound(String timeBound) {
		this.timeBound = timeBound;
	}

}
