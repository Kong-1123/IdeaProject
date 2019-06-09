package cn.com.dhcc.creditquery.person.queryweb.controller.reqresult;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.vo.LegalReview;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryReq;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/reqresult")
public class ReqResultController extends BaseController {

	private static final String PREFIX = "reqresult/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "queryTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String USERNAME = "username";

	@Autowired
	private CpqApproveFlowService cpqCheckInfoService;
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
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
	 * 分页列表 展示异步审批所有状态
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
		Date endTime = null;
		try {
			log.info("list page:" + page);
			String orgCode = UserConfigUtils.getDeptCode(request);
			String username = UserConfigUtils.getUserName(request);
			JPAParamGroup paramGroupFinal = processRequestParamsToJPAParmGroup(page, request);
			JPAParamGroup[] params = paramGroupFinal.getGroups();
			for (JPAParamGroup p : params) {
				if (p.getKey().equals("LT_queryTime")) {
					endTime = (Date) p.getValue();
					if (endTime != null) {
						endTime = endTimeHandler(endTime);
						p.setValue(endTime);
					}
				}
			}
			
			JPAParamGroup operatorGroup = new JPAParamGroup("EQ_operator", username);
			JPAParamGroup operOrgGroup = new JPAParamGroup("EQ_operOrg", orgCode);
			JPAParamGroup rekTypeGroup = new JPAParamGroup("EQ_rekType", Constants.CHECKTYPE_1);
			JPAParamGroup paramGroup = new JPAParamGroup(paramGroupFinal, rekTypeGroup, operatorGroup,operOrgGroup);
			queryResults = cpqCheckInfoService.getPage(paramGroup, page.getCurPage(), page.getMaxSize(), DIRECTION,
					ORDERBY);
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
		CpqApproveBo checkInfo = cpqCheckInfoService.findCpqApproveById(id);
		QueryEncryptUtil.QueryEncrypt(checkInfo);
		String jsonString = JsonUtil.toJSONString(checkInfo, DATE_FORMAT);
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
			List<CpqApproveBo> checkInfo = null;
			String username = getUserInfo(request, USERNAME);
			String deptCode = UserConfigUtils.getDeptCode(request);
			List<CpqApproveBo> checkInfos = cpqCheckInfoService.findCpqApprovesByIds(ids);
			for (CpqApproveBo cpqApproveBo : checkInfos) {
				if (!cpqApproveBo.getStatus().equals(Constants.WAIT_CHECK)
						|| cpqApproveBo.getOperator().equals(username)) {
					errorIds = new ArrayList<String>();
					errorIds.add(cpqApproveBo.getClientName());
				}
			}
			if (null == errorIds) {
				for (CpqApproveBo checkInfo2 : checkInfos) {
					checkInfo = new ArrayList<CpqApproveBo>();
					checkInfo2.setRekUser(username);
					checkInfo2.setStatus(Constants.ALREADY_RECEIVED);
					checkInfo2.setOperTime(new Date());
					checkInfo2.setRekOrg(deptCode);
					checkInfo.add(checkInfo2);
				}
				cpqCheckInfoService.batchUpdate(checkInfo);
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
	/**
	 * 领取审批任务（可批量，可单笔）
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/applyTask")
	@ResponseBody
	public String applyTask(Model model, HttpServletRequest request, @RequestBody List<String> ids) {
		log.info("applyTask ids : ", ids.toString());
		ResultBeans rs;
		try {
			List<CpqApproveBo> checkInfo = null;
			String username = getUserInfo(request, USERNAME);
			String deptCode = UserConfigUtils.getDeptCode(request);
			List<CpqApproveBo> checkInfos = cpqCheckInfoService.findCpqApprovesByIds(ids);
			for (CpqApproveBo checkInfo2 : checkInfos) {
				checkInfo = new ArrayList<CpqApproveBo>();
				checkInfo2.setRekUser(username);
				checkInfo2.setStatus(Constants.WAIT_CHECK);
				checkInfo2.setOperTime(new Date());
				checkInfo2.setRekOrg(deptCode);
				checkInfo.add(checkInfo2);
			}
			cpqCheckInfoService.batchUpdate(checkInfo);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("applyTask error:  ", e);
		}
		log.info("applyTask result : ", rs);
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
	
	@RequestMapping("/applyReport")
	@ResponseBody
	public String applyReport(HttpServletRequest request, CpqApproveBo checkInfo) {
		log.info("applyReport CpqCheckInfo = {} ", checkInfo);
		LegalReview lr = null;
		//根据ID从数据库中查询相应数据。将checkInfo中的证件号码替换为正常的
		String id = checkInfo.getId();
		CpqApproveBo checkInfo2 = cpqCheckInfoService.findCpqApproveById(id);
		QueryReq info = checkIngo2QueryReq(checkInfo2);
		String rootDeptCode = UserConfigUtils.getRootDeptCode(checkInfo2.getOperOrg());
		String loaclDay = ConfigUtil.getLocalReportValidity();
		boolean haveLocalReport = cpqFlowManageService.isHaveLocalReport(checkInfo2.getClientName(), checkInfo2.getCertType(), checkInfo2.getCertNo(), checkInfo2.getQryReason(),rootDeptCode,loaclDay);
		if(haveLocalReport){
			String msg ="本地存在信用报告，是否进行展示?";
			lr = new LegalReview(Constant.LOCAL_HAS_REPORT, msg);
		} else{
			lr = new LegalReview(Constant.LOCAL_NO_REPORT, "本地无报告，进行查询。");
		}
		log.info("applyReport result = {} ", lr);
		return JsonUtil.toJSONString(lr);
	}
	private QueryReq checkIngo2QueryReq(CpqApproveBo checkInfo) {
		QueryReq queryReq = new QueryReq();
		queryReq.setCertNo(checkInfo.getCertNo());
		queryReq.setCertType(checkInfo.getCertType());
		queryReq.setClientName(checkInfo.getClientName());
		queryReq.setQryReason(checkInfo.getQryReason());
		queryReq.setQueryFormat(checkInfo.getQueryFormat());
		queryReq.setQueryTime(checkInfo.getQueryTime());
		queryReq.setQueryType(checkInfo.getQueryType());
		queryReq.setAutharchiveId(checkInfo.getArchiveId());
		queryReq.setAssocbsnssData(checkInfo.getAssocbsnssData());
		queryReq.setCreditUser(checkInfo.getCreditUser());
		queryReq.setCheckId(checkInfo.getId());
		queryReq.setCheckWay(checkInfo.getRekType());
		queryReq.setOperOrg(checkInfo.getOperOrg());
		queryReq.setOperator(checkInfo.getOperator());
		queryReq.setRekOrg(checkInfo.getRekOrg());
		queryReq.setRekTime(checkInfo.getRekTime());
		queryReq.setRekUser(checkInfo.getRekUser());
		queryReq.setClientIp(checkInfo.getClientIp());
		return queryReq;
	}
}
