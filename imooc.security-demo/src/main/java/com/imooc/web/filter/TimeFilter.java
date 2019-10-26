package com.imooc.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

//如果使用过滤器
//@Component //若无加Component(如第三方过滤器)，可以在配置类中注册一个过滤器
public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("time filter start");
        long start = new Date().getTime();
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("time filter: " + (new Date().getTime() - start));
        System.out.println("time filter finish");
    }

    @Override
    public void destroy() {
        System.out.println("time filter destroy");

    }
}
