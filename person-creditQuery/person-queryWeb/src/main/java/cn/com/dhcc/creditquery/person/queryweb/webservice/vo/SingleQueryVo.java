package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.SR0201001OfWebservice;

/** 
* @author lekang.liu
* @date 2018年3月26日
*
*/
@SR0201001OfWebservice
@XStreamAlias("SingleQuery")
public class SingleQueryVo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4117389565884438111L;
	
	//客户姓名
	@XStreamAlias("ClientName")
	@Length(max = 30, message = "ClientName长度最大为30;" )
	@NotBlank(message = "ClientName不能为空;")
	@NotNull(message = "ClientName不能为空;")
    private String clientName;
	
    //证件类型
	@XStreamAlias("ClientType")
	@NotBlank(message = "ClientType不能为空;")
	@NotNull(message = "ClientType不能为空;")
    private String clientType;
	
    //证件号码
	@XStreamAlias("ClientNo")
	@NotBlank(message = "ClientNo不能为空;")
	@NotNull(message = "ClientNo不能为空;")
    private String clientNo;
	
    //查询原因
	@XStreamAlias("Qryreason")
	@NotBlank(message = "Qryreason不能为空;")
	@NotNull(message = "Qryreason不能为空;")
    private String qryreason;
	
    //查询版式
	@XStreamAlias("QueryFormat")
    private String queryFormat;
	
    //查询员
	@XStreamAlias("QueryUser")
	@NotBlank(message = "QueryUser不能为空;")
	@NotNull(message = "QueryUser不能为空;")
    private String queryUser;
	
    //查询方式
	@XStreamAlias("QueryPattern")
    private String queryPattern;
	
    //系统标识
	@XStreamAlias("SystemLabel")
	@NotBlank(message = "SystemLabel不能为空;")
	@NotNull(message = "SystemLabel不能为空;")
    private String systemLabel;
	
    //关联业务数据
	@XStreamAlias("AssociateBusinessData")
	@Length(max = 30, message = "AssociateBusinessData长度最大为30;" )
    private String associateBusinessData;
	
    //报文类型
	@XStreamAlias("ReportType")
	@NotBlank(message = "ReportType不能为空;")
	@NotNull(message = "ReportType不能为空;")
    private String reportType;
	
	public SingleQueryVo(String clientName, String clientType, String clientNo, String qryreason, String queryFormat, String queryUser, String queryPattern, String systemLabel, String associateBusinessData, String reportType) {
		super();
		this.clientName = clientName;
		this.clientType = clientType;
		this.clientNo = clientNo;
		this.qryreason = qryreason;
		this.queryFormat = queryFormat;
		this.queryUser = queryUser;
		this.queryPattern = queryPattern;
		this.systemLabel = systemLabel;
		this.associateBusinessData = associateBusinessData;
		this.reportType = reportType;
	}


	public SingleQueryVo() {
		super();
	}


	public String getClientName() {
		return clientName;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


	public String getClientType() {
		return clientType;
	}


	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	public String getClientNo() {
		return clientNo;
	}


	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}


	public String getQryreason() {
		return qryreason;
	}


	public void setQryreason(String qryreason) {
		this.qryreason = qryreason;
	}


	public String getQueryFormat() {
		return queryFormat;
	}


	public void setQueryFormat(String queryFormat) {
		this.queryFormat = queryFormat;
	}


	public String getQueryUser() {
		return queryUser;
	}


	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}


	public String getQueryPattern() {
		return queryPattern;
	}


	public void setQueryPattern(String queryPattern) {
		this.queryPattern = queryPattern;
	}


	public String getSystemLabel() {
		return systemLabel;
	}


	public void setSystemLabel(String systemLabel) {
		this.systemLabel = systemLabel;
	}


	public String getAssociateBusinessData() {
		return associateBusinessData;
	}


	public void setAssociateBusinessData(String associateBusinessData) {
		this.associateBusinessData = associateBusinessData;
	}


	public String getReportType() {
		return reportType;
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}
