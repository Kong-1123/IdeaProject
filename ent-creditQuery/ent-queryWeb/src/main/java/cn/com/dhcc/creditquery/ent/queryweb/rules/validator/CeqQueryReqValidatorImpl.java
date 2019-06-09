package cn.com.dhcc.creditquery.ent.queryweb.rules.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import cn.com.dhcc.creditquery.ent.queryweb.rules.QueryReqValidator;
import cn.com.dhcc.creditquery.ent.queryweb.vo.QueryReq;

public class CeqQueryReqValidatorImpl implements ConstraintValidator<QueryReqValidator, QueryReq> {

	@Override
	public void initialize(QueryReqValidator constraintAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(QueryReq value, ConstraintValidatorContext context) {
		boolean flag = false;
		StringBuffer messageTemplate = null;
		boolean nullflag = notNullValidate(value);
		if(nullflag){
			messageTemplate = new StringBuffer();
			messageTemplate.append("校验不通过!");
		}
		if (null == messageTemplate) {
			flag = true;
		} else {
			tip(context, messageTemplate.toString());
		}

		return flag;
	}

	private boolean notNullValidate(QueryReq value) {
		boolean flag = false;
		// 中征码与机构信用码至少有 一个
		if (StringUtils.isBlank(value.getSignCode()) && StringUtils.isBlank(value.getOrgInstCode())) {
			flag = true;
		}
	
		if (StringUtils.isBlank(value.getQueryFormat())) {
			flag = true;
		}
		if (StringUtils.isBlank(value.getQryReason())) {
			flag = true;
		}
		if (StringUtils.isBlank(value.getResultType())) {
			flag = true;
		}
		return flag;
	}

	private void tip(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	}
}
