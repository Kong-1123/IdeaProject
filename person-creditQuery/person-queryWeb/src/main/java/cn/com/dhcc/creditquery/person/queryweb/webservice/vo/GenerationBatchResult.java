/**
 *  Copyright (c)  @date 2018年9月12日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author lekang.liu
 * @date 2018年9月12日
 */
@XStreamAlias("GenerationBatchResult")
public class GenerationBatchResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392170586430739013L;

	// 结果代码
	@XStreamAlias("Code")
	private String code;

	// 返回信息
	@XStreamAlias("Message")
	private String message;

	// 批次号
	@XStreamAlias("BatchNo")
	private String batchNo;

	public GenerationBatchResult() {
		super();
	}

	public GenerationBatchResult(String code, String message, String batchNo) {
		super();
		this.code = code;
		this.message = message;
		this.batchNo = batchNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	
	
	
	
	
}
