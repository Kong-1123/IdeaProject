package cn.com.dhcc.creditquery.ent.queryweb.controller.inquire;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.dhcc.credit.creditplatform.jpa.util.UserInfoUtils;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.NetworkUtil;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqPageFlowControlInfo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.HttpClientUtil;
import cn.com.dhcc.creditquery.ent.queryweb.vo.LegalReview;
import cn.com.dhcc.creditquery.ent.queryweb.vo.QueryReq;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportLogService;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportViewReadService;
import cn.com.dhcc.creditquery.ent.reportview.util.ResultBeans;
import cn.com.dhcc.login.bean.LoginType;
import cn.com.dhcc.login.bean.RequestBean;
import cn.com.dhcc.login.factory.LoginProcessorFactory;
import cn.com.dhcc.login.processor.LoginProcessor;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.ReportQueryStep;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.CeqConstants;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.HtmlInWatermarkStyle;
import cn.com.dhcc.query.creditquerycommon.util.InConstant;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.SpecialCharactersUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

@Controller
@RequestMapping(value = "/inquire")
public class InquireController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(InquireController.class);
	private static final String PREFIX = "inquire/";
	private static final String REVISE_PREFIX = "revise/";

//	private final static String SUCCESS_CODE = "0";
	private final static String FAILURE_CODE = "1";
