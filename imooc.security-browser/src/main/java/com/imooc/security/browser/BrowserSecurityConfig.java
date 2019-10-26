package com.imooc.security.browser;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired private SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        //http.httpBasic()
        http.formLogin()
                //自定义登陆页
                .loginPage("/authentication/require")
                //自定义登陆信息表单提交地址
                .loginProcessingUrl("/authentication/form")
                .and()
                //http.authorizeRequests()方法有多个子节点，每个macher按照他们的声明顺序执行
                .authorizeRequests()
                //放行匹配的url
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage()).permitAll()
                //尚未匹配的任何URL都要求用户进行身份验证
                .anyRequest().authenticated()
                .and()
                //关闭csrf防跨域攻击
                .csrf().disable();

    }

    //注册一个密码加密解密的类，可以提供一个自定义的实现PasswordEncoder接口的类
    //这个加密算法已经很强大了
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
