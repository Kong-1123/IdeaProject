/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewWatermarkImageService;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.JurisdictionCache;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rzd
 * @date 2019年2月26日
 */
@Service
@Slf4j
public class CpqReportViewWatermarkImageServiceImpl implements CpqReportViewWatermarkImageService {

	/* (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewWatermarkImageService#getImage(java.lang.String, java.lang.String)
	 */
	@Override
	public String getImage(String username, String deptCode) {
		log.info(" -getImage 读取水印服务，请求参数：username:{}，deptCode:{}",username,deptCode);
		String infoString = null;
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(deptCode)) {
			log.info(" -getImage 读取水印服务，请求参数为空：username:{}，deptCode:{}",username,deptCode);
			return infoString;
		}
		try {
			JSONArray js = new JSONArray();
			// 生成图片
			Object jsonObject = JurisdictionCache.getDeptCodeName();
			String jsonStr = null;
			if(jsonObject != null) {
				jsonStr = String.valueOf(jsonObject);
			}else {
				log.info(" -getImage 读取水印服务读取DeptCodeName信息读取失败，请求参数：username:{}，deptCode:{}",username,deptCode);
				return infoString;
			}
			JSONObject deptCodeName = JSON.parseObject(jsonStr);
			String deptName = deptCodeName.get(deptCode).toString();
			String bankName = ConfigUtil.getBankName();
			js.add(bankName);
			js.add("查询员："+username);
			js.add(deptName);
			js.toJSONString();
			infoString = js.toJSONString();
		} catch (Exception e) {
			log.info(" -getImage username:{}，deptCode:{} 读取水印服务异常。",username,deptCode);
			log.error("错误信息：  ", e);
		}
		log.info(" -getImage username:{}，deptCode:{} 读取水印服务完成。",username,deptCode);
		return infoString;
	}
	
}
