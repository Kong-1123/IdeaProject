package cn.com.dhcc.creditquery.ent.queryweb.controller.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqConfigBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqConfigService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author chenting
 * @date 2018年4月16日 new
 */
@Slf4j
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {
	private static final String PREFIX = "config/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "configId";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CeqConfigService ceqConfigService;
	
	/***
	 * <分页列表界面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}

	/***
	 * <跳转修改页面>
	 * 
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}

	/***
	 * <跳转详情界面>
	 * 
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage() {
		return PREFIX + "detail";
	}

	/***
	 * <跳转查询界面>
	 * 
	 * @return
	 */
	@RequestMapping("/searchPage")
	public String searchPage() {
		return PREFIX + "search";
	}

	/***
	 * 分页显示数据
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String getPage(Model model, HttpServletRequest request, PageBean page) {
		Page<CeqConfigBo> results = null;
		Map<String, Object> params = processRequestParams(page, request);
		//获取顶级机构代码
		String topOrgCode = UserUtilsForConfig.getRootDeptCode(request);
		params.put("EQ_orgId", topOrgCode);
		try {
			results = ceqConfigService.getPage(params, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception ex) {
			results = new PageImpl<>(new ArrayList<CeqConfigBo>());
			log.error("config getPage:" + ex);
		}
		page = processQueryResults(model, page, results);
		String JsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return JsonString;
	}

	/***
	 * 修改参数
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResultBeans update(CeqConfigBo ceqConfigBo, HttpServletRequest request) {
		ResultBeans result = null;
		try {
			String loginUserName = getLoginUser(request,"username");	
			ceqConfigBo.setUpdateTime(new Date());
			ceqConfigBo.setUpdateUser(loginUserName);
			ceqConfigService.update(ceqConfigBo);
			log.debug("update:" + ceqConfigBo);
			result = ResultBeans.sucessResultBean();
		} catch (Exception ex) {
			result = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("update config:" + ex);
		}
		return result;
	}
	
	/**
	 * 根据id查询
	 * 用于页面展示
	 * @param id
	 * @return ceqconfig.json
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CeqConfigBo ceqConfigBo = null;
		try {
			ceqConfigBo = ceqConfigService.findById(id);
		} catch (Exception ex) {
			log.error("config findbyid:" , ex);
		}
		String JsonString = JsonUtil.toJSONString(ceqConfigBo, DATE_FORMAT);
		return JsonString;

	}
	
	//暂无用
	@RequestMapping("/findbyname")
	@ResponseBody
	public String findByName(String name) {
		CeqConfigBo ceqConfigBo = null;
		try {
			ceqConfigBo = ceqConfigService.findByConfigName(name);
		} catch (Exception ex) {
			log.error("config findbyname:" , ex);
		}
		String JsonString = JsonUtil.toJSONString(ceqConfigBo, DATE_FORMAT);
		return JsonString;
	}
	
	/**
	 * 从缓存中获取数据
	 * @param request
	 * @param infoName
	 * @return
	 */
	private String getLoginUser(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserUtilsForConfig.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
	
}
