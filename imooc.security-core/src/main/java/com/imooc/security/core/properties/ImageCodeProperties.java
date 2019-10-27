package com.imooc.security.core.properties;

/**
 * 图形验证码基本配置封装类
 */
public class ImageCodeProperties {

    //宽度像素
    private int width = 67;
    //高度像素
    private int height = 23;
    //验证码个数
    private int length = 4;
    //过期时间
    private int expireIn = 60;

    //指定哪些url需要图形验证 以逗号分隔
    private String url;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
