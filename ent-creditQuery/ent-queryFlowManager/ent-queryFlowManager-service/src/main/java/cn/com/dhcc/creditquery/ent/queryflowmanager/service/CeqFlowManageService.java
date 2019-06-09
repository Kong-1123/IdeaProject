package cn.com.dhcc.creditquery.ent.queryflowmanager.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqShortcutBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqPageFlowControlInfo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqReportQueryBo;
import cn.com.dhcc.creditquery.ent.queryflowmanager.bo.CeqQueryReportFlowBo;

/**
 * 个人信用报告查询流程管理服务
 *
 * @author liulekang
 */
public interface CeqFlowManageService {

    /**
     * 是否为接口查询----否
     */
    static final String ISINTERFACE_NO = "1";
    /**
     * 是否为预校验----是
     */
    static final String ISPREVALIDATE_YES = "0";
    /**
     * 是否为预校验---- 否
     */
    static final String ISPREVALIDATE_NO = "1";
    //TODO  微服务与分布式兼容
    /**
     * 授权步骤URL
     */
    static final String ARCHIVE_URL = "inquire/searchlist";
    /**
     * 审批页面URL
     */
    static final String CHECKINFO_URL = "inquire/checkInfo";
    /**
     * 查询流程总控页面
     */
    static final String DISPATCHER_URL = "inquire/dispatcher";

    /**
     * 授权步骤URL
     */
    static final String ARCHIVE_URL_MICRO = "inquire-searchlist";
    /**
     * 审批页面URL
     */
    static final String CHECKINFO_URL_MICRO = "inquire-checkInfo";

    /**
     * 审批方式 - 同步审批
     */
    static final String SYNC_RECHECK = "0";
    /**
     * 审批方式 - 异步审批
     */
    static final String ASYN_RECHECK = "1";

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
     * 接口查询权限编码
     */
    static final String WEBSERVICE_QUERY_ROLE = "Q_W_Q_R";
    /**
     * 同步异步标识:同步
     */
    static final String SYNCFLAGZERO = "0";
    /**
     * 同步异步标识：异步
     */
    static final String SYNCFLAGONE = "1";

    /**
     * 查询类型：企业
     */
    static String QUERY_TYPE_ENT = "E";

    /**
     * 请求访问来源-web
     */
    static final String ACCESS_SOURCE_WEB = "1";

    /**
     * 请求访问来源-interface
     */
    static final String ACCESS_SOURCE_INTERFACE = "2";

    static final String REPORT_FORMAT_XML = "XML";
    static final String REPORT_FORMAT_JSON = "JSON";
    static final String REPORT_FORMAT_HTML = "HTML";
    static final String REPORT_FORMAT_PDF = "PDF";

    static final String CODE_USER_IS_LOCKED_BUSINESS = "000022";
    static final String MSG_USER_IS_LOCKED_BUSINESS = "用户因为企业业务被锁定，不可查询";

    /**
     * 信用报告查询流程控制接口 1.调用查询管控服务 2.调用征信用户服务获取征信用户 3.根据查询规则进行路由：调用中心查询服务进行报告查询，进行本地报告查询
     * 4.存储查询记录 5.返回信息
     *
     * @param ceqReportQueryBo 查询信息 {@link} CeqReportQueryBo
     * @return {@link} CeqPageFlowControlInfo
     */
    CeqPageFlowControlInfo webQueryFlowManager(CeqReportQueryBo ceqReportQueryBo);

    /**
     * 接口查询流程管理
     *
     * @param ceqReportQueryBo 查询信息 {@link} CeqReportQueryBo
     * @return {@link} ReportBo
     * @author Jerry.chen
     * @date 2019年2月19日
     */
    CeqQueryReportFlowBo interfaceQueryFlowManager(CeqReportQueryBo ceqReportQueryBo);

    /**
     * 信用报告查询接口 根据返回报文类型参数，返回对应格式的信用报告
     *
     * @param ceqReportQueryBo 查询信息 {@link} CeqReportQueryBo
     * @return String 支持HTML/PDF/XML/JSON
     * @author Jerry.chen
     * @date 2019年2月19日
     */
    CeqQueryReportFlowBo queryReportStr(CeqReportQueryBo ceqReportQueryBo);

    /**
     * 新增信用报告查询记录
     *
     * @param ceqQueryRecordBo
     * @return {@link} CeqQueryRecordBo
     */
    CeqQueryRecordBo create(CeqQueryRecordBo ceqQueryRecordBo);

    /**
     * 修改信用报告查询记录
     *
     * @param ceqQueryRecordBo {@link} CeqQueryRecordBo
     * @return {@link} CeqQueryRecordBo
     */
    CeqQueryRecordBo update(CeqQueryRecordBo ceqQueryRecordBo);

    /**
     * 根据ID获取查询记录
     *
     * @param id
     * @return {@link} CeqQueryRecordBo
     */
    CeqQueryRecordBo findCeqQueryRecordBoById(String id);

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
     * 分页查询
     *
     * @param searchParams
     * @param pageNumber
     * @param pageSize
     * @param direction
     * @param orderBy
     * @return {@link} Page<CeqQueryRecordBo>
     */
    Page<CeqQueryRecordBo> getPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String direction,
                                   String orderBy);

    /**
     * 根据中征码及查询原因获取本地可用信用报告
     *
     * @param signCode
     * @param qryReason
     * @return 信用报告
     */
    CeqQueryReportFlowBo getLocalReport(String signCode,String qryReason, String topOrgCode,String localDay);

    /**
     * 根据中征码及查询原因判断本地是否存在可用信用报告
     *
     * @param signCode
     * @param qryReason
     * @return
     */
    boolean isHaveLocalReport(String signCode,String qryReason, String topOrgCode,String localDay);

    /**
     * 根据查询记录ID获取信用报告
     *
     * @param id
     * @return
     */
    String getReportById(String id, String reportType);

    /**
     * 批量查询报告
     *
     * @param batchFilePath 批量文件所在路径
     * @return
     */
    List<String> batchQueryReport(String batchFilePath);

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

    String getReportById(String recordId, String userName, String orgCode, String reportType);
}
