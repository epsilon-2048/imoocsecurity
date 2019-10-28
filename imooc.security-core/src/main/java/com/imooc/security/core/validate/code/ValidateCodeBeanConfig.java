package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.image.ImageCodeGenerator;
import com.imooc.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关bean注册配置类
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired private SecurityProperties securityProperties;

    /**
     * 当客户没有提供一个图形验证码生成器时，提供一个默认的图形验证码生成器
     * 当客户提供了一个名为imageCodeGenerator的组件后，将不注册默认图形验证码生成器
     */

    //@ConditionalOnMissingBean
    //这个注解会使spring容器在注册这个bean之前先检查容器中是否会有
    //这个imageCodeGenerator名字的bean，如果有则不注册
    //@ConditionalOnMissingBean(name = "imageCodeGenerator")
    //我觉得使用ValidateCodeGenerator.class更好
    //使用name属性，其他人提供自定义的图形验证码生成器时
    //就必须把组件name指定为imageCodeGenerator，否则就不生效
    //而使用value属性,其他人就只需将实现ValidateCodeGenerator接口的类注册为组件即可
    //@ConditionalOnMissingBean(value = ValidateCodeGenerator.class)
    //这里还是使用name属性吧，因为有多个实现了ValidateCodeGenerator的实现类，且功能不同
    //这里只要求当有自定义图形验证码生成器没有实现时，提供一个默认实现类，用value满足不了需求
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return  codeGenerator;
    }

    /**
     * 提供一个默认的短信发送器，只是在控制台打印，客户必须自己提供一个短信发送器
     * 实现SmsCodeSender接口
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }


}
