///**
// *  Copyright (c)  @date 2018年9月12日 DHCC, Inc.
// *  All rights reserved.
// *  东华软件股份公司 版权所有 征信监管产品工作平台
// */
//package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.task;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Future;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.redisson.api.RList;
//import org.redisson.api.RedissonClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.supercsv.io.CsvListReader;
//import org.supercsv.prefs.CsvPreference;
//
//import cn.com.dhcc.credit.platform.util.RedissonUtil;
//import cn.com.dhcc.credit.platform.util.ResourceManager;
//import cn.com.dhcc.credit.platform.util.SysDate;
//import cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean.BatchQueryDetailVo;
//import cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean.BatchRequestQueryResultVo;
//import cn.com.dhcc.creditquery.person.queryweb.util.BatchQueryThreadPoolExecutorUtil;
//import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
//import cn.com.dhcc.creditquery.person.queryweb.util.ValidateUtil;
//import cn.com.dhcc.creditquery.person.queryweb.util.WebApplicationContextUtil;
//import cn.com.dhcc.creditquery.person.queryweb.webservice.WebserviceCommon;
//import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
//import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;
//import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
//import cn.com.dhcc.query.creditpersonquerydao.entity.resultInfo.CpqResultinfo;
//import cn.com.dhcc.query.creditpersonquerydao.entity.userarr.CpqUserAttr;
//import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
//import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.CpqBatchquerydetailService;
//import cn.com.dhcc.query.creditpersonqueryservice.querynumcounter.service.CpqQueryNumCounterService;
//import cn.com.dhcc.query.creditpersonqueryservice.userattr.service.util.UserAttrUtil;
//import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
//import cn.com.dhcc.query.creditpersonqueryservice.vo.UserQueryNumVO;
//import cn.com.dhcc.query.creditquerycommon.Constant;
//import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
//import cn.com.dhcc.query.creditquerycommon.util.sftp.util.Sftp;
//import cn.com.dhcc.query.creditquerycommon.util.sftp.util.SftpUtil;
//
///**
// * 进行批量文件获取、验证、请求拆分、除错、去重
// *
// * @author lekang.liu
// * @date 2018年9月12日
// */
//public class GenerationBatchFilePretreatmentTask implements Runnable {
//
//	private static Logger log = LoggerFactory.getLogger(GenerationBatchFilePretreatmentTask.class);
//
//	private CpqBatchinfo batchInfo;
//
//	private CpqBatchinfoService batchinfoService;
//
//	private CpqBatchquerydetailService detailService;
//
//	private CpqQueryNumCounterService queryNumCounterService;
//
//	private RedissonClient redis = RedissonUtil.getLocalRedisson();
//
//	public CpqBatchinfo getBatchInfo() {
//		return batchInfo;
//	}
//
//	public void setBatchInfo(CpqBatchinfo batchInfo) {
//		this.batchInfo = batchInfo;
//	}
//
//	public GenerationBatchFilePretreatmentTask(CpqBatchinfo batchInfo) {
//		this.batchInfo = batchInfo;
//	}
//
//	@Override
//	public void run() {
//		init();
//		try {
//
//			// 文件获取及验证
//
//			// 请求文件原路径
//			String reqfile = batchInfo.getReqfile();
//			// 请求文件原文件名
//			String fileName = batchInfo.getReqFileName();
//
//			// 文件名验证
//			boolean isExist = batchinfoService.isExistBatchinfoByReqFileName(fileName, batchInfo.getId());
//
//			if (isExist) {
//				batchInfo.setErrCode("3");
//				batchInfo.setErrinfo("已存在请求文件名相同的批次");
//				batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
//				batchinfoService.update(batchInfo);
//				// 更改批次信息,将批次号存入redis
//				RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
//				list.add(batchInfo.getId());
//				return;
//			}
//
//			String operOrg = WebserviceCommon.getQueryUserOrg(batchInfo.getOperator());//获取当前机构
//			String topOrg = UserUtils.getRootDeptCode(operOrg);// 获取顶级机构
//			// 本地目标路径
//			String localFilePath = ConfigUtil.getBatchQueryFilePath(topOrg);
//			localFilePath = localFilePath + SysDate.getDate() + File.separator + batchInfo.getId();
//
//			batchInfo.setLocalFilePath(localFilePath);
//
//			localFilePath = localFilePath + File.separator + "request" + File.separator;
//			// 判断文件存不存在
//
//			String fileMode = batchInfo.getFileMode();
//
//			int falg = 0;
//
//			File localFile = new File(localFilePath);
//			if (!localFile.exists()) {
//				localFile.mkdirs();
//			}
//
//			if (Objects.equals("0", fileMode)/* FTP方式 */) {
//
//				// 后续改为使用FTP方式
//
//				// 获取SFTP连接
//				String sshHost = ResourceManager.getInstance().getValue("ftp.sshHost");
//				int sshPort = ResourceManager.getInstance().getIntValue("ftp.sshPort");
//				String sshUser = ResourceManager.getInstance().getValue("ftp.sshUser");
//				String sshPass = ResourceManager.getInstance().getValue("ftp.sshPass");
//				Sftp sftp = SftpUtil.getSftp(sshHost, sshPort, sshUser, sshPass);
//
//				// 文件存不存在
//				boolean exist = sftp.exist(reqfile, fileName);
//				falg = exist ? falg : 1;
//				if (!exist/* 文件不存在 */) {
//					batchInfo.setErrCode("1");
//					batchInfo.setErrinfo("文件不存在");
//					batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
//					batchinfoService.update(batchInfo);
//					// 更改批次信息,将批次号存入redis
//					RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
//					list.add(batchInfo.getId());
//					return;
//				}
//
//				// 获取文件
//				sftp.get(reqfile + fileName, localFilePath + fileName);
//				sftp.close();
//			} else/* NAS共享磁盘方式 */ {
//				// 结果文件生成路径
//				File srcFile = new File(reqfile + fileName);
//
//				if (!srcFile.exists()/* 文件不存在 */) {
//					batchInfo.setErrCode("1");
//					batchInfo.setErrinfo("文件不存在");
//					batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
//					batchinfoService.update(batchInfo);
//					// 更改批次信息,将批次号存入redis
//					RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
//					list.add(batchInfo.getId());
//					return;
//				}
//
//				File destFile = new File(localFilePath + fileName);
//
//				FileUtils.copyFile(srcFile, destFile);
//			}
//
//			// 请求拆分、除错、去重
//
//			File localPretreatmentFile = new File(localFilePath + fileName);
//
//			ArrayList<BatchQueryDetailVo> batchDetile = new ArrayList<>();
//			// 根据文件后缀进行不同处理
//			if (StringUtils.endsWith(fileName, ".xls")/* xls文件 */) {
//
//				Workbook workbook = WorkbookFactory.create(localPretreatmentFile);
//
//				Sheet sheetAt = workbook.getSheetAt(0);
//				boolean bl = true;
//				String orgCode = "";
//				for (Row row : sheetAt) {
//					// 获取单元格的值
//					int rowNum = row.getRowNum();
//					if (0 == rowNum) {
//						continue;
//					}
//					if(bl){
//						orgCode = WebserviceCommon.getQueryUserOrg(getValueByExc(row, 6));
//						bl = false;
//					}
//					BatchQueryDetailVo batchquerydetail = new BatchQueryDetailVo();
//
//					batchquerydetail.setCerttype(getValueByExc(row, 0));
//					batchquerydetail.setCertno(getValueByExc(row, 1));
//					batchquerydetail.setName(getValueByExc(row, 2));
//					batchquerydetail.setQryreason(getValueByExc(row, 3));
//					batchquerydetail.setQueryFormat(getValueByExc(row, 4));
//					batchquerydetail.setQrytype(getValueByExc(row, 5));
//					batchquerydetail.setOperorg(orgCode);
//					batchquerydetail.setOperator(getValueByExc(row, 6));
//					batchquerydetail.setAutharchiveid(getValueByExc(row, 7));
//					batchquerydetail.setAutharchivedata(getValueByExc(row, 8).trim());
//					batchDetile.add(batchquerydetail);
//				}
//
//			} else/* txt文件 */ {
//
//				FileInputStream fis = new FileInputStream(localPretreatmentFile);
//
//				InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
//
//				List<String> rowList = new ArrayList<String>();
//
//				CsvListReader csvReader = new CsvListReader(isr, CsvPreference.EXCEL_PREFERENCE);
//				boolean bl = true;
//				String orgCode = "";
//				while ((rowList = csvReader.read()) != null) {
//					// XXX 这里没有进行列数验证可能有问题
//					int lineNumber = csvReader.getLineNumber();
//					if (lineNumber == 1) {
//						continue;
//					}
//					if(bl){
//						orgCode = WebserviceCommon.getQueryUserOrg(getValue(rowList, 6));
//						bl = false;
//					}
//					BatchQueryDetailVo batchquerydetail = new BatchQueryDetailVo();
//					batchquerydetail.setCerttype(getValue(rowList, 0));
//					batchquerydetail.setCertno(getValue(rowList, 1));
//					batchquerydetail.setName(getValue(rowList, 2));
//					batchquerydetail.setQryreason(getValue(rowList, 3));
//					batchquerydetail.setQueryFormat(getValue(rowList, 4));
//					batchquerydetail.setQrytype(getValue(rowList, 5));
//					batchquerydetail.setOperorg(orgCode);
//					batchquerydetail.setOperator(getValue(rowList, 6));
//					batchquerydetail.setAutharchiveid(getValue(rowList, 7));
//					batchquerydetail.setAutharchivedata(getValue(rowList, 8).trim());
//					batchDetile.add(batchquerydetail);
//
//				}
//
//				isr.close();
//				fis.close();
//				csvReader.close();
//
//			}
//
//			// 判断文件行数
//			int size = batchDetile.size();
//			if (size > 2000) {
//				batchInfo.setErrCode("2");
//				batchInfo.setErrinfo("文件过大");
//				batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
//				batchinfoService.update(batchInfo);
//
//				// 更改批次信息,将批次号存入redis
//				RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
//				list.add(batchInfo.getId());
//
//				return;
//			}
//
//			// 除错
//			List<BatchQueryDetailVo> errorList = new ArrayList<>();
//
//			for (Iterator<BatchQueryDetailVo> iterator = batchDetile.iterator(); iterator.hasNext();) {
//				BatchQueryDetailVo batchquerydetailBean = (BatchQueryDetailVo) iterator.next();
//				ResultBeans validate = ValidateUtil.validate(batchquerydetailBean);
//				if (null != validate) {
//					batchquerydetailBean.setErrorFlag("1");
//					batchquerydetailBean.setErrorCode("7");/* 验证失败 */
//					batchquerydetailBean.setErrorMsg(validate.getMsg().replace("<br/>", ""));
//					errorList.add(batchquerydetailBean);
//					iterator.remove();
//				}
//
//			}
//
//			// for (BatchQueryDetailVo batchquerydetailBean : batchDetile) {
//			// ResultBeans validate =
//			// ValidateUtil.validate(batchquerydetailBean);
//			// if (null != validate) {
//			// batchquerydetailBean.setErrorFlag("1");
//			// batchquerydetailBean.setErrorCode("7");/*验证失败*/
//			// batchquerydetailBean.setErrorMsg(validate.getMsg().replace("<br/>",
//			// ""));
//			// errorList.add(batchquerydetailBean);
//			// }
//			// }
//			// batchDetile.removeAll(errorList);
//
//			// 去重
//			Set<BatchQueryDetailVo> set = new HashSet<>();
//
//			set.addAll(batchDetile);
//			// 不重复的请求
//			List<BatchQueryDetailVo> undupList = new ArrayList<>();
//
//			for (Iterator<BatchQueryDetailVo> iterator = set.iterator(); iterator.hasNext();) {
//				BatchQueryDetailVo batchquerydetailBean = (BatchQueryDetailVo) iterator.next();
//				undupList.add(batchquerydetailBean);
//				batchDetile.remove(batchquerydetailBean);
//			}
//
//			// 保存批量明细
//			// 此时undupList中全部为可查询请求、batchDetile为重复请求、errorList为错误请求
//
//			// 可查询list
//			ArrayList<CpqBatchquerydetail> cpqBatchDetile = new ArrayList<>();
//
//			for (BatchQueryDetailVo batchquerydetailBean : undupList) {
//				CpqBatchquerydetail cpqBatchquerydetail = new CpqBatchquerydetail();
//				BeanUtils.copyProperties(batchquerydetailBean, cpqBatchquerydetail);
//				cpqBatchquerydetail.setBatchno(batchInfo.getId());
//				cpqBatchquerydetail.setTimeBound(batchInfo.getQtimelimit());
//				// 1 标识一代报告
//				cpqBatchquerydetail.setVersion(1);
//				cpqBatchquerydetail.setUpdatetime(new Date());
//				cpqBatchDetile.add(cpqBatchquerydetail);
//			}
//
//			// 重复list
//			ArrayList<CpqBatchquerydetail> cpqDupList = new ArrayList<>();
//
//			for (BatchQueryDetailVo batchquerydetailBean : batchDetile) {
//				CpqBatchquerydetail cpqBatchquerydetail = new CpqBatchquerydetail();
//				BeanUtils.copyProperties(batchquerydetailBean, cpqBatchquerydetail);
//				cpqBatchquerydetail.setBatchno(batchInfo.getId());
//				cpqBatchquerydetail.setTimeBound(batchInfo.getQtimelimit());
//				// 1 标识一代报告
//				cpqBatchquerydetail.setVersion(1);
//				cpqBatchquerydetail.setUpdatetime(new Date());
//				cpqBatchquerydetail.setDupflag("1");
//				cpqBatchquerydetail.setErrorCode("8");
//				cpqBatchquerydetail.setErrorMsg("请求文件中存在与其重复的请求");
//				cpqDupList.add(cpqBatchquerydetail);
//			}
//
//			// 错误list
//			ArrayList<CpqBatchquerydetail> cpqErrorList = new ArrayList<>();
//
//			for (BatchQueryDetailVo batchquerydetailBean : errorList) {
//				CpqBatchquerydetail cpqBatchquerydetail = new CpqBatchquerydetail();
//				BeanUtils.copyProperties(batchquerydetailBean, cpqBatchquerydetail);
//				cpqBatchquerydetail.setBatchno(batchInfo.getId());
//				cpqBatchquerydetail.setTimeBound(batchInfo.getQtimelimit());
//				// 1 标识一代报告
//				cpqBatchquerydetail.setVersion(1);
//				cpqBatchquerydetail.setUpdatetime(new Date());
//				cpqErrorList.add(cpqBatchquerydetail);
//			}
//
//			// 更新表
//			detailService.createBatchDetailWithList(cpqDupList);
//			detailService.createBatchDetailWithList(cpqErrorList);
//			List<CpqBatchquerydetail> batchDetailList = detailService.createBatchDetailWithList(cpqBatchDetile);
//
//			// 更新批次状态
//			batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_QUERY);
//			batchinfoService.update(batchInfo);
//
//			// 拉起查询线程
//			// TODO 考虑到最优性能，这里可以将可查询数量进行分组，分批次进行查询
//			ExecutorService batchQueryExecutorService = BatchQueryThreadPoolExecutorUtil.getBatchQueryExecutorService();
//
//			List<Future<BatchRequestQueryResultVo>> futureList = new ArrayList<>();
//			UserQueryNumVO userQueryvo = new UserQueryNumVO();
//			CpqResultinfo cpqResultinfo = new CpqResultinfo();
//			for (int i = 0; i < batchDetailList.size(); i++) {
//				CpqBatchquerydetail batchquerydetail = batchDetailList.get(i);
//				//查询前计数
//				counter(batchquerydetail,userQueryvo,cpqResultinfo);
//				Future<BatchRequestQueryResultVo> submitFuture = batchQueryExecutorService
//						.submit(new GenerationBatchRequestQueryTask(batchquerydetail, batchInfo.getResulttype(),i));
//				futureList.add(submitFuture);
//			}
//
//			for (Future<BatchRequestQueryResultVo> future : futureList) {
//				future.get();
//			}
//
//			// 更新批次状态为查询处理完毕
//			batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_RESULT_PACK);
//			CpqBatchinfo update = batchinfoService.update(batchInfo);
//
//			// 调用打包线程进行打包处理
//			ExecutorService batchResultPackThreadPool = BatchQueryThreadPoolExecutorUtil.getBatchResultPackThreadPool();
//
//			batchResultPackThreadPool.submit(new GenerationBatchResultPackTask(update));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("GenerationBatchFilePretreatmentTask error batchInfo = {}, e = {} ", batchInfo, e);
//			// 更改批次信息,将批次号存入redis
//			batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
//			batchInfo.setErrinfo("处理异常");
//			batchinfoService.update(batchInfo);
//			// 更改批次信息,将批次号存入redis
//			RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
//			list.add(batchInfo.getId());
//		}
//
//	}
//
//	/**
//	 * 计数
//	 * @param batchquerydetail
//	 */
//	private void counter(CpqBatchquerydetail batchquerydetail,UserQueryNumVO vo,CpqResultinfo cpqResultinfo){
//		CpqUserAttr userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(batchquerydetail.getOperator());
//		if(userAttr != null){
//			CpqCcUser ccuser = UserAttrUtil.getCreditUserByCcId(userAttr.getCreditUser());
//			if(ccuser != null){
//				vo.setCreditUser(ccuser.getCcuser());
//			}
//		}
//		if(batchquerydetail.getTimeBound().toString().equals("0")){
//			vo.setSource("2");
//		}else{
//			vo.setSource("1");//本地
//		}
//		vo.setUserName(batchquerydetail.getOperator());
//		vo.setDeptCode(batchquerydetail.getOperorg());
//		queryNumCounterService.creditUserRecordHF(vo);//征信用户高频记录
//		queryNumCounterService.userRecordHF(vo);//内部用户高频记录
//		queryNumCounterService.monitoringInfoNotState(vo);//监控查询量 notstatus
//
//		cpqResultinfo.setOperator(batchquerydetail.getOperator());
//		cpqResultinfo.setCertNo(batchquerydetail.getCertno());
//		cpqResultinfo.setCertType(batchquerydetail.getCerttype());
//		cpqResultinfo.setOperOrg(batchquerydetail.getOperorg());
//		queryNumCounterService.DifferentPlacesQueryNum(cpqResultinfo);//监控异地查询数
//	}
//
//	private void init() {
//		batchinfoService = (CpqBatchinfoService) WebApplicationContextUtil.getBean("cpqBatchinfoServiceImpl");
//		detailService = (CpqBatchquerydetailService) WebApplicationContextUtil
//				.getBean("cpqBatchquerydetailServiceImpl");
//		queryNumCounterService = (CpqQueryNumCounterService) WebApplicationContextUtil.getBean("cpqQueryNumCounterServiceImpl");
//	}
//
//	private String getValue(List<String> rowList, int num) {
//		String returnString = "";
//		if (null != rowList.get(num)) {
//			returnString = rowList.get(num);
//			returnString = returnString.replace("\t", "");
//		}
//		return returnString;
//	}
//
//	/**
//	 * 获取单元格的值
//	 *
//	 * @param row
//	 * @param num
//	 * @return String
//	 */
//	private String getValueByExc(Row row, int num) {
//		String value = "";
//		Cell cell = row.getCell(num);
//		if (null != cell) {
//			cell.setCellType(Cell.CELL_TYPE_STRING);
//			value = cell.getStringCellValue();
//		}
//		return value;
//	}
//
//	/**
//	 * 将单元格设置为文本
//	 *
//	 * @param cell
//	 * @return Cell
//	 */
//	private Cell setCellTypeToString(Cell cell) {
//		cell.setCellType(Cell.CELL_TYPE_STRING);
//		return cell;
//	}
//
//}
