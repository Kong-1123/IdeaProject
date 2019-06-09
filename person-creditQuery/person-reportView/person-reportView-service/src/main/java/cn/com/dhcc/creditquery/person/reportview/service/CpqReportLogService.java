/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.service;

import java.util.List;
import java.util.Map;

import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.person.query.bo.reportview.CpqReportLogBo;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqReportLogView;

public interface CpqReportLogService {
	
	CpqReportLogBo save(CpqReportLogView printflow);
	
	/**
	 * <通过ID查询打印报告详细信息>
	 * @param id
	 * @return
	 */
	CpqReportLogBo findById(String id);
	
	
	Page<CpqReportLogBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy);
	
	void save(String creditId,String userName,String orgCode,String logType);
	
	long findPrintCount(String recordId);

	/**
	 * 根据参数批量查询报告使用记录
	 * @param searchParams
	 * @return
	 */
	List<CpqReportLogBo> findAll(Map<String, Object> searchParams);

	/**
	 * 根据id批量查询报告使用记录
	 * @param cpqReportLogIds
	 * @return
	 */
	List<CpqReportLogBo> findCpqReportLogBosByIds(List<String> cpqReportLogIds);
}
