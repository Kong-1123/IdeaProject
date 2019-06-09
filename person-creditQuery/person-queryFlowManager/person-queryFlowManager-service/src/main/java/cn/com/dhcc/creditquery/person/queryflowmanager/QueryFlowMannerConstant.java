/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager;

/**
 * 查询流程控制常量类
 * @Auther: liulekang
 * @Date: 2019/2/22 
 */
public class QueryFlowMannerConstant {

    public static final String HAVE_NORIGHT_INQUIRE= "10198";
    public static final String HAVE_NORIGHT_MSG = "没有查询原因权限！！";

    public static final String GREEN_PASS_CODE= "10399";
    public static final String GREEN_PASS_MSG = "当前用户为绿色通道用户";

    public static final String POLICY_DELEGATION_Y_CODE = "10100";
    public static final String POLICY_DELEGATION_Y_MSG = "本次查询需要进行授权";

    public static final String POLICY_DELEGATION_N_CODE = "10101";
    public static final String POLICY_DELEGATION_N_MSG = "本次查询不需要进行授权";

    public static final String POLICY_RECHECK_Y_CODE = "10200";
    public static final String POLICY_RECHECK_Y_MSG = "本次查询需要进行审批";

    public static final String POLICY_RECHECK_N_CODE = "10201";
    public static final String POLICY_RECHECK_N_MSG = "本次查询不需要进行审批";


    
    public static String CODE_WAIT_QUERY = "000008";
    public static String MSG_WAIT_QUERY = "暂时还无结果，请5分钟后再查询结果";
    
    public static String CODE_SYS_EXCEPTION = "000005";
	public static String MSG_SYS_EXCEPTION = "系统异常";
	
	public static String CODE_QUERYUSER_VALI_ERROR = "DBE015";
	public static String MSG_QUERYUSER_VALI_ERROR = "查询用户代码校验错误";
	
	public static String CODE_REFUSE = "000001";
	public static String MSG_REFUSE = "查询拒绝，鉴权失败，非法访问,用户无接口查询权限";
	
	public static String CODE_NO_QUERYUSER = "000018";
	public static String MSG_NO_QUERYUSER = "查询用户不存在";
	
	public static String CODE_NO_MAPPING_CREDITUSER = "000019";
	public static String MSG_NO_MAPPING_CREDITUSER = "查询用户未映射征信用户";
	
	public static String CODE_NO_CREDITUSER = "000020";
	public static String MSG_NO_CREDITUSER = "映射的征信用户不存在";
}
