/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean;

import java.io.Serializable;

import cn.com.dhcc.creditquery.person.queryweb.batchquery.rules.BatchReqBeanRules;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年6月29日
 *//*

@BatchReqBeanRules
public class BatchReqBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;// 被查询人姓名

	private String certtype;// 被查询人员证件类型

	private String certno;// 被查询人员证件号码

	private String qryreason;// 查询原因

	private String serviceCode;// 服务码
	
	private String operorg;// 查询机构

	private String operator;// 查询用户

	private String autharchiveid;// 档案编号

	private String assocbsnssdata;// 关联业务数据

	private String reqInfo;

	public String getOperorg() {
		return operorg;
	}

	public void setOperorg(String operorg) {
		this.operorg = operorg;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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


	public String getAutharchiveid() {
		return autharchiveid;
	}

	public void setAutharchiveid(String autharchiveid) {
		this.autharchiveid = autharchiveid;
	}

	public String getAssocbsnssdata() {
		return assocbsnssdata;
	}

	public void setAssocbsnssdata(String assocbsnssdata) {
		this.assocbsnssdata = assocbsnssdata;
	}

	public String getReqInfo() {
		return reqInfo;
	}

	public void setReqInfo(String reqInfo) {
		this.reqInfo = reqInfo;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Override
	public String toString() {
		return "BatchReqBean [operorg=" + operorg + ", operator=" + operator + ", name=" + name + ", certtype=" + certtype + ", certno=" + certno + ", qryreason=" + qryreason + ", serviceCode=" + serviceCode + ", autharchiveid=" + autharchiveid + ", assocbsnssdata=" + assocbsnssdata + ", reqInfo=" + reqInfo + "]";
	}

	*/
/**
	 * 重写equals方法用于set去重
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 *//*

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BatchReqBean) {
			BatchReqBean batchReqBean = (BatchReqBean) obj;
			if (batchReqBean.getName().equals(name) && batchReqBean.getCerttype().equals(certtype) && batchReqBean.getCertno().equals(certno) && batchReqBean.getServiceCode().equals(batchReqBean.getServiceCode())) {
				return true;
			}
		}
		return false;
	}

	*/
/*
	 * 重写hashCode方法用于set去重
	 * 
	 * @see java.lang.Object#hashCode()
	 *//*

	@Override
	public int hashCode() {
		return (null != name ? name.hashCode() : 0) + (null != certtype ? certtype.hashCode() : 0) + (null != certno ? certno.hashCode() : 0) + (null != serviceCode ? serviceCode.hashCode() : 0);
	}
	
	public void toBatchQueryDetail(CpqBatchquerydetail batchQueryDetail) {
		batchQueryDetail.setName(this.name);
		batchQueryDetail.setCerttype(this.certtype);
		batchQueryDetail.setCertno(this.certno);
		batchQueryDetail.setQryreason(this.qryreason);
		batchQueryDetail.setContentcode(this.serviceCode);
		batchQueryDetail.setOperorg(this.operorg);
		batchQueryDetail.setOperator(this.operator);
		batchQueryDetail.setAutharchiveid(this.autharchiveid);
		batchQueryDetail.setAutharchivedata(this.assocbsnssdata);
	}
	
}
*/
