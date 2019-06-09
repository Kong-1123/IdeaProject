package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class ValidateUtil {

	
	public static <T> ResultBeans validate(T t){
		Set<ConstraintViolation<T>> constraintViolations = MyValidatorFactory.getInstance().validate(t);
		if(constraintViolations.size() != 0){
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {
				   sb.append( constraintViolation.getMessage()).append("<br/>");
			}
			return new ResultBeans(Constants.ERRORCODE, sb.toString());
		}else{
			return null;
		}
	}
	
}
