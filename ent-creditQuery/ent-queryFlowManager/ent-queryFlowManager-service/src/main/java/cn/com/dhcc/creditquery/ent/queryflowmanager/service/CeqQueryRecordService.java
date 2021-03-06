package cn.com.dhcc.creditquery.ent.queryflowmanager.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;

/**
 * 查询记录接口服务
 *
 * @author liulekang
 */
public interface CeqQueryRecordService {
	
	static final String REPORT_FORMAT_XML = "XML";
	 static final String REPORT_FORMAT_JSON = "JSON";
	 static final String REPORT_FORMAT_HTML = "HTML";
	 static final String REPORT_FORMAT_PDF = "PDF";

    /**
     * 档案补录的菜单ID
     */
    static final String ACHIVEMENUID = "30010103";

    /**
     * 菜单名称
     */
    static final String NAME = "档案补录";

    /**
     * 菜单地址
     */
    static final String MENULINK = "/creditenterprisequeryweb/archive/archiverevise";

    /**
     * 微服务菜单地址
     */
    static final String MENULINK_MICRO = "/creditent-archiverevise";

    /**
     * 是否为微服务版本
     */
    static final  String IS_MICRO_SERVICE_CONFIG_KEY = "isMicroService";

    /**
     * title信息
     */
    static final String TITLE = "授权补录";

    /**
     * 信用报告来源-本地
     */
    static final String LOCAL_SOURCE = "1";

    /**
     * 信用报告来源-人行
     */
    static final String REMOTE_SOURCE = "2";

    /**
     * 启用本地报告
     */
    static final String ENABLE = "0";

    /**
     * 新增信用报告查询记录
     *
     * @param cpqQueryRecordBo
     * @return {@link} CpqQueryRecordBo
     */
    CeqQueryRecordBo create(CeqQueryRecordBo cpqQueryRecordBo);

    /**
     * 修改信用报告查询记录
     *
     * @param cpqQueryRecordBo {@link} CpqQueryRecordBo
     * @return {@link} CpqQueryRecordBo
     */
    CeqQueryRecordBo update(CeqQueryRecordBo cpqQueryRecordBo);

    /**
     * 根据ID获取查询记录
     *
     * @param id
     * @return {@link} CpqQueryRecordBo
     */
    CeqQueryRecordBo findById(String id);

    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNumber
     * @param pageSize
     * @param direction
     * @param orderBy
     * @return {@link} Page<CpqQueryRecordBo>
     */
    Page<CeqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction, String orderBy);

    /**
     * 根据中征码及查询原因获取本地localDay时间段内的可用信用报告
     *
     * @param signCode
     * @param qryReason
     * @param topOrgCode
     * @param localDay
     * @return 信用报告
     */
    CeqQueryReportFlowBo getLocalReport(String signCode,String qryReason, String topOrgCode,String localDay);

    /**
     * 根据客户三标及查询原因判断本地是否存在可用信用报告
     *
     * @param signCode
     * @param qryReason
     * @param topOrgCode
     * @return
     */
    boolean isHaveLocalReport(String signCode,String qryReason, String topOrgCode,String localDay);

    /**
     * 根据查询记录ID获取信用报告
     *
     * @param id
     * @param reportType
     * @return
     */
    String getReportById(String id,String reportType);

    /**
     * 获取档案补录快捷菜单
     *
     * @param userName
     * @param deptCodes
     * @param menuIds
     * @return
     */
    CeqShortcutBo getArchiveShortcut(String userName, List<String> deptCodes, List<String> menuIds);

    /**
     * 补录档案，更新查询记录的关联档案ID
     *
     * @param id
     * @param archiveId
     */
    void updateArchiveIdById(String id, String archiveId);

    /**
     * 是否存在该征信用户的查询记录
     *
     * @param creditUser
     * @return
     */
    boolean isQueriedCreditUser(String creditUser);


    /**
     * 获取某一用户的查询数量
     *
     * @param username
     * @return
     */
    long getUserQueryNum(String username);

    /**
     * 根据参数信息批量查询
     *
     * @param searchParams
     * @return
     */
    List<CeqQueryRecordBo> findAll(Map<String, Object> searchParams);

    /**
     * 根据ID列表进行批量查询
     *
     * @param ids
     * @return
     */
    List<CeqQueryRecordBo> findByIds(List<String> ids);
    

    /**
     * 根据Id读取信用报告
     * @param recordId 查询记录ID
     * @param userName 用户名
     * @param orgCode 操作机构
     * @param reportType 报告类型
     * @return
     */
    String readReportById(String recordId, String userName, String orgCode,String reportType);
}
