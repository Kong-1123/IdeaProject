package cn.com.dhcc.creditquery.person.queryweb.controller.unauthorizedcustomer;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.query.bo.querycontrol.CpqUnauthorizedCustomerBo;
import cn.com.dhcc.creditquery.person.querycontrol.service.authorizedcustomer.CpqUnauthorizedCustomerService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.controller.exceptionrule.ExceptionRuleController;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;


@Controller
@RequestMapping(value = "/cpqCustomer")
public class CpqUnauthorizedCustomerController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(ExceptionRuleController.class);
    private static final String PREFIX = "cpqCustomer/";
    private final static String DIRECTION = "asc";
    private final static String ORDERBY = "id";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private CpqUnauthorizedCustomerService cpqCustomerService;
	
	
	

	/**
	 * 获取用户三项标识列表
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list1(Model model, PageBean page, HttpServletRequest request) {

		Page<CpqUnauthorizedCustomerBo> queryResults = null;

		try {
			
			String deptCode = UserConfigUtils.getDeptCode(request);
			
			log.info("exceptionRule list2 page =" + page);
			Map<String, Object> searchParams = processRequestParams(page, request);
			searchParams.put("EQ_deptCode", deptCode);
			queryResults = cpqCustomerService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);

		} catch (Exception e) {
			log.error("exceptionRule list2 method =" + e);
			queryResults = new PageImpl<>(new ArrayList<CpqUnauthorizedCustomerBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("exceptionRule list2 result =" + page);
		return jsonString;

	}
			
}
