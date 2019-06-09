/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.authorizemanager.dao;

import cn.com.dhcc.creditquery.person.authorizemanager.entity.CpqArchiveAuthorizeManager;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 *  授权管理服务-个人查询授权信息管理 数据访问
 * @author sjk
 * @date 2019年2月21日
 */
public interface CpqArchiveDao extends PagingAndSortingRepository<CpqArchiveAuthorizeManager, String>,JpaSpecificationExecutor<CpqArchiveAuthorizeManager>{
    @Query("update CpqArchiveAuthorizeManager set status = ?1 where id = ?2")
    @Modifying
    void updateStatus(String status,String id);

    @Query("from CpqArchiveAuthorizeManager where clientName = ?1 and cretType = ?2 and cretNo = ?3")
    List<CpqArchiveAuthorizeManager> findByThreeStandardization(String clientName,String cretType,String cretNo);

    @Query("from CpqArchiveAuthorizeManager where id in (?1)")
    List<CpqArchiveAuthorizeManager> findByIds(List<String> ids);

    @Query("delete from CpqArchiveAuthorizeManager where id in(?1)")
    void deleteIds(List<String> ids);

    @Query("update CpqArchiveAuthorizeManager set queryNum = ?1 where id = ?2")
    @Modifying
    void updateQueryNumById(long queryNum,String id);

    @Query("from CpqArchiveAuthorizeManager where reqId = ?1")
    CpqArchiveAuthorizeManager findByReqId(String reqId);

}
