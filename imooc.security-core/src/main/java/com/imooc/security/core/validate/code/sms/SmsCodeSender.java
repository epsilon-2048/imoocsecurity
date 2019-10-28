package com.imooc.security.core.validate.code.sms;

import com.imooc.security.core.validate.code.ValidateCode;

/**
 * 发送手机验证码接口
 */
public interface SmsCodeSender {
    void send(String phone, ValidateCode code);
}
