/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.authorizemanager.service;

import java.util.List;

import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeFileBo;

/**
 * 授权管理服务-授权档案文件管理服务接口类
 * @author Jerry.chen
 * @date 2019年2月15日
 */
public interface CeqAuthorizeFileService {
	/**
	 * 创建企业查询授权文件记录
	 * @param CeqAuthorizeFileBo
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	void create(CeqAuthorizeFileBo ceqAuthorizeFileBo);

	/**
	 * 批量创建企业查询授权文件记录
	 * @param ceqAuthorizeFileBoList
	 * @author sjk
	 * @date 2019年3月14日
	 */
	void authorizeFileInterface(List<CeqAuthorizeFileBo> ceqAuthorizeFileBoList);
	
    /**
     * 修改企业查询授权文件记录
     * @param CeqAuthorizeFileBo
     * @return int 成功时，该值大于等于0，失败则小于0
     * @author Jerry.chen
     * @date 2019年2月15日
     */
	int update(CeqAuthorizeFileBo ceqAuthorizeFileBo);

	/**
	 * 根据企业查询文件授权ID,删除一条授权文件记录
	 * @param ceqAuthorizeFileId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteById(String ceqAuthorizeFileId);
	
	/**
	 * 根据企业查询授权ID,删除相关的授权档案记录
	 * @param ceqAuthorizeId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteByAuthorizeId(String ceqAuthorizeId);
	/**
	 * 根据授权信息记录ID，查询授权文件记录集合
	 * @param ceqAuthorizeId
	 * @return 
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	List<CeqAuthorizeFileBo> findCeqAuthorizeFileByAuthorizeId(String ceqAuthorizeId);
	/**
	 * 根据授权文件记录ID，查询授权文件记录信息
	 * @param ceqAuthorizeFileId
	 * @return 
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	CeqAuthorizeFileBo findCeqAuthorizeFileByAuthorizeFileId(String ceqAuthorizeFileId);


}
