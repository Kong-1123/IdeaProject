package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 近24小时近1周返回vo
 * @author drj
 *
 */
public class OneDayQueryVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4018114518073706328L;
	private List<String> date;
	private List<Map<String,List<Integer>>> series;
	
	
	public OneDayQueryVo() {
		super();
	}
	public OneDayQueryVo(List<String> date, List<Map<String,List<Integer>>> series) {
		super();
		this.date = date;
		this.series = series;
	}
	public List<String> getDate() {
		return date;
	}
	public void setDate(List<String> date) {
		this.date = date;
	}
	public List<Map<String,List<Integer>>> getSeries() {
		return series;
	}
	public void setSeries(List<Map<String,List<Integer>>> series) {
		this.series = series;
	}
	
	
	
}
