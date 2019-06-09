package cn.com.dhcc.creditquery.ent.queryweb.controller.checkinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.JPAParamGroup.ConjunctionType;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.DicServiceImpl;
import cn.com.dhcc.creditquery.ent.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.Excel;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lekang.liu
 * @date 2018年3月8日
 */
@Slf4j
@Controller
@RequestMapping(value = "/checkinfolog")
public class CheckInfoLogController extends BaseController {

	private static final String PREFIX = "checkinfolog/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "queryTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String USERNAME = "username";

	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;
	@Autowired
	private DicServiceImpl dicService;

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
	 * 只展示当前用户辖内提交的审批记录(包括审批通过、审批拒绝；包括同步审批与异步审批)
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
		try {
			JPAParamGroup paramGroupFinal = processRequestParamsToJPAParmGroup(page, request);

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
			
			
			JPAParamGroup statusGroup = new JPAParamGroup("EQ_status", Constants.CHECK_PASS);
			JPAParamGroup orStatusGroup = new JPAParamGroup("EQ_status", Constants.CHECK_REFUSAL);
			JPAParamGroup oraStatusGroup = new JPAParamGroup("EQ_status", Constants.INQUIRE_SUCCESS);
			JPAParamGroup paramGroup = new JPAParamGroup(ConjunctionType.OR, statusGroup, orStatusGroup,oraStatusGroup);
			
			JPAParamGroup group = null;
			if(null != deptCodeGroup){
				group = new JPAParamGroup(deptCodeGroup,paramGroup);
			}else{
				group = paramGroup;
			}
			
			queryResults = ceqApproveFlowService.getPage(new JPAParamGroup(paramGroupFinal, group), page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("checkinfo list method :" + e);
			queryResults = new PageImpl<>(new ArrayList<CeqApproveBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
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
		log.info("findCpqQueryRecordBoById method id = {}", id);
		CeqApproveBo ceqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
		log.info("findCeqQueryRecordBoById method checkInfo ={} ", ceqApproveBo);
		QueryEncryptUtil.QueryEncrypt(ceqApproveBo);
		return JsonUtil.toJSONString(ceqApproveBo, DATE_FORMAT);
	}

	@RequestMapping("/reSubCheckTask")
	@ResponseBody
	public String reSubCheckTask(HttpServletRequest request, String id) {
		log.info("reSubCheckTask method id", id);
		ResultBeans rs = null;
		try {
			CeqApproveBo ceqApproveBo = ceqApproveFlowService.findCeqApproveById(id);
			if (Objects.equals(Constants.CHECK_REFUSAL, ceqApproveBo.getStatus())) {
				if (Objects.equals(getUserInfo(request, USERNAME), ceqApproveBo.getOperator())) {
					ceqApproveBo.setStatus(Constants.WAIT_CHECK);
					ceqApproveBo.setRefuseReasons("");
					ceqApproveBo.setRekUser("");
					ceqApproveFlowService.update(ceqApproveBo);
					rs = ResultBeans.sucessResultBean();
				} else {
					rs = new ResultBeans(Constants.ERRORCODE, "不可重新提交他人的审批请求");
				}
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "只可重新提交审批拒绝的请求");
			}

		} catch (Exception e) {
			log.error("reSubCheckTask method ", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
	
	@RequestMapping("/exportExcel")
	@ResponseBody
	public HttpServletResponse exportExcel(String ids, HttpServletRequest request, HttpServletResponse response) {
		List<CeqApproveBo> queryResults = null;
//		String filePath = cpqConfigService.getConfigMaxValueByName(ConfigConstants.TEMP_PATH);
		String filePath = CeqConfigUtil.getTempPath();//临时目录
		try {
			if (StringUtils.isEmpty(ids)) {
				Map<String, Object> searchParams = processRequestParams(new PageBean(), request);
				queryResults = ceqApproveFlowService.getCeqApprovesBySearchParams(searchParams);
			} else {
				queryResults = ceqApproveFlowService.findCeqApprovesByIds(Arrays.asList(ids.split("\\,")));
			}
			String orgId = getUserInfo(request, "orgId");
			Excel excel = ExcelUtil.initExcel(dicService , CeqApproveBo.class);
			excel.write(queryResults, CeqApproveBo.class, filePath, "CpqCheckInfo.xls", true, true,orgId);
			
		} catch (Exception e) {
			log.error("exportExcel:" + e.getMessage());
		}
		File file = new File(filePath + File.separator + "CpqCheckInfo.xls");
		return FileUtil.download(file, response);
	}

}
