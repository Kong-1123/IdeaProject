/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.queryapi.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.dhcc.creditquery.ent.queryapi.entity.CeqSingleQuery;

/**
 * <企业单笔查询请求参数表>
 * @author guoshihu
 * @date 2019年1月18日
 */
public interface CeqSingleQueryDao extends PagingAndSortingRepository<CeqSingleQuery, String>,JpaSpecificationExecutor<CeqSingleQuery> {

    /**
     * 通过reqid查询数据
     * @param reqID
     * @return
     * @author yuzhao.xue
     * @date 2019年2月18日
     */
    @Query("from CeqSingleQuery q where q.reqId=?1")
    CeqSingleQuery findByReqId(String reqID);

}
