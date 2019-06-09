package cn.com.dhcc.creditquery.ent.queryweb.controller.checkinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.JPAParamGroup.ConjunctionType;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lekang.liu
 * @date 2018年3月8日
 */
@Slf4j
@Controller
@RequestMapping(value = "/checkinfotask")
public class CheckInfoTaskController extends BaseController {

	private static final String PREFIX = "checkinfotask/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "queryTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String USERNAME = "username";

	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;

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
	 * 分页列表 只展示当前用户已领取的审批请求
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
		Page<CeqApproveBo> queryResults = null;
		log.info("list page:" + page);
		try {
			JPAParamGroup paramGroupFinal = processRequestParamsToJPAParmGroup(page, request);
			JPAParamGroup paramGroup = null;
			JPAParamGroup rekTypeGroup = new JPAParamGroup("EQ_rekType", "1");
			JPAParamGroup orstatusGroup = new JPAParamGroup("EQ_status", Constants.SELECT_ARCHIVE);
			JPAParamGroup statusGroup = new JPAParamGroup(ConjunctionType.OR, orstatusGroup, new JPAParamGroup("EQ_status", Constants.ALREADY_RECEIVED));
			JPAParamGroup rekUserGroup = new JPAParamGroup("EQ_rekUser", getUserInfo(request, USERNAME));
			paramGroup = new JPAParamGroup(rekTypeGroup,statusGroup,rekUserGroup);
			queryResults = ceqApproveFlowService.getPage(new JPAParamGroup(paramGroupFinal, paramGroup), page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("checkinfo list method :" + e);
			queryResults = new PageImpl<>(new ArrayList<CeqApproveBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("list  result: ", jsonString);
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
		CeqApproveBo cpqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
		return JsonUtil.toJSONString(cpqApproveBo, DATE_FORMAT);
	}

	/**
	 * 审批通过（可单笔，可批量）
	 * 
	 * @param ids
	 * @return
	 */
	@LogOperation("异步审批通过")
	@RequestMapping("checkPass")
	@ResponseBody
	public String checkPass(HttpServletRequest request, @RequestBody List<String> ids) {
		ResultBeans rs;
		log.info("checkPass ids : ", ids.toString());
		try {
			List<String> errors = null;
			List<CeqApproveBo> ceqApproveBoList = null;
			String userid = getUserInfo(request, USERNAME);
			String deptCode = UserConfigUtils.getDeptCode(request);
			List<CeqApproveBo> ceqApproveBoQryList = ceqApproveFlowService.findCeqApprovesByIds(ids);
			// 判断是否必须查看档案才能审批通过
			List<String> canPassCheckStatus = new ArrayList<String>();
			canPassCheckStatus.add(Constants.SELECT_ARCHIVE);
			boolean checkMustSelectArchive = CeqConfigUtil.getCheckMustSelectArchive();
			if (!checkMustSelectArchive) {
				canPassCheckStatus.add(Constants.ALREADY_RECEIVED);
			}

			for (CeqApproveBo ceqApproveBo : ceqApproveBoQryList) {

				if (!canPassCheckStatus.contains(ceqApproveBo.getStatus()) || !ceqApproveBo.getRekUser().equals(userid)) {
					errors = new ArrayList<String>();
					errors.add(ceqApproveBo.getEnterpriseName());
				}
			}
			if (null == errors) {
				try {
					ceqApproveBoList = new ArrayList<CeqApproveBo>();
					for (CeqApproveBo ceqApproveBo : ceqApproveBoQryList) {
						ceqApproveBo.setStatus(Constants.CHECK_PASS);
						ceqApproveBo.setOperTime(new Date());
						ceqApproveBo.setRekOrg(deptCode);
						ceqApproveBo.setRekTime(new Date());
						ceqApproveBoList.add(ceqApproveBo);
					}
					ceqApproveFlowService.batchUpdate(ceqApproveBoList);

	/*				Iterator<CpqCheckInfo> iterator = checkInfo.iterator();
					while (iterator.hasNext()) {
						CpqCheckInfo checkInfo4query = (CpqCheckInfo) iterator.next();
						// 调用查询线程进行查询
						SingleQuery4CheckinfoTask query4CheckinfoTask = (SingleQuery4CheckinfoTask) getBean("singleQuery4CheckinfoTask");
						query4CheckinfoTask.setCpqCheckInfo(checkInfo4query);
						threadPoolTaskExecutor.submit(query4CheckinfoTask);
					}*/
					rs = ResultBeans.sucessResultBean();
				} catch (Exception e) {
					log.error("checkPass : ", e);
					// README
					// 根据需求（监管需求V1.4版，SR0404业务规则），审批通过出现异常时，需将该审批请求重置为待审批状态。故在此进行一次手动回滚
					for (CeqApproveBo ceqApproveBo : ceqApproveBoQryList) {
						ceqApproveBoList = new ArrayList<CeqApproveBo>();
						ceqApproveBo.setStatus(Constants.WAIT_CHECK);
						ceqApproveBo.setRekUser("");
						ceqApproveBo.setOperTime(new Date());
						ceqApproveBoList.add(ceqApproveBo);
					}
					ceqApproveFlowService.batchUpdate(ceqApproveBoList);
					rs = new ResultBeans(Constants.ERRORCODE, "审批过程中发生问题，审批请求已重置为待审批状态；请重新领取任务进行审批操作。");
				}
			} else {
				String message = checkMustSelectArchive ? "已查看档案" : "已查看档案或已领取";
				rs = new ResultBeans(Constants.ERRORCODE, "客户名称为" + errors.toString() + "的请求不可进行审批通过操作。可进行审批通过的记录应为" + message + "状态。");
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("checkPass method ", e);
		}
		log.info("receiveTask result : ", rs);
		return rs.toJSONStr();
	}

	/**
	 * 
	 * 审批拒绝
	 * 
	 * @param id
	 * @param refuseReasons
	 * @return
	 */
	@LogOperation("异步审批拒绝")
	@RequestMapping("/checkRefusal")
	@ResponseBody
	public String checkRefusal(String id, String refuseReasons) {
		ResultBeans rs;
		try {
			if (StringUtils.isNotBlank(refuseReasons)) {
				CeqApproveBo ceqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
				ceqApproveBo.setRefuseReasons(refuseReasons);
				ceqApproveBo.setOperTime(new Date());
				ceqApproveBo.setStatus(Constants.CHECK_REFUSAL);
				ceqApproveFlowService.update(ceqApproveBo);
				rs = ResultBeans.sucessResultBean();
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "请填写拒绝原因！");
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("checkRefusal :", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 
	 * 退回审批任务
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/backTask")
	@ResponseBody
	public String backTask(String id) {
		ResultBeans rs;
		try {
			CeqApproveBo ceqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
			ceqApproveBo.setStatus(Constants.WAIT_CHECK);
			ceqApproveBo.setOperTime(new Date());
			ceqApproveBo.setRekUser("");
			ceqApproveFlowService.update(ceqApproveBo);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("backTask" , e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 
	 * 查看授权档案时，修改记录状态为档案已查看。
	 * 
	 * @param checkId
	 * @return
	 */
	@RequestMapping("/selectArchive")
	@ResponseBody
	public String selectArchive(String id) {
		ResultBeans rs;
		try {
			CeqApproveBo ceqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
			ceqApproveBo.setStatus(Constants.SELECT_ARCHIVE);
			ceqApproveFlowService.update(ceqApproveBo);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("selectArchive", e);
		}
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
}
