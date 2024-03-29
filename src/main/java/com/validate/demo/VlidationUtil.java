package com.validate.demo;

/**
 * @author lijiaming
 * @title: VlidationUtil
 * @projectName demo
 * @description: TODO
 * @date 2019/7/1715:48
 */

import javax.validation.*;
import java.util.Set;

/**
 * @ClassName: VlidationUtil
 * @Description: 校验工具类
 * @author zhangyy
 * @date 2015-7-31 上午10:28:48
 */
public class VlidationUtil {

    private static Validator validator;

    static {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }


    /**
     * @throws ValidationException
     * @throws ValidationException
     * @Description: 校验方法
     * @param t 将要校验的对象
     * @throws ValidationException
     * void
     * @throws
     */
    public static <T> void validate(T t) throws ValidationException {
        Set<ConstraintViolation<T>> set =  validator.validate(t);
        if(set.size()>0){
            StringBuilder validateError = new StringBuilder();
            for(ConstraintViolation<T> val : set){
                validateError.append(val.getMessage() + " ;");
            }
            throw new ValidationException(validateError.toString());
        }
    }

}
