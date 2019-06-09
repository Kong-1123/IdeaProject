/*
package cn.com.dhcc.creditquery.person.queryweb.controller.ccusercorrelation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.Constants;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.vo.CcUserCorrelationVo;
import cn.com.dhcc.creditquery.person.queryweb.vo.CcUserCorrelationVoResultBean;
import cn.com.dhcc.query.creditpersonquerydao.entity.usermap.CpqUsermap;
import cn.com.dhcc.query.creditpersonqueryservice.ccusercorrelation.service.CpqCcUserCorrelationService;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/ccUserCorrelation")
public class CcUserCorrelationController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(CcUserCorrelationController.class);
	private static final String PREFIX = "ccusercorrelation/";

	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String DEFAULT_STATUS = "1";

	@Autowired
	private CpqCcUserCorrelationService service;

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
	 * <用户与征信用户关联页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/correlation")
	public String correlation() {
		return PREFIX + "correlation";
	}

	@RequestMapping("/delete")
	@ResponseBody
	public String delete(String id) {
		ResultBeans rs = null;
		try {
			service.delete(id);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
		}
		return rs.toJSONStr();
	}

	@RequestMapping("/start")
	@ResponseBody
	public String start(String id) {
		ResultBeans rs = null;
		try {
			service.start(id);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
		}
		return rs.toJSONStr();
	}

	@RequestMapping("/stop")
	@ResponseBody
	public String stop(String id) {
		ResultBeans rs = null;
		try {
			service.stop(id);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
		}
		return rs.toJSONStr();
	}

	@RequestMapping(value = "/createCorrelation", method = RequestMethod.POST)
	@ResponseBody
	public String correlation(@RequestBody CcUserCorrelationVo vo, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			String UserName = (String) UserUtils.getUserInfo(request).get("username");
			CcUserCorrelationVoResultBean rb = vo.getCorrelation(DEFAULT_STATUS, UserName, service);
			if (rb.isRepeatFlag()) {
				rs = new ResultBeans(Constants.ERRORCODE, "操作失败!<br/>" + rb.getErrorMsg());
			} else {
				List<CpqUsermap> correlation = rb.getMappers();
				service.save(correlation);
				rs = ResultBeans.sucessResultBean();
			}
		} catch (Exception e) {
			log.error("关联征信用户出现异常！", e);
			rs = new ResultBeans(Constants.ERRORCODE, "关联征信用户出现异常！");
		}
		return rs.toJSONStr();
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
	public String getPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqUsermap> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String deptCode = getWithinDeptCode(UserUtils.getDeptCode(request));
			searchParams.put("IN_deptcode", deptCode);
			queryResults = service.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("archive list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqUsermap>());
		}
		page = processQueryResults(model, page, queryResults == null ? new PageImpl<>(new ArrayList<CpqUsermap>()) : queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

}
*/
