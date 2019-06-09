package cn.com.dhcc.creditquery.ent.queryweb.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cn.com.dhcc.creditquery.ent.query.bo.BaseBo;

public class LegalReview extends BaseBo {

	/**
	 * 校验返回结果代码 10000 10011 10012 10020 10099 confirm提示信息。
	 */
	private String resultCode;
	/**
	 * 校验返回提示信息
	 */
	private String resultMsg;
	/**
	 * 跳转url地址
	 */
	private String url;
	
	private String checkWay;
	private String checkId;


	public LegalReview() {
		super();
	}

	public LegalReview(String resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}
	
	
	public LegalReview(String resultCode, String resultMsg, String url) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.url = url;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCheckWay() {
		return checkWay;
	}

	public void setCheckWay(String checkWay) {
		this.checkWay = checkWay;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
