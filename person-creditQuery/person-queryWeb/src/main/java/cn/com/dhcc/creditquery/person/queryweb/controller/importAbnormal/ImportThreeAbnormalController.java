/*
package cn.com.dhcc.creditquery.person.queryweb.controller.importAbnormal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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

import cn.com.dhcc.credit.platform.util.FileUtil;
import cn.com.dhcc.credit.platform.util.JsonUtil;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.creditquery.person.queryweb.util.Constants;
import cn.com.dhcc.creditquery.person.queryweb.util.MD5;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.util.UnicodeReader;
import cn.com.dhcc.query.creditpersonquerydao.entity.importthree.CpqImportThree;
import cn.com.dhcc.query.creditpersonquerydao.entity.resultInfo.CpqResultinfo;
import cn.com.dhcc.query.creditpersonqueryservice.dic.service.CpqDicCacheService;
import cn.com.dhcc.query.creditpersonqueryservice.importThree.service.CpqImportThreeService;
import cn.com.dhcc.query.creditpersonqueryservice.resultsInfo.service.CpqResultInfoService;
import cn.com.dhcc.query.creditpersonqueryservice.util.ExcelUtil;
import cn.com.dhcc.query.creditpersonqueryservice.vo.AbnormalThreeVO;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import io.netty.util.internal.StringUtil;

@Controller
@RequestMapping("/importThreeAbnormal")
public class ImportThreeAbnormalController extends BaseController {

	@Autowired
	private CpqImportThreeService importThreeService;
	@Autowired
	private CpqResultInfoService resultInfoService;

	private static Logger log = LoggerFactory.getLogger(ImportThreeAbnormalController.class);

	private final static String PREFIX = "importAbnormal/";
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static String FILE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";
	private final static String DIRECTION = "desc";
	private final static String ORDERBY1 = "compareFlag";
	private final static String ORDERBY2 = "importTime";
	private final static String QUERYQUANTITY = "3";
	private final static String NOTWORKTIME = "4";
	private final static String SLEEPUSER = "5";
	private final static String QUERYQUANTITY_MODULE = "usercode,queryorgno,querydate,NAME,CERTTYPE,CERTNO,flag,repno";
	private final static String NOTWORKTIME_MODULE = "usercode,queryorgno,querydate,NAME,CERTTYPE,CERTNO,flag,repno";
	private final static String SLEEPUSER_MODULE = "usercode,queryorgno,QUERYDATE,NAME,CERTTYPE,CERTNO,flag,repno";
	private final static String UNION_MODULE = "usercode,queryorgno,querydate,name,certtype,certno,flag,repno";

	private final static String FIT = "0";
	private final static String UNFIT = "1";
	private final static String HANDLE_ERROR = "2";

	private static final String YES = "1";
	private static final String NO = "0";

	@RequestMapping("/listQQ")
	public String listQQ() {
		return PREFIX + "listQQ";
	}

	@RequestMapping("/listNWT")
	public String listNWT() {
		return PREFIX + "listNWT";
	}

	@RequestMapping("/listSU")
	public String listSU() {
		return PREFIX + "listSU";
	}

	@RequestMapping("/illustrationPage")
	public String illustrationPage() {
		return PREFIX + "illustrationThree";
	}

	@RequestMapping("/detailPage")
	public String detailPage() {
		return PREFIX + "detailThree";
	}

	@RequestMapping("/uploadQQPage")
	public String uploadQQPage() {
		return PREFIX + "uploadQQ";
	}

	@RequestMapping("/uploadNWTPage")
	public String uploadNWTPage() {
		return PREFIX + "uploadNWT";
	}

	@RequestMapping("/uploadSUPage")
	public String uploadSUPage() {
		return PREFIX + "uploadSU";
	}

	@RequestMapping("/getPage")
	@ResponseBody
	public String getPage(Model model, PageBean page, HttpServletRequest request,String EQ_type) {
		Map<String, Object> searchParams = processRequestParams(page, request);
		log.info("importThreeAbnormal controller getPage start");
		Page<CpqImportThree> results = null;
		try {
			if (null != EQ_type) {
				long type = Long.parseLong(EQ_type);
				searchParams.put("EQ_type", type);
			}
			results = importThreeService.getPage(searchParams, page.getCurPage(), page.getMaxSize(), DIRECTION,
					ORDERBY1, ORDERBY2);
		} catch (Exception ex) {
			results = new PageImpl<>(new ArrayList<CpqImportThree>());
			log.error("importThreeAbnormal controller getPage error={}", ex);
		}
		page = processQueryResults(model, page, results);
		return JsonUtil.toJSONString(page, TIME_FORMAT);
	}

	@RequestMapping("/findCpqQueryRecordBoById")
	@ResponseBody
	public String findById(String id) {
		CpqImportThree importTwo = null;
		try {
			log.info("importThreeAbnormal controller findCpqQueryRecordBoById start,param is id ={}", id);
			importTwo = importThreeService.findById(id);
		} catch (Exception ex) {
			log.error("importThreeAbnormal controller findCpqQueryRecordBoById error={}", ex);
		}
		return JsonUtil.toJSONString(importTwo, TIME_FORMAT);
	}

	@RequestMapping("/uploadfile")
	@ResponseBody
	public void uploadfile(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request,
			HttpServletResponse response, String type) throws IOException {
		boolean flag = true;
		List<CpqImportThree> results = new ArrayList<CpqImportThree>();
		String userName = UserUtils.getUserName(request);
		try {
			// 获取文件大小
			Long fileSize = file.getFileItem().getSize();
			// 设置文件最大大小
			String maxSize = ConfigUtil.getMaxSize();
			if (StringUtils.isBlank(maxSize)) {
				maxSize = "5";
			}
			// 文件大小超过规定范围 返回对应提示信息
			if (fileSize.compareTo(Long.parseLong(maxSize) * 1024 * 1024) > 0) {
				String json = "{\"code\": 2,\"msg\":\"档案文件最大可为" + maxSize + "MB; 当前文件过大，请重新选择文件。\"}";
				response.getWriter().write(json);
				response.getWriter().flush();
				response.getWriter().close();
				return;
			}
			Date now = new Date();
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			String date = df.format(now);
			// 附件上传到服务器的路径
			String archiveStorePath = ConfigUtil.getArchiveStorePath() + File.separator + date;
			File father = new File(archiveStorePath);
			if (!father.exists()) {
				father.mkdirs();
			}
			String filePath = archiveStorePath + File.separator + file.getFileItem().getName();
			file.transferTo(new File(filePath));

			// 获取MD5值防止重复导入
			String md5Code = MD5.getMD5(new File(filePath));
			List<CpqImportThree> list = importThreeService.findByMd5Code(md5Code);
			if (list != null && list.size() != 0) {
				String json = "{\"code\": 2,\"msg\":\"该文件已被导入过\"}";
				response.getWriter().write(json);
				response.getWriter().flush();
				response.getWriter().close();
				return;
			}
			// 处理文件
			String matchModuleFlag = handleFile(filePath, type, userName, md5Code, results);
			if (matchModuleFlag.equals(UNFIT)) {
				String json = "{\"code\": 2,\"msg\":\"导入文件不符合模板要求\"}";
				response.getWriter().write(json);
				response.getWriter().flush();
				response.getWriter().close();
				return;
			} else if (matchModuleFlag.equals(HANDLE_ERROR)) {
				String json = "{\"code\": 2,\"msg\":\"文件处理失败\"}";
				response.getWriter().write(json);
				response.getWriter().flush();
				response.getWriter().close();
				return;
			} else {
				importThreeService.save(results);
			}

		} catch (Exception e) {
			log.error("importThreeAbnormal controller uploadfile error!,fileName={},error={}",
					file.getFileItem().getName(), e.getMessage());
			flag = false;
		}
		if (flag) {
			String json = "{\"code\": 0}";
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			String json = "{\"code\": 2}";
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	*/
/**
	 * 更新预警说明
	 * 
	 * @param id
	 * @param illustration
	 * @return
	 *//*

	@RequestMapping("/updateIllustration")
	@ResponseBody
	public String updateIllustration(String id, String illustration) {
		ResultBeans rs = null;
		try {
			log.info("importThreeAbnormal controller updateIllustration start");
			importThreeService.updateIllustration(id, illustration);
			rs = ResultBeans.sucessResultBean();
		} catch (Exception ex) {
			log.error("importThreeAbnormal controller updateIllustration error={}", ex);
			rs = new ResultBeans(Constants.ERRORCODE, Constants.ERRORMSG);
		}
		return rs.toJSONStr();
	}

	*/
