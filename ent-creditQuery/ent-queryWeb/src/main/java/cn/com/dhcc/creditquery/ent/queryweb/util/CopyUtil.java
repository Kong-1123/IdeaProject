/**
 *  Copyright (c)  2017-2027 ICFCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of ICFCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with ICFCC.
 */
package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.CommonUtil;

/**
 * <工具类>
 * 
 * @author Densen.Liu
 * @date 2017年3月30日
 */
public class CopyUtil {

	/**
	 * 
	 * <公共属性>
	 */
	public static List<String> commonProperties = Arrays
			.asList(new String[] { "serialVersionUID","operationIdentifier"});
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	/**
	 * <复制接口对象的属性值到业务对象的相同属性中>
	 * 
	 * @param sou
	 * @param des
	 * @throws Exception
	 * @author Densen.Liu
	 * @date 2017年4月7日
	 */
	public static void copy(Object sou, Object des) throws Exception {
		Class c2 = des.getClass();
		Class c1 = sou.getClass();
		Field[] fs = c2.getDeclaredFields();
		for (Field f : fs) {
			// 属性名
			String name = f.getName();
			// 公共属性忽略
			if (commonProperties.contains(name)) {
				continue;
			}
			// 属性类型
			Class type = f.getType();
			// 首字母转大写
			String methodName = name.substring(0, 1).toUpperCase() + name.substring(1);
			// 得到属性对应get方法
			Method getMethod = c1.getMethod("get" + methodName);
			// 执行源对象的get方法得到属性值
			Object value = getMethod.invoke(sou);
			// 属性对应的set方法
			Method setMethod = c2.getMethod("set" + methodName, type);
			setMethod.invoke(des, value);

		}
	}
	
}
