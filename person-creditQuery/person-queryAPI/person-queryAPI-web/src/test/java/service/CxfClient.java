/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * 
 * @author yuzhao.xue
 * @date 2019年3月26日
 */
public class CxfClient {

	public static void main(String[] args) throws IOException {
		//三部走
		//1、本地代理工厂实例
		JaxWsProxyFactoryBean pf = new JaxWsProxyFactoryBean();
		
		//2、设置webservice服务端的地址，加上wsdl也可以
		pf.setAddress("http://localhost:19090/personqueryapiweb/webservice/ReportQuery");
		
		//3、创建的到本地代理类（桩）
		CpqPersonReportQueryServiceWS creditReport = pf.create(CpqPersonReportQueryServiceWS.class);
		
		
		String path = Thread.currentThread().getContextClassLoader().getResource("req.xml").getPath();
		File file = new File(path);
		BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream(file),"UTF-8"));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String aa = sb.toString();
		
		String result = creditReport.getCreditReport(aa);
		
		System.out.println(result);
	}
}
