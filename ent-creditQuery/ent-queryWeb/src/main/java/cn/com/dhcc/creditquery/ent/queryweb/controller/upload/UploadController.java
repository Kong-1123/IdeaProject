//package cn.com.dhcc.creditquery.ent.queryweb.controller.upload;
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
//import cn.com.dhcc.query.creditenterprisequeryweb.controller.dissent.DissentController;
//import lombok.extern.slf4j.Slf4j;
//@Slf4j
//@Controller
//@RequestMapping(value = "/upload")
//public class UploadController extends BaseController {
//
//	private static Logger log = LoggerFactory.getLogger(UploadController.class);
//	private static final String PREFIX = "upload/";
//	@Autowired
//	private DissentController dissent;
//
//	@RequestMapping("/list")
//	public String list() {
//		return PREFIX + "list";
//	}
//
//	@RequestMapping("/tryLogin")
//	@ResponseBody
//	public String tryLogin(HttpServletRequest request, HttpServletResponse response) {
//		return dissent.tryLogin(request,response);
//	}
//
//}
