/**
 *  Copyright (c)  @date 2018年9月13日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*

package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.credit.platform.util.ResourceManager;
import cn.com.dhcc.creditquery.person.queryweb.util.WebApplicationContextUtil;
import cn.com.dhcc.creditquery.person.queryweb.util.ZipUtil;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;
import cn.com.dhcc.query.creditpersonquerydao.entity.resultInfo.CpqResultinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.resultmsg.CpqResultmsg;
import cn.com.dhcc.query.creditpersonqueryinquire.remote.util.EncryptUtil;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.CpqBatchquerydetailService;
import cn.com.dhcc.query.creditpersonqueryservice.policy.utils.InConstant;
import cn.com.dhcc.query.creditpersonqueryservice.queryreq.service.CpqResultinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.resultmsg.CpqResultmsgService;
import cn.com.dhcc.query.creditpersonqueryservice.resultmsg.impl.CpqResultmsgServiceImpl;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.util.sftp.util.Sftp;
import cn.com.dhcc.query.creditquerycommon.util.sftp.util.SftpUtil;

*/
/**
 * 一代批量结果打包工作线程
 * 
 * @author lekang.liu
 * @date 2018年9月13日
 *//*

public class GenerationBatchResultPackTask implements Runnable {

	private static Logger log = LoggerFactory.getLogger(GenerationBatchResultPackTask.class);
	
	private CpqBatchinfo batchInfo;

	private CpqBatchquerydetailService detailService;

	private CpqBatchinfoService batchinfoService;

	private CpqResultinfoService resultService;

	private CpqResultmsgService resultMsgService;

	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	public GenerationBatchResultPackTask(CpqBatchinfo batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Override
	public void run() {
		try {
			log.info("GenerationBatchResultPackTask is run batchInfo = ",batchInfo);
			init();
			// 根据批次号获取批次中所有的查询记录
			List<CpqBatchquerydetail> dupDetailList = detailService.getDupBatchDetailByBatchNo(batchInfo.getId());
			List<CpqBatchquerydetail> errorDetailList = detailService.getErrorBatchDetailByBatchNo(batchInfo.getId());
			List<CpqBatchquerydetail> sussDetailList = detailService.getSussBatchDetailByBatchNo(batchInfo.getId());

			// 0 查询成功 1 查询本地失败 2 查询人行失败
			// 剔除查询过程中失败的请求
			for (CpqBatchquerydetail cpqBatchquerydetail : sussDetailList) {
				if (!StringUtils.equals("0", cpqBatchquerydetail.getStatus())) {
					errorDetailList.add(cpqBatchquerydetail);
				}
			}
			sussDetailList.removeAll(errorDetailList);

			String path = batchInfo.getLocalFilePath();
			// 结果文件夹
			String resultPath = path + File.separator + "result" + File.separator;
			String zipPath = path + File.separator + "resultZip" + File.separator;
			
			//创建结果包文件夹
			File zipFileDir = new File(zipPath);
			if (!zipFileDir.exists()) {
				zipFileDir.mkdirs();
			}
			
			// 创建信用报告文件夹
			String reportPath = resultPath + "report" + File.separator;
			File reportDir = new File(reportPath);
			if (!reportDir.exists()) {
				reportDir.mkdirs();
			}
			// 成功结果文件
			String sussPath = resultPath + "suss_request.txt";
			File sussFile = new File(sussPath);

			String fileTitel = "证件类型,证件号码,客户姓名,查询原因,查询版式,查询类型,查询机构,查询员,档案编号,关联业务数据";
			// 写入文件头
			BufferedWriter sussFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sussFile), "UTF-8"));
			sussFileWriter.write(fileTitel);
			sussFileWriter.flush();

			for (CpqBatchquerydetail cpqBatchquerydetail : sussDetailList) {

				// 获取信用报告
				CpqResultinfo resultinfo = resultService.findById(cpqBatchquerydetail.getResultId());
				CpqResultmsg resultmsg = resultMsgService.findById(resultinfo.getCreditId());
				
				if (StringUtils.equals("2", batchInfo.getResulttype())||StringUtils.equals("4", batchInfo.getResulttype())) {
					String fileName = cpqBatchquerydetail.getName() + "_" + cpqBatchquerydetail.getCertno() + ".html";
					BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportPath + fileName), "GBK"));
					// 将有报告的请求生成报告
					byte[] html = resultmsg.getHtml();
					//这个htmlStr是加密的
					String htmlStr = new String(html,"GBK");
					htmlStr = EncryptUtil.decryptByRC4(htmlStr, InConstant.ENCODEPWD);
					htmlWriter.write(htmlStr);
					htmlWriter.flush();
					htmlWriter.close();
				}

				if (StringUtils.equals("1", batchInfo.getResulttype())||StringUtils.equals("4", batchInfo.getResulttype())) {
					String xmlfileName = cpqBatchquerydetail.getName() + "_" + cpqBatchquerydetail.getCertno() + ".xml";
					BufferedWriter xmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportPath + xmlfileName), "UTF-8"));
					xmlWriter.write(new String(resultmsg.getXml()));
					xmlWriter.flush();
					xmlWriter.close();
				}
				
				// 将成功请求写入成功列表
				sussFileWriter.newLine();
				sussFileWriter.write(cpqBatchquerydetail.toStringWithWriter(false));
				sussFileWriter.flush();

			}
			sussFileWriter.close();

			// 失败结果文件
			String errorPath = resultPath + "error_request.txt";
			File errorFile = new File(errorPath);

			// 写入文件头
			BufferedWriter errorFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(errorFile), "UTF-8"));
			String errorfileTitel = "证件类型,证件号码,客户姓名,查询原因,查询版式,查询类型,查询机构,查询员,档案编号,关联业务数据,错误代码,错误信息";
			errorFileWriter.write(errorfileTitel);
			errorFileWriter.flush();
			// 将重复请求写入错误列表
			for (CpqBatchquerydetail cpqBatchquerydetail : dupDetailList) {
				errorFileWriter.newLine();
				errorFileWriter.write(cpqBatchquerydetail.toStringWithWriter(true));
				errorFileWriter.flush();
			}
			// 将错误请求写入错误列表
			for (CpqBatchquerydetail cpqBatchquerydetail : errorDetailList) {
				errorFileWriter.newLine();
				errorFileWriter.write(cpqBatchquerydetail.toStringWithWriter(true));
				errorFileWriter.flush();
			}
			errorFileWriter.close();

			// 将文件打包
			String zipFileName = batchInfo.getReqFileName().substring(0, batchInfo.getReqFileName().lastIndexOf(".")) + ".zip";
			String zipFilePath = zipPath + zipFileName;
			ZipUtil.createZip(resultPath, zipFilePath);

			// 来源路径
			String destPath = batchInfo.getReqfile() + "result" + File.separator + zipFileName;

			// 将文件送入来源路径
			if (StringUtils.equals("0", batchInfo.getFileMode())*/
