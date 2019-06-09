package cn.com.dhcc.creditquery.person.queryapi.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.person.queryapproveflow.entity.CpqApprove;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
/**
 * 异步保存审核信息线程类
 * @author yuzhao.xue
 * @date 2019年2月14日
 */
public class CpqApproveSaveTask implements Runnable{

	private static Logger log = LoggerFactory.getLogger(CpqApproveSaveTask.class);

	private CpqApprove cpqApprove;
	

	/**
	 * @return the cpqApprove
	 */
	public CpqApprove getCpqApprove() {
		return cpqApprove;
	}


	/**
	 * @param cpqApprove the cpqApprove to set
	 */
	public void setCpqApprove(CpqApprove cpqApprove) {
		this.cpqApprove = cpqApprove;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			CpqApproveBo cpqApproveBo = ClassCloneUtil.copyObject(cpqApprove, CpqApproveBo.class);
			InitBeanUtil.getCpqApproveFlowService().create(cpqApproveBo);
		} catch (Exception e) {
			log.error("saveCheckInfo异常：{}",e);
		}
	}

}
