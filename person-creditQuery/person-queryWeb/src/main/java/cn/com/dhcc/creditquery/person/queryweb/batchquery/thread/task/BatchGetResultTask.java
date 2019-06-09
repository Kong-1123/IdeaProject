/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.File;
import java.util.Date;

import com.google.common.base.Objects;

import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.bean.dto.GetBatchQueryResultDto;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.bean.vo.BatchResultMsgBean;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.service.WebServiceBatchQueryService;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.ccuser.service.CpqCcUserService;
import cn.com.dhcc.query.creditpersonqueryservice.ccusercorrelation.service.CpqCcUserCorrelationService;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;

*/
/**
 * 获取结果线程
 * @author lekang.liu
 * @date 2018年7月24日
 *//*

public class BatchGetResultTask extends Thread{

	private CpqBatchinfo cpqBatchinfo;

	private WebServiceBatchQueryService queryService; // 二代webservice查询

	private CpqCcUserCorrelationService ccUserCorrelationService;

	private CpqCcUserService ccUserService;
	
	private CpqBatchinfoService batchinfoService;
	
	public BatchGetResultTask(CpqBatchinfo cpqBatchinfo) {

		this.cpqBatchinfo = cpqBatchinfo;
	}

	@Override
	public void run() {

		String operator = cpqBatchinfo.getOperator();
		// 获取征信用户
		String creditUserName = ccUserCorrelationService.findCCUserNameByUserName(operator);
		CpqCcUser creditUser = ccUserService.findByCreditUser(creditUserName);
		String creditUserPasswd = creditUser.getPasswd();
		// 人行请求文件
		String crcreqfile = cpqBatchinfo.getCrcreqfile();
		String batchQueryFileName = "";
		File file = new File(crcreqfile);
		if(file.exists()){
			batchQueryFileName = file.getName();
		}
		GetBatchQueryResultDto getBatchQueryResultDto = new GetBatchQueryResultDto(creditUser.getCcdept(), creditUserName, creditUserPasswd, batchQueryFileName);
		BatchResultMsgBean getBatchQueryResult = queryService.getBatchQueryResult(getBatchQueryResultDto);
		if(Objects.equal(getBatchQueryResult.getCode(), "00000000")){
			//已获取人行文件，更新redis
			CpqBatchinfo batch = batchinfoService.findById(cpqBatchinfo.getId());
			String reortPath = ConfigUtil.getBatchQueryFilePath()+File.separator+cpqBatchinfo.getId();
			batch.setCrcresfile(reortPath);
			batch.setStatus(Constant.BATCH_STATUS_MASSAGE_PARSING);
			batch.setUpdatetime(new Date());
			BatchInRedis batchInRedis = new BatchInRedis(batch.getId(), Constant.BATCH_STATUS_MASSAGE_PARSING, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
			batchinfoService.update(batch, batchInRedis);
			return;
		}
		//TODO  处理失败的情况进行处理
		
		
	}
}
*/
