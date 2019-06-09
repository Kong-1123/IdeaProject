/*package cn.com.dhcc.creditquery.ent.queryweb.controller.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Objects;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.Constants;
import cn.com.dhcc.creditquery.ent.queryweb.util.HttpClientUtil;
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.ent.queryweb.vo.QueryByUserVo;
import cn.com.dhcc.creditquery.ent.queryweb.vo.UserResultVO;
import cn.com.dhcc.creditquery.ent.queryweb.vo.UserStatisticsDataVO;
import cn.com.dhcc.creditquery.ent.queryweb.vo.UserVO;
import cn.com.dhcc.query.creditenterprisequerydao.entity.resultinfo.CeqResultinfo;
import cn.com.dhcc.query.creditenterprisequeryservice.query.service.QueryResultService;
import cn.com.dhcc.query.creditenterprisequeryservice.util.UserUtils;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.DBUtils;
import lombok.extern.slf4j.Slf4j;

*//**
 * <统计管理---按机构查询controller>
 * 
 * @author Mingyu.Li
 * @date 2018年4月19日
 * 
 *//*
@Slf4j
@Controller
@RequestMapping(value = "/query")
public class QueryResultController extends BaseController {

	private static final String PREFIX = "query/";
	private final static String DIRECTION = "desc";

	*//**
	 * 通过操作时间进行倒叙
	 *//*
	private final static String ORDERBY = "updateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CeqQueryResultService queryService;

	*//**
	 * <搜索框>
	 * 
	 * @return
	 *//*
	@RequestMapping("/list")
	public String search() {
		return PREFIX + "search";
	}

	*//**
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqResultinfo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);

			if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
				List<String> deptCodes = UserUtils.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes,",");
				searchParams.put("IN_operOrg", deptCodeStr);
			}
			queryResults = queryService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("CeqResultinfo list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqResultinfo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	*//**
	 * 
	 * @param id
	 * @return
	 *//*
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CeqResultinfo alter = queryService.findById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	*//**
	 * <企业查询统计,按机构统计查询>
	 * 
	 * @param page
	 * @param request
	 * @return
	 *//*
	@RequestMapping("/findByAmountTo")
	@ResponseBody
	@LogOperation("按机构统计查询")
	public String findByAmountTo(HttpServletRequest request, QueryByUserVo queryByUserVo) {
		ResultBeans rs = null;
		String dateFormat = "yyyy-MM-dd";
		HashMap<String, Object> searchParams = new HashMap<>();
		try {
			
			String startDate = queryByUserVo.getSearch_GTE_updateTime_DATE();
			String endDate = queryByUserVo.getSearch_LTE_updateTime_DATE();
			if(StringUtils.isBlank(startDate)||StringUtils.isBlank(endDate)){
				rs = new ResultBeans(Constants.ERRORCODE, "请选择开始时间及结束时间");
				return rs.toJSONStr();
			}
			
			//判断DB类型获取日期函数
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
			
			//默认为本机构
			List<String> deptCodes = new ArrayList<>();
			deptCodes.add(UserUtils.getDeptCode(request));
			if(Objects.equal("0", operOrg)辖内机构){
				deptCodes = UserUtils.getJurisdictionDeptCodes(request);
			}else if(Objects.equal("1", operOrg)直属机构){

				String url = CeqConfigUtil.getSystemUrl() + SYSTEMORGURL;
				Map<String, String> params = new HashMap<>();
				params.put("orgId", UserUtils.getDeptCode(request));
				String orgCode = HttpClientUtil.send(url, params, "utf-8").replace("\"", "");
				String orgId = StringUtils.strip(orgCode.toString(), "[]");
				
				String[] split = orgId.split(",");
				deptCodes = Arrays.asList(split);
			}
			searchParams.put("operOrg", deptCodes);
			
			List<Map<String, Object>> queryUserList = queryService.statisticsResultByOrg(searchParams);
			// 对查询结果进行计数
			if (null == queryUserList || queryUserList.size() == 0) {
				rs = new ResultBeans(Constants.ERRORCODE, "未查询到相关数据");
				return rs.toJSONStr();
			}

			List<String> datePart = getColumnDate(search_LTE_updateTime_DATE, search_GTE_updateTime_DATE, queryByUserVo.getSearch_EQ_formReport());

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
				ArrayList<UserStatisticsDataVO> data = new ArrayList<UserStatisticsDataVO>();
				Map<String, long[]> reasonMap = hashMap.get(userName);
				for (String reason : reasonMap.keySet()) {
					UserStatisticsDataVO dataVO = new UserStatisticsDataVO(reason, reasonMap.get(reason));
					data.add(dataVO);
				}
				UserVO userVO = new UserVO(userName, data);
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

	*//**
	 * 根据入参的时间确定输出数据的时间列
	 * 
	 *//*
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
			startTimeCalendar.add(amount,1);// 进行当前日期月份加1
		}
		return dates;

	}

}
*/