package cn.com.dhcc.creditquery.person.queryweb.controller.userquerynum;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.creditquery.person.query.bo.queryCounter.CpqUserQueryNumBo;
import cn.com.dhcc.creditquery.person.querycounter.service.CpqUserQueryNumService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

@Controller
@RequestMapping("/userquerynum")
public class UserQueryNumController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(UserQueryNumController.class);
	@Autowired
	private CpqUserQueryNumService CpqUserQueryNumService;

	/**
	 * 统计日期区间长度sss dao层需要单独修改
	 */

	private static final int SIZE = 7;

	@RequestMapping("/weekData")
	@ResponseBody
	public String weekData(HttpServletRequest request) {
		List<String> menuIds = UserUtilsForConfig.getUserMenuIds(request);
		if (!menuIds.contains(Constants.MenuId.PERSONAL_QUERY)) {// 没有“个人信用报告查询”权限
			return null;
		}
		Map<String, Object> searchParams = new HashMap<>();
		if (menuIds.contains(Constants.MenuId.SEARSH_BY_PERSON)
				|| menuIds.contains(Constants.MenuId.SEARSH_BY_ORGAN)
				|| menuIds.contains(Constants.MenuId.SYSTEM)) {// 管理员
			List<String> jurisdictionDeptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
			searchParams.put("IN_deptCode", jurisdictionDeptCodes);
		} else {
			String userName = UserUtilsForConfig.getUserName(request);
			searchParams.put("EQ_userName", userName);
		}
		List<CpqUserQueryNumBo> success = reverse(insertEmpty(getQueryNumList(searchParams, cn.com.dhcc.query.creditquerycommon.util.Constants.QUERY_SUCCESS)));
		List<CpqUserQueryNumBo> fail = reverse(insertEmpty(getQueryNumList(searchParams, cn.com.dhcc.query.creditquerycommon.util.Constants.QUERY_DEFEATE)));
		List<CpqUserQueryNumBo> noThisOne = reverse(
				insertEmpty(getQueryNumList(searchParams, cn.com.dhcc.query.creditquerycommon.util.Constants.QUERY_UNKNOWN)));
		if (success.size() != fail.size() || success.size() != noThisOne.size()) {// 这种情况统计会异常，真发生了再修复吧
			logger.error("用户名[{}]查询量数据异常，成功、失败、查无此人 条数不一致", UserUtilsForConfig.getUserName(request));
		}
		List<String> weekDay = getWeekDate(success);
		List<String> total = calculateTotalNum(success, fail, noThisOne);
		JSONObject result = new JSONObject();
		result.put("success", toQueryNumList(success));
		result.put("fail", toQueryNumList(fail));
		result.put("noThisOne", toQueryNumList(noThisOne));
		result.put("weekDay", weekDay);
		result.put("total", total);
		return result.toString();
	}

	/**
	 * 某些日期可能没有查询数据，插入空数据处理
	 * 
	 * @param list
	 * @return
	 */

	private List<CpqUserQueryNumBo> insertEmpty(List<CpqUserQueryNumBo> list) {
		List<CpqUserQueryNumBo> result = new ArrayList<>(SIZE);
		Loop: if (CollectionUtils.isNotEmpty(list)) {
			result.add(list.get(0));
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1)
					break;
				CpqUserQueryNumBo current = list.get(i);
				CpqUserQueryNumBo next = list.get(i + 1);
				Calendar currentDate = Calendar.getInstance();
				Calendar nextDate = Calendar.getInstance();
				currentDate.setTime(current.getBussinessDate());
				nextDate.setTime(next.getBussinessDate());
				int d1 = currentDate.get(Calendar.DAY_OF_YEAR);
				int d2 = nextDate.get(Calendar.DAY_OF_YEAR);
				int range = d1 - d2;
				if (range == 1) {
					result.add(next);
				} else {
					for (int j = 0; j < range - 1; j++) {
						CpqUserQueryNumBo middle = new CpqUserQueryNumBo();
						currentDate.add(Calendar.DAY_OF_YEAR, -1);
						middle.setBussinessDate(currentDate.getTime());
						middle.setStatus(current.getStatus());
						middle.setNum(0);
						result.add(middle);
						if (result.size() == SIZE)
							break Loop;
					}
					result.add(next);
				}
				if (result.size() == SIZE)
					break Loop;
			}
		}
		return result;
	}

	private List<CpqUserQueryNumBo> reverse(List<CpqUserQueryNumBo> list) {
		List<CpqUserQueryNumBo> reverse = new ArrayList<>(list.size());
		for (int i = list.size() - 1; i > -1; i--) {
			reverse.add(list.get(i));
		}
		return reverse;
	}

	/**
	 * 计算总数，之所以用String，是为了避免js丢失精度
	 * 
	 * @return
	 */

	private List<String> calculateTotalNum(List<CpqUserQueryNumBo> success, List<CpqUserQueryNumBo> fail,
			List<CpqUserQueryNumBo> noThisOne) {
		List<String> total = new ArrayList<>();
		for (int i = 0, l = success.size(); i < l; i++) {
			int s = success.get(i).getNum();
			int f = 0;
			if (i < fail.size())
				f = fail.get(i).getNum();
			int n = 0;
			if (i < noThisOne.size())
				n = noThisOne.get(i).getNum();
			total.add(String.valueOf(s + f + n));
		}
		return total;
	}

	private List<String> toQueryNumList(List<CpqUserQueryNumBo> list) {
		List<String> queryNumList = new ArrayList<>();
		for (CpqUserQueryNumBo queryNum : list) {
			queryNumList.add(String.valueOf(queryNum.getNum()));
		}
		return queryNumList;
	}

	private List<String> getWeekDate(List<CpqUserQueryNumBo> success) {
		List<String> weekDay = new ArrayList<>();
		DateFormat formator = new SimpleDateFormat("yyyy-MM-dd");
		for (CpqUserQueryNumBo queryNum : success) {
			weekDay.add(formator.format(queryNum.getBussinessDate()));
		}
		return weekDay;
	}

	/**
	 * 查询量列表
	 * 
	 * @param searchParams
	 * @param status       查询结果
	 * @return
	 */

	private List<CpqUserQueryNumBo> getQueryNumList(Map<String, Object> searchParams, int status) {
		searchParams.put("EQ_status", status);
		// 取前七条（近一周）
		if (searchParams.get("EQ_userName") != null) {
			Page<CpqUserQueryNumBo> page = CpqUserQueryNumService.getPage(searchParams, 1, 7, "desc", "bussinessDate");
			return page.getContent();
		}
		@SuppressWarnings("unchecked")
		List<Object[]> list = CpqUserQueryNumService
				.findQueryNumByDeptCode((List<String>) searchParams.get("IN_deptCode"), status);
		return convert(list);
	}

	private List<CpqUserQueryNumBo> convert(List<Object[]> list) {
		List<CpqUserQueryNumBo> result = new ArrayList<>();
		for (Object[] arr : list) {
			CpqUserQueryNumBo num = new CpqUserQueryNumBo();
			num.setBussinessDate((Date) arr[0]);
			num.setNum(((BigDecimal) arr[1]).intValue());
			result.add(num);
		}
		return result;
	}

	// 测试数据
	@RequestMapping("/testData")
	@ResponseBody
	public String testData(HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		Calendar now = Calendar.getInstance();
		for (int i = 0; i < 31; i++) {
			CpqUserQueryNumBo num0 = new CpqUserQueryNumBo();
			CpqUserQueryNumBo num1 = new CpqUserQueryNumBo();
			CpqUserQueryNumBo num2 = new CpqUserQueryNumBo();
			now.add(Calendar.DATE, -1);
			Date bussDate = now.getTime();
			num0.setCreateTime(bussDate);
			num0.setUpdateTime(bussDate);
			num0.setBussinessDate(bussDate);
			num0.setNum(new Random().nextInt(30));
			num0.setDeptCode(UserUtilsForConfig.getDeptCode(request));
			num0.setUserName(UserUtilsForConfig.getUserName(request));
			num0.setStatus(0);
			BeanUtils.copyProperties(num1, num0);
			num1.setStatus(1);
			num1.setNum(new Random().nextInt(5));
			BeanUtils.copyProperties(num2, num0);
			num2.setStatus(2);
			num2.setNum(new Random().nextInt(10));
			CpqUserQueryNumService.save(num0);
			CpqUserQueryNumService.save(num1);
			CpqUserQueryNumService.save(num2);
		}
		return "ok";
	}
}
