package com.imooc.security.core.authentication;

import com.imooc.security.core.properties.SecurityConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Arrays;

/**
 * 本安全框架的基本默认配置
 */

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {

        http.formLogin()
                //自定义登陆页
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                //自定义登陆信息表单提交地址
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                //将spring默认的失败处理器设置为本系统的默认处理器
                .successHandler(imoocAuthenticationSuccessHandler)
                //将spring默认的失败处理器设置为本系统的默认处理器
                .failureHandler(imoocAuthenticationFailureHandler);
    }

}
