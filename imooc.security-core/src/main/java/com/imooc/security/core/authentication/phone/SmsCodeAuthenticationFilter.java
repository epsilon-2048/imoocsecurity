package com.imooc.security.core.authentication.phone;

import com.imooc.security.core.properties.SecurityConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信验证过滤器
 */

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 请求参数名
     */
    private String phoneParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PHONE;
    //是否只处理post请求
    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        //处理/authentication/phone的post请求
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_PHONE, "POST"));
    }

    //认证流程
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String phone = this.obtainPhone(request);
            if (phone == null) {
                phone = "";
            }

            phone = phone.trim();
            //new 一个未认证的token
            SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phone);
            //把请求信息设置到token
            this.setDetails(request, authRequest);
            //把token传入manager，进行认证
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }


    /**
     * 获取手机号
     * @param request
     * @return
     */
    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(this.phoneParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneParameter(String usernameParameter) {
        Assert.hasText(phoneParameter, "phone parameter must not be empty or null");
        this.phoneParameter = phoneParameter;
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getPhoneParameter() {
        return this.phoneParameter;
    }


}
