package com.imooc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//Object 表示可以注解在任何类型上
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

    //@Autowired 可以注入任何spring管理的实例

    @Override
    public void initialize(MyConstraint myConsitraint) {
        //逻辑
        System.out.println("my validator init");
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        //逻辑处理
        System.out.println(o);
        return false;
    }
}
