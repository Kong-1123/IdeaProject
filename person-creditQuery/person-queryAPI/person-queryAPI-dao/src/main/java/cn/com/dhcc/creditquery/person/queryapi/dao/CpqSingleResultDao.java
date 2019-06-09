/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleResult;

/**
 * <个人单笔查询结果表>
 * @author guoshihu
 * @date 2019年1月18日
 */
public interface CpqSingleResultDao extends PagingAndSortingRepository<CpqSingleResult, String>,JpaSpecificationExecutor<CpqSingleResult> {

	/**
	 * 根据reqID查询对应的查询报告
	 * @param reqID
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	@Query("from CpqSingleResult res where res.reqId=?1")
	CpqSingleResult findByReqId(String reqID);

	/**
	 * 通过cpq_resultinfo表的id查询数据
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年4月1日
	 */
	@Query("from CpqSingleResult res where res.creditreportNo=?1")
	CpqSingleResult findByCreditreportNo(String creditreportNo);

	/**
	 * 通过批次号查询数据
	 * @param msgNo
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年4月3日
	 */
	@Query("from CpqSingleResult res where res.msgNo=?1")
	List<CpqSingleResult> findByMsgNo(String msgNo);

}
