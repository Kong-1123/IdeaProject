/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryflowmanager.ansytash;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * <个人单笔查询请求参数表>
 * @author guoshihu
 * @date 2019年1月18日
 */
public interface CeqSingleQueryTaskDao extends PagingAndSortingRepository<CeqSingleQueryTask, String>,JpaSpecificationExecutor<CeqSingleQueryTask> {

	/**
	 * 通过reqid查询数据
	 * @param reqID
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月18日
	 */
	@Query("from CeqSingleQueryTask q where q.reqId=?1")
	CeqSingleQueryTask findByReqId(String reqID);
	
	/**
	 * 查询表中异步需审核类型的未完成数据
	 * @return
	 * @author guoshihu
	 * @date 2019年2月20日
	 */
	@Query("from CeqSingleQueryTask q where q.syncFlag='1' and  q.asyncQueryFlag='0' and q.status='1' ")
	List<CeqSingleQueryTask> findAuditingData();

}
