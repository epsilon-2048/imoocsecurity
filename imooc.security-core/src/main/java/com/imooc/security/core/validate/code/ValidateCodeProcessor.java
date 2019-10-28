package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器,封装不同验证码的处理逻辑
 */
public interface ValidateCodeProcessor {
    /**
     * 验证码放入session时的 前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_PREFIX_FOR_CODE_";

    /**
     * 创建校验码
     * @param request spring对于request，response的包装类
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param request
     * @throws Exception
     */
   // void validate(ServletWebRequest request) throws Exception;


}
