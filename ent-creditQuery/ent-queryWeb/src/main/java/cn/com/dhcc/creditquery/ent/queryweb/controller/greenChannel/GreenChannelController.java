package cn.com.dhcc.creditquery.ent.queryweb.controller.greenChannel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.querycontrol.CeqGreenChannelBo;
import cn.com.dhcc.creditquery.ent.querycontrol.entity.CeqGreenChannel;
import cn.com.dhcc.creditquery.ent.querycontrol.service.greenchanne.CeqGreenChannelService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.HttpClientUtil;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/greenChannel")
public class GreenChannelController extends BaseController {
	private static final String PREFIX = "greenChannel/";

	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String FINDBYUSERNAME = "systemUser/findByUserName";

	@Autowired
	private CeqGreenChannelService greenChannelService;

	@Autowired
	private PlantFormInteractiveService plantFormInteractiveService;

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
	 * <跳转 新增页面>
	 * 
	 * @return
	 */
	@RequestMapping("/createPage")
	public String createPage() {
		return PREFIX + "create";
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(Model model, HttpServletRequest request, String id) {
		log.info("findById id = {} ", id);
		CeqGreenChannelBo greenChannelBo = greenChannelService.findById(id);
		String jsonString = JsonUtil.toJSONString(greenChannelBo, DATE_FORMAT);
		log.info("findById result = {} ", jsonString);
		return jsonString;
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
		Page<CeqGreenChannelBo> queryResults = null;
		Date endTime = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			log.info("searchParams ={}", searchParams);
			if (StringUtils.isBlank((String) searchParams.get("IN_operatorOrg"))) {
				List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes, ",");
				searchParams.put("IN_operatorOrg", deptCodeStr);
			}
			endTime = (Date) searchParams.get("LTE_endTimeAuthorized");
			if (endTime != null) {
				endTime = endTimeHandler(endTime);
				searchParams.put("LTE_endTimeAuthorized", endTime);
			}
			queryResults = greenChannelService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("list error={}", e);
			queryResults = new PageImpl<>(new ArrayList<CeqGreenChannelBo>());
		}
		page = processQueryResults(model, page, queryResults);
		return JsonUtil.toJSONString(page, DATE_FORMAT);
	}

	/**
	 * 添加方法
	 * 
	 * @param model
	 * @param request
	 * @param greenChannelBo
	 * @param
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(Model model, HttpServletRequest request, CeqGreenChannelBo greenChannelBo, String beginTimeAuthorized, String endTimeAuthorized) {
		ResultBeans rs = null;
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			Date beginTimeAuthorizedTime = simple.parse(beginTimeAuthorized);
			Date endTimeAuthorizedTime = simple.parse(endTimeAuthorized);
			int compareTo = beginTimeAuthorizedTime.compareTo(endTimeAuthorizedTime);
			if (compareTo >= 0) {
				return new ResultBeans(Constants.ERRORCODE, "授权起始时间 必须小于授权截止时间").toJSONStr();
			}

			Map<String, String> info = UserUtilsForConfig.getUserInfo(request);
			String userId = info.get("username").trim();
			String deptCode = info.get("orgNo").trim();
			Date now = new Date();
			// 授权人
			greenChannelBo.setOperator(userId);
			// 授权机构
			greenChannelBo.setOperatorOrg(deptCode);
			// 授权时间
			greenChannelBo.setUpdateTime(now);
			greenChannelBo.setStatus("0");
			greenChannelBo.setBeginTimeAuthorized(simple.parse(beginTimeAuthorized));
			greenChannelBo.setEndTimeAuthorized(simple.parse(endTimeAuthorized));
			greenChannelService.create(greenChannelBo);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("ccuser create error e={}", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 特权用户的停用
	 * @param id
	 * @return
	 */
	@RequestMapping("/stopGreen")
	@ResponseBody
	public String stopGreen(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CeqGreenChannelBo greenChannelBo = greenChannelService.findById(id);
			greenChannelBo.setStatus("1");
			greenChannelService.update(greenChannelBo);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("stopGreen error ", e);
		}
		return rss.toJSONStr();

	}

	/**
	 * 特权用户的启动
	 * @param id
	 * @return
	 */
	@RequestMapping("/startGreen")
	@ResponseBody
	public String startGreen(HttpServletRequest req, String id) {
		ResultBeans rss = null;
		try {
			CeqGreenChannelBo greenChannelBo = greenChannelService.findById(id);
			greenChannelBo.setStatus("0");
			greenChannelService.update(greenChannelBo);
			rss = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rss = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("startGreen error ", e);
		}
		return rss.toJSONStr();

	}

	/**
	 * 新增特权用户 判断用户辖内及机构
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public String checkUser(HttpServletRequest req, String userName) {
		log.info("checkUser method begin ...");
		ResultBeans rs = null;
		try {
			SystemUser systemUser = plantFormInteractiveService.findSystemUserByUserName(userName);
			if (null == systemUser) {
				rs = new ResultBeans(Constants.ERRORCODE, "该用户名不存在");
				return rs.toJSONStr();
			} else {
				String orgId = systemUser.getOrgId();
				if (UserUtilsForConfig.getJurisdictionDeptCodes(req).contains(orgId)) {
					rs = new ResultBeans(Constants.SUCCESSCODE, "合法用户");
					rs.setObjectData(orgId);
				} else {
					rs = new ResultBeans(Constants.ERRORCODE, "该用户不是辖内用户");
				}
			}
		} catch (Exception e) {
			log.error("checkUser : ", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}
}
