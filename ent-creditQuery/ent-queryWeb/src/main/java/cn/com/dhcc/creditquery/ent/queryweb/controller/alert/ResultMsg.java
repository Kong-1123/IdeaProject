package cn.com.dhcc.creditquery.ent.queryweb.controller.alert;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSON;

public class ResultMsg {
	private boolean code;
	private String msg;

	public ResultMsg() {
		super();
	}

	public ResultMsg(boolean code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 将实体转化转成JSON字符串
	 * 
	 * @return
	 */
	public String toJSONStr() {
		String result = JSON.toJSONString(this);
		return result;
	}

	public boolean isCode() {
		return code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
