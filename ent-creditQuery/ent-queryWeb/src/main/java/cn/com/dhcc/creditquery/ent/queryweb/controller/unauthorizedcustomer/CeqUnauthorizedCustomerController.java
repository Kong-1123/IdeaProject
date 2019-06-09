
package cn.com.dhcc.creditquery.ent.queryweb.controller.unauthorizedcustomer;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.querycontrol.CeqUnauthorizedCustomerBo;
import cn.com.dhcc.creditquery.ent.querycontrol.service.authorizedcustomer.CeqUnauthorizedCustomerService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;


@Controller
@RequestMapping(value = "/ceqCustomer")
public class CeqUnauthorizedCustomerController extends BaseController {
	
	private static Logger log = LoggerFactory.getLogger(CeqUnauthorizedCustomerController.class);
    private final static String DIRECTION = "asc";
    private final static String ORDERBY = "id";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private CeqUnauthorizedCustomerService ceqCustomerService;
	
	
	
	/**
	 * 获取用户三项标识列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list1(Model model, PageBean page, HttpServletRequest request) {

		Page<CeqUnauthorizedCustomerBo> queryResults = null;

		try {
			
			String deptCode = UserUtilsForConfig.getDeptCode(request);

			log.info("exceptionRule list2 page =" + page);
			Map<String, Object> searchParams = processRequestParams(page, request);
			//根据当前内部用户所在机构   取未授权列表
			searchParams.put("EQ_deptCode", deptCode);
			queryResults = ceqCustomerService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);

		} catch (Exception e) {
			log.error("exceptionRule list2 method =" + e);
			queryResults = new PageImpl<>(new ArrayList<CeqUnauthorizedCustomerBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("exceptionRule list2 result =" + page);
		return jsonString;

	}
			
}
