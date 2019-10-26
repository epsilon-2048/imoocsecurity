package com.imooc.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConstraintValidator.class) //校验逻辑由指定类执行
public @interface MyConstraint {
    //下面三个必须有，其余的按需
    String message(); //报错信息
    Class<?>[] groups() default {};  //组
    Class<? extends Payload>[] payload() default {};
}
