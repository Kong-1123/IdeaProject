package cn.com.dhcc.creditquery.ent.queryapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 企业单笔查询结果表--实体>
 * 
 * @author guoshihu
 * @date 2019年1月19日
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "ceq_single_result")
@XStreamAlias("SingleResult")
public class CeqSingleResult implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6169926732875049421L;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)	
	@XStreamOmitField
	private String id;
	/**
	 *  查询请求编号
	 */
	@XStreamAlias("reqID")
	@Column(name = "REQ_ID", length = 32)
	private String reqId;
	/**
	 *  处理结果代码
	 */
	@XStreamAlias("resCode")
	@Column(name = "RES_CODE", length = 6)
	private String resCode;
	/**
	 *  处理结果信息
	 */
	@XStreamAlias("resMsg")
	@Column(name = "RES_MSG", length = 400)
	private String resMsg;
	/**
	 *  html格式信用报告
	 */
	@XStreamAlias("htmlStr")
	@Column(name = "HTML_STR", length = 512)
	private String htmlStr;
	/**
	 *  xml格式信用报告
	 */
	@XStreamAlias("xmlStr")
	@Column(name = "XML_STR", length = 512)
	private String xmlStr;
	/**
	 *  json格式信用报告
	 */
	@XStreamAlias("jsonStr")
	@Column(name = "JSON_STR", length = 512)
	private String jsonStr;
	/**
	 *  pdf格式信用报告
	 */
	@XStreamAlias("pdfStr")
	@Column(name = "PDF_STR", length = 512)
	private String pdfStr;
	/**
	 *  信用报告来源
	 */
	@XStreamAlias("reportSource")
	@Column(name = "REPORT_SOURCE", length = 1)
	private String reportSource;
	/**
	 *  信用报告版本
	 */
	@XStreamAlias("reportVersion")
	@Column(name = "REPORT_VERSION", length = 5)
	private String reportVersion;
	/**
	 *  查询记录编号
	 */
	@XStreamAlias("creditreportNo")
	@Column(name = "CREDITREPORT_NO", length = 32)
	private String creditreportNo;
	/**
	 *  html报告md5
	 */
	@XStreamAlias("htmlMd5")
	@Column(name = "HTML_MD5", length = 32)
	private String htmlMd5;
	/**
	 *  xml报告md5
	 */
	@XStreamAlias("xmlMd5")
	@Column(name = "XML_MD5", length = 32)
	private String xmlMd5;
	/**
	 *  json报告md5
	 */
	@XStreamAlias("jsonMd5")
	@Column(name = "JSON_MD5", length = 32)
	private String jsonMd5;
	/**
	 *  pdf报告md5
	 */
	@XStreamAlias("pdfMd5")
	@Column(name = "PDF_MD5", length = 32)
	private String pdfMd5;
	/**
	 *  征信中心查询耗时
	 */
	@XStreamAlias("useTime")
	@Column(name = "USE_TIME", length = 6)
	private String useTime;
	
	/**
	 *  批量查询批次号
	 */
	@Column(name = "MSG_NO", length = 32)
	private String msgNo;
	
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