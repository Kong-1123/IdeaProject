package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.util.List;

/**
 * @author Mingyu.Li
 * @date 2018年4月12日
 *
 */
public class StatisticsVO {
	private String deptCode;
	private List<StatisticsDataVO> dataList;

	public StatisticsVO(String deptCode, List<StatisticsDataVO> dataList) {
		this.deptCode = deptCode;
		this.dataList = dataList;
	}

	public StatisticsVO() {
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public List<StatisticsDataVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<StatisticsDataVO> dataList) {
		this.dataList = dataList;
	}

}
