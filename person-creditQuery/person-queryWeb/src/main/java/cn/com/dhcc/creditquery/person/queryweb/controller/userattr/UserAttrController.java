package cn.com.dhcc.creditquery.person.queryweb.controller.userattr;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo;
import cn.com.dhcc.creditquery.person.queryconfig.service.CpqUserAttrService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/userattr")
public class UserAttrController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(UserAttrController.class);
	private static final String PREFIX = "userattr/";

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqUserAttrService service;


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
	 * 添加方法
	 * 
	 * @param model
	 * @param request
	 * @param id
	 * @param config
	 * @return
	 */
	/**
	 *
	 * @param userAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(CpqUserAttrBo userAttr, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			CpqUserAttrBo cpqUserAttr = service.findByUserId(userAttr.getUserName());
			if (null != cpqUserAttr) {
				return new ResultBeans(Constants.ERRORCODE, "该用户已创建用户属性信息，不可重复创建。如需修改，请选中该用户后点击修改按钮。").toJSONStr();
			}
			String createUser = UserConfigUtils.getUserName(request);
			Date createDate = new Date();
			userAttr.setCreateUser(createUser);
			userAttr.setCreateDate(createDate);
			userAttr.setLockStat("1");
			log.info("user attribute create start,param={}", userAttr);
			CpqUserAttrBo userAttr2 = service.create(userAttr);
			rs = ResultBeans.sucessResultBean();
			rs.setObjectData(userAttr2);
		} catch (Exception e) {
			log.error("user attribute create error", e);
			rs = new ResultBeans(Constants.ERRORCODE, "创建用户属性失败！");
		}
		return rs.toJSONStr();
	}


	/**
	 * 
	 * @param userName
	 * @return
	 */
	@RequestMapping("/findByUserName")
	@ResponseBody
	public String findByUserId(String userName) {
		CpqUserAttrBo userAttr = service.findByUserId(userName);
		return JsonUtil.toJSONString(userAttr, DATE_FORMAT);
	}


	/**
	 * 删除
	 * @param userNames
	 * @return
	 */
	@RequestMapping("/deleteByUserName")
	@ResponseBody
	public String deleteByUserName(String[] userNames) {
		ResultBeans rs = null;
		try {
			List<String> asList = Arrays.asList(userNames);
			service.deleteByUserName(asList);
			rs = new ResultBeans(Constants.SUCCESSCODE, "删除用户属性成功！");
		} catch (Exception e) {
			log.error("deleteByUserName error e = " + e);
			rs = new ResultBeans(Constants.ERRORCODE, "删除用户属性失败！");
		}
		return rs.toJSONStr();
		
	}

	/**
	 * 修改方法
	 * @param userAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(CpqUserAttrBo userAttr,HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			log.info("update start userAttr={}", userAttr);
			userAttr.setUpdateDate(new Date());
			service.update(userAttr);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("update error e={}", e);
			rs = new ResultBeans(Constants.ERRORCODE, "修改用户属性失败！");
		}
		return rs.toJSONStr();
	}
}
