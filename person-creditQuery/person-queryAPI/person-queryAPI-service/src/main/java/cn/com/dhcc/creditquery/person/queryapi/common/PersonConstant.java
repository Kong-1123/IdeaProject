/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.common;

/**
 * 个人处理结果代码、处理信息
 * @author guoshihu
 * @date 2019年1月16日
 */
public class PersonConstant {
	/**
	 * 违规查询进行阻断code
	 */
	public static String CHECK_USER_VIOLATE = "10999";
	/**
	 * 违规查询进行阻断msg
	 */
	public static String CHECK_USER_VIOLATE_MSG = "违规查询进行阻断";
	
	
	public static String CODE_SUSSCE = "000000";
	public static String MSG_SUSSCE = "查询成功";
	public static String CODE_REFUSE = "000001";
	public static String MSG_REFUSE = "查询拒绝，鉴权失败，非法访问,请对访问的ip做权限配置";
	public static String CODE_ERROR = "000002";
	public static String MSG_ERROR = "请求参数错误，参数不合法";
	public static String CODE_QUERY_FAILED = "000003";
	public static String MSG_QUERY_FAILED = "查询失败";
	public static String CODE_ANALYSIS_FAILED = "000004";
	public static String MSG_ANALYSIS_FAILED = "解析失败";
	public static String CODE_SYS_EXCEPTION = "000005";
	public static String MSG_SYS_EXCEPTION = "系统异常";
	public static String CODE_CREDIT_EXCEPTION = "000006";
	public static String MSG_CREDIT_EXCEPTION = "征信中心系统异常，无法查询";
    public static String CODE_NOT_TRACE_REPORT = "000007";
    public static String MSG_NOT_TRACE_REPORT  = "查无此人";
    public static String CODE_WAIT_QUERY = "000008";
    public static String MSG_WAIT_QUERY = "暂时还无结果，请5分钟后再查询结果";
    public static String CODE_ALLOW_PRINT = "000009";
    public static String MSG_ALLOW_PRINT = "允许打印";
    public static String CODE_NOTALLOW_PRINT = "000010";
    public static String MSG_NOTALLOW_PRINT = "不允许打印";
    public static String CODE_ALLOW_SAVE = "000011";
    public static String MSG_ALLOW_SAVE = "允许保存";
    public static String CODE_NOTALLOW_SAVE = "000012";
    public static String MSG_NOTALLOW_SAVE = "不允许保存";
    public static String CODE_ALLOW_LOOK = "000013";
    public static String MSG_ALLOW_LOOK = "允许查看";
    public static String CODE_NOTALLOW_LOOK = "000014";
    public static String MSG_NOTALLOW_LOOK = "不允许查看";
    public static String CODE_WAIT_EXAMINE = "000015";
    public static String MSG_WAIT_EXAMINE = "查询请求待审批";
    public static String CODE_REQUEST_WAIT_QUERY = "000016";
    public static String MSG_REQUEST_WAIT_QUERY = "查询请求待查询";
    public static String CODE_EXAMINE_RESUSE = "000017";
    public static String MSG_EXAMINE_RESUSE = "查询请求审批拒绝";
    public static String CODE_NO_QUERYUSER = "000018";
    public static String MSG_NO_QUERYUSER = "查询用户不存在";
    public static String CODE_NO_MAPPING_CREDITUSER = "000019";
    public static String MSG_NO_MAPPING_CREDITUSER = "查询用户未映射征信用户";
    public static String CODE_NO_CREDITUSER = "000020";
    public static String MSG_NO_CREDITUSER = "映射的征信用户不存在";
    public static String CODE_LOCAL_NO_REPORT = "000021";
    public static String MSG_LOCAL_NO_REPORT = "本地无报告";
   
    
    
    public static String CODE_QUERYREASON_VALI_ERROR = "DBE001";
    public static String MSG_QUERYREASON_VALI_ERROR = "查询原因校验错误";
    public static String CODE_PERSONNAME_VALI_ERROR = "DBE002";
    public static String MSG_PERSONNAME_VALI_ERROR = "信息主体名称校验错误";
    public static String CODE_PERSONCERTYPE_VALI_ERROR = "DBE003";
    public static String MSG_PERSONCERTYPE_VALI_ERROR = "信息主体证件类型校验错误";
    public static String CODE_PERSONCERTNO_VALI_ERROR = "DBE004";
    public static String MSG_PERSONCERTNO_VALI_ERROR = "信息主体证件号码校验错误";
    public static String CODE_ENTERNAME_VALI_ERROR = "DBE005";
    public static String MSG_ENTERNAME_VALI_ERROR = "企业名称校验错误";
    public static String CODE_ENTERCERTYPE_VALI_ERROR = "DBE006";
    public static String MSG_ENTERCERTYPE_VALI_ERROR = "企业身份标识类型校验错误";
    public static String CODE_ENTERCERTNO_VALI_ERROR = "DBE007";
    public static String MSG_ENTERCERTNO_VALI_ERROR = "企业身份标识号码校验错误";
    public static String CODE_QUERYORG_VALI_ERROR = "DBE008";
    public static String MSG_QUERYORG_VALI_ERROR = "查询机构校验错误";
    public static String CODE_STARTORG_VALI_ERROR = "DBE009";
    public static String MSG_STARTORG_VALI_ERROR = "发起机构校验错误";
    public static String CODE_QUERYORG_NO_SERVE = "DBE010";
    public static String MSG_QUERYORG_NO_SERVE = "查询机构与中心未签署服务协议";
    public static String CODE_QUERYORG_SERVE_NOT_APPLICABLE = "DBE011";
    public static String MSG_QUERYORG_SERVE_NOT_APPLICABLE = "查询机构与中心签署服务协议不适用本次查询";
    public static String CODE_STARTORG_NO_SERVE = "DBE012";
    public static String MSG_STARTORG_NO_SERVE = "发起机构与中心未签署服务协议";
    public static String CODE_STARTORG_SERVE_NOT_APPLICABLE = "DBE013";
    public static String MSG_STARTORG_SERVE_NOT_APPLICABLE = "发起机构与中心签署服务协议不适用本次查询";
    public static String CODE_SERVECODE_VALI_ERROR = "DBE014";
    public static String MSG_SERVECODE_VALI_ERROR = "服务代码校验错误";
    public static String CODE_QUERYUSER_VALI_ERROR = "DBE015";
    public static String MSG_QUERYUSER_VALI_ERROR = "查询用户代码校验错误";
    public static String CODE_STARTUSER_VALI_ERROR = "DBE016";
    public static String MSG_STARTUSER_VALI_ERROR = "发起用户代码校验错误";
    public static String CODE_MESSAGE_VALI_ERROR = "DBE017";
    public static String MSG_MESSAGE_VALI_ERROR = "信息记录标识号校验错误";
    
    
    
    public static String CODE_SENDORG_NOT_QUERYORG = "DBO001";
    public static String MSG_SENDORG_NOT_QUERYORG = "报文发送机构代码与查询机构代码不一致";
    public static String CODE_ORGRELATION_VALI_ERROR = "DBO002";
    public static String MSG_ORGRELATION_VALI_ERROR = "机构代理关系校验错误";
    public static String CODE_REQUEST_FILENUMBER = "DBO003";
    public static String MSG_REQUEST_FILENUMBER = "批量查询请求文件记录数超过2000条";
    public static String CODE_REPORT_OVERLENGTH = "DBO004";
    public static String MSG_REPORT_OVERLENGTH = "信用报告超长，仅提供部分信息";
    /**
     * ip配置表启用标识
     */
    public static String STOP_FLAG = "1";
    
}
