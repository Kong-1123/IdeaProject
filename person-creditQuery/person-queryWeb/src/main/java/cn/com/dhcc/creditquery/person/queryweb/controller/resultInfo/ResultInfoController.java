package cn.com.dhcc.creditquery.person.queryweb.controller.resultInfo;

import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.CsvExportGetterImpl;
import cn.com.dhcc.creditquery.person.queryweb.util.QueryEncryptUtil;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvUtil;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.*;

/**
 * <查询记录管理----控制层>
 *
 * @author Mingyu.Li
 * @date 2018年3月10日
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
    private CsvExportGetterImpl csvExportGetterImpl;

    @Autowired
    private CpqFlowManageService cpqFlowManageService;

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
    public String list(Model model, PageBean page, HttpServletRequest request, String search_GTE_LTE_queryTime_DATE) {
        Page<CpqQueryRecordBo> queryResults = null;
        String orgCode = UserConfigUtils.getDeptCode(request);
        try {
            Map<String, Object> searchParams = processRequestParams(page, request);
            if (StringUtils.isNotBlank(search_GTE_LTE_queryTime_DATE)) {
                String[] split = StringUtils.split(search_GTE_LTE_queryTime_DATE);
                searchParams.remove("GTE_LTE_queryTime");
                searchParams.put("GTE_queryTime", SysDate.getDate(split[0]));
                Date LTE_queryTime = SysDate.getDate(split[2]);
                Date Today = getToday(LTE_queryTime);
                searchParams.put("LTE_queryTime", Today);
            }
            if (StringUtils.isBlank((String) searchParams.get("IN_operOrg"))) {
                List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
                String deptCodeStr = StringUtils.join(deptCodes, ",");
                searchParams.put("IN_operOrg", deptCodeStr);
            }
            queryResults = cpqFlowManageService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
            log.debug("page:" + page);
        } catch (Exception e) {
            log.error("archive list method e=", e);
            queryResults = new PageImpl<>(new ArrayList<CpqQueryRecordBo>());
        }
        page = processQueryResults(model, page, queryResults);
        String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
        return jsonString;
    }

    /**
     * <赋值一天最大时间>
     *
     * @param lTE_queryTime
     * @return void
     * @author Mingyu.Li
     * @date 2018年8月9日
     */
    private Date getToday(Date lTE_queryTime) {
        Calendar calendar = DateUtils.toCalendar(lTE_queryTime);
        calendar.set(Calendar.HOUR_OF_DAY,
                calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,
                calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,
                calendar.getActualMaximum(Calendar.SECOND));
        return calendar.getTime();
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
        CpqQueryRecordBo resultInfo = cpqFlowManageService.findCpqQueryRecordBoById(id);
        QueryEncryptUtil.QueryEncrypt(resultInfo);
        return JsonUtil.toJSONString(resultInfo, DATE_FORMAT);
    }

    /**
     * <查询信用报告记录>
     *
     * @param creditId
     * @return
     */
    @RequestMapping("/findByCreditReport")
    @ResponseBody
    public String findByCreditReport(String creditId) throws Exception {
        String html = cpqFlowManageService.getReportById(creditId,CpqFlowManageService.REPORT_FORMAT_HTML);
        String jsonString = JsonUtil.toJSONString(html);
        return jsonString;

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
            String filePath = ConfigUtil.getTempPath();
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
                if (StringUtils.isBlank((String) searchParams.get("IN_operOrg"))) {
                    List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
                    String deptCodeStr = StringUtils.join(deptCodes, ",");
                    searchParams.put("IN_operOrg", deptCodeStr);
                }
                List<CpqQueryRecordBo> resultInfo = cpqFlowManageService.findAll(searchParams);
                //README  20180529   经要求，要在证件号码、机构字段值前拼接 ' 号。
                QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
                for (CpqQueryRecordBo cpqResultinfo : resultInfo) {
                	String certNo = cpqResultinfo.getCertNo();
                	if(StringUtils.isNotBlank(certNo)) {
                		cpqResultinfo.setCertNo("'" + certNo);
                	}
                    String operOrg = cpqResultinfo.getOperOrg();
                    if(StringUtils.isNotBlank(operOrg)) {
                    	cpqResultinfo.setOperOrg("'" + operOrg);
                    }
                    String rekOrg = cpqResultinfo.getRekOrg();
                    if(StringUtils.isNotBlank(rekOrg)) {
                    	cpqResultinfo.setRekOrg("'" + rekOrg);
                    }
                    String queryOrg = cpqResultinfo.getQueryOrg();
                    if(StringUtils.isNotBlank(queryOrg)) {
                    	cpqResultinfo.setQueryOrg("'" + queryOrg);
                    }
                }
                writer.write(resultInfo);
            } else {
                List<CpqQueryRecordBo> resultInfo = cpqFlowManageService.findByIds(ids);
                //README  20180529   经要求，要在证件号码、机构字段值前拼接 ' 号。
                QueryEncryptUtil.resultinfoQueryEncrypt(resultInfo);
                for (CpqQueryRecordBo cpqResultinfo : resultInfo) {
                	String certNo = cpqResultinfo.getCertNo();
                	if(StringUtils.isNotBlank(certNo)) {
                		cpqResultinfo.setCertNo("'" + certNo);
                	}
                    String operOrg = cpqResultinfo.getOperOrg();
                    if(StringUtils.isNotBlank(operOrg)) {
                    	cpqResultinfo.setOperOrg("'" + operOrg);
                    }
                    String rekOrg = cpqResultinfo.getRekOrg();
                    if(StringUtils.isNotBlank(rekOrg)) {
                    	cpqResultinfo.setRekOrg("'" + rekOrg);
                    }
                    String queryOrg = cpqResultinfo.getQueryOrg();
                    if(StringUtils.isNotBlank(queryOrg)) {
                    	cpqResultinfo.setQueryOrg("'" + queryOrg);
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