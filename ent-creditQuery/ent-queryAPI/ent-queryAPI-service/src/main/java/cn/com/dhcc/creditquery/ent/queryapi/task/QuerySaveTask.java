package cn.com.dhcc.creditquery.ent.queryapi.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleQuery;
import cn.com.dhcc.creditquery.ent.queryapi.util.InitBeanUtil;
import cn.com.dhcc.creditquery.ent.querybo.queryapi.SingleQueryBo;
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
			CeqSingleQuery ceqSingleQuery=new CeqSingleQuery();
			ClassCloneUtil.copyObject(singleQueryBo,ceqSingleQuery);
			switch (singleQueryBo.getQuerierCertype()) {
			case "10"://中征码
				ceqSingleQuery.setSignCode(singleQueryBo.getQuerierCertno());
				break;
			case "20"://统一社会信用代码
				ceqSingleQuery.setUniformSocialCredCode(singleQueryBo.getQuerierCertno());
				break;
			case "30"://组织机构代码
				ceqSingleQuery.setOrgInstCode(singleQueryBo.getQuerierCertno());
				break;
			case "41"://纳税人识别号（国税）
				ceqSingleQuery.setGsRegiNo(singleQueryBo.getQuerierCertno());
				break;
			case "42"://纳税人识别号（地税）
				ceqSingleQuery.setDsRegiNo(singleQueryBo.getQuerierCertno());
				break;
			default:
				break;
			}
			ceqSingleQuery.setEnterpriseName(singleQueryBo.getQuerierName());
			ceqSingleQuery.setStatus(AUDIT_NO);
			InitBeanUtil.saveSingleQuery(ceqSingleQuery);
		} catch (Exception e) {
			log.error("querySave异常：{}",e);
		}
	}

}
