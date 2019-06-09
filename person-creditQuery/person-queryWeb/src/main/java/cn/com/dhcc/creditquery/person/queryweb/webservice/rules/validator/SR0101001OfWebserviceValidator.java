package cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.redisson.api.RedissonClient;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.SR0101001OfWebservice;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.BatchQueryVo;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

public class SR0101001OfWebserviceValidator implements ConstraintValidator<SR0101001OfWebservice, BatchQueryVo> {
	private static RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void initialize(SR0101001OfWebservice constraintAnnotation) {

	}

	@Override
	public boolean isValid(BatchQueryVo value, ConstraintValidatorContext context) {
		boolean flag = false;
		StringBuffer messageTemplate = null;
		if (null != value) {

			if (!(redis.getMap(PersonCacheConstant.RESULT_TYPE_Q_K).keySet().contains(value.getReportType()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ReportType不在数据字典中;");
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
