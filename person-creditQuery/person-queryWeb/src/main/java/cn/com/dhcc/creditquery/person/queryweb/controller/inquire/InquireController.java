package cn.com.dhcc.creditquery.person.queryweb.controller.inquire;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqPageFlowControlInfo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo.WebQueryBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.vo.LegalReview;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryReq;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportLogService;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewReadService;
import cn.com.dhcc.creditquery.person.reportview.util.ResultBeans;
import cn.com.dhcc.login.bean.LoginType;
import cn.com.dhcc.login.bean.RequestBean;
import cn.com.dhcc.login.factory.LoginProcessorFactory;
import cn.com.dhcc.login.processor.LoginProcessor;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.ReportQueryStep;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.HtmlInWatermarkStyle;
import cn.com.dhcc.query.creditquerycommon.util.InConstant;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

/**
 * 查询信用报告
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/inquire")
public class InquireController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(InquireController.class);
	private static final String PREFIX = "inquire/";
	private static final String REVISE_PREFIX = "revise/";

	private final static String FAILURE_CODE = "1";
	private final static String charset = "UTF-8";

	@Autowired
	private CpqFlowManageService cpqFlowManageService;

	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
	@Autowired
	private CpqReportViewReadService cpqReportViewReadService;
	@Autowired
	CpqReportLogService cpqReportLogService;

	@Autowired
	private PlantFormInteractiveService plantFormInteractiveService;

	/**
	 * <跳转主流程页面>
	 *
	 * @return
	 */
	@RequestMapping("/flow")
	public String createPage(HttpServletRequest request) {
		return PREFIX + "flow";
	}

	/**
	 * <跳转查询信息录入页面>
	 *
	 * @return
	 */
	@RequestMapping("/report")
	public String createQuery(HttpServletRequest request) {
		return PREFIX + "report";
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

	/**
	 * @return String
	 */
	@RequestMapping("/searchlist")
	public String searchlist() {
		return PREFIX + "searchlist";
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

	/**
	 * 查询前预校验
	 *
	 * @param request
	 * @return String
	 */
	@RequestMapping("/preValidate")
	@ResponseBody
	public String preValidate(HttpServletRequest request) {
		try {
			String userName = UserConfigUtils.getUserName(request);
			String queryOrg = UserConfigUtils.getDeptCode(request);
			// 调用流程控制服务进行预校验
			CpqReportQueryBo cpqReportQueryBo = new CpqReportQueryBo();
			WebQueryBo webQueryBo = new WebQueryBo();
			webQueryBo.setQueryNextStep(ReportQueryStep.PRECHECK);
			cpqReportQueryBo.setOperator(userName);
			webQueryBo.setQueryOrg(queryOrg);
			cpqReportQueryBo.setWebQueryBo(webQueryBo);
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(cpqReportQueryBo);
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
			CpqReportQueryBo reportQueryBo = buildCpqReportQueryBo(request, info, "QUERYREPORT");
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(reportQueryBo);
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
		LegalReview lr = new LegalReview();
		try {
			CpqReportQueryBo reportQueryBo = buildCpqReportQueryBo(request, info, "QUERYLOCALREPORT");
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(reportQueryBo);
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
	 * @param approveBo {@link CpqApproveBo}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showLocalCreditReport")
	@ResponseBody
	@LogOperation("异步审批查询本地")
	public String asyncQueryLocalCreditReport(Model model, HttpServletRequest request, CpqApproveBo approveBo)
			throws Exception {
		log.info("asyncQueryLocalCreditReport QueryReq = {}", approveBo);
		LegalReview lr = new LegalReview();
		try {
			CpqApproveBo cpqApproveBo = cpqApproveFlowService.findCpqApproveById(approveBo.getId());
			approveBo.setCertNo(cpqApproveBo.getCertNo());
			QueryReq info = checkIngo2QueryReq(approveBo);
			CpqReportQueryBo queryreport = buildCpqReportQueryBo(request, info, "QUERYLOCALREPORT");
			queryreport.getWebQueryBo().setBatchFlag("1");
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(queryreport);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())/* 查询成功 */) {
//				CpqApproveBo cpqApproveBo = cpqApproveFlowService.findCpqApproveById(pageFlowControlInfo.getApproveRecordId());
				cpqApproveBo.setStatus(Constants.INQUIRE_SUCCESS);
				cpqApproveBo.setOperTime(new Date());
				cpqApproveFlowService.update(cpqApproveBo);
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

	/**
	 * 展示报告
	 * 
	 * @param recordId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/showCredit")
	public void showCredit(String recordId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter writer = null;
		String userName = UserUtilsForConfig.getUserName(request);
		String orgCode = UserUtilsForConfig.getDeptCode(request);
		String report = cpqFlowManageService.getReportById(recordId, userName, orgCode,CpqFlowManageService.REPORT_FORMAT_HTML);
		//生成信用报告水印和样式
		report = HtmlInWatermarkStyle.cpqHtmlInWatermarkStyle(report, userName, orgCode, 
				cn.com.dhcc.query.creditquerycommon.util.Constants.CPQ_WATERMARK_STYLE,"isWeb");
		response.setContentType("text/html;charset=utf-8");
		writer = response.getWriter();
		writer.print(report);
		writer.close();
	}

	/**
	 * 保存信用报告打印记录
	 *
	 * @param recordId
	 * @param request
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/savePrintLog")
	@ResponseBody
	public String savePrintLog(String recordId, HttpServletRequest request) throws Exception {
		ResultBeans rs = null;
		try {
			// 调用信用报告展示服务
			String userName = UserInfoUtils.getUserInfo(request).get("username");
			String orgCode = UserInfoUtils.getUserInfo(request).get("orgNo");
			rs = cpqReportViewReadService.savePrintLog(recordId, userName, orgCode);
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("savePrintLog error:", e);
		}
		return rs.toJSONStr();

	}


	/**
	 * 保存信用报告到客户电脑磁盘
	 *
	 * @param creditId
	 * @param recordId
	 * @param request
	 * @param response
	 * @return HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping("/saveReport")
	@ResponseBody
	public HttpServletResponse saveReport(String creditId, String recordId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("信用报告生成");
		String operator = UserConfigUtils.getUserName(request);//获取用户信息
		String orgCode = UserConfigUtils.getDeptCode(request);//获取用户机构代码
		saveOperateLog(recordId, request, Constant.save_log_type);
		String htmlPage = cpqFlowManageService.getReportById(recordId,CpqFlowManageService.REPORT_FORMAT_HTML);
		//生成信用报告水印和样式
		htmlPage = HtmlInWatermarkStyle.cpqHtmlInWatermarkStyle(htmlPage, operator, orgCode, 
				cn.com.dhcc.query.creditquerycommon.util.Constants.CPQ_WATERMARK_STYLE);
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
			CpqQueryRecordBo queryRecordBo = cpqFlowManageService.findCpqQueryRecordBoById(id);
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
			String clientName = info.getClientName();
			String certType = info.getCertType();
			String certNo = info.getCertNo();
			String queryReasonID = info.getQueryReasonID();
			String rootDeptCode = UserConfigUtils.getRootDeptCode(request);
			String localReportValidity = ConfigUtil.getLocalReportValidity();
			boolean haveLocalReport = cpqFlowManageService.isHaveLocalReport(clientName, certType, certNo,
					queryReasonID, rootDeptCode,localReportValidity);
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
		log.info("dispatcher QueryReq = {} ", info);
		// 整理参数
		try {
			CpqReportQueryBo cpqReportQueryBo = buildCpqReportQueryBo(request, info, step);
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(cpqReportQueryBo);
			if(null == pageFlowControlInfo) {
				return JsonUtil.toJSONString(new LegalReview(Constant.POLICY_RECHECK_E, "该次请求失败，请联系管理员！"));
			}
			LegalReview legalReview = new LegalReview(pageFlowControlInfo.getResCode(),pageFlowControlInfo.getResMsg());
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
	 * 处理请求参数信息，构建报告查询业务对象
	 *
	 * @param request
	 * @param info
	 * @return
	 */
	private CpqReportQueryBo buildCpqReportQueryBo(HttpServletRequest request, QueryReq info, String step) {
		log.info("buildCpqReportQueryBo param  queryReq = {} , step = {}", info, step);
		CpqReportQueryBo cpqReportQueryBo = new CpqReportQueryBo();
		cpqReportQueryBo.initSerialNumber();
		CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		if (null == webQueryBo) {
			webQueryBo = new WebQueryBo();
		}
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
		if(StringUtils.equals("2492639468071259136", resultType) || StringUtils.equals("H", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.HTML);
		} else if(StringUtils.equals("2492639468054481920", resultType) || StringUtils.equals("P", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.PDF);
		} else if (StringUtils.equals("X", resultType)){
			webQueryBo.setReportResultType(CpqReportResultType.XML);
		} else if(StringUtils.equals("HX", resultType)) {
			webQueryBo.setReportResultType(CpqReportResultType.HX);
		}
		//TODO  重构
		webQueryBo.setReportVersion(info.getQueryFormat());
		cpqReportQueryBo.setCertNo(info.getCertNo());
		cpqReportQueryBo.setClientName(info.getClientName());
		cpqReportQueryBo.setCertType(info.getCertType());
		cpqReportQueryBo.setQueryReasonId(info.getQryReason());
		cpqReportQueryBo.setWebQueryBo(webQueryBo);
		
		log.info("buildCpqReportQueryBo  result cpqReportQueryBo = ", cpqReportQueryBo);
		return cpqReportQueryBo;
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
	public String queryReportWhithRecheck(HttpServletRequest request, CpqApproveBo approveBo) {
		log.info("queryReportWhithRecheck param cpqApproveBo = ", approveBo);
		LegalReview lr = new LegalReview();
		try {
			CpqApproveBo cpqApproveBo = cpqApproveFlowService.findCpqApproveById(approveBo.getId());
			approveBo.setCertNo(cpqApproveBo.getCertNo());
			QueryReq info = checkIngo2QueryReq(approveBo);
			CpqReportQueryBo queryreport = buildCpqReportQueryBo(request, info, "QUERYREPORT");
			queryreport.getWebQueryBo().setBatchFlag("1");
			CpqPageFlowControlInfo pageFlowControlInfo = cpqFlowManageService.webQueryFlowManager(queryreport);
			if (StringUtils.equals(Constant.CHECK_SUCCESS, pageFlowControlInfo.getResCode())) {
				//查询成功
				cpqApproveBo.setStatus(Constants.INQUIRE_SUCCESS);
				cpqApproveBo.setOperTime(new Date());
				cpqApproveFlowService.update(cpqApproveBo);
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
	 * 审批对象转换为查询对象
	 *
	 * @param checkInfo
	 * @return
	 */
	private QueryReq checkIngo2QueryReq(CpqApproveBo checkInfo) {
		log.info("checkIngo2QueryReq checkInfo = {} ", checkInfo);
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
		queryReq.setCheckId(checkInfo.getId());
		queryReq.setResultType(checkInfo.getResultType());
		log.info("checkIngo2QueryReq queryReq = {} ", queryReq);
		return queryReq;
	}

	@RequestMapping("/getloginType")
	@ResponseBody
	public String getLoginType() {
		String loginType = plantFormInteractiveService.getLoginType();
		log.info("getloginType result = {}", loginType);
		return loginType;

	}

	/**
	 * 验证用户指纹
	 * @param request
	 * @param fingerPrint
	 * @param rekUser
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/validateFinger")
	@ResponseBody
	public String validateFinger(HttpServletRequest request, String fingerPrint, String rekUser) throws Exception {
		log.info("validateFinger fingerPrint = {}", fingerPrint);
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
			checkType =ConfigUtil.getCheckType();
		}catch (Exception e){
			log.error("getCheckType error e = {} ", e);
		}
		log.info("getCheckType result checkType  = {} ",checkType);
		return checkType;
	}
	
}
