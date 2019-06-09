package cn.com.dhcc.creditquery.person.queryweb.controller.statistics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

import cn.com.dhcc.credit.platform.util.HttpClientUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqAlertBo;
import cn.com.dhcc.creditquery.person.query.bo.querystatistics.CpqQueryStatisticsBo;
import cn.com.dhcc.creditquery.person.querystatistics.service.CpqPersonQueryStatisticsService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryByUserVo;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryRelevantVo;
import cn.com.dhcc.creditquery.person.queryweb.vo.ReleventVO;
import cn.com.dhcc.creditquery.person.queryweb.vo.UserResultVO;
import cn.com.dhcc.creditquery.person.queryweb.vo.UserStatisticsDataVO;
import cn.com.dhcc.creditquery.person.queryweb.vo.UserVO;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.DBUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 统计控制器
 * 
 * @author yuzhao.xue
 * @date 2019年2月28日
 */
@Slf4j
@Controller
public class StatisticsController extends BaseController {
	private final static String DIRECTION = "desc";
	/**
	 * 通过操作时间进行倒叙
	 */
	private final static String ORDERBY = "alertDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqPersonQueryStatisticsService statisticsService;

	@Autowired
	private PlantFormInteractiveService plantFormInteractiveService;

	// -------------来源QueryByUserController

	private static final String PREFIX_QUERYBYUSER = "queryByUser/";
	/**
	 * queryStatisticType 0-按用户
	 */
	final static String QUERYSTATISTICTYPE_USER = "0";
	/**
	 * queryStatisticType 0-按用户
	 */
	final static String QUERYSTATISTICTYPE_ORG = "1";

