package cn.com.dhcc.creditquery.person.queryflowmanager.ansytash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 个人单笔查询结果表--实体>
 * 
 * @author guoshihu
 * @date 2019年1月19日
 */
@Entity
@Table(name = "cpq_single_result")
@XStreamAlias("SingleResult")
public class CpqSingleResultTask implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6169926732875049421L;
	@XStreamOmitField
	private String id;
	/**
	 *  查询请求编号
	 */
	@XStreamAlias("reqID")
	private String reqId;
	/**
	 *  处理结果代码
	 */
	@XStreamAlias("resCode")
	private String resCode;
	/**
	 *  处理结果信息
	 */
	@XStreamAlias("resMsg")
	private String resMsg;
	/**
	 *  html格式信用报告
	 */
	@XStreamAlias("htmlStr")
	private String htmlStr;
	/**
	 *  xml格式信用报告
	 */
	@XStreamAlias("xmlStr")
	private String xmlStr;
	/**
	 *  json格式信用报告
	 */
	@XStreamAlias("jsonStr")
	private String jsonStr;
	/**
	 *  pdf格式信用报告
	 */
	@XStreamAlias("pdfStr")
	private String pdfStr;
	/**
	 *  信用报告来源
	 */
	@XStreamAlias("reportSource")
	private String reportSource;
	/**
	 *  信用报告版本
	 */
	@XStreamAlias("reportVersion")
	private String reportVersion;
	/**
	 *  查询记录编号
	 */
	@XStreamAlias("creditreportNo")
	private String creditreportNo;
	/**
	 *  html报告md5
	 */
	@XStreamAlias("htmlMd5")
	private String htmlMd5;
	/**
	 *  xml报告md5
	 */
	@XStreamAlias("xmlMd5")
	private String xmlMd5;
	/**
	 *  json报告md5
	 */
	@XStreamAlias("jsonMd5")
	private String jsonMd5;
	/**
	 *  pdf报告md5
	 */
	@XStreamAlias("pdfMd5")
	private String pdfMd5;
	/**
	 *  征信中心查询耗时
	 */
	@XStreamAlias("useTime")
	private String useTime;

	@XStreamOmitField
	private String msgNo;

	/** default constructor */
	public CpqSingleResultTask() {
	}


	public CpqSingleResultTask(String resCode, String resMsg) {
		super();
		this.resCode = resCode;
		this.resMsg = resMsg;
	}

	public CpqSingleResultTask(String id, String reqId, String resCode, String resMsg, String htmlStr, String xmlStr,
			String jsonStr, String pdfStr, String reportSource, String reportVersion, String creditreportNo,
			String htmlMd5, String xmlMd5, String jsonMd5, String pdfMd5, String useTime) {
		super();
		this.id = id;
		this.reqId = reqId;
		this.resCode = resCode;
		this.resMsg = resMsg;
		this.htmlStr = htmlStr;
		this.xmlStr = xmlStr;
		this.jsonStr = jsonStr;
		this.pdfStr = pdfStr;
		this.reportSource = reportSource;
		this.reportVersion = reportVersion;
		this.creditreportNo = creditreportNo;
		this.htmlMd5 = htmlMd5;
		this.xmlMd5 = xmlMd5;
		this.jsonMd5 = jsonMd5;
		this.pdfMd5 = pdfMd5;
		this.useTime = useTime;
	}

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "REQ_ID", length = 32)
	public String getReqId() {
		return this.reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	@Column(name = "RES_CODE", length = 6)
	public String getResCode() {
		return this.resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	@Column(name = "RES_MSG", length = 400)
	public String getResMsg() {
		return this.resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	@Column(name = "HTML_STR", length = 512)
	public String getHtmlStr() {
		return this.htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}

	@Column(name = "HTML_MD5", length = 32)
	public String getHtmlMd5() {
		return this.htmlMd5;
	}

	public void setHtmlMd5(String htmlMd5) {
		this.htmlMd5 = htmlMd5;
	}

	@Column(name = "XML_STR", length = 512)
	public String getXmlStr() {
		return this.xmlStr;
	}

	public void setXmlStr(String xmlStr) {
		this.xmlStr = xmlStr;
	}

	@Column(name = "XML_MD5", length = 32)
	public String getXmlMd5() {
		return this.xmlMd5;
	}

	public void setXmlMd5(String xmlMd5) {
		this.xmlMd5 = xmlMd5;
	}

	@Column(name = "JSON_STR", length = 512)
	public String getJsonStr() {
		return this.jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	@Column(name = "JSON_MD5", length = 32)
	public String getJsonMd5() {
		return this.jsonMd5;
	}

	public void setJsonMd5(String jsonMd5) {
		this.jsonMd5 = jsonMd5;
	}

	@Column(name = "PDF_STR", length = 512)
	public String getPdfStr() {
		return this.pdfStr;
	}

	public void setPdfStr(String pdfStr) {
		this.pdfStr = pdfStr;
	}

	@Column(name = "PDF_MD5", length = 32)
	public String getPdfMd5() {
		return this.pdfMd5;
	}

	public void setPdfMd5(String pdfMd5) {
		this.pdfMd5 = pdfMd5;
	}

	@Column(name = "REPORT_SOURCE", length = 1)
	public String getReportSource() {
		return this.reportSource;
	}

	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}

	@Column(name = "REPORT_VERSION", length = 5)
	public String getReportVersion() {
		return this.reportVersion;
	}

	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}

	@Column(name = "CREDITREPORT_NO", length = 32)
	public String getCreditreportNo() {
		return this.creditreportNo;
	}

	public void setCreditreportNo(String creditreportNo) {
		this.creditreportNo = creditreportNo;
	}

	@Column(name = "USE_TIME", length = 6)
	public String getUseTime() {
		return this.useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	
	@Column(name = "MSG_NO")
	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CpqSingleResultTask [id=" + id + ", reqId=" + reqId + ", resCode=" + resCode + ", resMsg=" + resMsg
				+ ", htmlStr=" + htmlStr + ", xmlStr=" + xmlStr + ", jsonStr=" + jsonStr + ", pdfStr=" + pdfStr
				+ ", reportSource=" + reportSource + ", reportVersion=" + reportVersion + ", creditreportNo="
				+ creditreportNo + ", htmlMd5=" + htmlMd5 + ", xmlMd5=" + xmlMd5 + ", jsonMd5=" + jsonMd5 + ", pdfMd5="
				+ pdfMd5 + ", useTime=" + useTime + ", msgNo=" + msgNo + "]";
	}

}