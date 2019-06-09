package cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;

import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.SR0201001OfWebservice;
import cn.com.dhcc.creditquery.person.queryweb.webservice.vo.SingleQueryVo;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;

/**
 * @author lekang.liu
 * @date 2018年3月26日
 *
 */
public class SR0201001OfWebserviceValidator implements ConstraintValidator<SR0201001OfWebservice, SingleQueryVo> {

    private static RedissonClient redis = RedissonUtil.getLocalRedisson();

    private static final String NAME_REGEX = "[\\u4E00-\\u9FA5·s_a-zA-Z.s][\\s\\u4E00-\\u9FA5·s_a-zA-Z.s]*[\\u4E00-\\u9FA5·s_a-zA-Z.s]*";

    private static final String CLIENTNO_REGEX = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X)";
    
    private static final String CLIENTNAME = "ClientName只能填写中文汉字与英文字母;";
    private static final String CLIENTTYPE = "ClientType不在数据字典中;";
    private static final String QRYREASON = "Qryreason不在数据字典中;";
    private static final String QUERYFORMAT = "QueryFormat不在数据字典中;";
    private static final String REPORTTYPE = "ReportType不在数据字典中;";
    private static final String QUERYPATTERN = "QueryPattern不在数据字典中;";
    private static final String CLIENTNO = "ClientNo不符合规则;";

    private static final String CLIENTTYPE_IDCARD = "0";

    private static ArrayList<Object> resultTypeList = null;
    private static ArrayList<Object> queryPatternList = null;

    static {
        resultTypeList = new ArrayList<>();
        resultTypeList.add("0");
        resultTypeList.add("1");
        resultTypeList.add("2");
        resultTypeList.add("4");

        queryPatternList = new ArrayList<>();
        queryPatternList.add("1");
        queryPatternList.add("0");
        queryPatternList.add("2");
    }

    @Override
    public void initialize(SR0201001OfWebservice constraintAnnotation) {
    }

    @Override
    public boolean isValid(SingleQueryVo value, ConstraintValidatorContext context) {
        boolean flag = false;
        StringBuffer messageTemplate = null;
        if (null != value) {
            value.setClientName(value.getClientName().trim());
            value.setAssociateBusinessData(value.getAssociateBusinessData().trim());

            // 被查询人姓名
            boolean matches = Pattern.matches(NAME_REGEX, value.getClientName());
            if (!matches) {
                messageTemplate = new StringBuffer();
                messageTemplate.append(CLIENTNAME);
            }

            if (!(redis.getMap(PersonCacheConstant.IDTYPE_Q_K).keySet().contains(value.getClientType()))) {
                messageTemplate = new StringBuffer();
                messageTemplate.append(CLIENTTYPE);
            }
            if (!(redis.getMap(PersonCacheConstant.QRYREASON_Q_K).keySet().contains(value.getQryreason()))) {
                if (null == messageTemplate) {
                    messageTemplate = new StringBuffer();
                }
                messageTemplate.append(QRYREASON);
            }
            if (!(redis.getMap(PersonCacheConstant.QRYFORMAT_Q_K).keySet().contains(value.getQueryFormat()))) {
                if (null == messageTemplate) {
                    messageTemplate = new StringBuffer();
                }
                messageTemplate.append(QUERYFORMAT);
            }

            // if
            // (!(redis.getMap("resultType_Q_K").keySet().contains(value.getReportType())))
            // {
            if (!resultTypeList.contains(value.getReportType())) {
                if (null == messageTemplate) {
                    messageTemplate = new StringBuffer();
                }
                messageTemplate.append(REPORTTYPE);
            }

            if (StringUtils.isNotBlank(value.getQueryPattern()) && !queryPatternList.contains(value.getQueryPattern())) {
                if (null == messageTemplate) {
                    messageTemplate = new StringBuffer();
                }
                messageTemplate.append(QUERYPATTERN);
            }
            boolean validate = validate(value.getClientType(), value.getClientNo());
            if (!validate) {
                if (null == messageTemplate) {
                    messageTemplate = new StringBuffer();
                }
                messageTemplate.append(CLIENTNO);
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
        switch (clientType) {
        case CLIENTTYPE_IDCARD:
            boolean matches = Pattern.matches(CLIENTNO_REGEX,clientNo);
            return matches;
        default:
            break;
        }
        return true;
    }

    public static void main(String[] args) {
        // String regex = ".*\\d+.*";
        // String regex =
        // "[\\u4E00-\\u9FA5·s_a-zA-Z.s][\\s\\u4E00-\\u9FA5·s_a-zA-Z.s]*[\\u4E00-\\u9FA5·s_a-zA-Z.s]*";
        // String regex = "\\S.*\\S*";
        // String regex = "(^\\s+)|(\\s+$)";
        String regex = "[\\s　]|[ ]$/gi";
        boolean matches = Pattern.matches(regex, "abc");
        System.out.println(matches);
    }
}
