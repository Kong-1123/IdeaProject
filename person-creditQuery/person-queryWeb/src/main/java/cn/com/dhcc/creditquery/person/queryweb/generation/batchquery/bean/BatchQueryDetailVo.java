/*
package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;


*/
/**
 * 
 * @author lekang.liu
 * @date 2018年9月12日
 *//*


@BatchQueryDetailVoRules
public class BatchQueryDetailVo implements Serializable {

	private static final long serialVersionUID = 8218870395873498377L;
	// 客户名称
	@Length(max = 30,message = "客户姓名最大30位;")
	@NotBlank(message = "客户姓名不能为空;")
	private String name;
	// 证件类型
	@Length(max = 1,message = "证件类型最大1位;")
	@NotBlank(message = "证件类型不能为空;")
	private String certtype;
	// 证件号码
	@Length(max = 18,message = "证件号码最大18位;")
	@NotBlank(message = "证件号码不能为空;")
	private String certno;
	// 查询原因
	@Length(max = 2,message = "查询原因最大2位;")
	@NotBlank(message = "查询原因不能为空;")
	private String qryreason;
	// 查询类型
	@Length(max = 1,message = "查询类型最大1位;")
	@NotBlank(message = "查询类型不能为空;")
	private String qrytype;
	// 查询用户
	@Length(max = 45,message = "查询用户最大45位;")
	@NotBlank(message = "查询用户不能为空;")
	private String operator;
	// 查询机构
	@Length(max = 14,message = "查询机构代码最大14位;")
	@NotBlank(message = "查询机构代码不能为空;")
	private String operorg;
	// 档案ID
	@Length(max = 300,message = "关联档案最大300位;")
	private String autharchiveid;
	// 批次号
	private String batchno;
	// 重复标识 （0：不重复，1：重复） 默认为空，即未重复
	private String dupflag;
	//错误标识 （0：无错误，1：错误 ）默认为空，即正确
	private String errorFlag;
	// 关联业务数据
	@Length(max = 30,message = "关联业务数据最大30位;")
	private String autharchivedata;
	// 查询版式
	@Length(max = 2,message = "查询版式最大2位;")
	@NotBlank(message = "查询版式不能为空;")
	private String queryFormat;
	
	private String errorCode;
	
	private String errorMsg;

	public BatchQueryDetailVo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCerttype() {
		return certtype;
	}

	public void setCerttype(String certtype) {
		this.certtype = certtype;
	}

	public String getCertno() {
		return certno;
	}

	public void setCertno(String certno) {
		this.certno = certno;
	}

	public String getQryreason() {
		return qryreason;
	}

	public void setQryreason(String qryreason) {
		this.qryreason = qryreason;
	}

	public String getQrytype() {
		return qrytype;
	}

	public void setQrytype(String qrytype) {
		this.qrytype = qrytype;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperorg() {
		return operorg;
	}

	public void setOperorg(String operorg) {
		this.operorg = operorg;
	}

	public String getAutharchiveid() {
		return autharchiveid;
	}

	public void setAutharchiveid(String autharchiveid) {
		this.autharchiveid = autharchiveid;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public String getDupflag() {
		return dupflag;
	}

	public void setDupflag(String dupflag) {
		this.dupflag = dupflag;
	}

	public String getAutharchivedata() {
		return autharchivedata;
	}

	public void setAutharchivedata(String autharchivedata) {
		this.autharchivedata = autharchivedata;
	}

	public String getQueryFormat() {
		return queryFormat;
	}

	public void setQueryFormat(String queryFormat) {
		this.queryFormat = queryFormat;
	}

	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchno == null) ? 0 : batchno.hashCode());
		result = prime * result + ((certno == null) ? 0 : certno.hashCode());
		result = prime * result + ((certtype == null) ? 0 : certtype.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchQueryDetailVo other = (BatchQueryDetailVo) obj;
		if (batchno == null) {
			if (other.batchno != null)
				return false;
		} else if (!batchno.equals(other.batchno))
			return false;
		if (certno == null) {
			if (other.certno != null)
				return false;
		} else if (!certno.equals(other.certno))
			return false;
		if (certtype == null) {
			if (other.certtype != null)
				return false;
		} else if (!certtype.equals(other.certtype))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	private void toCpqBatchquerydetail() {
		CpqBatchquerydetail batchquerydetail = new CpqBatchquerydetail();
		
		batchquerydetail.setName(name);
		batchquerydetail.setCerttype(certtype);
		batchquerydetail.setCertno(certno);
		batchquerydetail.setQryreason(qryreason);
		batchquerydetail.setQrytype(qrytype);
		batchquerydetail.setOperator(operator);
		batchquerydetail.setOperorg(operorg);
		batchquerydetail.setAutharchivedata(autharchivedata);
		batchquerydetail.setAutharchiveid(autharchiveid);
		batchquerydetail.setDupflag(dupflag);
		batchquerydetail.setErrorFlag(errorFlag);
		batchquerydetail.setQueryFormat(queryFormat);
		
	}

}*/
