/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年6月28日
 *//*

@Component
public class ThreadStart extends HttpServlet{

	*/
/**
	* @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	*//*

	private static final long serialVersionUID = 8433084734211891571L;

	@Autowired
	@Qualifier("batchTaskExecutor")
	private ThreadPoolTaskExecutor batchTaskExecutor;

	private ManageConfig manageConfig;

	private ManageThread managerThread;
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		startThread();
		
		super.init(config);
	}
	
	@Override
	public void destroy() {
		stopThread();
		super.destroy();
	}
	
	
//	 @PostConstruct
	public void startThread() {
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		manageConfig = (ManageConfig) applicationContext.getBean("manageConfig");
		managerThread = new ManageThread(manageConfig);
		managerThread.start();
	}

//	 @PreDestroy
	public void stopThread() {
		// 停止任务
		managerThread.stoptask();
		// 停止线程
		managerThread.interrupt();
		// 停止线程池
		batchTaskExecutor.shutdown();
	}

//	@Override
//	public void afterPropertiesSet() throws Exception {
//		startThread();
//	}
	

}
*/
