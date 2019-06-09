/*
package cn.com.dhcc.creditquery.person.queryweb.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.auth.client.service.OAuthTokenService;
import cn.com.dhcc.auth.common.consts.TokenValidateResult;
import cn.com.dhcc.auth.common.util.Result;
import cn.com.dhcc.credit.platform.util.Collections3;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.credit.platform.util.ResourceManager;
import cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.task.GenerationBatchFilePretreatmentTask;
import cn.com.dhcc.creditquery.person.queryweb.util.BatchQueryThreadPoolExecutorUtil;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.util.ValidateUtil;
import cn.com.dhcc.creditquery.person.queryweb.webservice.consts.AccessTokenResult;
import cn.com.dhcc.creditquery.person.queryweb.webservice.consts.GetBatchResult;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.AccessTokenResultVo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GenerationBatchQueryInfo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GenerationBatchQueryVo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GenerationBatchResult;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GetBatchResultVoV2;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryVo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryWithArchiveVo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleResultVo;
import cn.com.dhcc.query.creditpersonquerydao.entity.alert.CpqAlert;
import cn.com.dhcc.query.creditpersonquerydao.entity.archive.CpqArchive;
import cn.com.dhcc.query.creditpersonquerydao.entity.archivefile.CpqArchivefile;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.http.InquireService;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.info.ResponseInfo;
import cn.com.dhcc.query.creditpersonqueryservice.accesssystem.service.CpqAccessSystemService;
import cn.com.dhcc.query.creditpersonqueryservice.alter.service.CpqAlterService;
import cn.com.dhcc.query.creditpersonqueryservice.archive.service.CpqArchiveService;
import cn.com.dhcc.query.creditpersonqueryservice.archivefile.service.CpqArchivefileService;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.policy.vo.QueryReq;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.interfaces.CreditReportQueryWebService;
import cn.com.dhcc.query.creditquerycommon.interfaces.util.XmlUtil;
import cn.com.dhcc.query.creditquerycommon.util.sftp.util.Sftp;
import cn.com.dhcc.query.creditquerycommon.util.sftp.util.SftpUtil;

*/
/**
 * @author lekang.liu
 * @date 2018年3月26日
 *
 *//*

@WebService(name = "creditreportquery", endpointInterface = ("cn.com.dhcc.query.creditquerycommon.interfaces.CreditReportQueryWebService"), serviceName = "CreditReportService", portName = "CreditReportServicePort", targetNamespace = "http://webservice.creditquery.dhcc.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class CreditReportQueryWebServiceImpl implements CreditReportQueryWebService {
	private static Logger log = LoggerFactory.getLogger(CreditReportQueryWebServiceImpl.class);
	*/
/**
	 * 查询本地，并返回本地信用报告
	 *//*

	private static int QUERY_LOCAL_REPORT = 1;
	private static String QUERY_LOCAL_REPORT_PARAM = "1";
	*/
/**
	 * 查询本地，并返回压测固定信用报告
	 *//*

	private static int QUERY_LOCAL_TEST_REPORT = 0;
	private static String QUERY_LOCAL_TETS_REPORT_PARAM = "0";
	private static String QUERY_PBOC_REPORT_PARAM = "2";
	
	private static String LINEBREAK = "<br/>";
	private static String DEFAULT_QUERYFORMAT = "30";
	
	private static CpqAccessSystemService accessSystemService = null;
	private static InquireService inquireService = null;
	private static CpqAlterService alterService = null;
	
	private OAuthTokenService tokenService = new OAuthTokenService();
	*/
