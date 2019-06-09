/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.manage.BatchManageThread;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchErrorBean;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchReqBean;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.CpqBatchquerydetailService;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.impl.CpqBatchquerydetailServiceImpl;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;

*/
/**
 * 对批次文件进行去重，除错；并拆分存储查询记录
 * 
 * @author lekang.liu
 * @date 2018年6月29日
 *//*

public class BatchFilePretreatmentTask extends Thread {
	private static Logger log = LoggerFactory.getLogger(BatchFilePretreatmentTask.class);

	private CpqBatchinfoService batchinfoService;

	private CpqBatchquerydetailService batchDetailService;

	private CpqBatchinfo batchInfo;

	public BatchFilePretreatmentTask() {
		super();
	}

	public BatchFilePretreatmentTask(CpqBatchinfo batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Override
	public void run() {
		log.info("BatchFilePretreatmentTask  get task = {}", batchInfo.getId());
		init();
		try {

			// 错误请求信息列表
			List<BatchErrorBean> errorList = new ArrayList<BatchErrorBean>();

			// 有效请求信息列表
			List<BatchReqBean> reqList = new ArrayList<BatchReqBean>();

			// 人行请求信息列表
			List<BatchReqBean> crcReqList = new ArrayList<BatchReqBean>();

			// 请求数
			AtomicInteger reqcount = new AtomicInteger(0);

			// 错误请求数
			AtomicInteger errorcount = new AtomicInteger(0);

			// 人行请求数
			AtomicInteger crcount = new AtomicInteger(0);

			// 本地请求数
			AtomicInteger nativcount = new AtomicInteger(0);

			// 读取处理请求文件
			readReqFile(errorList, reqList, batchInfo, reqcount, errorcount);

			// 获取错误文件存放路径
			String errorpath = ConfigUtil.getBatchQueryFilePath();
			// 错误请求信息列表数不为空则写错误文件
			if (!errorList.isEmpty()) {
				Calendar cal = Calendar.getInstance();
				DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				String filePreffixName = format.format(cal.getTime());
				String reqfilePath = batchInfo.getReqfile();
				// 获取请求文件名
				String filename = reqfilePath.substring(reqfilePath.lastIndexOf(File.separator) + 1, reqfilePath.lastIndexOf("."));
				errorpath = errorpath + batchInfo.getId() + "";
				// 生产错误文件名
				filename = filename + "_" + filePreffixName + "_error.txt";
				// 写出错误文件
				writeErrorFile(errorList, errorpath, filename);
			}
			// 应在接口中进行校验
			if (reqcount.get() == 0) {
				// 文件中无请求
				updateBatchWithNoReq();
				return;
			}

			// 可能出现(请求文件中获取的请求全部错误)
			if (reqcount.get() == errorcount.get()) {
				// 更新批次信息为查询完毕，直接进行打包
				updateBatchInfo(reqcount, errorcount, crcount, nativcount, errorpath, "2");
				return;
			}

			// 判断查询请求时限是否为0（为0代表只差远程）
			if (0 == batchInfo.getQtimelimit()) {
				// 所有有效请求插入人行请求信息列表
				crcReqList.addAll(reqList);
				crcount.set(reqList.size());
			} else */
/* 否则全查本地 *//*
 {
				nativcount.set(reqList.size());
			}

			// 去重后的请求列表
			List<BatchReqBean> uniqueArray = new ArrayList<BatchReqBean>();

			// 重复的请求列表
			List<BatchReqBean> echoArray = new ArrayList<BatchReqBean>();

			// 进行请求去重
			uniqueReqList(crcReqList, uniqueArray, echoArray);

			// 更新批次信息、保存批次请求明细
			saveBatchDetail(uniqueArray, echoArray);
			// 更新批次信息为待查询。并更新redis
			updateBatchInfo(reqcount, errorcount, crcount, nativcount, errorpath, "1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	*/
