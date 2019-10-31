package com.imooc.security.core.properties;

/**
 * 图形验证码基本配置封装类
 */
public class ImageCodeProperties extends SmsCodeProperties {


    /**
     * 宽度像素
     */
    private int width = 67;

    /**
     * 高度像素
     */
    private int height = 23;

    public ImageCodeProperties() {
        setLength(4);  //设置默认长度
    }

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

}
