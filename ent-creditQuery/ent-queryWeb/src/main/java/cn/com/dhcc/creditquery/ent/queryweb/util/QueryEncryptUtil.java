/**
 * 
 */
package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.reportview.CeqReportLogBo;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.ent.queryweb.controller.archive.ArchiveController;
import cn.com.dhcc.creditquery.ent.queryweb.controller.checkinfo.CheckInfoController;
import cn.com.dhcc.creditquery.ent.queryweb.controller.checkinfo.CheckInfoTaskController;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;

/** 
* @Description: 
* @author: wenchaobo
* @date: 2018.10.24 
*/
public class QueryEncryptUtil {
	private static Logger log = LoggerFactory.getLogger(QueryEncryptUtil.class);
	
	/**
	 * @Description: 列表脱敏
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> void QueryEncrypt(BaseController controller,List<T> list){
		String flag = CeqConfigUtil.getSensitiveCryto();
		if(StringUtils.equals(flag, "0")){			
			if(!(CheckInfoController.class.isInstance(controller) || CheckInfoTaskController.class.isInstance(controller) || ArchiveController.class.isInstance(controller))){
				T t = list.get(0);
				if(CeqAuthorizeManagerBo.class.isInstance(t)){
					archiveQueryEncrypt((List<CeqAuthorizeManagerBo>) list);
				}else if (CeqQueryRecordBo.class.isInstance(t)) {
					resultinfoQueryEncrypt((List<CeqQueryRecordBo>) list);
				}else if (CeqReportLogBo.class.isInstance(t)) {
					reportLogQueryEncrypt( (List<CeqReportLogBo>) list);
				}else if (CeqApproveBo.class.isInstance(t)) {
					checkInfoQueryEncrypt( (List<CeqApproveBo>) list);
				}
			}
		}
		
	}
	

	/*
	 * @Description: 明细脱敏
	 * 
	 */
	public static <T> void QueryEncrypt(T t){
		String flag = CeqConfigUtil.getSensitiveCryto();
		if(StringUtils.equals(flag, "0")){	
			
			if(CeqAuthorizeManagerBo.class.isInstance(t)){
				oneEncrypt((CeqAuthorizeManagerBo)t);
			}else if (CeqQueryRecordBo.class.isInstance(t)) {
				oneEncrypt((CeqQueryRecordBo)t);
			}else if (CeqReportLogBo.class.isInstance(t)) {
				oneEncrypt((CeqReportLogBo)t);
			}else if (CeqApproveBo.class.isInstance(t)) {
				oneEncrypt((CeqApproveBo)t);
			}
		}
	}
	
	/**
	 * @Description: 电话号码脱敏
	 * 
	 */
	public static  String  numberEncrypt(String number){
		if(number.length() == 11){
			return QueryEncryptImplUtil.phoneEncrypt(number);
		}else{
			return QueryEncryptImplUtil.telEncrypt(number);	
		}
		
	}
	
	
	public static void archiveQueryEncrypt(List<CeqAuthorizeManagerBo> list){
		for (CeqAuthorizeManagerBo cpqArchive : list) {
			cpqArchive.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(cpqArchive.getSignCode()));
		}
	}
	
	public static void resultinfoQueryEncrypt(List<CeqQueryRecordBo> list){
		for (CeqQueryRecordBo resultinfo : list) {
			resultinfo.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(resultinfo.getSignCode()));
		}
	}
	
	public static void reportLogQueryEncrypt(List<CeqReportLogBo> list){
		for (CeqReportLogBo resultinfo : list) {
			resultinfo.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(resultinfo.getSignCode()));
		}
	}
	
	public static void checkInfoQueryEncrypt(List<CeqApproveBo> list){
		for (CeqApproveBo resultinfo : list) {
			resultinfo.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(resultinfo.getSignCode()));
		}
	}
	/**
	 * 内部逻辑一样，由于get,set方法不一致，所以不能做统一处理
	 * @param object
	 * @return void
	 */
	public static void oneEncrypt(CeqReportLogBo object){
		object.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(object.getSignCode()));
	}
	
	public static void oneEncrypt(CeqApproveBo object){
		object.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(object.getSignCode()));
	}
	
	public static void oneEncrypt(CeqAuthorizeManagerBo object){
		object.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(object.getSignCode()));
	}
	
	public static void oneEncrypt(CeqQueryRecordBo object){
		object.setSignCode(QueryEncryptImplUtil.loancardCodeEncrypt(object.getSignCode()));
	}
	
}
