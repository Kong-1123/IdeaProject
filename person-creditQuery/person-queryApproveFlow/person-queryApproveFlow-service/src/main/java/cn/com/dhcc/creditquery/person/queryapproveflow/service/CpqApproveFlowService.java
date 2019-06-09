/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapproveflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqPageFlowControlInfo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;

/**
 * 个人征信授权审批服务
 * 
 * @author yahongcai
 * @date 2019年2月18日
 */
public interface CpqApproveFlowService {
	/**
	 * backPostFlg 0-前一岗位
	 */
	final static String BACKPOSTFLG_PREVIOUS = "0";
	/**
	 * backPostFlg 1-发起岗位
	 */
	final static String BACKPOSTFLG_LAUNCH = "1";
	
	/**
	 * 审批失败代码
	 */
	static final String FAILURE_CODE = "1";

	/**
	 * 审批失败信息
	 */
	static final String SAME_USER_MSG = "查询审批不能是同一人!";

	/**
	 * 审批失败信息
	 */
	static final String NON_EXISTENT = "审批用户不存在,请重新输入审批用户!!";

	/**
	 * 审批失败信息
	 */
	static final String NO_PRIVILEGE_MSG = "该用户没有同步审批权限,请输入有审批权限的用户！";

	static final String CHECKSUCCESS_MSG = "提交审批成功!";

	static final String CHECKFILED_MSG = "审批失败";

	/**
	 * 审批状态 - 待审批
	 */
	final static String PENDING_REVIEW = "1";
	/**
	 * 审批状态 - 审批通过
	 */
	final static String APPROVED_REVIEW = "3";

	/**
	 * 审批角色代码
	 */
	static final String CHECKROLE_ID = "Q_W_C_R";

	/**
	 * 创建个人征信授权审批记录
	 * 
	 * @param cpqApproveBo
	 * @return @see CpqApproveBo
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	CpqApproveBo create(CpqApproveBo cpqApproveBo);

	/**
	 * 修改个人征信授权审批记录
	 * 
	 * @param cpqApproveBo
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	int update(CpqApproveBo cpqApproveBo);

	/**
	 * 批量修改个人征信授权审批记录
	 * 
	 * @param cpqApproveBos
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	int batchUpdate(List<CpqApproveBo> cpqApproveBos);

	/**
	 * 根据个人征信授权审批记录Id，获取个人征信授权审批记录
	 * 
	 * @param cpqApproveId
	 * @return @see CpqApproveBo
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	CpqApproveBo findCpqApproveById(String cpqApproveId);

	/**
	 * 根据个人征信授权审批记录Id集合，获取个人征信授权审批记录列表
	 * 
	 * @param cpqApproveIds
	 * @return @see List<CpqApproveBo>
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	List<CpqApproveBo> findCpqApprovesByIds(List<String> cpqApproveIds);

	/**
	 * 分页查询
	 * 
	 * @param jpaParamGroup
	 * @param pageNumber
	 * @param pageSize
	 * @param direction
	 * @param orderBy
	 * @return @see Page<CpqApproveBo>
	 * @throws Exception
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	Page<CpqApproveBo> getPage(JPAParamGroup jpaParamGroup, int pageNumber, int pageSize, String direction,
			String orderBy) throws Exception;

	/**
	 * 根据用户账号、所属机构及菜单编号获取授权审批快捷菜单
	 * 
	 * @param userName
	 * @param menuIdList
	 * @param deptIdlist
	 * @return @see ShortcutBo
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	CpqShortcutBo getApproveShortCut(String userName, List<String> menuIdList, List<String> deptIdlist);

	/**
	 * 根据授权档案ID获取个人征授权审批记录
	 * 
	 * @param archiveId
	 * @return @see List<CpqApproveBo>
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	List<CpqApproveBo> findCpqApprovesByArchiveId(String archiveId);

	/**
	 * 根据个人征信授权审批ID，更新授权档案Id的值
	 * 
	 * @param cpqApproveId
	 * @param archiveId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	int updateArchiveIdByCpqApproveId(String cpqApproveId, String archiveId);

	/**
	 * 根据查询条件获取个人征信授权审批记录
	 * 
	 * @param searchParams
	 * @return @see List<CpqApproveBo>
	 * @author yahongcai
	 * @date 2019年2月18日
	 */
	List<CpqApproveBo> getCpqApprovesBySearchParams(Map<String, Object> searchParams);

	/**
	 * 向外系统或者本系统相关岗位人员发送审批结果通知 TODO 扩展接口 在拆分服务第一阶段暂不实现该接口
	 * 
	 * @param cpqApproveBo
	 * @return 通知成功返回成功，否则返回失败
	 * @author yahongcai
	 * @date 2019年2月19日
	 */
	boolean feedbackCpqApproveResult(CpqApproveBo cpqApproveBo);

	/**
	 * 审批拒绝，根据返回岗位标识退回到指定岗位 backPostFlg 0:上一岗位 1:发起岗位 TODO 扩展接口 在拆分服务第一阶段暂不实现该接口
	 * 
	 * @param cpqApproveBo
	 * @param backPostFlg
	 * @return 拒绝成功返回true，否则返回false
	 * @author yahongcai
	 * @date 2019年2月19日
	 */
	boolean cpqApproveRejected(CpqApproveBo cpqApproveBo, String backPostFlg);

	/**
	 * 撤销审批记录 TODO 扩展接口 在拆分服务第一阶段暂不实现该接口
	 * 
	 * @param cpqApproveBo
	 * @return 撤销成功返回true，否则返回false
	 * @author yahongcai
	 * @date 2019年2月19日
	 */
	boolean cpqApproveCancelled(CpqApproveBo cpqApproveBo);

	/**
	 * 通过reqId查询记录暂时用户接口
	 * 
	 * @param reqId
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年3月8日
	 */
	CpqApproveBo findCpqApproveByReqId(String reqId);

	/**
	 * 同步审批
	 * 
	 * @param cpqReportQueryBo
	 * @return
	 * @throws Exception
	 */
	CpqPageFlowControlInfo sysncApprove(CpqReportQueryBo cpqReportQueryBo) throws Exception;

}
