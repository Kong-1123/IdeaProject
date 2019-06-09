/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapproveflow.service.impl;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import cn.com.dhcc.credit.platform.util.JPAParamGroup;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo.Menu;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqPageFlowControlInfo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.queryapproveflow.dao.CeqApproveDao;
import cn.com.dhcc.creditquery.ent.queryapproveflow.entity.CeqApprove;
import cn.com.dhcc.creditquery.ent.queryapproveflow.service.CeqApproveFlowService;
import cn.com.dhcc.platformmiddleware.login.LoginServer;
import cn.com.dhcc.platformmiddleware.vo.SystemRole;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.CpqReportResultType;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.KeyProperties;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.PageUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 *  企业查询审批服务-企业查询审批管理服务实现类
 * @author yahongcai
 * @date 2019年2月23日
 */
@Slf4j
@Transactional(value = "transactionManager")
@Service
public class CeqApproveFlowServiceImpl  implements CeqApproveFlowService{
	private static String MENU_APPROVE_RECEIVE = "30020103";
	private static String MENU_APPROVE_RECEIVE_NAME = "审批任务领取";
	private static String MENU_APPROVE_RECEIVE_URL = "/creditenterprisequeryweb/checkinfo/list";

	private static String MENU_APPROVE_RECEIVE_URL_MICRO = "/creditent-checkinfo";

	/**
	 * 是否为微服务版本
	 */
	private static final  String IS_MICRO_SERVICE_CONFIG_KEY = "isMicroService";
	/**
	 * 审批任务管理，取审批任务菜单ID
	 */
	private static String MENU_APPROVE_TASK = "30020102";
	private static String MENU_APPROVE_TASK_NAME = "审批任务管理";

