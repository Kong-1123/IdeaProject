/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.authorizemanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import cn.com.dhcc.creditquery.person.authorizemanager.entity.CpqArchiveAuthorizeManager;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;

/**
 * 
 * @author DHC-S
 * @date 2019年2月22日
 */
public class test {

	public static void main(String[] args) {
		
		
//		String[] a = {"1","2","3","4"};
//		List<String> list = Arrays.asList(a);
		CpqArchiveBo bo = new CpqArchiveBo();
		CpqArchiveBo bo1 = new CpqArchiveBo();
		bo.setArchiveType("asd");
		bo.setClientName("sss");
		bo.setCreator("sd");
		List<CpqArchiveBo> list = new ArrayList<>();
		bo1.setArchiveType("asd");
		bo1.setClientName("sss");
		bo1.setCreator("sd");
		list.add(bo);
		list.add(bo1);
		Page<CpqArchiveBo> page = new PageImpl<>(list);
		Page<CpqArchiveAuthorizeManager> copyPage = ClassCloneUtil.copyPage(page, CpqArchiveAuthorizeManager.class);
		
		List<CpqArchiveAuthorizeManager> pageList = copyPage.getContent();
		for (CpqArchiveAuthorizeManager cpqArchiveAuthorizeManager : pageList) {
			System.out.println(cpqArchiveAuthorizeManager.getArchiveType());
			System.out.println(cpqArchiveAuthorizeManager.getClientName());
			System.out.println(cpqArchiveAuthorizeManager.getCreator());
		}
		
		
	}
	
}
