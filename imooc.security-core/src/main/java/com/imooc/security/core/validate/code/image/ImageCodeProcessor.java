package com.imooc.security.core.validate.code.image;

import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片验证码处理器
 */

@Component("imageCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor {

    /**
     * 发送图形验证码，将其写到响应中
     * @param request httpServletRequest、httpServletResponse的包装类
     * @param validateCode
     * @throws Exception
     */
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        ImageIO.write(((ImageCode)validateCode).getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}
