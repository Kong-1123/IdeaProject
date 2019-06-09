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

import cn.com.dhcc.creditquery.person.reportview.entity.CpqArchiveView;

public interface CpqArchiveViewDao extends PagingAndSortingRepository<CpqArchiveView, String>,JpaSpecificationExecutor<CpqArchiveView>{

	@Query("update CpqArchiveView set queryNum = ?1 where id = ?2")
	@Modifying
	void updateQueryNumById(long queryNum,String id);
	@Query("update CpqArchiveView set status = ?1,archiveType = ?2 where id = ?3")
	@Modifying
	void updateStatus(String status,String archiveType, String id);

	@Query("from CpqArchiveView where clientName = ?1 and cretType = ?2 and cretNo = ?3")
	List<CpqArchiveView> findByThreeStandardization(String clientName,String cretType,String cretNo);

	@Query("from CpqArchiveView where id in (?1)")
	List<CpqArchiveView> findByIds(List<String> ids);
}
