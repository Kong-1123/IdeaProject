/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.File;
import java.util.Date;

import com.google.common.base.Objects;

import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.bean.vo.BatchResultMsgBean;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.service.WebServiceBatchQueryService;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditquerycommon.Constant;

*/
/**
 * 解析报文
 * @author lekang.liu
 * @date 2018年7月24日
 *//*

public class BatchMessageParsingTask extends Thread {
	private CpqBatchinfo batchInfo;
	
	private WebServiceBatchQueryService queryService; //二代webservice查询
	
	private CpqBatchinfoService batchinfoService;

	public BatchMessageParsingTask(CpqBatchinfo batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Override
	public void run() {
		//TODO  进行结果文件解析,并保存查询记录
		String crcresfile = batchInfo.getCrcresfile();
		File file = new File(crcresfile);
		if(file.exists()){
			//进行文件解析，并保存查询记录
			BatchResultMsgBean parsingMessage = queryService.parsingMessage(batchInfo);
			if(null != parsingMessage && Objects.equal(parsingMessage.getCode(), "00000000")){
				CpqBatchinfo batch = batchinfoService.findById(batchInfo.getId());
				batch.setStatus(Constant.BATCH_STATUS_RESULT_PACK);
				batch.setUpdatetime(new Date());
				BatchInRedis batchInRedis = new BatchInRedis(batch.getId(), Constant.BATCH_STATUS_GET_QUERYSTATUS, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
				batchinfoService.update(batch, batchInRedis);
			}
		}
		
		
		
	}
}
*/
