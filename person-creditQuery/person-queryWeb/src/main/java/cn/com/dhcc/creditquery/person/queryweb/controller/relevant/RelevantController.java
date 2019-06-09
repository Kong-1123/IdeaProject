/*
package cn.com.dhcc.creditquery.person.queryweb.controller.relevant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.credit.platform.util.HttpClientUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.DBUtils;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryRelevantVo;
import cn.com.dhcc.creditquery.person.queryweb.vo.ReleventVO;
import cn.com.dhcc.query.creditpersonquerydao.entity.alert.CpqAlert;
import cn.com.dhcc.query.creditpersonqueryservice.relevant.service.CpqRelevantService;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;

*/
/**
 * <违规用户统计-控制器>
 * 
 * @author Mingyu.Li
 * @date 2018年3月15日
 * 
 *//*


@Controller
@RequestMapping(value = "/relevant")
public class RelevantController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(RelevantController.class);
	private static final String PREFIX = "relevant/";

	private final static String DIRECTION = "desc";

	private final static String SYSTEMORGURL = "systemOrg/findDepartmentDirectlyUnder";

	*/
/**
	 * 所有机构: 1：为辖内直属机构
	 *//*

	private final static String ORGRANGE = "1";

	*/
/**
	 * 通过操作时间进行倒叙
	 *//*

	private final static String ORDERBY = "updateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqRelevantService relevantService;

	*/
/**
	 * <违规用户统计主页 页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/list")
	public String list() {
		return PREFIX + "search";
	}

	*/
/**
	 * <跳转到详情页面>
	 * 
	 * @return
	 *//*

	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}

	*/
/**
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
		Page<CpqAlert> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			*/
/*	String deptCode = getWithinDeptCode(UserUtils.getDeptCode(request));
			searchParams.put("IN_orgCode", deptCode);*//*

			*/
/**
			 * 改直属为辖内
			 *//*

			if(StringUtils.isBlank((String)searchParams.get("IN_orgCode"))){
				List<String> deptCodes = UserUtils.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes,",");
				searchParams.put("IN_orgCode", deptCodeStr);
			}
			queryResults = relevantService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION,
					ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("archive list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqAlert>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	*/
/**
	 * 
	 * @param id
	 * @return
	 *//*

	@RequestMapping("/findCpqQueryRecordBoById")
	@ResponseBody
	public String findById(String id) {
		CpqAlert alter = relevantService.findById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	*/
/**
	 * <通过违规类型统计违规用户统计>
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 *//*

	@RequestMapping("/findByRelieve")
	@ResponseBody
	@LogOperation("违规分析统计")
	public String findByRelieve(Model model, PageBean page, HttpServletRequest request, QueryRelevantVo vo) {
		ResultBeans rs = null;
		SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd");
		List<ReleventVO> reList = new ArrayList<>();
		try {
			Date search_GTE_alertDate_DATE = vo.getSearch_GTE_alertDate_DATE();
			Date search_GTE_startDate_DATE = vo.getSearch_GTE_startDate_DATE();
			if(null == search_GTE_alertDate_DATE || null == search_GTE_startDate_DATE){
				rs = new ResultBeans(Constants.ERRORCODE, "请选择开始时间及结束时间");
				return rs.toJSONStr();
			}
			
			String trueEndDate =simDate.format(vo.getSearch_GTE_alertDate_DATE());
			String alertDate = simDate.format(vo.getSearch_GTE_startDate_DATE());
			String bussDate = simDate.format(vo.getSearch_GTE_alertDate_DATE());
			Map<String, String> info = UserUtils.getUserInfo(request);
			String deptCode = info.get("orgNo").trim();
			
//			String bussDateFuntion = DBUtils.getDBChar2DateFuntion(":BUSSDATE", DBUtils.DateFmater.YYYYMMDD);
//			String alertDateFuntion = DBUtils.getDBChar2DateFuntion(":ALERTDATE", DBUtils.DateFmater.YYYYMMDD);
			String bussDateFuntion = DBUtils.getDBDate2CharFuntion("ALERT_DATE", DBUtils.DateFmater.YYYYMMDD);
			String alertDateFuntion = DBUtils.getDBDate2CharFuntion("ALERT_DATE", DBUtils.DateFmater.YYYYMMDD);
			
			// 如果选择的是辖内直属
			if (vo.getSearch_IN_deptCode().equals(ORGRANGE)) {
				String url = ConfigUtil.getSystemUrl() + SYSTEMORGURL;
				Map<String,String> params = new HashMap<>();
				params.put("orgId", deptCode);
				String orgCode = HttpClientUtil.send(url, params, "utf-8").replace("\"", "");
				String[] orgId = StringUtils.strip(orgCode.toString(),"[]").split(",");
				ArrayList<String> arrayList = new ArrayList<>();
				for(String org:orgId){
					arrayList.add(org);
				}
				List<String[]> alertList = relevantService.findByAllStat(arrayList,alertDate, bussDate, alertDateFuntion,  bussDateFuntion);
				if (alertList != null && alertList.size() > 0) {
					for (int i = 0; i < alertList.size(); i++) {
						Object[] obj = (Object[]) alertList.get(i);
						String org = obj[0].toString();
						String status0 = obj[1].toString();
						String status1 =  obj[2].toString();
						String status2 =  obj[3].toString();
						String status3 =  obj[4].toString();
						String status4 =  obj[5].toString();
						String status5 =  obj[6].toString();
						String status6 =  obj[7].toString();
						String status7 =  obj[8].toString();
						String status8 =  obj[9].toString();
						String status9 =  obj[10].toString();
						String status10 =  obj[11].toString();
						reList.add(new ReleventVO(org,status0,status1,status2,status3,status4,status5,status6,status7,status8,status9,status10));
					}
				}else{
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
			}else if(vo.getSearch_IN_deptCode().equals("2")*/
