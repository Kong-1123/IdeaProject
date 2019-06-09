package cn.com.dhcc.creditquery.person.queryapi.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.CommonUtil;
import cn.com.dhcc.creditquery.person.queryapi.entity.CpqSingleQuery;
import cn.com.dhcc.creditquery.person.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.person.querybo.queryapi.SingleQueryBo;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;

public class QuerySaveTask implements Runnable{

	/**
	 * 处理状态未完成
	 */
	private static final String AUDIT_NO = "1";
	private static Logger log = LoggerFactory.getLogger(QuerySaveTask.class);
	private SingleQueryBo singleQueryBo;
	

	/**
	 * @return the singleQueryBo
	 */
	public SingleQueryBo getSingleQueryBo() {
		return singleQueryBo;
	}


	/**
	 * @param singleQueryBo the singleQueryBo to set
	 */
	public void setSingleQueryBo(SingleQueryBo singleQueryBo) {
		this.singleQueryBo = singleQueryBo;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			CpqSingleQuery cpqSingleQuery=new CpqSingleQuery();
			ClassCloneUtil.copyObject(singleQueryBo,cpqSingleQuery);
			cpqSingleQuery.setStatus(AUDIT_NO);
			InitBeanUtil.saveSingleQuery(cpqSingleQuery);
		} catch (Exception e) {
			log.error("querySave异常：{}",e);
		}
	}

}
