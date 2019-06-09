package cn.com.dhcc.creditquery.person.queryweb.filter;

import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CookieCheckFilter  implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req  = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		Map<String, String> userInfo = UserConfigUtils.getUserInfo(req);
		
		if(null == userInfo || userInfo.isEmpty()){
			String systemUrl = ConfigUtil.getSystemUrl();
			resp.sendRedirect(systemUrl+"login");
			return;
		}
		
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		
	}

}
