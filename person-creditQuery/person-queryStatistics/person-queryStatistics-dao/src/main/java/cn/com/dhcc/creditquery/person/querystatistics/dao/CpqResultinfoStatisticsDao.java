package cn.com.dhcc.creditquery.person.querystatistics.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.person.querystatistics.entity.CpqResultinfoStatistics;

/**
 * <查询结果-dao层>
 * 
 * @author Mingyu.Li
 * @date 2018年3月10日
 * 
 */
public interface CpqResultinfoStatisticsDao
		extends PagingAndSortingRepository<CpqResultinfoStatistics, String>, JpaSpecificationExecutor<CpqResultinfoStatistics> {

	@Query("select r from CpqResultinfoStatistics r where r.id in(?1)")
	List<CpqResultinfoStatistics> findByIdIn(List<String> ids);

	@Query("update CpqResultinfoStatistics set assocbsnssData = ?2,archiveRevise= ?3 where id = ?1")
	@Modifying
	void updateAssociationById(String id, String associate, String archiveRevise);

	@Query("update CpqResultinfoStatistics set autharchiveId = ?2 where id = ?1")
	@Modifying
	void updateArchiveIdById(String id, String archiveId);

	@Query("SELECT MAX(c.queryTime) FROM CpqResultinfoStatistics c WHERE c.autharchiveId = ?1")
	Date getLastQueryTimeByArchiveId(String archive);

	@Query("select r from CpqResultinfoStatistics r where r.creditId = (?1)")
	List<CpqResultinfoStatistics> findbyCreditId(String creditId);

	@Query("from CpqResultinfoStatistics where creditUser = ?1")
	List<CpqResultinfoStatistics> findByCcuser(String ccuser);

	/**
	 * <辖内档案补录快捷入口>
	 * 
	 * @param userName
	 * @param qryReason
	 * @return
	 * @author Mingyu.Li
	 * @param userName
	 * @date 2018年8月6日
	 * @return int
	 */
	@Query("from CpqResultinfoStatistics c where c.qryReason !=?1 and c.operOrg in(?2) and c.autharchiveId is null")
	List<CpqResultinfoStatistics> findByArchiveCount( String qryReason, List<String> deptCodes);

	/**
	 * <本机构档案补录入口>
	 * 
	 * @param userName
	 * @param date
	 * @param orgId
	 * @return
	 * @author Mingyu.Li
	 * @date 2018年8月6日
	 * @return int
	 */
	@Query("from CpqResultinfoStatistics c where c.operator=?1 and c.operOrg=?2 and c.qryReason=?3")
	List<CpqResultinfoStatistics> findByArchiveCount(String userName, String orgId, String qryReason);

	@Query("from CpqResultinfoStatistics where certNo like CONCAT(?1,'%',?2) and certType=?3 and clientName=?4 and queryTime>=?5 and queryTime<=?6 and creditUser=?7")
	List<CpqResultinfoStatistics> findToAbnormalThree(String certNoHead, String certNoTail, String certType, String clientName,
			Timestamp startTime, Timestamp endTime, String creditUser);

	@Query("from CpqResultinfoStatistics where certNo like CONCAT(?1,'%',?2) and certType=?3 and clientName=?4 and qryReason=?5 and queryFormat=?6 and queryTime>=?7 and queryTime<=?8 and creditUser=?9")
	List<CpqResultinfoStatistics> findToAbnormalTwo(String certNoHead, String certNoTail, String certType, String clientName,
			String qryReason, String queryFormat, Timestamp startTime, Timestamp endTime, String creditUser);
}
