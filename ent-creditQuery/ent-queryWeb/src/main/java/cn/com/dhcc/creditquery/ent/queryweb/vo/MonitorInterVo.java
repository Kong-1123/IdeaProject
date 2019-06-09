package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.util.List;
import java.util.Map;

public class MonitorInterVo {

	/**
	 * 参数获取
	 */
	private Map<String,String> param;
	/**
	 * 实时查询
	 */
	private QueryVolumResultVo realTimeQuery;
	/**
	 * 当天查询总量
	 */
	private QueryVolumResultVo allLineOfDay;
	/**
	 * 全行当天查询状态
	 */
	private Map<Integer,Integer> queryState;
	/**
	 * 近24小时  oneDayQuery
	 */
	private OneDayQueryVo oneDayQuery;
	
	/**
	 * 近一周  weekQuery
	 */
	private OneDayQueryVo weekQuery;
	/**
	 * 机构查询量排行榜 前10
	 */
	private QueryVolumResultVo orgQueryNumRank;
	/**
	 * 征信用户查询量排行榜 creditUserQueryNumRank 取前十
	 */
	private QueryVolumResultVo creditUserQueryNumRank;
	/**
	 * 内部用户查询量排行榜 前10
	 */
	private QueryVolumResultVo innerUserQueryNumRank;
	/**
	 * 征信用户查询异常
	 */
	private QueryVolumResultVo creditExc;
	/**
	 * 内部用户查询异常
	 */
	private QueryVolumResultVo innerExc;
	
	
	/**
	 *  **** *** getter and setter  **** ****
	 */
	public MonitorInterVo() {
		super();
	}

	public MonitorInterVo(Map<String, String> param, QueryVolumResultVo realTimeQuery, QueryVolumResultVo allLineOfDay,
			Map<Integer, Integer> queryState, OneDayQueryVo oneDayQuery, OneDayQueryVo weekQuery,
			QueryVolumResultVo orgQueryNumRank, QueryVolumResultVo creditUserQueryNumRank,
			QueryVolumResultVo innerUserQueryNumRank, QueryVolumResultVo creditExc, QueryVolumResultVo innerExc) {
		super();
		this.param = param;
		this.realTimeQuery = realTimeQuery;
		this.allLineOfDay = allLineOfDay;
		this.queryState = queryState;
		this.oneDayQuery = oneDayQuery;
		this.weekQuery = weekQuery;
		this.orgQueryNumRank = orgQueryNumRank;
		this.creditUserQueryNumRank = creditUserQueryNumRank;
		this.innerUserQueryNumRank = innerUserQueryNumRank;
		this.creditExc = creditExc;
		this.innerExc = innerExc;
	}

	public OneDayQueryVo getOneDayQuery() {
		return oneDayQuery;
	}

	public void setOneDayQuery(OneDayQueryVo oneDayQuery) {
		this.oneDayQuery = oneDayQuery;
	}

	public OneDayQueryVo getWeekQuery() {
		return weekQuery;
	}

	public void setWeekQuery(OneDayQueryVo weekQuery) {
		this.weekQuery = weekQuery;
	}

	public Map<Integer, Integer> getQueryState() {
		return queryState;
	}

	public QueryVolumResultVo getRealTimeQuery() {
		return realTimeQuery;
	}

	public void setRealTimeQuery(QueryVolumResultVo realTimeQuery) {
		this.realTimeQuery = realTimeQuery;
	}

	public QueryVolumResultVo getAllLineOfDay() {
		return allLineOfDay;
	}

	public void setAllLineOfDay(QueryVolumResultVo allLineOfDay) {
		this.allLineOfDay = allLineOfDay;
	}

	public QueryVolumResultVo getOrgQueryNumRank() {
		return orgQueryNumRank;
	}

	public void setOrgQueryNumRank(QueryVolumResultVo orgQueryNumRank) {
		this.orgQueryNumRank = orgQueryNumRank;
	}

	public QueryVolumResultVo getCreditUserQueryNumRank() {
		return creditUserQueryNumRank;
	}

	public void setCreditUserQueryNumRank(QueryVolumResultVo creditUserQueryNumRank) {
		this.creditUserQueryNumRank = creditUserQueryNumRank;
	}

	public QueryVolumResultVo getInnerUserQueryNumRank() {
		return innerUserQueryNumRank;
	}

	public void setInnerUserQueryNumRank(QueryVolumResultVo innerUserQueryNumRank) {
		this.innerUserQueryNumRank = innerUserQueryNumRank;
	}

	public QueryVolumResultVo getInnerExc() {
		return innerExc;
	}

	public void setInnerExc(QueryVolumResultVo innerExc) {
		this.innerExc = innerExc;
	}

	public QueryVolumResultVo getCreditExc() {
		return creditExc;
	}

	public void setCreditExc(QueryVolumResultVo creditExc) {
		this.creditExc = creditExc;
	}

	public void setQueryState(Map<Integer, Integer> queryState) {
		this.queryState = queryState;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}
	
}