/**
	 * 处理导入的文件
	 * 
	 * @param filePath
	 * @param type
	 * @param userName
	 * @param md5Code
	 * @param results
	 * @return
	 * @throws IOException
	 *//*

	private String handleFile(String filePath, String type, String userName, String md5Code,
			List<CpqImportThree> results) throws IOException {
		log.info("handleFile start,param is filePath={}", filePath);
		String flag = FIT;
		BufferedReader br = null;
		String module = null;
		try {
			// 获取字符集
			String codeFormat = resolveCode(filePath);
			FileInputStream fis = new FileInputStream(filePath);
			// InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			// 将UTF-8 BOM 转为UTF-8
			UnicodeReader ur = new UnicodeReader(fis, codeFormat);
			br = new BufferedReader(ur);
			String recordString = null;
			String head = null;
			if ((head = br.readLine()) != null) {
				module = UNION_MODULE;
				// 因睡眠用户重启文件数据项QUERYDATE为大写，与其他两个不同，所以都转为小写
				head = head.trim().toLowerCase();
				log.info(head);
				log.info(module);
				log.info(String.valueOf(head.length()));
				log.info(String.valueOf(module.length()));
				// if (head.length() != module.length()) {
				// head = head.substring(1, head.length());
				// }
				// 判断文件数据项是否符合模板
				if (!head.equals(module)) {
					flag = UNFIT;
					return flag;
				}
				while ((recordString = br.readLine()) != null) {
					if (!StringUtil.isNullOrEmpty(recordString)) {
						recordString = recordString.trim();
						CpqImportThree oneRecord = handleRecord(type, recordString, md5Code, userName);
						results.add(oneRecord);
					}
				}
			}
		} catch (Exception e) {
			log.error("importThreeAbnormal controller handleFile error={}", e);
			flag = HANDLE_ERROR;
		} finally {
			br.close();
		}
		return flag;
	}

	*/
