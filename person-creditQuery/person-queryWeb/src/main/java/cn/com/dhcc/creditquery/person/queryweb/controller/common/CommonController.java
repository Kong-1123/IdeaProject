package cn.com.dhcc.creditquery.person.queryweb.controller.common;

import cn.com.dhcc.creditquery.person.querypboc.service.CpqDicCacheService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.JurisdictionCache;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping(value = "/common")
public class CommonController extends BaseController {

	@Autowired
	private CpqDicCacheService dicCacheService;

	@RequestMapping("/cpqDic")
	@ResponseBody
	public Map<String, String> getdic(String type) {
		Map<Object, Object> map = dicCacheService.getDic(type);
		Set<Object> keySet = map.keySet();
		Map<String, String> dicMap = new HashMap<String, String>();
		for (Iterator<Object> iterator = keySet.iterator(); iterator.hasNext();) {
			String keyStr = iterator.next().toString();
			String valStr = map.get(keyStr).toString();
			dicMap.put(keyStr, valStr);
		}
		return dicMap;
	}

	/**
	 * 显示信用报告时，动态实时生成水印图片
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getImage")
	@ResponseBody
	public String getImage(HttpServletRequest request, HttpServletResponse response) {
		String infoString = null;
		try {
			JSONArray js = new JSONArray();
			response.setContentType("text/html; charset=utf-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			// 生成图片
			String username = getUserInfo(request, "username");
			String deptCode = UserConfigUtils.getDeptCode(request);
			String json = (String) JurisdictionCache.getDeptCodeName();
			JSONObject deptCodeName = JSON.parseObject(json);
			String deptName = deptCodeName.get(deptCode).toString();
			String bankName = ConfigUtil.getBankName();
			js.add(bankName);
			js.add("查询员："+username);
			js.add(deptName);
			js.toJSONString();
			infoString = js.toJSONString();
		} catch (Exception e) {
			log.error("getImage  ", e);
		}
		return infoString;
	}

	
	
	/**
	 * <菜单权限页面>
	 * 
	 * @return
	 */
	@RequestMapping("/RoleReasonList")
	public String menuRoleList() {
		return "common/RoleReasonList";
	}
	
	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}

}
