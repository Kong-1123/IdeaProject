package cn.com.dhcc.creditquery.ent.queryweb.controller.reportlog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.creditquery.ent.queryweb.util.DicServiceImpl;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.Excel;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.ent.query.bo.reportview.CeqReportLogBo;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportLogService;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * <信用报告操作记录-控制层>
 * @author wenjie·chu
 *
 * 2018年4月19日-下午3:51:58
 */

@Slf4j
@Controller
@RequestMapping(value = "/reportlog")
public class ReportLogController extends BaseController {
	private static final String PREFIX = "reportlog/";
	
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "operateDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private CeqReportLogService printFlowservice;

	@Autowired
	private DicServiceImpl dicService;
	
	/**
	 * <分页列表页面>
	 * @return
	 */
	@RequestMapping("/list")
	public String list(){
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
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String fidById(String id){
		CeqReportLogBo printflow = printFlowservice.findById(id);
		QueryEncryptUtil.QueryEncrypt(printflow);
		return JsonUtil.toJSONString(printflow);
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
	
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request){
		Page<CeqReportLogBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String operateDept = (String) searchParams.get("EQ_operateDept");
			if(StringUtils.isBlank(operateDept)){
				List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				searchParams.put("IN_operateDept",StringUtils.join(deptCodes,","));
			}
			queryResults = printFlowservice.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("printflow list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqReportLogBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	@RequestMapping("/exportExcel")
	@ResponseBody
	public HttpServletResponse exportExcel(String ids, PageBean page, HttpServletRequest request, HttpServletResponse response) {
		List<CeqReportLogBo> queryResults = null;
		String filePath = CeqConfigUtil.getTempPath();//临时目录
		try {
			if (org.apache.commons.lang3.StringUtils.isEmpty(ids)) {
				// 通过baseController的方法获取页面的查询条件
				Map<String, Object> searchParams = processRequestParams(page, request);
				if (org.apache.commons.lang3.StringUtils.isBlank((String) searchParams.get("IN_operOrg"))) {
					List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
					String deptCodeStr = org.apache.commons.lang3.StringUtils.join(deptCodes, ",");
					searchParams.put("IN_operOrg", deptCodeStr);
				}
				queryResults = printFlowservice.findAll(searchParams);
			} else {
				queryResults = printFlowservice.findCeqReportLogBosByIds(Arrays.asList(ids.split("\\,")));
			}
			String orgId = UserConfigUtils.getDeptCode(request);
			Excel excel = ExcelUtil.initExcel(dicService , CeqReportLogBo.class);
			excel.write(queryResults, CeqReportLogBo.class, filePath, "CeqReportLogBo.xls", true, true,orgId);
		} catch (Exception e) {
			log.error("exportExcel error e = {} " , e);
		}
		File file = new File(filePath + File.separator + "CeqReportLogBo.xls");
		return FileUtil.download(file, response);
	}

}
