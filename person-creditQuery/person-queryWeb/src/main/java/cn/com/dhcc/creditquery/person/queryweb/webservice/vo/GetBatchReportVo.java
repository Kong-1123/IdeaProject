package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author lekang.liu
 * @date 2018年7月26日
 */
@XStreamAlias("GetBatchResultVo")
public class GetBatchReportVo implements Serializable {

	/**
	*/
	private static final long serialVersionUID = -4495901827962025920L;

	@XStreamAlias("BatchNo")
	@NotNull(message = "BatchNo字段不可为空；")
	@NotBlank(message = "BatchNo字段不可为空；")
	private String batchNo;

	// 查询员
	@XStreamAlias("QueryUser")
	@NotNull(message = "QueryUser字段不可为空；")
	@NotBlank(message = "QueryUser字段不可为空；")
	private String queryUser;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getQueryUser() {
		return queryUser;
	}

	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}

}
