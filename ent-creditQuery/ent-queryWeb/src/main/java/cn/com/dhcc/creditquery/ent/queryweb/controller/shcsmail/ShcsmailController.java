package cn.com.dhcc.creditquery.ent.queryweb.controller.shcsmail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/shcsmail")
public class ShcsmailController extends BaseController {
	private static final String PREFIX = "shcsmail/";

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}
}
