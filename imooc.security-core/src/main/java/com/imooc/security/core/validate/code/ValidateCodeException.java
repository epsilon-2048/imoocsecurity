package com.imooc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义图形验证码认证异常，继承自spring安全框架的异常基类
 */
public class ValidateCodeException extends AuthenticationException {

    public static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String explanation) {
        super(explanation);
    }
}