/* FTP方式 *//*
) {
				// 获取SFTP连接
				String sshHost = ResourceManager.getInstance().getValue("ftp.sshHost");
				int sshPort = ResourceManager.getInstance().getIntValue("ftp.sshPort");
				String sshUser = ResourceManager.getInstance().getValue("ftp.sshUser");
				String sshPass = ResourceManager.getInstance().getValue("ftp.sshPass");
				Sftp sftp = SftpUtil.getSftp(sshHost, sshPort, sshUser, sshPass);
				
				sftp.put(zipFilePath, destPath);
				sftp.close();
			} else*/
/* NAS方式 *//*
 {
				// 结果文件生成路径
				File srcFile = new File(zipFilePath);

				File destFile = new File(destPath);

				FileUtils.copyFile(srcFile, destFile);
			}
			// 更改批次信息,将批次号存入redis
			RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
			list.add(batchInfo.getId());

			batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_DONE);
			batchInfo.setResfile(destPath);
			batchinfoService.update(batchInfo);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("GenerationBatchResultPackTask is error batchInfo = {} , e = {}",batchInfo, e);
			batchInfo.setStatus(Constant.GENERATION_BATCH_STATUS_DISPOSE_EXC);
			batchInfo.setErrinfo("处理异常");
			batchinfoService.update(batchInfo);
			// 更改批次信息,将批次号存入redis
			RList<Object> list = redis.getList(Constant.DONE_BATCH_KEY);
			list.add(batchInfo.getId());
		}

	}

	private void init() {
		batchinfoService = (CpqBatchinfoService) WebApplicationContextUtil.getBean("cpqBatchinfoServiceImpl");
		detailService = (CpqBatchquerydetailService) WebApplicationContextUtil.getBean("cpqBatchquerydetailServiceImpl");
		resultService = (CpqResultinfoService) WebApplicationContextUtil.getBean("cpqResultinfoServiceImpl");
		resultMsgService = (CpqResultmsgServiceImpl) WebApplicationContextUtil.getBean("cpqResultmsgServiceImpl");
	}
	

}
*/
