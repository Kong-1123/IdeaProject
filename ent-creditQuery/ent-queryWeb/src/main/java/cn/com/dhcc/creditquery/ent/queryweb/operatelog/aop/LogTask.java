package cn.com.dhcc.creditquery.ent.queryweb.operatelog.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.alibaba.fastjson.JSON;

import cn.com.dhcc.creditquery.ent.businessmonitor.service.CeqOperateLogService;
import cn.com.dhcc.creditquery.ent.query.bo.businessmonitor.CeqOperateLogBo;
import cn.com.dhcc.creditquery.ent.queryweb.base.BaseController;
import cn.com.dhcc.query.creditquerycommon.operatelog.annotation.LogOperation;

/**
 * 记录操作人的增删改记录 线程类
 */
public class LogTask extends BaseController implements Runnable {

	private ProceedingJoinPoint joinPoint;
	private final static Log log = LogFactory.getLog(LogTask.class);
	private CeqOperateLogService logService;
	private String operateTime;
	private Object returnResult;
	private Date createTime;
	private String ip;
	private String userName;
	private String operOrg;

	

	/**
	 * @param joinPoint
	 * @param logService
	 * @param operateTime
	 * @param returnResult
	 * @param createTime
	 * @param ip
	 * @param userName
	 * @param operOrg
	 */
	public LogTask(ProceedingJoinPoint joinPoint, CeqOperateLogService logService, String operateTime,
			Object returnResult, Date createTime, String ip, String userName, String operOrg) {
		super();
		this.joinPoint = joinPoint;
		this.logService = logService;
		this.operateTime = operateTime;
		this.returnResult = returnResult;
		this.createTime = createTime;
		this.ip = ip;
		this.userName = userName;
		this.operOrg = operOrg;
	}

	public LogTask() {
		super();
	}

	@Override
	public void run() {
		log.debug("Runnable Run start");
		CeqOperateLogBo operateLogBo = new CeqOperateLogBo();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		// 获取方法名
		String methodName = method.getName();
		log.debug("success方法名称:" + methodName);
		operateLogBo.setMethodName(methodName);
		LogOperation annotation = method.getAnnotation(LogOperation.class);
		boolean flag = StringUtils.isBlank(ip) || StringUtils.isBlank(userName);
		if (annotation.isQuery() && flag) {
			return;
		}
		// 目标动作
		String methodLogMessage = annotation.value();
		// 类的路径
		String clazzName = joinPoint.getTarget().getClass().getName();
		operateLogBo.setServicePath(clazzName);
		Object[] arg = joinPoint.getArgs();
		if (arg != null) {
			log.info("Args:" + Arrays.toString(joinPoint.getArgs()));
			operateLogBo.setParam(Arrays.toString(joinPoint.getArgs()));
		}
		// return结果
		log.debug("time:" + operateTime + " ms");
		log.debug("clazzName:" + clazzName);
		if (returnResult != null) {
			operateLogBo.setReturnResult(JSON.toJSONString(returnResult));
		}
		operateLogBo.setOperateTime(String.valueOf(operateTime));
		operateLogBo.setCreateTime(createTime);
		if (null != userName) {
			operateLogBo.setUserName(userName);
		}
		operateLogBo.setIp(ip);
		operateLogBo.setActName(methodLogMessage);
		
		if (null != operOrg) {
			operateLogBo.setOperOrg(operOrg);
		}

		try {
			log.info("operateLogBo:" + operateLogBo);
			logService.create(operateLogBo);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.debug("Runnable Run end");
	}

}
