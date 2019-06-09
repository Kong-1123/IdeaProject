/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.ansytash;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * <个人单笔查询结果表>
 * @author guoshihu
 * @date 2019年1月18日
 */
public interface CpqSingleResultTaskDao extends PagingAndSortingRepository<CpqSingleResultTask, String>,JpaSpecificationExecutor<CpqSingleResultTask> {

	/**
	 * 根据reqID查询对应的查询报告
	 * @param reqID
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年1月22日
	 */
	@Query("from CpqSingleResultTask res where res.reqId=?1")
	CpqSingleResultTask findByReqId(String reqID);

}
