package com.imooc.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.security.browser.support.SimpleResponse;
import com.imooc.security.core.properties.LoginResponseType;
import com.imooc.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证失败后的处理器实现类，实现AuthenticationFailureHandler接口
 * SimpleUrlAuthenticationFailureHandler spring默认的处理器
 */


@Component("imoocAuthenticationFailureHandler")
public class ImoocAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {//implements AuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //spring提供的工具类
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //AuthenticationException 包含了认证过程中发送错误所产生的异常

        logger.info("登陆失败");
        if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            //只返回错误信息
            //e.printStackTrace();
            httpServletResponse.getWriter().print(objectMapper.writeValueAsString(new SimpleResponse(e.getMessage())));
        } else {
            //调用默认方法
            super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        }

    }
}
