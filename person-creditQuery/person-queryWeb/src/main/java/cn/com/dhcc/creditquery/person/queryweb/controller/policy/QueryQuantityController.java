/*
package cn.com.dhcc.creditquery.person.queryweb.controller.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditpersonqueryservice.policy.inquiryflow.CpqInquiryPolicyService;
import cn.com.dhcc.query.creditpersonqueryservice.policy.utils.WorkflowControl;
import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.QueryReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/queryquantity")
public class QueryQuantityController  extends BaseController{
	private static Logger log = LoggerFactory.getLogger(QueryQuantityController.class);
	private static final String PREFIX = "queryquantity/";
	@Autowired
	CpqInquiryPolicyService inquiryPolicyService;
	*/
/**
	 * <分页列表页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/list")
	public String list(QueryReq queryReq) {
		WorkflowControl workflowControl = inquiryPolicyService.queryQuantityControl(queryReq);
		String jsonSting = JsonUtil.toJSONString(workflowControl);
		return jsonSting;
	}

}
*/
