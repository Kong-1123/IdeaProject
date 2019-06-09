/**
 * Copyright (c)  2018-2028 DHCC, Inc.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of DHCC,
 * Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryweb.base;

import cn.com.dhcc.query.creditquerycommon.service.PlantFormInteractiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * TODO 不确定直接向BaseController中注入bean,先这么使用。有空了再直接注入试试
 * @Auther: liulekang
 * @Date: 2019/4/4 
 */
@Component
public class BaseControllerAutowired {

    @Autowired
    private PlantFormInteractiveService plantFormInteractiveService;

    private static BaseControllerAutowired baseControllerAutowired;

    @PostConstruct
    public void initService(){
        baseControllerAutowired = this;
        baseControllerAutowired.plantFormInteractiveService = this.plantFormInteractiveService;
    }

    public static PlantFormInteractiveService getPlantFormInteractiveService(){
        return  baseControllerAutowired.plantFormInteractiveService;
    }
}
