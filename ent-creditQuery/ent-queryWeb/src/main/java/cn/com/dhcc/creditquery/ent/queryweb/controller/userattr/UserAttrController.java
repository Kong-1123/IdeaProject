package cn.com.dhcc.creditquery.ent.queryweb.controller.userattr;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryconfig.CeqUserAttrBo;
import cn.com.dhcc.creditquery.ent.queryconfig.service.CeqUserAttrService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;


/**
 * 
 * @author chenting
 * @date 2018年4月21日 new
 */
@Controller
@RequestMapping("/userattr")
public class UserAttrController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(UserAttrController.class);

	@Autowired
	private CeqUserAttrService service;

	private final static String PREFIX = "userattr/";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


	/**
	 * 跳转 详情 页面
	 * 
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage() {
		return PREFIX + "detail";
	}



	/**
	 * 跳转 新增 页面
	 * 
	 * @return
	 */
	@RequestMapping("/createPage")
	public String createPage() {
		return PREFIX + "create";
	}

	/**
	 * 跳转 修改 页面
	 * 
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}


	/**
	 * 新增
	 * 
	 * @param model
	 * @param ceqUserAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(CeqUserAttrBo ceqUserAttrTo,HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			
			CeqUserAttrBo userAttr = service.findByUserId(ceqUserAttrTo.getUserName());
			if (null != userAttr) {
				return new ResultBeans(Constants.ERRORCODE, "该用户已创建用户属性信息，不可重复创建。如需修改，请选中该用户后点击修改按钮。").toJSONStr();
			}
			Date createDate = new Date();
			String userName = UserUtilsForConfig.getUserName(request);
			ceqUserAttrTo.setCreateUser(userName);
			ceqUserAttrTo.setCreateDate(createDate);
			ceqUserAttrTo.setLockStat("1");
			log.info("user attribute create start,param={}", ceqUserAttrTo);
			CeqUserAttrBo ceqUserAttr2 = service.create(ceqUserAttrTo);
			rs = ResultBeans.sucessResultBean();
			rs.setObjectData(ceqUserAttr2);
		} catch (Exception ex) {
			log.error("user attribute create error" , ex);
			rs = new ResultBeans(Constants.ERRORCODE, "企业用户属性新建失败");
		}
		return rs.toJSONStr();
	}

	
	/**
	 * 删除
	 * 
	 * @param model
	 * @param request
	 * @param id
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
			rs = new ResultBeans(Constants.ERRORCODE, "删除用户属性失败！");
		}
		return rs.toJSONStr();
		
	}
	

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findByUserName")
	@ResponseBody
	public String findByUserName(String userName) {
		CeqUserAttrBo userAttr = service.findByUserId(userName);
		return JsonUtil.toJSONString(userAttr, DATE_FORMAT);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 * @param ceqUserAttr
	 * @param request
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String update(CeqUserAttrBo userAttr,HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			log.info("update start userAttr={}", userAttr);
			Date updateDate = new Date();
			userAttr.setUpdateDate(updateDate);
			log.info("user attribute update start,param={}", userAttr);
			service.update(userAttr);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception ex) {
			rs = new ResultBeans(Constants.ERRORCODE, "企业用户属性更新失败");
			log.error("user attribute update error e = " , ex);
		}
		return rs.toJSONStr();
	}
}
