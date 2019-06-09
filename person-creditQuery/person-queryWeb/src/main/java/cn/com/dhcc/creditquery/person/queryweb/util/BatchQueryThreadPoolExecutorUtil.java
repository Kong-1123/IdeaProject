/**
 *  Copyright (c)  @date 2018年9月12日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 *//*

package cn.com.dhcc.creditquery.person.queryweb.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.dhcc.credit.platform.util.ResourceManager;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年9月12日
 *//*

public class BatchQueryThreadPoolExecutorUtil {
	
	//一代批量文件处理线程线程池
	private static ExecutorService filePretreatmentThreadPool;
	//一代批量请求查询线程线程池
	private static ExecutorService batchQueryThreadPool;
	//一代批量结果打包线程线程池
	private static ExecutorService batchResultPackThreadPool;
	
	*/
/**
	 *	获取文件处理线程池 
	 * @return ExecutorService
	 *//*

	public static ExecutorService getFilePretreatmentExecutorService() {
		if(filePretreatmentThreadPool == null){
			synchronized (BatchQueryThreadPoolExecutorUtil.class) {
				if(filePretreatmentThreadPool == null){
					int maximumPoolSize = ResourceManager.getInstance().getIntValue("filePretreatmentPool.MaximumSize");
					filePretreatmentThreadPool = Executors.newFixedThreadPool(maximumPoolSize);
				}
			}
		}
		return filePretreatmentThreadPool;
	}
	
	*/
/**
	 *	获取批量查询线程池 
	 * @return ExecutorService
	 *//*

	public static ExecutorService getBatchQueryExecutorService() {
		if(batchQueryThreadPool == null){
			synchronized (BatchQueryThreadPoolExecutorUtil.class) {
				if(batchQueryThreadPool == null){
					int maximumPoolSize = ResourceManager.getInstance().getIntValue("batchQueryPool.MaximumSize");
					batchQueryThreadPool = Executors.newFixedThreadPool(maximumPoolSize);
				}
			}
		}
		return batchQueryThreadPool;
	}
	
	*/
/**
	 *	获取批量结果打包线程池 
	 * @return ExecutorService
	 *//*

	public static ExecutorService getBatchResultPackThreadPool() {
		if(batchResultPackThreadPool == null){
			synchronized (BatchQueryThreadPoolExecutorUtil.class) {
				if(batchResultPackThreadPool == null){
					int maximumPoolSize = ResourceManager.getInstance().getIntValue("batchResultPackPool.MaximumSize");
					batchResultPackThreadPool = Executors.newFixedThreadPool(maximumPoolSize);
				}
			}
		}
		return batchResultPackThreadPool;
	}
}
*/