/**
	 * 个人信用报告单笔同步查询接口
	 * http://localhost:9009/creditpersonqueryweb/creditreportservice/creditsinglequery?wsdl
	 * <h1>传入报文样例为：</h1>
	 * &lt;SingleQuery&gt;&lt;ClientName&gt;lekang.liu&lt;/ClientName&gt;&lt;ClientType&gt;X&lt;/ClientType&gt;&lt;ClientNo&gt;123&lt;/ClientNo&gt;&lt;Qryreason&gt;01&lt;/Qryreason&gt;&lt;QueryFormat&gt;0&lt;/QueryFormat&gt;&lt;QueryUser&gt;admin&lt;/QueryUser&gt;&lt;QueryPattern&gt;12&lt;/QueryPattern&gt;&lt;ArchiveNo&gt;&lt;/ArchiveNo&gt;&lt;SystemLabel&gt;01&lt;/SystemLabel&gt;&lt;AssociateBusinessData&gt;&lt;/AssociateBusinessData&gt;&lt;ReportType&gt;1&lt;/ReportType&gt;&lt;TimeBound&gt;0&lt;/TimeBound&gt;&lt;/SingleQuery&gt;
	 * <h1>传入报文中以下节点不可为空：</h1>
	 * ClientName、ClientType、ClientNo、Qryreason、QueryUser、SystemLabel、ReportType
	 * 
	 * @param parXml
	 * @return
	 *//*

	@Override
	public String singleQuery(String parXml) {
		log.info("webservice singleQuery ---", parXml);
		
		long startTime = System.currentTimeMillis();
		
		SingleResultVo singleResultVo = new SingleResultVo();
		try {
		    
		    initBean();
		    
			long validationStartTime = System.currentTimeMillis();
			
			SingleQueryVo queryVo = null;
			ResponseInfo inquireCredit = null;
			long parse4XmlValidateStartTime = System.currentTimeMillis();
			queryVo = XmlUtil.parse4Xml(SingleQueryVo.class, parXml);
			if (StringUtils.isBlank(queryVo.getQueryFormat())) {
				queryVo.setQueryFormat(DEFAULT_QUERYFORMAT);
			}
			long parse4XmlValidatEndTime = System.currentTimeMillis();
            log.info("Query time consuming  parse4Xml consumeTime : " + (parse4XmlValidatEndTime - parse4XmlValidateStartTime));
            
            //查询用户预警阻断校验
            CpqAlert alert = alterService.findByUserStatus(queryVo.getQueryUser());
            if(alert != null){
        		singleResultVo.setCode(Conntanst.CODE_USER_IS_LOCKED_BUSINESS);
        		singleResultVo.setMessage(Conntanst.MSG_USER_IS_LOCKED_BUSINESS);
        		return WebserviceCommon.toResultXml(singleResultVo);
            }
            
			//客户系统校验
			long accessSystemValidateStartTime = System.currentTimeMillis();
			
			boolean accessSystem = accessSystemService.accessSystemNoIsExist(queryVo.getSystemLabel());
			if (!accessSystem) {
				singleResultVo.setCode(Conntanst.CODE_NOSYS);
				singleResultVo.setMessage(Conntanst.MSG_NOSYS);
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			long accessSystemValidateEndTime = System.currentTimeMillis();
			log.info("Query time consuming  accessSystemValidate consumeTime : " + (accessSystemValidateEndTime - accessSystemValidateStartTime));
			//数据校验
			long dataValidateStartTime = System.currentTimeMillis();
			ResultBeans result = ValidateUtil.validate(queryVo);
			if (null != result) {
				singleResultVo.setCode(Conntanst.CODE_VLIDATA);
				String resultMsg = result.getMsg().replace(LINEBREAK, "");
				singleResultVo.setMessage(Conntanst.MSG_VLIDATA + ": " + resultMsg);
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			
			long dataValidateEndTime = System.currentTimeMillis();
			log.info("Query time consuming  dataValidate consumeTime : " + (dataValidateEndTime - dataValidateStartTime));
			
			QueryReq queryReq = WebserviceCommon.createQueryReqBySingleQuery(queryVo);
			// 进行查询员有效性校验
			long userValidateStartTime = System.currentTimeMillis();
			boolean validateQueryUser = WebserviceCommon.validateQueryUser(singleResultVo, queryVo.getQueryUser(),queryReq);
			if (!validateQueryUser) {
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			long userValidateEndTime = System.currentTimeMillis();
			log.info("Query time consuming  userValidate consumeTime : " + (userValidateEndTime - userValidateStartTime));
			
			
			// 获取查询用户所属机构
			long getOrgCodeValidateStartTime = System.currentTimeMillis();
			String operOrg = WebserviceCommon.getQueryUserOrg(queryVo.getQueryUser());
			queryReq.setOperOrg(operOrg);
			long getOrgCodeValidateEndTime = System.currentTimeMillis();
			log.info("Query time consuming  getOrgCodeValidate consumeTime : " + (getOrgCodeValidateEndTime - getOrgCodeValidateStartTime));
			
			// 进行查询时间校验
			long workTimeValidateStartTime = System.currentTimeMillis();
			SingleResultVo validateCanQuery = WebserviceCommon.validateCanQuery(queryReq);
			if (null != validateCanQuery) {
				return WebserviceCommon.toResultXml(validateCanQuery);
			}
			long workTimeValidateEndTime = System.currentTimeMillis();
			log.info("Query time consuming   workTimeValidate consumeTime : " + (workTimeValidateEndTime - workTimeValidateStartTime));
			
			long validationEndTime = System.currentTimeMillis();
			log.info("Query time consuming  validation consumeTime : " + (validationEndTime - validationStartTime));
			
			int queryPattern = Integer.parseInt(queryVo.getQueryPattern());
			inquireCredit = queryRouter(singleResultVo, inquireService, queryReq, queryPattern);
			if(StringUtils.isBlank(inquireCredit.getQueryReq().getHtml())){
				singleResultVo.setCode(Conntanst.CODE_NOT_REPORT);
				singleResultVo.setMessage(Conntanst.MSG_NOT_REPORT);
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			if(queryPattern != QUERY_LOCAL_REPORT){//不是本地时
				if(inquireCredit.getQueryReq().getStatus().equals("2")){//查无此人
					singleResultVo.setCode(Conntanst.CODE_NOT_TRACE_REPORT);
					singleResultVo.setMessage(Conntanst.MSG_NOT_TRACE_REPORT);
					return WebserviceCommon.toResultXml(singleResultVo);
				}
			}
			WebserviceCommon.changeReport(singleResultVo, queryVo.getReportType(), inquireCredit.getQueryReq());
			singleResultVo.setCode(Conntanst.CODE_SUSSCE);
			singleResultVo.setMessage(Conntanst.MSG_SUSSCE);
		} catch (Exception e) {
			log.error("webservice singleQuery ---", e);
			singleResultVo.setCode(Conntanst.CODE_EXC);
			singleResultVo.setMessage(Conntanst.MSG_EXC);
		}
		
		long endTime = System.currentTimeMillis();
		log.info("Query time consuming  Query consumeTime : " + (endTime - startTime));
		
		return WebserviceCommon.toResultXml(singleResultVo);
	}
	
	*/
