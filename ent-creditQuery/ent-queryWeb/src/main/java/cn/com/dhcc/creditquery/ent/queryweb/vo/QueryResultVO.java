package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Mingyu.Li
 * @date 2018年3月28日
 * 
 */
public class QueryResultVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2824856850823947918L;

	private List<StatisticsVO> data;

	private String startDate;

	private String endDate;

	private Set<String> dateSet;

	public QueryResultVO() {
	}

	public QueryResultVO(List<StatisticsVO> data, String startDate, String endDate, Set<String> dateSet) {
		super();
		this.data = data;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateSet = dateSet;
	}

	public List<StatisticsVO> getData() {
		return data;
	}

	public void setData(List<StatisticsVO> data) {
		this.data = data;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Set<String> getDateSet() {
		return dateSet;
	}

	public void setDateSet(Set<String> dateSet) {
		this.dateSet = dateSet;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);

	}

}
