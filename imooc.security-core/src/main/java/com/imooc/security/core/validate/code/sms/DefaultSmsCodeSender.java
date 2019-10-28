package com.imooc.security.core.validate.code.sms;

import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送手机验证码的默认实现类
 */
public class DefaultSmsCodeSender implements SmsCodeSender {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void send(String phone, ValidateCode code) {
        logger.info("已成功发送至手机：" + phone + ",验证码：" + code.getCode());
        //System.out.println("已成功发送至手机：" + phone + ",验证码：" + code.getCode());
    }
}
