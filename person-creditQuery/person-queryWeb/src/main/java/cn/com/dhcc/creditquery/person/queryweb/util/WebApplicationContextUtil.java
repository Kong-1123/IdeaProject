/**
 *  Copyright (c)  @date 2018年9月12日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author lekang.liu
 * @date 2018年9月12日
 */
public class WebApplicationContextUtil {
	private static WebApplicationContext applicationContext = null;
	
	static {
		applicationContext = ContextLoader.getCurrentWebApplicationContext();
	}
	
	/**
	 * 获取bean
	 * 
	 * @param beanName
	 * @return Object
	 */
	public static Object getBean(String beanName) {
		Object bean = applicationContext.getBean(beanName);
		return bean;
	}
}
