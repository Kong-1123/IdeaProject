package cn.com.dhcc.creditquery.ent.queryweb.controller;

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

import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo.Menu;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.alert.CeqAlertService;
import cn.com.dhcc.creditquery.ent.querycontrol.service.exceptionrule.CeqExceptionRuleService;
import cn.com.dhcc.creditquery.ent.querycounter.service.CeqAlertCounterService;
import cn.com.dhcc.creditquery.ent.querycounter.service.CeqMonitorCounterService;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

@Controller
public class IndexController extends BaseController {
	@Autowired
	private CeqMonitorCounterService ceqMonitorCounterService;
	@Autowired
	private CeqAlertCounterService alertCountService;
	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	@Autowired
	private CeqApproveFlowService ceqApproveFlowService;
	@Autowired
	private CeqExceptionRuleService ceqExceptionRuleService;
	@Autowired
	private CeqAlertService ceqAlertService;
	/**
	 * 待审批任务  样式名
	 */
	private static final String CHECKINFOTASK_MENU_STYLE = "DHC-gerenfuheguanli";
	/**
	 * 可领取任务 样式名
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
		String userName = UserUtilsForConfig.getUserName(request);
		List<String> menuIds = UserUtilsForConfig.getUserMenuIds(request);
		List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
		String topOrgCode = UserConfigUtils.getTopOrgCode();
		String maxQuery = ceqExceptionRuleService.findByOrgCodeAndRuleCode(topOrgCode, "C0001").getRuleValue1();
		
		CeqShortcutBo queryNum = ceqMonitorCounterService.getQueryNumByDB(userName, menuIds,maxQuery);//查询量
		CeqShortcutBo archiveNum = ceqFlowManageService.getArchiveShortcut(userName, deptCodes, menuIds);//授权补录
		CeqShortcutBo checkInfo = ceqApproveFlowService.getApproveShortCut(userName, menuIds, deptCodes);//审批任务
		//预警管理
		Integer timeCount = ceqAlertService.countByAlertReason(ALERT_TIME, deptCodes);//非工作时间预警数量
		Integer quantityCount = ceqAlertService.countByAlertReason(ALERT_QUANTITY, deptCodes);//单日查询超量查询量
		CeqShortcutBo alertCount = alertCountService.getAlertCountForBench(userName, menuIds, deptCodes,timeCount,quantityCount);//预警管理
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
	private void addTextMenu(JSONArray down,CeqShortcutBo ceqShortcutBo){
		if(ceqShortcutBo != null && ceqShortcutBo.getMenus() != null && ceqShortcutBo.getMenus().size() != 0)
			down.add(ceqShortcutBo);
	}
	private JSONArray buildShortcutWithPicture(CeqShortcutBo ceqShortcutBo){
		JSONArray withPic = new JSONArray();
		if(ceqShortcutBo == null || CollectionUtils.isEmpty(ceqShortcutBo.getMenus())){
			return withPic;
		}
		for (Menu menu : ceqShortcutBo.getMenus()) {
			Menu m = new Menu();
			withPic.add(m);
			try {
				BeanUtils.copyProperties(m, menu);
			} catch (Exception e) {}
			if(StringUtils.endsWith(menu.getLink(), "checkinfotask/list")){
				m.setStyle(CHECKINFOTASK_MENU_STYLE);
				m.setName("待审批任务");
			}else if(StringUtils.endsWith(menu.getLink(), "checkinfo/list")){
				m.setStyle(CHECKINFO_MENU_STYLE);
				m.setName("可领取任务");
			}else{
				withPic.remove(withPic.size() - 1);
			}
		}
		return withPic;
	}
}
