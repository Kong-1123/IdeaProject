/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapproveflow.service.impl;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.com.dhcc.query.creditquerycommon.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqShortcutBo.Menu;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqPageFlowControlInfo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqReportQueryBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.dao.CpqApproveDao;
import cn.com.dhcc.creditquery.person.queryapproveflow.entity.CpqApprove;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.platformmiddleware.login.LoginServer;
import cn.com.dhcc.platformmiddleware.vo.SystemRole;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import lombok.extern.slf4j.Slf4j;

/**
 *  个人查询审批服务-个人查询审批管理服务实现类
 * @author yahongcai
 * @date 2019年2月23日
 */
@Slf4j
@Transactional(value = "transactionManager")
@Service
public class CpqApproveFlowServiceImpl  implements CpqApproveFlowService{
	private static String MENU_APPROVE_RECEIVE = "20020103";
	private static String MENU_APPROVE_RECEIVE_NAME = "审批任务领取";

	private static String MENU_APPROVE_RECEIVE_URL = "/creditpersonqueryweb/checkinfo/list";

	private static String MENU_APPROVE_RECEIVE_URL_MICRO = "/creditper-checkinfo";

	/**
	 * 是否为微服务版本
	 */
	private static final  String IS_MICRO_SERVICE_CONFIG_KEY = "isMicroService";

	/**
	 * 审批任务管理，取审批任务菜单ID
	 */
	private static String MENU_APPROVE_TASK = "20020102";
	private static String MENU_APPROVE_TASK_NAME = "审批任务管理";
	private static String MENU_APPROVE_TASK_URL = "/creditpersonqueryweb/checkinfotask/list";
	private static String MENU_APPROVE_TASK_URL_MICRO = "/creditper-checkinfotask";

