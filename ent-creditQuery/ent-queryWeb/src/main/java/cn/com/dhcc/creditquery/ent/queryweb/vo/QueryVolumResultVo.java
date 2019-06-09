package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 查询量排行榜resultVO
 * @author drj
 *
 */
public class QueryVolumResultVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2433731911209517699L;
	private List<String> orgCode;
	private List<Integer> series;
	private Integer value;
	private List<String> data;
	
	public QueryVolumResultVo(List<String> data) {
		super();
		this.data = data;
	}
	public QueryVolumResultVo(List<String> orgCode, List<Integer> series, Integer value, List<String> data) {
		super();
		this.orgCode = orgCode;
		this.series = series;
		this.value = value;
		this.data = data;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	public QueryVolumResultVo() {
		super();
	}
	public QueryVolumResultVo(List<String> orgCode, List<Integer> series) {
		super();
		this.orgCode = orgCode;
		this.series = series;
	}
	public QueryVolumResultVo(List<String> orgCode, List<Integer> series, Integer value) {
		super();
		this.orgCode = orgCode;
		this.series = series;
		this.value = value;
	}
	public QueryVolumResultVo( Integer value) {
		super();
		this.value = value;
	}
	public List<String> getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(List<String> orgCode) {
		this.orgCode = orgCode;
	}
	public List<Integer> getSeries() {
		return series;
	}
	public void setSeries(List<Integer> series) {
		this.series = series;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
