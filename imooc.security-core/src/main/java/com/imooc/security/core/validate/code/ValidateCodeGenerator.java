package com.imooc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 图形验证码生成器接口，可实现此接口来生成自己定义的图形验证码
 */
public interface ValidateCodeGenerator {

    ImageCode generator(ServletWebRequest request);
}