//	private final static String FAILURE_MSG = "操作失败";
//	private final static String PEDDING_CODE = "02";

	private final static String charset = "UTF-8";

	@Autowired
	private CeqFlowManageService ceqFlowManageService;

	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;
	@Autowired
	private CeqReportViewReadService ceqReportViewReadService;
	@Autowired
	CeqReportLogService cpqReportLogService;

	@Autowired
	private PlantFormInteractiveService plantFormInteractiveService;

	/**
	 * <跳转 流程页面>
	 * 
	 * @return
	 */
	@RequestMapping("/flow")
	public String createPage(HttpServletRequest request) {
		return PREFIX + "flow";
	}

	/**
	 * <跳转 查询页面>
	 * 
	 * @return
	 */
	@RequestMapping("/report")
	public String createQuery(HttpServletRequest request) {
		return PREFIX + "report";
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
	 * <跳转到详情页面>
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}

	/**
	 * <跳转到审批页面>
	 * 
	 * @return
	 */
	@RequestMapping("/checkInfo")
	public String check() {
		return PREFIX + "checkInfo";
	}

	@RequestMapping("/searchlist")
	public String searchlist() {
		return PREFIX + "searchlist";
	}

	/**
	 * 跳转到信用报告打印附件页面
	 * 
	 * @return
	 */
	@RequestMapping("/printattch")
	public String printattch() {
		return PREFIX + "printattch";
	}

	/**
	 * <跳转到关联业务数据补录页面>
	 * 
	 * @return
	 */
	@RequestMapping("/revise")
	public String revise() {
		return REVISE_PREFIX + "relevancecreate";
	}

	@RequestMapping("/preValidate")
	@ResponseBody
	public String preValidate(HttpServletRequest request) {
		try {
			String userName = UserConfigUtils.getUserName(request);
			String queryOrg = UserConfigUtils.getDeptCode(request);
			// 调用流程控制服务进行预校验
			CeqReportQueryBo cpqReportQueryBo = new CeqReportQueryBo();
			WebQueryBo webQueryBo = new WebQueryBo();
			webQueryBo.setQueryNextStep(ReportQueryStep.PRECHECK);
			cpqReportQueryBo.setOperator(userName);
			webQueryBo.setQueryOrg(queryOrg);
			cpqReportQueryBo.setWebQueryBo(webQueryBo);
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(cpqReportQueryBo);
			LegalReview legalReview = new LegalReview(pageFlowControlInfo.getResCode(),
					pageFlowControlInfo.getResMsg());
			return JsonUtil.toJSONString(legalReview);
		} catch (Exception e) {
			log.error("preValidate method e = ", e);
			return JsonUtil.toJSONString(new LegalReview(Constant.CHECK_ERROR, "系统异常"));
		}
	}

	/**
	 * 查询人行
	 * 
	 * @param model
	 * @param request
	 * @param info
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showReport")
	@ResponseBody
	@LogOperation("查询人行信用报告")
	public String queryReport(Model model, HttpServletRequest request, QueryReq info, HttpServletResponse resp)
			throws Exception {
		log.info("queryReport QueryReq = {}", info);
		LegalReview lr = new LegalReview();
		try {
			CeqReportQueryBo reportQueryBo = buildCeqReportQueryBo(request, info, "QUERYREPORT");
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(reportQueryBo);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())/* 查询成功 */) {
				String queryRecordId = pageFlowControlInfo.getQueryRecordId();
				lr.setResultCode(InConstant.CODE_SUCCESS);
				lr.setResultMsg(InConstant.CODE_SUCCESS_MSG);
				lr.setUrl(PREFIX + "revealReport?recordId=" + queryRecordId);
			} else {
				lr.setResultCode(pageFlowControlInfo.getResCode());
				lr.setResultMsg(pageFlowControlInfo.getResMsg());
			}
		} catch (Exception e) {
			log.error("queryReport error:", e);
			lr.setResultCode(Constants.ERRORCODE);
			lr.setResultMsg(e.getMessage());
		}
		String JsonString = JsonUtil.toJSONString(lr);
		log.info("queryReport result = {}", JsonString);
		return JsonString;
	}

	/**
	 * 查询本地
	 * 
	 * @param model
	 * @param request
	 * @param info
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showLocalReport")
	@ResponseBody
	@LogOperation("查询本地信用报告")
	public String queryLocalReport(Model model, HttpServletRequest request, QueryReq info) throws Exception {
		log.info("showLocalReport QueryReq = {}", info);
		log.info("showLocalReport QueryReq = {}", info);
		LegalReview lr = new LegalReview();
		try {
			CeqReportQueryBo reportQueryBo = buildCeqReportQueryBo(request, info, "QUERYLOCALREPORT");
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(reportQueryBo);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())/* 查询成功 */) {
				String queryRecordId = pageFlowControlInfo.getQueryRecordId();
				lr.setResultCode(InConstant.CODE_SUCCESS);
				lr.setResultMsg(InConstant.CODE_SUCCESS_MSG);
				lr.setUrl(PREFIX + "revealReport?recordId=" + queryRecordId);
			} else {
				lr.setResultCode(pageFlowControlInfo.getResCode());
				lr.setResultMsg(pageFlowControlInfo.getResMsg());
			}
		} catch (Exception e) {
			log.error("showLocalReport error", e);
			lr.setResultCode(Constants.ERRORCODE);
			lr.setResultMsg(e.getMessage());
		}
		String JsonString = JsonUtil.toJSONString(lr);
		log.info("showLocalReport result = {}", JsonString);
		return JsonString;
	}

	/**
	 * 异步查询本地
	 * 
	 * @param model
	 * @param request
	 * @param approveBo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showLocalCreditReport")
	@ResponseBody
	@LogOperation("异步审批查询本地")
	public String asyncQueryLocalCreditReport(Model model, HttpServletRequest request, CeqApproveBo approveBo)
			throws Exception {
		log.info("asyncQueryLocalCreditReport QueryReq = {}", approveBo);
		LegalReview lr = new LegalReview();
		try {
			CeqApproveBo cpqApproveBo = ceqApproveFlowService.findCeqApproveById(approveBo.getId());
			approveBo.setSignCode(cpqApproveBo.getSignCode());
			QueryReq info = checkIngo2QueryReq(approveBo);
			CeqReportQueryBo queryreport = buildCeqReportQueryBo(request, info, "QUERYLOCALREPORT");
			queryreport.getWebQueryBo().setBatchFlag("1");
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(queryreport);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())/* 查询成功 */) {
//				CpqApproveBo cpqApproveBo = cpqApproveFlowService.findCpqApproveById(pageFlowControlInfo.getApproveRecordId());
				cpqApproveBo.setStatus(Constants.INQUIRE_SUCCESS);
				cpqApproveBo.setOperTime(new Date());
				ceqApproveFlowService.update(cpqApproveBo);
				String queryRecordId = pageFlowControlInfo.getQueryRecordId();
				lr.setResultCode(InConstant.CODE_SUCCESS);
				lr.setResultMsg(InConstant.CODE_SUCCESS_MSG);
				lr.setUrl(PREFIX + "revealReport?recordId=" + queryRecordId);
			} else {
				lr.setResultCode(InConstant.CODE_ERROR);
				lr.setResultMsg(pageFlowControlInfo.getResMsg());
			}
		} catch (Exception e) {
			log.error("asyncQueryLocalCreditReport error", e);
			lr = new LegalReview(Constants.ERRORCODE, e.getMessage());
		}
		String JsonString = JsonUtil.toJSONString(lr);
		log.info("asyncQueryLocalCreditReport result = {}", JsonString);
		return JsonString;
	}

	@RequestMapping("/revealReport")
	public ModelAndView revealReport(String creditId, String recordId, HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView();
		model.addObject("creditId", creditId);
		model.addObject("recordId", recordId);
		model.setViewName("report/reportPage");
		return model;
	}

	private static final String FUNCTION_STR = "function popReportUrl(reportUrl){";
	private static final String RETURN_STR = "function popReportUrl(reportUrl){ return;";
	
	@RequestMapping("/showCredit")
	public void showCredit(String recordId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter writer = null;
		String userName = UserUtilsForConfig.getUserName(request);
		String orgCode = UserUtilsForConfig.getDeptCode(request);
		String report = ceqFlowManageService.getReportById(recordId, userName, orgCode,
				CeqFlowManageService.REPORT_FORMAT_HTML);
		//生成信用报告水印和样式
		report = HtmlInWatermarkStyle.ceqHtmlInWatermarkStyle(report, userName, orgCode, CeqConstants.CEQ_WATERMARK_STYLE);
			
		response.setContentType("text/html;charset=utf-8");
		writer = response.getWriter();
		report = report.replace(FUNCTION_STR, RETURN_STR);
		writer.print(report);
		writer.close();
	}


	@RequestMapping("/savePrintLog")
	@ResponseBody
	public String savePrintLog(String recordId, HttpServletRequest request) throws Exception {
		ResultBeans rs = null;
		try {
			// 调用信用报告展示服务
			String userName = UserInfoUtils.getUserInfo(request).get("username");
			String orgCode = UserInfoUtils.getUserInfo(request).get("orgNo");
			rs = ceqReportViewReadService.savePrintLog(recordId, userName, orgCode);
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("savePrintLog error:", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 保存信用报告使用记录，并展示报告
	 * 
	 * @param creditId
	 * @param recordId
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return HttpServletResponse
	 */
	@RequestMapping("/saveReport")
	@ResponseBody
	public HttpServletResponse saveReport(String creditId, String recordId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("信用报告生成");
		String operator = UserConfigUtils.getUserName(request);//获取用户信息
		String orgCode = UserConfigUtils.getDeptCode(request);//获取用户机构代码
		saveOperateLog(recordId, request, Constant.save_log_type);
		String htmlPage = ceqFlowManageService.getReportById(recordId, CeqFlowManageService.REPORT_FORMAT_HTML);
		////生成信用报告水印和样式
		htmlPage = HtmlInWatermarkStyle.ceqHtmlInWatermarkStyle(htmlPage, operator, orgCode, CeqConstants.CEQ_WATERMARK_STYLE);
		
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + recordId + ".html");
		toClient.write(htmlPage.getBytes("UTF-8"));
		toClient.flush();
		toClient.close();
		return null;
	}

	/**
	 * 保存信用报告使用记录
	 * 
	 * @param recordId
	 * @param request
	 * @param type
	 */
	private void saveOperateLog(String recordId, HttpServletRequest request, String type) {
		String userName = UserConfigUtils.getUserName(request);
		String orgCode = UserConfigUtils.getDeptCode(request);
		cpqReportLogService.save(recordId, userName, orgCode, type);
	}

	@RequestMapping("/getStatus")
	@ResponseBody
	public String getStatus(String id) throws Exception {
		ResultBeans rs = null;
		try {
			CeqQueryRecordBo queryRecordBo = ceqFlowManageService.findCeqQueryRecordBoById(id);
			if (!InConstant.QUERY_FAILURE.equals(queryRecordBo.getStatus())) {
				rs = new ResultBeans(Constants.SUCCESSCODE, Constants.SUCCESSMSG);
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}

	/**
	 * 本地是否存在信用报告
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping("/isExisted")
	@ResponseBody
	public String isExistedLocalReport(QueryReq info, HttpServletRequest request) {
		log.info("isExistedLocalReport QueryReq = {} ", info);
		ResultBeans rs = null;
		try {
			String signCode = info.getSignCode();
			String queryReasonID = info.getQueryReasonID();
			String rootDeptCode = UserConfigUtils.getRootDeptCode(request);
			String localReportValidity = CeqConfigUtil.getLocalReportValidity();
			boolean haveLocalReport = ceqFlowManageService.isHaveLocalReport(signCode, queryReasonID, rootDeptCode,
					localReportValidity);
			log.debug("isExistedLocalReport  cpqFlowManageService.isHaveLocalReport result haveLocalReport = ",
					haveLocalReport);
			if (haveLocalReport) {
				rs = ResultBeans.sucessResultBean();
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			}
		} catch (Exception e) {
			log.error("isExistedLocalReport error e =  ", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		log.info("isExistedLocalReport result = {}", rs.toJSONStr());
		return rs.toJSONStr();
	}

	/**
	 * 查询流程总控制
	 * 
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping("/dispatcher")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, QueryReq info, String step) {
		log.info("dispatcher QueryReq = {} ,step = {}", info, step);
		// 整理参数
		try {
			CeqReportQueryBo cpqReportQueryBo = buildCeqReportQueryBo(request, info, step);
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(cpqReportQueryBo);
			if (null == pageFlowControlInfo) {
				return JsonUtil.toJSONString(new LegalReview(Constant.POLICY_RECHECK_E, "该次请求失败，请联系管理员！"));
			}
			LegalReview legalReview = new LegalReview(pageFlowControlInfo.getResCode(),
					pageFlowControlInfo.getResMsg());
			legalReview.setUrl(pageFlowControlInfo.getNextStepPageUrl());
			legalReview.setCheckId(pageFlowControlInfo.getApproveRecordId());
			legalReview.setCheckWay(pageFlowControlInfo.getApproveWay());
			legalReview.setBoCommonField(pageFlowControlInfo);
			return JsonUtil.toJSONString(legalReview);
		} catch (Exception e) {
			log.error("dispatcher is error e = ", e);
			return JsonUtil.toJSONString(new LegalReview(Constant.POLICY_RECHECK_E, "系统异常"));
		}
	}

	/**
	 * 异步审批查询人行
	 * 
	 * @param request
	 * @param approveBo
	 * @return
	 */
	@RequestMapping("/showCreditReport")
	@ResponseBody
	@LogOperation("异步审批查询人行")
	public String queryReportWhithRecheck(HttpServletRequest request, CeqApproveBo approveBo) {
		log.info("queryReportWhithRecheck param ceqApproveBo = ", approveBo);
		LegalReview lr = new LegalReview();
		try {
			CeqApproveBo cpqApproveBo = ceqApproveFlowService.findCeqApproveById(approveBo.getId());
			approveBo.setSignCode(cpqApproveBo.getSignCode());
			QueryReq info = checkIngo2QueryReq(approveBo);
			CeqReportQueryBo queryreport = buildCeqReportQueryBo(request, info, "QUERYREPORT");
			queryreport.getWebQueryBo().setBatchFlag("1");
			CeqPageFlowControlInfo pageFlowControlInfo = ceqFlowManageService.webQueryFlowManager(queryreport);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())) {
				// 查询成功
				cpqApproveBo.setStatus(Constants.INQUIRE_SUCCESS);
				cpqApproveBo.setOperTime(new Date());
				ceqApproveFlowService.update(cpqApproveBo);
				String queryRecordId = pageFlowControlInfo.getQueryRecordId();
				lr.setResultCode(InConstant.CODE_SUCCESS);
				lr.setResultMsg(InConstant.CODE_SUCCESS_MSG);
				lr.setUrl(PREFIX + "revealReport?recordId=" + queryRecordId);
			} else {
				lr.setResultCode(InConstant.CODE_ERROR);
				lr.setResultMsg(pageFlowControlInfo.getResMsg());
			}
		} catch (Exception e) {
			log.error("queryReportWhithRecheck error:", e);
			lr = new LegalReview(Constants.ERRORCODE, e.getMessage());
		}
		log.info("queryReportWhithRecheck result lr = {}", lr);
		return JsonUtil.toJSONString(lr);
	}

	/**
	 * 获取认证类型
	 * 
	 * @return
	 */
	@RequestMapping("/getloginType")
	@ResponseBody
	public String getLoginType(HttpServletRequest request) {
		String loginType = plantFormInteractiveService.getLoginType();
		log.info("getLoginType loginType = {} ",  loginType);
		return loginType;

	}

	/**
	 * 验证指纹
	 * 
	 * @param request
	 * @param fingerPrint
	 * @param rekUser
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/validateFinger")
	@ResponseBody
	public String validateFinger(HttpServletRequest request, String fingerPrint, String rekUser) throws Exception {
		log.info("validateFinger start ");
		String userName = UserConfigUtils.getUserName(request);
		// 判断查询员是否属于复核员的辖内机构
		SystemUser systemUser = LoginValidateUtil.findUserByUserName(rekUser);
		String rekOrg = systemUser.getOrgId();
		String queryOrg = UserConfigUtils.getDeptCode(request);
		List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(rekOrg);

		LegalReview lr = new LegalReview();
		if (null != deptCodes && !deptCodes.contains(queryOrg)) {
			lr.setResultCode(Constants.ERRORCODE);
			lr.setResultMsg("审批用户不存在,请重新输入审批用户!!");
			return JsonUtil.toJSONString(lr);
		}
		if (StringUtils.isEmpty(rekOrg)) {
			lr.setResultCode(Constants.ERRORCODE);
			lr.setResultMsg("审批用户不存在,请重新输入审批用户!!");
			return JsonUtil.toJSONString(lr);
		}
		if (Objects.equals(userName, rekUser)) {
			lr.setResultCode(FAILURE_CODE);
			lr.setResultMsg("查询审批不能是同一人!");
		} else {
			RequestBean requestBean = new RequestBean();
			requestBean.setUserName(rekUser);
			requestBean.setFinger(fingerPrint);
			requestBean.setDeptCode(rekOrg);
			String type = "FINGER";
			try {
				LoginProcessorFactory loginProcessorFactory = LoginProcessorFactory.init();
				LoginProcessor loginProcessor = loginProcessorFactory.pairProcessor(LoginType.valueOf(type));
				if (loginProcessor.validate(requestBean)) {
					lr.setResultCode(Constants.ERRORCODE);
					lr.setResultMsg(loginProcessor.getWrongMsg());
				} else {
					lr.setResultCode(Constants.SUCCESSCODE);
					lr.setResultMsg(loginProcessor.getWrongMsg());
				}
			} catch (Exception e) {
				log.error("validateFinger error:", e);
				lr.setResultCode(Constants.ERRORCODE);
				lr.setResultMsg(e.getMessage());
			}
		}
		log.info("validateFinger result = {} ", lr);
		return JsonUtil.toJSONString(lr);
	}

	/**
	 * 处理请求参数信息，构建报告查询业务对象
	 *
	 * @param request
	 * @param info
	 * @return
	 */
	private CeqReportQueryBo buildCeqReportQueryBo(HttpServletRequest request, QueryReq info, String step) {
		log.info("buildCpqReportQueryBo param  queryReq = {} , step = {}", info, step);
		//处理企业名称中含有"&"
		String enterPriseName = SpecialCharactersUtil.handlerSpecialCharacter(info.getEnterpriseName());
		info.setEnterpriseName(enterPriseName);
		CeqReportQueryBo cpqReportQueryBo = new CeqReportQueryBo();
		cpqReportQueryBo.initSerialNumber();
		CeqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		if (null == webQueryBo) {
			webQueryBo = new WebQueryBo();
		}
		ClassCloneUtil.copyObject(info, cpqReportQueryBo);
		ClassCloneUtil.copyObject(info, webQueryBo);
		log.debug("buildCpqReportQueryBo  request is null ? ", null != request);
		if (null != request) {
			String isNeedCheck = request.getParameter("isNeedCheck");
			if (!StringUtils.equals("notNeedCheck", isNeedCheck)) {
				webQueryBo.setNeedCheck(true);
			} else {
				webQueryBo.setNeedCheck(false);
			}
			String userName = UserConfigUtils.getUserName(request);
			String deptCode = UserConfigUtils.getDeptCode(request);
			String rootDeptCode = UserConfigUtils.getRootDeptCode(request);
			List<String> userRoles = UserConfigUtils.getAllRoleIds(request);
			cpqReportQueryBo.setTopOrgCode(rootDeptCode);
			cpqReportQueryBo.setOperator(userName);
			// webQueryBo.queryOrg 是查询机构 而在原queryReq中对应为operOrg.
			// queryReq中的queryOrg为金融机构代码。对应为征信用户的机构代码，改为由userRouter获取
			webQueryBo.setQueryOrg(deptCode);
			webQueryBo.setTopOrg(rootDeptCode);
			webQueryBo.setUserRoles(userRoles);
			// 获取IP地址
			String ipAddress = "";
			try {
				ipAddress = NetworkUtil.getIpAddress(request);
			} catch (IOException e) {
				log.error("dispatcher getIp error e = ", e);
			}
			webQueryBo.setClientIp(ipAddress);
		}

		String capitalize = StringUtils.upperCase(step);
		webQueryBo.setQueryNextStep(ReportQueryStep.valueOf(capitalize));
		webQueryBo.setApproveId(info.getCheckId());
		webQueryBo.setApproveWay(info.getCheckWay());
		webQueryBo.setApprovePassword(info.getRekPasword());
		webQueryBo.setApproveUser(info.getRekUser());
		webQueryBo.setApproveOrg(info.getRekOrg());
		webQueryBo.setApproveTime(info.getRekTime());
		webQueryBo.setRelationBaseData(info.getAssocbsnssData());
		webQueryBo.setBatchFlag("1");
		webQueryBo.setAuthArchiveId(info.getAutharchiveId());
		String resultType = info.getResultType();
		if (StringUtils.equals("2492639467253369856", resultType) || StringUtils.equals("H", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.HTML);
		} else if (StringUtils.equals("2492638851672153088", resultType) || StringUtils.equals("P", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.PDF);
		} else if (StringUtils.equals("X", resultType)){
			webQueryBo.setReportResultType(CpqReportResultType.XML);
		} else if(StringUtils.equals("HX", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.HX);
		}
		webQueryBo.setReportVersion(info.getQueryFormat());
		cpqReportQueryBo.setQueryReasonId(info.getQryReason());
		cpqReportQueryBo.setWebQueryBo(webQueryBo);

		log.info("buildCpqReportQueryBo  result cpqReportQueryBo = ", cpqReportQueryBo);
		return cpqReportQueryBo;
	}

	/**
	 * 审批对象转换为查询对象
	 *
	 * @param checkInfo
	 * @return
	 */
	private QueryReq checkIngo2QueryReq(CeqApproveBo checkInfo) {
		log.info("checkIngo2QueryReq checkInfo = {} ", checkInfo);
		QueryReq queryReq = new QueryReq();
		ClassCloneUtil.copyObject(checkInfo, queryReq);

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
		queryReq.setCheckId(checkInfo.getId());
		queryReq.setResultType(checkInfo.getResultType());
		log.info("checkIngo2QueryReq queryReq = {} ", queryReq);
		return queryReq;
	}
	
	
	/**
	 * 生产环境1，测试环境0
	 * @return
	 */
	@RequestMapping("/isProMode")
	@ResponseBody
	public String isProMode(){
		log.info("isProMode start!");
		String productMode = "1";
		try{
			productMode = CeqConfigUtil.getCOMMTYPEPBOC();
		}catch (Exception e){
			log.error("isProMode error e = {} ", e);
		}
		log.info("isProMode result productMode  = {} ",productMode);
		return productMode;
	}
	
	/**
	 * 1-同步
	 * 2-异步
	 * 3-同步/异步
	 * @return
	 */
	@RequestMapping("/getCheckType")
	@ResponseBody
	public String getCheckType(){
		log.info("getCheckType start!");
		String checkType = "3";
		try{
			checkType = CeqConfigUtil.getCheckType();
		}catch (Exception e){
			log.error("getCheckType error e = {} ", e);
		}
		log.info("getCheckType result checkType  = {} ",checkType);
		return checkType;
	}

}
