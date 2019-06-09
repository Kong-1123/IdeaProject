/**
 * Copyright (c) 2005-2014 leelong.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package cn.com.dhcc.creditquery.person.queryweb.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import com.google.common.net.HttpHeaders;

import cn.com.dhcc.credit.platform.util.Encodes;
import cn.com.dhcc.credit.platform.util.PageBean;
import cn.com.dhcc.credit.platform.util.SysDate;

/**
 * Http与Servlet工具类.
 * 
 */
public class Servlets2
{

	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
	private static final String DATE_END_WITH = "_DATE";
	private static final String DATE_RANGE_END_WITH = "_DATE_RANGE";

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds)
	{
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis()
				+ (expiresSeconds * 1000));
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response)
	{
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate)
	{
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag)
	{
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * 
	 * @param lastModified
	 *            内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request,
			HttpServletResponse response, long lastModified)
	{
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < (ifModifiedSince + 1000)))
		{
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * 
	 * @param etag
	 *            内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
			HttpServletResponse response, String etag)
	{
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null)
		{
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue))
			{
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens())
				{
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag))
					{
						conditionSatisfied = true;
					}
				}
			}
			else
			{
				conditionSatisfied = true;
			}

			if (conditionSatisfied)
			{
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName)
	{
		try
		{
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
					+ encodedfileName + "\"");
		}
		catch (UnsupportedEncodingException e)
		{
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request,
			String prefix)
	{
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null)
		{
			prefix = "";
		}
		while ((paramNames != null) && paramNames.hasMoreElements())
		{
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix))
			{
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if ((values == null) || (values.length == 0))
				{
					// Do nothing, no values found at all.
				}
				else if (values.length > 1)
				{
					params.put(unprefixed, values);
				}
				else if (unprefixed.endsWith(DATE_END_WITH))
				{ // 如果搜索为时间转换成date类型
					unprefixed = unprefixed.replace(DATE_END_WITH, "");
					Date d = unprefixed.startsWith("LTE") ? getLastSecondOfDay(SysDate.getDate(values[0])) : SysDate.getDate(values[0], "yyyy-MM-dd HH:mm:ss");
					params.put(unprefixed, d);
				}
				else if(unprefixed.endsWith(DATE_RANGE_END_WITH)){
					unprefixed = unprefixed.replace(DATE_RANGE_END_WITH, "");
					String[] arr1 = unprefixed.split("_");
					String[] arr2 = values[0].split(" - ");
					params.put(arr1[0] + "_" + arr1[2], SysDate.getDate(arr2[0], "yyyy-MM-dd HH:mm:ss"));
					params.put(arr1[1] + "_" + arr1[2], getLastSecondOfDay(SysDate.getDate(arr2[1])));
				}
				else
				{
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	private static Date getLastSecondOfDay(Date LTE_operateDate) {
		Calendar calendar = DateUtils.toCalendar(LTE_operateDate);
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		return calendar.getTime();
	}
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request,
			String prefix,PageBean page)
	{
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null)
		{
			prefix = "";
		}
		
		while ((paramNames != null) && paramNames.hasMoreElements())
		{
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix))
			{
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				//判断参数值为空串的情况
				if (isInvalidValues(values)) 
				{
					continue;
				} else {
					if (unprefixed.endsWith(DATE_END_WITH))
					{ // 如果搜索为时间转换成date类型
						unprefixed = unprefixed.replace(DATE_END_WITH, "");
						Date d = unprefixed.startsWith("LTE") ? getLastSecondOfDay(SysDate.getDate(values[0])) : SysDate.getDate(values[0], "yyyy-MM-dd HH:mm:ss");
						params.put(unprefixed, d);
						page.getParamsMap().put(paramName, values[0]);
					}	
					else if(unprefixed.endsWith(DATE_RANGE_END_WITH)){
						unprefixed = unprefixed.replace(DATE_RANGE_END_WITH, "");
						String[] arr1 = unprefixed.split("_");
						String[] arr2 = values[0].split(" - ");
						params.put(arr1[0] + "_" + arr1[2], SysDate.getDate(arr2[0], "yyyy-MM-dd HH:mm:ss"));
						params.put(arr1[1] + "_" + arr1[2], getLastSecondOfDay(SysDate.getDate(arr2[1])));
						page.getParamsMap().put(paramName, values[0]);
					} else {
						
						String value=valuesToString(values);
						page.getParamsMap().put(paramName, value);
						params.put(unprefixed, value);
					}
				}
			}
		}
		return params;
	}
	/*
	 * 把对应参数获取值拼接成字符串，以,分隔
	 */
	public static String valuesToString(String[] values){
		StringBuilder sb = new StringBuilder();
		for(String value:values){
			sb.append(value);
			sb.append(",");
		}
//		String v2=sb.substring(0, sb.length()-1).toString();
		String value1=sb.toString();
		String value2=value1.substring(0, value1.length()-1);
		return value2;
	}
	/**
	 * 判断参数值为空串的情况
	 * @param values
	 * @return
	 */
	private static boolean isInvalidValues(String[] values){
		boolean invalid = false;
		if(null == values){
			invalid =  true;
		} else if(values.length == 0) {
			invalid =  true;
		} else {
			invalid = StringUtils.isEmpty(values[0]);
		}
		return invalid;	
	}
	
	public static void main(String[] args) {
		String[] s = {"   "};
		boolean re = isInvalidValues(s);
		System.out.print(re);
	}
	/**
	 * 组合Parameters生成Query String的Parameter部分, 并在paramter name上加上prefix.
	 * 
	 * @see #getParametersStartingWith
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix)
	{
		if ((params == null) || (params.size() == 0))
		{
			return "";
		}

		if (prefix == null)
		{
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=')
					.append(entry.getValue());
			if (it.hasNext())
			{
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}

	/**
	 * 客户端对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic(String userName, String password)
	{
		String encode = userName + ":" + password;
		return "Basic " + Encodes.encodeBase64(encode.getBytes());
	}

}
