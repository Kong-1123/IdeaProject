/**
 *  Copyright (c)  @date 2018年9月14日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator;

import java.util.ArrayList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.GenerationBatchQueryInfoRules;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.GenerationBatchQueryInfo;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryWithArchiveVo;

/**
 * 
 * @author lekang.liu
 * @date 2018年9月14日
 */
public class GenerationBatchQueryInfoValidator implements ConstraintValidator<GenerationBatchQueryInfoRules, GenerationBatchQueryInfo	> {

	@Override
	public void initialize(GenerationBatchQueryInfoRules constraintAnnotation) {

	}
	@Override
	public boolean isValid(GenerationBatchQueryInfo value, ConstraintValidatorContext context) {
		boolean flag = false;
		StringBuffer messageTemplate = null;
		if (null != value) {
			ArrayList<Object> resultTypeList = new ArrayList<>();
			resultTypeList.add("1");
			resultTypeList.add("2");
			resultTypeList.add("4");
			if (!resultTypeList.contains(value.getReportType())) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ReportType不在数据字典中;");
			}
			
			ArrayList<Object> batchQueryFileModeList = new ArrayList<>();
			batchQueryFileModeList.add("0");
			batchQueryFileModeList.add("1");
			if (!batchQueryFileModeList.contains(value.getBatchQueryFileMode())) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("BatchQueryFileMode不在数据字典中;");
			}
			
			
			if (null == messageTemplate) {
				flag = true;
			} else {
				tip(context, messageTemplate.toString());
			}
		}
		return flag;
	}
	
	private void tip(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	}

}
