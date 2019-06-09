package cn.com.dhcc.creditquery.person.queryweb.rules.validator;

import cn.com.dhcc.creditquery.person.queryweb.rules.QueryReqValidator;
import cn.com.dhcc.creditquery.person.queryweb.vo.QueryReq;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpqQueryReqValidatorImpl implements ConstraintValidator<QueryReqValidator, QueryReq> {

	@Override
	public void initialize(QueryReqValidator constraintAnnotation) {
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
		if (StringUtils.isBlank(value.getClientName())) {
			flag = true;
		}
		if (StringUtils.isBlank(value.getCertNo())) {
			flag = true;
		}
		if (StringUtils.isBlank(value.getCertType())) {
			flag = true;
		}
		if (StringUtils.isBlank(value.getQueryType())) {
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
