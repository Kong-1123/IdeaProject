package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.util.Date;

/**
 * 
 * @author Gao liwei
 * @date 2018年8月9日
 */
public class QueryRelevantVo {
	private Date search_GTE_startDate_DATE;
	private Date search_GTE_alertDate_DATE;

	private String search_IN_deptCode;

	public QueryRelevantVo() {
	}

	public QueryRelevantVo(Date search_GTE_startDate_DATE, Date search_GTE_alertDate_DATE, String search_IN_deptCode) {
		super();
		this.search_GTE_startDate_DATE = search_GTE_startDate_DATE;
		this.search_GTE_alertDate_DATE = search_GTE_alertDate_DATE;
		this.search_IN_deptCode = search_IN_deptCode;
	}

	public Date getSearch_GTE_startDate_DATE() {
		return search_GTE_startDate_DATE;
	}

	public void setSearch_GTE_startDate_DATE(Date search_GTE_startDate_DATE) {
		this.search_GTE_startDate_DATE = search_GTE_startDate_DATE;
	}

	public Date getSearch_GTE_alertDate_DATE() {
		return search_GTE_alertDate_DATE;
	}

	public void setSearch_GTE_alertDate_DATE(Date search_GTE_alertDate_DATE) {
		this.search_GTE_alertDate_DATE = search_GTE_alertDate_DATE;
	}

	public String getSearch_IN_deptCode() {
		return search_IN_deptCode;
	}

	public void setSearch_IN_deptCode(String search_IN_deptCode) {
		this.search_IN_deptCode = search_IN_deptCode;
	}

}
