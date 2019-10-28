package com.imooc.security.core.validate.code;

import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class ValidateCodeController {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     * @param request
     * @param response
     * @param type
     * @throws Exception
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {

        if (validateCodeProcessors.get(type+"CodeProcessor") != null)
            validateCodeProcessors.get(type+"CodeProcessor").create(new ServletWebRequest(request, response));
        else
            System.out.println("没有提供这种服务的处理器");
    }

    /*  //重构抽象出模板方法
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Autowired
    @Qualifier("imageCodeGenerator")
    private ValidateCodeGenerator imageCodeGenerator;

    @Autowired
    @Qualifier("smsCodeGenerator")
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;



    //------一般会把主干逻辑一样的方法抽出成为一个模板方法
    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {

    }*/
}
