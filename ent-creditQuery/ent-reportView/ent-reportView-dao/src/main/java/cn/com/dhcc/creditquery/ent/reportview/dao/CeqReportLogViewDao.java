/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.reportview.entity.CeqReportLogView;

import java.util.List;

/**
 * <信用报告打印记录-Dao层>
 * @author wenjie·chu
 *
 * 2018年3月17日-下午4:27:50
 */
public interface CeqReportLogViewDao extends PagingAndSortingRepository<CeqReportLogView, String>,JpaSpecificationExecutor<CeqReportLogView> {
	@Query("select count(a.id) from CeqReportLogView a where  a.recordId = ?1 and operateType = '0' " )
	long findPrintCount(String recordId);

	@Query("from CeqReportLogView where id in (?1)")
	List<CeqReportLogView> findCeqReportLogViewsByIds(List<String> ids);

}