/**
	 * 根据参数的不同，进行查询路由
	 * 	queryPattern == 1  --> 查询本地信用报告，返回对应报告
	 * 	queryPattern == 0  --> 查询本地信用报告，返回预先设置的固定报告--测试用
	 *  queryPattern == 其他  --> 查询征信中心信用报告
	 * @param singleResultVo
	 * @param inquireService
	 * @param queryReq
	 * @param queryPattern
	 * @return
	 *//*

	private ResponseInfo queryRouter(SingleResultVo singleResultVo, InquireService inquireService, QueryReq queryReq,
			int queryPattern) {
		ResponseInfo inquireCredit;
		if (queryPattern == QUERY_LOCAL_REPORT) {//查询本地
			inquireCredit = inquireService.inquireLocalCreditReport(queryReq);
			singleResultVo.setReportSource(QUERY_LOCAL_REPORT_PARAM);
		} else if(queryPattern == QUERY_LOCAL_TEST_REPORT){//返回一个固定报告，仅供压测
		    long inquireTestCreditReportStartTime = System.currentTimeMillis();
			singleResultVo.setReportSource(QUERY_LOCAL_TETS_REPORT_PARAM);
			inquireCredit = inquireService.inquireTestCreditReport(queryReq);
			long inquireTestCreditReportEndTime = System.currentTimeMillis();
		    log.info("Query time consuming  inquireTestCreditReport consumeTime : " + (inquireTestCreditReportEndTime - inquireTestCreditReportStartTime));
		}else */
/* 查询人行 *//*
 {
			long inquireCreditReportStartTime = System.currentTimeMillis();
			inquireCredit = inquireService.inquireCreditReport(queryReq);
			singleResultVo.setReportSource(QUERY_PBOC_REPORT_PARAM);
			long inquireCreditReportEndTime = System.currentTimeMillis();
			log.info("Query time consuming  inquireCreditReport consumeTime : " + (inquireCreditReportEndTime - inquireCreditReportStartTime));
		}
		return inquireCredit;
	}

	*/
/**
	 * 个人信用报告单笔同步查询含档案接口
	 *  http://localhost:9009/creditpersonqueryweb/creditreportservice/creditsinglequery?wsdl
	 *  <h1>传入报文样例为：</h1>
	 *  &lt;SingleQueryWithArchive&gt;&lt;ClientName&gt;lekang.liu&lt;/ClientName&gt;&lt;ClientType&gt;X&lt;/ClientType&gt;&lt;ClientNo&gt;123&lt;/ClientNo&gt;&lt;Qryreason&gt;01&lt;/Qryreason&gt;&lt;QueryFormat&gt;30&lt;/QueryFormat&gt;&lt;QueryUser&gt;admin&lt;/QueryUser&gt;&lt;QueryPattern&gt;12&lt;/QueryPattern&gt;&lt;SystemLabel&gt;01&lt;/SystemLabel&gt;&lt;AssociateBusinessData&gt;&lt;/AssociateBusinessData&gt;&lt;ReportType&gt;2&lt;/ReportType&gt;&lt;TimeBound&gt;0&lt;/TimeBound&gt;&lt;ArchiveType&gt;1&lt;/ArchiveType&gt;&lt;ArchiveFileName&gt;123.txt&lt;/ArchiveFileName&gt;&lt;ArchiveInfo&gt;archiveInfo&lt;/ArchiveInfo&gt;&lt;ArchiveExpireDate&gt;2018-06-18&lt;/ArchiveExpireDate&gt;&lt;/SingleQueryWithArchive&gt;
	 *	<h1>传入报文中以下节点不可为空：</h1>
	 * 	ClientName、ClientType、ClientNo、Qryreason、QueryUser、SystemLabel、ReportType
	 * 	@param parXml
	 *  @return
	 *//*

	@Override
	public String singleQueryWithArchive(String parXml) {
		log.info("webservice singleQueryWithArchive ---", parXml);
		SingleResultVo singleResultVo = new SingleResultVo();
		try {
			SingleQueryWithArchiveVo queryVo = null;
			ResponseInfo inquireCredit = null;
			queryVo = XmlUtil.parse4Xml(SingleQueryWithArchiveVo.class, parXml);
			if (StringUtils.isBlank(queryVo.getQueryFormat())) {
				queryVo.setQueryFormat("30");
			}
			boolean accessSystem = accessSystemService.accessSystemNoIsExist(queryVo.getSystemLabel());
			if (accessSystem) {
				singleResultVo.setCode(Conntanst.CODE_NOSYS);
				singleResultVo.setMessage(Conntanst.MSG_NOSYS);
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			ResultBeans result = ValidateUtil.validate(queryVo);
			if (null != result) {
				singleResultVo.setCode(Conntanst.CODE_VLIDATA);
				String resultMsg = result.getMsg().replace("<br/>", "");
				singleResultVo.setMessage(Conntanst.MSG_VLIDATA + ": " + resultMsg);
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			QueryReq queryReq = WebserviceCommon.createQueryReqBySingleQueryWithArchive(queryVo);
			// 进行查询员有效性校验
			boolean validateQueryUser = WebserviceCommon.validateQueryUser(singleResultVo, queryVo.getQueryUser(),queryReq);
			if (!validateQueryUser) {
				return WebserviceCommon.toResultXml(singleResultVo);
			}
			// 获取查询用户所属机构
			String operOrg = WebserviceCommon.getQueryUserOrg(queryVo.getQueryUser());
			queryReq.setOperOrg(operOrg);

			// 进行查询量与查询时间校验
			SingleResultVo validateCanQuery = WebserviceCommon.validateCanQuery(queryReq);
			if (null != validateCanQuery) {
				return WebserviceCommon.toResultXml(validateCanQuery);
			}

			// 是否创建档案
			String archiveType = queryVo.getArchiveType();
			if (StringUtils.isNotBlank(archiveType)) {
				CpqArchive archive = null;
				switch (archiveType) {
				case "0":
					// 档案编号
					queryReq.setAutharchiveId(queryVo.getArchiveInfo());
					break;
				case "1":
					// 电子档案
					archive = createArchive(queryVo, operOrg);
					createArchiveFile(queryVo, archive, null, false);
					queryReq.setAutharchiveId(archive.getId());
					break;
				case "2":
					// 档案文件路径（本地）
					archive = createArchive(queryVo, operOrg);
					// 从本地磁盘读取文件
					byte[] archiveFile = readFile(queryVo.getArchiveInfo(), queryVo.getArchiveFileName());
					if (null == archiveFile) {
						singleResultVo.setCode(Conntanst.CODE_FILE_NOT_FOND);
						singleResultVo.setMessage(Conntanst.MSG_FILE_NOT_FOND);
						return WebserviceCommon.toResultXml(singleResultVo);
					}
					// 保存文件
					createArchiveFile(queryVo, archive, archiveFile, true);
					queryReq.setAutharchiveId(archive.getId());
					break;
				case "3":
					// 档案文件路径（FTP）
					archive = createArchive(queryVo, operOrg);
					String archiveStorePath = ConfigUtil.getArchiveStorePath() + File.separator + archive.getId();
					// 通过SFTP的方式获取档案文件至档案保存路径
					String archiveFileName = queryVo.getArchiveFileName();
					String archiveInfo = queryVo.getArchiveInfo();
					SftpGetFile(archiveInfo, archiveFileName, archiveStorePath);
					// 保存档案文件记录
					saveArchiveFile(archive, queryVo, archiveStorePath);
					queryReq.setAutharchiveId(archive.getId());
					break;
				}
			}
			// 判断是否查询本地
			if (Integer.parseInt(queryVo.getQueryPattern()) == 0) {
				inquireCredit = inquireService.inquireLocalCreditReport(queryReq);
				singleResultVo.setReportSource("0");
			} else {
				inquireCredit = inquireService.inquireCreditReport(queryReq);
				singleResultVo.setReportSource("1");
			}
			WebserviceCommon.changeReport(singleResultVo, queryVo.getReportType(), inquireCredit.getQueryReq());
			singleResultVo.setCode(Conntanst.CODE_SUSSCE);
			singleResultVo.setMessage(Conntanst.MSG_SUSSCE);

		} catch (Exception e) {
			log.error("webservice singleQueryWithArchive ---", e);
			singleResultVo.setCode(Conntanst.CODE_EXC);
			singleResultVo.setMessage(Conntanst.MSG_EXC);
		}

		return WebserviceCommon.toResultXml(singleResultVo);
	}

	*/
