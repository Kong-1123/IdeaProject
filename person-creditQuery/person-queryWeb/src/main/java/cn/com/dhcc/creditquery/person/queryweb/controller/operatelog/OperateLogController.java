package cn.com.dhcc.creditquery.person.queryweb.controller.operatelog;

import java.util.ArrayList;
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

import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.businessmonitor.service.CpqOperateLogService;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqOperateLogBo;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/operatelog")
public class OperateLogController extends BaseController{

    private static final String PREFIX = "operatelog/";
    private final static String DIRECTION = "desc";
    private final static String ORDERBY = "createTime";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    
    @Autowired
    private CpqOperateLogService service;
    
    
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
    	log.info("list start : page = {}",page);
        Page<CpqOperateLogBo> queryResults = null;
        try {
            Map<String, Object> searchParams = processRequestParams(page, request);
            if(StringUtils.isBlank((String)searchParams.get("IN_operOrg"))){
				List<String> deptCodes = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCode = StringUtils.join(deptCodes,",");
				searchParams.put("IN_operOrg", deptCode);
			}
            queryResults = service.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
            log.debug("page:" + page);
        } catch (Exception e) {
            log.error("log list method error:" ,e);
            queryResults = new PageImpl<>(new ArrayList<CpqOperateLogBo>());
        }
        page = processQueryResults(model, page, queryResults);
        String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
    	log.info("list end : jsonString = {}",jsonString);
        return jsonString;
    }
}

