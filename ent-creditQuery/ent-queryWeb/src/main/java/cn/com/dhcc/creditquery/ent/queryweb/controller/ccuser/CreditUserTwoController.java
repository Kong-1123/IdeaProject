/**
 *  Copyright (c)  @date 2018年8月16日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.ent.queryweb.controller.ccuser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqPbocUserBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqPbocUserService;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqUserAttrService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.commonquerypboc.serviceWeb.remote.bean.ResponseInfo;
import cn.com.dhcc.creditquery.person.commonquerypboc.serviceWeb.remote.bean.ResponseInfoCodeEnum;
import cn.com.dhcc.creditquery.person.commonquerypboc.serviceWeb.remote.http.login.service.CreditUserLoginService;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.creditquerycommon.util.db.util.CipherUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lekang.liu
 * @date 2018年8月16日
 */
@Slf4j
@Controller
@RequestMapping(value = "/creditUser")
public class CreditUserTwoController extends BaseController {
	private static final String PREFIX = "creditUser/";
	// 排序方向
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	
	
	@Autowired
	private CeqPbocUserService ceqPbocUserService;
	@Autowired
	private CreditUserLoginService loginService;
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	@Autowired
	private CeqUserAttrService ceqUserAttrService;

	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}

	/**
	 * <跳转 新增页面>
	 * 
	 * @return
	 */
	@RequestMapping("/createPage")
	public String createPage() {
		return PREFIX + "create";
	}

	/**
	 * <跳转 修改 页面>
	 * 
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}

	/**
	 * <跳转到详情页面>
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}

	/**
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqPbocUserBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);

			if (StringUtils.isBlank((String) searchParams.get("IN_deptCode"))) {
				String deptCode = getWithinDeptCode(UserUtilsForConfig.getDeptCode(request));
				searchParams.put("IN_deptCode", deptCode);
			}
			queryResults = ceqPbocUserService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("list method e={}", e);
			queryResults = new PageImpl<>(new ArrayList<CeqPbocUserBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CeqPbocUserBo ccUser = ceqPbocUserService.findById(id);
		String password = ccUser.getCreditPassword();
		// 密码解密
		password = CipherUtil.decryptBasedDes(password);
		ccUser.setCreditPassword(password);
		return JsonUtil.toJSONString(ccUser, DATE_FORMAT);
	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(Model model, HttpServletRequest request, String id) {
		ResultBeans rs = null;
		try {
			String[] pbocUserIds = id.split(",");
			for (int i = 0; i < pbocUserIds.length; i++) {//循环取出每个征信用户id
				String creditUserId = pbocUserIds[i];
				CeqPbocUserBo cpqPbocUserBo = ceqPbocUserService.findById(creditUserId);
				//判断是否存在关联映射、判断是否存在查询记录
				List<String> psersonUserName = null;
				psersonUserName = ceqUserAttrService.findUserNameByCreditUser(cpqPbocUserBo.getCreditUser());
				if ((psersonUserName != null && psersonUserName.size() != 0)){
					rs = new ResultBeans(Constants.ERRORCODE, "该征信用户"+cpqPbocUserBo.getCreditName()+"存在用户关联，无法删除");
					return rs.toJSONStr();
				}
				boolean isExistsRecord = ceqFlowManageService.isQueriedCreditUser(cpqPbocUserBo.getCreditUser());
				if(!isExistsRecord) {//存在查询记录
					rs = new ResultBeans(Constants.ERRORCODE, "该征信用户"+cpqPbocUserBo.getCreditName()+"存在查询记录，无法删除");
				}
			}
			if(null == rs) {
				List<String> pbocUserIdList = Arrays.asList(pbocUserIds);
				ceqPbocUserService.deleteByIds(pbocUserIdList);
				rs = ResultBeans.sucessResultBean();
			}
		} catch (Exception e) {
			log.error("delete error e={}", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);

		}
		return rs.toJSONStr();

	}

	/**
	 * 添加方法
	 * 
	 * @param model
	 * @param request
	 * @param id
	 * @param config
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(Model model, HttpServletRequest request, CeqPbocUserBo ccUser) {
		ResultBeans rs = null;
		try {
			Map<String, String> userInfo = UserUtilsForConfig.getUserInfo(request);
			String createUser = userInfo.get("username").trim();
			String deptcode = UserUtilsForConfig.getDeptCode(request);
			Date createDate = new Date();
			ccUser.setCreateUser(createUser);
			ccUser.setCreateDate(createDate);
			ccUser.setUpdateUser(createUser);
			ccUser.setUpdateDate(createDate);
			ccUser.setDeptCode(deptcode);
			ccUser.setCreditPassword(CipherUtil.encryptBasedDes(ccUser.getCreditPassword()));
			
			CeqPbocUserBo ceqPbocUserBo = ceqPbocUserService.findByCreditName(ccUser.getCreditName());
			if(ceqPbocUserBo != null) {//存在查询记录
				rs = new ResultBeans(Constants.ERRORCODE, "该征信用户名称"+ceqPbocUserBo.getCreditName()+"已经存在存在，无法新增");
			}
			if(rs == null) {
				ceqPbocUserService.create(ccUser);
				rs = ResultBeans.sucessResultBean();
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("create error", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 测试二代征信用户密码是否可用
	 * 
	 * @param CeqPbocUserBo
	 *            {@link CpqPbocUserBo}
	 * @return String
	 */
	@RequestMapping("/testCreditPassWord")
	@ResponseBody
	public String testCreditPassWord(CeqPbocUserBo CeqPbocUserBo,HttpServletRequest request) {
		log.info("testCreditPassWord CpqPbocUserBo ={}", CeqPbocUserBo);
		ResultBeans rs;
		try {
			String creditUser = CeqPbocUserBo.getCreditUser();
			String password = CeqPbocUserBo.getCreditPassword();
			String creditOrgcode = CeqPbocUserBo.getCreditOrgCode();
			String machineNetwork = CeqPbocUserBo.getMachineNetwork();
			String machineDisk = CeqPbocUserBo.getMachineDisk();
			String machineCPU = CeqPbocUserBo.getMachineCPU();
			if (StringUtils.isBlank(creditUser) || StringUtils.isBlank(password)
					|| StringUtils.isBlank(creditOrgcode)) {
				return new ResultBeans(Constants.ERRORCODE, "征信用户账号、密码或机构为空，请检查您输入的信息~！").toJSONStr();
			}
			String topOrgCode = UserUtilsForConfig.getRootDeptCode(request);
			ResponseInfo login = loginService.loginTest(creditUser,machineCPU,machineDisk,machineNetwork,password,topOrgCode);
			ResponseInfoCodeEnum code = login.getCode();
			if (Objects.equal(ResponseInfoCodeEnum.LOGIN_SUCCESS, code)
					|| Objects.equal(ResponseInfoCodeEnum.LOGIN_PWD_MODIFY, code)) {
				rs = ResultBeans.sucessResultBean();
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "征信用户名或密码不可用，请检查您输入的密码~！");
			}
		} catch (Exception e) {
			log.error("create error", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}

	/**
	 * 添加方法
	 * 
	 * @param model
	 * @param request
	 * @param id
	 * @param config
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(Model model, HttpServletRequest request, CeqPbocUserBo ccUser) {
		ResultBeans rs = null;
		try {
			Map<String, String> userInfo = UserUtilsForConfig.getUserInfo(request);
			String createUser = userInfo.get("username").trim();
			Date updateDate = new Date();
			ccUser.setUpdateUser(createUser);
			ccUser.setUpdateDate(updateDate);
			ccUser.setCreditPassword(CipherUtil.encryptBasedDes(ccUser.getCreditPassword()));
			ceqPbocUserService.update(ccUser);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("create error", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 根据机构码获取其辖内征信用户
	 * 
	 * @param deptCode
	 * @return Object
	 */
	@RequestMapping("/getccIdList")
	@ResponseBody
	public Object getccIdList(HttpServletRequest request) {
		log.info("getccIdList start.");
		String deptCode = UserUtilsForConfig.getDeptCode(request);
		List<CeqPbocUserBo> list = null;
		JSONArray json = new JSONArray();
		JSONObject jo = new JSONObject();
		try {
			String alldeptCode = getWithinDeptCode(deptCode);
			String[] orgId = alldeptCode.split(",");
			ArrayList<String> arrayList = new ArrayList<>();
			for (String org : orgId) {
				arrayList.add(org);
			}
			log.info("getccIdList findByDeptCode arrayList = {}", arrayList);
			list = ceqPbocUserService.findByOrgCodes(arrayList);

			for (CeqPbocUserBo ceqCcUser : list) {
				String ccid = ceqCcUser.getCreditName();
				json.add(ccid);
			}
			jo.put("ccid", json);
		} catch (Exception e) {
			log.error("getccIdList method ,", e);
			jo.put("ccid", json);
		}
		return jo;

	}

	public static Object getBean(String beanName) {
		Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
		return bean;
	}
}