/**
	 * 创建档案记录
	 * 
	 * @return void
	 * @throws IOException
	 *//*

	private CpqArchive createArchive(SingleQueryWithArchiveVo queryVo, String operOrg) throws Exception {
		// 电子文件 创建档案
		CpqArchive create = null;
		try {
			CpqArchive archive = WebserviceCommon.createArchive(queryVo, operOrg);
			CpqArchiveService archiveService = (CpqArchiveService) WebserviceCommon.getBean("cpqArchiveServiceImpl");
			create = archiveService.create(archive);
		} catch (Exception e) {
			log.error("createArchive", e);
		}
		return create;
	}

	*/
/**
	 * 创建档案文件
	 * 
	 * @return void
	 * @throws IOException
	 *//*

	private void createArchiveFile(SingleQueryWithArchiveVo queryVo, CpqArchive create, byte[] archiveFileByte, boolean isLocalFile) throws Exception {
		// 创建档案文件
		FileOutputStream outputStream = null;
		try {
			CpqArchiveService archiveService = (CpqArchiveService) WebserviceCommon.getBean("cpqArchiveServiceImpl");
			CpqArchivefileService archiveFileService = (CpqArchivefileService) WebserviceCommon.getBean("cpqArchivefileServiceImpl");
			// 保存档案文件
			String archiveStorePath = ConfigUtil.getArchiveStorePath() + File.separator + create.getId();
			File father = new File(archiveStorePath);
			if (!father.exists()) {
				father.mkdirs();
			}
			// 计算档案文件数量
			List<CpqArchivefile> files = archiveFileService.findByArchiveId(create.getId());
			boolean paper = false;
			if (Collections3.isNotEmpty(files)) {
				for (CpqArchivefile archivefile : files) {
					if (archivefile.getPaperfileid() != null) {
						paper = true;
						archiveFileService.delete(archivefile.getId());
					}
				}
			}
			long size = files.size();
			if (paper) {
				size -= 1;
			}
			size += 1;
			// 将档案写入磁盘
			File archiveFile = new File(archiveStorePath + File.separator + queryVo.getArchiveFileName());
			outputStream = new FileOutputStream(archiveFile);

			archiveFileByte = isLocalFile ? archiveFileByte : queryVo.getArchiveInfo().getBytes();

			outputStream.write(archiveFileByte);
			outputStream.close();
			// 保存档案文件记录
			CpqArchivefile archivefile = new CpqArchivefile();
			archivefile.setFilename(queryVo.getArchiveFileName());
			archivefile.setFilepath(archiveStorePath);
			archivefile.setArchiveid(create.getId());
			Date now = new Date();
			archivefile.setCreatetime(now);
			archivefile.setOperator(queryVo.getQueryUser());
			archivefile.setOpertime(now);
			archivefile.setOperorg(create.getOperorg());
			archiveFileService.save(archivefile);

			// 更新档案记录
			create.setArchiveType("1");
			create.setStatus("1");
			create.setQuantity(size);
			archiveService.update(create);

		} catch (Exception e) {
			log.error("createArchiveFile", e);
		} finally {
			if (null != outputStream) {
				outputStream.close();
			}
		}
	}

	*/
