package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，当客户没有提供一个图形验证码生成器时，提供一个默认的图形验证码生成器
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired private SecurityProperties securityProperties;

    @Bean
    //这个注解会使spring容器在注册这个bean之前先检查容器中是否会有
    //这个imageCodeGenerator名字的bean，如果有则不注册
    //@ConditionalOnMissingBean(name = "imageCodeGenerator")
    //我觉得使用ValidateCodeGenerator.class更好
    //使用name属性，其他人提供自定义的图形验证码生成器时
    //就必须把组件name指定为imageCodeGenerator，否则就不生效
    //而使用value属性,其他人就只需将实现ValidateCodeGenerator接口的类注册为组件即可
    @ConditionalOnMissingBean(value = ValidateCodeGenerator.class)
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return  codeGenerator;
    }
}
