/**
 *  Copyright (c)  @date 2018年9月11日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.GenerationBatchQueryInfoRules;

/**
 * 
 * @author lekang.liu
 * @date 2018年9月11日
 */
@XStreamAlias("BatchQueryInfo")
@GenerationBatchQueryInfoRules
public class GenerationBatchQueryInfo implements Serializable {

	private static final long serialVersionUID = 4131769157862774323L;

	// 批量请求文件方式
	@XStreamAlias("BatchQueryFileMode")
	@Length(max = 6, message = "BatchQueryFileMode最大长度为6；")
	@NotBlank(message = "BatchQueryFileMode字段不可为空；")
	private String batchQueryFileMode;

	// 批量请求文件路径
	@XStreamAlias("BatchQueryFilePath")
	@Length(max = 100, message = "BatchQueryFilePath最大长度为100；")
	@NotBlank(message = "BatchQueryFilePath字段不可为空；")
	private String batchQueryFilePath;

	// 批量请求文件名称
	@XStreamAlias("BatchQueryFileName")
	@Length(max = 30, message = "BatchQueryFileName最大长度为30；")
	@NotBlank(message = "BatchQueryFileName字段不可为空；")
	private String batchQueryFileName;

	// 操作员
	@XStreamAlias("Operator")
	@Length(max = 14, message = "BatchQueryFileName最大长度为14；")
	@NotBlank(message = "Operator字段不可为空；")
	private String operator;

	// 报文格式
	@XStreamAlias("ReportType")
	@Length(max = 6, message = "ReportType最大长度为6；")
	@NotBlank(message = "ReportType不能为空;")
	private String reportType;

	// 查询时限
	@XStreamAlias("TimeBound")
	@Pattern(regexp = "^-?\\d+$", message="TimeBound只能是整数；")
	@NotBlank(message = "TimeBound不能为空;")
	private String timeBound;
	
	@XStreamAlias("SystemLabel")
	@NotBlank(message = "SystemLabel不能为空;")
	private String SystemLabel;

	public GenerationBatchQueryInfo() {
	}


	public GenerationBatchQueryInfo(String batchQueryFileMode, String batchQueryFilePath, String batchQueryFileName, String operator, String reportType, String timeBound,
	        String systemLabel) {
		this.batchQueryFileMode = batchQueryFileMode;
		this.batchQueryFilePath = batchQueryFilePath;
		this.batchQueryFileName = batchQueryFileName;
		this.operator = operator;
		this.reportType = reportType;
		this.timeBound = timeBound;
		this.SystemLabel = systemLabel;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
	
	public String getSystemLabel() {
		return SystemLabel;
	}

	public void setSystemLabel(String systemLabel) {
		SystemLabel = systemLabel;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