/**
	 * 读取本地磁盘的档案文件
	 * 
	 * @return void
	 * @throws IOException
	 *//*

	private byte[] readFile(String filePath, String fileName) throws Exception {
		FileInputStream inputStream = null;
		byte[] filecontent = null;
		try {
			File file = new File(filePath + File.separator + fileName);
			if (!file.exists()) {
				return filecontent;
			}
			Long filelength = file.length();
			filecontent = new byte[filelength.intValue()];
			inputStream = new FileInputStream(file);
			inputStream.read(filecontent);
			inputStream.close();
		} catch (Exception e) {
			log.error("readFile", e);
			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return filecontent;
	}

	*/
/**
	 * 保存档案附件记录
	 * 
	 * @return void
	 *//*

	private void saveArchiveFile(CpqArchive archive, SingleQueryWithArchiveVo queryVo, String archiveStorePath) {
		// 保存档案文件记录
		CpqArchivefileService archiveFileService = (CpqArchivefileService) WebserviceCommon.getBean("cpqArchivefileServiceImpl");
		CpqArchiveService archiveService = (CpqArchiveService) WebserviceCommon.getBean("cpqArchiveServiceImpl");
		List<CpqArchivefile> files = archiveFileService.findByArchiveId(archive.getId());
		boolean paper = false;
		if (Collections3.isNotEmpty(files)) {
			for (CpqArchivefile archivefile : files) {
				if (archivefile.getPaperfileid() != null) {
					paper = true;
					archiveFileService.delete(archivefile.getId());
				}
			}
		}
		long size = files.size();
		if (paper) {
			size -= 1;
		}
		size += 1;

		// 保存档案文件记录
		CpqArchivefile archivefile = new CpqArchivefile();
		archivefile.setFilename(queryVo.getArchiveFileName());
		archivefile.setFilepath(archiveStorePath);
		archivefile.setArchiveid(archive.getId());
		Date now = new Date();
		archivefile.setCreatetime(now);
		archivefile.setOperator(queryVo.getQueryUser());
		archivefile.setOpertime(now);
		archivefile.setOperorg(archive.getOperorg());
		archiveFileService.save(archivefile);

		// 更新档案记录
		archive.setArchiveType("1");
		archive.setStatus("1");
		archive.setQuantity(size);
		archiveService.update(archive);
	}

	*/
/**
	 * 获取远程文件
	 * 
	 * @return void
	 *//*

	private void SftpGetFile(String filePath, String fileName, String storePath) {
		// 获取SFTP连接
		String sshHost = ResourceManager.getInstance().getValue("ftp.sshHost");
		int sshPort = ResourceManager.getInstance().getIntValue("ftp.sshPort");
		String sshUser = ResourceManager.getInstance().getValue("ftp.sshUser");
		String sshPass = ResourceManager.getInstance().getValue("ftp.sshPass");
		Sftp sftp = SftpUtil.getSftp(sshHost, sshPort, sshUser, sshPass);

		// 判断目标跳板机路径分隔符
		String pwd = sftp.pwd();
		String separator = pwd.contains("/") ? "/" : "\\";
		// 远程路径
		filePath = filePath.endsWith(separator) ? filePath : filePath + separator;
		filePath = filePath + fileName;

		// 本地路径
		File father = new File(storePath);
		if (!father.exists()) {
			father.mkdirs();
		}
		storePath = storePath.endsWith(File.separator) ? storePath : storePath + File.separator;
		String archivePath = storePath + fileName;

		// 获取档案文件至档案保存路径
		sftp.get(filePath, archivePath);
	}

	*/
