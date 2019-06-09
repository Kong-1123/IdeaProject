///**
// *  Copyright (c)  @date 2018年6月4日 DHCC, Inc.
// *  All rights reserved.
// *  东华软件股份公司 版权所有 征信监管产品工作平台
// */
//package cn.com.dhcc.creditquery.person.queryweb.webservice;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.ParseException;
//import org.redisson.api.RedissonClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.ContextLoader;
//
//import com.alibaba.fastjson.JSON;
//
//import cn.com.dhcc.credit.platform.util.RedissonUtil;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.BatchResultVo;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GenerationBatchQueryInfo;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryVo;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryWithArchiveVo;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleResultVo;
//import cn.com.dhcc.platformmiddleware.constant.LoginVlidateConstants;
//import cn.com.dhcc.platformmiddleware.login.LoginServer;
//import cn.com.dhcc.platformmiddleware.login.result.LoginValidateResultBean;
//import cn.com.dhcc.platformmiddleware.vo.SystemRole;
//import cn.com.dhcc.platformmiddleware.vo.SystemUser;
//import cn.com.dhcc.query.creditpersonquerydao.entity.alert.CpqAlert;
//import cn.com.dhcc.query.creditpersonquerydao.entity.archive.CpqArchive;
//import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
//import cn.com.dhcc.query.creditpersonquerydao.entity.userarr.CpqUserAttr;
//import cn.com.dhcc.query.creditpersonqueryservice.alter.service.CpqAlterService;
//import cn.com.dhcc.query.creditpersonqueryservice.ccusercorrelation.service.CpqCcUserCorrelationService;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.legalcheck.CpqLegalCheckService;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.time.CpqTimePolicyService;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.ExceptionRuleValidateVo;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.LegalReview;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.QueryReq;
//import cn.com.dhcc.query.creditpersonqueryservice.userattr.service.util.UserAttrUtil;
//import cn.com.dhcc.query.creditpersonqueryservice.util.LoginValidateUtil;
//import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
//import cn.com.dhcc.query.creditquerycommon.Constant;
//import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;
//
///**
// *
// * @author lekang.liu
// * @date 2018年6月4日
// */
//@Component
//public class WebserviceCommon {
//	private static Logger log = LoggerFactory.getLogger(WebserviceCommon.class);
//	private static String WEBSERVICE_QUERY_ROLE = "Q_W_Q_R";
//	private static String DEFAULT_QUERYFORMAT = "30"; //默认queryformat
//	private static String DEFAULT_QUERYTYPE = "0";  //默认querytype
//	private static String BATCHFLAG_SINGLEQUERY = "1"; // 单笔查询
//	private static String ISINTERFACE = "0";
//	private static String ISPREVALIDATE = "1";
//
//
//
//	private static List<String> haveXMLList = null;
//	private static List<String> havePDFList = null;
//	private static List<String> haveHTMLList = null;
//
//	static{
//	    String[] haveXML = { "1", "4", "6", "7" };
//	    String[] haveHTML = { "2", "4", "5", "7" };
//	    String[] havePDF = { "3", "5", "6", "7" };
//	    haveXMLList = Arrays.asList(haveXML);
//	    havePDFList = Arrays.asList(havePDF);
//	    haveHTMLList = Arrays.asList(haveHTML);
//	}
//
//	@Autowired
//	private CpqTimePolicyService timePolicyService;
//
//	@Autowired
//    private CpqLegalCheckService legalCheckService;
//
//	@Autowired
//	private CpqAlterService alterService;
//
//	private RedissonClient redis = RedissonUtil.getLocalRedisson();
//
//	private static WebserviceCommon webserviceCommon;
//
//	@PostConstruct
//    public void initService(){
//	    webserviceCommon = this;
//	    webserviceCommon.timePolicyService = this.timePolicyService;
//	    webserviceCommon.legalCheckService = this.legalCheckService;
//	    webserviceCommon.alterService = this.alterService;
//	    webserviceCommon.redis = this.redis;
//    }
//
//	/**
//	 * 组装创建查询类
//	 *
//	 * @param queryVo {@link SingleQueryVo}
//	 * @return
//	 */
//	public static QueryReq createQueryReqBySingleQuery(SingleQueryVo queryVo) {
//		String queryUser = queryVo.getQueryUser();
//		// 获取征信用户
//		CpqUserAttr userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(queryUser);
//		CpqCcUser ccUser = UserAttrUtil.getCreditUserByCcId(userAttr.getCreditUser());
//		String creditUser = ccUser.getCcuser();
//		// 调用接口进行查询
//		QueryReq queryReq = new QueryReq();
//		queryReq.setOperator(queryUser);
//		queryReq.setCertNo(queryVo.getClientNo());
//		queryReq.setCertType(queryVo.getClientType());
//		queryReq.setClientName(queryVo.getClientName());
//		queryReq.setQryReason(queryVo.getQryreason());
//		// 查询版式字段 设置默认值为银行版
//		queryReq.setQueryFormat(StringUtils.isBlank(queryVo.getQueryFormat()) ? DEFAULT_QUERYFORMAT  : queryVo.getQueryFormat());
//		// 接口未传递查询类型 设置为非数字解读
//		queryReq.setQueryType(DEFAULT_QUERYTYPE);
//		queryReq.setCreditUser(creditUser);
//		queryReq.setQueryMode("1");
//		queryReq.setAssocbsnssData(queryVo.getAssociateBusinessData());
//		queryReq.setResultType(queryVo.getReportType());
//		queryReq.setBatchFlag(BATCHFLAG_SINGLEQUERY);
//		queryReq.setQueryTime(new Date());
//		queryReq.setUpdateTime(new Date());
//		return queryReq;
//	}
//
//	/**
//	 * 组装创建查询类
//	 *
//	 * @param queryVo {@link SingleQueryWithArchiveVo}
//	 * @return
//	 */
//	public static QueryReq createQueryReqBySingleQueryWithArchive(SingleQueryWithArchiveVo queryVo) {
//		String queryUser = queryVo.getQueryUser();
//		CpqCcUserCorrelationService ccUserCorrelationService = (CpqCcUserCorrelationService) getBean("ccUserCorrelationServiceImpl");
//		// 获取征信用户
//		String creditUser = ccUserCorrelationService.findCCUserNameByUserName(queryUser);
//		// 调用接口进行查询
//		QueryReq queryReq = new QueryReq();
//		queryReq.setOperator(queryUser);
//		queryReq.setCertNo(queryVo.getClientNo());
//		queryReq.setCertType(queryVo.getClientType());
//		queryReq.setClientName(queryVo.getClientName());
//		queryReq.setQryReason(queryVo.getQryreason());
//
//		// 查询版式字段 设置默认值为银行版
//		queryReq.setQueryFormat(StringUtils.isBlank(queryVo.getQueryFormat()) ? "30" : queryVo.getQueryFormat());
//		// 接口未传递查询类型 设置为非数字解读
//		queryReq.setQueryType("0");
//		queryReq.setCreditUser(creditUser);
//		queryReq.setQueryMode(queryVo.getQueryPattern());
//		queryReq.setAssocbsnssData(queryVo.getAssociateBusinessData());
//		queryReq.setResultType(queryVo.getReportType());
//		queryReq.setQtimeLimit(queryVo.getTimeBound());
//		return queryReq;
//	}
//
//	/**
//	 * 转换信用报告
//	 *
//	 * @param singleResultVo
//	 * @param reportType
//	 * @param resultNo
//	 */
//	public static void changeReport(SingleResultVo singleResultVo, String reportType, QueryReq queryReq) {
//		// 判断是否需要转换XML与PDF
//		if (haveHTMLList.contains(reportType)) {
//			singleResultVo.setHtmlReport(queryReq.getHtml());
//		}
//
//		if (haveXMLList.contains(reportType)) {
//			singleResultVo.setXmlReport(queryReq.getXml());
//		}
//
//		if (havePDFList.contains(reportType)) {
//			// TODO 转换PDF格式报文,暂时不提供该格式报文
//			String pdf = "";
//			singleResultVo.setPdfReport(pdf);
//		}
//		singleResultVo.setResultNo(queryReq.getId());
//	}
//
//	public static SingleResultVo validateCanQuery(QueryReq queryReq) {
//		// 进行查询量与查询时间校验
//
////		LegalReview legalReview = webserviceCommon.timePolicyService.checkWorkTime(queryReq.getOperator());
////		String code = legalReview.getResultCode();
////		if (!Objects.equals(Constant.CHECK_SUCCESS, code)) {
////			return new SingleResultVo(Conntanst.CODE_NON_WORK_DAY, Conntanst.MSG_NON_WORK_DAY);
////		}
//	    // 进行异常阻断服务验证
//		 ExceptionRuleValidateVo exceptionRuleValidateVo = new ExceptionRuleValidateVo();
//         exceptionRuleValidateVo.setIsInterface(ISINTERFACE);
//         exceptionRuleValidateVo.setIsPreValidate(ISPREVALIDATE);
//         exceptionRuleValidateVo.setQueryUserName(queryReq.getOperator());
//         exceptionRuleValidateVo.setCreditUserName(queryReq.getCreditUser());
//         exceptionRuleValidateVo.setOrgCode(queryReq.getOperOrg());
//         exceptionRuleValidateVo.setClientName(queryReq.getClientName());
//         exceptionRuleValidateVo.setCertType(queryReq.getCertType());
//         exceptionRuleValidateVo.setCertNo(queryReq.getCertNo());
//         exceptionRuleValidateVo.setQueryReson(queryReq.getQryReason());
//
//         LegalReview lr = webserviceCommon.legalCheckService.exceptionRuleValidate(exceptionRuleValidateVo);
//         String resultCode = lr.getResultCode();
//         if (Objects.equals(Constant.CHECK_USER_VIOLATE, resultCode)
//                 ||Objects.equals(Constant.CHECK_ERROR, resultCode)/*违规查询进行阻断*/) {
//             return new SingleResultVo(Conntanst.CODE_VALIDATE, lr.getResultMsg());
//         }
//         if (Objects.equals(Constant.CHECK_COMMON_ERR, resultCode)/*处理异常*/) {
//             return new SingleResultVo(Conntanst.CODE_EXC, Conntanst.MSG_EXC);
//         }
//
//		return null;
//	}
//
//	/**
//	 * 用户可用性校验
//	 *
//	 * @param singleResultVo
//	 * @param queryUser
//	 * @return boolean
//	 */
//	public static boolean validateQueryUser(BatchResultVo batchResultVo, String queryUser) {
//		String json = "";
//		try {
//			json = LoginValidateUtil.validateUser(queryUser);
//			List<SystemRole> list = LoginServer.analysis(json);
//
//			if(null == list){
//				batchResultVo.setCode(Conntanst.CODE_USER_QUERY_ROLE);
//				batchResultVo.setMessage(Conntanst.MSG_USER_QUERY_ROLE);
//				return false;
//			}
//			for (SystemRole systemRole : list) {
//				if (Objects.equals(systemRole.getId(), WEBSERVICE_QUERY_ROLE)) {
//					return true;
//				}
//			}
//			batchResultVo.setCode(Conntanst.CODE_USER_QUERY_ROLE);
//			batchResultVo.setMessage(Conntanst.MSG_USER_QUERY_ROLE);
//			return false;
//		} catch (ParseException e) {
//			log.error("webservice validateQueryUser ---", e);
//			batchResultVo.setCode(Conntanst.CODE_EXC);
//			batchResultVo.setMessage(Conntanst.MSG_EXC);
//		} catch (IOException e) {
//			log.error("webservice validateQueryUser ---", e);
//			batchResultVo.setCode(Conntanst.CODE_EXC);
//			batchResultVo.setMessage(Conntanst.MSG_EXC);
//		} catch (Exception e) {
//			log.error("validateQueryUser method ", e);
//			analysis(json, batchResultVo);
//		}
//		return false;
//
//	}
//
//	/**
//	 * 请求报文参数可用性
//	 * @param generationBatchQueryInfo
//	 * @return
//	 */
//	public static String validateBatchParamVersion1(GenerationBatchQueryInfo generationBatchQueryInfo){
//		String errorInfo = "";
//		//操作用户
//		CpqUserAttr userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(generationBatchQueryInfo.getOperator());
//		if(userAttr == null){
//			errorInfo = "操作用户不存在！";
//			return errorInfo;
//		}
//		CpqAlert alert = webserviceCommon.alterService.findByUserStatus(generationBatchQueryInfo.getOperator());
//		if(alert != null){
//			errorInfo = "因异常阻断，您的用户已被锁定，请在预警信息解锁！";
//			return errorInfo;
//		}
//		return errorInfo;
//	}
//
//
//	/**
//	 * 用户可用性校验
//	 *
//	 * @param singleResultVo
//	 * @param queryUser
//	 * @return boolean
//	 */
//	public static boolean validateQueryUser(SingleResultVo singleResultVo, String queryUser,QueryReq queryReq) {
//		String json = "";
//		try {
//			// 进行查询员有效性校验
//			// 获取征信用户
//			CpqUserAttr userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(queryUser);
//			if(null == userAttr){
//				singleResultVo.setCode(Conntanst.CODE_USER_NOT_USE);
//				singleResultVo.setMessage(Conntanst.MSG_USER_NOT_USE);
//				return false;
//			}
//
//			CpqCcUser ccUser = UserAttrUtil.getCreditUserByCcId(userAttr.getCreditUser());
//			queryReq.setQueryOrg(ccUser.getCcdept());//为入库准备
//			if(null == ccUser || StringUtils.isBlank(ccUser.getCcuser())){
//				singleResultVo.setCode(Conntanst.CODE_USER_QUERY_NOCREDIT);
//				singleResultVo.setMessage(Conntanst.MSG_USER_QUERY_NOCREDIT);
//				return false;
//			}
//
//			json = LoginValidateUtil.validateUser(queryUser);
//			List<SystemRole> list = LoginServer.analysis(json);
//			if(null == list){
//				singleResultVo.setCode(Conntanst.CODE_USER_QUERY_ROLE);
//				singleResultVo.setMessage(Conntanst.MSG_USER_QUERY_ROLE);
//				return false;
//			}
//
//			String rootDeptCode = UserUtils.getRootDeptCode(getQueryUserOrg(queryUser));
//			for (SystemRole systemRole : list) {
//				if (Objects.equals(systemRole.getId(), WEBSERVICE_QUERY_ROLE+rootDeptCode)) {
//					return true;
//				}
//			}
//			singleResultVo.setCode(Conntanst.CODE_USER_QUERY_ROLE);
//			singleResultVo.setMessage(Conntanst.MSG_USER_QUERY_ROLE);
//			return false;
//		} catch (ParseException e) {
//			log.error("webservice validateQueryUser ---", e);
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//		} catch (IOException e) {
//			log.error("webservice validateQueryUser ---", e);
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//		} catch (Exception e) {
//			log.error("validateQueryUser method ", e);
//			analysis(json, singleResultVo);
//		}
//		return false;
//
//	}
//
//	/**
//	 * 解析用户验证信息
//	 *
//	 * @param code
//	 * @return SingleResultVo
//	 */
//	public static SingleResultVo analysis(String code, SingleResultVo singleResultVo) {
//		LoginValidateResultBean bean = null;
//		try {
//			bean = JSON.parseObject(code, LoginValidateResultBean.class);
//		} catch (Exception e) {
//			log.error("analysis error:", e.getMessage());
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//			return singleResultVo;
//		}
//		String beanCode = bean.getCode();
//		switch (beanCode) {
//		case LoginVlidateConstants.NON_EXISTENT:
//			singleResultVo.setCode(Conntanst.CODE_USER_NONEXISTENT);
//			singleResultVo.setMessage(Conntanst.MSG_USER_NONEXISTENT);
//			break;
//		case LoginVlidateConstants.IS_NULL_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_NULL);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_NULL);
//			break;
//		case LoginVlidateConstants.LOCKED_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_LOCKED);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_LOCKED);
//			break;
//		case LoginVlidateConstants.STOP_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_STOP);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_STOP);
//			break;
//		default:
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//		}
//		return singleResultVo;
//	}
//	/**
//	 * 解析用户验证信息
//	 *
//	 * @param code
//	 * @return SingleResultVo
//	 */
//	public static BatchResultVo analysis(String code, BatchResultVo singleResultVo) {
//		LoginValidateResultBean bean = null;
//		try {
//			bean = JSON.parseObject(code, LoginValidateResultBean.class);
//		} catch (Exception e) {
//			log.error("analysis error:", e.getMessage());
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//			return singleResultVo;
//		}
//		String beanCode = bean.getCode();
//		switch (beanCode) {
//		case LoginVlidateConstants.NON_EXISTENT:
//			singleResultVo.setCode(Conntanst.CODE_USER_NONEXISTENT);
//			singleResultVo.setMessage(Conntanst.MSG_USER_NONEXISTENT);
//			break;
//		case LoginVlidateConstants.IS_NULL_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_NULL);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_NULL);
//			break;
//		case LoginVlidateConstants.LOCKED_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_LOCKED);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_LOCKED);
//			break;
//		case LoginVlidateConstants.STOP_CODE:
//			singleResultVo.setCode(Conntanst.CODE_USER_IS_STOP);
//			singleResultVo.setMessage(Conntanst.MSG_USER_IS_STOP);
//			break;
//		default:
//			singleResultVo.setCode(Conntanst.CODE_EXC);
//			singleResultVo.setMessage(Conntanst.MSG_EXC);
//		}
//		return singleResultVo;
//	}
//
//	/**
//	 * 获取bean
//	 * @param beanName
//	 * @return Object
//	 */
//	public static Object getBean(String beanName) {
//		Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
//		return bean;
//	}
//
////	/**
////	 * 将对象转换为XML
////	 * @return String
////	 */
////	public static String toResultXml(SingleResultVo singleResultVo) {
////		String xml = StringEscapeUtils.unescapeXml(XmlUtil.Obj2Xml(singleResultVo));
////		return xml;
////	}
//
//	/**
//	 * 将对象转换为XML
//	 * @return String
//	 */
//	public static String toResultXml(Object batchResultVo) {
//		String xml = StringEscapeUtils.unescapeXml(XmlUtil.Obj2Xml(batchResultVo));
//		return xml;
//	}
//
//	/**
//	 * 返回用户所属机构
//	 *
//	 * @param userName
//	 * @return
//	 */
//	public static String getQueryUserOrg(String userName) {
//		log.info("getQueryUserOrg userName={}", userName);
//		String userDeptcode = "";
//		try {
//			SystemUser user = LoginValidateUtil.findUserByUserName(userName);
//			userDeptcode = user.getOrgId();
//		} catch (ParseException e) {
//			log.error("getQueryUserOrg error:", e);
//		} catch (IOException e) {
//			log.error("getQueryUserOrg error:", e);
//		} catch (Exception e) {
//			log.error("getQueryUserOrg error:", e);
//		}
//		log.info("getQueryUserOrg result={}", userDeptcode);
//		return userDeptcode;
//	}
//
//	/**
//	 * 组装创建查询类
//	 *
//	 * @param queryVo
//	 * @return
//	 */
//	public static CpqArchive createArchive(SingleQueryWithArchiveVo queryVo,String operOrg) {
//		CpqArchive cpqArchive = new CpqArchive();
//		cpqArchive.setClientName(queryVo.getClientName());
//		cpqArchive.setCretType(queryVo.getClientType());
//		cpqArchive.setCretNo(queryVo.getClientNo());
//		Date now = new Date();
//		cpqArchive.setStartDate(now);
//		cpqArchive.setExpireDate(queryVo.getArchiveExpireDate());
//		cpqArchive.setCreator(queryVo.getQueryUser());
//		cpqArchive.setCreatTime(now);
//		cpqArchive.setOwnorg(operOrg);
//		cpqArchive.setOperator(queryVo.getQueryUser());
//		cpqArchive.setOperTime(now);
//		cpqArchive.setOperorg(operOrg);
//		cpqArchive.setQuantity(0L);
//		cpqArchive.setStatus("2");
//		cpqArchive.setQueryNum(0L);
//		return cpqArchive;
//	}
//
//}
