package cn.com.dhcc.creditquery.person.queryweb.controller.orgattr;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqOrgAttrBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqOrgAttrService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 机构属性
 * 
 * @author lhf
 * @date 2019年4月18日
 */

@Slf4j
@Controller
@RequestMapping(value = "/orgattr")
public class OrgAttrController extends BaseController {

	private static final String PREFIX = "orgattr/";

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqOrgAttrService service;

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
	 * <跳转 修改 页面>
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
	@RequestMapping("/detailPage")
	public String detail() {
		return PREFIX + "detail";
	}

	/**
	 *
	 * @param orgAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String saveOrUpdate(CpqOrgAttrBo orgAttr, HttpServletRequest request) {
		log.info("saveOrUpdate start,orgAttr = {}", orgAttr);
		ResultBeans rs = null;
		try {
			String userName = UserUtilsForConfig.getUserName(request);
			orgAttr.setCreateUser(userName);
			service.saveOrUpdate(orgAttr);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			log.error("org attribute create error", e);
			rs = new ResultBeans(Constants.ERRORCODE, "创建机构属性失败！");
		}
		log.info("saveOrUpdate end,rs = {}", rs);
		return rs.toJSONStr();
	}

	/**
	 * 
	 * @param orgId
	 * @return
	 */
	@RequestMapping("/findByOrgId")
	@ResponseBody
	public String findByOrgId(String orgId) {
		log.info("findByOrgId start, orgId = {}", orgId);
		CpqOrgAttrBo orgAttr = null;
		try {
			orgAttr = service.findByOrgId(orgId);
		} catch (Exception e) {
			orgAttr = new CpqOrgAttrBo();
			log.error("findByOrgId error,e = {}",e);
		}
		log.info("findByOrgId end, orgAttr = {}", orgAttr);
		return JsonUtil.toJSONString(orgAttr, DATE_FORMAT);
	}

}
