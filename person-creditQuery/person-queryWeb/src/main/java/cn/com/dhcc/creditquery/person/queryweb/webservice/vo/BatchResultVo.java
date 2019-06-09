package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author lekang.liu
 * @date 2018年7月26日
 */
@XStreamAlias("BatchResultVo")
public class BatchResultVo implements Serializable {

	/**
	*/
	private static final long serialVersionUID = -5924360473496873677L;

	// 结果代码
	@XStreamAlias("Code")
	private String code;

	// 返回信息
	@XStreamAlias("Message")
	private String message;

	// 查询记录编号
	@XStreamAlias("BatchNo")
	private String batchNo;
	
	public BatchResultVo() {
		super();
	}

	public BatchResultVo(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public BatchResultVo(String code, String message, String batchNo) {
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