/**
	 * 个人信用报告批量查询请求接口
	 * 
	 * @param
	 * @return
	 *//*

	@Override
	public String sendBatchQuery(String parXml) {

//		log.info("webservice singleQuery ---", parXml);
//		BatchResultVo batchResultVo = new BatchResultVo();
//		try {
//			BatchQueryVo queryVo = null;
//			queryVo = XmlUtil.parse4Xml(BatchQueryVo.class, parXml);
//
//			boolean accessSystem = accessSystemService.accessSystemNoIsExist(queryVo.getSystemLabel());
//			if (accessSystem) {
//				batchResultVo.setCode(Conntanst.CODE_NOSYS);
//				batchResultVo.setMessage(Conntanst.MSG_NOSYS);
//				return WebserviceCommon.toResultXml(batchResultVo);
//			}
//			ResultBeans result = ValidateUtil.validate(queryVo);
//			if (null != result) {
//				batchResultVo.setCode(Conntanst.CODE_VLIDATA);
//				String resultMsg = result.getMsg().replace("<br/>", "");
//				batchResultVo.setMessage(Conntanst.MSG_VLIDATA + ": " + resultMsg);
//				return WebserviceCommon.toResultXml(batchResultVo);
//			}
//			// 进行查询员有效性校验
//			boolean validateQueryUser = WebserviceCommon.validateQueryUser(batchResultVo, queryVo.getQueryUser());
//			// 获取机构
//			String operOrg = WebserviceCommon.getQueryUserOrg(queryVo.getQueryUser());
//			if (!validateQueryUser) {
//				return WebserviceCommon.toResultXml(batchResultVo);
//			}
//
//			// 获取批量文件等信息进行处理
//			String filePath = queryVo.getBatchQueryFilePath();
//			// 判断文件位置是在本地还是FTP 0:本地 1：远程
//			String fileMode = queryVo.getBatchQueryFileMode();
//
//			// 判断文件格式是否符合要求
//			String fileName = queryVo.getBatchQueryFileName();
//			String[] split = fileName.split("\\.");
//
//			boolean iscsv = split[split.length - 1].equals("csv");
//			if (fileName.endsWith(".") || !iscsv) {
//				batchResultVo.setCode(Conntanst.CODE_BATCHFILE_FILEMODE);
//				batchResultVo.setMessage(Conntanst.MSG_BATCHFILE_FILEMODE);
//				return WebserviceCommon.toResultXml(batchResultVo);
//			}
//
//			String batchQueryFilePath = ConfigUtil.getBatchQueryFilePath();
//			// 文件在本地
//			if (fileMode.equals("0")) {
//				// 判断文件是否存在
//				filePath = filePath.endsWith(File.separator) ? filePath : filePath + File.separator;
//
//				File file = new File(filePath + fileName);
//				if (!file.exists()) {
//					batchResultVo.setCode(Conntanst.CODE_BATCHFILE_NOT_FOND);
//					batchResultVo.setMessage(Conntanst.MSG_BATCHFILE_NOT_FOND);
//					return WebserviceCommon.toResultXml(batchResultVo);
//				}
//				// 将文件转移至批量文件指定路径下
//				File file2 = new File(batchQueryFilePath + fileName);
//				FileUtils.copyFile(file, file2);
//
//				// 保存批次信息
//				CpqBatchinfo batchinfo = new CpqBatchinfo();
//				batchinfo.setReqfile(batchQueryFilePath + fileName);
//				batchinfo.setStatus(cn.com.dhcc.query.creditquerycommon.Constant.BATCH_STATUS_PENDING);
//				batchinfo.setOperator(queryVo.getQueryUser());
//				batchinfo.setCstmsysid(queryVo.getSystemLabel());
//				batchinfo.setResulttype(queryVo.getReportType());
//				batchinfo.setQtimelimit(Integer.parseInt(queryVo.getTimeBound()));
//				batchinfo.setOperorg(operOrg);
//				batchinfo.setQuerytime(new Date());
//				batchinfo.setUpdatetime(new Date());
//				batchinfo.setVersion(0);
//				CpqBatchinfo create = batchinfoService.create(batchinfo);
//				String id = create.getId();
//				// 返回批次号
//				batchResultVo.setBatchNo(id);
//				batchResultVo.setCode(Conntanst.CODE_SUSSCE);
//				batchResultVo.setMessage(Conntanst.MSG_SUSSCE);
//				return WebserviceCommon.toResultXml(batchResultVo);
//			} else {
//				// 文件在FTP服务器
//				// 获取远程文件
//				SftpGetFile(filePath, fileName, batchQueryFilePath);
//				// 保存批次信息
//				CpqBatchinfo batchinfo = new CpqBatchinfo();
//				batchinfo.setReqfile(batchQueryFilePath + fileName);
//				batchinfo.setStatus(cn.com.dhcc.query.creditquerycommon.Constant.BATCH_STATUS_PENDING);
//				batchinfo.setOperator(queryVo.getQueryUser());
//				batchinfo.setCstmsysid(queryVo.getSystemLabel());
//				batchinfo.setResulttype(queryVo.getReportType());
//				batchinfo.setQtimelimit(Integer.parseInt(queryVo.getTimeBound()));
//				batchinfo.setOperorg(operOrg);
//				batchinfo.setQuerytime(new Date());
//				batchinfo.setUpdatetime(new Date());
//				batchinfo.setVersion(0);
//				CpqBatchinfo create = batchinfoService.create(batchinfo);
//				String id = create.getId();
//
//				batchResultVo.setBatchNo(id);
//				batchResultVo.setCode(Conntanst.CODE_SUSSCE);
//				batchResultVo.setMessage(Conntanst.MSG_SUSSCE);
//				return WebserviceCommon.toResultXml(batchResultVo);
//			}
//
//		} catch (Exception e) {
//			log.error("webservice singleQuery ---", e);
//			batchResultVo.setCode(Conntanst.CODE_EXC);
//			batchResultVo.setMessage(Conntanst.MSG_EXC);
//		}
//		return WebserviceCommon.toResultXml(batchResultVo);
	    return "";

	}

	*/
