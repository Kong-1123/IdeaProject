package cn.com.dhcc.creditquery.ent.queryweb.operatelog.aop;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.com.dhcc.credit.platform.util.NetworkUtil;
import cn.com.dhcc.creditquery.ent.businessmonitor.service.CeqOperateLogService;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;

/**
 * 日志记录，添加、删除、修改方法AOP
 */
@Aspect
@Component
public class OperateLogAspect {

	private final static Log log = LogFactory.getLog(OperateLogAspect.class);
	
	@Autowired
	private CeqOperateLogService logService;

	/**
	 * 只对增删改进行日志统计
	 */
	@Pointcut("@annotation(cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation)")
	private void serviceCall() {
	}

	/**
	 * 环绕通知
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "serviceCall()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		String ip = "";
		String userName = "";
		String operOrg = "";
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (null != requestAttributes) {
				HttpServletRequest request = requestAttributes.getRequest();
				ip = NetworkUtil.getIpAddress(request);
				Map<String, String> userInfo = UserUtilsForConfig.getUserInfo(request);
				if (userInfo == null || userInfo.isEmpty()) {
					return null;
				}
				String username = userInfo.get("username");
				if (StringUtils.isBlank(username)) {
					return null;
				}
				userName = username.trim();
				operOrg = UserUtilsForConfig.getDeptCode(request);
				log.info("IP:" + ip + "  userName:" + userName);
			}
		} catch (Exception e1) {
			log.error("日志切面异常", e1);
		}
		Object result = null;
		Date createTime = new Date();
		long start = System.currentTimeMillis();
		// 返回值
		result = joinPoint.proceed();
		long end = System.currentTimeMillis();
		// 操作执行时间
		long operateTime = end - start;
		log.debug("result:" + result);
		ThreadPoolExecutorUtil.getExecutorService().execute(new LogTask(joinPoint, logService,
				String.valueOf(operateTime), result, createTime, ip, userName,operOrg));
		return result;
	}

	public CeqOperateLogService getLogService() {
		return logService;
	}

	public void setLogService(CeqOperateLogService logService) {
		this.logService = logService;
	}

	public static Log getLog() {
		return log;
	}

}
