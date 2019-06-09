/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.authorizemanager.service;

import java.util.List;

import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;

/**
 * 授权管理服务-授权档案文件管理服务接口类
 * @author Jerry.chen
 * @date 2019年2月15日
 */
public interface CpqAuthorizeFileService {
	/**
	 * 创建个人查询授权文件记录
	 * @param cpqArchivefileBo
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	void create(CpqArchivefileBo cpqArchivefileBo);
	
	/**
	 * 批量创建个人查询授权文件记录
	 * @param cpqArchivefileBoList
	 * @author sjk
	 * @date 2019年3月14日
	 */
	void createList(List<CpqArchivefileBo> cpqArchivefileBoList);
	
    /**
     * 修改个人查询授权文件记录
     * @param cpqArchivefileBo
     * @return int 成功时，该值大于等于0，失败则小于0
     * @author Jerry.chen
     * @date 2019年2月15日
     */
	int update(CpqArchivefileBo cpqArchivefileBo);

	/**
	 * 根据个人查询文件授权ID,删除一条授权文件记录
	 * @param cpqArchiveFileId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteById(String cpqArchiveFileId);
	
	/**
	 * 根据个人查询授权ID,删除相关的授权档案文件记录
	 * @param cpqArchiveId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteByArchiveId(String cpqArchiveId);
	/**
	 * 根据授权信息记录ID，查询授权文件记录集合
	 * @param ids
	 * @return 
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	List<CpqArchivefileBo> findCpqArchivefilesByArchiveId(String cpqArchiveId);
	/**
	 * 根据授权文件记录ID，查询授权文件记录信息
	 * @param cpqArchiveFileId
	 * @return 
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	CpqArchivefileBo findCpqArchivefileByArchiveFileId(String cpqArchiveFileId);


}
