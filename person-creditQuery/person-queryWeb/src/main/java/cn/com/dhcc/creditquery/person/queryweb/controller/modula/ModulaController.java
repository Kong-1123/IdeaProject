/*
package cn.com.dhcc.creditquery.person.queryweb.controller.modula;

import java.util.ArrayList;
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
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditpersonquerydao.entity.modula.CpqModula;
import cn.com.dhcc.query.creditpersonqueryservice.modula.service.CpqModulaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/modula")
public class ModulaController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(ModulaController.class);
	private static final String PREFIX = "modula/";

	private final static String ORDERBY = "sortNum";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	CpqModulaService cpqModulaService;

	*/
/**
	 * <搜索框>
	 * 
	 * @return
	 *//*

	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";
	}

	*/
/**
	 * <跳转 修改 页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}

	*/
/**
	 * <分页列表页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}

	*/
/**
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 *//*

	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqModula> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			queryResults = cpqModulaService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), "", ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("modula list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqModula>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

}
*/
