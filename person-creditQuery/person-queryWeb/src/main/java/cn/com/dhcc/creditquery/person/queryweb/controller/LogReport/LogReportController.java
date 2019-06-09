/*
package cn.com.dhcc.creditquery.person.queryweb.controller.LogReport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dhcc.auth.client.util.redis.RedissonUtil;
import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditpersonqueryservice.config.service.CpqConfigService;
import cn.com.dhcc.query.creditpersonqueryservice.util.ExcelUtil;
import cn.com.dhcc.query.creditpersonqueryservice.vo.LogReport;
import cn.com.dhcc.query.creditpersonqueryservice.vo.RuleConfig;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigConstants;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/LogReport")
public class LogReportController {

	 private static RedissonClient redisson = RedissonUtil.getLocalRedisson();

	 private static final String PREFIX = "LogReport/";
	 
	 private static final String REDISKEY = "LOGREPORT_KEY";
	 
	 List<LogReport> list;
	 
	 @Autowired
	 private CpqConfigService  service;
	 
	 
	 */
/**
	     * <分页列表页面>
	     * 
	     * @return
	     *//*

	    @RequestMapping("/list")
	    public String list() {
	        return PREFIX + "list";
	    }
	    
	    
	    */
/**
	     * <跳转修 页面>
	     * 
	     * @return
	     *//*

	    @RequestMapping("/updatePage")
	    public String updatePage() {
	        return PREFIX + "update";
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
		 * <跳转 新增页面>
		 * 
		 * @return
		 *//*

		@RequestMapping("/createPage")
		public String createPage() {
			return PREFIX + "create";
		}
		
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/getPage")
		@ResponseBody
		public String list(Model model, PageBean page, HttpServletRequest request) {
			
			String jsonString = null;
			try {
				RBucket<Object> bucket =redisson.getBucket(REDISKEY);
				list = (List<LogReport>) bucket.get();
				if (null == list || list.size() == 0){
					return null;
				}else{
					
				jsonString = JsonUtil.toJSONString(getSortList(list));
				}
				
			} catch (Exception e) {
				return null;
			}
			return jsonString;
		}
		
		 public static List<LogReport> getSortList(List<LogReport> list){
		        Collections.sort(list, new Comparator<LogReport>() {
		            @Override
		            public int compare(LogReport o1, LogReport o2) {
		                if(o1.getId().compareTo(o2.getId())>0){
		                    return 1;
		                }
		                if(o1.getId().compareTo(o2.getId())==0){
		                    return 0;
		                }
		                return -1;
		            }
		        });
		        return list;
		    }
		
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/create")
		@ResponseBody
		public String create(Model model, HttpServletRequest request, LogReport ruleConfig) {
			ResultBeans rs = null;
			int IdIndext;
			try {
				Map<String, String> info = UserUtils.getUserInfo(request);
				String userId = info.get("username").trim();
				ruleConfig.setOperater(userId);
				RBucket<Object> bucket =redisson.getBucket(REDISKEY);
				list = (List<LogReport>) bucket.get();
				if(null == list){
					IdIndext = 0;
					list=new ArrayList<LogReport>();
				}else{
					IdIndext =list.size();
				}
				ruleConfig.setId(String.valueOf(IdIndext));
				list.add(ruleConfig);
				bucket.set(list);
				rs = new ResultBeans(Constants.SUCCESSCODE, "操作成功！");
			} catch (Exception e) {
				rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
				
			}
			return rs.toJSONStr();
		}
		
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/findCpqQueryRecordBoById")
		@ResponseBody
		public String findById(String id) {
			RBucket<Object> bucket =redisson.getBucket(REDISKEY);
			LogReport ruleConfig = null;
			list = (List<LogReport>) bucket.get();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getId().equals(id)){
					ruleConfig = list.get(i);
				}
			}
			String returnStr = JsonUtil.toJSONString(ruleConfig);
			return returnStr;
		}
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/update")
		@ResponseBody
		public String update(LogReport ruleConfig) {
			ResultBeans rs = null;
			RBucket<Object> bucket =redisson.getBucket(REDISKEY);
			try{
			list = (List<LogReport>) bucket.get();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getId().equals(ruleConfig.getId())){
					list.remove(i);
					list.add(ruleConfig);
					bucket.set(list);
				}
			}
			rs = new ResultBeans(Constants.SUCCESSCODE, "操作成功！");
			}catch(Exception e){
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());	
			}
			return rs.toJSONStr();
		}
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/delete")
		@ResponseBody
		public String delete(String id) {
			ResultBeans rs = null;
			RBucket<Object> bucket =redisson.getBucket(REDISKEY);
			try{
			list = (List<LogReport>) bucket.get();
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getId().equals(id)){
					list.remove(i);
					bucket.set(list);
				}
			}
			rs = new ResultBeans(Constants.SUCCESSCODE, "操作成功！");
			}catch(Exception e){
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());	
			}
			return rs.toJSONStr();
		}
	
		
		@SuppressWarnings("unchecked")
		@RequestMapping("/exportExcel")
		@ResponseBody
		public HttpServletResponse exportExcel(String ids, HttpServletRequest request, HttpServletResponse response) {
			List<LogReport> queryResults = new ArrayList<LogReport>();
			String filePath = service.getConfigValueByName(ConfigConstants.EXPORTPATH);
			try {
				   RBucket<Object> bucket =redisson.getBucket(REDISKEY);
				   List<String> idList = Arrays.asList(ids.split("\\,"));
				   list = (List<LogReport>) bucket.get();
					for (int i = 0; i < list.size(); i++) {
						if(idList.contains(list.get(i).getId())){
							queryResults.add(list.get(i));
						}
					}
				
				ExcelUtil.initExcel().write(queryResults, LogReport.class, filePath, "RuleConfig.xls", true, true);
			} catch (Exception e) {
			}
			File file = new File(filePath + File.separator + "RuleConfig.xls");
			return FileUtil.download(file, response);
		}
}
*/
