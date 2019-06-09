/**
 *  Copyright (c)  @date 2018年6月6日 DHCC, Inc.
 *  All rights reserved.
 *  东华软件股份公司 版权所有 征信监管产品工作平台 
 */
package cn.com.dhcc.creditquery.person.queryweb.webservice.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cn.com.dhcc.creditquery.person.queryweb.webservice.rules.validator.SR0205001OfWebserviceValidator;

/**
 * 
 * @author lekang.liu
 * @date 2018年6月6日
 */
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,ElementType.TYPE })  
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SR0205001OfWebserviceValidator.class)
public @interface SR0205001OfWebservice {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
