package cn.com.dhcc.creditquery.person.queryweb.controller.dateinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.dhcc.creditquery.person.queryconfig.service.CpqWorkDateCfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.credit.platform.util.Constants;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.creditquery.person.query.bo.queryconfig.WorkdateCfgBo;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.vo.DateInfoVo;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lekang.liu
 * @date 2018年3月21日
 *
 */
@Slf4j
@Controller
@RequestMapping(value = "/dateinfo")
public class DateInfoController extends BaseController {

	private static final String PREFIX = "dateinfo/";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private CpqWorkDateCfgService cpqWorkDateCfgService;

	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "dateinfo";
	}

	@RequestMapping("/getDate")
	@ResponseBody
	public String getDate(String year, String month,HttpServletRequest request) {
		List<WorkdateCfgBo> dateInfos = null;
		try {
			String parm = year + "-" + month + "%%";
			 String topOrgCode = UserConfigUtils.getRootDeptCode(request);
			dateInfos = (List<WorkdateCfgBo>) cpqWorkDateCfgService.findWorkdateCfgsByYearMonthAndOrgId(parm,topOrgCode);
		} catch (Exception e) {
			log.error("dateInfo getDate method :" + e.getMessage());
		}
		String jsonString = JsonUtil.toJSONString(dateInfos, DATE_FORMAT);
		return jsonString;
	}

	/**
	 * 保存工作日修改信息
	 * @param request
	 * @param dateInfoVo
	 * @return
	 */
	@RequestMapping("/saveDate")
	@ResponseBody
	@LogOperation("工作日修改")
	public String saveDate(HttpServletRequest request, @RequestBody List<DateInfoVo> dateInfoVo) {
		ResultBeans rs = null;
		String topOrgCode = UserConfigUtils.getRootDeptCode(request);
		List<WorkdateCfgBo> dateInfoListAdd = new ArrayList<WorkdateCfgBo>();
		List<WorkdateCfgBo> dateInfoListDel = new ArrayList<WorkdateCfgBo>();
		try {
			for (DateInfoVo dateInfoVo2 : dateInfoVo) {
				WorkdateCfgBo dateinfo = null;
				WorkdateCfgBo cpqDateinfo = cpqWorkDateCfgService.findWorkdateCfgByDateIdAndOrgId(dateInfoVo2.getYmd(),topOrgCode);
				if (null != cpqDateinfo) {
					cpqDateinfo.setDateId(dateInfoVo2.getYmd());
					cpqDateinfo.setStatus(dateInfoVo2.getIsWorkDay());
					cpqDateinfo.setUpdateDate(new Date());
					cpqDateinfo.setUpdateUser(getUserInfo(request, "username"));
					cpqDateinfo.setOrgId(topOrgCode);
					dateInfoListDel.add(cpqDateinfo);
				} else {
					dateinfo = new WorkdateCfgBo();
					dateinfo.setDateId(dateInfoVo2.getYmd());
					dateinfo.setStatus(dateInfoVo2.getIsWorkDay());
					dateinfo.setUpdateDate(new Date());
					dateinfo.setUpdateUser(getUserInfo(request, "username"));
					dateinfo.setOrgId(topOrgCode);
					dateInfoListAdd.add(dateinfo);
				}
			}
			cpqWorkDateCfgService.create(dateInfoListAdd);
			cpqWorkDateCfgService.delete(dateInfoListDel);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error(" saveDate method :" + e.getMessage());
		}
		return rs.toJSONStr();
	}

	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserConfigUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}

}
