package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义过滤器来校验图形、短信验证码，
 * 继承自 OncePerRequestFilter ,spring提供的工具类，保证该过滤器只执行一次
 * 实现InitializingBean，目的是初始化urls
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 校验码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统配置信息
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();


    /**
     * 主要用来做类URLs字符串匹配的工具类；
     */

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        //需要验证码的url集合
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM + ":POST", ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_PHONE + ":POST", ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
       // urlMap.keySet().forEach(System.out::println);
        Set<String> urls = urlMap.keySet();
       /* for (String url : urls) {
            System.out.println(url + "=" + urlMap.get(url));
        }*/
    }

    /**
     * 将系统中配置的需要校验验证码的URL根据校验的类型放入map
     * key的格式如：user/*:POST
     * 使用这种做法会将相同的url给覆盖掉，也就是当短信验证和图形验证的（url：请求方式）相同时
     * 短信验证的会覆盖掉图形验证，意思是短信验证和图形验证不能共存与同一条（url：请求方式）
     * 例如短信的user/*:POST会覆盖掉图形的user/*:POST，但不会覆盖user/*:*
     * 但当匹配的有url/*:* 和 url/*:POST，优先使用url/*:POST的所对应的ValidateCodeType
     * @param urlString
     * @param type
     */
    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        String urlPattern = "%s:%s";
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                url = String.format(urlPattern,
                        //type.toString(),
                        StringUtils.substringBeforeLast(url,":"),
                        StringUtils.upperCase(StringUtils.substringAfterLast(url,":")));
                urlMap.put(url, type);
            }
        }
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //请求是否需验证码验证，通过匹配配置的url
        ValidateCodeType type = getValidateCodeType(httpServletRequest);
        if (type != null) {
            logger.info("校验请求(" + httpServletRequest.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(httpServletRequest, httpServletResponse));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        String urlAndMethod = "%s:%s";
        //这个判断不合理, 如何获取type?
       // if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
        Set<String> urls = urlMap.keySet();
        for (String url : urls) {
            urlAndMethod = String.format(urlAndMethod,request.getRequestURI(),StringUtils.upperCase(request.getMethod()));
            if (pathMatcher.match(url, urlAndMethod)) {
                //如果匹配成功，且不为通配符，则直接返回
                //即如果有匹配到url/*:* 和 url/*:POST，优先返回url/*:POST
                if (!StringUtils.substringAfterLast(url,":").equals("*")){
                    result = urlMap.get(url);
                    return result;
                }
                result = urlMap.get(url);
            }
        }
       // }
        return result;
    }

    /*private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        //获取系统为该请求生成的验证码封装类
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,
                ValidateCodeProcessor.SESSION_KEY_PREFIX +"IMAGE");

        //获取请求中的验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            //移除系统为该请求生成的验证码封装类
            sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY_PREFIX +"IMAGE");
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        //移除系统为该请求生成的验证码封装类
        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX +"IMAGE");
    }
    */

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String url = "uer/**/usr/*:post";
        System.out.println(StringUtils.substringAfterLast("uer/**/usr:/*:post",":"));
        url = StringUtils.substringBeforeLast(url,":") + ":" + StringUtils.upperCase(StringUtils.substringAfterLast(url,":"));
        System.out.println(url);
        System.out.println(pathMatcher.match("SSM@uer/**/usr/*:get","SSM@uer/user/usr/1:post"));
        String urlPattern = "%s@%s:%s";
        url = String.format(urlPattern,
                "SMS",
                StringUtils.substringBeforeLast(url,":"),
                StringUtils.upperCase(StringUtils.substringAfterLast(url,":")));
        System.out.println(url);
    }

}