/**
	 * 将文件中每行数据转为实体
	 * 
	 * @param type
	 * @param record
	 * @param md5Code
	 * @param userName
	 * @return
	 * @throws ParseException
	 *//*

	private CpqImportThree handleRecord(String type, String record, String md5Code, String userName)
			throws ParseException {
		log.info("handleRecord start,param is record={}", record);
		String[] param = record.split(",");
		CpqImportThree importThree = new CpqImportThree();
		DateFormat df = new SimpleDateFormat(FILE_TIME_FORMAT);
		Date queryDate = df.parse(param[2]);
		Timestamp queryTime = new Timestamp(queryDate.getTime());
		importThree.setUsercode(param[0]);
		importThree.setQueryOrgNo(param[1]);
		importThree.setQueryDate(queryTime);
		importThree.setName(param[3]);
		importThree.setCertType(param[4]);
		importThree.setCertNo(param[5]);
		importThree.setResultFlag(param[6]);
		// 如果状态为未查得，信用报告编号栏位无数据
		if (param.length == 7) {
			importThree.setRepNo(null);
		} else {
			importThree.setRepNo(param[7]);
		}
		importThree.setIllustration("");
		importThree.setType(type);
		importThree.setMd5Code(md5Code);
		Date now = new Date();
		importThree.setImportTime(new Timestamp(now.getTime()));
		importThree.setImportUser(userName);
		compare(importThree);
		return importThree;
	}

	*/
/**
	 * 获取导入文件的编码
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 *//*

	private String resolveCode(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		byte[] head = new byte[3];
		inputStream.read(head);
		String code = "GBK";
		if (head[0] == -1 && head[1] == -2)
			code = "UTF-16";
		else if (head[0] == -2 && head[1] == -1)
			code = "Unicode";
		else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
			code = "UTF-8";

		inputStream.close();
		return code;
	}

	*/
/**
	 * 对比数据
	 * 
	 * @param importAbnormal
	 * @throws ParseException
	 *//*

	private void compare(CpqImportThree importAbnormal) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String queryDate = df.format(importAbnormal.getQueryDate());
		Timestamp startTime = new Timestamp(df.parse(queryDate).getTime());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startTime);
		calendar.add(calendar.DATE, 1);
		Timestamp endTime = new Timestamp(calendar.getTime().getTime());
		String[] certNos = importAbnormal.getCertNo().split("\\*\\*\\*\\*");
		String certNoHead = certNos[0];
		String certNoTail = certNos[1];
		List<CpqResultinfo> resultInfoList = resultInfoService.findToAbnormalThree(certNoHead, certNoTail,
				importAbnormal.getCertType(), importAbnormal.getName(), startTime, endTime,
				importAbnormal.getUsercode());
		if (resultInfoList != null && resultInfoList.size() != 0) {
			importAbnormal.setCompareFlag(YES);
		} else {
			importAbnormal.setCompareFlag(NO);
		}
	}

	@RequestMapping("/exportExcel")
	@ResponseBody
	public HttpServletResponse exportExcel(String ids, HttpServletRequest request, HttpServletResponse response,
			String type) {
		List<CpqImportThree> queryResults = null;
		String abnormalType = null;
		if (type.equals(SLEEPUSER)) {
			abnormalType = "睡眠用户重启";
		} else if (type.equals(QUERYQUANTITY)) {
			abnormalType = "查询量异常";
		} else if (type.equals(NOTWORKTIME)) {
			abnormalType = "非工作时间查询";
		}
		String fileName = System.currentTimeMillis() + "-abnormal-" + abnormalType + ".xls";
		String filePath = ConfigUtil.getExportPath();
		try {
			if (StringUtils.isEmpty(ids)) {
				Map<String, Object> searchParams = processRequestParams(new PageBean(), request);
				searchParams.put("EQ_type", type);
				queryResults = importThreeService.getAbnormalForList(searchParams);
			} else {
				queryResults = importThreeService.findByIds(Arrays.asList(ids.split("\\,")));
			}
			List<AbnormalThreeVO> vos = new ArrayList<>();
			for (CpqImportThree importThree : queryResults) {
				AbnormalThreeVO vo = new AbnormalThreeVO();
				BeanUtils.copyProperties(vo, importThree);
				vos.add(vo);
			}
			ExcelUtil.initExcel().write(vos, AbnormalThreeVO.class, filePath, fileName, true, true);
			File file = new File(filePath + File.separator + fileName);
			return FileUtil.download(file, response);
		} catch (Exception e) {
			log.error("exportExcel:", e);
		}
		return null;
	}

}
*/
