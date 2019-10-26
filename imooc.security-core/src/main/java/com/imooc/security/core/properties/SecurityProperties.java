package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 解析配置文件并（注入）绑定属性
 */
@ConfigurationProperties(
        prefix = "imooc.security"
)
public class SecurityProperties {

    /**
     * 认证浏览器相关配置类
     */
    private BrowserProperties browser = new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
