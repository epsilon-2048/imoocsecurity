package com.imooc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Aspect
//@Component
public class TimeAspect {

    @Around("execution(* com.imooc.web.controller.UserController.*(..))")
    public Object handleContrllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect atart");
        long start = new Date().getTime();
        //调用被拦截的方法
        Object object = pjp.proceed();
        System.out.println("time aspect:" + (new Date().getTime() - start));
        System.out.println("time aspect end");
        return object;
    }
}
