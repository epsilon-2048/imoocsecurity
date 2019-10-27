package com.imooc.security.core.properties;


/**
 * 关于浏览器认证的有关配置属性类
 */
public class BrowserProperties {

    /**
     * 配置登陆页，提供默认值
     */
    private String loginPage = "/imooc-signIn.html";

    private LoginType loginType = LoginType.JSON;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
