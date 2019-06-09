/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.manage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.base.Objects;

import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchFilePretreatmentTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchGenerateMessageTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchGetQueryStatusTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchGetResultTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchMessageParsingTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchResultFilePackTask;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.BatchSendQueryTask;
import cn.com.dhcc.query.creditpersonquerydao.entity.batchinfo.CpqBatchinfo;
import cn.com.dhcc.query.creditpersonqueryservice.batchinfo.service.impl.CpqBatchinfoServiceImpl;
import cn.com.dhcc.query.creditquerycommon.Constant;

*/
/**
 * 任务分配线程
 * 
 * @author lekang.liu
 * @date 2018年6月29日
 *//*

public class BatchManageThread extends Thread {
	private static Logger log = LoggerFactory.getLogger(BatchManageThread.class);

	private boolean runFlag = true;

	@Override
	public void run() {
		log.info("BatchManageThread is runing");
		*/
/*
		 * 任务分发状态 
		 * 0：待处理                     进行批量文件预处理 
		 * 1：待生成报文              进行人行批量查询报文生成
		 * 2：待发送查询请求      发送批量查询请求
		 * 3：已发送查询请求      向人行查询处理状态 
		 * 4：人行处理完毕          向人行获取查询结果 
		 * 5：查询完毕                  进行结果文件解析 
		 * 6：解析完毕                  进行结果文件打包 
		 * 7：处理完毕
		 * 
		 *//*


		while (runFlag) {
			try {
				// 查询出所有未被处理的记录
				CpqBatchinfoServiceImpl batchinfoService = (CpqBatchinfoServiceImpl) getBean("cpqBatchinfoServiceImpl");
				ThreadPoolTaskExecutor batchTaskExecutor = (ThreadPoolTaskExecutor) getBean("batchTaskExecutor");
				List<CpqBatchinfo> batchinfo = batchinfoService.getBatchinfo();
				log.info("BatchManageThread get  task = {}", batchinfo);
				// 根据状态进行分类，交给对应的线程进行处理
				for (CpqBatchinfo cpqBatchinfo : batchinfo) {
					String status = cpqBatchinfo.getStatus();
					
					if (Objects.equal(status, Constant.BATCH_STATUS_PENDING)*/
/* 进行文件预处理操作 *//*
) {
						BatchFilePretreatmentTask pretreatmentTask = new BatchFilePretreatmentTask(cpqBatchinfo);
						batchTaskExecutor.execute(pretreatmentTask);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_GENERATE)*/
/* 生成报文 *//*
) {
						BatchGenerateMessageTask task = new BatchGenerateMessageTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_SEND)*/
/* 发送批量查询请求报文 *//*
) {
						BatchSendQueryTask task = new BatchSendQueryTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_GET_QUERYSTATUS)*/
/* 向人行查询处理状态 *//*
) {
						BatchGetQueryStatusTask task = new BatchGetQueryStatusTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_GET_RESULT)*/
/* 向人行获取查询结果 *//*
) {
						BatchGetResultTask task = new BatchGetResultTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_MASSAGE_PARSING)*/
/* 进行结果文件解析 *//*
) {
						BatchMessageParsingTask task = new BatchMessageParsingTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					} else if (Objects.equal(status, Constant.BATCH_STATUS_RESULT_PACK)*/
/* 进行结果文件打包 *//*
) {
						BatchResultFilePackTask task = new BatchResultFilePackTask(cpqBatchinfo);
						batchTaskExecutor.execute(task);
					}
					
				}

			} catch (Exception e) {
				log.error("BatchManageThread error ", e);
			}

		}
	}

	public void stopTask() {
		runFlag = false;
		log.info("BatchManageThread whil stop.");
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
