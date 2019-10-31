package com.imooc.security.browser;

import com.imooc.security.browser.support.SimpleResponse;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户访问网站，打开了一个链接(origin url)。
     * 请求发送给服务器，服务器判断用户请求了受保护的资源。
     * 由于用户没有登录，服务器重定向到登录页面
     * 填写表单，点击登录
     * 浏览器将用户名密码以表单形式发送给服务器
     * 服务器验证用户名密码。成功，进入到下一步。否则要求用户重新认证（第三步）
     * 服务器对用户拥有的权限（角色）判定: 有权限，重定向到origin url; 权限不足，返回状态码403("forbidden").
     *
     * requestCache用来缓存origin url
     */
    private RequestCache requestCache = new HttpSessionRequestCache();
    /**
     * 负责 Spring Security 框架内的所有重定向的 RedirectStrategy ，
     * 将请求重定向到指定的URL
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired private SecurityProperties securityProperties;

    /**
     * 当需要身份认证时，跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse RequireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是：" +  targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl,".html")) {
                redirectStrategy.sendRedirect(request,response,securityProperties.getBrowser().getLoginPage());
            }
        }

        return new SimpleResponse("访问的服务器需要身份认证，请引导用户到登录页");
    }
}
