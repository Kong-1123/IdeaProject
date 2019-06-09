package cn.com.dhcc.creditquery.ent.queryweb.controller.monitorinter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.creditquery.ent.businessmonitor.service.CeqBusinessMonitorService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.ent.queryweb.vo.MonitorInterVo;
import cn.com.dhcc.creditquery.ent.queryweb.vo.OneDayQueryVo;
import cn.com.dhcc.creditquery.ent.queryweb.vo.QueryVolumResultVo;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;

/**
 * This calss is the Controller class of the ceq_monitorinter 
 * 
 * @author drj
 *
 */
@Controller
@RequestMapping("/monitorinter")
public class MonitorInterController extends BaseController {
	private static final String PREFIX = "monitorinter/";
	private static Logger log = LoggerFactory.getLogger(MonitorInterController.class);
	@Autowired
	private CeqBusinessMonitorService monitorInterService;
	private static String ERRO_MSG="对不起！您没有该权限！";

	/**
	 * Get system configuration parameters:Maxinum;Time interval
	 * @return
	 */
	@RequestMapping("/getQueryConfigParam")
	@ResponseBody
	public MonitorInterVo getQueryConfigParam() {
		log.info("MonitorinterController getQueryConfigParam method start......");
		String maxNum = CeqConfigUtil.getQueryCountMaxNum();
		String time = CeqConfigUtil.getTimeInterval();
		log.info("MonitorinterController getQueryConfigParam method maxNum:"+maxNum+"---time:"+time);
		Map<String, String> param = new HashMap<String, String>();
		param.put("time", time);
		param.put("maxNum", maxNum);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setParam(param);
		log.info("MonitorinterController getQueryConfigParam method end......" + vo);
		return vo;
	}

	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "monitorinter";
	}

	/**
	 * 判断是否是顶级机构显示该页面与否
	 * @param request
	 * @return
	 */
	@RequestMapping("/istopcode")
	@ResponseBody
	public String isTopCode(HttpServletRequest request) {
		log.info("MonitorinterController istopcode method start......");
		ResultBeans rs = null;
		try {
			// 获取当前用户的机构码
			String orgCode = UserConfigUtils.getDeptCode(request);
			// 获取当前用户的顶级机构
			String topOrg = UserConfigUtils.getRootDeptCode(orgCode);
			log.info("MonitorinterController istopcode method orgCode:"+orgCode+"---topOrg:"+topOrg);
			if (StringUtils.equals(orgCode, topOrg)) {
				rs = new ResultBeans(Constants.SUCCESSCODE, Constants.SUCCESSMSG);
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, ERRO_MSG);
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error("MonitorinterController istopcode method", e.getMessage());
		}
		log.info("MonitorinterController istopcode method end......" + rs.toJSONStr());
		return rs.toJSONStr();
	}

	/**
	 * 单位时间内查询量仪表盘
	 * 
	 * @return
	 */
	@RequestMapping("/realTimeQueryGauge")
	@ResponseBody
	public MonitorInterVo realTimeQueryGauge(HttpServletRequest request) {
		log.info("MonitorinterController realTimeQueryGauge method start ......");
		// 获取当前用户的机构代码
		MonitorInterVo vo = new MonitorInterVo();
		try {
			String orgCode = UserConfigUtils.getDeptCode(request);
			Long time = Long.parseLong(CeqConfigUtil.getTimeInterval());
			log.info("MonitorinterController realTimeQueryGauge method orgCode:"+orgCode+"---time:"+time);
			Integer realTimeQuery = monitorInterService.getRealTimeQuery(orgCode, time);
			QueryVolumResultVo valueVo = new QueryVolumResultVo(realTimeQuery);
			valueVo.setValue(realTimeQuery);
			vo.setRealTimeQuery(valueVo);
		} catch (Exception e) {
			log.error("MonitorinterController realTimeQueryGauge method", e.getMessage());
		}
		log.info("MonitorinterController realTimeQueryGauge method end ......", vo);
		return vo;
	}

	/**
	 * 当天查询总量
	 * 
	 * @return
	 */
	@RequestMapping("/allLineOfDayPie")
	@ResponseBody
	public MonitorInterVo allLineOfDayPie(HttpServletRequest request) {
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController allLineOfDayPie method start......orgCode:"+orgCode);
		Integer allLineOfDay = monitorInterService.getAllLineOfDay(orgCode);
		QueryVolumResultVo valueVo = new QueryVolumResultVo(allLineOfDay);
		valueVo.setValue(allLineOfDay);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setAllLineOfDay(valueVo);
		log.info("MonitorinterController allLineOfDayPie method end......",vo);
		return vo;

	}

	/**
	 * 全行当天查询状态图
	 * 
	 * @return
	 */
	@RequestMapping("/queryStatePie")
	@ResponseBody
	public MonitorInterVo queryStatePie(HttpServletRequest request) {
		log.info("MonitorinterController queryStatePie method start......");
		// 获取当前用户机构代码
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController queryStatePie method orgCode:"+orgCode);
		Map<Integer, Integer> queryState = monitorInterService.getQueryState(orgCode);
		MonitorInterVo vo = new MonitorInterVo();

		HashMap<Integer, Integer> jsonMap = new HashMap<Integer, Integer>();
		jsonMap.put(1, queryState.get(1));
		jsonMap.put(2, queryState.get(2));
		jsonMap.put(3, queryState.get(3));

		vo.setQueryState(queryState);
		log.info("MonitorinterController queryStatePie method end......",vo);
		return vo;
	}

	/**
	 * 24小时查询量 传入当前机构
	 * 
	 * @return
	 */
	@RequestMapping("/oneDayQueryVoluLine")
	@ResponseBody
	public MonitorInterVo oneDayQueryVoluLine(HttpServletRequest request) {
		log.info("MonitorinterController oneDayQueryVoluLine method start......");
		MonitorInterVo vo = new MonitorInterVo();
		try {
			// 获取当前用户机构代码
			String orgCode = UserConfigUtils.getDeptCode(request);
			log.info("MonitorinterController oneDayQueryVoluLine method orgCode:"+orgCode);
			ArrayList<Entry<String, Map<String, Integer>>> oneDayQueryVoluLine = monitorInterService.oneDayQueryVoluLine(orgCode);
			// 此时 arrayList 中的key YYYYMMDD HH 时间字符串 格式化为 HH arrayList里的数据
			// 转换成需要返回的json格式 return出去
			List<String> dateList = new ArrayList<>();
			List<Integer> seriesListof1 = new ArrayList<>();
			List<Integer> seriesListof2 = new ArrayList<>();
			List<Integer> seriesListof3 = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

			for (int i = 0; i < oneDayQueryVoluLine.size(); i++) {
				Entry<String, Map<String, Integer>> entry = oneDayQueryVoluLine.get(i);
				Date date = sdf.parse(entry.getKey());
				String hour = String.valueOf(date.getHours());
				dateList.add(hour);
				Map<String, Integer> value = entry.getValue();
				Integer integerOf1 = value.get("1");
				seriesListof1.add(integerOf1);
				Integer integerOf2 = value.get("2");
				seriesListof2.add(integerOf2);
				Integer integerOf3 = value.get("3");
				seriesListof3.add(integerOf3);
			}

			ArrayList<Map<String, List<Integer>>> seriesList = new ArrayList<>();

			Map<String, List<Integer>> seriesMap = new HashMap<>();
			seriesMap.put("1", seriesListof1);
			seriesMap.put("2", seriesListof2);
			seriesMap.put("3", seriesListof3);
			seriesList.add(seriesMap);

			OneDayQueryVo oneDayQuery = new OneDayQueryVo(dateList, seriesList);
			vo.setOneDayQuery(oneDayQuery);
		} catch (Exception e) {
			log.error("monitorinter oneDayQueryVoluLine  method :" + e.getMessage());
		}
		log.info("MonitorinterController oneDayQueryVoluLine method end......",vo);
		return vo;
	}

	/**
	 * 近一周查询量
	 * 
	 * @return
	 */
	@RequestMapping("/oneWeekQueryVoluLine")
	@ResponseBody
	public MonitorInterVo oneWeekQueryVoluLine(HttpServletRequest request) {
		log.info("MonitorinterController oneWeekQueryVoluLine method start......");
		MonitorInterVo vo = new MonitorInterVo();
		try {
			// 获取当前用户机构代码
			String orgCode = UserConfigUtils.getDeptCode(request);
			log.info("MonitorinterController oneWeekQueryVoluLine method orgCode:"+orgCode);
			ArrayList<Entry<String, Map<Integer, Integer>>> oneDayQueryVoluLine = monitorInterService.oneWeekQueryVoluLine(orgCode);
			// 此时 arrayList 中的key YYYYMMDD时间字符串 格式化为DD-HH arrayList里的数据
			// 转换成需要返回的json格式 return出去
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			// 存放日期的list
			List<String> dateList = new ArrayList<>();
			// 分别存放 1 2 3 状态的list
			List<Integer> seriesListof1 = new ArrayList<>();
			List<Integer> seriesListof2 = new ArrayList<>();
			List<Integer> seriesListof3 = new ArrayList<>();
			for (int i = 0; i < oneDayQueryVoluLine.size(); i++) {
				Entry<String, Map<Integer, Integer>> entry = oneDayQueryVoluLine.get(i);
				// 从Entry中拿到日期格式化为MM-DD
				Date date = sdf.parse(entry.getKey());
				cal.setTime(date);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DAY_OF_MONTH);
				// 将格式化好的日期存放到dateList中
				dateList.add(month + "-" + day);
				// 获取<状态值，num>
				Map<Integer, Integer> value = entry.getValue();
				// 分别将1 2 3 存放到对应的list中
				Integer integerOf1 = value.get("1");
				seriesListof1.add(integerOf1);
				Integer integerOf2 = value.get("2");
				seriesListof2.add(integerOf2);
				Integer integerOf3 = value.get("3");
				seriesListof3.add(integerOf3);
			}

			ArrayList<Map<String, List<Integer>>> seriesList = new ArrayList<>();
			// 对series数据格式转换
			Map<String, List<Integer>> seriesMap = new HashMap<>();
			seriesMap.put("1", seriesListof1);
			seriesMap.put("2", seriesListof2);
			seriesMap.put("3", seriesListof3);
			seriesList.add(seriesMap);
			// 将数据按照格式返给前台
			OneDayQueryVo weekQuery = new OneDayQueryVo(dateList, seriesList);
			vo.setWeekQuery(weekQuery);
		} catch (Exception e) {
			log.error("monitorinter oneWeekQueryVoluLine  method :" + e.getMessage());
		}
		log.info("MonitorinterController oneWeekQueryVoluLine method end......",vo);
		return vo;
	}

	/**
	 * 机构查询量排行榜
	 * 
	 * @return
	 */
	@RequestMapping("/instituQueryVoluLine")
	@ResponseBody
	public MonitorInterVo instituQueryVoluLine(HttpServletRequest request) {
		String orgCode = UserConfigUtils.getDeptCode(request);
		String toporg = UserConfigUtils.getRootDeptCode(orgCode);// 获取顶级机构
		// 获取当前用户辖内机构码
		List<String> jurisdictionDeptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(toporg);
		log.info("MonitorinterController instituQueryVoluLine method start......orgCode:"+orgCode+"----toporg:"+toporg+"--jurisdictionDeptCodes:"+jurisdictionDeptCodes);
		List<Entry<String, Integer>> orgQueryNumRankList = monitorInterService.getOrgQueryNumRank(jurisdictionDeptCodes,toporg);
		QueryVolumResultVo orgQueryNumRank = orgQueryNumRank(orgQueryNumRankList);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setOrgQueryNumRank(orgQueryNumRank);
		log.info("MonitorinterController instituQueryVoluLine method end......",vo);
		return vo;
	}

	/**
	 * 征信用户查询量排名
	 * 
	 * @return
	 */
	@RequestMapping("/creditInforQueryVoluLine")
	@ResponseBody
	public MonitorInterVo creditInforQueryVoluLine(HttpServletRequest request) {
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController creditInforQueryVoluLine method start......orgCode:"+orgCode);
		List<Entry<String, Integer>> orgQueryNumRankList = monitorInterService.getCcuserQueryNumRank(orgCode);
		QueryVolumResultVo creditUserQueryNumRank = orgQueryNumRank(orgQueryNumRankList);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setCreditUserQueryNumRank(creditUserQueryNumRank);
		log.info("MonitorinterController creditInforQueryVoluLine method end......",vo);
		return vo;
	}

	/**
	 * 内部用户查询量排名
	 * 
	 * @return
	 */
	@RequestMapping("/internalUserQueryVoluLine")
	@ResponseBody
	public MonitorInterVo internalUserQueryVoluLine(HttpServletRequest request) {
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController internalUserQueryVoluLine method start......orgCode:"+orgCode);
		List<Entry<String, Integer>> orgQueryNumRankList = monitorInterService.getInnerUserQueryNumRank(orgCode);
		QueryVolumResultVo innerUserQueryNumRank = orgQueryNumRank(orgQueryNumRankList);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setInnerUserQueryNumRank(innerUserQueryNumRank);
		log.info("MonitorinterController internalUserQueryVoluLine method end......",vo);
		return vo;
	}

	/**
	 * 征信用户查询异常
	 * 
	 * @return
	 */
	@RequestMapping("/creditUserExcep")
	@ResponseBody
	public MonitorInterVo creditUserExcep(HttpServletRequest request) {
		// 获取当前用户机构代码
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController creditUserExcep method start......orgCode:"+orgCode);
		List<String> creditExc = monitorInterService.getCreditExc(orgCode);
		QueryVolumResultVo data = new QueryVolumResultVo(creditExc);
		data.setData(creditExc);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setCreditExc(data);
		log.info("MonitorinterController creditUserExcep method end......",vo);
		return vo;
	}

	/**
	 * 内部用户查询异常
	 * @param request
	 * @return MonitorInterVo
	 */
	@RequestMapping("/internalUserExcep")
	@ResponseBody
	public MonitorInterVo internalUserExcep(HttpServletRequest request) {
		// 获取当前用户机构代码
		String orgCode = UserConfigUtils.getDeptCode(request);
		log.info("MonitorinterController internalUserExcep method start......orgCode:"+orgCode);
		List<String> innerExc = monitorInterService.getInnerExc(orgCode);
		QueryVolumResultVo data = new QueryVolumResultVo(innerExc);
		data.setData(innerExc);
		MonitorInterVo vo = new MonitorInterVo();
		vo.setInnerExc(data);
		log.info("MonitorinterController internalUserExcep method end......",vo);
		return vo;
	}

	/**
	 * This method is to convert the data format of the list passed in by the other three methods.
	   instituQueryVoluLine/creditInforQueryVoluLine/internalUserQueryVoluLine three methods included in this class
	 * @param orgQueryNumRankList
	 * @return QueryVolumResultVo
	 */
	private QueryVolumResultVo orgQueryNumRank(List<Entry<String, Integer>> orgQueryNumRankList) {
		log.info("MonitorinterController orgQueryNumRank method start......orgQueryNumRankList:"+orgQueryNumRankList);
		List<String> orgCode = new ArrayList<>();
		List<Integer> series = new ArrayList<>();
		for (Entry<String, Integer> entry : orgQueryNumRankList) {
			orgCode.add(entry.getKey());
			series.add(entry.getValue());
		}
		QueryVolumResultVo orgQueryNumRank = new QueryVolumResultVo(orgCode, series);
		log.info("MonitorinterController orgQueryNumRank method end......",orgQueryNumRank);
		return orgQueryNumRank;
	}
} 
