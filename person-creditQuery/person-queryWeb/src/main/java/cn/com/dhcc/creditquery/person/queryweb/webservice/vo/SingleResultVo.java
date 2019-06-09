package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author lekang.liu
 * @date 2018年3月26日
 *
 */
@XStreamAlias("SingleResult")
public class SingleResultVo implements Serializable {
	private static final long serialVersionUID = 8104278110678270422L;

	// 结果代码
	@XStreamAlias("Code")
	private String code;
	// 返回信息
	@XStreamAlias("Message")
	private String message;
	// html报告
	@XStreamAlias("HtmlReport")
	private String htmlReport;
	// XML报告
	@XStreamAlias("XmlReport")
	private String xmlReport;
	// PDF报告
	@XStreamAlias("PdfReport")
	private String pdfReport;
	// 查询记录编号
	@XStreamAlias("ResultNo")
	private String resultNo;
	// 信用报告来源
	@XStreamAlias("ReportSource")
	private String reportSource;
	// 档案ID
	@XStreamAlias("ArvhiceId")
	private String archiveId;

	public SingleResultVo(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public SingleResultVo(String code, String message, String htmlReport, String xmlReport, String pdfReport, String resultNo, String reportSource, String archiveId) {
		super();
		this.code = code;
		this.message = message;
		this.htmlReport = htmlReport;
		this.xmlReport = xmlReport;
		this.pdfReport = pdfReport;
		this.resultNo = resultNo;
		this.reportSource = reportSource;
		this.archiveId = archiveId;
	}

	public SingleResultVo() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHtmlReport() {
		return htmlReport;
	}

	public void setHtmlReport(String htmlReport) {
		this.htmlReport = htmlReport;
	}

	public String getXmlReport() {
		return xmlReport;
	}

	public void setXmlReport(String xmlReport) {
		this.xmlReport = xmlReport;
	}

	public String getPdfReport() {
		return pdfReport;
	}

	public void setPdfReport(String pdfReport) {
		this.pdfReport = pdfReport;
	}

	public String getResultNo() {
		return resultNo;
	}

	public void setResultNo(String resultNo) {
		this.resultNo = resultNo;
	}

	public String getReportSource() {
		return reportSource;
	}

	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}
}
