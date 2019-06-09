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
import java.util.List;

import javax.validation.Valid;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author guoshihu
 * @date 2019年1月21日
 */
public class AuthorizedFileBo implements Serializable {
	private static final long serialVersionUID = 6169927733875149521L;
	@XStreamImplicit
	@Valid
	private List<AuthorFileBo> authorfile;
	
	

	public AuthorizedFileBo() {
	}

	public AuthorizedFileBo(List<AuthorFileBo> authorfile) {
		super();
		this.authorfile = authorfile;
	}

	public List<AuthorFileBo> getAuthorfile() {
		return authorfile;
	}

	public void setAuthorfile(List<AuthorFileBo> authorfile) {
		this.authorfile = authorfile;
	}
	
	
	
	
}
