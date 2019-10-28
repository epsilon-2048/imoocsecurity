package com.imooc.security.core.validate.code;

import java.time.LocalDateTime;

//验证码封装类
public class ValidateCode {

    //验证码
    private String code;

    //过期时间
    private LocalDateTime expireTime;


    public ValidateCode( String code, int expireIn) {

        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 是否过期
     * @return 过期返回true
     */
    public boolean isExpired() {
        return  getExpireTime().isBefore(LocalDateTime.now());
    }
}