/**
	 * 读取请求文件，校验请求
	 * 
	 * @param errorList
	 * @param reqList
	 * @param batchinfoDto
	 * @param reqcount
	 * @param errorcount
	 * @throws ICRQSException
	 *//*

	private void readReqFile(List<BatchErrorBean> errorList, List<BatchReqBean> reqList, CpqBatchinfo batchinfoDto, AtomicInteger reqcount, AtomicInteger errorcount) throws Exception {
		log.info("BatchFilePretreatmentTask  readReqFile batchinfoDto id = {}", batchinfoDto.getId());
		// 获取请求文件名路径
		String reqFile = batchinfoDto.getReqfile();

		// String orgcode = batchinfoDto.getOperorg();

		File file = new File(reqFile);

		BufferedReader in = null;

		CsvListReader csvReader = null;

		InputStreamReader isr = null;

		FileInputStream charfis = null;

		FileInputStream fis = null;

		try {

			fis = new FileInputStream(file);

			charfis = new FileInputStream(file);

			// 判断请求文件编码

			isr = new InputStreamReader(fis, "GBK");

			List<String> rowList = new ArrayList<String>();

			csvReader = new CsvListReader(isr, CsvPreference.EXCEL_PREFERENCE);

			List<BatchReqBean> preReqList = new ArrayList<BatchReqBean>();

			while ((rowList = csvReader.read()) != null) {

				// 读取一行请求请求数加1
				reqcount.incrementAndGet();

				if (9 == rowList.size()) {
					BatchReqBean batchReq = new BatchReqBean();
					batchReq.setName(getValue(rowList, 0));
					batchReq.setCerttype(getValue(rowList, 1));
					batchReq.setCertno(getValue(rowList, 2));
					batchReq.setQryreason(getValue(rowList, 3));
					batchReq.setServiceCode(getValue(rowList, 4));
					batchReq.setOperator(getValue(rowList, 5));
					batchReq.setAutharchiveid(getValue(rowList, 6));
					batchReq.setAssocbsnssdata(getValue(rowList, 7));
					preReqList.add(batchReq);
				} else {
					BatchErrorBean errorBean = new BatchErrorBean();
					errorBean.setContent(rowList.toString());
					errorBean.setErrorcode("0");
					errorList.add(errorBean);
				}
			}

			// 请求数大于1则进行验证
			if (preReqList.size() > 0) {
				// 验证数据是否可用
				// 所有请求 有效请求 错误请求
				BatchReqValidate.validate(preReqList, reqList, errorList);
			}
			// 设置错误请求数
			errorcount.set(errorList.size());

		} catch (Exception e) {
			log.info("BatchFilePretreatmentTask  readReqFile error = {}", e);
			throw e;
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != isr) {
					isr.close();
				}
				if (null != fis) {
					fis.close();
				}
				if (null != charfis) {
					charfis.close();
				}
				if (null != csvReader) {
					csvReader.close();
				}
			} catch (IOException e) {
				log.info("BatchFilePretreatmentTask  readReqFile error = {}", e);
				throw e;
			}
		}

	}

	*/
/**
	 * 
	 * @param errorList
	 * @param filepath
	 * @param filename
	 * @throws ICRQSException
	 * @return void 返回类型
	 *//*

	private void writeErrorFile(List<BatchErrorBean> errorList, String filepath, String filename) throws Exception {
		File folder = new File(filepath);
		BufferedOutputStream out = null;
		try {
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File errorFile = new File(filepath + File.separator + filename);
			if (errorFile.exists()) {
				errorFile.delete();
			}
			out = new BufferedOutputStream(new FileOutputStream(errorFile));
			String title = "错误行内容,错误代码,错误原因";
			out.write(title.getBytes());
			out.write("\r\n".getBytes());
			for (BatchErrorBean batchError : errorList) {
				StringBuffer sb = new StringBuffer();
				sb.append(batchError.getContent().replaceAll(",", "_")).append(",");
				sb.append(batchError.getErrorcode()).append(",");
				sb.append(batchError.getErrorinfo());
				out.write((sb.toString()).getBytes());
				out.write("\r\n".getBytes());
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getValue(List<String> rowList, int num) {
		String returnString = null;
		if (null != rowList.get(num)) {
			returnString = rowList.get(num);
			returnString = returnString.replace("\t", "");
		}
		return returnString;
	}

	private void uniqueReqList(List<BatchReqBean> reqList, List<BatchReqBean> uniqueArray, List<BatchReqBean> echoArray) throws Exception {

		// 实例化用于去重的set
		Set<BatchReqBean> set = new HashSet<BatchReqBean>();
		// set添加所有请求
		set.addAll(reqList);
		// 重复列表添加所有请求
		echoArray.addAll(reqList);
		// 去重列表添加set内容（由于set的特性，set内的内容已经是去重之后的请求）
		uniqueArray.addAll(set);
		// 遍历set
		for (Iterator<BatchReqBean> iterator = set.iterator(); iterator.hasNext();) {
			BatchReqBean batchReqBean = iterator.next();
			// 从重复列表中去除非重复请求
			echoArray.remove(batchReqBean);
		}
	}

	private void updateBatchInfo(AtomicInteger reqcount, AtomicInteger errorcount, AtomicInteger crcount, AtomicInteger nativcount, String errorpath, String status) throws Exception {
		log.info("BatchFilePretreatmentTask  updateBatchInfo start  batchInfo  status = {}", status);
		try {
			CpqBatchinfo targetDto = batchinfoService.findById(batchInfo.getId());
			targetDto.setReqcount(reqcount.get());
			targetDto.setErrorreq(errorcount.get());
			// 本地请求数
			targetDto.setLocreq(nativcount.get());
			// 人行请求数
			targetDto.setPbcreq(crcount.get());
			targetDto.setPbcerr(0);
			targetDto.setPbcright(0);
			targetDto.setUpdatetime(new Date(System.currentTimeMillis()));

			if (StringUtils.isNotBlank(errorpath)) {
				// 错误文件
				targetDto.setResfileerr(errorpath);
			}
			targetDto.setStatus(status);

			BatchInRedis batchInRedis = new BatchInRedis(targetDto.getId(), status, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
			batchinfoService.update(targetDto, batchInRedis);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BatchFilePretreatmentTask  updateBatchInfo  batchInfo ID = "+batchInfo.getId()+ " error = {}", e);
		}
		File reqfile = new File(batchInfo.getReqfile());// 删除请求文件
		if (reqfile.exists()) {
			reqfile.delete();
		}
		log.info("BatchFilePretreatmentTask  updateBatchInfo end ");
	}

	*/
