/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqResultinfoView;

public interface CeqResultinfoService {

	CeqResultinfoView create(CeqResultinfoView resultinfo);

	CeqResultinfoView update(CeqResultinfoView resultinfo);

	CeqResultinfoView findById(String id);

	Page<CeqResultinfoView> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String orderBy);

	CeqResultinfoView updateRedisByCeqResultinfo(CeqResultinfoView resultinfo);

	long getCurrentUserQueryNum(String username);

	Page<CeqResultinfoView> getRelevancePage(Map<String, Object> searchParams, int curPage, int maxSize,
			String direction, String orderby);

	Page<CeqResultinfoView> getArchiveRevisePage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy);

	void updateAssociateById(String id, String associate, String archiveRevise);

	void updateArchiveIdById(String id, String archiveId);

	Date getLastQueryTimeByArchiveId(String archiveId);

	CeqResultinfoView findByCreditId(String creditId);

	List<CeqResultinfoView> findByCcuser(String ccuser);

	void setUserQueryNumbyRedis(String username);

	/**
	 * <辖内机构档案补录快捷入口>
	 * 
	 * @param date
	 * @param orgId
	 * @return
	 * @author Mingyu.Li
	 * @param qryReason
	 * @date 2018年8月6日
	 * @return int
	 */
	List<CeqResultinfoView> findByArchiveCount(String qryReason, List<String> deptCodes);

	/**
	 * 档案 快捷菜单
	 * 
	 * @param userName
	 * @param deptCodes
	 * @param menuIds
	 * @return
	 */
	CeqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds);

}
