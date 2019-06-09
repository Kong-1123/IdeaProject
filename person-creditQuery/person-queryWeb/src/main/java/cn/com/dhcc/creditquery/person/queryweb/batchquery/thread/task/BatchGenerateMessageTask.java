/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchquerydetail.CpqBatchquerydetail;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.service.WebServiceBatchQueryService;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.CpqBatchquerydetailService;
import cn.com.dhcc.query.creditpersonqueryservice.batchquerydetail.service.impl.CpqBatchquerydetailServiceImpl;
import cn.com.dhcc.query.creditquerycommon.Constant;

*/
/**
 * 生成报文文件
 * 
 * @author lekang.liu
 * @date 2018年7月24日
 *//*

public class BatchGenerateMessageTask extends Thread {

	private CpqBatchinfoService batchinfoService;

	private CpqBatchquerydetailService batchDetailService;
	
	private WebServiceBatchQueryService queryService;

	private CpqBatchinfo cpqBatchinfo;

	public BatchGenerateMessageTask(CpqBatchinfo cpqBatchinfo) {
		this.cpqBatchinfo = cpqBatchinfo;
	}

	@Override
	public void run() {
		// 转换报文文件
		init();
		String id = cpqBatchinfo.getId();
		List<CpqBatchquerydetail> batchDetailList = batchDetailService.getBatchDetailByBatchNo(id);
		//生成加密后的报文请求文件
		File messageFile = queryService.generateMessageFile(batchDetailList);
		
		if(null == messageFile || !messageFile.exists()){
			//TODO  进行文件生成失败的处理
		}
		//获取文件路径，进行保存
		String path = messageFile.getPath();
		CpqBatchinfo batchinfo = batchinfoService.findById(id);
		batchinfo.setCrcreqfile(path);
		batchinfo.setStatus(Constant.BATCH_STATUS_SEND);
		batchinfo.setUpdatetime(new Date());
		//更新批次信息并更新redis
		BatchInRedis batchInRedis = new BatchInRedis(batchinfo.getId(), Constant.BATCH_STATUS_SEND, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
		batchinfoService.update(batchinfo, batchInRedis);
		
	}
	
	public void init() {
		batchinfoService = (CpqBatchinfoService) getBean("cpqBatchinfoServiceImpl");
		batchDetailService = (CpqBatchquerydetailServiceImpl) getBean("cpqBatchquerydetailServiceImpl");
		queryService = (WebServiceBatchQueryService) getBean("webServiceBatchQueryServiceImpl");
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
}
*/
