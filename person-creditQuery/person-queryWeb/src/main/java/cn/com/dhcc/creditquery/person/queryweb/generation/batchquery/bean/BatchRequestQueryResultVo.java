/**
 *  Copyright (c)  @date 2018年9月13日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*

package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean;

import java.io.Serializable;

*/
/**
 * 一代批量请求查询结果
 * 
 * @author lekang.liu
 * @date 2018年9月13日
 *//*

public class BatchRequestQueryResultVo implements Serializable {

	private static final long serialVersionUID = 1605565813210419870L;

	// 查询请求Id
	private String batchQueryDetailId;

	// 状态
	private String status;

	public String getBatchQueryDetailId() {
		return batchQueryDetailId;
	}

	public void setBatchQueryDetailId(String batchQueryDetailId) {
		this.batchQueryDetailId = batchQueryDetailId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static class Builder {
		BatchRequestQueryResultVo batchRequestQueryResultVo;
		
		public Builder(){
			batchRequestQueryResultVo = new BatchRequestQueryResultVo();
		}
		
		public BatchRequestQueryResultVo build(){
			return batchRequestQueryResultVo;
		}
		
		public Builder status(String status){
			batchRequestQueryResultVo.setStatus(status);
			return this;
		}
		
		public Builder batchQueryDetailId(String batchQueryDetailId){
			batchRequestQueryResultVo.setBatchQueryDetailId(batchQueryDetailId);
			return this;
		}
	}

}
*/
