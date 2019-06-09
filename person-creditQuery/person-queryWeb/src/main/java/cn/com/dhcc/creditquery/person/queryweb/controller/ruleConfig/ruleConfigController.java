/*
package cn.com.dhcc.creditquery.person.queryweb.controller.ruleConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.query.creditpersonqueryservice.config.service.CpqConfigService;
import cn.com.dhcc.query.creditpersonqueryservice.util.ExcelUtil;
import cn.com.dhcc.query.creditpersonqueryservice.vo.RuleConfig;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigConstants;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ruleConfig")
public class ruleConfigController  extends BaseController{
	 private static RedissonClient redisson = RedissonUtil.getLocalRedisson();

	 private static final String PREFIX = "ruleConfig/";
	 
	 private static final String REDISKEY = "RULECONFIG_KEY";
	 
	 List<RuleConfig> list;
	 
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
				list = (List<RuleConfig>) bucket.get();
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
		
		 public static List<RuleConfig> getSortList(List<RuleConfig> list){
		        Collections.sort(list, new Comparator<RuleConfig>() {
		            @Override
		            public int compare(RuleConfig o1, RuleConfig o2) {
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
		public String create(Model model, HttpServletRequest request, RuleConfig ruleConfig) {
			ResultBeans rs = null;
			int IdIndext = 0;
			try {
				Map<String, String> info = UserUtils.getUserInfo(request);
				String userId = info.get("username").trim();
				ruleConfig.setOperater(userId);
				RBucket<Object> bucket =redisson.getBucket(REDISKEY);
				list = (List<RuleConfig>) bucket.get();
				if(null == list){
					list=new ArrayList<RuleConfig>();
				}else{
					for (int i = 0; i < list.size(); i++) {
						int ID = Integer.parseInt(list.get(i).getId());
						IdIndext =IdIndext+ ID +1;
					}
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
			RuleConfig ruleConfig = null;
			list = (List<RuleConfig>) bucket.get();
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
		public String update(RuleConfig ruleConfig) {
			ResultBeans rs = null;
			RBucket<Object> bucket =redisson.getBucket(REDISKEY);
			try{
			list = (List<RuleConfig>) bucket.get();
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
			list = (List<RuleConfig>) bucket.get();
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
			List<RuleConfig> queryResults = new ArrayList<RuleConfig>();
			String filePath = service.getConfigValueByName(ConfigConstants.EXPORTPATH);
			try {
				   RBucket<Object> bucket =redisson.getBucket(REDISKEY);
				   List<String> idList = Arrays.asList(ids.split("\\,"));
				   list = (List<RuleConfig>) bucket.get();
					for (int i = 0; i < list.size(); i++) {
						if(idList.contains(list.get(i).getId())){
							queryResults.add(list.get(i));
						}
					}
				
				ExcelUtil.initExcel().write(queryResults, RuleConfig.class, filePath, "RuleConfig.xls", true, true);
			} catch (Exception e) {
			}
			File file = new File(filePath + File.separator + "RuleConfig.xls");
			return FileUtil.download(file, response);
		}
		
	
		
}
*/
