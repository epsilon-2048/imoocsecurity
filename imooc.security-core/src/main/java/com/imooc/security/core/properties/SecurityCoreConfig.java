package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 如果一个配置类只配置@ConfigurationProperties注解，而没有使用@Component，
 * 那么在IOC容器中是获取不到properties 配置文件转化的bean。
 * 说白了 @EnableConfigurationProperties 相当于
 * 把使用 @ConfigurationProperties 的类进行了一次注入。
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
}
