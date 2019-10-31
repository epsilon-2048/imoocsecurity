package com.imooc.security.core.properties;


/**
 * 关于浏览器认证的有关配置属性类
 */
public class BrowserProperties {

    /**
     * 配置登陆页，提供默认值
     */
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**
     * 登陆后返回格式
     */
    private LoginResponseType loginResponseType = LoginResponseType.JSON;

    /**
     * rememberMe过期时间，秒为单位
     */
    private int rememberMeSeconds = 3600;

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginResponseType getLoginResponseType() {
        return loginResponseType;
    }

    public void setLoginResponseType(LoginResponseType loginResponseType) {
        this.loginResponseType = loginResponseType;
    }
}