	private static String MENU_APPROVE_TASK_URL = "/creditenterprisequeryweb/checkinfotask/list";
	private static String MENU_APPROVE_TASK_URL_MICRO = "/creditent-checkinfotask";
	@Autowired
	private CeqApproveDao ceqApproveDao;
	@LogOperation("新增征信查询审批记录")
	@Override
	public CeqApproveBo create(CeqApproveBo ceqApproveBo) {
		log.info("CeqApproveFlowServiceImpl.create start ceqApproveBo={}",ceqApproveBo);
		CeqApprove ceqApprove = ClassCloneUtil.copyObject(ceqApproveBo, CeqApprove.class);
		try {
			ceqApprove = ceqApproveDao.save(ceqApprove);
			ceqApproveBo = ClassCloneUtil.copyObject(ceqApprove, CeqApproveBo.class);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.create error={},ceqApprove={} ",e,ceqApprove);
		}
		log.info("CeqApproveFlowServiceImpl.create end,ceqApproveBo={}",ceqApproveBo);
		return ceqApproveBo;
	}
	@LogOperation("修改征信查询审批记录")
	@Override
	public int update(CeqApproveBo ceqApproveBo) {
		log.info("CeqApproveFlowServiceImpl.update start,ceqApproveBo={}",ceqApproveBo);
		CeqApprove cpqApprove = ClassCloneUtil.copyObject(ceqApproveBo, CeqApprove.class);
		int resultFlg = 0;
		try {
			ceqApproveDao.save(cpqApprove);
		} catch (Exception e) {
			log.error("CeqApproveFlowServiceImpl.update error={},ceqApprove={} ",e,cpqApprove);
			resultFlg = -1;
		}
		log.info("CeqApproveFlowServiceImpl.update end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@LogOperation("批量更新征信查询审批记录")
	@Override
	public int batchUpdate(List<CeqApproveBo> ceqApproveBos) {
		log.info("CeqApproveFlowServiceImpl.batchUpdate start,ceqApproveBos={}",ceqApproveBos);
		List<CeqApprove> cpqApproveList = ClassCloneUtil.copyIterableObject(ceqApproveBos, CeqApprove.class);
		int resultFlg = 0;
		List<List<CeqApprove>> splitCeqApproveList = null;
		try {
			splitCeqApproveList = Lists.partition(cpqApproveList, 900);
    		for(int i=0;i<splitCeqApproveList.size();i++) {
    			ceqApproveDao.saveAll(splitCeqApproveList.get(i));
    		}
		} catch (Exception e) {
			log.error("CeqApproveFlowServiceImpl.batchUpdate error={},splitCeqApproveList={} ",e,splitCeqApproveList);
			resultFlg = -1;
		}
		log.info("CeqApproveFlowServiceImpl.batchUpdate end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@Override
	public CeqApproveBo findCeqApproveById(String ceqApproveId) {
		log.info("CeqApproveFlowServiceImpl.findCeqApproveById start,ceqApproveId ={}",ceqApproveId);
		Optional<CeqApprove> ceqApprove = null;
		CeqApproveBo ceqApproveBo = null;
		try {
			ceqApprove = ceqApproveDao.findById(ceqApproveId);
			ceqApproveBo = ClassCloneUtil.copyObject(ceqApprove, CeqApproveBo.class);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.findCeqApproveById error={},ceqApprove={} ",e,ceqApprove);
		}
		log.info("CeqApproveFlowServiceImpl.findCeqApproveById end,ceqApproveBo ={}",ceqApproveBo);
		return ceqApproveBo;
	}

	@Override
	public List<CeqApproveBo> findCeqApprovesByIds(List<String> ceqApproveIds) {
		log.info("CeqApproveFlowServiceImpl.findCeqApprovesByIds start,ceqApproveIds ={}",ceqApproveIds);
	
		List<CeqApprove> ceqApproveList = new ArrayList<CeqApprove>();
		List<CeqApproveBo> ceqApproveBoList = new ArrayList<CeqApproveBo>();
		List<List<String>> splitIdList = Lists.partition(ceqApproveIds, 900);
		try {
			for(int i=0;i<splitIdList.size();i++) {
				List<CeqApprove> splitCeqApproveList = ceqApproveDao.findCeqApprovesByIds(ceqApproveIds);
				ceqApproveList.addAll(splitCeqApproveList);
			}
			ceqApproveBoList = ClassCloneUtil.copyIterableObject(ceqApproveList, CeqApproveBo.class);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.findCeqApprovesByIds error={},ceqApproveList={} ",e,ceqApproveList);
		}
		log.info("CeqApproveFlowServiceImpl.findCeqApprovesByIds end,ceqApproveBoList ={}",ceqApproveBoList);
		return ceqApproveBoList;
	}

	@Override
	public Page<CeqApproveBo> getPage(JPAParamGroup jpaParamGroup, int pageNumber, int pageSize, String direction,
			String orderBy) throws Exception {
		log.info("CeqApproveFlowServiceImpl.getPage start,jpaParamGroup ={}, pageNumber ={},pageSize ={}, direction ={}, orderBy ={}"
				,jpaParamGroup,pageNumber,pageSize,direction,orderBy);
		Page<CeqApprove> ceqApprovePage = null;
		Page<CeqApproveBo> ceqApproveBoPage = null;
		try {
			ceqApprovePage = PageUtil.getPageByJPA(jpaParamGroup, pageNumber, pageSize, direction, orderBy, ceqApproveDao,
					CeqApprove.class);
			ceqApproveBoPage = ClassCloneUtil.copyPage(ceqApprovePage, CeqApproveBo.class);
		}catch(Exception e) {
			log.error("CpqApproveFlowServiceImpl.getPage error={},cpqApprovePage={},cpqApproveBoPage={} ",e,ceqApprovePage,ceqApproveBoPage);
		}
		log.info("CpqApproveFlowServiceImpl.getPage end, cpqApproveBoPage={}",ceqApproveBoPage);
		return ceqApproveBoPage;
	}

	@Override
	public CeqShortcutBo getApproveShortCut(String userName, List<String> menuIdList, List<String> deptIdlist) {
		log.info("CeqApproveFlowServiceImpl.getApproveShortCut start,userName ={}, menuIdList ={},deptIdlist ={}",
				userName,menuIdList,deptIdlist);
		CeqShortcutBo ceqShortcutBo = new CeqShortcutBo();
		if (null == menuIdList) {
			ceqShortcutBo.setTitle("审批任务");
			return ceqShortcutBo;
		}
		List<Menu> menuList = new ArrayList<Menu>();
		Menu receiveTaskMenu = new Menu();
		Menu ceqApproveTaskMenu = new Menu();
		try {
			ceqShortcutBo.setTitle("审批任务");
			boolean receiveFlag = isShowMenu(MENU_APPROVE_RECEIVE, menuIdList);
			if (receiveFlag) {
				handleMenu(deptIdlist, receiveTaskMenu,userName);
				menuList.add(receiveTaskMenu);
			}
			boolean taskFlag = isShowMenu(MENU_APPROVE_TASK, menuIdList);
			if (taskFlag) {
				handleTaskMenuByUser(userName, ceqApproveTaskMenu);
				menuList.add(ceqApproveTaskMenu);
			}
			ceqShortcutBo.setMenus(menuList);
		} catch (Exception e) {
			log.error("CeqApproveFlowServiceImpl.getApproveShortCut  error={}",e);
		}
		log.info("CeqApproveFlowServiceImpl.getApproveShortCut end ceqShortcutBo={}",ceqShortcutBo);
		return ceqShortcutBo;
	}
	/**
	 * 是否展示菜单信息
	 * @param menu
	 * @param menuIdList
	 * @return  成功返回true,否则返回false
	 */
	private boolean isShowMenu(String menu, List<String> menuIdList) {
		log.info("CeqApproveFlowServiceImpl.isShowMenu start,menu={},menuIdList={}",menu,menuIdList);
		boolean contrainFlg = false;
		if (menuIdList.contains(menu)) {
			contrainFlg = true;
		}
		log.info("CeqApproveFlowServiceImpl.isShowMenu end,contrainFlg={}",contrainFlg);
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
		log.info("CeqApproveFlowServiceImpl.handleMenu start,userName ={},receiveTaskMenu={},deptIdlist={}",userName,receiveTaskMenu,deptIdlist);
		int receiveCount = getReceiveTask(deptIdlist,userName);
		receiveTaskMenu.setCount(receiveCount);
		String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
		String url = MENU_APPROVE_RECEIVE_URL;
		if(Boolean.parseBoolean(property)){
			url = MENU_APPROVE_RECEIVE_URL_MICRO;
		}
		receiveTaskMenu.setLink(url);
		receiveTaskMenu.setName(MENU_APPROVE_RECEIVE_NAME);
		log.info("CeqApproveFlowServiceImpl.handleMenu end,receiveTaskMenu ={}",receiveTaskMenu);
		return receiveTaskMenu;
	}
	
	/**
	 * 辖内领取任务数
	 * @param deptIdlist
	 * @return @see int
	 */
	private int getReceiveTask(List<String> deptIdlist,String userName) {
		log.info("CeqApproveFlowServiceImpl.getReceiveTask start,userName ={},deptIdlist={}",userName,deptIdlist);
		List<CeqApprove> ceqApproveList = null;
		try {
			ceqApproveList = ceqApproveDao.findReceiveTask(deptIdlist,userName);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.getReceiveTask error,e={}",e);
		}
		int size = 0 ;
		if (ceqApproveList != null) {
			size = ceqApproveList.size();
		}
		return size;
	}
	
	/**
	 * 审批任务管理
	 * @param userName
	 * @param ceqApproveTaskMenu
	 * @return @see Menu
	 */
	private Menu handleTaskMenuByUser(String userName, Menu ceqApproveTaskMenu) {
		log.info("CeqApproveFlowServiceImpl.handleTaskMenuByUser start,userName ={},ceqApproveTaskMenu={}",userName,ceqApproveTaskMenu);
		int taskCount = getCeqApproveTaskByUser(userName);
		ceqApproveTaskMenu.setCount(taskCount);
		String property = KeyProperties.getProperty(IS_MICRO_SERVICE_CONFIG_KEY);
		String url = MENU_APPROVE_TASK_URL;
		if(Boolean.parseBoolean(property)){
			url = MENU_APPROVE_TASK_URL_MICRO;
		}
		ceqApproveTaskMenu.setLink(url);
		ceqApproveTaskMenu.setName(MENU_APPROVE_TASK_NAME);
		log.info("CeqApproveFlowServiceImpl.handleTaskMenuByUser end,ceqApproveTaskMenu ={}",ceqApproveTaskMenu);
		return ceqApproveTaskMenu;
	}
	
	/**
	 * 审批任务数
	 * @param userName
	 * @return @see int
	 */
	private int getCeqApproveTaskByUser(String userName) {
		log.info("CeqApproveFlowServiceImpl.getCeqApproveTaskByUser start,userName ={}",userName);
		List<CeqApprove> ceqApproveList = null;
		try {
			ceqApproveList = ceqApproveDao.findTaskByUser(userName);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.getCeqApproveTaskByUser error={},ceqApproveList={}",e,ceqApproveList);
		}
		int size = 0 ;
		if (ceqApproveList != null) {
			size = ceqApproveList.size();
		}
		log.info("CeqApproveFlowServiceImpl.getCeqApproveTaskByUser end,size ={}",size);
		return size;
	}

	@Override
	public List<CeqApproveBo> findCeqApprovesByArchiveId(String archiveId) {
		log.info("CeqApproveFlowServiceImpl.findCeqApprovesByArchiveId start,archiveId ={}",archiveId);
		List<CeqApprove> ceqApproveList = null;
		List<CeqApproveBo> ceqApproveBoList = null;
		try {
			ceqApproveList = ceqApproveDao.findCeqApprovesByArchiveId(archiveId);
			ceqApproveBoList = ClassCloneUtil.copyIterableObject(ceqApproveList, CeqApproveBo.class);
		}catch(Exception e) {
			log.error("CeqApproveFlowServiceImpl.findCeqApprovesByArchiveId error={}, ceqApproveBoList={}",e,ceqApproveBoList);
		}
		log.info("CeqApproveFlowServiceImpl.findCeqApprovesByArchiveId end,ceqApproveBoList ={}",ceqApproveBoList);
		return ceqApproveBoList;
	}
	@LogOperation("根据征信查询审批记录Id更新授权档案Id")
	@Override
	public int updateArchiveIdByCeqApproveId(String ceqApproveId, String archiveId) {
		log.info("CeqApproveFlowServiceImpl.updateArchiveIdByCeqApproveId start,ceqApproveId={},archiveId={}",ceqApproveId,archiveId);
		int resultFlg = 0;
		try {
			ceqApproveDao.updateArchiveIdByCeqApproveId(ceqApproveId, archiveId);
		} catch (Exception e) {
			log.error("CeqApproveFlowServiceImpl.updateArchiveIdByCpqApproveId  error={}",e);
			resultFlg = -1;
		}
		log.info("CeqApproveFlowServiceImpl.updateArchiveIdByCpqApproveId end,resultFlg={}",resultFlg);
		return resultFlg;
	}
	@Override
	public List<CeqApproveBo> getCeqApprovesBySearchParams(Map<String, Object> searchParams) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean feedbackCeqApproveResult(CeqApproveBo ceqApproveBo) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean ceqApproveRejected(CeqApproveBo ceqApproveBo, String backPostFlg) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean ceqApproveCancelled(CeqApproveBo ceqApproveBo) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService#findCpqApproveByReqId(java.lang.String)
	 */
	@Override
	public CeqApproveBo findCeqApproveByReqId(String reqId) {
		CeqApprove ceqApprove = ceqApproveDao.findCeqApproveByReqId(reqId);
		return ClassCloneUtil.copyObject(ceqApprove, CeqApproveBo.class);
	}
	
	
	
	/**
	 * 同步审批
	 *
	 * @param ceqReportQueryBo
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public CeqPageFlowControlInfo sysncApprove(CeqReportQueryBo ceqReportQueryBo) throws Exception {
		// 进行同步审批
		CeqReportQueryBo.WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
		String queryUser = ceqReportQueryBo.getOperator();
		String approveUser = webQueryBo.getApproveUser();
		// 判断查询员是否属于复核员的辖内机构
		// 访问平台获取审批用户详细信息
		SystemUser systemUser = LoginValidateUtil.findUserByUserName(approveUser);
		if(null == systemUser) {
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
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
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(NON_EXISTENT);
			pageFlowControlInfo.setResCode(FAILURE_CODE);
			return pageFlowControlInfo;
		}
		if (queryUser.equals(approveUser)) {
			// 查询用户与审批用户相同，返回相应信息
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(SAME_USER_MSG);
			pageFlowControlInfo.setResCode(FAILURE_CODE);
			return pageFlowControlInfo;
		}
		CeqPageFlowControlInfo pageFlowControlInfo = checkApproveUserAndRole(ceqReportQueryBo, webQueryBo);
		// 审批通过，新增审批记录
		if (StringUtils.equals(pageFlowControlInfo.getResCode(), Constant.REVIEW_SYN_OK)) {
			CeqApproveBo ceqApproveBo = buildCeqApproveBo(ceqReportQueryBo);
			ceqApproveBo.setStatus(APPROVED_REVIEW);
			CeqApproveBo create = this.create(ceqApproveBo);
			pageFlowControlInfo.setApproveRecordId(create.getId());
		}
		return pageFlowControlInfo;
	}

	/**
	 * 验证审批用户及其角色
	 *
	 * @param ceqReportQueryBo
	 * @param webQueryBo
	 * @return
	 */
	private CeqPageFlowControlInfo checkApproveUserAndRole(CeqReportQueryBo ceqReportQueryBo,
			CeqReportQueryBo.WebQueryBo webQueryBo) {
		// 验证审批用户密码
		List<SystemRole> systemRoles = null;
		// 这个用户验证包封装的真的是难用！！！！！！！！！！！！！！
		try {
			String validateResult = LoginValidateUtil.validate(webQueryBo.getApproveUser(),
					webQueryBo.getApprovePassword());
			systemRoles = LoginServer.analysis(validateResult);
		} catch (ConnectTimeoutException e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(CHECKFILED_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		} catch (SocketTimeoutException e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(CHECKFILED_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		} catch (Exception e) {
			log.error("LoginValidateUtil.validate field e = ", e);
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(e.getMessage());
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		}
		if (null == systemRoles) {
			CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
			pageFlowControlInfo.setResMsg(NO_PRIVILEGE_MSG);
			pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
			return pageFlowControlInfo;
		}
		String orgId = UserConfigUtils.getTopOrgCode();
		for (SystemRole systemRole : systemRoles) {
			// 验证是否用审批权限；
			if (StringUtils.equals(CHECKROLE_ID + orgId, systemRole.getId())) {
				CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
				pageFlowControlInfo.setResMsg(CHECKSUCCESS_MSG);
				pageFlowControlInfo.setResCode(Constant.REVIEW_SYN_OK);
				return pageFlowControlInfo;
			}
		}
		CeqPageFlowControlInfo pageFlowControlInfo = CeqPageFlowControlInfo.build(ceqReportQueryBo);
		pageFlowControlInfo.setResMsg(NO_PRIVILEGE_MSG);
		pageFlowControlInfo.setResCode(Constant.REVIEW_ERROR);
		return pageFlowControlInfo;
	}
	
	
	/**
	 * 构造审批业务对象
	 *
	 * @param ceqReportQueryBo
	 * @return
	 */
	private CeqApproveBo buildCeqApproveBo(CeqReportQueryBo ceqReportQueryBo) {
		CeqReportQueryBo.WebQueryBo webQueryBo = ceqReportQueryBo.getWebQueryBo();
		CeqApproveBo approveBo = new CeqApproveBo();
		approveBo.setArchiveId(webQueryBo.getAuthArchiveId());
		approveBo.setAssocbsnssData(webQueryBo.getRelationBaseData());
		approveBo.setSignCode(ceqReportQueryBo.getSignCode());
		approveBo.setOrgCreditCode(ceqReportQueryBo.getOrgCreditCode());
		approveBo.setEnterpriseName(ceqReportQueryBo.getEnterpriseName());
		approveBo.setOrgInstCode(ceqReportQueryBo.getOrgInstCode());
		approveBo.setUniformSocialCredCode(ceqReportQueryBo.getUniformSocialCredCode());
		approveBo.setGsRegiNo(ceqReportQueryBo.getGsRegiNo());
		approveBo.setDsRegiNo(ceqReportQueryBo.getDsRegiNo());
		approveBo.setFrgCorpNo(ceqReportQueryBo.getFrgCorpNo());
		approveBo.setRegiTypeCode(ceqReportQueryBo.getRegiTypeCode());
		approveBo.setCreditUser(webQueryBo.getCreditUser());
		approveBo.setOperator(ceqReportQueryBo.getOperator());
		approveBo.setOperOrg(webQueryBo.getQueryOrg());
		approveBo.setOperTime(new Date());
		approveBo.setQryReason(ceqReportQueryBo.getQueryReasonId());
		approveBo.setQueryFormat(webQueryBo.getReportVersion());
		approveBo.setQueryTime(new Date());
		approveBo.setRekType(webQueryBo.getApproveWay());
		approveBo.setRekUser(webQueryBo.getApproveUser());
		approveBo.setRekOrg(webQueryBo.getApproveOrg());
		CpqReportResultType reportResultType = webQueryBo.getReportResultType();
		approveBo.setResultType(reportResultType.getCode());
		approveBo.setQueryType("0");
		approveBo.setRekTime(new Date());
		approveBo.setBoCommonField(ceqReportQueryBo);
		return approveBo;
	}
	

}
