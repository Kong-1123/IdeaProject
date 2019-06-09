/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.io.File;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.base.Objects;

import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.BatchInRedis;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonquerydao.entity.ccuser.CpqCcUser;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.bean.dto.SendBatchQueryDto;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.bean.vo.BatchResultMsgBean;
import cn.com.dhcc.query.creditpersonqueryinterface.webservice.batchquery.service.WebServiceBatchQueryService;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.CpqBatchinfoService;
import cn.com.dhcc.query.creditpersonqueryservice.ccuser.service.CpqCcUserService;
import cn.com.dhcc.query.creditpersonqueryservice.ccusercorrelation.service.CpqCcUserCorrelationService;
import cn.com.dhcc.query.creditquerycommon.Constant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;

*/
/**
 * 发送批量查询请求
 * @author lekang.liu
 * @date 2018年6月29日
 *//*

@Component
public class BatchSendQueryTask extends Thread {

	private CpqBatchinfo batchInfo;
	
	private WebServiceBatchQueryService queryService; //二代webservice查询
	
	private CpqCcUserCorrelationService ccUserCorrelationService;
	
	private CpqCcUserService ccUserService;
	
	private CpqBatchinfoService batchinfoService;
	
	public BatchSendQueryTask() {
		super();
	}

	public BatchSendQueryTask(CpqBatchinfo batchInfo) {
		this.batchInfo = batchInfo;
	}

	@Override
	public void run() {
		//  向人行发送请求报文信息
		String operator = batchInfo.getOperator();
		// 获取征信用户
		String creditUserName = ccUserCorrelationService.findCCUserNameByUserName(operator);
		CpqCcUser creditUser = ccUserService.findByCreditUser(creditUserName);
		String creditUserPasswd = creditUser.getPasswd();
		//人行请求文件路径
		String crcreqfile = batchInfo.getCrcreqfile();
		//TODO  信用报告结果文件存储路径
		String reortPath = ConfigUtil.getBatchQueryFilePath()+File.separator+batchInfo.getId();
		
		SendBatchQueryDto batchQueryDto = new SendBatchQueryDto(creditUser.getCcdept(),creditUserName,creditUserPasswd,reortPath,reortPath);
		
		BatchResultMsgBean sendBatchQueryMessage = queryService.sendBatchQueryMessage(batchQueryDto);
		
		if(Objects.equal(sendBatchQueryMessage.getCode(), "00000000")){
			//处理成功，更新批次信息与redis
			CpqBatchinfo batch = batchinfoService.findById(batchInfo.getId());
			batch.setStatus(Constant.BATCH_STATUS_GET_QUERYSTATUS);
			batch.setUpdatetime(new Date());
			BatchInRedis batchInRedis = new BatchInRedis(batch.getId(), Constant.BATCH_STATUS_GET_QUERYSTATUS, Constant.BATCH_ROWSTATUS_UNTREATED, new Date());
			batchinfoService.update(batch, batchInRedis);
			return;
		}
		//TODO  对发送未成功的情况进行处理
		

	}
	
	
	public void init() {
		queryService = (WebServiceBatchQueryService) getBean("webServiceBatchQueryServiceImpl");
		ccUserCorrelationService = (CpqCcUserCorrelationService) getBean("ccUserCorrelationServiceImpl");
		ccUserService = (CpqCcUserService) getBean("cpqCcUserServiceImpl");
		batchinfoService = (CpqBatchinfoService) getBean("cpqBatchinfoServiceImpl");
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
