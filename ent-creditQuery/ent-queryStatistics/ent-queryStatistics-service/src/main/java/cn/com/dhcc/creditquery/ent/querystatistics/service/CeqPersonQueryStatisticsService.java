/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.querystatistics.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqAlertBo;
import cn.com.dhcc.creditquery.ent.query.bo.querystatistics.CeqQueryStatisticsBo;
/**
 * 	企业征信查询统计服务
 * @author Jerry.chen
 * @date 2019年2月16日
 */
public interface CeqPersonQueryStatisticsService {
	/**
	 * queryStatisticType 0-按用户
	 */
	final static String QUERYSTATISTICTYPE_USER ="0";
	/**
	 * queryStatisticType 1-按机构
	 */
	final static String QUERYSTATISTICTYPE_ORG ="1";
	/**
	 * queryOrgScope  1-辖内所有预警记录  2-本机构所有预警记录 3-辖内直属预警记录
	 */
	final static String  QUERYORGSCOPE_WITHIN = "1";
	/**
	 * queryOrgScope  2-本机构所有预警记录
	 */
	final static String  QUERYORGSCOPE_SELF = "2";
	/**
	 * queryOrgScope  3-辖内直属预警记录
	 */
	final static String  QUERYORGSCOPE_WITHIN_DIRECT = "3";
	/**
	 * 根据查询统计记录ID获取查询记录
	 * @param id
	 * @return  @see QueryStatisticsBo
	 * @author Jerry.chen
	 * @date 2019年2月16日
	 */
	CeqQueryStatisticsBo findQueryStatisticsById(String queryStatisticId);
     /**
      * 分页查询
      * @param searchParams
      * @param pageNumber
      * @param pageSize
      * @param direction
      * @param orderBy
      * @return  @see Page<QueryStatisticsBo>
      * @author Jerry.chen
      * @date 2019年2月16日
      */
	Page<CeqQueryStatisticsBo> getQueryStatisticPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String... orderBy);
    /**
     * 按用户/机构统计查询结果记录
     * @param searchParams
     * @param queryStatisticType 0-按用户  1-按机构
     * @return  @see List<Map<String, Object>>
     * @author Jerry.chen
     * @date 2019年2月16日
     */
	List<Map<String, Object>> queryStatisticsListGroupByUser(Map<String, Object> searchParams,String queryStatisticType);
	
	/**
	 * 根据预警记录ID获取预警记录
	 * @param alertId
	 * @return  @see CpqAlertBo
	 * @author Jerry.chen
	 * @date 2019年2月16日
	 */
	CeqAlertBo findCeqAlertById(String alertId);
    /**
     * 分页查询(按预警统计)
     * @param searchParams
     * @param pageNumber
     * @param pageSize
     * @param direction
     * @param orderBy
     * @return @see Page<CpqAlertBo>
     * @author Jerry.chen
     * @date 2019年2月16日
     */
    
	Page<CeqAlertBo> getCeqAlertPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
			String... orderBy);

	/**
	 * 根据查询参数查询预警信息
	 * @param searchParams
	 * @return  @see List<CpqAlertBo>
	 * @author Jerry.chen
	 * @date 2019年2月16日
	 */
	List<CeqAlertBo> findCeqAlertsByParams(Map<String, Object> searchParams);
    /**
     * 按预警原因分组统计预警记录
     * @param AlertReason
	 * @param deptCodes
     * @return @see Map<String,CpqAlertBo>
     * @author Jerry.chen
     * @date 2019年2月16日
     */
	Integer findCeqAlertGroupbyAleratReason(String AlertReason ,List<String> deptCodes);
	/**
	 * 根据机构及统计方式查询预警信息 
	 * @param queryOrgScope  1-辖内所有预警记录  2-本机构所有预警记录 3-辖内直属预警记录
	 * @param deptCode
	 * @param alertBeginDate
	 * @param alertEndDate
	 * @return  @see List<CpqAlertBo>
	 * @author Jerry.chen
	 * @date 2019年2月16日
	 */
	List<CeqAlertBo> findCeqAlerts(String queryOrgScope,String deptCode, Date alertBeginDate,Date alertEndDate);
	
	/**
	 * <获取到辖内直属的所有预警信息>
	 * @param deptCode
	 * @param alertDate
	 * @param bussDate
	 * @param alertDateFuntion
	 * @param bussDateFuntion
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年3月5日
	 */
	List<String[]> findByAllStat(List<String> deptCode, String alertDate, String bussDate,String alertDateFuntion, String bussDateFuntion);
	
	
	/**
	 * <获取到辖内所有的违规信息>
	 * @param ids
	 * @param bussDate
	 * @param alertDate
	 * @return
	 */
	List<String[]> findByTotal(String deptCode, String alertDate,String bussDate,String alertDateFuntion, String bussDateFuntion);
	/**
	 * <获取到本机构所有的违规信息>
	 * @param ids
	 * @param bussDate
	 * @param alertDate
	 * @return
	 */
	List<String[]> findByTotal4Agency(String deptCode, String alertDate,String bussDate,String alertDateFuntion, String bussDateFuntion);
	
	/**修改预警信息
	 * @param alter
	 * @author yuzhao.xue
	 * @date 2019年2月28日
	 */
	void updateAlert(CeqAlertBo alter);
	/**
	 * 解锁用户
	 * @param alter
	 * @author yuzhao.xue
	 * @date 2019年2月28日
	 */
	void unlockUser(CeqAlertBo alter);
	/**
	 * 清空预警次数
	 * @param userName
	 * @author yuzhao.xue
	 * @date 2019年2月28日
	 */
	void clearAlertCount(String userName);
	/**
	 * 锁定用户
	 * @param alter
	 * @author yuzhao.xue
	 * @date 2019年2月28日
	 */
	void lockedUser(CeqAlertBo alter);
	/**
	 * @param ids
	 * @return
	 * @author yuzhao.xue
	 * @date 2019年2月28日
	 */
	List<CeqAlertBo> findByIds(List<String> ids);
}