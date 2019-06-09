/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread;

import java.util.ArrayList;
import java.util.List;

import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.manage.BatchManageThread;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年6月28日
 *//*

public class ManageThread extends Thread {

	*/
/**
	 * 批量管理线程数
	 *//*

	int batchManageThreadNum = 10;

	*/
/**
	 * 批量管理线程数List
	 *//*

	List<BatchManageThread> batchManageThreadList = new ArrayList<BatchManageThread>();

	public ManageThread() {
		super();
	}

	public ManageThread(ManageConfig manageConfig) {
		super();
		this.batchManageThreadNum = manageConfig.getBatchManageThreadNum();
	}

	@Override
	public void run() {
		// 初始化
		init();
		for (int i = 0; i < batchManageThreadNum; i++) {
			BatchManageThread batchManageThread = batchManageThreadList.get(i);
			batchManageThread.start();
		}
	}

	public void stoptask() {
		for (int i = 0; i < batchManageThreadNum; i++) {
			batchManageThreadList.get(i).stopTask();
		}
	}

	public void init() {
		// 初始化线程将new出的线程放入线程组
		for (int i = 0; i < batchManageThreadNum; i++) {
			BatchManageThread thread = new BatchManageThread();
			thread.setName("BatchManageThread-" + (i + 1));
			batchManageThreadList.add(thread);
		}
	}
}
*/