/**
	 * 个人批量查询结果获取接口
	 * 
	 * @param parXml
	 *            传入报文
	 * @return 结果信息
	 *//*

	@Override
	public String getBatchResult(String parXml) {
//		log.info("webservice getBatchResult ---", parXml);
//		GetBatchResultVo getBatchResultVo = new GetBatchResultVo();
//		CpqBatchinfoService batchinfoService = (CpqBatchinfoService) WebserviceCommon.getBean("cpqBatchinfoServiceImpl");
//		try {
//
//			GetBatchReportVo getBatchReportVo = XmlUtil.parse4Xml(GetBatchReportVo.class, parXml);
//			ResultBeans result = ValidateUtil.validate(getBatchReportVo);
//			if (null != result) {
//				getBatchResultVo.setCode(Conntanst.CODE_VLIDATA);
//				String resultMsg = result.getMsg().replace("<br/>", "");
//				getBatchResultVo.setMessage(Conntanst.MSG_VLIDATA + ": " + resultMsg);
//				return WebserviceCommon.toResultXml(getBatchResultVo);
//			}
//			String batchNo = getBatchReportVo.getBatchNo();
//
//			Map<Integer, CpqBatchinfo> batchInfoMap = batchinfoService.getFinishedBatchInfoByBatchNo(batchNo);
//			Set<Integer> keySet = batchInfoMap.keySet();
//			Iterator<Integer> iterator = keySet.iterator();
//			Integer finished = iterator.next();
//			if (1 == finished) {
//				// 未处理完成
//				getBatchResultVo.setCode(Conntanst.CODE_EXC);
//				getBatchResultVo.setMessage("未处理完成");
//				return WebserviceCommon.toResultXml(getBatchResultVo);
//			}
//			if (-1 == finished) {
//				// 流水号不存在
//				getBatchResultVo.setCode(Conntanst.CODE_EXC);
//				getBatchResultVo.setMessage("流水号不存在");
//				return WebserviceCommon.toResultXml(getBatchResultVo);
//			}
//			CpqBatchinfo batchInfo = batchInfoMap.get(finished);
//
//			if (!Objects.equal(getBatchReportVo.getQueryUser(), batchInfo.getOperator())) {
//				getBatchResultVo.setCode(Conntanst.CODE_BATCHQUERY_USERDIFFER);
//				getBatchResultVo.setMessage(Conntanst.MSG_BATCHQUERY_USERDIFFER);
//				return WebserviceCommon.toResultXml(getBatchResultVo);
//			}
//
//			String resultFilePath = batchInfo.getResfile();
//
//			getBatchResultVo.setCode(Conntanst.CODE_SUSSCE);
//			getBatchResultVo.setMessage(Conntanst.MSG_SUSSCE);
//			getBatchResultVo.setResultFilePath(resultFilePath);
//			return WebserviceCommon.toResultXml(getBatchResultVo);
//
//		} catch (Exception e) {
//			log.error("webservice getBatchResult ---", e);
//			getBatchResultVo.setCode(Conntanst.CODE_EXC);
//			getBatchResultVo.setMessage(Conntanst.MSG_EXC);
//		}

//		return WebserviceCommon.toResultXml(getBatchResultVo);
	    return "";
	}

	@Override
	public String accessToken(String clientId, String clientSecret) {
		log.info("access token begin...clientId:{},clientSecret:{}", clientId, clientSecret);
		AccessTokenResultVo resultVo = new AccessTokenResultVo();
		if (StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret)) {
			resultVo.setCode(AccessTokenResult.INVALID_PARAM.getCode()).setMsg(AccessTokenResult.INVALID_PARAM.getMsg());
			return WebserviceCommon.toResultXml(resultVo);
		}
		Result<String> result = tokenService.accessToken(clientId, clientSecret);
		int tokenAccessCode = result.getCode();
		for (AccessTokenResult accessTokenResult : AccessTokenResult.values()) {
			if (accessTokenResult.getRelativeCode() == tokenAccessCode) {
				resultVo.setCode(accessTokenResult.getCode()).setMsg(accessTokenResult.getMsg()).setToken(result.getBody());
				break;
			}
		}
		log.info("access token end...clientId:{},clientSecret:{}", clientId, clientSecret);
		return WebserviceCommon.toResultXml(resultVo);
	}

	@Override
	public String getBatchResultByVerson1(String id, String token) {
		log.info("getBatchResultV2 begin...id:{},token:{}", id, token);
		GetBatchResultVoV2 resultVo = new GetBatchResultVoV2();
		if (StringUtils.isBlank(id) || StringUtils.isBlank(token)) {
			resultVo.setCode(GetBatchResult.INVALID_PARAM.getCode()).setMessage(GetBatchResult.INVALID_PARAM.getMsg());
			return WebserviceCommon.toResultXml(resultVo);
		}
		// token校验
		Result<?> tokenValidateResult = tokenService.validateToken(token);
		if (tokenValidateResult.getCode() != TokenValidateResult.SUCCESS.getCode()) {
			log.info("clientId:[{}]token validate failed,error msg:[{}],token:[{}]", id, tokenValidateResult.getMsg(), token);
			resultVo.setCode(GetBatchResult.VALIDATE_TOKEN_FAILED.getCode()).setMessage(GetBatchResult.VALIDATE_TOKEN_FAILED.getMsg());
			return WebserviceCommon.toResultXml(resultVo);
		}
		// 检查redis
		RList<String> batchResultList = RedissonUtil.getLocalRedisson().getList(Constant.DONE_BATCH_KEY);
		if (!batchResultList.contains(id)) {
			log.info("clientId:[{}] bachResult not completed", id);
			resultVo.setCode(GetBatchResult.NOT_COMPLETED.getCode()).setMessage(GetBatchResult.NOT_COMPLETED.getMsg());
			return WebserviceCommon.toResultXml(resultVo);
		}
		// 查询DB
		CpqBatchinfoService batchinfoService = (CpqBatchinfoService) WebserviceCommon.getBean("cpqBatchinfoServiceImpl");
		CpqBatchinfo batchInfo = batchinfoService.findById(id);
		if (Constant.GENERATION_BATCH_STATUS_DISPOSE_DONE.equals(batchInfo.getStatus())) {
			log.info("getBatchResultV2 success...id:{}", id);
			// 成功啦
			String resfile = batchInfo.getResfile();
			String[] split;
			if(resfile.contains("\\")){
				split = resfile.split("\\\\");
			}else{
				split = resfile.split("/");
			}
			String path = resfile.replace(split[split.length - 1], "");
			String fileName = split[split.length - 1];
			String filePath = path;
			resultVo.setCode(GetBatchResult.SUCCESS.getCode()).setMessage(GetBatchResult.SUCCESS.getMsg()).setFileName(fileName).setFilePath(filePath);
			return WebserviceCommon.toResultXml(resultVo);
		}
		 
		String errorCode = batchInfo.getErrCode();
		String errorMsg = batchInfo.getErrinfo();
		resultVo.setCode(errorCode).setMessage(errorMsg);
		log.info("getBatchResultV2 end...id:{}", id);
		return WebserviceCommon.toResultXml(resultVo);
	}

	*/
