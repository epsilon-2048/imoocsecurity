package com.imooc.security.browser;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired private SecurityProperties securityProperties;

    //配置自定义认证成功处理器，其实现类已经注册为带名字的组件，注入组件名即可
    @Autowired
    AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    //配置自定义认证失败处理器，其实现类已经注册为带名字的组件，注入组件名即可
    @Autowired
    AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    //数据库源
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //图形验证码认证过滤器
        ValidateCodeFilter filter = new ValidateCodeFilter();
        //设置认证失败处理器
        filter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        //传入配置
        filter.setSecurityProperties(securityProperties);
        //初始化需认证的url
        filter.afterPropertiesSet();

        //--------------不需做登陆验证的url----------------------------
        Set<String> urls = new HashSet<>();
        Collections.addAll(urls,"/authentication/require",
                securityProperties.getBrowser().getLoginPage(),
                "/code/image");
        //urls.add(null);
        String[] urlStrings = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                securityProperties.getPermitUrls(),",");
        if (urlStrings != null && urlStrings.length != 0)
            urls.addAll(Arrays.asList(urlStrings));
        //System.out.println(Arrays.toString(urls.toArray(new String[0])));
        //***--------------------------------------------

        //http.httpBasic()
        http
            //在使用UsernamePasswordAuthenticationFilter过滤器之前先使用我们的图片验证码过滤器
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                //自定义登陆页
                .loginPage("/authentication/require")
                //自定义登陆信息表单提交地址
                .loginProcessingUrl("/authentication/form")
                //配置自定义认证成功处理器
                .successHandler(imoocAuthenticationSuccessHandler)
                //配置自定义认证失败处理器
                .failureHandler(imoocAuthenticationFailureHandler)
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
            //http.authorizeRequests()方法有多个子节点，每个macher按照他们的声明顺序执行
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
