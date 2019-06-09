/**
 *  Copyright (c)  @date 2018年5月9日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author lekang.liu
 * @date 2018年5月9日
 */
public class IpAndMacVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2847780570192498190L;

	private String userName;

	private List<String> ip;

	private List<String> mac;

	public IpAndMacVo() {
	}

	public IpAndMacVo(String userName, List<String> ip, List<String> mac) {
		this.userName = userName;
		this.ip = ip;
		this.mac = mac;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getIp() {
		return ip;
	}

	public void setIp(List<String> ip) {
		this.ip = ip;
	}

	public List<String> getMac() {
		return mac;
	}

	public void setMac(List<String> mac) {
		this.mac = mac;
	}

}
