package cn.com.dhcc.creditquery.ent.queryweb.controller.operatelog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import cn.com.dhcc.creditquery.ent.businessmonitor.service.CeqOperateLogService;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqOperateLogBo;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

/**
 * 
 * @author chenting
 * @date 2018年4月18日 new
 */

@Controller
@RequestMapping("/operatelog")
public class OperateLogController extends BaseController {
	@Autowired
	private CeqOperateLogService service;

	private Logger log = LoggerFactory.getLogger(OperateLogController.class);

	private final static String PREFIX = "operatelog/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "createTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/***
	 * <分页展示页面>
	 * 
	 * @return
	 */
	@RequestMapping("list")
	private String list() {
		return PREFIX + "list";
	}

	/***
	 * <跳转搜索页面>
	 * 
	 * @return
	 */
	@RequestMapping("searchPage")
	private String searchPage() {
		return PREFIX + "search";
	}

	/***
	 * 分页展示
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return page.json
	 */
	@RequestMapping("getPage")
	@ResponseBody
	private String getPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqOperateLogBo> results = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			 if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
					List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
					String deptCode = StringUtils.join(deptCodes,",");
					searchParams.put("IN_operOrg", deptCode);
				}
			results = service.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
		} catch (Exception ex) {
			results = new PageImpl<>(new ArrayList<CeqOperateLogBo>());
			log.error("operatelog getPage error", ex);
		}
		page = processQueryResults(model, page, results);
		return JsonUtil.toJSONString(page, DATE_FORMAT);
	}

}
