package com.imooc.security.core.properties;

/**
 * 验证码封装类
 * 封装图形验证码和短信验证码
 */

public class ValidateCodeProperties {
    ImageCodeProperties image = new ImageCodeProperties();

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}
