package cn.com.dhcc.creditquery.ent.authorizemanager.service;

import java.util.List;
import java.util.Map;

import org.redisson.api.RLock;
import org.springframework.data.domain.Page;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;

/**
 * 授权管理服务-企业查询授权信息管理服务接口类
 * 
 * @author Jerry.chen
 * @date 2019年2月15日
 */
public interface CeqAuthorizeManagerService {

	/**
	 * 创建企业查询授权信息记录
	 * @param CeqAuthorizeManagerBo
	 * @return @see CeqAuthorizeManagerBo
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	CeqAuthorizeManagerBo create(CeqAuthorizeManagerBo ceqAuthorizeManagerBo);

	/**
	 * 修改企业查询授权信息记录
	 * @param CeqAuthorizeManagerBo
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int update(CeqAuthorizeManagerBo ceqAuthorizeManagerBo);

	/**
	 * 根据企业查询授权ID,删除一条授权信息记录
	 * @param ceqAuthorizeId
	 * @return int 成功时，该值大于等于0，失败则小于0
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteById(String ceqAuthorizeId);

	/**
	 * 批量删除企业查询授权信息记录
	 * @param ceqAuthorizeIds List<String>
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	int deleteByIds(List<String> ceqAuthorizeIds);

	/**
	 * 查询单条授权记录
	 * @param authorizeId 授权记录ID
	 * @return 授权记录
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	CeqAuthorizeManagerBo findById(String authorizeId);

	/**
	 * 分页查询
	 * @param searchParams
	 * @param pageNumber
	 * @param pageSize
	 * @param direction
	 * @param orderBy
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	Page<CeqAuthorizeManagerBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String direction, String orderBy);

	/**
	 * 更新授权信息记录状态
	 * @param status
	 * @param authorizeId
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	void updateStatus(String status, String authorizeId);

	/**
	 * 根据中证码查询授权信息记录
	 * @param signCode  中证码
	 * @return 授权信息记录集合
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	List<CeqAuthorizeManagerBo> findBySignCode(String signCode);

	/**
	 * 批量查询授权信息记录(查询条件导出)
	 * @param param
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	List<CeqAuthorizeManagerBo> findAll(Map<String, Object> param);

	/**
	 * 根据授权信息记录ID集合，查询授权信息记录集合（默认全部导出）
	 * @param authorizeIdList
	 * @return
	 * @author Jerry.chen
	 * @date 2019年2月15日
	 */
	List<CeqAuthorizeManagerBo> findByIds(List<String> authorizeIdList);

	/**
	 * 从reids根据id获取报告数量
	 * @param authorizeId
	 * @return
	 * @author sjk
	 * @date 2019年3月19日
	 */
	long getQueryNumByRedis(String authorizeId);
	
	/**
	 * 获取锁用于保存档案文件信息记录
	 * @param userId 操作用户
	 * @return
	 * @author sjk
	 * @date 2019年2月25日
	 */
	RLock getRlockForSavePaer(String userId);

	/**
	 * 获取锁用于更新档案信息中的报告数量
	 * @return
	 * @author sjk
	 * @date 2019年2月25日
	 */
	RLock getRlockForUpdate();

	/**
	 * 更新档案信息中的报告数量
	 * @param authorizeId
	 * @return
	 * @author sjk
	 * @date 2019年2月25日
	 */
	boolean updateQueryNumByRedis(String authorizeId);

	/**
	 * 获取档案文件类型
	 * @param topOrg
	 * @return
	 * @author sjk
	 * @date 2019年3月20日
	 */
	JSONObject getFileTypeForArchive(String topOrg);
	
	/**
	 * 根据reqid获取授权信息
	 * @param reqId
	 * @return
	 * @author sjk
	 * @date 2019年3月26日
	 */
	CeqAuthorizeManagerBo findByReqId(String reqId);

	
}
