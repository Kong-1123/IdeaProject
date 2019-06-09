/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.rules.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.redisson.api.RedissonClient;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.rules.BatchReqBeanRules;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchReqBean;

public class BatchReqBeanRulesValidator implements ConstraintValidator<BatchReqBeanRules, BatchReqBean> {
	private static RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void initialize(BatchReqBeanRules constraintAnnotation) {
	}

	@Override
	public boolean isValid(BatchReqBean value, ConstraintValidatorContext context) {
		boolean flag = false;
		StringBuffer messageTemplate = null;
		if (null != value) {
			if (!(redis.getMap("idType_Q_K").keySet().contains(value.getCerttype()))) {
				messageTemplate = new StringBuffer();
				messageTemplate.append("ClientType不在数据字典中;");
			}
			if (!(redis.getMap("qryReason_Q_K").keySet().contains(value.getQryreason()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("Qryreason不在数据字典中;");
			}
			if (!(redis.getMap("serviceCode_Q_K").keySet().contains(value.getServiceCode()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("serviceCode不在数据字典中;");
			}

			boolean validate = validate(value.getCerttype(), value.getCertno());
			if (!validate) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ClientNo不符合规则;");
			}
		}

		if (null == messageTemplate) {
			flag = true;
		} else {
			tip(context, messageTemplate.toString());
		}

		return flag;
	}

	private boolean validate(String clientType, String clientNo) {
		String pattern = "";
		switch (clientType) {
		case "0":
			pattern = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)";
			Pattern compile = Pattern.compile(pattern);
			return compile.matcher(clientNo).matches();
		default:
			break;
		}
		return true;
	}

	private void tip(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	}
}*/