	@Autowired
	private CpqApproveDao cpqApproveDao;
	@LogOperation("新增征信查询审批记录")
	@Override
	public CpqApproveBo create(CpqApproveBo cpqApproveBo) {
		log.info("CpqApproveFlowServiceImpl.create start cpqApproveBo={}",cpqApproveBo);
		CpqApprove cpqApprove = ClassCloneUtil.copyObject(cpqApproveBo, CpqApprove.class);
		try {
			cpqApprove = cpqApproveDao.save(cpqApprove);
			cpqApproveBo = ClassCloneUtil.copyObject(cpqApprove, CpqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.create error={},cpqApprove={} ",e,cpqApprove);
		}
		log.info("CpqApproveFlowServiceImpl.create end,cpqApproveBo={}",cpqApproveBo);
		return cpqApproveBo;
	}
	@LogOperation("修改征信查询审批记录")
	@Override
	public int update(CpqApproveBo cpqApproveBo) {
		log.info("CpqApproveFlowServiceImpl.update start,cpqApproveBo={}",cpqApproveBo);
		CpqApprove cpqApprove = ClassCloneUtil.copyObject(cpqApproveBo, CpqApprove.class);
		int resultFlg = 0;
		try {
			cpqApproveDao.save(cpqApprove);
		} catch (Exception e) {
			log.error("CpqApproveFlowServiceImpl.update error={},cpqApprove={} ",e,cpqApprove);
			resultFlg = -1;
		}
		log.info("CpqApproveFlowServiceImpl.update end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@LogOperation("批量更新征信查询审批记录")
	@Override
	public int batchUpdate(List<CpqApproveBo> cpqApproveBos) {
		log.info("CpqApproveFlowServiceImpl.batchUpdate start,cpqApproveBos={}",cpqApproveBos);
		List<CpqApprove> cpqApproveList = ClassCloneUtil.copyIterableObject(cpqApproveBos, CpqApprove.class);
		int resultFlg = 0;
		List<List<CpqApprove>> splitCpqApproveList = null;
		try {
			splitCpqApproveList = Lists.partition(cpqApproveList, 900);
    		for(int i=0;i<splitCpqApproveList.size();i++) {
    			//cpqApproveDao.save(splitCpqApproveList.get(i));
    			cpqApproveDao.save(splitCpqApproveList.get(i));
    		}
		} catch (Exception e) {
			log.error("CpqApproveFlowServiceImpl.batchUpdate error={},splitCpqApproveList={} ",e,splitCpqApproveList);
			resultFlg = -1;
		}
		log.info("CpqApproveFlowServiceImpl.batchUpdate end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@Override
	public CpqApproveBo findCpqApproveById(String cpqApproveId) {
		log.info("CpqApproveFlowServiceImpl.findCpqApproveById start,cpqApproveId ={}",cpqApproveId);
		Optional<CpqApprove> cpqApprove = null;
		CpqApproveBo cpqApproveBo = null;
		try {
			cpqApprove = cpqApproveDao.findById(cpqApproveId);
			cpqApproveBo = ClassCloneUtil.copyObject(cpqApprove, CpqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.findCpqApproveById error={},cpqApprove={} ",e,cpqApprove);
		}
		log.info("CpqApproveFlowServiceImpl.findCpqApproveById end,cpqApproveBo ={}",cpqApproveBo);
		return cpqApproveBo;
	}

	@Override
	public List<CpqApproveBo> findCpqApprovesByIds(List<String> cpqApproveIds) {
		log.info("CpqApproveFlowServiceImpl.findCpqApprovesByIds start,cpqApproveIds ={}",cpqApproveIds);
		List<CpqApprove> cpqApproveList = new ArrayList();
		List<CpqApproveBo> cpqApproveBoList = new ArrayList();
		List<List<String>> splitIdList = Lists.partition(cpqApproveIds, 900);
		try {
			for(int i=0;i<splitIdList.size();i++) {
				List<CpqApprove> splitCpqApproveList = cpqApproveDao.findCpqApprovesByIds(cpqApproveIds);
				cpqApproveList.addAll(splitCpqApproveList);
			}
			cpqApproveBoList = ClassCloneUtil.copyIterableObject(cpqApproveList, CpqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.findCpqApprovesByIds error={},cpqApproveList={} ",e,cpqApproveList);
		}
		log.info("CpqApproveFlowServiceImpl.findCpqApprovesByIds end,cpqApproveBoList ={}",cpqApproveBoList);
		return cpqApproveBoList;
	}

	@Override
	public Page<CpqApproveBo> getPage(JPAParamGroup jpaParamGroup, int pageNumber, int pageSize, String direction,
			String orderBy) throws Exception {
		log.info("CpqApproveFlowServiceImpl.getPage start,jpaParamGroup ={}, pageNumber ={},pageSize ={}, direction ={}, orderBy ={}"
				,jpaParamGroup,pageNumber,pageSize,direction,orderBy);
		Page<CpqApprove> cpqApprovePage = null;
		Page<CpqApproveBo> cpqApproveBoPage = null;
		try {
			cpqApprovePage = PageUtil.getPageByJPA(jpaParamGroup, pageNumber, pageSize, direction, orderBy, cpqApproveDao,
					CpqApprove.class);
			cpqApproveBoPage = ClassCloneUtil.copyPage(cpqApprovePage, CpqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.getPage error={},cpqApprovePage={},cpqApproveBoPage={} ",e,cpqApprovePage,cpqApproveBoPage);
		}
		log.info("CpqApproveFlowServiceImpl.getPage end, cpqApproveBoPage={}",cpqApproveBoPage);
		return cpqApproveBoPage;
	}

	@Override
	public CpqShortcutBo getApproveShortCut(String userName, List<String> menuIdList, List<String> deptIdlist) {
		log.info("CpqApproveFlowServiceImpl.getApproveShortCut start,userName ={}, menuIdList ={},deptIdlist ={}",
				userName,menuIdList,deptIdlist);
		CpqShortcutBo cpqShortcutBo = new CpqShortcutBo();
		if (null == menuIdList) {
			cpqShortcutBo.setTitle("审批任务");
			return cpqShortcutBo;
		}
		List<Menu> menuList = new ArrayList<Menu>();
		Menu receiveTaskMenu = new Menu();
		Menu cpqApproveTaskMenu = new Menu();
		try {
			cpqShortcutBo.setTitle("审批任务");
			boolean receiveFlag = isShowMenu(MENU_APPROVE_RECEIVE, menuIdList);
			if (receiveFlag) {
				handleMenu(deptIdlist, receiveTaskMenu,userName);
				menuList.add(receiveTaskMenu);
			}
			boolean taskFlag = isShowMenu(MENU_APPROVE_TASK, menuIdList);
			if (taskFlag) {
				handleTaskMenuByUser(userName, cpqApproveTaskMenu);
				menuList.add(cpqApproveTaskMenu);
			}
			cpqShortcutBo.setMenus(menuList);
		} catch (Exception e) {
			log.error("CpqApproveFlowServiceImpl.getApproveShortCut  error={}",e);
		}
		log.info("CpqApproveFlowServiceImpl.getApproveShortCut end cpqShortcutBo={}",cpqShortcutBo);
		return cpqShortcutBo;
	}
	/**
	 * 是否展示菜单信息
	 * @param menu
	 * @param menuIdList
	 * @return  成功返回true,否则返回false
	 */
	private boolean isShowMenu(String menu, List<String> menuIdList) {
		log.info("CpqApproveFlowServiceImpl.isShowMenu start,menu={},menuIdList={}",menu,menuIdList);
		boolean contrainFlg = false;
		if (menuIdList.contains(menu)) {
			contrainFlg = true;
		}
		log.info("CpqApproveFlowServiceImpl.isShowMenu end,contrainFlg={}",contrainFlg);
		return contrainFlg;
	}
	
	/**
	 * 辖内审批任务领取
	 * 
	 * @param deptIdlist
	 * @param receiveTaskMenu
	 * @return @see Menu
	 */
	private Menu handleMenu(List<String> deptIdlist, Menu receiveTaskMenu,String userName) {
		log.info("CpqApproveFlowServiceImpl.handleMenu start,userName ={},receiveTaskMenu={},deptIdlist={}",userName,receiveTaskMenu,deptIdlist);
		int receiveCount = getReceiveTask(deptIdlist,userName);
		receiveTaskMenu.setCount(receiveCount);
		String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
		String url = MENU_APPROVE_RECEIVE_URL;
		if(Boolean.parseBoolean(property)){
			url = MENU_APPROVE_RECEIVE_URL_MICRO;
		}
		receiveTaskMenu.setLink(url);
		receiveTaskMenu.setName(MENU_APPROVE_RECEIVE_NAME);
		log.info("CpqApproveFlowServiceImpl.handleMenu end,receiveTaskMenu ={}",receiveTaskMenu);
		return receiveTaskMenu;
	}
	
	/**
	 * 辖内领取任务数
	 * @param deptIdlist
	 * @return @see int
	 */
	private int getReceiveTask(List<String> deptIdlist,String userName) {
		log.info("CpqApproveFlowServiceImpl.getReceiveTask start,userName ={},deptIdlist={}",userName,deptIdlist);
		List<CpqApprove> cpqApproveList = null;
		try {
			cpqApproveList = cpqApproveDao.findReceiveTask(deptIdlist,userName);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.getReceiveTask error,e={}",e);
		}
		int size = 0 ;
		if (cpqApproveList != null) {
			size = cpqApproveList.size();
		}
		return size;
	}
	
	/**
	 * 审批任务管理
	 * @param userName
	 * @param cpqApproveTaskMenu
	 * @return @see Menu
	 */
	private Menu handleTaskMenuByUser(String userName, Menu cpqApproveTaskMenu) {
		log.info("CpqApproveFlowServiceImpl.handleTaskMenuByUser start,userName ={},cpqApproveTaskMenu={}",userName,cpqApproveTaskMenu);
		int taskCount = getCpqApproveTaskByUser(userName);
		cpqApproveTaskMenu.setCount(taskCount);
		String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
		String url = MENU_APPROVE_TASK_URL;
		if(Boolean.parseBoolean(property)){
			url = MENU_APPROVE_TASK_URL_MICRO;
		}
		cpqApproveTaskMenu.setLink(url);
		cpqApproveTaskMenu.setName(MENU_APPROVE_TASK_NAME);
		log.info("CpqApproveFlowServiceImpl.handleTaskMenuByUser end,cpqApproveTaskMenu ={}",cpqApproveTaskMenu);
		return cpqApproveTaskMenu;
	}
	
	/**
	 * 审批任务数
	 * @param userName
	 * @return @see int
	 */
	private int getCpqApproveTaskByUser(String userName) {
		log.info("CpqApproveFlowServiceImpl.getCpqApproveTaskByUser start,userName ={}",userName);
		List<CpqApprove> cpqApproveList = null;
		try {
			cpqApproveList = cpqApproveDao.findTaskByUser(userName);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.getCpqApproveTaskByUser error={},cpqApproveList={}",e,cpqApproveList);
		}
		int size = 0 ;
		if (cpqApproveList != null) {
			size = cpqApproveList.size();
		}
		log.info("CpqApproveFlowServiceImpl.getCpqApproveTaskByUser end,size ={}",size);
		return size;
	}

	@Override
	public List<CpqApproveBo> findCpqApprovesByArchiveId(String archiveId) {
		log.info("CpqApproveFlowServiceImpl.findCpqApprovesByArchiveId start,archiveId ={}",archiveId);
		List<CpqApprove> cpqApproveList = null;
		List<CpqApproveBo> cpqApproveBoList = null;
		try {
			cpqApproveList = cpqApproveDao.findCpqApprovesByArchiveId(archiveId);
			cpqApproveBoList = ClassCloneUtil.copyIterableObject(cpqApproveList, CpqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.findCpqApprovesByArchiveId error={}, cpqApproveBoList={}",e,cpqApproveBoList);
		}
		log.info("CpqApproveFlowServiceImpl.findCpqApprovesByArchiveId end,cpqApproveBoList ={}",cpqApproveBoList);
		return cpqApproveBoList;
	}
	@LogOperation("根据征信查询审批记录Id更新授权档案Id")
	@Override
	public int updateArchiveIdByCpqApproveId(String cpqApproveId, String archiveId) {
		log.info("CpqApproveFlowServiceImpl.updateArchiveIdByCpqApproveId start,cpqApproveId={},archiveId={}",cpqApproveId,archiveId);
		int resultFlg = 0;
		try {
			cpqApproveDao.updateArchiveIdByCpqApproveId(cpqApproveId, archiveId);
		} catch (Exception e) {
			log.error("CpqApproveFlowServiceImpl.updateArchiveIdByCpqApproveId  error={}",e);
			resultFlg = -1;
		}
		log.info("CpqApproveFlowServiceImpl.updateArchiveIdByCpqApproveId end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@Override
	public List<CpqApproveBo> getCpqApprovesBySearchParams(Map<String, Object> searchParams) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean feedbackCpqApproveResult(CpqApproveBo cpqApproveBo) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean cpqApproveRejected(CpqApproveBo cpqApproveBo, String backPostFlg) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean cpqApproveCancelled(CpqApproveBo cpqApproveBo) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService#findCpqApproveByReqId(java.lang.String)
	 */
	@Override
	public CpqApproveBo findCpqApproveByReqId(String reqId) {
		CpqApprove cpqApprove = cpqApproveDao.findCpqApproveByReqId(reqId);
		return ClassCloneUtil.copyObject(cpqApprove, CpqApproveBo.class);
	}
	
	
	
	/**
	 * 同步审批
	 *
	 * @param cpqReportQueryBo
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public CpqPageFlowControlInfo sysncApprove(CpqReportQueryBo cpqReportQueryBo) throws Exception {
		// 进行同步审批
		CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		String queryUser = cpqReportQueryBo.getOperator();
		String approveUser = webQueryBo.getApproveUser();
		// 判断查询员是否属于复核员的辖内机构
		// 访问平台获取审批用户详细信息
		SystemUser systemUser = LoginValidateUtil.findUserByUserName(approveUser);
		if(null == systemUser) {
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(NON_EXISTENT);
			pageFlowControlInfo.setResCode(FAILURE_CODE);
			return pageFlowControlInfo;
		}
		String deptCode = webQueryBo.getQueryOrg();
		String rekOrg = systemUser.getOrgId();
		webQueryBo.setApproveOrg(rekOrg);
		List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodesByDeptCode(rekOrg);
		if (null != deptCodes && !deptCodes.contains(deptCode)) {
			// 查询用户不属于该审批用户管理范围，返回相应信息
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(NON_EXISTENT);
			pageFlowControlInfo.setResCode(FAILURE_CODE);
			return pageFlowControlInfo;
		}
		if (queryUser.equals(approveUser)) {
			// 查询用户与审批用户相同，返回相应信息
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(SAME_USER_MSG);
			pageFlowControlInfo.setResCode(FAILURE_CODE);
			return pageFlowControlInfo;
		}
		CpqPageFlowControlInfo pageFlowControlInfo = checkApproveUserAndRole(cpqReportQueryBo, webQueryBo);
		// 审批通过，新增审批记录
		if (StringUtils.equals(pageFlowControlInfo.getResCode(), Constant.REVIEW_SYN_OK)) {
			CpqApproveBo cpqApproveBo = buildCpqApproveBo(cpqReportQueryBo);
			cpqApproveBo.setStatus(APPROVED_REVIEW);
			CpqApproveBo create = this.create(cpqApproveBo);
			pageFlowControlInfo.setApproveRecordId(create.getId());
		}
		return pageFlowControlInfo;
	}

	/**
	 * 验证审批用户及其角色
	 *
	 * @param cpqReportQueryBo
	 * @param webQueryBo
	 * @return
	 */
	private CpqPageFlowControlInfo checkApproveUserAndRole(CpqReportQueryBo cpqReportQueryBo,
			CpqReportQueryBo.WebQueryBo webQueryBo) {
		// 验证审批用户密码
		List<SystemRole> systemRoles = null;
		// 这个用户验证包封装的真的是难用！！！！！！！！！！！！！！
		try {
			String approveUser = webQueryBo.getApproveUser();
			String approvePassword = webQueryBo.getApprovePassword();
			log.info("checkApproveUserAndRole  LoginValidateUtil.validate  params  userName = [{}]   approvePassword = [{}] ",approveUser,approvePassword);
			String validateResult = LoginValidateUtil.validate(approveUser,approvePassword);
			log.info("checkApproveUserAndRole  LoginValidateUtil.validate  result = {} ",validateResult);
			systemRoles = LoginServer.analysis(validateResult);
		} catch (ConnectTimeoutException e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(CHECKFILED_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		} catch (SocketTimeoutException e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(CHECKFILED_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		} catch (Exception e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(e.getMessage());
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		}
		if (null == systemRoles) {
			CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
			pageFlowControlInfo.setResMsg(NO_PRIVILEGE_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		}
		String orgId = UserConfigUtils.getTopOrgCode();
		for (SystemRole systemRole : systemRoles) {
			// 验证是否用审批权限；
			if (StringUtils.equals(CHECKROLE_ID + orgId, systemRole.getId())) {
				CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
				pageFlowControlInfo.setResMsg(CHECKSUCCESS_MSG);
				pageFlowControlInfo.setResCode(Constant.REVIEW_SYN_OK);
				return pageFlowControlInfo;
			}
		}
		CpqPageFlowControlInfo pageFlowControlInfo = CpqPageFlowControlInfo.build(cpqReportQueryBo);
		pageFlowControlInfo.setResMsg(NO_PRIVILEGE_MSG);
		pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
		return pageFlowControlInfo;
	}
	
	
	/**
	 * 构造审批业务对象
	 *
	 * @param cpqReportQueryBo
	 * @return
	 */
	private CpqApproveBo buildCpqApproveBo(CpqReportQueryBo cpqReportQueryBo) {
		CpqReportQueryBo.WebQueryBo webQueryBo = cpqReportQueryBo.getWebQueryBo();
		CpqApproveBo approveBo = new CpqApproveBo();
		approveBo.setArchiveId(webQueryBo.getAuthArchiveId());
		approveBo.setAssocbsnssData(webQueryBo.getRelationBaseData());
		approveBo.setClientName(cpqReportQueryBo.getClientName());
		approveBo.setCertNo(cpqReportQueryBo.getCertNo());
		approveBo.setCertType(cpqReportQueryBo.getCertType());
		approveBo.setCreditUser(webQueryBo.getCreditUser());
		approveBo.setOperator(cpqReportQueryBo.getOperator());
		approveBo.setOperOrg(webQueryBo.getQueryOrg());
		approveBo.setOperTime(new Date());
		approveBo.setQryReason(cpqReportQueryBo.getQueryReasonId());
		approveBo.setQueryFormat(webQueryBo.getReportVersion());
		approveBo.setQueryTime(new Date());
		approveBo.setRekType(webQueryBo.getApproveWay());
		approveBo.setRekUser(webQueryBo.getApproveUser());
		approveBo.setRekOrg(webQueryBo.getApproveOrg());
		CpqReportResultType reportResultType = webQueryBo.getReportResultType();
		approveBo.setResultType(reportResultType.getCode());
		approveBo.setQueryType("0");
		approveBo.setRekTime(new Date());
		approveBo.setBoCommonField(cpqReportQueryBo);
		return approveBo;
	}
	

}
