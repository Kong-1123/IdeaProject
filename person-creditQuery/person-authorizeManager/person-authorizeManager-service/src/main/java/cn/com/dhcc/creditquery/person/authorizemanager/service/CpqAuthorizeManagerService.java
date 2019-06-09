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
import java.util.Map;

import org.redisson.api.RLock;
import org.springframework.data.domain.Page;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;

/**
 * 授权管理服务-个人查询授权信息管理服务接口类
 * @author Jerry.chen
 * @date 2019年2月15日
 */
public interface CpqAuthorizeManagerService {
		/**
		 * 创建个人查询授权信息记录
		 * @param cpqArchiveBo
		 * @return @see CpqArchiveBo
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		CpqArchiveBo create(CpqArchiveBo cpqArchiveBo);
		/**
		 * 修改个人查询授权信息记录
		 * @param cpqArchiveBo
		 * @return int 成功时，该值大于等于0，失败则小于0
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		int update(CpqArchiveBo cpqArchiveBo);
		/**
		 * 根据个人查询授权ID,删除一条授权信息记录
		 * @param cpqArchiveId
		 * @return int 成功时，该值大于等于0，失败则小于0
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		int deleteById(String cpqArchiveId);
		/**
		 * 批量删除个人查询授权信息记录
		 * @param cpqArchiveIds  List<String>
		 * @return 
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		//---实现该接口的研发人员需要考虑性能，每次最大提交1000条
		int deleteByIds(List<String> cpqArchiveIds);
		/**
		 * 查询单条授权记录
		 * @param archiveId 授权记录ID
		 * @return 授权记录
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		CpqArchiveBo findById(String archiveId);
		/**
		 * 分页查询
		 * @param searchParams 
		 * @param pageNumber
		 * @param pageSize
		 * @param direction
		 * @param orderBy
		 * @return
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		Page<CpqArchiveBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize,String direction, String orderBy);
		/**
		 * 更新授权信息记录状态
		 * @param status
		 * @param archiveType
		 * @param id
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		void updateStatus(String status,String id);
		/**
		 * 根据客户标识查询授权信息记录
		 * @param customerName 客户姓名
		 * @param certType 证件类型
		 * @param certNo  证件号码
		 * @return 授权信息记录集合
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		List<CpqArchiveBo> findByCustomerIdentify(String customerName,String certType,String certNo);
		/**
		 * 批量查询授权信息记录(查询条件导出)
		 * @param param
		 * @return 
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		List<CpqArchiveBo> findAll(Map<String,Object> param);
		/**
		 * 根据授权信息记录ID集合，查询授权信息记录集合（默认全部导出）
		 * @param ids
		 * @return
		 * @author Jerry.chen
		 * @date 2019年2月15日
		 */
		List<CpqArchiveBo> findByIds(List<String> ids);
	
		/**
		 * 获取锁用于保存档案文件信息记录
		 * @param userId
		 * @return
		 * @author sjk
		 * @date 2019年2月25日
		 */
		RLock getRlockForSavePaer(String userId);
		
		/**
		 * 获取锁用于更新档案信息中的报告数量
		 * @return
		 * @author sjk
		 * @date 2019年2月25日
		 */
		RLock getRlockForUpdate();
		
		/**
		 * 更新档案信息中的报告数量
		 * @param id
		 * @return
		 * @author sjk
		 * @date 2019年2月25日
		 */
		boolean updateQueryNumByRedis(String id);
		
		long getQueryNumByRedis(String id);
		
		
		JSONObject getFileTypeForArchive(String topOrg);
		
		/**
		 * 根据reqid获取授权信息
		 * @param reqId
		 * @return
		 * @author sjk
		 * @date 2019年3月26日
		 */
		CpqArchiveBo findByReqId(String reqId);
		
}
