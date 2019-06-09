/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapproveflow.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.queryapproveflow.entity.CeqApprove;

/**
 *  个人查询审批DAO-个人查询审批数据访问
 * @author yahongcai
 * @date 2019年2月23日
 */
public interface CeqApproveDao extends PagingAndSortingRepository<CeqApprove, String>,JpaSpecificationExecutor<CeqApprove>{

	@Query("from CeqApprove where id in (?1)")
	List<CeqApprove> findCeqApprovesByIds(List<String> ids);
	
    @Query("SELECT c FROM CeqApprove c where c.archiveId = ?1")
    List<CeqApprove> findCeqApprovesByArchiveId(String archiveId);
    
	@Query("update CeqApprove set archiveId = ?2 where id = ?1")
	@Modifying
	void updateArchiveIdByCeqApproveId(String cpqApproveId, String archiveId);
	
    @Query("SELECT c FROM CeqApprove c where c.operOrg in (?1) and c.rekType = '1' and status = '1' and operator != ?2")
    List<CeqApprove> findReceiveTask(List<String> deptIdList,String userName);
    
    @Query("SELECT c FROM CeqApprove c where c.rekUser = ?1 and c.rekType = '1' and status in ('2','5')" )
    List<CeqApprove> findTaskByUser(String userName);

	/**通过reqId查询记录暂用于接口
	 * @param reqId
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
    @Query("from CeqApprove c where c.reqId=?1" )
	CeqApprove findCeqApproveByReqId(String reqId);

}
