package cn.com.dhcc.creditquery.person.queryweb.filter;
/*package cn.com.dhcc.query.creditpersonqueryweb.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

*//**
 * IP与MAC绑定校验Filter
 * @author lekang.liu
 * @date 2018年4月8日
 *
 *//*
public class IPAndMacBindFilter implements Filter {

	private static List<String> postfixList = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String[] postfixStr = new String[] { "jpg", "png", "gif", "css", "js", "json" };
		postfixList = Arrays.asList(postfixStr);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		// 暂无redisKEY销毁行为； 等待平台提供Session销毁通知后，补加登陆绑定信息的移除功能。接收到销毁通知，于REDIS中删除响应的value值。
		boolean falg = false;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		falg = isCommonUrl(request);
		if (!falg) {
			// 进行IP与MAC地址绑定的校验
			if (!new ValidateIPAndMac(request).falg) {
				// 地址绑定不符禁止使用监管业务
				// 自定义HTTP状态码，通过error-page配置跳转到提示页面
				int errorCode = 601;
				response.sendError(errorCode);
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
	
	*//**
	 * 判断是否为不需进行绑定校验的静态文件及数据字典加载请求
	 * @param request  访问请求
	 * @return 为静态文件及数据字典加载请求时，返回为{@code true},其他情况为{@code false}
	 *//*
	private static boolean isCommonUrl(HttpServletRequest request){
		boolean falg = false;
		// 对URL进行过滤
				String url = request.getRequestURI();
				String name = request.getContextPath() + "/";
				url = url.replace(name, "");
				String[] split = url.split("\\/");
				// 过滤后缀
				if (split[split.length - 1].contains(".")) {
					String[] split2 = split[split.length - 1].split("\\.");
					falg = postfixList.contains(split2[split2.length - 1]);
				}
				// 过滤common类请求
				if (Objects.equals("common", split[0])) {
					falg = true;
				}
				//过滤接口的所有请求
				if(split[0].contains("creditreportservice")){
					falg = true;
				}
				
		return falg;
	}
}
*/