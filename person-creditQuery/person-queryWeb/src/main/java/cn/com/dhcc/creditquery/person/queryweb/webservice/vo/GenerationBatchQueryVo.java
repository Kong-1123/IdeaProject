/**
 *  Copyright (c)  @date 2018年9月12日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 一代批量查询
 * 
 * @author lekang.liu
 * @date 2018年9月12日
 */
@XStreamAlias("GenerationBatchQueryVo")
public class GenerationBatchQueryVo {

	@XStreamAlias("BatchQueryInfo")
	private GenerationBatchQueryInfo generationBatchQueryInfo;
	@XStreamAlias("Token")
	private String token;

	public GenerationBatchQueryVo() {
		super();
	}

	public GenerationBatchQueryVo(GenerationBatchQueryInfo generationBatchQueryInfo, String token) {
		super();
		this.generationBatchQueryInfo = generationBatchQueryInfo;
		this.token = token;
	}

	public GenerationBatchQueryInfo getGenerationBatchQueryInfo() {
		return generationBatchQueryInfo;
	}

	public void setGenerationBatchQueryInfo(GenerationBatchQueryInfo generationBatchQueryInfo) {
		this.generationBatchQueryInfo = generationBatchQueryInfo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
