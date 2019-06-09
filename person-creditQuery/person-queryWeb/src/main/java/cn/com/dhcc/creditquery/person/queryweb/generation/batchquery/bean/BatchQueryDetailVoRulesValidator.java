/*
package cn.com.dhcc.creditquery.person.queryweb.generation.batchquery.bean;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraints.Length;
import org.redisson.api.RedissonClient;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.rules.BatchReqBeanRules;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchReqBean;

public class BatchQueryDetailVoRulesValidator implements ConstraintValidator<BatchQueryDetailVoRules, BatchQueryDetailVo> {
	private static RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void initialize(BatchQueryDetailVoRules constraintAnnotation) {
	}

	@Override
	public boolean isValid(BatchQueryDetailVo value, ConstraintValidatorContext context) {
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
			if (!(redis.getMap("qryFormat_Q_K").keySet().contains(value.getQueryFormat()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("QueryFormat不在数据字典中;");
			}
			if (!(redis.getMap("qryType_Q_K").keySet().contains(value.getQrytype()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("Qrytype不在数据字典中;");
			}
			boolean validate = validate(value.getCerttype(), value.getCertno());
			if (!validate) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ClientNo不符合规则;");
			}
			
			//被查询人姓名
			String regex = "[\\u4E00-\\u9FA5·s_a-zA-Z.s][\\s\\u4E00-\\u9FA5·s_a-zA-Z.s]*[\\u4E00-\\u9FA5·s_a-zA-Z.s]*";
			boolean matches = Pattern.matches(regex,value.getName());
			if(!matches){
				messageTemplate = new StringBuffer();
				messageTemplate.append("ClientName只能填写中文汉字与英文字母;");
			}
			
			//进行长度控制
			value.setName(validateLength(value.getName(),30));
			value.setCerttype(validateLength(value.getCerttype(),1));
			value.setCertno(validateLength(value.getCertno(),18));
			value.setQryreason(validateLength(value.getQryreason(),2));
			value.setQrytype(validateLength(value.getQrytype(),1));
			value.setOperator(validateLength(value.getOperator(),45));
			value.setOperorg(validateLength(value.getOperorg(),14));
			value.setAutharchiveid(validateLength(value.getAutharchiveid(),300));
			value.setAutharchivedata(validateLength(value.getAutharchivedata(),50));
			value.setQueryFormat(validateLength(value.getQueryFormat(),2));
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
			pattern = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X)";
			Pattern compile = Pattern.compile(pattern);
			return compile.matcher(clientNo).matches();
		default:
			break;
		}
		return true;
	}
	
	private String validateLength(String dataValue,int length){
		String value = dataValue;
		if(value.length() > length){
			value = value.substring(0, length);
		}
		return value;
	}
	

	private void tip(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	}
}
*/
