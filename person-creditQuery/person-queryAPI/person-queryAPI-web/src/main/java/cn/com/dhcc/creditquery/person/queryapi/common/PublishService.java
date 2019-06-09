/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryapi.common;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import cn.com.dhcc.creditquery.person.queryapi.service.CpqPersonReportQueryServiceWS;
import cn.com.dhcc.creditquery.person.queryapi.service.impl.CpqPersonReportQueryServiceImpl;

/**
 * 
 * @author yuzhao.xue
 * @date 2019年1月21日
 */
public class PublishService{
    /**
     * 使用CXF的JaxWsServerFactoryBean发布服务
     * @param
     */
    public static void main(String[] args) {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(CpqPersonReportQueryServiceWS.class);
        //服务发布地址
        factory.setAddress("http://localhost:8090/querywebservice-person/webservice/ReportQuery");
        factory.setServiceBean(new CpqPersonReportQueryServiceImpl());
        factory.create();
        System.out.println("publish success");
    }
}
