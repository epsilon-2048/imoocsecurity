package com.imooc.security.browser;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.phone.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 继承本系统的默认配置，并追加浏览器特定配置
 */

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {


    /**
     * 系统参数配置类
     */
    @Autowired private SecurityProperties securityProperties;

    /**
     * 数据库源
     */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 手机号登陆配置，
     * 已将所有的配置移至core模块，在本模块注入即可，这样的好处是，在任何需要
     * 手机号登陆功能的模块中只要添加core依赖即可，实现可重用，需配合短信验证码使用
     * 因为本功能并没有实现短信验证码，而是将短信验证码剥离开来，实现短信验证码的可重用
     */
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    /**
     *  图形、短信验证码配置
     * 将所有的配置移至core模块，在本模块注入即可，这样的好处是，在任何需要
     * 图形、短信验证码功能的模块中只要添加core依赖即可，实现可重用
     */
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {


    //--------------不需做登陆验证的url------这里再重构下----------------------
    Set<String> urls = new HashSet<>();
    Collections.addAll(urls,
            SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,
            securityProperties.getBrowser().getLoginPage(),
            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+SecurityConstants.DEFAULT_VALIDATE_CODE_TYPE_SMS,
            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+SecurityConstants.DEFAULT_VALIDATE_CODE_TYPE_IMAGE);
    //urls.add(null);
    String[] urlStrings = StringUtils.splitByWholeSeparatorPreserveAllTokens(
            securityProperties.getPermitUrls(),",");
    if (urlStrings != null && urlStrings.length != 0)
        urls.addAll(Arrays.asList(urlStrings));
    //System.out.println(Arrays.toString(urls.toArray(new String[0])));
    //***--------------------------------------------

        //http.httpBasic()

        // 添加本系统的默认配置
        applyPasswordAuthenticationConfig(http);

        http //加入图形、短信验证码配置
            .apply(validateCodeSecurityConfig)
            .and()
            //加入手机号登陆的配置
            .apply(smsCodeAuthenticationSecurityConfig)
            .and()
            //记住我配置 功能实现很简单，在这里配置就可以了
            .rememberMe()
                //
                .tokenRepository(persistentTokenRepository())
                //记住我过期时间
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                //用这个userDetail去做登陆
                .userDetailsService(userDetailsService)
            .and()
            //http.authorizeRequests()方法有多个子节点，每个matcher按照他们的声明顺序执行
            .authorizeRequests()
                //放行匹配的url
                .antMatchers(urls.toArray(new String[0]))
                .permitAll()
                //尚未匹配的任何URL都要求用户进行身份验证
                .anyRequest()
                .authenticated()
            .and()
            .csrf()
                //关闭csrf防跨域攻击
                .disable();
    }

    //注册一个密码加密解密的类，可以提供一个自定义的实现PasswordEncoder接口的类
    //这个加密算法已经很强大了
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(dataSource);
        //设置启动时自动建表
        //tokenRepository.setCreateTableOnStartup(true);

        return tokenRepository;
    }


}
