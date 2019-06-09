/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import cn.com.dhcc.query.creditquerycommon.util.excle.util.ExcelForClass;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.ExcelForFeild;


/**
*   <保存审批请求记录信息 >
* @author lekang.liu
* @date 2018年3月7日
*/
@Entity
@Table(name = "CPQ_CHECKINFO")
@ExcelForClass(sheetName = "系统参数列表")
public class CpqCheckInfoView implements Serializable{

    private static final long serialVersionUID = 8020649712918282353L;
    
    //主键   
    private String id;
    @ExcelForFeild(value = "被查询客户姓名",sort = 1)
    //客户姓名
    private String clientName;
    
    //证件类型
    private String clientType;
    @ExcelForFeild(value = "被查询客户证件号码",sort = 2)
    //证件号码
    private String clientNo;
    @ExcelForFeild(value = "查询用途",sort = 3)
    //查询原因
    private String qryreason;
    
    //查询版式
    private String queryFormat;

    //查询类型
    private String queryType;
    @ExcelForFeild(value = "查询时间",sort = 0)
    //查询时间
    private Date queryTime;
    
    //审批状态
    private String status;
    
    //审批拒绝原因
    private String refuseReasons;
    
    //关联档案编号
    private String archiveId;
    
    private String assocbsnssData;
    
	//操作时间
    private Date operTime;
    @ExcelForFeild(value = "查询人",sort = 4)
    //审批请求人
    private String operAtor;
    @ExcelForFeild(value = "查询人所在机构",sort = 5)
    //审批请求机构
    private String operOrg;
    @ExcelForFeild(value = "查询人主管",sort = 6)
    //审批用户
    private String rekUser;
    
    //审批机构
    private String rekOrg;
    
    //审批时间
    private Date rekTime;
    
    //审批类型
    private String rekType;
    
    private String creditUser;
    
	//请求客户端IP
	private String clientIp;
	//查询策略1-本地;2-人行
	private String source;
	@ExcelForFeild(value = "所在机构主管行长",sort = 7)
	@Transient
	private String president;
	 @ExcelForFeild(value = "备注",sort = 8)
	@Transient
	private String remarks;
	@Transient
	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	}
	@Transient
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 结果类型
	 */
	private String resultType;

    public CpqCheckInfoView() {
    }

	public CpqCheckInfoView(String id, String clientName, String clientType, String clientNo, String qryreason,
			String queryFormat, String queryType, Date queryTime, String status, String refuseReasons, String archiveId,
			String assocbsnssData, Date operTime, String operAtor, String operOrg, String rekUser, String rekOrg,
			Date rekTime, String rekType, String creditUser, String clientIp, String source, String resultType) {
		super();
		this.id = id;
		this.clientName = clientName;
		this.clientType = clientType;
		this.clientNo = clientNo;
		this.qryreason = qryreason;
		this.queryFormat = queryFormat;
		this.queryType = queryType;
		this.queryTime = queryTime;
		this.status = status;
		this.refuseReasons = refuseReasons;
		this.archiveId = archiveId;
		this.assocbsnssData = assocbsnssData;
		this.operTime = operTime;
		this.operAtor = operAtor;
		this.operOrg = operOrg;
		this.rekUser = rekUser;
		this.rekOrg = rekOrg;
		this.rekTime = rekTime;
		this.rekType = rekType;
		this.creditUser = creditUser;
		this.clientIp = clientIp;
		this.source = source;
		this.resultType = resultType;
	}






	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "CLIENT_NAME")
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    @Column(name = "CERT_TYPE")
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
    
    @Column(name = "CERT_NO")
    public String getClientNo() {
        return clientNo;
    }
    
    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }
    
    @Column(name = "QRY_REASON")
    public String getQryreason() {
        return qryreason;
    }

    public void setQryreason(String qryreason) {
        this.qryreason = qryreason;
    }
    
    @Column(name = "QUERY_FORMAT")
    public String getQueryFormat() {
        return queryFormat;
    }

    public void setQueryFormat(String queryFormat) {
        this.queryFormat = queryFormat;
    }
    
    @Column(name = "QUERY_TYPE")
    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
    @Column(name = "QUERY_TIME")
    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "REFUSE_REASONS")
    public String getRefuseReasons() {
        return refuseReasons;
    }

    public void setRefuseReasons(String refuseReasons) {
        this.refuseReasons = refuseReasons;
    }
    @Column(name = "ARCHIVE_ID")
    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }
    @Column(name = "ASSOCBSNSS_DATA")
    public String getAssocbsnssData() {
		return assocbsnssData;
	}

	public void setAssocbsnssData(String assocbsnssData) {
		this.assocbsnssData = assocbsnssData;
	}
	
    @Column(name = "OPER_TIME")
    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
    @Column(name = "OPERATOR")
    public String getOperAtor() {
        return operAtor;
    }

    public void setOperAtor(String operAtor) {
        this.operAtor = operAtor;
    }
    @Column(name = "OPER_ORG")
    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }
    @Column(name = "REK_USER")
    public String getRekUser() {
        return rekUser;
    }

    public void setRekUser(String rekUser) {
        this.rekUser = rekUser;
    }
    @Column(name = "REK_ORG")
    public String getRekOrg() {
        return rekOrg;
    }

    public void setRekOrg(String rekOrg) {
        this.rekOrg = rekOrg;
    }
    @Column(name = "REK_TIME")
    public Date getRekTime() {
        return rekTime;
    }

    public void setRekTime(Date rekTime) {
        this.rekTime = rekTime;
    }

    @Column(name = "REK_TYPE")
    public String getRekType() {
        return rekType;
    }

    public void setRekType(String rekType) {
        this.rekType = rekType;
    }
    
    @Column(name = "CREDIT_USER")
	public String getCreditUser() {
		return creditUser;
	}

	public void setCreditUser(String creditUser) {
		this.creditUser = creditUser;
	}
	
	@Column(name = "CLIENT_IP", length = 30)
	public String getClientIp() {
		return clientIp;
	}


	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	@Column(length = 1)
	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "RESULT_TYPE", length = 1)
	public String getResultType() {
		return this.resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
		
	}
}







