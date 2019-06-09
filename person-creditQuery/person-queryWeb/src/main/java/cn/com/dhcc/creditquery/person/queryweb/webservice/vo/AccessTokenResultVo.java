package cn.com.dhcc.creditquery.person.queryweb.webservice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("accessTokenResult")
public class AccessTokenResultVo implements Serializable {

	private static final long serialVersionUID = 1027600805909607255L;
	
	private String code;
	@XStreamAlias("message")
	private String msg;
	private String token;
	
	public AccessTokenResultVo(String code, String msg, String token) {
		this.code = code;
		this.msg = msg;
		this.token = token;
	}
	
	public AccessTokenResultVo() {

	}

	public String getCode() {
		return code;
	}
	public AccessTokenResultVo setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public AccessTokenResultVo setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public String getToken() {
		return token;
	}
	public AccessTokenResultVo setToken(String token) {
		this.token = token;
		return this;
	}

}
