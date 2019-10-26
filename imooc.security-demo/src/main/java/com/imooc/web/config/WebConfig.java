package com.imooc.web.config;

import com.imooc.web.filter.TimeFilter;
import com.imooc.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        super.configureAsyncSupport(configurer);
        //跟异步请求相关的四个配置
        /*configurer.registerCallableInterceptors();
        configurer.registerDeferredResultInterceptors();
        configurer.setDefaultTimeout();
        configurer.setTaskExecutor();*/
    }

    //自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(timeInterceptor);
    }

    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter filter = new TimeFilter();
        registrationBean.setFilter(filter);

        //以这种方式注册过滤器，可以过滤指定url,
        List<String> urls = new ArrayList<>();
        //过滤全部
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
