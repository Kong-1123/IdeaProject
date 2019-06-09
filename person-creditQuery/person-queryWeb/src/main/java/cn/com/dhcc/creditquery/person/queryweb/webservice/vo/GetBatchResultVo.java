package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GetBatchResultVo")
public class GetBatchResultVo implements Serializable {

	/**
	*/
	private static final long serialVersionUID = 2652305164639650060L;

	// 结果代码
	@XStreamAlias("Code")
	private String code;

	// 返回信息
	@XStreamAlias("Message")
	private String message;
	
	@XStreamAlias("ResultFilePath")
	private String resultFilePath;

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

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
	
	

}
