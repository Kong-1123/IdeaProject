/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.ent.query.bo.reportview.CeqReportLogBo;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqReportLogView;

/**
 * 报告使用记录服务
 */
public interface CeqReportLogService {

	CeqReportLogBo save(CeqReportLogView printflow);

	/**
	 * <通过ID查询打印报告详细信息>
	 * 
	 * @param id
	 * @return
	 */
	CeqReportLogBo findById(String id);

	Page<CeqReportLogBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy);

	void save(String creditId, String userName, String orgCode, String logType);

	/**
	 * 根据查询记录ID获取已打印次数
	 * 
	 * @param recordId
	 * @return
	 */
	long findPrintCount(String recordId);

	/**
	 * 根据参数批量查询报告使用记录
	 * @param searchParams
	 * @return
	 */
	List<CeqReportLogBo> findAll(Map<String, Object> searchParams);

	/**
	 * 根据id批量查询报告使用记录
	 * @param ceqReportLogIds
	 * @return
	 */
	List<CeqReportLogBo> findCeqReportLogBosByIds(List<String> ceqReportLogIds);

}
