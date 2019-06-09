package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.util.List;

/**
 * 用户统计结果对象
 * 
 * @author lekang.liu
 * @date 2018年8月7日
 */
public class UserResultVO {

	private List date;

	private List<UserVO> data;

	public UserResultVO() {
		super();
	}

	public UserResultVO(List date, List<UserVO> data) {
		super();
		this.date = date;
		this.data = data;
	}

	public List getDate() {
		return date;
	}

	public void setDate(List date) {
		this.date = date;
	}

	public List<UserVO> getData() {
		return data;
	}

	public void setData(List<UserVO> data) {
		this.data = data;
	}

}
