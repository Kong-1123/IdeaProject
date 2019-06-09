package cn.com.dhcc.creditquery.person.queryweb.controller.alter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
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
import cn.com.dhcc.credit.platform.util.NetworkUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqAlertBo;
import cn.com.dhcc.creditquery.person.querycontrol.service.alert.CpqAlertService;
import cn.com.dhcc.creditquery.person.querycounter.service.CpqAlertCounterService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.CsvExportGetterImpl;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvUtil;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * <预警信息-控制层>
 * 
 * @author Mingyu.Li
 * @date 2018年3月10日
 * 
 */

@Slf4j
@Controller
@RequestMapping(value = "/alert")
public class AlertController extends BaseController {

	private static final String PREFIX = "alert/";
	private String search_EQ_aleratReason;
	private final static String DIRECTION = "desc";

	private static final String CHECKSTATUS = "checkStatus";
	/**
	 * 核实状态为0-未核实
	 */
	private static final String CHECKSTATUSDATA0 = "0";
	/**
	 * 通过操作时间进行倒叙
	 */
	private final static String ORDERBY = "alertDate";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String UNLOCK = "0"; // 用户未锁定
	private static final String LOCKED = "1"; // 用户已锁定
	private static final String PASS_MSG = "OK"; // 用户未锁定信息
	private static final String CHECKED = "1"; // 已核实状态

	@Autowired
	private CpqAlertService alterService;

	@Autowired
	private CsvExportGetterImpl csvExportGetterImpl;
	@Autowired
	CpqAlertCounterService alertCountService;

	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list(String search_EQ_aleratReason) {
		this.search_EQ_aleratReason = search_EQ_aleratReason;
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
	 * <跳转到解除预警信息页面>
	 * 
	 * @return
	 */
	@RequestMapping("/relieveReason")
	public String relieveReason() {
		return PREFIX + "relieveReason";

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
		Page<CpqAlertBo> queryResults = null;
		Date endTime = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			if(StringUtils.isBlank((String)searchParams.get("IN_orgCode"))){
				List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes,",");
				searchParams.put("IN_orgCode", deptCodeStr);
			}

			if(!searchParams.containsKey("EQ_aleratReason")){
				searchParams.put("EQ_aleratReason", search_EQ_aleratReason);
			}
			if(search_EQ_aleratReason != null){
				searchParams.put("EQ_checkStatus", CHECKSTATUSDATA0);
			}
			queryResults = alterService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY, CHECKSTATUS);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("list error:", e);
			queryResults = new PageImpl<>(new ArrayList<CpqAlertBo>());
		}
		page = processQueryResults(model, page, queryResults);
		return JsonUtil.toJSONString(page, DATE_FORMAT);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CpqAlertBo alter = alterService.findById(id);
		return JsonUtil.toJSONString(alter, DATE_FORMAT);
	}

	/**
	 * <录入解除预警原因>
	 * 
	 * @param id
	 * @param removeAlertReason
	 * @param request
	 * @return
	 */
	@LogOperation("解除预警原因")
	@RequestMapping("/relieve")
	@ResponseBody
	public String relieve(String id, String removeAlertReason, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			CpqAlertBo alter = alterService.findById(id);
			handleRequest(request, alter);
			alter.setRemoveAlertReason(removeAlertReason);
			alter.setCheckStatus(CHECKED);
			alterService.update(alter);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("relieve error:", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 获取操作用户及操作用户IP
	 * 
	 * @param request
	 * @param alter
	 */
	private void handleRequest(HttpServletRequest request, CpqAlertBo alter) {
		String createUser = UserConfigUtils.getUserName(request);
		String ip;
		try {
			ip = NetworkUtil.getIpAddress(request);
			alter.setClientIp(ip);
		} catch (IOException e) {
			log.error("handleRequest error :", e);
		}
		alter.setUpdateDate(SysDate.getSysDate());
		alter.setCreateUser(createUser);
	}

	/**
	 * <解锁用户>
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@LogOperation("解锁用户")
	@RequestMapping("/unLock")
	@ResponseBody
	public String unLock(String id, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			CpqAlertBo alter = alterService.findById(id);
			handleRequest(request, alter);
			alter.setUserStatus(UNLOCK);
			alterService.unlockUser(alter);
			alertCountService.clearAlertCount(alter.getUserName());
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("unLock user error:", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * <锁定用户>
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@LogOperation("锁定用户")
	@RequestMapping("/addLock")
	@ResponseBody
	public String addLock(String id, HttpServletRequest request) {
		ResultBeans rs = null;
		try {
			CpqAlertBo alter = alterService.findById(id);
			handleRequest(request, alter);
			alter.setUserStatus(LOCKED);
			alterService.lockedUser(alter);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("addLock user error:", e);
		}
		return rs.toJSONStr();
	}

	@RequestMapping("/isUserLocked")
	@ResponseBody
	public String isUserLocked(String userName) {
		log.info("isUserLocked userName={}", userName);
		ResultMsg resultMsg = null;
		try {
			CpqAlertBo alert = alterService.findByUserStatus(userName);
			if (null == alert) {
				resultMsg = new ResultMsg(false, PASS_MSG);
			} else {
				String lock_msg = "用户因为个人业务被锁定,请联系系统管理员";
				resultMsg = new ResultMsg(true, lock_msg);
			}
		} catch (Exception e) {
			log.error("isUserLocked error:", e.getMessage());
			resultMsg = new ResultMsg(false, PASS_MSG);
		}
		log.info("isUserLocked result={}", resultMsg);
		return resultMsg.toJSONStr();
	}

	@RequestMapping("/exportLoad")
	@ResponseBody
	public HttpServletResponse exportLoad(@RequestParam List<String> ids, PageBean page, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 如果他为空，则获取所有符合查询条件的数据并导出
		HttpServletResponse download = null;
		File csvFile = null;
		CsvWriter writer = null;
		try {
			String time = SysDate.getFullDate();
			String userName = UserConfigUtils.getUserInfo(request).get("username").trim();
			String filePath = ConfigUtil.getTempPath();
			String path = filePath + File.separator + "alert";
			String filename = userName + time + ".csv";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			csvFile = new File(file, filename);
			CsvConfig csvConfig = new CsvConfig();
			csvConfig.setCsvExportGetter(csvExportGetterImpl);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"));
			writer = CsvUtil.getWriter(bufferedWriter, csvConfig);
			if (ids == null || ids.size() == 0) {
				Map<String, Object> searchParams = processRequestParams(page, request);
				List<CpqAlertBo> cpqalerts = alterService.findAll(searchParams);
				for (CpqAlertBo cpqAlert : cpqalerts) {
					cpqAlert.setOrgCode("'" + cpqAlert.getOrgCode());
				}
				writer.write(cpqalerts);
			} else {
				List<CpqAlertBo> cpqalerts = alterService.findByIds(ids);
				for (CpqAlertBo cpqAlert : cpqalerts) {
					cpqAlert.setOrgCode("'" + cpqAlert.getOrgCode());
				}
				writer.write(cpqalerts);
			}
			download = FileUtil.download(csvFile, response);

		} catch (Exception e) {
			log.error("exportLoad ", e);
		} finally {
			if (null != writer) {
				writer.close();
			}
		}

		return download;
	}
	
	@RequestMapping("/getAlertReason")
	@ResponseBody
	public String getAlertReason() {
		//FIXME  不知道这个是干什么用的，但是这个search_EQ_aleratReason肯定有BUG
		return search_EQ_aleratReason == null?"":search_EQ_aleratReason;
	}
}
