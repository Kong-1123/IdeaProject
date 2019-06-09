package cn.com.dhcc.creditquery.person.queryweb.controller.checkinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;

/**
 *
 * @author lekang.liu
 * @date 2018年3月8日
 */
@Controller
@RequestMapping(value = "/checkinfo")
public class CheckInfoController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(CheckInfoController.class);
	private static final String PREFIX = "checkinfo/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "queryTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String USERNAME = "username";

	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
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
	 * <跳转到详情页面>
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}

	/**
	 * <搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";
	}

	/**
	 * 分页列表 只展示待审批的任务
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @param pageType
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request, String pageType) {
		Page<CpqApproveBo> queryResults = null;
		try {
			log.info("list page:" + page);
			JPAParamGroup paramGroupFinal = processRequestParamsToJPAParmGroup(page, request);
			/**
			 * 辖内
			 */
			JPAParamGroup[] groups = paramGroupFinal.getGroups();
			boolean  flag = true;
			for (JPAParamGroup jpaParamGroup : groups) {
				if("IN_operOrg".equals(jpaParamGroup.getKey())){
					flag = false;
				}
			}
			JPAParamGroup deptCodeGroup = null;
			if(flag){
				List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes,",");
				deptCodeGroup = new JPAParamGroup("IN_operOrg", deptCodeStr);
			}

			JPAParamGroup operUser = new JPAParamGroup("NE_operator", getUserInfo(request, USERNAME));
			JPAParamGroup rekTypeGroup = new JPAParamGroup("EQ_rekType", Constants.CHECKTYPE_1);
			JPAParamGroup statusGroup = new JPAParamGroup("EQ_status", Constants.WAIT_CHECK);
			JPAParamGroup group = new JPAParamGroup(statusGroup, rekTypeGroup,operUser);
			JPAParamGroup paramGroup = null;
			if(null != deptCodeGroup){
				paramGroup = new JPAParamGroup(deptCodeGroup,group);
			}else{
				paramGroup = group;
			}
			queryResults = cpqApproveFlowService.getPage(new JPAParamGroup(paramGroupFinal, paramGroup), page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
		} catch (Exception e) {
			log.error("checkinfo list method :" + e);
			queryResults = new PageImpl<>(new ArrayList<CpqApproveBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("list : result: ", jsonString);
		return jsonString;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(Model model, HttpServletRequest request, String id) {
		log.info("findCpqQueryRecordBoById id = {} ", id);
		CpqApproveBo cpqApproveBo = cpqApproveFlowService.findCpqApproveById(id);
		String jsonString = JsonUtil.toJSONString(cpqApproveBo, DATE_FORMAT);
		log.info("findCpqQueryRecordBoById result = {} ", jsonString);
		return jsonString;
	}

	/**
	 * 领取审批任务（可批量，可单笔）
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/receiveTask")
	@ResponseBody
	public String receiveTask(Model model, HttpServletRequest request, @RequestBody List<String> ids) {
		log.info("receiveTask ids : ", ids.toString());
		ResultBeans rs;
		try {
			List<String> errorIds = null;
			List<CpqApproveBo> cpqApproveBoList = null;
			String username = getUserInfo(request, USERNAME);
			String deptCode = UserConfigUtils.getDeptCode(request);
			List<CpqApproveBo> cpqApproveBos = cpqApproveFlowService.findCpqApprovesByIds(ids);
			for (CpqApproveBo cpqApproveBo : cpqApproveBos) {
				if (!cpqApproveBo.getStatus().equals(Constants.WAIT_CHECK) || cpqApproveBo.getOperator().equals(username)) {
					errorIds = new ArrayList<String>();
					errorIds.add(cpqApproveBo.getClientName());
				}
			}
			if (null == errorIds) {
				cpqApproveBoList = new ArrayList<CpqApproveBo>();
				for (CpqApproveBo cpqApproveBo : cpqApproveBos) {
					cpqApproveBo.setRekUser(username);
					cpqApproveBo.setStatus(Constants.ALREADY_RECEIVED);
					cpqApproveBo.setOperTime(new Date());
					cpqApproveBo.setRekOrg(deptCode);
					cpqApproveBoList.add(cpqApproveBo);
				}
				cpqApproveFlowService.batchUpdate(cpqApproveBoList);
				rs = ResultBeans.sucessResultBean();
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "客户姓名为" + errorIds.toString() + "的请求不可领取，请领取其他任务。");
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("receiveTask method  ", e);
		}
		log.info("receiveTask result : ", rs);
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
}
