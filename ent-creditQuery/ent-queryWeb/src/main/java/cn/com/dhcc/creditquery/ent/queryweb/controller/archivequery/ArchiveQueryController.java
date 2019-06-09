package cn.com.dhcc.creditquery.ent.queryweb.controller.archivequery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeManagerService;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
@RequestMapping(value = "/archivequery")
public class ArchiveQueryController extends BaseController{

	private static final String PREFIX = "archivequery/";

	
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "operTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String DATE_FORMAT_RANG = "yyyy-MM-dd";
	
	@Autowired
	private CeqAuthorizeManagerService ceqAuthorizeManagerService;


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
		log.info("getPage method begin ...");
		Page<CeqAuthorizeManagerBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
				List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
				String deptCode = StringUtils.join(deptCodes,",");
				searchParams.put("IN_operOrg", deptCode);
			}
			searchParams.put("NE_queryNum", 0L);
			log.info("searchParams ={}", searchParams);
			queryResults = ceqAuthorizeManagerService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("archive list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqAuthorizeManagerBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT_RANG);
		log.info("getPage method end ...,return ={}", jsonString);
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
		CeqAuthorizeManagerBo archive = ceqAuthorizeManagerService.findById(id);
		QueryEncryptUtil.QueryEncrypt(archive);
		return JsonUtil.toJSONString(archive, DATE_FORMAT);
	}


}
