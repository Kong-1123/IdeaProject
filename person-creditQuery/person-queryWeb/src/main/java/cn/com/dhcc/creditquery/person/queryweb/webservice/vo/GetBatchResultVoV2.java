package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GetBatchResultVo")
public class GetBatchResultVoV2 implements Serializable {
	private static final long serialVersionUID = -4260248474688006519L;
	
	private String code;
	private String message;
	private String filePath;
	private String fileName;
	
	public GetBatchResultVoV2(String code, String message, String filePath, String fileName) {
		this.code = code;
		this.message = message;
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	public GetBatchResultVoV2() {}

	public String getCode() {
		return code;
	}
	public GetBatchResultVoV2 setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public GetBatchResultVoV2 setMessage(String message) {
		this.message = message;
		return this;
	}
	public String getFilePath() {
		return filePath;
	}
	public GetBatchResultVoV2 setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	public String getFileName() {
		return fileName;
	}
	public GetBatchResultVoV2 setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

}
