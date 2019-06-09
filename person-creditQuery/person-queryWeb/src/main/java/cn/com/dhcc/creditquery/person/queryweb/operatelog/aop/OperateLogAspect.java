package cn.com.dhcc.creditquery.person.queryweb.operatelog.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import cn.com.dhcc.creditquery.person.businessmonitor.service.CpqOperateLogService;
import cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig;


/**
 * 日志记录，添加、删除、修改方法AOP
 * 
 * 
 */

@Aspect
@Component
public class OperateLogAspect {

	private final static Log log = LogFactory.getLog(OperateLogAspect.class);

	@Autowired
	private CpqOperateLogService logService;

	
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
		Object result = null;
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (null != requestAttributes) {
				HttpServletRequest request = requestAttributes.getRequest();
				ip = NetworkUtil.getIpAddress(request);
				userName = UserUtilsForConfig.getUserInfo(request).get("username").trim();
				operOrg = UserUtilsForConfig.getDeptCode(request);
				log.info("IP:" + ip + "  userName:" + userName);
			}
			Date createTime = new Date();
			long start = System.currentTimeMillis();
			// 返回值
			result = joinPoint.proceed();
			long end = System.currentTimeMillis();
			// 操作执行时间
			long operateTime = end - start;
			log.debug("result:" + result);
			ThreadPoolExecutorUtil.getExecutorService().execute(new LogTask(joinPoint, logService,
					String.valueOf(operateTime), result, createTime, ip, userName, operOrg));

		} catch (Exception e1) {
			log.error("日志切面发生异常", e1);
		}
		return result;
	}

}

