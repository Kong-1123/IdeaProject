/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class ResultBeans {
	private String msg;
	private String code;
	private Object objectData;
	private static ResultBeans sucess = new ResultBeans();

	public ResultBeans() {

	}

	public static ResultBeans sucessResultBean() {
		return sucessResultBean(null);
	}

	public static ResultBeans sucessResultBean(String sucessMsg) {
		sucess.setCode(Constants.SUCCESSCODE);
		if (StringUtils.isEmpty(sucessMsg)) {
			sucess.setMsg(Constants.SUCCESSMSG);
		} else {
			sucess.setMsg(sucessMsg);
		}
		return sucess;
	}

	public static ResultBeans sucessResultBean(String sucessMsg, Map<String, Object> map) {
		sucess.setCode(Constants.SUCCESSCODE);
		if (StringUtils.isEmpty(sucessMsg)) {
			sucess.setMsg(Constants.SUCCESSMSG);
		} else {
			sucess.setMsg(sucessMsg);
		}
		sucess.setObjectData(map);
		return sucess;
	}

	public ResultBeans(String msg, String code, Object objectData) {
		super();
		this.msg = msg;
		this.code = code;
		this.objectData = objectData;
	}

	public ResultBeans(String code, String msg) {
		super();
		this.msg = msg;
		this.code = code;
	}

	public static ResultBeans resultBeanData(String msg, String code, Object objectData) {
		ResultBeans result = new ResultBeans(msg, code, objectData);
		return result;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getObjectData() {
		return objectData;
	}

	public void setObjectData(Object objectData) {
		this.objectData = objectData;
	}

	public static ResultBeans getSucess() {
		return sucess;
	}

	public static void setSucess(ResultBeans sucess) {
		ResultBeans.sucess = sucess;
	}

}
