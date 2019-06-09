package cn.com.dhcc.creditquery.person.queryweb.webservice.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator.SR0201001OfWebserviceValidator;

/** 
* @author lekang.liu
* @date 2018年3月26日
*
*/
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,ElementType.TYPE })  
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SR0201001OfWebserviceValidator.class)
public @interface SR0201001OfWebservice {
	
	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}
