package cn.com.dhcc.creditquery.ent.queryweb.controller.queyreq;

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
import cn.com.dhcc.creditquery.ent.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.InConstant;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvUtil;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/queryreq")
public class QueryReqController extends BaseController {

	private static final String PREFIX = "queryreq/";

	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "updateTime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CeqFlowManageService ceqFlowManageService;
	
    @Autowired
    private CsvExportGetterImpl csvExportGetterImpl;
    
    private final static String RESULTINFO = "resultinfo";
	
	
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
			Map<String, String> userInfo = UserUtilsForConfig.getUserInfo(request);
			String username = userInfo.get("username").trim();
			String orgCode = UserUtilsForConfig.getDeptCode(request);
			Map<String, Object> searchParams = processRequestParams(page, request);
			searchParams.put("EQ_operator", username);
			searchParams.put("EQ_operOrg", orgCode);
			queryResults = ceqFlowManageService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("Query list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqQueryRecordBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}
	
	
	/**
	 * <档案补录分页列表>
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getList")
	@ResponseBody
	public String getPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CeqQueryRecordBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String qryReason = "01";
			searchParams.put("NE_qryReason", qryReason);
			searchParams.put("ISNULL_autharchiveId", "1");
			if(StringUtils.isBlank((String) searchParams.get("IN_operOrg"))){
				List<String> deptCode = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCode,",");
				searchParams.put("IN_operOrg", deptCodeStr);
			}
			queryResults = ceqFlowManageService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("Query list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CeqQueryRecordBo>());
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
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CeqQueryRecordBo queryReq = ceqFlowManageService.findCeqQueryRecordBoById(id);
		QueryEncryptUtil.QueryEncrypt(queryReq);
		return JsonUtil.toJSONString(queryReq, DATE_FORMAT);
	}



	@RequestMapping("/getStatus")
	@ResponseBody
	public String getStatus(String id) throws Exception {
		ResultBeans rs = null;
		try {
			CeqQueryRecordBo resultinfo = ceqFlowManageService.findCeqQueryRecordBoById(id);
			if (!InConstant.QUERY_FAILURE.equals(resultinfo.getStatus())) {
				rs = new ResultBeans(Constants.SUCCESSCODE, Constants.SUCCESSMSG);
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}
	
	
	  /**
     * <导出>
     *
     * @param ids
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
            String userName = UserConfigUtils.getUserInfo(request).get("username").trim();
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
            if (null == ids || ids.size() == 0) {
                // 通过baseController的方法获取页面的查询条件
                Map<String, Object> searchParams = processRequestParams(page, request);
                if (StringUtils.isBlank((String) searchParams.get("EQ_operator"))) {
                	String useName = UserUtilsForConfig.getUserName(request);
                    searchParams.put("EQ_operator", useName);
                }
                List<CeqQueryRecordBo> resultInfo = ceqFlowManageService.findAll(searchParams);
                //README  20180529   经要求，要在证件号码、机构字段值前拼接 ' 号。
                QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
                for (CeqQueryRecordBo ceqResultinfo : resultInfo) {
                	String signCode = ceqResultinfo.getSignCode();
                	if(StringUtils.isNotBlank(signCode)) {
                		ceqResultinfo.setSignCode("'" + signCode);
                	}
                    String operOrg = ceqResultinfo.getOperOrg();
                    if(StringUtils.isNotBlank(operOrg)) {
                    	ceqResultinfo.setOperOrg("'" + operOrg);
                    }
                    String rekOrg = ceqResultinfo.getRekOrg();
                    if(StringUtils.isNotBlank(rekOrg)) {
                    	ceqResultinfo.setRekOrg("'" + rekOrg);
                    }
                    String queryOrg = ceqResultinfo.getQueryOrg();
                    if(StringUtils.isNotBlank(queryOrg)) {
                    	ceqResultinfo.setQueryOrg("'" + queryOrg);
                    }
                }
                writer.write(resultInfo);
            } else {
                List<CeqQueryRecordBo> resultInfo = ceqFlowManageService.findByIds(ids);
                //README  20180529   经要求，要在证件号码、机构字段值前拼接 ' 号。
                QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
                for (CeqQueryRecordBo ceqResultinfo : resultInfo) {
                	String signCode = ceqResultinfo.getSignCode();
                	if(StringUtils.isNotBlank(signCode)) {
                		ceqResultinfo.setSignCode("'" + signCode);
                	}
                    String operOrg = ceqResultinfo.getOperOrg();
                    if(StringUtils.isNotBlank(operOrg)) {
                    	ceqResultinfo.setOperOrg("'" + operOrg);
                    }
                    String rekOrg = ceqResultinfo.getRekOrg();
                    if(StringUtils.isNotBlank(rekOrg)) {
                    	ceqResultinfo.setRekOrg("'" + rekOrg);
                    }
                    String queryOrg = ceqResultinfo.getQueryOrg();
                    if(StringUtils.isNotBlank(queryOrg)) {
                    	ceqResultinfo.setQueryOrg("'" + queryOrg);
                    }
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
