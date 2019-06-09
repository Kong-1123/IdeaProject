/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.dhcc.creditquery.person.queryweb.util.ZipUtil;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年7月24日
 *//*

public class BatchResultFilePackTask extends Thread {
	private CpqBatchinfo batchInfo;
	
	private CpqBatchinfoService batchinfoService;

	public BatchResultFilePackTask(CpqBatchinfo batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Override
	public void run() {
		// 结果文件打包
		try {
			// 错误信息文件
			String resfileerr = batchInfo.getResfileerr();
			File resfileerrFile = new File(resfileerr);
			// 人行结果信息文件
			String resfilesucc = batchInfo.getResfilesucc();
			File resfilesuccFile = new File(resfilesucc);
			
			List<File> zipFileList = new ArrayList<File>();
			zipFileList.add(resfileerrFile);
			zipFileList.add(resfilesuccFile);
			// 获取结果文件路径
			String path = ConfigUtil.getBatchQueryFilePath() + File.separator + "Batch_Result_File" + batchInfo.getId();
			String zipFileName = batchInfo.getResfilesucc();
			File file = new File(zipFileName);
			String name2 = file.getName();
			String[] split = name2.split("\\.");
			zipFileName = split[0] + ".zip";
			//生成文件
			File zipFile = ZipUtil.getZipFile(zipFileList, path, zipFileName);
			String resfilePath = zipFile.getPath();
			
			batchInfo.setResfile(resfilePath);
			batchInfo.setStatus(Constant.BATCH_STATUS_DISPOSE_DONE);
			batchInfo.setUpdatetime(new Date());
			
			BatchInRedis batchInRedis = new BatchInRedis(batchInfo.getId(), Constant.BATCH_STATUS_DISPOSE_DONE, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
			batchinfoService.update(batchInfo, batchInRedis);
			
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		}


	}
}
*/
