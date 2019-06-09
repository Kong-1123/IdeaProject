/*
package cn.com.dhcc.creditquery.person.queryweb.controller.ouperininfoup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.controller.dissent.DissentController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/ouperininfoup")
public class OuPerinInfoUpController extends BaseController {
	private static final String PREFIX = "ouperininfoup/";
	@Autowired
	private DissentController dissent;

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}
	@RequestMapping("/tryLogin")
	@ResponseBody
	public String tryLogin(HttpServletRequest request, HttpServletResponse response) {
		return dissent.tryLogin(request, response);
	}


}
*/
