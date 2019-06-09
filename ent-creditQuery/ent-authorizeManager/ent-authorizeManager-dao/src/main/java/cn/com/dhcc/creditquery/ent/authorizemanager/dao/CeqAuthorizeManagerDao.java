/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.authorizemanager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.authorizemanager.entity.CeqAuthorizeManager;

/**
 *  授权管理服务-企业查询授权信息管理 数据访问
 * @author sjk
 * @date 2019年2月21日
 */
public interface CeqAuthorizeManagerDao extends PagingAndSortingRepository<CeqAuthorizeManager, String>,JpaSpecificationExecutor<CeqAuthorizeManager>{

	@Query("update CeqAuthorizeManager set status = ?1 where id = ?2")
	@Modifying
	void updateStatus(String status,String authorizeId);
	
	@Query("from CeqAuthorizeManager where signCode = ?1")
	List<CeqAuthorizeManager> findByThreeStandardization(String signCode);
	
	@Query("from CeqAuthorizeManager where id in (?1)")
	List<CeqAuthorizeManager> findByIds(List<String> authorizeIdList);
	
	@Query("delete from CeqAuthorizeManager where id in(?1)")
	void deleteIds(List<String> authorizeIdList);
	
	@Query("update CeqAuthorizeManager set queryNum = ?1 where id = ?2")
	@Modifying
	void updateQueryNumById(long queryNum,String authorizeId);
	
	@Query("from CeqAuthorizeManager where reqId = ?1")
	CeqAuthorizeManager findByReqId(String reqId);
}
