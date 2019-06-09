/**
 *  Copyright (c)  @date 2018年11月26日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.ent.queryweb.controller.exceptionrule;

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
import cn.com.dhcc.creditquery.ent.querycontrol.bo.CeqExceptionRuleBo;
import cn.com.dhcc.creditquery.ent.querycontrol.service.exceptionrule.CeqExceptionRuleService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

/**
 * 
 * @author lekang.liu
 * 
 */

@Controller
@RequestMapping(value = "/exceptionRule")
public class ExceptionRuleController extends BaseController{
	
	
	private static Logger log = LoggerFactory.getLogger(ExceptionRuleController.class);
    private static final String PREFIX = "exceptionRule/";
    private final static String DIRECTION = "asc";
    private final static String ORDERBY = "ruleCode";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	
	@Autowired
	private CeqExceptionRuleService service;
	
	
	
	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}

	/**
	 * <详情页面>
	 * 
	 * @return
	 */
	
	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}	
	
	/**
	 * <跳转到客户三项标识页面>
	 * 
	 * @return
	 */
	@RequestMapping("/identify")
	public String identify() {
		return PREFIX + "identify";
	}
	
	
	/**
	 * <跳转到修改页面>
	 * 
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}	
	
	
	
	
	/**
	 * 
	 * 分页列表
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {

		Page<CeqExceptionRuleBo> queryResults = null;
		
		try {
			
			log.info("exceptionRule list page =" + page);
			Map<String, Object> searchParams = processRequestParams(page, request);	
			String deptCode = UserUtilsForConfig.getRootDeptCode(request);
			searchParams.put("EQ_deptCode", deptCode);
			queryResults = service.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			
		} catch (Exception e) {
			log.error("exceptionRule list method =" + e);
			queryResults = new PageImpl<>(new ArrayList<CeqExceptionRuleBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("exceptionRule list result =" + page);
		return jsonString;
	}
	
	
	/**
	 *  修改
	 * @param model
	 * @param request
	 * @param ceqExceptionRuleBo
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	 @LogOperation("异常查询阻断修改")
	public ResultBeans update(Model model, HttpServletRequest request, CeqExceptionRuleBo ceqExceptionRuleBo) {
		ResultBeans rs = null;
		
		try {
			
			log.info("exceptionRule update CeqExceptionRule =" + ceqExceptionRuleBo);
			service.update(ceqExceptionRuleBo);
			rs = ResultBeans.sucessResultBean();
			
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
            log.error("ExceptionRule update method =" + e);
		}
		log.info("exceptionRule update result =" + rs);
		return rs;
		
	} 
	
	
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
    @ResponseBody
    public String findById(String id) {
		log.info("exceptionRule findById id =" + id);
		CeqExceptionRuleBo cer = service.findById(id);
		String jsonString = JsonUtil.toJSONString(cer, DATE_FORMAT);
		log.info("exceptionRule findById result =" + jsonString);
        return jsonString;
    }
	
	

	/**
	 * 规则的启用和停用
	 * @param id 規則主鍵
	 * @param stopFlag 啟停状态
	 * @return
	 */
	@RequestMapping("/stopFlag")
    @ResponseBody
    @LogOperation("异常查询阻断启停用")
    public String stopFlag(String id , String stopFlag ) {
		
		ResultBeans rs = null;
		try {	
			log.info("ExceptionRuleController stopFlag method begin,  id= {},stopFlag = {}",
					id, stopFlag);
			if( null != id && ("0".equals(stopFlag) || "1".equals(stopFlag) )){				
				service.updateStopFlagById( id , stopFlag);
				rs = new ResultBeans(Constants.SUCCESSCODE, Constants.SUCCESSMSG);
			}			
		} catch (Exception e) {
			//e.printStackTrace();
			//log.error(msg);
			log.error("ExceptionRuleController stopFlag method error = ",e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		log.info("ExceptionRuleController stopFlag method result = ", rs.toJSONStr());
       return rs.toJSONStr();
    }
	
}