/*本机构*//*
){
				List<String[]> alterList = relevantService.findByTotal4Agency(deptCode, alertDate, bussDate,alertDateFuntion,bussDateFuntion);
				if (alterList != null && alterList.size() > 0) {
					for (int i = 0; i < alterList.size(); i++) {
					    Object[] obj = (Object[]) alterList.get(i);
                        String org = obj[0].toString();
                        String status0 = obj[1].toString();
                        String status1 =  obj[2].toString();
                        String status2 =  obj[3].toString();
                        String status3 =  obj[4].toString();
                        String status4 =  obj[5].toString();
                        String status5 =  obj[6].toString();
                        String status6 =  obj[7].toString();
                        String status7 =  obj[8].toString();
                        String status8 =  obj[9].toString();
                        String status9 =  obj[10].toString();
                        String status10 =  obj[11].toString();
                        reList.add(new ReleventVO(org,status0,status1,status2,status3,status4,status5,status6,status7,status8,status9,status10));
					}
				}else{
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
			}else {
				List<String[]> alterList = relevantService.findByTotal(deptCode, alertDate, bussDate,alertDateFuntion,bussDateFuntion);
				if (alterList != null && alterList.size() > 0) {
					for (int i = 0; i < alterList.size(); i++) {
						Object[] obj = (Object[]) alterList.get(i);
						 String org = obj[0].toString();
	                        String status0 = obj[1].toString();
	                        String status1 =  obj[2].toString();
	                        String status2 =  obj[3].toString();
	                        String status3 =  obj[4].toString();
	                        String status4 =  obj[5].toString();
	                        String status5 =  obj[6].toString();
	                        String status6 =  obj[7].toString();
	                        String status7 =  obj[8].toString();
	                        String status8 =  obj[9].toString();
	                        String status9 =  obj[10].toString();
	                        String status10 =  obj[11].toString();
	                        reList.add(new ReleventVO(org,status0,status1,status2,status3,status4,status5,status6,status7,status8,status9,status10));
					}
				}else{
					rs = new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对");
				}
			}
			if(reList.size()!=0){
				JSONObject json = new JSONObject();
				json.put("startDate", alertDate);
				json.put("endDate", trueEndDate);
				json.put("list", reList);
				String jsonString = json.toJSONString();
				return jsonString;
			}else{
				return new ResultBeans(Constants.ERRORCODE, "您输入的查询条件没有查询到结果，请确认核对").toJSONStr();
			}
		} catch (Exception e) {
			log.error("person illegal user controller"+e.getMessage());
			rs = new ResultBeans(Constants.ERRORCODE, "后台出错");
		}
		return rs.toJSONStr();

	}

}
*/
