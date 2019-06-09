/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.person.reportview.entity.CpqCheckInfoView;

/**
*
* @author lekang.liu
* @date 2018年3月7日
*/
@Component
public interface CpqCheckInfoViewDao extends PagingAndSortingRepository<CpqCheckInfoView, String>, JpaSpecificationExecutor<CpqCheckInfoView>{
    
    @Query("SELECT c FROM CpqCheckInfoView  c where c.id in (?1)")
    List<CpqCheckInfoView> findByIdIn(List<String> ids);
    
    @Query("SELECT c FROM CpqCheckInfoView  c where c.operOrg in (?1) and c.rekType = '1' and status = '1' and operAtor != ?2")
    List<CpqCheckInfoView> findReceiveTask(List<String> deptIdList,String userName);
    
    @Query("SELECT c FROM CpqCheckInfoView  c where c.operOrg in (?1) and c.rekType = '1' and status in ('2','5')")
    List<CpqCheckInfoView> findTaskByDept(List<String> deptIdList);
    
    @Query("SELECT c FROM CpqCheckInfoView  c where c.rekUser = ?1 and c.rekType = '1' and status in ('2','5')" )
    List<CpqCheckInfoView> findTaskByUser(String userName);
    @Query("SELECT c FROM CpqCheckInfoView  c where c.archiveId = ?1")
    List<CpqCheckInfoView> findByArchiveId(String archiveid);
    
	@Query("update CpqCheckInfoView  set archiveId = ?2 where id = ?1")
	@Modifying
	void updateArchiveIdById(String checkId, String archiveid);

	@Query("from CpqCheckInfoView  u where u.id in (?1)")
	List<CpqCheckInfoView> findConfigByIds(List<String> asList);
    
}
