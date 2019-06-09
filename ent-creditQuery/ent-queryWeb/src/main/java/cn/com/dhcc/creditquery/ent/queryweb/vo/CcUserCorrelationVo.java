/*
package cn.com.dhcc.creditquery.person.queryweb.vo;

import java.util.List;

public class CcUserCorrelationVo {
	
	private List<User> users;
	private CcUserVo creditUser;
	
	public CcUserCorrelationVo(){
	}
	
	
	public CcUserCorrelationVo(List<User> users, CcUserVo creditUser) {
		super();
		this.users = users;
		this.creditUser = creditUser;
	}

	

	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public CcUserVo getCreditUser() {
		return creditUser;
	}


	public void setCreditUser(CcUserVo creditUser) {
		this.creditUser = creditUser;
	}

//
//	public CcUserCorrelationVoResultBean getCorrelation(String initStatus,String createUserId,CpqCcUserCorrelationService service){
//		List<CpqUsermap> correlation = new ArrayList<>();
//		boolean repeatFlag = false;
//		CpqUsermap map = null;
//		Date now = new Date();
//		StringBuilder sb = new StringBuilder();
//		for (User user : users) {
//			if(service.isRepeat(user.getId())){
//				repeatFlag = true;
//				sb.append(user.getId() + "已被分配征信用户!").append("<br/>");
//			}
//			if(repeatFlag == false){
//				map = new CpqUsermap();
//				map.setCredituser(creditUser.getCcuser());
//				map.setCreditdeptcode(creditUser.getCcdept());
//				map.setUserName(user.getId());
//				map.setDeptcode(user.getOrgId());
//				map.setStatus(initStatus);
//				map.setCreateUser(createUserId);
//				map.setCreateDate(now);
//				map.setUpdateDate(now);
//				correlation.add(map);
//			}
//		}
//		return new CcUserCorrelationVoResultBean(repeatFlag, correlation ,sb.toString());
//	}

	

}

*/
