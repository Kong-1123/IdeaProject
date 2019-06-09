package cn.com.dhcc.creditquery.ent.queryweb.util;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * <校验对象工厂类用于创建校验对象>
 * @author Tianyu.Li
 *
 */
public class MyValidatorFactory {

    private MyValidatorFactory() {};
    
    public static Validator getInstance(){
	   return InstanceHolder.validator;
    }
    
    private static class InstanceHolder{
    	static Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
    }
}
