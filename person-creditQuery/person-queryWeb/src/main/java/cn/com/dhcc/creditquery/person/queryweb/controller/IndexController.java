package cn.com.dhcc.creditquery.person.queryweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo.Menu;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.querycontrol.service.alert.CpqAlertService;
import cn.com.dhcc.creditquery.person.querycontrol.service.exceptionrule.CpqExceptionRuleService;
import cn.com.dhcc.creditquery.person.querycounter.service.CpqAlertCounterService;
import cn.com.dhcc.creditquery.person.querycounter.service.CpqMonitorCounterService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController extends BaseController {
	@Autowired
	private CpqMonitorCounterService cpqMonitorCounterService;
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
	@Autowired
	private CpqAlertCounterService cpqAlertCounterService;
//	@Autowired
//	private CpqUserAttrService cpqUserAttrService;
	@Autowired
	private CpqAlertService cpqAlertService;
	@Autowired
	private CpqExceptionRuleService cpqExceptionRuleService;
	
	/**
	 * 待审批任务  样式名
	 */
	private static final String CHECKINFOTASK_MENU_STYLE = "DHC-gerenfuheguanli";
	/**
	 * 可领取任务  样式名
	 */
	private static final String CHECKINFO_MENU_STYLE = "DHC-lingqufuherenwu";
	
	/**
	 * 预警原因 1---单日查询超量 2---非工作时间查询
	 */
	private final static String ALERT_QUANTITY = "1";
	private final static String ALERT_TIME = "2";
	
	/**
	 * 首页快捷入口
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserShortcut")
	@ResponseBody
	public String getUserShortcut(HttpServletRequest request){
		log.info("IndexController method : getUserShortcut request begin");
		String userName = UserConfigUtils.getUserName(request);
		List<String> menuIds = UserConfigUtils.getUserMenuIds(request);
		List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
		String topOrgCode = UserConfigUtils.getTopOrgCode();
		String maxQuery = cpqExceptionRuleService.findByOrgCodeAndRuleCode(topOrgCode, "C0001").getRuleValue1();
//		CpqUserAttrBo cpqUserAttrBo = cpqUserAttrService.findByUserId(userName);
		CpqShortcutBo queryNum = cpqMonitorCounterService.getQueryNumByDB(userName, menuIds, deptCodes,maxQuery);//查询量
		CpqShortcutBo archiveNum = cpqFlowManageService.getArchiveShortcut(userName, deptCodes, menuIds);//授权补录
		CpqShortcutBo checkInfo = cpqApproveFlowService.getApproveShortCut(userName, menuIds, deptCodes);//审批任务
		//预警管理
		Integer timeCount = cpqAlertService.countByAlertReason(ALERT_TIME, deptCodes);//非工作时间预警数量
		Integer quantityCount = cpqAlertService.countByAlertReason(ALERT_QUANTITY, deptCodes);//单日查询超量查询量
		CpqShortcutBo alertCount = cpqAlertCounterService.getAlertCountForBench(userName, menuIds, deptCodes,timeCount,quantityCount);
		JSONArray down = new JSONArray();
		addTextMenu(down, archiveNum);
		addTextMenu(down, checkInfo);
		addTextMenu(down, alertCount);
		addTextMenu(down, queryNum);
		JSONArray up = buildShortcutWithPicture(checkInfo);
		JSONObject result = new JSONObject();
		result.put("cardMenu", up);
		result.put("textMenu", down);
		return result.toJSONString();
	}
	private void addTextMenu(JSONArray down,CpqShortcutBo cpqShortcutBo){
		if(cpqShortcutBo != null && cpqShortcutBo.getMenus() != null && cpqShortcutBo.getMenus().size() != 0)
			down.add(cpqShortcutBo);
	}
	
	private JSONArray buildShortcutWithPicture(CpqShortcutBo checkInfo){
		JSONArray withPic = new JSONArray();
		if(checkInfo == null || CollectionUtils.isEmpty(checkInfo.getMenus())){
			return withPic;
		}
		for (Menu menu : checkInfo.getMenus()) {
			Menu m = new Menu();
			withPic.add(m);
			try {
				BeanUtils.copyProperties(m, menu);
			} catch (Exception e) {}
			if(StringUtils.endsWith(menu.getLink(), "checkinfotask-list")){
				m.setStyle(CHECKINFOTASK_MENU_STYLE);
				m.setName("待审批任务");
			}else if(StringUtils.endsWith(menu.getLink(), "checkinfo-list")){
				m.setStyle(CHECKINFO_MENU_STYLE);
				m.setName("可领取任务");
			}else{
				withPic.remove(withPic.size() - 1);
			}
		}
		return withPic;
	}
}
