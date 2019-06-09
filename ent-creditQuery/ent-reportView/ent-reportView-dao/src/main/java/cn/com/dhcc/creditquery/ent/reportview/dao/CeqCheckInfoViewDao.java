/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.reportview.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.ent.reportview.entity.CeqCheckInfoView;

/**
*
* @author lekang.liu
* @date 2018年3月7日
*/
@Component
public interface CeqCheckInfoViewDao extends PagingAndSortingRepository<CeqCheckInfoView, String>, JpaSpecificationExecutor<CeqCheckInfoView>{
    

    
}
