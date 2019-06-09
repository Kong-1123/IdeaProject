
package cn.com.dhcc.creditquery.person.queryweb.controller.reasoncof;

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

import cn.com.dhcc.credit.platform.util.Constants;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.querycontrol.bo.CpqQueryReasonBo;
import cn.com.dhcc.creditquery.person.querycontrol.entity.CpqQueryReason;
import cn.com.dhcc.creditquery.person.querycontrol.service.queryreason.CpqQueryReasonService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

/**
 * <查询原因参数管理-控制层>
 * 
 * @author wenjie·chu
 *
 *         2018年3月26日-上午11:13:12
 */

@Controller
@RequestMapping(value = "/queryReason")
public class QueryReasonController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(QueryReasonController.class);
	private static final String PREFIX = "queryreason/";

	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqQueryReasonService queryReasonService;

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

	@RequestMapping("/findCpqQueryRecordBoById")
	@ResponseBody
	public String fidById(String id) {
		CpqQueryReasonBo reasonconf = queryReasonService.findById(id);
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
			CpqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req, "username"));
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
	 * list页面授权的启动
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("/startDelegationPolicy")
	@ResponseBody
	@LogOperation("查询原因授权启用")
	public String startDelegationPolicy(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CpqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req, "username"));
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
	 * list页面审批的停止
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("/stopRecheckPolicy")
	@ResponseBody
	@LogOperation("查询原因审批启停用")
	public String stopRecheckPolicy(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CpqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req, "username"));
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
	 * list页面审批的启动
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("/startRecheckPolicy")
	@ResponseBody
	@LogOperation("查询原因授权启用")
	public String startRecheckPolicy(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CpqQueryReasonBo queryreason = queryReasonService.findById(id);
			queryreason.setUpdateTime(new Date());
			queryreason.setOperator(getUserInfo(req, "username"));
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
		Page<CpqQueryReasonBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			// 获取顶级机构代码
			String topOrgCode = UserUtilsForConfig.getRootDeptCode(request);
			searchParams.put("EQ_orgId", topOrgCode);

			queryResults = queryReasonService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION,
					ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("queryreason list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqQueryReasonBo>());
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
