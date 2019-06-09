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

import org.hibernate.validator.constraints.Length;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cn.com.dhcc.query.queryapicommon.rules.EnumJudgeRules;

/**
 * <授权文件参数>
 * @author guoshihu
 * @date 2019年1月19日
 */
@XStreamAlias("file")
public class AuthorFileBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6169927732875049421L;
	
	@XStreamAlias("fileType")
	@EnumJudgeRules(message = "fileType不在字典中", type = "fileType", isPerson = false)
	@Length(max = 1, message = "fileType长度最大为1;")
	private String fileType;
	@XStreamAlias("fileFormat")
	@Length(max = 4, message = "fileFormat长度最大为4;")
	private String fileFormat;
	@XStreamAlias("fileContent")
	private String fileContent;
	@XStreamAlias("MD5")
	@Length(max = 32, message = "md5长度最大为32;")
	private String md5;
	@XStreamAlias("URL")
	@Length(max = 500, message = "url长度最大为500;")
	private String url;

	

	public AuthorFileBo() {
	}

	public AuthorFileBo(String fileType, String fileFormat, String fileContent, String md5, String url) {
		super();
		this.fileType = fileType;
		this.fileFormat = fileFormat;
		this.fileContent = fileContent;
		this.md5 = md5;
		this.url = url;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
   
}
