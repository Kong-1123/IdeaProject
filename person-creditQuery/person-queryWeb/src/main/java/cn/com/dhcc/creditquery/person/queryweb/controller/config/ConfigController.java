package cn.com.dhcc.creditquery.person.queryweb.controller.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqConfigBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqConfigService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;

/**
 *
 * @author lekang.liu
 * @date 2018年3月10日
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(ConfigController.class);
	private static final String PREFIX = "config/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "configId";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqConfigService cpqConfigService;

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
	 * <搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";
	}

	/**
	 * <跳转修改页面>
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
		Page<CpqConfigBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			// 获取顶级机构代码
			String topOrgCode = UserConfigUtils.getRootDeptCode(request);
			searchParams.put("EQ_orgId", topOrgCode);
			queryResults = cpqConfigService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("config list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqConfigBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	/**
	 * 
	 * @param configid
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String configid) {
		CpqConfigBo cpqConfigBo = cpqConfigService.findById(configid);
		return JsonUtil.toJSONString(cpqConfigBo, DATE_FORMAT);
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		// 根据请求获取redis中存入的用户信息对象
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}

	/**
	 * 修改方法
	 * 
	 * @param model
	 * @param request
	 * @param cpqConfigBo
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResultBeans update(Model model, HttpServletRequest request, CpqConfigBo cpqConfigBo) {
		ResultBeans rs = null;
		try {
			cpqConfigBo.setUpdateUser(getUserInfo(request, "username"));
			cpqConfigBo.setUpdateTime(new Date());
			cpqConfigService.update(cpqConfigBo);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rs;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/findByName")
	@ResponseBody
	public String findByName(String name) {
		CpqConfigBo cpqConfigBo = cpqConfigService.findByConfigName(name);
		return JsonUtil.toJSONString(cpqConfigBo, DATE_FORMAT);
	}

}
