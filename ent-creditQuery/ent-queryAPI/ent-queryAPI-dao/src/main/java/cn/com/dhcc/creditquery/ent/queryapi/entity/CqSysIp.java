/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * <子系统ip地址表实体类>
 * @author guoshihu
 * @date 2019年1月17日
 */
@Entity
@Table(name = "CQ_SYS_IP")
public class CqSysIp  implements Serializable{
	
	private static final long serialVersionUID = 7703801172531017342L;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	@Column(name = "SYS_CODE",  nullable = false, length = 50)
	private String sysCode;  
	@Column(name = "IP_ADDR",  nullable = false, length = 15)
	private String ipAddr;   
	@Column(name = "STATUS",  nullable = false, length = 1)
	private String status;  
	@Column(name = "CREATE_USER", length = 50)
	private String createUser;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "UPDATE_USER", length = 50)
	private String updateUser;
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	private String ex1;       
	private String ex2;
	
	

	public CqSysIp() {
		super();
	}
	public CqSysIp(String id, String sysCode, String ipAddr, String status, String createUser, Date createDate,
			String updateUser, Date updateDate, String ex1, String ex2) {
		super();
		this.id = id;
		this.sysCode = sysCode;
		this.ipAddr = ipAddr;
		this.status = status;
		this.createUser = createUser;
		this.createDate = createDate;
		this.updateUser = updateUser;
		this.updateDate = updateDate;
		this.ex1 = ex1;
		this.ex2 = ex2;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getEx1() {
		return ex1;
	}
	public void setEx1(String ex1) {
		this.ex1 = ex1;
	}
	public String getEx2() {
		return ex2;
	}
	public void setEx2(String ex2) {
		this.ex2 = ex2;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
	
	
}