	/**
	 * <通过用户统计搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/queryByUser/list")
	public String searchByUser() {
		return PREFIX_QUERYBYUSER + "search";
	}

	/**
	 * 通过用户统计分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryByUser/getPage")
	@ResponseBody
	public String listByUser(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqQueryStatisticsBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String deptCode = getWithinDeptCode(UserConfigUtils.getDeptCode(request));
			searchParams.put("IN_operOrg", deptCode);
			queryResults = statisticsService.getQueryStatisticPage(searchParams, page.getCurPage(), page.getMaxSize(),
					DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("CpqResultinfo list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqQueryStatisticsBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	/**
	 * 按用户统计通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryByUser/findCpqQueryRecordBoById")
	@ResponseBody
	public String findByIdByUser(String id) {
		CpqQueryStatisticsBo alter = statisticsService.findQueryStatisticsById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	/**
	 * <个人查询统计,按用户统计查询>
	 * 
	 * @param queryByUserVo
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryByUser/findByQueryUserSum")
	@ResponseBody
	@LogOperation("按用户统计查询")
	public String findByQueryUserSum(HttpServletRequest request, QueryByUserVo queryByUserVo) {
		ResultBeans rs = null;
		String dateFormat = "yyyy-MM-dd";
		HashMap<String, Object> searchParams = new HashMap<>();
		try {

			String startDate = queryByUserVo.getSearch_GTE_updateTime_DATE();
			String endDate = queryByUserVo.getSearch_LTE_updateTime_DATE();
			if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
				rs = new ResultBeans(Constants.ERRORCODE, "请选择开始时间及结束时间");
				return rs.toJSONStr();
			}

			// 判断DB类型获取日期函数
			DBUtils.DateFmater fmater = null;
			String formReport = queryByUserVo.getSearch_EQ_formReport();// 制式报表
			if (StringUtils.isBlank((CharSequence) formReport) || "1".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYYMMDD;
			} else if ("2".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYYMM;
				dateFormat = "yyyy-MM";
			} else if ("3".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYY;
				dateFormat = "yyyy";
			}
			String funtion = DBUtils.getDBDate2CharFuntion("QUERY_TIME", fmater);
			searchParams.put("FUNTION", funtion);

			SimpleDateFormat simDate = new SimpleDateFormat(dateFormat);
			Date search_LTE_updateTime_DATE = simDate.parse(startDate);
			Date search_GTE_updateTime_DATE = simDate.parse(endDate);
			searchParams.put("startDate", startDate);
			searchParams.put("endDate", endDate);
			searchParams.put("EQ_userType", queryByUserVo.getSearch_EQ_userType());
			searchParams.put("EQ_batchFlag", queryByUserVo.getSearch_EQ_batchFlag());
			searchParams.put("EQ_source", queryByUserVo.getSearch_EQ_source());
			searchParams.put("EQ_autharchiveId", queryByUserVo.getSearch_EQ_autharchiveId());
			searchParams.put("EQ_status", queryByUserVo.getSearch_EQ_status());
			searchParams.put("EQ_formReport", queryByUserVo.getSearch_EQ_formReport());
			searchParams.put("EQ_queryType", queryByUserVo.getSearch_EQ_queryType());
			searchParams.put("EQ_operOrg", queryByUserVo.getSearch_EQ_operOrg());

			List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
			searchParams.put("operOrg", deptCodes);

			List<Map<String, Object>> queryUserList = statisticsService.queryStatisticsListGroupByUser(searchParams,
					QUERYSTATISTICTYPE_USER);
			// 对查询结果进行计数
			if (null == queryUserList || queryUserList.size() == 0) {
				rs = new ResultBeans(Constants.ERRORCODE, "未查询到相关数据");
				return rs.toJSONStr();
			}

			List<String> datePart = getColumnDate(search_LTE_updateTime_DATE, search_GTE_updateTime_DATE,
					queryByUserVo.getSearch_EQ_formReport());

			HashMap<String, Map<String, long[]>> hashMap = new HashMap<>();

			for (Map<String, Object> map : queryUserList) {
				String username = (String) map.get("username");
				String queryTime = (String) map.get("queryTime");
				String queryReason = (String) map.get("queryReason");
				long count = ((Number) map.get("count")).longValue();
				Map<String, long[]> object = null;
				if (hashMap.containsKey(username)) {
					object = hashMap.get(username);
				} else {
					object = new HashMap<>();
					hashMap.put(username, object);
				}
				long[] countNum = null;
				if (object.containsKey(queryReason)) {
					countNum = object.get(queryReason);
				} else {
					countNum = new long[datePart.size()];
					object.put(queryReason, countNum);
				}

				int i = datePart.indexOf(queryTime);
				countNum[i] = count;
			}

			List<UserVO> userVoList = new ArrayList<>();

			for (String userName : hashMap.keySet()) {
				ArrayList<UserStatisticsDataVO> dataList = new ArrayList<UserStatisticsDataVO>();
				Map<String, long[]> reasonMap = hashMap.get(userName);
				for (String reason : reasonMap.keySet()) {
					UserStatisticsDataVO dataVO = new UserStatisticsDataVO(reason, reasonMap.get(reason));
					dataList.add(dataVO);
				}
				UserVO userVO = new UserVO(userName, dataList);
				userVoList.add(userVO);
			}
			UserResultVO userResultVO = new UserResultVO(datePart, userVoList);

			return JsonUtil.toJSONString(userResultVO);
		} catch (Exception e) {
			log.error("person queryByUser controller e={}" + e);
			rs = new ResultBeans(Constants.ERRORCODE, "系统异常");
		}
		return rs.toJSONStr();
	}

	/**
	 * 根据入参的时间确定输出数据的时间列
	 * 
	 */
	public List<String> getColumnDate(Date startTime, Date endTime, String formatReport) {
		String dateFormat = "yyyy-MM-dd";
		int amount = Calendar.DATE;
		switch (formatReport) {
		case "2":
			dateFormat = "yyyy-MM";
			amount = Calendar.MONTH;
			break;
		case "3":
			dateFormat = "yyyy";
			amount = Calendar.YEAR;
			break;
		default:
			break;
		}

		Calendar startTimeCalendar = Calendar.getInstance();// 定义日期实例
		Calendar endTimeCalendar = Calendar.getInstance();// 定义日期实例
		startTimeCalendar.setTime(startTime);// 设置日期起始时间
		endTimeCalendar.setTime(endTime);
		ArrayList<String> dates = new ArrayList<String>();

		while (!startTimeCalendar.after(endTimeCalendar)) {// 判断是否到结束日期
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String str = sdf.format(startTimeCalendar.getTime());
			dates.add(str);
			startTimeCalendar.add(amount, 1);// 进行当前日期月份加1
		}
		return dates;

	}

	// 通过机构统计 来源QueryResultController

	private static final String PREFIX = "query/";

	/**
	 * <搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/query/list")
	public String search() {
		return PREFIX + "search";
	}

	/**
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */

