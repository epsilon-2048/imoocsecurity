package com.imooc.security.core.properties;

/**
 * 图形验证码基本配置封装类
 */
public class SmsCodeProperties {

    //验证码个数
    private int length = 6;

    //过期时间
    private int expireIn = 60;

    //指定哪些url需要验证 以逗号分隔
    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
