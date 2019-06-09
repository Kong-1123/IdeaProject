/**
 *  Copyright (c)  @date 2018年5月8日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*
package cn.com.dhcc.query.creditenterprisequeryweb.controller.ipAndMac;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.query.creditenterprisequerydao.entity.ipaddr.CeqIpaddr;
import cn.com.dhcc.query.creditenterprisequerydao.entity.macaddr.CeqMacaddr;
import cn.com.dhcc.query.creditenterprisequerydao.entity.userattr.CeqUserAttr;
import cn.com.dhcc.query.creditenterprisequeryservice.ipaddr.service.CeqIpaddrService;
import cn.com.dhcc.query.creditenterprisequeryservice.macaddr.service.CeqMacaddrService;
import cn.com.dhcc.query.creditenterprisequeryservice.userattr.util.CeqUserAttrUtil;
import cn.com.dhcc.query.creditenterprisequeryweb.base.BaseController;
import cn.com.dhcc.query.creditenterprisequeryweb.util.Constants;
import cn.com.dhcc.query.creditenterprisequeryweb.util.ResultBeans;
import cn.com.dhcc.query.creditenterprisequeryweb.util.UserUtils;
import cn.com.dhcc.query.creditenterprisequeryweb.vo.IpAndMacVo;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import lombok.extern.slf4j.Slf4j;

*//**
 * 
 * @author lekang.liu
 * @date 2018年5月8日
 *//*
@Slf4j
@Controller
@RequestMapping(value = "/ipAndMac")
public class IpAndMacContrller extends BaseController {
	private static Logger log = LoggerFactory.getLogger(IpAndMacContrller.class);
	private static final String PREFIX = "ipandmac/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CeqIpaddrService ipService;

	@Autowired
	private CeqMacaddrService macService;

	@RequestMapping("/addIP")
	public String addIP() {
		return PREFIX + "addIP";
	}

	@RequestMapping("/addMac")
	public String addMac() {
		return PREFIX + "addMac";
	}

	@RequestMapping("/ipList")
	public String ipList() {
		return PREFIX + "ipList";
	}

	@RequestMapping("/macList")
	public String macList() {
		return PREFIX + "macList";
	}

	@RequestMapping("/ipDetail")
	public String ipDetail() {
		return PREFIX + "ipDetail";
	}

	@RequestMapping("/macDetail")
	public String macDetail() {
		return PREFIX + "macDetail";
	}

	@RequestMapping("/updateIp")
	public String updateIp() {
		return PREFIX + "updateIp";
	}

	@RequestMapping("/updateMac")
	public String updateMac() {
		return PREFIX + "updateMac";
	}

	@RequestMapping("/getIpPage")
	@ResponseBody
	public String getIpPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqIpaddr> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			queryResults = ipService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("getIpPage method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqIpaddr>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	@RequestMapping("/getMacPage")
	@ResponseBody
	public String getMacPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqMacaddr> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			queryResults = macService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("getIpPage method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqMacaddr>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	@RequestMapping("/findIpByUserName")
	@ResponseBody
	public String findIpByUserName(String userName) {
		Iterable<CeqIpaddr> ipList = ipService.findByUserName(userName);
		String jsonString = JsonUtil.toJSONString(ipList, DATE_FORMAT);
		return jsonString;
	}

	@RequestMapping("/findMacByUserName")
	@ResponseBody
	public String findMacByUserName(String userName) {
		Iterable<CeqMacaddr> macList = macService.findByUserName(userName);
		String jsonString = JsonUtil.toJSONString(macList, DATE_FORMAT);
		return jsonString;
	}

	@RequestMapping("/getAddIpNum")
	@ResponseBody
	public String getAddIpNum(String userName, HttpServletRequest req) {
		try {
			String loginBindMode = CeqConfigUtil.getLoginBindMode();
			if (Objects.equals("0", loginBindMode)) {
				return "{num:0}";
			}
			CeqUserAttr cpqUserAttr = CeqUserAttrUtil.findCeqUserAttrBySystemUserId(userName);
			// 设置的绑定数量
			String bindAddrNum = CeqConfigUtil.getLoginBindAddrNum();
			if (null != cpqUserAttr) {
				String bindNumber = cpqUserAttr.getBindNumber();
				bindAddrNum = bindNumber;
			}
			// 已绑定数量
			List<CeqIpaddr> findByUserName = (List<CeqIpaddr>) ipService.findByUserName(userName);
			int parseInt = Integer.parseInt(bindAddrNum);
			int size = findByUserName.size();
			int num = parseInt - size;
			return "{\"num\":" + num + " }";
		} catch (Exception e) {
			log.error("getAddIpNum ", e.getMessage());
			return "{\"num\":0}";
		}
	}

	@RequestMapping("/getAddMacNum")
	@ResponseBody
	public String getAddMacNum(String userName, HttpServletRequest req) {
		try {
			String loginBindMode = CeqConfigUtil.getLoginBindMode();
			if (Objects.equals("0", loginBindMode)) {
				return "{num:0}";
			}
			CeqUserAttr cpqUserAttr = CeqUserAttrUtil.findCeqUserAttrBySystemUserId(userName);
			// 设置的绑定数量
			String bindAddrNum = CeqConfigUtil.getLoginBindAddrNum();
			if (null != cpqUserAttr) {
				String bindNumber = cpqUserAttr.getBindNumber();
				bindAddrNum = bindNumber;
			}
			// 已绑定数量
			List<CeqMacaddr> findByUserName = (List<CeqMacaddr>) macService.findByUserName(userName);
			int parseInt = Integer.parseInt(bindAddrNum);
			int size = findByUserName.size();
			int num = parseInt - size;
			return "{\"num\":" + num + " }";
		} catch (Exception e) {
			log.error("getAddIpNum ", e.getMessage());
			return "{\"num\":0}";
		}
	}

	// IP解绑
	@RequestMapping("/untieIp")
	@ResponseBody
	public String untieIp(String id) {
		ResultBeans rs;
		try {
			ipService.deleteById(id);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("untieIp method ", e.getMessage());
		}
		return rs.toJSONStr();
	}

	// IP解绑
	@RequestMapping("/untieMac")
	@ResponseBody
	public String untieMac(String id) {
		ResultBeans rs;
		try {
			macService.deleteById(id);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("untieIp method ", e.getMessage());
		}
		return rs.toJSONStr();
	}
	
	@RequestMapping("/addIpByUsername")
	@ResponseBody
	public String addIpByUsername(IpAndMacVo vo, HttpServletRequest req){
		ResultBeans rs;
		try {
			List<String> ipList = vo.getIp();
			String userName = vo.getUserName();
			ArrayList<CeqIpaddr> cpqIpaddrList = new ArrayList<CeqIpaddr>();
			Date date = new Date();
			String createUser = getUserInfo(req,"username");
			for (String ip : ipList) {
				CeqIpaddr cpqIpaddr = new CeqIpaddr(date,createUser,ip,date,userName);
				cpqIpaddrList.add(cpqIpaddr);
			}
			ipService.createByList(cpqIpaddrList);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("addIpByUsername method ", e.getMessage());
		}
		return rs.toJSONStr();
	}
	
	@RequestMapping("/addMacByUsername")
	@ResponseBody
	public String addMacByUsername(IpAndMacVo vo, HttpServletRequest req){
		ResultBeans rs;
		try {
			List<String> macList = vo.getMac();
			String userName = vo.getUserName();
			ArrayList<CeqMacaddr> cpqIpaddrList = new ArrayList<CeqMacaddr>();
			Date date = new Date();
			String createUser = getUserInfo(req,"username");
			for (String mac : macList) {
				CeqMacaddr cpqIpaddr = new CeqMacaddr(date,createUser,mac,date,userName);
				cpqIpaddrList.add(cpqIpaddr);
			}
			macService.createByList(cpqIpaddrList);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("addMacByUsername method ", e.getMessage());
		}
		return rs.toJSONStr();
	}
	
	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}

}
*/