	@RequestMapping("/query/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqQueryStatisticsBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String deptCode = getWithinDeptCode(UserUtilsForConfig.getDeptCode(request));
			searchParams.put("IN_operOrg", deptCode);
			queryResults = statisticsService.getQueryStatisticPage(searchParams, page.getCurPage(), page.getMaxSize(),
					DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("CpqResultinfo list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqQueryStatisticsBo>());
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

	@RequestMapping("/query/findCpqQueryRecordBoById")
	@ResponseBody
	public String findById(String id) {
		CpqQueryStatisticsBo alter = statisticsService.findQueryStatisticsById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	/**
	 * <个人查询统计,按机构统计查询>
	 * 
	 * @param queryByUserVo
	 * @param request
	 * @return
	 */

	@LogOperation("按机构统计查询")
	@RequestMapping("/query/findByAmountTo")
	@ResponseBody
	public String findByAmountTo(HttpServletRequest request, QueryByUserVo queryByUserVo) {
		ResultBeans rs = null;
		String dateFormat = "yyyy-MM-dd";
		HashMap<String, Object> searchParams = new HashMap<>();
		try {

			String startDate = queryByUserVo.getSearch_GTE_updateTime_DATE();
			String endDate = queryByUserVo.getSearch_LTE_updateTime_DATE();
			if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
				rs = new ResultBeans(Constants.ERRORCODE, "请选择开始时间及结束时间");
				return rs.toJSONStr();
			}

			// 判断DB类型获取日期函数
			DBUtils.DateFmater fmater = null;
			String formReport = queryByUserVo.getSearch_EQ_formReport();// 制式报表
			if (StringUtils.isBlank((CharSequence) formReport) || "1".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYYMMDD;
			} else if ("2".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYYMM;
				dateFormat = "yyyy-MM";
			} else if ("3".equals(formReport)) {
				fmater = DBUtils.DateFmater.YYYY;
				dateFormat = "yyyy";
			}
			String funtion = DBUtils.getDBDate2CharFuntion("QUERY_TIME", fmater);
			searchParams.put("FUNTION", funtion);

			SimpleDateFormat simDate = new SimpleDateFormat(dateFormat);
			Date search_LTE_updateTime_DATE = simDate.parse(startDate);
			Date search_GTE_updateTime_DATE = simDate.parse(endDate);

			String operOrg = queryByUserVo.getSearch_EQ_operOrg();
			searchParams.put("startDate", startDate);
			searchParams.put("endDate", endDate);
//			searchParams.put("EQ_operOrg", queryByUserVo.getSsearch_EQ_operOrg());
			searchParams.put("EQ_batchFlag", queryByUserVo.getSearch_EQ_batchFlag());
			searchParams.put("EQ_source", queryByUserVo.getSearch_EQ_source());
			searchParams.put("EQ_autharchiveId", queryByUserVo.getSearch_EQ_autharchiveId());
			searchParams.put("EQ_status", queryByUserVo.getSearch_EQ_status());
			searchParams.put("EQ_formReport", queryByUserVo.getSearch_EQ_formReport());
			searchParams.put("EQ_queryType", queryByUserVo.getSearch_EQ_queryType());

			// 默认为本机构
			List<String> deptCodes = new ArrayList<>();
			deptCodes.add(UserUtilsForConfig.getDeptCode(request));
			// 辖内机构
			if (Objects.equal("0", operOrg)) {
				deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				//
			} else if (Objects.equal("1", operOrg)) {
				String deptCode = UserUtilsForConfig.getDeptCode(request);
				String orgCode = plantFormInteractiveService.findDepartmentDirectlyUnder(deptCode);
				String orgId = StringUtils.strip(orgCode.toString(), "[]");
				String[] split = orgId.split(",");
				deptCodes = Arrays.asList(split);
			}
			// 获取辖内机构
			searchParams.put("operOrg", deptCodes);

			List<Map<String, Object>> queryUserList = statisticsService.queryStatisticsListGroupByUser(searchParams,
					QUERYSTATISTICTYPE_ORG);
			// 对查询结果进行计数
			if (null == queryUserList || queryUserList.size() == 0) {
				rs = new ResultBeans(Constants.ERRORCODE, "未查询到相关数据");
				return rs.toJSONStr();
			}

			List<String> datePart = getColumnDate(search_LTE_updateTime_DATE, search_GTE_updateTime_DATE,
					queryByUserVo.getSearch_EQ_formReport());

			HashMap<String, Map<String, long[]>> hashMap = new HashMap<>();

			for (Map<String, Object> map : queryUserList) {
				String username = (String) map.get("org");
				String queryTime = (String) map.get("queryTime");
				String queryReason = (String) map.get("queryReason");
				long count = ((Number) map.get("count")).longValue();
				Map<String, long[]> object = null;
				if (hashMap.containsKey(username)) {
					object = hashMap.get(username);
				} else {
					object = new HashMap<>();
					hashMap.put(username, object);
				}
				long[] countNum = null;
				if (object.containsKey(queryReason)) {
					countNum = object.get(queryReason);
				} else {
					countNum = new long[datePart.size()];
					object.put(queryReason, countNum);
				}

				int i = datePart.indexOf(queryTime);
				countNum[i] = count;
			}

			List<UserVO> userVoList = new ArrayList<>();

			for (String userName : hashMap.keySet()) {
				ArrayList<UserStatisticsDataVO> dataList = new ArrayList<UserStatisticsDataVO>();
				Map<String, long[]> reasonMap = hashMap.get(userName);
				for (String reason : reasonMap.keySet()) {
					UserStatisticsDataVO dataVO = new UserStatisticsDataVO(reason, reasonMap.get(reason));
					dataList.add(dataVO);
				}
				UserVO userVO = new UserVO(userName, dataList);
				userVoList.add(userVO);
			}
			UserResultVO userResultVO = new UserResultVO(datePart, userVoList);

			return JsonUtil.toJSONString(userResultVO);
		} catch (Exception e) {
			log.error("person queryByUser controller" + e.getMessage());
			rs = new ResultBeans(Constants.ERRORCODE, "系统异常");
		}
		return rs.toJSONStr();
	}

	// 违规分析统计 来源RelevantController

	private static final String PREFIX_RELEVANT = "relevant/";

	private final static String SYSTEMORGURL = "systemOrg/findDepartmentDirectlyUnder";

	/**
	 * 所有机构: 1：为辖内直属机构
	 */

	private final static String ORGRANGE = "1";

	/**
	 * <违规用户统计主页 页面>
	 * 
	 * @return
	 */

	@RequestMapping("/relevant/list")
	public String list() {
		return PREFIX_RELEVANT + "search";
	}

	/**
	 * <跳转到详情页面>
	 * 
	 * @return
	 */

	@RequestMapping("/relevant/detail")
	public String detail() {
		return PREFIX + "detail";
	}

	/**
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */

	@RequestMapping("/relevant/getPage")
	@ResponseBody
	public String relevantList(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqAlertBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String deptCode = getWithinDeptCode(UserUtilsForConfig.getDeptCode(request));
			searchParams.put("IN_orgCode", deptCode);
			/**
			 * 改直属为辖内
			 */
			if (StringUtils.isBlank((String) searchParams.get("IN_orgCode"))) {
				List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes, ",");
				searchParams.put("IN_orgCode", deptCodeStr);
			}
			queryResults = statisticsService.getCpqAlertPage(searchParams, page.getCurPage(), page.getMaxSize(),
					DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("archive list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqAlertBo>());
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

	@RequestMapping("/relevant/findCpqQueryRecordBoById")
	@ResponseBody
	public String findRelevantById(String id) {
		CpqAlertBo alter = statisticsService.findCpqAlertById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	/**
	 * <通过违规类型统计违规用户统计>
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/relevant/findByRelieve")
	@ResponseBody
	@LogOperation("违规分析统计")
	public String findByRelieve(Model model, PageBean page, HttpServletRequest request, QueryRelevantVo vo) {
		ResultBeans rs = null;
		SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd");
		List<ReleventVO> reList = new ArrayList<>();
		try {
			Date search_GTE_alertDate_DATE = vo.getSearch_GTE_alertDate_DATE();
			Date search_GTE_startDate_DATE = vo.getSearch_GTE_startDate_DATE();
			if (null == search_GTE_alertDate_DATE || null == search_GTE_startDate_DATE) {
				rs = new ResultBeans(Constants.ERRORCODE, "请选择开始时间及结束时间");
				return rs.toJSONStr();
			}

			String trueEndDate = simDate.format(vo.getSearch_GTE_alertDate_DATE());
			String alertDate = simDate.format(vo.getSearch_GTE_startDate_DATE());
			String bussDate = simDate.format(vo.getSearch_GTE_alertDate_DATE());
			Map<String, String> info = UserUtilsForConfig.getUserInfo(request);
			String deptCode = info.get("orgNo").trim();

//			String bussDateFuntion = DBUtils.getDBChar2DateFuntion(":BUSSDATE", DBUtils.DateFmater.YYYYMMDD);
//			String alertDateFuntion = DBUtils.getDBChar2DateFuntion(":ALERTDATE", DBUtils.DateFmater.YYYYMMDD);
			String bussDateFuntion = DBUtils.getDBDate2CharFuntion("ALERT_DATE", DBUtils.DateFmater.YYYYMMDD);
			String alertDateFuntion = DBUtils.getDBDate2CharFuntion("ALERT_DATE", DBUtils.DateFmater.YYYYMMDD);

			// 如果选择的是辖内直属
			if (vo.getSearch_IN_deptCode().equals(ORGRANGE)) {
				// 获取辖内直属机构代码
				ArrayList<String> arrayList = getOrgId(deptCode);
				List<String[]> alertList = statisticsService.findByAllStat(arrayList, alertDate, bussDate,
						alertDateFuntion, bussDateFuntion);
				if (alertList != null && alertList.size() > 0) {
					for (int i = 0; i < alertList.size(); i++) {
						Object[] obj = (Object[]) alertList.get(i);
						String org = obj[0].toString();
						String status0 = obj[1].toString();
						String status1 = obj[2].toString();
						String status2 = obj[3].toString();
						String status3 = obj[4].toString();
						String status4 = obj[5].toString();
						String status5 = obj[6].toString();
						String status6 = obj[7].toString();
						String status7 = obj[8].toString();
						String status8 = obj[9].toString();
						String status9 = obj[10].toString();
						String status10 = obj[11].toString();
						reList.add(new ReleventVO(org, status0, status1, status2, status3, status4, status5, status6,
								status7, status8, status9, status10));
					}
				} else {
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
				// 本机构
			} else if (vo.getSearch_IN_deptCode().equals("2")) {
				List<String[]> alterList = statisticsService.findByTotal4Agency(deptCode, alertDate, bussDate,
						alertDateFuntion, bussDateFuntion);
				if (alterList != null && alterList.size() > 0) {
					for (int i = 0; i < alterList.size(); i++) {
						Object[] obj = (Object[]) alterList.get(i);
						String org = obj[0].toString();
						String status0 = obj[1].toString();
						String status1 = obj[2].toString();
						String status2 = obj[3].toString();
						String status3 = obj[4].toString();
						String status4 = obj[5].toString();
						String status5 = obj[6].toString();
						String status6 = obj[7].toString();
						String status7 = obj[8].toString();
						String status8 = obj[9].toString();
						String status9 = obj[10].toString();
						String status10 = obj[11].toString();
						reList.add(new ReleventVO(org, status0, status1, status2, status3, status4, status5, status6,
								status7, status8, status9, status10));
					}
				} else {
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
			} else {
				List<String[]> alterList = statisticsService.findByTotal(deptCode, alertDate, bussDate,
						alertDateFuntion, bussDateFuntion);
				if (alterList != null && alterList.size() > 0) {
					for (int i = 0; i < alterList.size(); i++) {
						Object[] obj = (Object[]) alterList.get(i);
						String org = obj[0].toString();
						String status0 = obj[1].toString();
						String status1 = obj[2].toString();
						String status2 = obj[3].toString();
						String status3 = obj[4].toString();
						String status4 = obj[5].toString();
						String status5 = obj[6].toString();
						String status6 = obj[7].toString();
						String status7 = obj[8].toString();
						String status8 = obj[9].toString();
						String status9 = obj[10].toString();
						String status10 = obj[11].toString();
						reList.add(new ReleventVO(org, status0, status1, status2, status3, status4, status5, status6,
								status7, status8, status9, status10));
					}
				} else {
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
			}
			if (reList.size() != 0) {
				JSONObject json = new JSONObject();
				json.put("startDate", alertDate);
				json.put("endDate", trueEndDate);
				json.put("list", reList);
				String jsonString = json.toJSONString();
				return jsonString;
			} else {
				return new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对").toJSONStr();
			}
		} catch (Exception e) {
			log.error("person illegal user controller" + e.getMessage());
			rs = new ResultBeans(Constants.ERRORCODE, "后台出错");
		}
		return rs.toJSONStr();

	}

	/**
	 * 获取辖内直属机构代码
	 * 
	 * @param deptCode
	 * @return
	 * @throws IOException
	 */
	private ArrayList<String> getOrgId(String deptCode) throws IOException {
		String orgCode = plantFormInteractiveService.findDepartmentDirectlyUnder(deptCode);
		String[] orgId = StringUtils.strip(orgCode.toString(), "[]").split(",");
		ArrayList<String> arrayList = new ArrayList<>();
		for (String org : orgId) {
			arrayList.add(org);
		}
		return arrayList;
	}
}
