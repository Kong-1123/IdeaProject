/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapproveflow.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.person.queryapproveflow.entity.CpqApprove;

/**
 *  个人查询审批DAO-个人查询审批数据访问
 * @author yahongcai
 * @date 2019年2月23日
 */
public interface CpqApproveDao extends PagingAndSortingRepository<CpqApprove, String>,JpaSpecificationExecutor<CpqApprove>{

	@Query("from CpqApprove where id in (?1)")
	List<CpqApprove> findCpqApprovesByIds(List<String> ids);
	
    @Query("SELECT c FROM CpqApprove c where c.archiveId = ?1")
    List<CpqApprove> findCpqApprovesByArchiveId(String archiveId);
    
	@Query("update CpqApprove set archiveId = ?2 where id = ?1")
	@Modifying
	void updateArchiveIdByCpqApproveId(String cpqApproveId, String archiveId);
	
    @Query("SELECT c FROM CpqApprove c where c.operOrg in (?1) and c.rekType = '1' and status = '1' and operAtor != ?2")
    List<CpqApprove> findReceiveTask(List<String> deptIdList,String userName);
    
    @Query("SELECT c FROM CpqApprove c where c.rekUser = ?1 and c.rekType = '1' and status in ('2','5')" )
    List<CpqApprove> findTaskByUser(String userName);

	/**通过reqId查询记录暂用于接口
	 * @param reqId
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
    @Query("from CpqApprove c where c.reqId=?1" )
	CpqApprove findCpqApproveByReqId(String reqId);

}
