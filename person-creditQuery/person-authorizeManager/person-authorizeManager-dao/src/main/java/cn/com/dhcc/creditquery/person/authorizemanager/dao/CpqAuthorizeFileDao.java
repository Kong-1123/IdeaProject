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

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.person.authorizemanager.entity.CpqArchiveFile;

/**
 * 授权管理服务-个人查询授权文件数据访问
 * @author sjk
 * @date 2019年2月22日
 */
public interface CpqAuthorizeFileDao extends PagingAndSortingRepository<CpqArchiveFile, String>,JpaSpecificationExecutor<CpqArchiveFile>{

	@Query("from CpqArchiveFile where archiveid = ?1")
	List<CpqArchiveFile> findByArchiveId(String archiveId);
	
	@Query("delete from CpqArchiveFile where archiveid = ?1")
	void deleteByArchiveId(String archiveId);
	
	
}
