package cn.com.dhcc.creditquery.ent.queryweb.controller.queryresult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.service.CeqFlowManageService;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.util.CsvExportGetterImpl;
import cn.com.dhcc.creditquery.ent.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvUtil;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * <查询记录管理----控制层>
 * 
 * @author Mingyu.Li
 * @date 2018年3月10日
 * 
 */
@Slf4j
@Controller
@RequestMapping(value = "/resultInfo")
public class ResultInfoController extends BaseController {

	private static final String PREFIX = "resultInfo/";

	private final static String DIRECTION = "desc";

	private final static String RESULTINFO = "resultinfo";

	/**
	 * 通过操作时间进行倒叙
	 */
	private final static String ORDERBY = "updateTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CeqFlowManageService infoService;
	@Autowired
	private CsvExportGetterImpl csvExportGetterImpl;

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
	 * <搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";
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
	 * 分页列表
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqQueryRecordBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);

			if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
				List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes,",");
				searchParams.put("IN_operOrg", deptCodeStr);
			}
			
			queryResults = infoService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("CeqResultinfo list method e = " , e);
			queryResults = new PageImpl<>(new ArrayList<CeqQueryRecordBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}

	/**
	 * <通过id进行查询>
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CeqQueryRecordBo resultInfo = infoService.findCeqQueryRecordBoById(id);
		QueryEncryptUtil.QueryEncrypt(resultInfo);
		return JsonUtil.toJSONString(resultInfo, DATE_FORMAT);
	}

	/**
	 * <查询信用报告记录>
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
//	@RequestMapping("/findByCreditReport")
//	@ResponseBody
//	public String findByCreditReport(String creditId) throws Exception {
//		String html = localService.getHtmlPage(creditId);
//		String jsonString = JsonUtil.toJSONString(html);
//		return jsonString;
//		
//	}


	/**
	 * <导出>
	 * 
	 * @param ids
	 * @param model
	 * @param request
	 * @param page
	 * @param response
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/exportLoad")
	@ResponseBody
	public HttpServletResponse exportLoad(@RequestParam List<String> ids, PageBean page, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 如果他为空，则获取所有符合查询条件的数据并导出
		HttpServletResponse download = null;
		File csvFile = null;
		CsvWriter writer = null;
		try {
			String time = SysDate.getFullDate();
			String userName = UserUtilsForConfig.getUserInfo(request).get("username").trim();
			String filePath = CeqConfigUtil.getTempPath();
			String path = filePath + File.separator + RESULTINFO;
			String filename = userName + time + ".csv";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			csvFile = new File(file, filename);
			CsvConfig csvConfig = new CsvConfig();
			csvConfig.setCsvExportGetter(csvExportGetterImpl);
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"));
			writer = CsvUtil.getWriter(bufferedWriter, csvConfig);
			if (null == ids ||ids.size()==0) {
				// 通过baseController的方法获取页面的查询条件
				Map<String, Object> searchParams = processRequestParams(page, request);
				if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
					List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
					String deptCodeStr = StringUtils.join(deptCodes,",");
					searchParams.put("IN_operOrg", deptCodeStr);
				}
				List<CeqQueryRecordBo> resultInfo = infoService.findAll(searchParams);
				QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
				//README  20180529   经要求，导出时要在中征码、机构信用码、机构字段值前拼接 ' 号。
				for (CeqQueryRecordBo ceqResultinfo : resultInfo) {
					String signCode = ceqResultinfo.getSignCode();
                	if(StringUtils.isNotBlank(signCode)) {
                		ceqResultinfo.setSignCode("'" + signCode);
                	}
                    String uniformSocialCredCode = ceqResultinfo.getUniformSocialCredCode();
                    if(StringUtils.isNotBlank(uniformSocialCredCode)) {
                    	ceqResultinfo.setUniformSocialCredCode("'" + uniformSocialCredCode);
                    }
                    String rekOrg = ceqResultinfo.getRekOrg();
                    if(StringUtils.isNotBlank(rekOrg)) {
                    	ceqResultinfo.setRekOrg("'" + rekOrg);
                    }
                    String operOrg = ceqResultinfo.getOperOrg();
                    if(StringUtils.isNotBlank(operOrg)) {
                    	ceqResultinfo.setOperOrg("'"+operOrg);
                    }
                    String queryOrg = ceqResultinfo.getQueryOrg();
                    if(StringUtils.isNotBlank(queryOrg)) {
                    	ceqResultinfo.setQueryOrg("'" + queryOrg);
                    }
				}
				writer.write(resultInfo);
			} else {
				List<CeqQueryRecordBo> resultInfo = infoService.findByIds(ids);
				QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
				//README  20180529   经要求，导出时要在中征码、机构信用码、机构字段值前拼接 ' 号。
				for (CeqQueryRecordBo ceqResultinfo : resultInfo) {
					ceqResultinfo.setSignCode("'"+ceqResultinfo.getSignCode());
					ceqResultinfo.setUniformSocialCredCode("'"+ceqResultinfo.getUniformSocialCredCode());
					ceqResultinfo.setRekOrg("'"+ceqResultinfo.getRekOrg());
					ceqResultinfo.setOperOrg("'"+ceqResultinfo.getOperOrg());
					ceqResultinfo.setQueryOrg("'"+ceqResultinfo.getQueryOrg());
				}
				writer.write(resultInfo);
			}
			download = FileUtil.download(csvFile, response);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			writer.close();
			if (csvFile != null) {
				csvFile.delete();
			}
		}
		return download;
	}
}