package cn.com.dhcc.creditquery.person.queryweb.controller.archive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.com.dhcc.credit.platform.util.Collections3;
import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeFileService;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeManagerService;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.OperationIdentifier;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.queryapproveflow.service.CpqApproveFlowService;
import cn.com.dhcc.creditquery.person.queryflowmanager.service.CpqFlowManageService;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.CsvExportGetterImpl;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.util.ValidateUtil;
import cn.com.dhcc.creditquery.person.queryweb.vo.ReviseVo;
import cn.com.dhcc.platform.filestorage.info.FileReadRequest;
import cn.com.dhcc.platform.filestorage.info.FileReadResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvConfig;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvUtil;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvWriter;

@Controller
@RequestMapping(value = "/archive")
public class ArchiveController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(ArchiveController.class);
	private static final String PREFIX = "archive/";
	private static final String REVISE_PREFIX = "revise/";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY = "operTime";
	private final static String RESULTINFO_ORDERBY = "updateTime";//查询记录表
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private final static String DATE_FORMAT_ = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat date_format_df = new SimpleDateFormat(DATE_FORMAT_);
	/**
	 * 新增档案初始化状态为2-无资料
	 */
	private final static String DEFAULT_STATUS = "2";
	private final static String VAILD_STATUS = "1";
	private final static String E_RECORD_ARCHIVE_TYPE = "1";
	private final static String PAPER_ARCHIVE_TYPE = "2";
	private final static String ALLARCHIVETYPE = "4";
	private final static long DEFAULT_QUERYNUM = 0L;
	private final static long DEFAULT_QUANTITY = 0L;
	private final static long PAPER_QUANTITY = 1L;

	@Autowired
	private CpqAuthorizeManagerService cpqAuthorizeManagerService;
	@Autowired
	private CpqFlowManageService cpqFlowManageService;
	@Autowired
	private CpqAuthorizeFileService cpqAuthorizeFileService;
	@Autowired
	private CsvExportGetterImpl csvExportGetterImpl;
	@Autowired
	private CpqApproveFlowService cpqApproveFlowService;
	@Autowired
	private FileStorageService fileStorageService;
	
	
	/**
	 * <分页列表页面>
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return PREFIX + "list";
	}

	@RequestMapping("/searchlist")
	public String searchList() {
		return PREFIX + "searchlist";
	}

	/**
	 * <跳转 新增页面>
	 * 
	 * @return
	 */
	@RequestMapping("/createPage")
	public String createPage() {
		return PREFIX + "create";
	}

	@RequestMapping("/revisecreate")
	public String revisecreate() {
		return PREFIX + "revisecreate";
	}

	@RequestMapping("/reviseupload")
	public String reviseupload() {
		return PREFIX + "reviseupload";
	}

	// @RequestMapping("/upLoad")
	// public String upLoad() {
	// return PREFIX + "upload";
	// }

	@RequestMapping("/uploadrepairs")
	public String uploadRepairs() {
		return PREFIX + "uploadrepairs";
	}

	@RequestMapping("/downLoad")
	public String downLoad() {
		return PREFIX + "download";
	}

	/**
	 * <搜索框>
	 * 
	 * @return
	 */
	@RequestMapping("/search")
	public String search() {
		return PREFIX + "search";
	}

	/**
	 * <跳转 修改 页面>
	 * 
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return PREFIX + "update";
	}

	/**
	 * <跳转到详情页面>
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail() {
		return PREFIX + "detail";
	}
	
	
	
	/**
	 * <跳转 档案新增页>
	 * 
	 * @return
	 */
	@RequestMapping("/createArchive")
	public String createArchive(HttpServletRequest request) {
		return PREFIX + "createArchive";
	}

	
	/**
	 * <档案补录分页列表页面>
	 * 
	 * @return
	 */

	@RequestMapping("/archiverevise")
	public String archiveRevise() {
		return REVISE_PREFIX + "archiverevise";
	}
	
	/**
	 * <跳转到档案补录页面>
	 * @return
	 */
	@RequestMapping("/archiverevisenow")
	public String revisepage() {
		return PREFIX + "revisecreate";// 如果不存在跳转到新增页面
	}
	
	
	/**
	 * 分页列表
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPage")
	@ResponseBody
	public String list(Model model, PageBean page, HttpServletRequest request, String EQ_queryNum) {
		log.info("getPage method begin ...");
		Page<CpqArchiveBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			if (StringUtils.isBlank((String) searchParams.get("IN_ownorg"))) {
				List<String> deptCodes = UserConfigUtils.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCodes, ",");
				searchParams.put("IN_ownorg", deptCodeStr);
			}
			if (null != EQ_queryNum) {
				long deadline = Long.parseLong(EQ_queryNum);
				searchParams.put("EQ_queryNum", deadline);
			}
			log.info("searchParams ={}", searchParams);
			queryResults = cpqAuthorizeManagerService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION, ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("archive list method :" + e);
			queryResults = new PageImpl<>(new ArrayList<CpqArchiveBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		log.info("getPage method end ...,return ={}", jsonString);
		return jsonString;
	}

	/**
	 * <档案上传附件方法>
	 * 
	 * @param file
	 *            上传的文件
	 * @param id
	 *            档案的id
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public void uploadfile(@RequestParam("file") CommonsMultipartFile file, String selfId, String fileType, String id, HttpServletRequest request,  String fileNameList, HttpServletResponse response) throws IOException {
		boolean flag = true;
		try {
			Map<String, String> info = UserConfigUtils.getUserInfo(request);
//			boolean paper = false;
			if (null != selfId) {
				List<CpqApproveBo> cpqApproveBoList = cpqApproveFlowService.findCpqApprovesByArchiveId(id);
				boolean falg = false;
				for (CpqApproveBo cpqApproveBo : cpqApproveBoList) {
					if (Objects.equals(Constants.RECEIVE, cpqApproveBo.getStatus())/* 已领取状态 */
					        || Objects.equals(Constants.AUDITPASS, cpqApproveBo.getStatus())/* 审批通过状态 */
					        || Objects.equals(Constants.SELECT_ARCHIVE, cpqApproveBo.getStatus())/* 已查看档案 */) {
						falg = true;
						break;
					}
				}
				if (falg) {
					String json = "{\"code\": 2,\"msg\":\"该档案关联的审批请求已被领取或审批通过，不可修改！\"}";
					response.getWriter().write(json);
					response.getWriter().flush();
					response.getWriter().close();
					return;
				}
				CpqArchivefileBo archiveFile = cpqAuthorizeFileService.findCpqArchivefileByArchiveFileId(selfId);
				if (null != archiveFile) {
					String[] split2 = fileNameList.split(",");
					List<String> asList = Arrays.asList(split2);
					if(!asList.contains(archiveFile.getFilename())){
						String path = archiveFile.getFilepath() + File.separator + archiveFile.getFilename();
						File dest = new File(path);
						if (dest.exists()) {
							dest.delete();
						}
					}
//					paper = true;
					cpqAuthorizeFileService.deleteById(selfId);
				}
			}
			String userId = info.get("username").trim();
			String deptCode = info.get("orgNo").trim();
			CpqArchivefileBo archivefile = new CpqArchivefileBo();
			archivefile.setCommonsMultipartFile(file);
