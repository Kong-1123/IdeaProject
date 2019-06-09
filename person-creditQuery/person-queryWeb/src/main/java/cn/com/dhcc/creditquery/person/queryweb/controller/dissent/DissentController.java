/*
package cn.com.dhcc.creditquery.person.queryweb.controller.dissent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
import cn.com.dhcc.query.creditpersonquerydao.entity.userarr.CpqUserAttr;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.http.impl.RemoteRequestCoordinatorImpl;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.http.login.LoginCRCException;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.info.RequestInfo;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.info.ResponseInfo;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.util.BaseCookie;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.util.CookieInfo;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.util.CookieUtil;
import cn.com.dhcc.query.creditpersonqueryservice.ccuser.service.CpqCcUserService;
import cn.com.dhcc.query.creditpersonqueryservice.userattr.service.util.UserAttrUtil;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
@Controller
@RequestMapping(value = "/dissent")
public class DissentController extends BaseController{


	private static Logger log = LoggerFactory.getLogger(DissentController.class);
	private static final String PREFIX = "dissent/";
	private static final String USERNAME = "username";
	private static final String CHARSET = "GBK";
	private static final String SUBSYSTEM = "ent";

	@Autowired
	private RemoteRequestCoordinatorImpl loginSrevice;

	@Autowired
	private CpqCcUserService ccuserService;

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}
	@RequestMapping("/tryLogin")
	@ResponseBody
	public String tryLogin(HttpServletRequest request, HttpServletResponse response) {
		ResultBeans rs = null;
		try {
			String userName = getUserInfo(request, USERNAME);
			CpqUserAttr findCpqUserAttrBySystemUserId = UserAttrUtil.findCpqUserAttrBySystemUserId(userName);
			if (null==findCpqUserAttrBySystemUserId) {
				rs = new ResultBeans(Constants.ERRORCODE, "对不起，您未配置属性，该功能无法使用。");
				return rs.toJSONStr();
			}
			String ccId = findCpqUserAttrBySystemUserId.getCreditUser();
			if (StringUtils.isBlank(ccId)) {
				rs = new ResultBeans(Constants.ERRORCODE, "对不起，您未配置征信用户，该功能无法使用。");
				return rs.toJSONStr();
			}
			CpqCcUser findByCcId = ccuserService.findByCcId(ccId);
			String ccuser2 = findByCcId.getCcuser();
			CookieInfo info = loginToCreditCenter(ccuser2, findByCcId);
			
			List<BaseCookie> cookie = info.getCookie();
			
			if(null == cookie){
				rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			}else{
				for (BaseCookie cookie2 : cookie) {
					if("JSESSIONID".equals(cookie2.getName())){
						Cookie c = new Cookie(cookie2.getName(), cookie2.getValue());
						c.setPath("/shcreditunion");
						response.addCookie(c);
						rs = ResultBeans.sucessResultBean();
						break;
					}
				}
			}
			
			
		} catch (Exception e) {
			log.error("tryLogin error :", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}

	private CookieInfo loginToCreditCenter(String ccuserName, CpqCcUser ccuser) throws LoginCRCException, IOException {
		CookieInfo cookie = new CookieInfo(ccuserName, ccuser.getPasswd(), ccuser.getCcdept());
		cookie = CookieUtil.getCookie(cookie);
		cookie = null;
		if (null == cookie) {
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setEncoding(CHARSET);
			requestInfo.setOrgcode(ccuser.getCcdept());
			requestInfo.setPassword(ccuser.getPasswd());
			requestInfo.setUser(ccuserName);
			requestInfo.setSubSystem(SUBSYSTEM);
			ResponseInfo login = loginSrevice.login(requestInfo);
			cookie = login.getCookie();
//			cookie = new CookieInfo(ccuserName, ccuser.getPasswd(), ccuser.getCcdept());
//			cookie = CookieUtil.getCookie(cookie);
		}
		return cookie;
	}


}
*/
