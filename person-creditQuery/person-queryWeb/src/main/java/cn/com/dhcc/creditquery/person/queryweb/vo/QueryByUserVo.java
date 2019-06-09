package cn.com.dhcc.creditquery.person.queryweb.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户统计VO
 * 
 * @author lekang.liu
 * @date 2018年8月7日
 */
public class QueryByUserVo implements Serializable{
	/**
	*/
	private static final long serialVersionUID = -206430248664344637L;
	/**
	 * 起始日期
	 */
	private String search_LTE_updateTime_DATE;
	/**
	 * 结束日期
	 */
	private String search_GTE_updateTime_DATE;

	/**
	 * 用户类型
	 */
	private String search_EQ_userType;

	/**
	 * 批量标识
	 */
	private String search_EQ_batchFlag;

	/**
	 * 报告来源
	 */
	private String search_EQ_source;

	/**
	 * 是否存在档案
	 */
	private String search_EQ_autharchiveId;
	/**
	 * 查询结果
	 */
	private String search_EQ_status;

	/**
	 * 制式报表
	 */
	private String search_EQ_formReport;

	/**
	 * 查询类型
	 */
	private String search_EQ_queryType;

	/**
	 * 所属机构
	 */
	private String search_EQ_operOrg;

	public QueryByUserVo() {
		super();
	}

	public QueryByUserVo(String search_LTE_updateTime_DATE, String search_GTE_updateTime_DATE, String search_EQ_userType, String search_EQ_batchFlag, String search_EQ_source,
	        String search_EQ_autharchiveId, String search_EQ_status, String search_EQ_formReport, String search_EQ_queryType, String search_EQ_operOrg) {
		super();
		this.search_LTE_updateTime_DATE = search_LTE_updateTime_DATE;
		this.search_GTE_updateTime_DATE = search_GTE_updateTime_DATE;
		this.search_EQ_userType = search_EQ_userType;
		this.search_EQ_batchFlag = search_EQ_batchFlag;
		this.search_EQ_source = search_EQ_source;
		this.search_EQ_autharchiveId = search_EQ_autharchiveId;
		this.search_EQ_status = search_EQ_status;
		this.search_EQ_formReport = search_EQ_formReport;
		this.search_EQ_queryType = search_EQ_queryType;
		this.search_EQ_operOrg = search_EQ_operOrg;
	}

	public String getSearch_LTE_updateTime_DATE() {
		return search_LTE_updateTime_DATE;
	}

	public void setSearch_LTE_updateTime_DATE(String search_LTE_updateTime_DATE) {
		this.search_LTE_updateTime_DATE = search_LTE_updateTime_DATE;
	}

	public String getSearch_GTE_updateTime_DATE() {
		return search_GTE_updateTime_DATE;
	}

	public void setSearch_GTE_updateTime_DATE(String search_GTE_updateTime_DATE) {
		this.search_GTE_updateTime_DATE = search_GTE_updateTime_DATE;
	}

	public String getSearch_EQ_userType() {
		return search_EQ_userType;
	}

	public void setSearch_EQ_userType(String search_EQ_userType) {
		this.search_EQ_userType = search_EQ_userType;
	}

	public String getSearch_EQ_batchFlag() {
		return search_EQ_batchFlag;
	}

	public void setSearch_EQ_batchFlag(String search_EQ_batchFlag) {
		this.search_EQ_batchFlag = search_EQ_batchFlag;
	}

	public String getSearch_EQ_source() {
		return search_EQ_source;
	}

	public void setSearch_EQ_source(String search_EQ_source) {
		this.search_EQ_source = search_EQ_source;
	}

	public String getSearch_EQ_autharchiveId() {
		return search_EQ_autharchiveId;
	}

	public void setSearch_EQ_autharchiveId(String search_EQ_autharchiveId) {
		this.search_EQ_autharchiveId = search_EQ_autharchiveId;
	}

	public String getSearch_EQ_status() {
		return search_EQ_status;
	}

	public void setSearch_EQ_status(String search_EQ_status) {
		this.search_EQ_status = search_EQ_status;
	}

	public String getSearch_EQ_formReport() {
		return search_EQ_formReport;
	}

	public void setSearch_EQ_formReport(String search_EQ_formReport) {
		this.search_EQ_formReport = search_EQ_formReport;
	}

	public String getSearch_EQ_queryType() {
		return search_EQ_queryType;
	}

	public void setSearch_EQ_queryType(String search_EQ_queryType) {
		this.search_EQ_queryType = search_EQ_queryType;
	}

	public String getSearch_EQ_operOrg() {
		return search_EQ_operOrg;
	}

	public void setSearch_EQ_operOrg(String search_EQ_operOrg) {
		this.search_EQ_operOrg = search_EQ_operOrg;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
