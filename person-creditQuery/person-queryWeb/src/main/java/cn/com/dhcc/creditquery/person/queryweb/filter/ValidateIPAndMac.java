package cn.com.dhcc.creditquery.person.queryweb.filter;
/*package cn.com.dhcc.query.creditpersonqueryweb.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;

import cn.com.dhcc.credit.platform.util.NetworkUtil;
import cn.com.dhcc.query.creditpersonquerydao.entity.ipaddr.CpqIpaddr;
import cn.com.dhcc.query.creditpersonquerydao.entity.macaddr.CpqMacaddr;
import cn.com.dhcc.query.creditpersonquerydao.entity.userarr.CpqUserAttr;
import cn.com.dhcc.query.creditpersonqueryservice.ipaddr.service.CpqIpaddrService;
import cn.com.dhcc.query.creditpersonqueryservice.macaddr.service.CpqMacaddrService;
import cn.com.dhcc.query.creditpersonqueryservice.userattr.service.util.UserAttrUtil;
import cn.com.dhcc.query.creditpersonqueryweb.util.GetMacAddress;
import cn.com.dhcc.query.creditpersonqueryservice.util.UserUtils;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;

*//**
 * @author lekang.liu
 * @date 2018年4月8日
 *
 *//*
public class ValidateIPAndMac {

	private HttpServletRequest request;
	boolean falg;
	private int bindIPNum = 0;
	private int bindMACNum = 0;
	private String macAddress = null;
	private CpqIpaddrService ipaddrService;
	private CpqMacaddrService macaddrService;

	public ValidateIPAndMac(HttpServletRequest request) {
		this.request = request;
		falg = validate();
	}

	*//**
	 * 进行登陆绑定校验
	 * 
	 * @return
	 *//*
	public boolean validate() {
		boolean haveFlag = false;
		String username = "";
		try {
			username = getUserInfo(request, "username");
			CpqUserAttr cpqUserAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(username);
			if (null != cpqUserAttr) {
				// 特权用户
				if (!Objects.equals("0", cpqUserAttr.getBindPermit())) {
					return true;
				}
			}
			String terminalBindMode = ConfigUtil.getTerminalBindMode();
			// 不进行绑定
			if (Objects.equals("0", terminalBindMode)) {
				return true;
			}

			haveFlag = bindValidate(username, terminalBindMode);

			if (!haveFlag) {
				haveFlag = isAutomateBind(terminalBindMode, cpqUserAttr);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return haveFlag;
		}
		return haveFlag;
	}

	*//**
	 * 针对不同的绑定类型，进行不同的登陆绑定校验
	 * 
	 * @param username
	 * @param terminalBindMode
	 * @return
	 * @throws IOException
	 *//*
	private boolean bindValidate(String username, String terminalBindMode) throws IOException {
		boolean flag = false;
		String ip = NetworkUtil.getIpAddress(request);
		// 绑定IP
		if (Objects.equals("1", terminalBindMode)) {
			flag = ipValidate(username, ip);
			if (!flag) {
				request.setAttribute("IPOrMACmsg", "IP地址与绑定的不符，禁止使用征信监管业务");
			}
		}
		// 绑定MAC
		if (Objects.equals("2", terminalBindMode)) {
			macAddress = GetMacAddress.getMacAddress(ip);
			if (null == macAddress) {
				request.setAttribute("IPOrMACmsg", "获取MAC地址失败，禁止使用征信监管业务。请联系管理员！");
				return false;
			}
			flag = macValidate(username, macAddress);
			if (!flag) {
				request.setAttribute("IPOrMACmsg", "MAC地址与绑定的不符，禁止使用征信监管业务");
			}
		}
		// 组合绑定
		if (Objects.equals("3", terminalBindMode)) {
			macAddress = GetMacAddress.getMacAddress(ip);
			if (null == macAddress) {
				request.setAttribute("IPOrMACmsg", "获取MAC地址失败，禁止使用征信监管业务。请联系管理员！");
				return false;
			}
			flag = ipValidate(username, ip) && macValidate(username, macAddress);
			if (!flag) {
				request.setAttribute("IPOrMACmsg", "IP和MAC地址与绑定的不符，禁止使用征信监管业务");
			}
		}
		return flag;
	}

	*//**
	 * IP地址绑定校验
	 * 
	 * @param username
	 * @param ip
	 * @return
	 * @throws IOException
	 *//*
	private boolean ipValidate(String username, String ip) throws IOException {
		String bindInfo = getBindInfo4Session(username + "_IP");
		if (Objects.equals(bindInfo, ip)) {
			return true;
		}
		ipaddrService = (CpqIpaddrService) getBean("cpqIpaddrServiceImpl");
		List<CpqIpaddr> cpqIpaddrList = (List<CpqIpaddr>) ipaddrService.findByUserName(username);
		if (null != cpqIpaddrList) {
			List<String> ipList = new ArrayList<String>();
			for (CpqIpaddr ipaddr : cpqIpaddrList) {
				ipList.add(ipaddr.getIpAddr());
			}
			boolean contains = ipList.contains(ip);
			if (contains) {
				setBindInfo2Session(username + "_IP", ip);
			}
			return contains;
		}
		return false;
	}

	*//**
	 * MAC地址绑定校验
	 * 
	 * @param username
	 * @param macAddress
	 * @return
	 * @throws IOException
	 *//*
	private boolean macValidate(String username, String macAddress) throws IOException {
		String bindInfo = getBindInfo4Session(username + "_MAC");
		if (Objects.equals(bindInfo, macAddress)) {
			return true;
		}
		macaddrService = (CpqMacaddrService) getBean("cpqMacaddrServiceImpl");
		List<CpqMacaddr> cpqMacaddrList = (List<CpqMacaddr>) macaddrService.findByUserName(username);
		bindIPNum = (null == cpqMacaddrList) ? 0 : cpqMacaddrList.size();
		List<String> macList = new ArrayList<String>();
		for (CpqMacaddr macaddr : cpqMacaddrList) {
			macList.add(macaddr.getMacAddr());
		}
		boolean contains = macList.contains(macAddress);
		if (contains) {
			setBindInfo2Session(username + "_MAC", macAddress);
		}
		return contains;
	}

	*//**
	 * 自动绑定
	 * 
	 * @param terminalBindMode
	 * @param cpqUserAttr
	 * @return
	 * @throws IOException
	 *//*
	private boolean isAutomateBind(String terminalBindMode, CpqUserAttr cpqUserAttr) throws IOException {
		String username = getUserInfo(request, "username");
		String ip = NetworkUtil.getIpAddress(request);
		// 判断绑定模式，是否进行自动绑定
		String bindMode = ConfigUtil.getLoginBindMode();
		if (!Objects.equals("1", bindMode)) {
			// 不自动绑定
			return false;
		}
		// 绑定模式为自动，判断绑定数量，进行绑定
		String bindAddrNum = ConfigUtil.getLoginBindAddrNum();
		if (null != cpqUserAttr) {
			String bindNumber = cpqUserAttr.getBindNumber();
			bindAddrNum = bindNumber;
		}
		// 绑定IP
		if (Objects.equals("1", terminalBindMode)) {
			if (Integer.parseInt(bindAddrNum) > bindIPNum) {
				// 进行自动绑定
				CpqIpaddr cpqIpaddr = new CpqIpaddr(new Date(), username, ip, new Date(), username);
				ipaddrService.create(cpqIpaddr);
				setBindInfo2Session(username + "_IP", ip);
				return true;
			}
			return false;
		}
		// 绑定MAC
		if (Objects.equals("2", terminalBindMode)) {
			if (null == macAddress) {
				return false;
			}
			if (Integer.parseInt(bindAddrNum) > bindMACNum) {
				// 进行自动绑定
				CpqMacaddr cpqMacaddr = new CpqMacaddr(new Date(), username, macAddress, new Date(), username);
				macaddrService.create(cpqMacaddr);
				setBindInfo2Session(username + "_MAC", macAddress);
				return true;
			}
			return false;
		}
		// 组合绑定
		if (Objects.equals("3", terminalBindMode)) {
			if (Integer.parseInt(bindAddrNum) > (bindIPNum < bindMACNum ? bindIPNum : bindMACNum)) {
				// 自动绑定
				if (null == macAddress) {
					return false;
				}
				CpqIpaddr cpqIpaddr = new CpqIpaddr(new Date(), username, ip, new Date(), username);
				ipaddrService.create(cpqIpaddr);
				CpqMacaddr cpqMacaddr = new CpqMacaddr(new Date(), username, macAddress, new Date(), username);
				macaddrService.create(cpqMacaddr);
				setBindInfo2Session(username + "_MAC", macAddress);
				setBindInfo2Session(username + "_IP", ip);
				return true;
			}
			return false;
		}
		return false;
	}

	*//**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param infoName
	 * @return
	 *//*
	private String getUserInfo(HttpServletRequest request, String infoName) {
		Map<String, String> info = UserUtils.getUserInfo(request);
		String infoValue = info.get(infoName).trim();
		return infoValue;
	}
	
	
	private String getBindInfo4Session(String bindKey) {
		Map<String, String> info = UserUtils.getUserInfo(request);
		String bindInfo = info.get(bindKey);
		return bindInfo;
	}
	
	private void setBindInfo2Session(String bindKey,String bindInfo) {
		Map<String, String> info = UserUtils.getUserInfo(request);
		info.put(bindKey, bindInfo);
	}

	private Object getBean(String beanName) {
		Object bean = ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
		return bean;
	}
}
*/