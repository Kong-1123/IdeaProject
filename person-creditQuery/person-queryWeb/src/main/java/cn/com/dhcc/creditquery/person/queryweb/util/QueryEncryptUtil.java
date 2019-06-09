/**
 * 
 */
package cn.com.dhcc.creditquery.person.queryweb.util;

import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.query.bo.reportview.CpqReportLogBo;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.controller.archive.ArchiveController;
import cn.com.dhcc.creditquery.person.queryweb.controller.checkinfo.CheckInfoController;
import cn.com.dhcc.creditquery.person.queryweb.controller.checkinfo.CheckInfoTaskController;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
		String flag = ConfigUtil.getSensitiveCryto();
		if(StringUtils.equals(flag, "0")){			
			if(!(CheckInfoController.class.isInstance(controller) || CheckInfoTaskController.class.isInstance(controller) || ArchiveController.class.isInstance(controller))){
				T t = list.get(0);
				if(CpqArchiveBo.class.isInstance(t)){
					archiveQueryEncrypt((List<CpqArchiveBo>) list);
				}else if (CpqQueryRecordBo.class.isInstance(t)) {
					resultinfoQueryEncrypt((List<CpqQueryRecordBo>) list);
				}else if (CpqReportLogBo.class.isInstance(t)) {
					reportLogQueryEncrypt( (List<CpqReportLogBo>) list);
				}else if (CpqApproveBo.class.isInstance(t)) {
					checkInfoQueryEncrypt( (List<CpqApproveBo>) list);
				}
			}
		}
		
	}
	

	/*
	 * @Description: 明细脱敏
	 * 
	 */
	public static <T> void QueryEncrypt(T t){
		String flag = ConfigUtil.getSensitiveCryto();
		if(StringUtils.equals(flag, "0")){	
			
			if(CpqArchiveBo.class.isInstance(t)){
				oneEncrypt((CpqArchiveBo)t);
			}else if (CpqQueryRecordBo.class.isInstance(t)) {
				oneEncrypt((CpqQueryRecordBo)t);
			}else if (CpqReportLogBo.class.isInstance(t)) {
				oneEncrypt((CpqReportLogBo)t);
			}else if (CpqApproveBo.class.isInstance(t)) {
				oneEncrypt((CpqApproveBo)t);
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
	
	
	public static void archiveQueryEncrypt(List<CpqArchiveBo> list){
		for (CpqArchiveBo cpqArchive : list) {
			if(whetherIdCard(cpqArchive.getCretType()) ){
			cpqArchive.setCretNo(QueryEncryptImplUtil.idEncrypt(cpqArchive.getCretNo()));
			}else{
			cpqArchive.setCretNo(QueryEncryptImplUtil.elseidEncrypt(cpqArchive.getCretNo()));	
			}
		}
	}
	
	public static void resultinfoQueryEncrypt(List<CpqQueryRecordBo> list){
		for (CpqQueryRecordBo resultinfo : list) {
			if( whetherIdCard(resultinfo.getCertType())){
				resultinfo.setCertNo(QueryEncryptImplUtil.idEncrypt(resultinfo.getCertNo()));
			}else{
				resultinfo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(resultinfo.getCertNo()));
			}
		}
	}
	
	public static void reportLogQueryEncrypt(List<CpqReportLogBo> list){
		for (CpqReportLogBo reportLog : list) {
			if(whetherIdCard(reportLog.getCertType())){
				reportLog.setCertNo(QueryEncryptImplUtil.idEncrypt(reportLog.getCertNo()));
			}else{
				reportLog.setCertNo(QueryEncryptImplUtil.elseidEncrypt(reportLog.getCertNo()));
			}
		}
	}
	
	public static void checkInfoQueryEncrypt(List<CpqApproveBo> list){
		for (CpqApproveBo checkInfo : list) {
			if(whetherIdCard(checkInfo.getCertType())){
				checkInfo.setCertNo(QueryEncryptImplUtil.idEncrypt(checkInfo.getCertNo()));
			}else{
				checkInfo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(checkInfo.getCertNo()));
			}
		}
	}
	/**
	 * 内部逻辑一样，由于get,set方法不一致，所以不能做统一处理
	 * @param object
	 * @return void
	 */
	public static void oneEncrypt(CpqReportLogBo object){
		if(whetherIdCard(object.getCertType())){
			object.setCertNo(QueryEncryptImplUtil.idEncrypt(object.getCertNo()));
		}else{
			object.setCertNo(QueryEncryptImplUtil.elseidEncrypt(object.getCertNo()));
		}
	}
	
	public static void oneEncrypt(CpqApproveBo object){
		if(whetherIdCard(object.getCertType())){
			object.setCertNo(QueryEncryptImplUtil.idEncrypt(object.getCertNo()));
		}else{
			object.setCertNo(QueryEncryptImplUtil.elseidEncrypt(object.getCertNo()));	
		}
	}
	
	public static void oneEncrypt(CpqArchiveBo object){
		if(whetherIdCard(object.getCretType())){
			object.setCretNo(QueryEncryptImplUtil.idEncrypt(object.getCretNo()));
		}else{
			object.setCretNo(QueryEncryptImplUtil.elseidEncrypt(object.getCretNo()));
		}
	}
	
	public static void oneEncrypt(CpqQueryRecordBo object){
		if(whetherIdCard(object.getCertType())){
			object.setCertNo(QueryEncryptImplUtil.idEncrypt(object.getCertNo()));
		}else{
			object.setCertNo(QueryEncryptImplUtil.elseidEncrypt(object.getCertNo()));
		}
	}
	
	/**
	 * 证件类型为0和7的是身份证和临时身份证，统一脱敏规则
	 * @param type
	 * @return
	 * @return boolean
	 */
	private static boolean whetherIdCard(String type){
		return type.equals("10")?true:false;
	}
}
