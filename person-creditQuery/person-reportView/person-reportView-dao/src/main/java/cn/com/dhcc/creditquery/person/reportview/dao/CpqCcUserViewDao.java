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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.person.reportview.entity.CpqCcUserView;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqUserAttrView;

public interface CpqCcUserViewDao
		extends PagingAndSortingRepository<CpqCcUserView, String>, JpaSpecificationExecutor<CpqCcUserView> {
	@Query("from CpqCcUserView  where ccuser = ?1")
	CpqCcUserView findCreditUser(String creditUser);
	
	@Query("from CpqCcUserView  where ccId = ?1")
	CpqCcUserView findByCcId(String ccId);
	
	@Modifying
	@Query("update CpqCcUserView set ccuser=?2,ccdept=?3,passwd=?4,updateUser=?5,updateDate=?6 where id=?1")
	void updateCcdept(String id,String  ccuser,String ccdept,String updateUser,String passwd,Date updateDate);
	
	@Modifying
	@Query("update CpqCcUserView set passwd=?2,updateUser=?3,updateDate=?4 where id=?1")
	void updatePwd(String id,String passwd,String updateUser,Date updateDate);
	
	@Query("from CpqCcUserView where deptcode in (?1)")
	List<CpqCcUserView> findByDeptCode(ArrayList<String> deptCode);
}
