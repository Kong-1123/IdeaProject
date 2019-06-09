//package cn.com.dhcc.creditquery.ent.queryweb.controller.dissent;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
//import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
//import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//@RequestMapping(value = "/dissent")
//public class DissentController extends BaseController {
//
//	private static final String PREFIX = "dissent/";
//	private static final String USERNAME = "username";
//	private static final String CHARSET = "GBK";
//	private static final String SUBSYSTEM = "ent";
//
//	@Autowired
//	private RemoteRequestCoordinatorImpl loginSrevice;
//
//	@Autowired
//	private CeqCcUserService ccuserService;
//
//	@RequestMapping("/list")
//	public String list() {
//		return PREFIX + "list";
//	}
//
//	@RequestMapping("/tryLogin")
//	@ResponseBody
//	public String tryLogin(HttpServletRequest request, HttpServletResponse response) {
//		ResultBeans rs = null;
//		try {
//			String userName = getUserInfo(request, USERNAME);
//			CeqUserAttr findCpqUserAttrBySystemUserId = CeqUserAttrUtil.findCeqUserAttrBySystemUserId(userName);
//			if (null==findCpqUserAttrBySystemUserId) {
//				rs = new ResultBeans(Constants.ERRORCODE, "对不起，您未配置属性，该功能无法使用。");
//				return rs.toJSONStr();
//			}
//			String ccId = findCpqUserAttrBySystemUserId.getCreditUser();
//			if (StringUtils.isBlank(ccId)) {
//				rs = new ResultBeans(Constants.ERRORCODE, "对不起，您未配置征信用户，该功能无法使用。");
//				return rs.toJSONStr();
//			}
//			CeqCcuser findByCcId = ccuserService.findByCcId(ccId);
//			if (null != findByCcId) {
//				String ccuser2 = findByCcId.getCcUser();
//				CookieInfo info = loginToCreditCenter(ccuser2, findByCcId);
//				List<BaseCookie> cookies = info.getCookies();
//				if (cookies.size() > 0) {
////					String cookieInfo = info.getContext().get(0).replace("JSESSIONID=", "");
////					cookieInfo = cookieInfo.substring(0, cookieInfo.indexOf(";"));
////					Cookie c = new Cookie("JSESSIONID", cookieInfo);
////					c.setPath("/shwebroot");
////					response.addCookie(c);
//					for (BaseCookie baseCookie : cookies) {
//						if("JSESSIONID".equals(baseCookie.getName())){
//							Cookie c = new Cookie("JSESSIONID", baseCookie.getValue());
//							c.setPath("/shwebroot");
//							response.addCookie(c);
//						}
//					}
//					rs = ResultBeans.sucessResultBean();
//				}
//			}else{
//				rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
//			}
//			
//		} catch (Exception e) {
//			log.error("tryLogin error :", e);
//			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
//		}
//		return rs.toJSONStr();
//	}
//
//	private String getUserInfo(HttpServletRequest request, String infoName) {
//		Map<String, String> info = UserUtils.getUserInfo(request);
//		String infoValue = info.get(infoName).trim();
//		return infoValue;
//	}
//
//	private CookieInfo loginToCreditCenter(String ccuserName, CeqCcuser ccuser) throws LoginCRCException, IOException {
//		CookieInfo cookie = new CookieInfo(ccuserName, ccuser.getPasswd(), ccuser.getCcDept());
//		cookie = CookieUtil.getCookie(cookie);
//
//		if (true) {
//			RequestInfo requestInfo = new RequestInfo();
//			requestInfo.setEncoding(CHARSET);
//			requestInfo.setOrgcode(ccuser.getCcDept());
//			requestInfo.setPassword(ccuser.getPasswd());
//			requestInfo.setUser(ccuserName);
//			requestInfo.setSubSystem(SUBSYSTEM);
//			ResponseInfo login = loginSrevice.login(requestInfo);
//			cookie = login.getCookie();
//		}
//		return cookie;
//	}
//	@RequestMapping({ "/peoplesBankEnterpriseMsg" })
//	@ResponseBody
//	public String peoplesBankEnterpriseMsg() {
//		String nginx = CeqConfigUtil.getConfigValueByName("nginxRoot");
//		try {
//			Document doc = Jsoup.connect(nginx + "shwebroot/index_demur.jsp").get();
//			Elements elements = doc.select("a[href!=#]");
//			for (Element ele : elements) {
//				ele.attr("href", ele.absUrl("href"));
//			}
//			Elements imgs = doc.select("img[src]");
//			for (Element ele : imgs) {
//				ele.attr("src", ele.absUrl("src"));
//			}
//			Elements scripts = doc.select("script[src]");
//			for (Element ele : scripts) {
//				ele.attr("src", ele.absUrl("src"));
//			}
//			Elements links = doc.select("link[href]");
//			for (Element ele : links) {
//				ele.attr("href", ele.absUrl("href"));
//			}
//			Elements body = doc.select("body");
//			body.removeAttr("onload");
//			Elements form = doc.select("form[name=logon]");
//			form.remove();
//			Elements a = doc.select(".menuButton");
//			a.remove();
//			return doc.toString();
//		} catch (IOException e) {
//			log.error("请求人行企业异议处理失败", e);
//		}
//		return "";
//	}
//}
