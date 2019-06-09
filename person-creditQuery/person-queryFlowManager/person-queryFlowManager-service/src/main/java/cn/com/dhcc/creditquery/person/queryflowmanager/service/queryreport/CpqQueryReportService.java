/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.service.queryreport;

import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.queryflowmanager.bo.CpqQueryReportFlowBo;

/**
 * 信用报告查询服务
 *
 * @Auther: liulekang
 * @Date: 2019/2/26
 */
public interface CpqQueryReportService {
	
	  /**
     * 请求访问来源-web
     */
    static final String ACCESS_SOURCE_WEB = "1";

    /**
     * 请求访问来源-interface
     */
    static final String ACCESS_SOURCE_INTERFACE = "2";


    /**
     * 把查询需要的信息传进来
     * 调用userRouter 获取征信用户。
     * 调用PBOC进行报告查询。
     * 调用fileStorage存储报告
     * 调用queryReacode保存查询记录
     * 返回信用报告查询信息{查询记录ID，直接把报告给回去}
     */
	CpqQueryReportFlowBo creditReportQuery(CpqReportQueryBo cpqReportQueryBo);
	
}
