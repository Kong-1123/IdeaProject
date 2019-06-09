/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.dao;

import cn.com.dhcc.creditquery.person.queryflowmanager.entity.CpqResultinfoFlowManager;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * <查询结果-dao层>
 *
 * @author Mingyu.Li
 * @date 2018年3月10日
 *
 */
public interface CpqResultinfoDao
        extends PagingAndSortingRepository<CpqResultinfoFlowManager, String>, JpaSpecificationExecutor<CpqResultinfoFlowManager> {

    @Query("select r from CpqResultinfoFlowManager r where r.id in(?1)")
    List<CpqResultinfoFlowManager> findByIdIn(List<String> ids);

    @Query("update CpqResultinfoFlowManager set assocbsnssData = ?2,archiveRevise= ?3 where id = ?1")
    @Modifying
    void updateAssociationById(String id, String associate, String archiveRevise);

    @Query("update CpqResultinfoFlowManager set autharchiveId = ?2 where id = ?1")
    @Modifying
    void updateArchiveIdById(String id, String archiveId);

    @Query("SELECT MAX(c.queryTime) FROM CpqResultinfoFlowManager c WHERE c.autharchiveId = ?1")
    Date getLastQueryTimeByArchiveId(String archive);

    @Query("select r from CpqResultinfoFlowManager r where r.creditId = (?1)")
    List<CpqResultinfoFlowManager> findbyCreditId(String creditId);

    @Query("from CpqResultinfoFlowManager where creditUser = ?1")
    List<CpqResultinfoFlowManager> findByCcuser(String ccuser);

    /**
     * <辖内档案补录快捷入口>
     *
     * @param qryReason
     * @param deptCodes
     * @return
     * @author Mingyu.Li
     * @date 2018年8月6日
     * @return int
     */
    @Query("from CpqResultinfoFlowManager c where c.qryReason !=?1 and c.operOrg in(?2) and c.autharchiveId is null")
    List<CpqResultinfoFlowManager> findByArchiveCount(String qryReason, List<String> deptCodes);

    /**
     * <本机构档案补录入口>
     *
     * @param userName
     * @param orgId
     * @param qryReason
     * @return
     * @author Mingyu.Li
     * @date 2018年8月6日
     * @return int
     */
    @Query("from CpqResultinfoFlowManager c where c.operator=?1 and c.operOrg=?2 and c.qryReason=?3")
    List<CpqResultinfoFlowManager> findByArchiveCount(String userName, String orgId, String qryReason);

    @Query("from CpqResultinfoFlowManager where certNo like CONCAT(?1,'%',?2) and certType=?3 and clientName=?4 and queryTime>=?5 and queryTime<=?6 and creditUser=?7")
    List<CpqResultinfoFlowManager> findToAbnormalThree(String certNoHead, String certNoTail, String certType, String clientName,
                                            Timestamp startTime, Timestamp endTime, String creditUser);

    @Query("from CpqResultinfoFlowManager where certNo like CONCAT(?1,'%',?2) and certType=?3 and clientName=?4 and qryReason=?5 and queryFormat=?6 and queryTime>=?7 and queryTime<=?8 and creditUser=?9")
    List<CpqResultinfoFlowManager> findToAbnormalTwo(String certNoHead, String certNoTail, String certType, String clientName,
                                          String qryReason, String queryFormat, Timestamp startTime, Timestamp endTime, String creditUser);
}
