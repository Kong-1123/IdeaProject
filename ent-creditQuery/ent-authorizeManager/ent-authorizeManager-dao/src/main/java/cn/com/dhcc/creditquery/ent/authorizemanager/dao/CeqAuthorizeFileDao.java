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

import cn.com.dhcc.creditquery.ent.authorizemanager.entity.CeqAuthorizeFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 授权管理服务-企业查询授权文件数据访问
 * @author sjk
 * @date 2019年2月22日
 */
public interface CeqAuthorizeFileDao extends PagingAndSortingRepository<CeqAuthorizeFile, String>,JpaSpecificationExecutor<CeqAuthorizeFile>{

	@Query("from CeqAuthorizeFile where archiveId = ?1")
	List<CeqAuthorizeFile> findByAuthorizeId(String authorizeId);
	
	@Query("delete from CeqAuthorizeFile where archiveId = ?1")
	void deleteByAuthorizeId(String authorizeId);
	

}
