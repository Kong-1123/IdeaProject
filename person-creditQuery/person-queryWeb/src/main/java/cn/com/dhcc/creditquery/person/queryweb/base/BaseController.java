package cn.com.dhcc.creditquery.person.queryweb.base;

import cn.com.dhcc.credit.platform.util.HttpClientUtil;
import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.creditquery.person.queryweb.util.Servlets2;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public abstract class BaseController {
	private final String PAGE_NUMBER_PARA = "pageNo";
	private final String PAGE_SIZE_PARA = "pageSize";
	public final static String QUERY_PARMA_PREFIX = "search_";
	public final static String SYSTEMORGURL = "systemOrg/findDepartmentDirectlyUnder";
	private static Logger log = LoggerFactory.getLogger(BaseController.class);

	public Map<String, Object> processRequestParams(PageBean page, HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets2.getParametersStartingWith(request, QUERY_PARMA_PREFIX, page);
		return searchParams;
	}
	
	public JPAParamGroup processRequestParamsToJPAParmGroup(PageBean page, HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets2.getParametersStartingWith(request, QUERY_PARMA_PREFIX, page);
        List<JPAParamGroup> list = new ArrayList<>();
        JPAParamGroup jpaParamGroup = null;
        for (Entry<String, Object> entry : searchParams.entrySet())
        {
            jpaParamGroup = new JPAParamGroup(entry.getKey(), entry.getValue());
            list.add(jpaParamGroup);
        }
        
        return new JPAParamGroup(list.toArray(new JPAParamGroup[]{} ));
    }

	/**
	 * 将查询结果，回填到返回的PageBean中。
	 * 
	 * @param page
	 * @param queryResults
	 */
	public <T> PageBean processQueryResults(Model model, PageBean page, Page<T> queryResults) {
		if (null == queryResults)
			return null;
		List<T> list = queryResults.getContent();
//		log.info("queryResults list "+queryResults.getContent());
		if (CollectionUtils.isNotEmpty(list)) {
			//展示列表脱敏
			QueryEncryptUtil.QueryEncrypt(this,list);
			page.setList(list);
			// 设置总的页数
			page.setPageCnt(queryResults.getTotalPages());
			// 设置总的条数
			page.setRecordCnt(new Long(queryResults.getTotalElements()).intValue());
			page.pageResult(list, page.getRecordCnt(), page.getMaxSize(), page.getCurPage());
		}
//		log.info("page list "+page.getList());
		return page;
	}

	public String[] getParaStrings(String name, ServletRequest request) {
		String[] s = {};
		if (request.getParameterValues(name) == null) {
			return s;
		}
		return request.getParameterValues(name);
	}

	public String getParaString(String name, ServletRequest request) {
		if (request.getParameter(name) == null) {
			return "";
		}
		return request.getParameter(name);
	}

	public Long getParaLong(String name, ServletRequest request) {
		return Long.parseLong(getParaString(name, request));
	}

	public Integer getParaInteger(String name, ServletRequest request) {
		return Integer.parseInt(getParaString(name, request));
	}

	public BigDecimal getParaBigDecimal(String name, ServletRequest request) {
		return new BigDecimal(getParaString(name, request));
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
	}
	
	/**
	 * 获取机构树
	 * 
	 * @return String[]
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public String getWithinDeptCode(String deptCode) throws ParseException, IOException {
		String url = ConfigUtil.getSystemUrl() + SYSTEMORGURL;
		Map<String, String> params = new HashMap<>();
		params.put("orgId", deptCode);
		String orgCode = HttpClientUtil.send(url, params, "utf-8").replace("\"", "");
		String orgId = StringUtils.strip(orgCode.toString(), "[]");
		orgId = StringUtils.isBlank(orgId) ? deptCode : orgId + ","+ deptCode;
		return orgId;
	}
	
	//根据信息可见范围参数值返回不同的机构树
	public String getDeptCodeByParam(String deptCode) throws ParseException,IOException{
		String param=ConfigUtil.getVisibility();
		if(param.equals("0")){
			return deptCode;
		}else{
			return getWithinDeptCode(deptCode);
		}
	}
	
	public Date endTimeHandler(Date endTime) throws Exception{
		//结束时间为短码格式，时分秒默认为0，若想统计结束日期当日的数据，需将结束时间加一天
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(endTime); 
		calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动 
		endTime=calendar.getTime(); 
		return endTime;
	}
}

