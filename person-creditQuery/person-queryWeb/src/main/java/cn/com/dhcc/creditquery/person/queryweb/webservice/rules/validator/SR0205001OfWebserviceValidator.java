package cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.SR0205001OfWebservice;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryWithArchiveVo;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

/**
 * @author lekang.liu
 * @date 2018年6月6日
 *
 */
public class SR0205001OfWebserviceValidator implements ConstraintValidator<SR0205001OfWebservice, SingleQueryWithArchiveVo> {

	private static RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void initialize(SR0205001OfWebservice constraintAnnotation) {
	}

	@Override
	public boolean isValid(SingleQueryWithArchiveVo value, ConstraintValidatorContext context) {
		boolean flag = false;
		StringBuffer messageTemplate = null;
		if (null != value) {
			if (!(redis.getMap(PersonCacheConstant.IDTYPE_Q_K)).keySet().contains(value.getClientType())) {
				messageTemplate = new StringBuffer();
				messageTemplate.append("ClientType不在数据字典中;");
			}
			if (!(redis.getMap(PersonCacheConstant.QRYREASON_Q_K).keySet().contains(value.getQryreason()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("Qryreason不在数据字典中;");
			}
			if (!(redis.getMap(PersonCacheConstant.QRYFORMAT_Q_K).keySet().contains(value.getQueryFormat()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("QueryFormat不在数据字典中;");
			}
			if (!(redis.getMap(PersonCacheConstant.RESULT_TYPE_Q_K).keySet().contains(value.getReportType()))) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ReportType不在数据字典中;");
			}
			boolean validate = validate(value.getClientType(), value.getClientNo());
			if (!validate) {
				if (null == messageTemplate) {
					messageTemplate = new StringBuffer();
				}
				messageTemplate.append("ClientNo不符合规则;");
			}
			if (StringUtils.isNotBlank(value.getArchiveType())) {
				if (StringUtils.isBlank(value.getArchiveFileName())) {
					if (null == messageTemplate) {
						messageTemplate = new StringBuffer();
					}
					messageTemplate.append("ArchiveType存在时，ArchiveFileName不可为空;");
				}
				if (StringUtils.isBlank(value.getArchiveInfo())) {
					if (null == messageTemplate) {
						messageTemplate = new StringBuffer();
					}
					messageTemplate.append("ArchiveType存在时，ArchiveInfo不可为空;");
				}
				if (null == value.getArchiveExpireDate()) {
					if (null == messageTemplate) {
						messageTemplate = new StringBuffer();
					}
					messageTemplate.append("ArchiveType存在时，ArchiveExpireDate不可为空;");
				}
				if (null != value.getArchiveExpireDate()) {
					Calendar thisTime = Calendar.getInstance();
					Calendar instance = Calendar.getInstance();
					instance.setTime(value.getArchiveExpireDate());
					int compareTo = instance.compareTo(thisTime);
					if (compareTo < 0 ) {
						if (null == messageTemplate) {
							messageTemplate = new StringBuffer();
						}
						messageTemplate.append("ArchiveExpireDate不可早于当前日期;");
					}
				}
				List<String> list = Arrays.asList(new String[] { "0", "1", "2", "3" });
				if (!list.contains(value.getArchiveType())) {
					if (null == messageTemplate) {
						messageTemplate = new StringBuffer();
					}
					messageTemplate.append("ArchiveType不符合规则;");
				}
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
}
