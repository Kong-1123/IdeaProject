/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.queryapi.entity.CqSysIp;


/**
 * <子系统ip地址表dao>
 * @author guoshihu
 * @date 2019年1月17日
 */
public interface CqSysIpDao extends PagingAndSortingRepository<CqSysIp, String>,JpaSpecificationExecutor<CqSysIp>  {
	/**
	 * 根据系统标识查找当前状态的所有ip
	 * @param sysCode
	 * @param status
	 * @return
	 * @author guoshihu
	 * @date 2019年1月19日
	 */
	@Query("SELECT c.ipAddr FROM CqSysIp c WHERE c.sysCode=?1 and c.status=?2 ")
	List<String> findIpBySysCode(String sysCode,String status);
}
