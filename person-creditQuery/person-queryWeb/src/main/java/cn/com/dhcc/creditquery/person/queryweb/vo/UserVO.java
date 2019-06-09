package cn.com.dhcc.creditquery.person.queryweb.vo;

import java.util.List;

/**
 * @author Mingyu.Li
 * @date 2018年4月12日 
 * 
 */
public class UserVO {
	
	private String userName;
	private List<UserStatisticsDataVO> dataList;

	public UserVO(String userName, List<UserStatisticsDataVO> dataList) {
		this.userName = userName;
		this.dataList = dataList;
	}

	public UserVO() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserStatisticsDataVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<UserStatisticsDataVO> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserVO other = (UserVO) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}
