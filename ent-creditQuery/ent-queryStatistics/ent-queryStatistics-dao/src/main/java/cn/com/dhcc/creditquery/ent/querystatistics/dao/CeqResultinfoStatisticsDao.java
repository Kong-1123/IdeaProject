package cn.com.dhcc.creditquery.ent.querystatistics.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.querystatistics.entity.CeqResultinfoStatistics;

/**
 * <查询结果-dao层>
 * 
 * @author Mingyu.Li
 * @date 2018年3月10日
 * 
 */
@SuppressWarnings("unused")
public interface CeqResultinfoStatisticsDao
		extends PagingAndSortingRepository<CeqResultinfoStatistics, String>, JpaSpecificationExecutor<CeqResultinfoStatistics> {

	@Query("select r from CeqResultinfoStatistics r where r.id in(?1)")
	List<CeqResultinfoStatistics> findByIdIn(List<String> ids);

	@Query("update CeqResultinfoStatistics set assocbsnssData = ?2,archiveRevise= ?3 where id = ?1")
	@Modifying
	void updateAssociationById(String id, String associate, String archiveRevise);

	@Query("update CeqResultinfoStatistics set autharchiveId = ?2 where id = ?1")
	@Modifying
	void updateArchiveIdById(String id, String archiveId);

	@Query("SELECT MAX(c.queryTime) FROM CeqResultinfoStatistics c WHERE c.autharchiveId = ?1")
	Date getLastQueryTimeByArchiveId(String archive);

	@Query("select r from CeqResultinfoStatistics r where r.creditId = (?1)")
	List<CeqResultinfoStatistics> findbyCreditId(String creditId);

	@Query("from CeqResultinfoStatistics where creditUser = ?1")
	List<CeqResultinfoStatistics> findByCcuser(String ccuser);

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
	@Query("from CeqResultinfoStatistics c where c.qryReason !=?1 and c.operOrg in(?2) and c.autharchiveId is null")
	List<CeqResultinfoStatistics> findByArchiveCount( String qryReason, List<String> deptCodes);

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
	@Query("from CeqResultinfoStatistics c where c.operator=?1 and c.operOrg=?2 and c.qryReason=?3")
	List<CeqResultinfoStatistics> findByArchiveCount(String userName, String orgId, String qryReason);
}
