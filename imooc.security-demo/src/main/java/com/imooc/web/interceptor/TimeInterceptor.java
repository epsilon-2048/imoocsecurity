package com.imooc.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
//使用拦截器，只用Component并不能实现效果
//还需在配置类中配置下
public class TimeInterceptor implements HandlerInterceptor {
    //实际方法调用前调用
    //动态代理中拦截器模式
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("preHandle");
        System.out.println(((HandlerMethod)o).getBean().getClass().getName());

        httpServletRequest.setAttribute("time", new Date().getTime());
        return true; //返回false则不执行被代理方法
    }
    //实际方法（无异常）调用后调用，
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("preHandle");
        long start = (long) httpServletRequest.getAttribute("time");
        System.out.println("time interceptor: "+(new Date().getTime() - start ));
    }
    //实际方法（不管是否有异常）调用后调用，
    //如果被代理方法抛出的异常是异常处理器所指定的类或其子类，则异常处理由处理器处理
    //而不会被传递到afterCompletion
    //拦截器会拦截所有controller
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
        Long start = (Long) httpServletRequest.getAttribute("time");
        System.out.println("time interceptor: " + (new Date().getTime() - start));
        System.out.println("ex is:" + e);
    }
}
