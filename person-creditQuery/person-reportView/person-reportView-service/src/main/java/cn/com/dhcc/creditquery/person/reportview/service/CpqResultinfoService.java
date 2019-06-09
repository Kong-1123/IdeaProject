/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.reportview.service;

import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqResultinfoView;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CpqResultinfoService {

	CpqResultinfoView create(CpqResultinfoView resultinfo);

	CpqResultinfoView update(CpqResultinfoView resultinfo);

	CpqResultinfoView findById(String id);

	Page<CpqResultinfoView> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy);

	CpqResultinfoView updateRedisByCpqResultinfo(CpqResultinfoView resultinfo);

	long getCurrentUserQueryNum(String username);

	Page<CpqResultinfoView> getRelevancePage(Map<String, Object> searchParams, int curPage, int maxSize, String direction,
			String orderby);

	Page<CpqResultinfoView> getArchiveRevisePage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy);

	void updateAssociateById(String id, String associate, String archiveRevise);

	void updateArchiveIdById(String id, String archiveId);
	
	Date getLastQueryTimeByArchiveId(String archiveId);
	
	CpqResultinfoView findByCreditId(String creditId);
	
	List<CpqResultinfoView> findByCcuser(String ccuser);
	
	void setUserQueryNumbyRedis(String username);

	/**
	 * <辖内机构档案补录快捷入口>
	 * @param date
	 * @param orgId
	 * @return
	 * @author Mingyu.Li
	 * @param qryReason 
	 * @date 2018年8月6日
	 * @return int 
	 */
	List<CpqResultinfoView> findByArchiveCount(String qryReason,List<String> deptCodes);

	/**
	 * 档案 快捷菜单
	 * @param userName
	 * @param deptCodes
	 * @param menuIds
	 * @return
	 */
	CpqShortcutBo getArchiveShortcut(String userName,List<String> deptCodes,List<String> menuIds);

}
