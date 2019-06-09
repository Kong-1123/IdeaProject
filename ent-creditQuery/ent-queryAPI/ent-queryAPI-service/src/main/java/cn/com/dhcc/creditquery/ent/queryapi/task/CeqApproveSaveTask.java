package cn.com.dhcc.creditquery.ent.queryapi.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.ent.queryapproveflow.entity.CeqApprove;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
/**
 * 异步保存审核信息线程类
 * @author yuzhao.xue
 * @date 2019年2月14日
 */
public class CeqApproveSaveTask implements Runnable{

	private static Logger log = LoggerFactory.getLogger(CeqApproveSaveTask.class);

	private CeqApprove ceqApprove;
	

	/**
	 * @return the cpqApprove
	 */
	public CeqApprove getCeqApprove() {
		return ceqApprove;
	}


	/**
	 * @param cpqApprove the cpqApprove to set
	 */
	public void setCeqApprove(CeqApprove ceqApprove) {
		this.ceqApprove = ceqApprove;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			CeqApproveBo ceqApproveBo = ClassCloneUtil.copyObject(ceqApprove, CeqApproveBo.class);
			InitBeanUtil.getCeqApproveFlowService().create(ceqApproveBo);
		} catch (Exception e) {
			log.error("saveCheckInfo异常：{}",e);
		}
	}

}
