package com.imooc.security.core.properties;

/**
 * 验证码封装类
 * 封装图形验证码和短信验证码
 */

public class ValidateCodeProperties {

    //图形验证码
    private ImageCodeProperties image = new ImageCodeProperties();

    //短信验证码
    private SmsCodeProperties sms = new SmsCodeProperties();

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }

    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }
}
