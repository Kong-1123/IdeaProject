package cn.com.dhcc.creditquery.ent.queryweb.controller.reasoncof;

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
import cn.com.dhcc.creditquery.ent.querycontrol.bo.CeqQueryReasonBo;
import cn.com.dhcc.creditquery.ent.querycontrol.service.queryreason.CeqQueryReasonService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author wenjie·chu
 *
 * 2018年4月23日-上午10:40:24
 */
@Slf4j
@Controller
@RequestMapping(value = "/queryReason")
public class QueryReasonController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(QueryReasonController.class);
	private static final String PREFIX = "queryreason/";
	
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private CeqQueryReasonService queryReasonService;
	
	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";

	}
	
	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String fidById(String id) {
		CeqQueryReasonBo reasonconf = queryReasonService.findById(id);
		return JsonUtil.toJSONString(reasonconf, DATE_FORMAT);
	}
	
	/**
	 * list页面授权的停止
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/stopDelegationPolicy")
	@ResponseBody
	@LogOperation("查询原因授权停用")
	public String stopDelegationPolicy(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CeqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req,"username"));
			queryreason.setDelegationPolicy("0");
			queryReasonService.update(queryreason);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rss.toJSONStr();
	}
	
	
	
	
	/**
	 * list页面授权的启动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/startDelegationPolicy")
	@ResponseBody
	@LogOperation("查询原因授权启用")
	public String startDelegationPolicy(HttpServletRequest req,String id) {
		ResultBeans rss = null;
		try {
			CeqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req,"username"));
			queryreason.setDelegationPolicy("1");
			queryReasonService.update(queryreason);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rss.toJSONStr();

	}
	
	
	
	
	/**
	 * list页面审批的停止
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/stopRecheckPolicy")
	@ResponseBody
	@LogOperation("查询原因审批停用")
	public String stopRecheckPolicy(HttpServletRequest req,String id) {
		ResultBeans rss = null;
		try {
			CeqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req,"username"));
			queryreason.setRecheckPolicy("0");
			queryReasonService.update(queryreason);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rss.toJSONStr();

	}
	
	
	
	/**
	 * list页面审批的启动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/startRecheckPolicy")
	@ResponseBody
	@LogOperation("查询原因审批启用")
	public String startRecheckPolicy(HttpServletRequest req,String id) {
		ResultBeans rss = null;
		try {
			CeqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req,"username"));
			queryreason.setRecheckPolicy("1");
			queryReasonService.update(queryreason);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rss.toJSONStr();

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
		Page<CeqQueryReasonBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			//获取顶级机构代码
			String topOrgCode = UserUtilsForConfig.getRootDeptCode(request);
			searchParams.put("EQ_orgId", topOrgCode);
			
			queryResults = queryReasonService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("queryreason list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqQueryReasonBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}
	
	
	
	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserUtilsForConfig.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
}
