package cn.com.dhcc.creditquery.ent.queryweb.vo;

/**
 * @author Mingyu.Li
 * @date 2018年4月12日
 *
 */
public class StatisticsDataVO {
	private Integer count;
	private String date;

	public StatisticsDataVO(Integer count, String date) {
		super();
		this.count = count;
		this.date = date;
	}

	public StatisticsDataVO() {
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
