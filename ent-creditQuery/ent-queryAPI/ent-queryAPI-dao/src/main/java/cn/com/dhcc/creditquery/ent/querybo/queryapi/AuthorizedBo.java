/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.querybo.queryapi;

import java.io.Serializable;
import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import cn.com.dhcc.query.queryapicommon.rules.EnumJudgeRules;
import cn.com.dhcc.query.queryapicommon.util.converter.XstreamDateConverter;

/**
 * <授权参数开始>
 * @author guoshihu
 * @date 2019年1月19日
 */
public class AuthorizedBo implements Serializable {
	private static final long serialVersionUID = 6169927733875149421L;
	
	/**
	 * 授权类型
	 */
	@XStreamAlias("authorType")
	@EnumJudgeRules(message = "authorType不在字典中", type = "authorType", isPerson = false)
	@Length(max = 1, message = "authorType长度最大为1;")
	private String authorType;
	/**
	 *  授权编号
	 */
	@XStreamAlias("authorNum")
	@Length(max = 100, message = "authorNum长度最大为100;")
	private String authorNum;
	/**
	 *  授权起始日期
	 */
	@XStreamAlias("authorBeginDate")
	@XStreamConverter(value = XstreamDateConverter.class)
	private Date authorBeginDate;
	/**
	 *  授权结束日期
	 */
	@XStreamAlias("authorEndDate")
	@XStreamConverter(value = XstreamDateConverter.class)
	private Date authorEndDate;
	/**
	 *  影像平台url
	 */
	@XStreamAlias("authorizedURL")
	@Length(max = 500, message = "authorNum长度最大为500;")
	private String authorizedURL;
	
	@XStreamAlias("authorizedFile")
	@Valid
	private AuthorizedFileBo authorizedFile;

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getAuthorNum() {
		return authorNum;
	}

	public void setAuthorNum(String authorNum) {
		this.authorNum = authorNum;
	}

	public Date getAuthorBeginDate() {
		return authorBeginDate;
	}

	public void setAuthorBeginDate(Date authorBeginDate) {
		this.authorBeginDate = authorBeginDate;
	}

	public Date getAuthorEndDate() {
		return authorEndDate;
	}

	public void setAuthorEndDate(Date authorEndDate) {
		this.authorEndDate = authorEndDate;
	}

	public String getAuthorizedURL() {
		return authorizedURL;
	}

	public void setAuthorizedURL(String authorizedURL) {
		this.authorizedURL = authorizedURL;
	}

	

	public AuthorizedFileBo getAuthorizedFileBo() {
		return authorizedFile;
	}

	public void setAuthorizedFileBo(AuthorizedFileBo authorizedFile) {
		this.authorizedFile = authorizedFile;
	}

	
	public AuthorizedBo(String authorType, String authorNum, Date authorBeginDate, Date authorEndDate,
			String authorizedURL, AuthorizedFileBo authorizedFile) {
		super();
		this.authorType = authorType;
		this.authorNum = authorNum;
		this.authorBeginDate = authorBeginDate;
		this.authorEndDate = authorEndDate;
		this.authorizedURL = authorizedURL;
		this.authorizedFile = authorizedFile;
	}

	public AuthorizedBo() {
	}
	
	

}
