package cn.com.dhcc.creditquery.person.queryweb.operatelog.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.com.dhcc.creditquery.person.businessmonitor.service.CpqOperateLogService;
import cn.com.dhcc.creditquery.person.query.bo.businessmonitor.CpqOperateLogBo;
import cn.com.dhcc.creditquery.person.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;


/**
 * 记录操作人的增删改记录 线程类
 */

public class LogTask extends BaseController implements Runnable {

	private ProceedingJoinPoint joinPoint;
	private final static Logger log = LoggerFactory.getLogger(LogTask.class);
	private CpqOperateLogService logService;
	private String operateTime;
	private Object returnResult;
	private Date createTime;
	private String ip;
	private String userName;
	private String operOrg;

	public LogTask(ProceedingJoinPoint joinPoint, CpqOperateLogService logService, String operateTime, Object returnResult, Date createTime, String ip, String userName, String operOrg) {
		super();
		this.joinPoint = joinPoint;
		this.logService = logService;
		this.operateTime = operateTime;
		this.returnResult = returnResult;
		this.createTime = createTime;
		this.ip = ip;
		this.userName = userName;
		this.operOrg =  operOrg;
	}

	public LogTask() {
		super();
	}

	@Override
	public void run() {
		log.debug("Runnable Run start");
		CpqOperateLogBo operateLog = new CpqOperateLogBo();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		// 获取方法名
		String methodName = method.getName();
		log.debug("success方法名称:" + methodName);
		operateLog.setMethodName(methodName);
		LogOperation annotation = method.getAnnotation(LogOperation.class);
		boolean flag = StringUtils.isBlank(ip) || StringUtils.isBlank(userName);
		if (annotation.isQuery() && flag) {
			return;
		}
		// 目标动作
		String methodLogMessage = annotation.value();
		// 类的路径
		String clazzName = joinPoint.getTarget().getClass().getName();
		operateLog.setServicePath(clazzName);
		Object[] arg = joinPoint.getArgs();
		if (arg != null) {
			log.info("Args:" + Arrays.toString(joinPoint.getArgs()));
			operateLog.setParam(Arrays.toString(joinPoint.getArgs()));
		}
		log.debug("time:" + operateTime + " ms");
		log.debug("clazzName:" + clazzName);
		if (returnResult != null) {
			operateLog.setReturnResult(JSON.toJSONString(returnResult));
		}
		operateLog.setOperateTime(String.valueOf(operateTime));
		operateLog.setCreateTime(createTime);
		if (null != userName) {
			operateLog.setUserName(userName);
		}
		operateLog.setIp(ip);
		operateLog.setActName(methodLogMessage);
		if (null != operOrg) {
			operateLog.setOperOrg(operOrg);
		}
		try {
			log.info("operateLog:" + operateLog);
			logService.operateLog(operateLog);
		} catch (Exception e) {
			log.error("日志记录线程出错", e);
		}
		log.debug("Runnable Run end");
	}

}
