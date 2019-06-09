package cn.com.dhcc.creditquery.ent.queryweb.operatelog.aop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.dhcc.credit.platform.util.ResourceManager;
/**
 * 日志管理 线程池
 * 
 */
public class ThreadPoolExecutorUtil {
	// 池中允许的最大线程数
	private static ExecutorService threadPool;
	
	public static ExecutorService getExecutorService() {
		if(threadPool == null){
			synchronized (ThreadPoolExecutorUtil.class) {
				if(threadPool == null){
					int maximumPoolSize = ResourceManager.getInstance().getIntValue("maximumPoolSize");
					threadPool = Executors.newFixedThreadPool(maximumPoolSize);
				}
			}
		}
		return threadPool;
	}
}
