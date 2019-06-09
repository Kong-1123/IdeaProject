package cn.com.dhcc.creditquery.ent.queryweb.controller.roleReason;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqRoleReasonRelBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqRoleReasonRelService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.JurisdictionCache;
import cn.com.dhcc.query.creditquerycommon.util.Tree;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/common")
public class RoleReasonController extends BaseController {
	
	private static final String PERSON_DICTYEP = "qryReason";

	private static final String ENTERPRISE_DICTYEP = "qryReason_ceq";

	@Autowired
	private CeqRoleReasonRelService roleReasonRelService;

	@RequestMapping("/orgTree")
	@ResponseBody
	public String orgTree(HttpServletRequest request) {
		String deptCode = UserUtilsForConfig.getDeptCode(request);
		String treeValue = JurisdictionCache.getTreeValue(deptCode);
		return treeValue;
	}

	@RequestMapping("/getDeptCodeName")
	@ResponseBody
	public Object getDeptCodeName() {
		Object json = JurisdictionCache.getDeptCodeName();
		return json;
	}

	/**
	 * ( 获取全部的查询原因
	 * 
	 * @param id
	 * @param request
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllReason")
	@ResponseBody
	public String getAllReason(String id, String type, HttpServletRequest request) {
		List<Tree> allReasonList = null;
		allReasonList = roleReasonRelService.getEntAllReasonRel(id);
		if (CollectionUtils.isNotEmpty(allReasonList)) {
			log.info("allMenuList:{}", JSON.toJSONString(allReasonList));
		}
		return JSON.toJSONString(allReasonList);
	}

	/**
	 * 获取当前角色拥有的原因集合
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getUserReason")
	@ResponseBody
	public String getUserReason(String id, String type, HttpServletRequest request) {
		List<Tree> allButton = null;
		allButton = roleReasonRelService.getEntUserReason(id);
		return JsonUtil.toJSONString(allButton);
	}

	/**
	 * 保存角色分配的查询原因
	 * 
	 * @param request
	 * @param infoName
	 * @return
	 */

	@RequestMapping("/saveUserReason")
	@ResponseBody
	public String saveUserReason(String Ids, String userId, String type, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			String dicType = Objects.equals("0", type) ? PERSON_DICTYEP : ENTERPRISE_DICTYEP;
			String[] IdArray = Ids.split(",");
			String RoleId = IdArray[0];
			roleReasonRelService.deleteByRoleId(RoleId, dicType);
			for (int i = 1; i < IdArray.length; i++) {
				CeqRoleReasonRelBo roleReason = new CeqRoleReasonRelBo();
				String ReasonId = IdArray[i];
				roleReason.setRoleId(RoleId);
				roleReason.setReasonId(ReasonId);
				roleReason.setReasonType(dicType);
				// Map<String, String> info = UserUtils.getUserInfo(request);
				// String userId = info.get("username").trim();
				Date now = new Date();
				roleReason.setCreateUser(userId);
				roleReason.setCreateTime(now);
				roleReasonRelService.save(roleReason);
			}

			rs = new ResultBeans(Constants.SUCCESSCODE, Constants.SUCCESSMSG);
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("error", e);
		}

		return rs.toJSONStr();
	}

	/**
	 * 根据角色ID与系统类型获取对应的查询原因列表
	 * @param roleId  角色ID
	 * @param type  系统类型：0:个人 1：企业
	 * @param request
	 * @return String
	 */
	@RequestMapping("/getReasonRelByRoleIdAndType")
	@ResponseBody
	public String listReasonRelByRoleIdAndType(String roleId, String type, HttpServletRequest request) {
		List<String> allReasonList = null;
		try{
			if (Objects.equals("0", type)) {
				allReasonList = roleReasonRelService.listEntRelByRoleId(roleId);
			} else {
//				allReasonList = roleReasonRelService.listEnterpriseReasonRelByRoleId(roleId);
			}
			if (CollectionUtils.isNotEmpty(allReasonList)) {
				log.info("allMenuList:{}", JSON.toJSONString(allReasonList));
			}
		}catch(Exception e){
			e.printStackTrace();
			allReasonList = new ArrayList<String>();
		}
		return JSON.toJSONString(allReasonList);
	}
}
