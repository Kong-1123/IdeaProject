/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.util;

public class Constants {

	public final static String BUSSMODELEN_FM = "FM";  //查询流程管理服务
	/**
	 * 系统来源
	 */
	public final static String SOURCESYSTEM_QP = "QP"; //个人查询
	public final static String ERRORCODE = "00000001";
	public final static String ERRORMSG = "操作失败！";
	public final static String SUCCESSCODE = "00000000";
	public final static String SUCCESSMSG = "操作成功！";

	//页面访问权限
	public final static String AUTHORITY_ARCHIVE = "/creditpersonqueryweb/archive";
	
	public final static String AUTHORITY_CHECKINFO = "/creditpersonqueryweb/checkinfo";
	
	public final static String AUTHORITY_SINGLEQUERY = "/creditpersonqueryweb/queryreq";

	// 机构配置 启用标志
	public final static String STARTFLAG = "1";

	public final static String RECORDSTOPFLAGStSTART = "1";

	public final static String STOPFLAG = "2";

	// 数据库倒序关键字
	public final static String DIRECTION = "desc";

	// 删除 状态 0:未删除
	public final static String EXISTSTATE = "0";
	// 删除 状态 1:删除
	public final static String DELETESTATE = "1";
	public final static String PAGETYPE_SAVE = "S";
	public final static String PAGETYPE_UPDATE = "U";
	public final static String PAGETYPE_QUERY = "Q";

	// 审批状态
	public final static String WAIT_CHECK = "1";
	public final static String ALREADY_RECEIVED = "2";
	public final static String CHECK_PASS = "3";
	public final static String CHECK_REFUSAL = "4";
	public final static String SELECT_ARCHIVE = "5";
	public final static String INQUIRE_SUCCESS = "6";

	// 审批类型
	public final static String CHECKTYPE_0 = "0";
	public final static String CHECKTYPE_1 = "1";
	
	//已领取
	public final static String RECEIVE = "2";
	//符合通过
	public final static String AUDITPASS = "3";
	
	/**
	 * 查询结果:成功
	 */
	public final static int QUERY_STATUS_SUCCESS = 0;
	/**
	 * 查询结果:失败
	 */
	public final static int QUERY_STATUS_FAIL = 1;
	/**
	 * 查询结果:查无此人
	 */
	public final static int QUERY_STATUS_NO_THIS_ONE = 2;

	/**
	 * 用于判断权限的菜单ID
	 */
	public static interface MenuId{
		/**
		 * 个人信用报告查询
		 */
		String PERSONAL_QUERY = "20030104"; 
		/**
		 * 按用户查询统计
		 */
		String SEARSH_BY_PERSON = "20040101"; 
		/**
		 * 按机构查询统计
		 */
		String SEARSH_BY_ORGAN = "20040102"; 
		/**
		 * 系统管理
		 */
		String SYSTEM = "10000000"; 
		/**
		 * 档案补录
		 */
		String ARCHIVE_REVISE = "20010102";
		/**
		 * 审批管理
		 */
		String RE_CHECK = "20020000";
		/**
		 * 预警信息管理
		 */
		String ALERT = "20060102";
	}
}
