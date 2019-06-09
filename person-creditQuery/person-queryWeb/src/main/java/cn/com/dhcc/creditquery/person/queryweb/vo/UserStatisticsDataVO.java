package cn.com.dhcc.creditquery.person.queryweb.vo;

/**
 * @author Mingyu.Li
 * @date 2018年4月12日
 *
 */
public class UserStatisticsDataVO {
	private String queryReason;
	
	private long[] count;
	
	public UserStatisticsDataVO() {
		super();
	}

	public UserStatisticsDataVO(String queryReason, long[] count) {
		super();
		this.queryReason = queryReason;
		this.count = count;
	}

	public String getQueryReason() {
		return queryReason;
	}

	public void setQueryReason(String queryReason) {
		this.queryReason = queryReason;
	}

	public long[] getCount() {
		return count;
	}

	public void setCount(long[] count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queryReason == null) ? 0 : queryReason.hashCode());
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
		UserStatisticsDataVO other = (UserStatisticsDataVO) obj;
		if (queryReason == null) {
			if (other.queryReason != null)
				return false;
		} else if (!queryReason.equals(other.queryReason))
			return false;
		return true;
	}
	
}
