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

import cn.com.dhcc.creditquery.person.reportview.entity.CpqUserAttrView;

public interface CpqUserAttrViewDao extends JpaSpecificationExecutor<CpqUserAttrView>,PagingAndSortingRepository<CpqUserAttrView, String>{

	@Query("from CpqUserAttrView where userName = ?1")
	CpqUserAttrView findByUserId(String userId);
	
	@Query("SELECT userName FROM CpqUserAttrView WHERE id IN (?1)")
	List<String> findByUserNamesByIds(List<String> ids);
	
	@Query("SELECT userName FROM CpqUserAttrView WHERE seniorCreditUser = ?1")
	List<String> findUserNameBySeniorCreditUser(String SeniorCreditUser);
	
	@Query("UPDATE CpqUserAttrView SET creditUser = NULL WHERE  creditUser = ?1")
	@Modifying
	void updateUserAttrCreditUser2Null(String creditUser);
	
	@Query("DELETE FROM CpqUserAttrView WHERE userName = ?1")
	@Modifying
	void deleteByUserName(List<String> userNames);

	
}