//			archivefile.setFilename(fileName);
//			archivefile.setFilepath(archiveStorePath);
			archivefile.setArchiveid(id);
			archivefile.setFileType(fileType);

			Date now = new Date();
			archivefile.setCreatetime(now);
			archivefile.setOperator(userId);
			archivefile.setOpertime(now);
			archivefile.setOperorg(deptCode);
			archivefile.setAuthorizeFileMode(Constants.AUTHORIZEFILE_MODE_1);
			RLock lock = cpqAuthorizeManagerService.getRlockForSavePaer(userId);
			try {
				if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
					List<CpqArchivefileBo> files = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(id);
					cpqAuthorizeFileService.create(archivefile);
					long size = files.size();
//					/**
//					 * 修改当档案时，档案数量少1
//					 */
					/*if (paper) {
						size -= 1;
					}*/
					size += 1;
					CpqArchiveBo cpqArchiveBo = cpqAuthorizeManagerService.findById(id);
					cpqArchiveBo.setArchiveType(E_RECORD_ARCHIVE_TYPE);
					if (Collections3.isNotEmpty(files)) {
						for (CpqArchivefileBo cpqArchivefileBo : files) {
							if (null != cpqArchivefileBo.getPaperfileid()) {
								cpqArchiveBo.setArchiveType(ALLARCHIVETYPE);
							}
						}
					}
					cpqArchiveBo.setStatus(VAILD_STATUS);
					cpqArchiveBo.setQuantity(size);
					cpqAuthorizeManagerService.update(cpqArchiveBo);
				}
			} catch (InterruptedException e) {
				log.error("updateQueryNumByRedis method error... e = {}", e);
			} finally {
				lock.unlock();
			}

		} catch (Exception e) {
			log.error("archiveId={},upload archiveFile error!,fileName={}, e = {}", id, file.getFileItem().getName(), e);
			flag = false;
		}
		if (flag) {
			String json = "{\"code\": 0}";
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			String json = "{\"code\": 1}";
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	/**
	 * 修改上传的文件
	 * 
	 * @param file
	 * @param archiveId
	 * @param resultInfoId
	 * @param archiveRevise
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/reviseuploadfile")
	@ResponseBody
	public void reviseUploadfile(@RequestParam("file") CommonsMultipartFile file, String archiveId, String resultInfoId, String archiveRevise, String fileType, String selfId,
	        HttpServletRequest request, String fileNameList, HttpServletResponse response) throws IOException {
		uploadfile(file, selfId, fileType, archiveId, request,fileNameList, response);
		// String type = ReviseMath.getReviseType(archiveRevise,
		// Revise.ARCHIVE);
		cpqFlowManageService.updateArchiveIdById(resultInfoId, archiveId);
//		List<CpqArchivefile> files = fileService.findByArchiveId(archiveId);
//		if (Collections3.isEmpty(files)) {
//			service.updateQueryNumByRedis(archiveId);
//		}
	}

	@RequestMapping("/revisesavePaper")
	@ResponseBody
	public String reviseSavePaper(Model model, HttpServletRequest request, ReviseVo vo) {
		ResultBeans rs = null;
		try {
			String result = savePaper(model, request, vo.getCpqArchivefileBo());
			rs = JSONObject.parseObject(result, ResultBeans.class);
			if (rs.getCode().equals(Constants.SUCCESSCODE)) {
				// String type = ReviseMath.getReviseType(vo.getArchiveRevise(),
				// Revise.ARCHIVE);
				cpqFlowManageService.updateArchiveIdById(vo.getResultInfoId(), vo.getArchiveid());
				CpqQueryRecordBo cpqQueryRecordBo = cpqFlowManageService.findCpqQueryRecordBoById(vo.getResultInfoId());
				if(!StringUtils.isBlank(cpqQueryRecordBo.getCheckId())){
					cpqApproveFlowService.updateArchiveIdByCpqApproveId(cpqQueryRecordBo.getCheckId(), vo.getArchiveid());
				}
//				service.updateQueryNumByRedis(vo.getArchiveid());
				rs = new ResultBeans(Constants.SUCCESSCODE, "补录授权信息成功!");
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "补录授权信息失败!");
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, "补录授权信息失败!");
		}
		return rs.toJSONStr();
	}

	@RequestMapping("/findByArchiveId")
	@ResponseBody
	public String findByArchiveId(String archiveId) {
		log.info("findByArchiveId method begin,param is archiveId = {}", archiveId);
		List<CpqArchivefileBo> files = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(archiveId);
		CpqArchivefileBo PaperFile = null;
		for (int i = 0; i < files.size(); i++) {
			if (null == files.get(i).getFileType()) {
				PaperFile = files.get(i);
				files.remove(i);
			}
		}
		List<CpqArchivefileBo> sortFiles = getSortList(files);
		if (null != PaperFile) {
			sortFiles.add(PaperFile);
		}
		String returnStr = JsonUtil.toJSONString(sortFiles, DATE_FORMAT);
		log.info("findByArchiveId method end,return = {}", returnStr);
		return returnStr;
	}

	/*
	 * 将拼装好的CpqArchivefile进行排序
	 */
	public static List<CpqArchivefileBo> getSortList(List<CpqArchivefileBo> list) {
		Collections.sort(list, new Comparator<CpqArchivefileBo>() {
			@Override
			public int compare(CpqArchivefileBo o1, CpqArchivefileBo o2) {
				if (o1.getFileType().compareTo(o2.getFileType()) > 0) {
					return 1;
				}
				if (o1.getFileType().compareTo(o2.getFileType()) == 0) {
					return 0;
				}
				return -1;
			}
		});
		return list;
	}

	@RequestMapping("/downloadFile")
	@ResponseBody
	public String downloadFile(HttpServletResponse response, HttpServletRequest request, String id) {
		CpqArchivefileBo archivefile = cpqAuthorizeFileService.findCpqArchivefileByArchiveFileId(id);
		File file = new File(archivefile.getFilepath() + File.separator + archivefile.getFilename());
		FileUtil.download(file, response);
		return null;
	}
	
	/**
	 * 添加方法
	 * 
	 * @param model
	 * @param request
	 * @param archiveVo
	 * @param resultInfoId
	 * @param isRevise
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String create(Model model, HttpServletRequest request, CpqArchiveBo cpqArchiveBo,String resultInfoId,String isRevise) {
		ResultBeans rs = null;
		try {
			cpqArchiveBo.setOperationIdentifier(OperationIdentifier.CREATE);
			rs = ValidateUtil.validate(cpqArchiveBo);
			if (rs == null) {
				Map<String, String> info = UserConfigUtils.getUserInfo(request);
				String userId = info.get("username").trim();
				String deptCode = info.get("orgNo").trim();
				Date now = new Date();
				cpqArchiveBo.setCreator(userId);
				cpqArchiveBo.setCreatTime(now);
				cpqArchiveBo.setOwnorg(deptCode);
				cpqArchiveBo.setOperator(userId);
				cpqArchiveBo.setOperTime(now);
				cpqArchiveBo.setOperorg(deptCode);
				cpqArchiveBo.setQuantity(DEFAULT_QUANTITY);
				cpqArchiveBo.setStatus(DEFAULT_STATUS);
				//若是存在resultInfoId，则为补录档案，默认关联数为1
				if(StringUtils.isBlank(resultInfoId)){
					cpqArchiveBo.setQueryNum(DEFAULT_QUERYNUM);
				}else{
					cpqArchiveBo.setQueryNum(PAPER_QUANTITY);
				}
				String id = cpqAuthorizeManagerService.create(cpqArchiveBo).getId();
				
				if(StringUtils.isNotBlank(isRevise) && "0".equals(isRevise)){
					cpqAuthorizeManagerService.updateQueryNumByRedis(id);
				}
				
				rs = new ResultBeans(Constants.SUCCESSCODE, id);
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("error:", e);
		}
		return rs.toJSONStr();
	}

	@RequestMapping("/savePaper")
	@ResponseBody
	public String savePaper(Model model, HttpServletRequest request, CpqArchivefileBo cpqArchivefileBo) {
		ResultBeans rs = null;
		try {
			Map<String, String> info = UserConfigUtils.getUserInfo(request);
			String userId = info.get("username").trim();
			String deptCode = info.get("orgNo").trim();
			Date now = new Date();
			cpqArchivefileBo.setCreatetime(now);
			cpqArchivefileBo.setOperator(userId);
			cpqArchivefileBo.setOpertime(now);
			cpqArchivefileBo.setOperorg(deptCode);
			String paperfileid = cpqArchivefileBo.getPaperfileid();
			String filedescribe = cpqArchivefileBo.getFiledescribe();
			if(cpqArchivefileBo.getId() == null
					&& (StringUtils.isBlank(filedescribe)&&StringUtils.isBlank(paperfileid))
					/*  如果ID为null, 并且备注与纸质编号同时为空，则代表纸质档案未进行变动    */){
				
				return  new ResultBeans(Constants.SUCCESSCODE, "操作成功！").toJSONStr();
			}
			if (cpqArchivefileBo.getId() == null) {
				String archiveId = cpqArchivefileBo.getArchiveid();
				List<CpqArchivefileBo> files = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(archiveId);
				if (Collections3.isNotEmpty(files)) {
					for (CpqArchivefileBo file : files) {
						if (file.getFilename() == null) {
							cpqAuthorizeFileService.deleteById(file.getId());
						}
					}
				}
				/**
				 * 档案编号为空时，不进行操作。
				 */
				if(StringUtils.isEmpty(cpqArchivefileBo.getPaperfileid())){
					return  new ResultBeans(Constants.SUCCESSCODE, "操作成功！").toJSONStr();
				}
				cpqArchivefileBo.setAuthorizeFileMode(Constants.AUTHORIZEFILE_MODE_1);
				cpqAuthorizeFileService.create(cpqArchivefileBo);
				CpqArchiveBo archiveBo = cpqAuthorizeManagerService.findById(archiveId);
				archiveBo.setArchiveType(PAPER_ARCHIVE_TYPE);
				if (Collections3.isNotEmpty(files)) {
					for (CpqArchivefileBo cpqArchivefile : files) {
						if (null != cpqArchivefile.getFilepath()) {
							archiveBo.setArchiveType(ALLARCHIVETYPE);
						}
					}
				}
				archiveBo.setStatus(VAILD_STATUS);
				archiveBo.setQuantity(PAPER_QUANTITY + files.size());
				cpqAuthorizeManagerService.update(archiveBo);
				rs = new ResultBeans(Constants.SUCCESSCODE, "操作成功！");
			} else {
				List<CpqApproveBo> cpqApproveBoList = cpqApproveFlowService.findCpqApprovesByArchiveId(cpqArchivefileBo.getArchiveid());
				boolean falg = false;
				for (CpqApproveBo cpqApproveBo : cpqApproveBoList) {
					if (Objects.equals(Constants.RECEIVE, cpqApproveBo.getStatus())/* 已领取状态 */
					        || Objects.equals(Constants.AUDITPASS, cpqApproveBo.getStatus())/* 审批通过状态 */
					        || Objects.equals(Constants.SELECT_ARCHIVE, cpqApproveBo.getStatus())/* 已查看档案 */) {
						falg = true;
						break;
					}
				}
				if (falg) {
					rs = new ResultBeans(Constants.ERRORCODE, "该授权信息关联的审批请求已被领取或审批通过，不可修改！");
				} else {
					/**
					 * 修改档案时，没有档案编号时，删除改档案。
					 */
					if(StringUtils.isBlank(cpqArchivefileBo.getPaperfileid())
//					if(StringUtils.isBlank(archivefile.getPaperfileid())
//							&&StringUtils.isBlank(archivefile.getFiledescribe())
						/* 如果修改后纸质档案编号与备注同时为空，则删除这条记录*/){
						//将档案信息类型改为电子档案(不会出现只有纸质档案，删除后无档案信息的情况；故删除类型固定为电子档案)
						CpqArchiveBo archiveBo = cpqAuthorizeManagerService.findById(cpqArchivefileBo.getArchiveid());
						archiveBo.setArchiveType("1");
						archiveBo.setQuantity(archiveBo.getQuantity() -1L);
						archiveBo.setOperTime(new Date());
						cpqAuthorizeManagerService.update(archiveBo);
						cpqAuthorizeFileService.deleteById(cpqArchivefileBo.getId());
					}else{
						cpqAuthorizeFileService.create(cpqArchivefileBo);
					}
					rs = new ResultBeans(Constants.SUCCESSCODE, "操作成功！");
				}
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, e.getMessage());
			log.error("error:", e);
		}
		return rs.toJSONStr();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public String findById(String id) {
		CpqArchiveBo cpqArchiveBo = cpqAuthorizeManagerService.findById(id);
		return JsonUtil.toJSONString(cpqArchiveBo, DATE_FORMAT_);
	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@LogOperation(value = "删除档案信息", isQuery = true)
	public String deleteArchive(Model model, HttpServletRequest request, String id) {
		ResultBeans rs = null;
		try {
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				String deleteId = ids[i];
				List<CpqApproveBo> cpqApproveBoList = cpqApproveFlowService.findCpqApprovesByArchiveId(deleteId);
				if (!cpqApproveBoList.isEmpty()) {
					rs = new ResultBeans(Constants.ERRORCODE, "已提交审批业务,不可删除该授权信息");
					return rs.toJSONStr();
				}
				CpqArchiveBo cpqArchiveBo = cpqAuthorizeManagerService.findById(deleteId);
//				CpqArchiveVo archiveVo = new CpqArchiveVo();
//				CopyUtil.copy(cpqArchiveBo, archiveVo);
				cpqArchiveBo.setOperationIdentifier(OperationIdentifier.DELETE);
				rs = ValidateUtil.validate(cpqArchiveBo);
				if (rs == null) {
					cpqAuthorizeManagerService.deleteById(deleteId);
					List<CpqArchivefileBo> files = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(deleteId);
					if (Collections3.isNotEmpty(files)) {
						for (CpqArchivefileBo file : files) {
							if (file.getFilename() != null) {
								String path = file.getFilepath() + File.separator + file.getFilename();
								File dest = new File(path);
								if (dest.exists()) {
									dest.delete();
								}
								deleteFather(file.getFilepath());
							}
							cpqAuthorizeFileService.deleteById(file.getId());
						}
					}
					rs = new ResultBeans(Constants.SUCCESSCODE, "删除授权信息成功!");
				}
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, "删除授权信息失败!");
			log.error(e.getMessage());

		}
		return rs.toJSONStr();

	}

	@RequestMapping("/deleteArchiveFile")
	@ResponseBody
	public String deleteArchiveFile(HttpServletRequest request, String id) {
		ResultBeans rs = null;
		try {
			// 判断系统参数是否允许删除档案
			// boolean canDelete = ConfigUtil.isCanArchiveDelete();
			// if (!canDelete) {
			// rs = new ResultBeans(Constants.ERRORCODE, "系统不允许删除档案");
			// return rs.toJSONStr();
			// }

			CpqArchivefileBo file = cpqAuthorizeFileService.findCpqArchivefileByArchiveFileId(id);
			String archiveid = file.getArchiveid();
			List<CpqApproveBo> cpqApproveBoList = cpqApproveFlowService.findCpqApprovesByArchiveId(archiveid);
			if (!cpqApproveBoList.isEmpty()) {
				for (CpqApproveBo cpqApproveBo : cpqApproveBoList) {
					if(!Objects.equals(cpqApproveBo.getStatus(), "4")){
						rs = new ResultBeans(Constants.ERRORCODE, "已提交审批业务,不可删除该档案");
						return rs.toJSONStr();
					}
				}
			}
			String path = file.getFilepath() + File.separator + file.getFilename();
			File dest = new File(path);
			if (dest.exists()) {
				dest.delete();
			}
			String archiveId = file.getArchiveid();
			List<CpqArchivefileBo> files = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(archiveId);
			cpqAuthorizeFileService.deleteById(id);
			if (Collections3.isEmpty(files)) {
				deleteFather(file.getFilepath());
				CpqArchiveBo cpqArchiveBo = cpqAuthorizeManagerService.findById(archiveId);
				cpqArchiveBo.setQuantity(DEFAULT_QUANTITY);
				cpqArchiveBo.setStatus(DEFAULT_STATUS);
				cpqAuthorizeManagerService.update(cpqArchiveBo);
			} else {
				CpqArchiveBo cpqArchiveBo = cpqAuthorizeManagerService.findById(archiveId);
				cpqArchiveBo.setQuantity(cpqArchiveBo.getQuantity() -1L);
				cpqAuthorizeManagerService.update(cpqArchiveBo);
			}
			rs = ResultBeans.sucessResultBean();
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		}
		return rs.toJSONStr();

	}

	/**
	 * 修改方法
	 * 
	 * @param model
	 * @param request
	 * @param archiveVo
	 * @param creatTime
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	@LogOperation(value = "修改档案信息", isQuery = true)
	public String update(Model model, HttpServletRequest request, CpqArchiveBo cpqArchiveBo, String creatTime) {

		ResultBeans rs;
		RLock lock = cpqAuthorizeManagerService.getRlockForUpdate();
		try {
			List<CpqApproveBo> cpqApproveBoList = cpqApproveFlowService.findCpqApprovesByArchiveId(cpqArchiveBo.getId());
			boolean falg = false;
			for (CpqApproveBo cpqApproveBo : cpqApproveBoList) {
				if (Objects.equals(Constants.RECEIVE, cpqApproveBo.getStatus())/* 已领取状态 */
				        || Objects.equals(Constants.AUDITPASS, cpqApproveBo.getStatus())/* 审批通过状态 */
				        || Objects.equals(Constants.SELECT_ARCHIVE, cpqApproveBo.getStatus())/* 已查看档案 */) {
					falg = true;
					break;
				}
			}
			if (falg) {
				return new ResultBeans(Constants.ERRORCODE, "该档案关联的审批请求已被领取或审批通过，不可修改！").toJSONStr();
			}
			Date parse = date_format_df.parse(creatTime);
			cpqArchiveBo.setCreatTime(parse);
			cpqArchiveBo.setOperationIdentifier(OperationIdentifier.UPDATE);
			rs = ValidateUtil.validate(cpqArchiveBo);
			if (rs == null) {
//				CpqArchiveBo cpqArchiveBo = ClassCloneUtil.copyObject(archiveVo, CpqArchiveBo.class);
				Map<String, String> info = UserConfigUtils.getUserInfo(request);
				String userName = info.get("username").trim();
				String deptCode = info.get("orgNo").trim();
				Date now = new Date();
				cpqArchiveBo.setOperator(userName);
				cpqArchiveBo.setOperTime(now);
				cpqArchiveBo.setOperorg(deptCode);
				List<CpqArchivefileBo> files2 = cpqAuthorizeFileService.findCpqArchivefilesByArchiveId(cpqArchiveBo.getId());
				int i =0;
				for (CpqArchivefileBo cpqArchivefileBo : files2) {
					if(null == cpqArchivefileBo.getPaperfileid()){
						i++;
					}
				}
				if(i == 0){
					cpqArchiveBo.setArchiveType(PAPER_ARCHIVE_TYPE);
				}
				if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
					long queryNum = cpqAuthorizeManagerService.getQueryNumByRedis(cpqArchiveBo.getId());
					CpqArchiveBo cpqArchive = cpqAuthorizeManagerService.findById(cpqArchiveBo.getId());
					cpqArchiveBo.setQueryNum(queryNum);
					cpqArchiveBo.setQuantity(cpqArchive.getQuantity());
					cpqAuthorizeManagerService.update(cpqArchiveBo);
				}
				String id = cpqArchiveBo.getId();
				rs = new ResultBeans(Constants.SUCCESSCODE, id);
			}
		} catch (Exception e) {
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
			log.error(e.getMessage());
		} finally {
			lock.unlock();
		}
		return rs.toJSONStr();
	}

	/**
	 *  <导出>
	 * @param ids
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/exportLoad")
	@ResponseBody
	public HttpServletResponse exportLoad(@RequestParam List<String> ids, PageBean page, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 如果他为空，则获取所有符合查询条件的数据并导出
		HttpServletResponse download = null;
		File csvFile = null;
		CsvWriter writer = null;
		try {
			String time = SysDate.getFullDate();
			String userName = UserConfigUtils.getUserInfo(request).get("username").trim();
			String filePath = ConfigUtil.getTempPath();
			String path = filePath + File.separator + "archive";
			String filename = userName + time + ".csv";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			csvFile = new File(file, filename);
			CsvConfig csvConfig = new CsvConfig();
			csvConfig.setCsvExportGetter(csvExportGetterImpl);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"));
			writer = CsvUtil.getWriter(bufferedWriter, csvConfig);
			if (ids == null || ids.size() == 0) {
				// 通过baseController的方法获取页面的查询条件
				Map<String, Object> searchParams = processRequestParams(page, request);
				List<CpqArchiveBo> cpqArchiveBos = cpqAuthorizeManagerService.findAll(searchParams);
				// README 20180529 经要求，要在证件号码、机构字段值前拼接 ' 号。
				for (CpqArchiveBo cpqArchiveBo : cpqArchiveBos) {
					cpqArchiveBo.setCretNo("'" + cpqArchiveBo.getCretNo());
					cpqArchiveBo.setOwnorg("'" + cpqArchiveBo.getOwnorg());
					cpqArchiveBo.setOperorg("'" + cpqArchiveBo.getOperorg());
				}
				writer.write(cpqArchiveBos);
			} else {
				List<CpqArchiveBo> cpqArchiveBos = cpqAuthorizeManagerService.findByIds(ids);
				// README 20180529 经要求，要在证件号码、机构字段值前拼接 ' 号。
				for (CpqArchiveBo cpqArchiveBo : cpqArchiveBos) {
					cpqArchiveBo.setCretNo("'" + cpqArchiveBo.getCretNo());
					cpqArchiveBo.setOwnorg("'" + cpqArchiveBo.getOwnorg());
					cpqArchiveBo.setOperorg("'" + cpqArchiveBo.getOperorg());
				}
				writer.write(cpqArchiveBos);
			}
			download = FileUtil.download(csvFile, response);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			writer.close();
			if (csvFile != null) {
				csvFile.delete();
			}
		}
		return download;
	}

	private void deleteFather(String fatherPath) {
		File file = new File(fatherPath);
		String[] chirld = file.list();
		if (chirld == null || chirld.length == 0) {
			file.delete();
		}
	}

	@RequestMapping("/getArchiveType")
	@ResponseBody
	public Object getFileTypeForArchive() {
		String topOrg = UserUtilsForConfig.getTopOrgCode();
		JSONObject jo = cpqAuthorizeManagerService.getFileTypeForArchive(topOrg);
		return jo;
	}

	@RequestMapping("/getPictureFile")
	@ResponseBody
	public void getPictureFile(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		FileReadRequest fileReadRequest = new FileReadRequest();
		OutputStream os = null;
		try {
			response.reset();
			CpqArchivefileBo archiveFileBo = cpqAuthorizeFileService.findCpqArchivefileByArchiveFileId(id);
			String filePath = archiveFileBo.getFilepath();
			String fileName = archiveFileBo.getFilename();
			String poxtfix = fileName.substring(fileName.lastIndexOf("."));
			poxtfix = poxtfix.substring(1, poxtfix.length());
			if (poxtfix.equals("pdf")) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("image/" + poxtfix); // 设置返回内容格式
			}
			File file = new File(filePath);
			if (file.exists()) { // 如果文件存在
				String systemStorageType = ConfigUtil.getSystemStorageType();
        		if(StringUtils.isNotBlank(systemStorageType)) {
        			fileReadRequest.setStorageType(systemStorageType);
        		}
				fileReadRequest.setUri(filePath);
				fileReadRequest.setDecrypt(true);
				fileReadRequest.setZipFlag(true);
				fileReadRequest.setResultType("0");
				FileReadResponse fileReadResponse = fileStorageService.readFileContent(fileReadRequest);
				byte[] contentBytes = fileReadResponse.getContentBytes();
				os = response.getOutputStream(); // 创建输出流
				os.write(contentBytes);
			}
		} catch (Exception e) {
			log.error("getPictureFile error ",e);
		} finally {
			os.flush();
			os.close();
		}
	}

	/**
	 * 用于当客户端浏览器为IE时，预览档案文件使用。 根 据ID获取档案文件，将档案文件写入应用内目录。 并将路径返回用于预览
	 * 
	 * @param id
	 *            档案文件ID
	 * @param request
	 * @return String
	 */
	@RequestMapping("/getPreviewPathInIE")
	@ResponseBody
	public String getPreviewPathInIE(String id, HttpServletRequest request) {
		ResultBeans rs;
		try {
			// 获取文件路径
			CpqArchivefileBo archiveFileBo = cpqAuthorizeFileService.findCpqArchivefileByArchiveFileId(id);
			String filePath = archiveFileBo.getFilepath();
			String fileName = archiveFileBo.getFilename();
			File file = new File(filePath + File.separator + fileName);
			if (file.exists()) { // 如果文件存在
				// 创建应用跟路径
				String pdfPath = getDefaultPathForPdf();
				// 用于页面获取的应用相对路径 （在应用根路径下每天新建一个文件夹，文件夹名字为YYYY-MM-DD）
				String appPath = SysDate.getDate() + File.separator + fileName;
				// 复制文件
				FileUtils.copyFile(file, new File(pdfPath + appPath));
				rs = new ResultBeans(Constants.SUCCESSCODE, appPath);
			} else {
				rs = new ResultBeans(Constants.ERRORCODE, "该档案文件已不存在~！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getPreviewPathInIE error e = ", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}
	
	@RequestMapping("/previewArchiveFile4NotUploaded")
	@ResponseBody
	public String previewArchiveFile4NotUploaded(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request) throws IOException {
		log.info("previewArchiveFile4NotUploaded fileName = ", file.getFileItem().getName());
		ResultBeans rs;
		try{
			String fileName = file.getFileItem().getName();
			String split[] = fileName.split("\\\\");
			fileName = split[split.length-1];
			// 创建应用根路径
			String pdfPath = getDefaultPathForPdf();
			// 用于页面获取的应用相对路径 （在应用根路径下每天新建一个文件夹，文件夹名字为YYYY-MM-DD）
			String appPath = SysDate.getDate();
			File father = new File(pdfPath + appPath);
			if (!father.exists()) {
				father.mkdirs();
			}
			String path = appPath + File.separator + DigestUtils.md5Hex(fileName) + ".pdf";
			String filePath = pdfPath + path;
			file.transferTo(new File(filePath));
			log.info("previewArchiveFile path = ", path);
			return path;
		}catch(Exception e){
			log.error("previewArchiveFile error e = ", e);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		
		return rs.toJSONStr();
		
	}

	
	
	
	
	
	/**
	 * <档案补录分页列表>
	 * 
	 * @param model
	 * @param page
	 * @param request
	 * @return
	 */

	@RequestMapping("/getList")
	@ResponseBody
	public String getPage(Model model, PageBean page, HttpServletRequest request) {
		Page<CpqQueryRecordBo> queryResults = null;
		try {
			Map<String, Object> searchParams = processRequestParams(page, request);
			String qryReason = "01";
			searchParams.put("NE_qryReason", qryReason);
			searchParams.put("ISNULL_autharchiveId", "1");
			if (StringUtils.isBlank((String) searchParams.get("IN_operOrg"))) {
				List<String> deptCode = UserUtilsForConfig.getJurisdictionDeptCodes(request);
				String deptCodeStr = StringUtils.join(deptCode, ",");
				searchParams.put("IN_operOrg", deptCodeStr);
			}
			queryResults = cpqFlowManageService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION,
					RESULTINFO_ORDERBY);
			log.debug("page:" + page);
		} catch (Exception e) {
			log.error("Query list method :" + e.getMessage());
			queryResults = new PageImpl<>(new ArrayList<CpqQueryRecordBo>());
		}
		page = processQueryResults(model, page, queryResults);
		String jsonString = JsonUtil.toJSONString(page, DATE_FORMAT);
		return jsonString;
	}
	
	/**
	 * 获取上传的默认pdf路径
	 * @param pdfPath
	 * @return
	 */
	private String getDefaultPathForPdf() {
		log.info("getDefaultPathForPdf bein!");
		String pdfPath = this.getClass().getClassLoader().getResource("/").getPath();
		pdfPath = pdfPath.substring(0, pdfPath.indexOf("WEB-INF"));
		log.info("getDefaultPathForPdf  end,the path = {}",pdfPath);
		return pdfPath;
	}
}
