/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.queryflowmanager.bo;

import cn.com.dhcc.creditquery.person.query.bo.BaseBo;
import cn.com.dhcc.creditquery.person.query.bo.reportanalysis.CpqReportBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 个人查询报告业务对象
 * @author sjk
 * @date 2019年2月27日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CpqQueryReportFlowBo extends BaseBo{

	/**
	 * 查询记录id
	 */
	private String queryRecordId;
	
	/**
	 * 征信中心查询耗时
	 */
	private String useTime;
	/**
	 * 信用报告来源
	 */
	private String reportSource;
	
	/**
	 *  信用报告html格式
	 */
	private String htmlCreditReport;
	
	/**
	 *  信用报告xml格式
	 */
	private String xmlCreditReport;
	
	/**
	 *  信用报告pdf格式
	 */
	private String pdfCreditReport;
	
	/**
	 *  信用报告json格式
	 */
	private String jsonCreditReport;
	
	/**
	 * html报告存储路径
	 */
	private String htmlReportPath;
	/**
	 * xml报告存储路径
	 */
	private String xmlReportPath;
	/**
	 * pdf报告存储路径
	 */
	private String pdfReportPath;
	/**
	 * json报告存储路径
	 */
	private String jsonReportPath;
	
	/**
	 * 信用报告bo
	 */
	private CpqReportBo cpqReportBo;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
		
	}
	
}