/**
	 * 批次中无请求信息，将批次状态更新为处理失败。错误原因记为无请求信息。
	 * 
	 * @param 参数
	 * @return void 返回类型
	 *//*

	public void updateBatchWithNoReq() {
		log.info("BatchFilePretreatmentTask  updateBatchWithNoReq  batchInfo id = {}", batchInfo.getId());
		batchInfo.setStatus("4");
		batchInfo.setErrinfo("无请求信息");
		BatchInRedis batchInRedis = new BatchInRedis(batchInfo.getId(), "4", Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
		batchinfoService.update(batchInfo, batchInRedis);
	}

	public void init() {
		batchinfoService = (CpqBatchinfoService) getBean("cpqBatchinfoServiceImpl");
		batchDetailService = (CpqBatchquerydetailServiceImpl) getBean("cpqBatchquerydetailServiceImpl");
	}

	*/
/**
	 * 获取bean
	 * 
	 * @param beanName
	 * @return Object
	 *//*

	public static Object getBean(String beanName) {
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		Object bean = applicationContext.getBean(beanName);
		return bean;
	}

	private void saveBatchDetail(List<BatchReqBean> uniqueArray, List<BatchReqBean> echoArray) {
		List<CpqBatchquerydetail> batchDetail = new ArrayList<CpqBatchquerydetail>();
		// 将请求存入明细表
		int i = 1;
		for (BatchReqBean batchReqBean : uniqueArray) {
			CpqBatchquerydetail batchquerydetail = new CpqBatchquerydetail();
			batchReqBean.toBatchQueryDetail(batchquerydetail);
			batchquerydetail.setBatchno(batchInfo.getId());
			batchquerydetail.setDupflag("0");
			batchquerydetail.setUpdatetime(new Date());
			batchquerydetail.setOperorg(batchInfo.getOperorg());
			batchquerydetail.setVersion(2);
			int num = i++;
			batchquerydetail.setResultId(num+"");
			batchDetail.add(batchquerydetail);
		}
		for (BatchReqBean batchReqBean : echoArray) {
			CpqBatchquerydetail batchquerydetail = new CpqBatchquerydetail();
			batchReqBean.toBatchQueryDetail(batchquerydetail);
			batchquerydetail.setBatchno(batchInfo.getId());
			batchquerydetail.setDupflag("1");
			batchquerydetail.setUpdatetime(new Date());
			batchquerydetail.setOperorg(batchInfo.getOperorg());
			batchquerydetail.setVersion(2);
			int num = i++;
			batchquerydetail.setResultId(num+"");

			batchDetail.add(batchquerydetail);
		}

		// TODO 存入明细表
		// BatchInRedis batchInRedis = new
		// BatchInRedis(batchInfo.getId(),"1",Conntanst.BATCH_ROWSTATUS_UNTREATED,
		// new Date());
		batchDetailService.createBatchDetailWithList(batchDetail);

	}

}
*/