/**
	 * 1.校验请求。 2.进行平台认证。 3.保存批次信息。
	 * 一代批量查询接口
	 *//*

	@Override
	public String sendBatchQueryByVerson1(String parXml) {
		log.info("webservice sendBatchQueryByVerson1 ---", parXml);
		GenerationBatchResult resultVo = new GenerationBatchResult();

		try {
			GenerationBatchQueryVo generationBatchQueryVo = XmlUtil.parse4Xml(GenerationBatchQueryVo.class, parXml);
			// 进行token认证
			String token = generationBatchQueryVo.getToken();
			if (StringUtils.isBlank(token)) {
				resultVo.setCode(Conntanst.CODE_VLIDATA);
				resultVo.setMessage(Conntanst.MSG_VLIDATA + ": Token信息不可为空");
				return WebserviceCommon.toResultXml(resultVo);
			}
			Result<?> tokenValidateResult = tokenService.validateToken(token);
			if (tokenValidateResult.getCode() != TokenValidateResult.SUCCESS.getCode()) {
				log.info("token validate failed,error msg:[{}],token:[{}]", tokenValidateResult.getMsg(), token);
				resultVo.setCode(GetBatchResult.VALIDATE_TOKEN_FAILED.getCode());
				resultVo.setMessage(GetBatchResult.VALIDATE_TOKEN_FAILED.getMsg());
				return WebserviceCommon.toResultXml(resultVo);
			}

			// 进行参数校验
			GenerationBatchQueryInfo generationBatchQueryInfo = generationBatchQueryVo.getGenerationBatchQueryInfo();
			ResultBeans result = ValidateUtil.validate(generationBatchQueryInfo);
			if (null != result) {
				resultVo.setCode(Conntanst.CODE_VLIDATA);
				String resultMsg = result.getMsg().replace("<br/>", "");
				resultVo.setMessage(Conntanst.MSG_VLIDATA + ": " + resultMsg);
				return WebserviceCommon.toResultXml(resultVo);
			}
			//参数可用性校验
			String errInfo = WebserviceCommon.validateBatchParamVersion1(generationBatchQueryInfo);
			if(StringUtils.isNotBlank(errInfo)){
				resultVo.setCode(Conntanst.CODE_VLIDATA);
				resultVo.setMessage(errInfo);
				return WebserviceCommon.toResultXml(resultVo);
			}
			// 验证后缀
			String batchQueryFileName = generationBatchQueryInfo.getBatchQueryFileName();
			if (!StringUtils.endsWith(batchQueryFileName, ".xls") && !StringUtils.endsWith(batchQueryFileName, ".txt")) {
				resultVo.setCode(Conntanst.CODE_BATCHFILE_FILEMODE);
				resultVo.setMessage(Conntanst.MSG_BATCHFILE_FILEMODE);
				return WebserviceCommon.toResultXml(resultVo);
			}
			String batchQueryFilePath = generationBatchQueryInfo.getBatchQueryFilePath();
			batchQueryFilePath = batchQueryFilePath.endsWith(File.separator) ? batchQueryFilePath : batchQueryFilePath + File.separator;
			// 保存批次信息
			CpqBatchinfoService batchinfoService = (CpqBatchinfoService) WebserviceCommon.getBean("cpqBatchinfoServiceImpl");
			CpqBatchinfo batchinfo = new CpqBatchinfo();
			batchinfo.setReqfile(batchQueryFilePath);
			batchinfo.setReqFileName(batchQueryFileName);
			batchinfo.setStatus(cn.com.dhcc.query.creditquerycommon.Constant.GENERATION_BATCH_STATUS_PENDING);
			batchinfo.setOperator(generationBatchQueryInfo.getOperator());
			batchinfo.setCstmsysid(generationBatchQueryInfo.getSystemLabel());
			batchinfo.setResulttype(generationBatchQueryInfo.getReportType());
			batchinfo.setQtimelimit(Integer.parseInt(generationBatchQueryInfo.getTimeBound()));
			batchinfo.setFileMode(generationBatchQueryInfo.getBatchQueryFileMode());
			String operOrg = WebserviceCommon.getQueryUserOrg(generationBatchQueryInfo.getOperator());//获取当前机构
			batchinfo.setOperorg(operOrg);
			batchinfo.setQuerytime(new Date());
			batchinfo.setUpdatetime(new Date());
			batchinfo.setVersion(1);
			CpqBatchinfo create = batchinfoService.create(batchinfo);
			String id = create.getId();
			// 拉起批量任务处理线程
			ExecutorService filePretreatmentExecutorService = BatchQueryThreadPoolExecutorUtil.getFilePretreatmentExecutorService();
			filePretreatmentExecutorService.submit(new GenerationBatchFilePretreatmentTask(create));

			// 返回信息
			resultVo.setBatchNo(id);
			resultVo.setCode(Conntanst.CODE_SUSSCE);
			resultVo.setMessage(Conntanst.MSG_SUSSCE);
			return WebserviceCommon.toResultXml(resultVo);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("webservice sendBatchQueryByVerson1 ---", e);
			resultVo.setCode(Conntanst.CODE_EXC);
			resultVo.setMessage(Conntanst.MSG_EXC);
		}

		return WebserviceCommon.toResultXml(resultVo);
	}
	
	*/
/**
     * 
     * @return void
     *//*

    private static void initBean() {
        if( null == accessSystemService){
            accessSystemService = (CpqAccessSystemService) WebserviceCommon.getBean("cpqAccessSystemServiceImpl");
        }
        if(null == inquireService){
            inquireService = (InquireService) WebserviceCommon.getBean("inquireServiceImpl");
        }
        if(null == alterService){
        	alterService = (CpqAlterService) WebserviceCommon.getBean("cpqAlterServiceImpl");
        }
    }

}
*/
