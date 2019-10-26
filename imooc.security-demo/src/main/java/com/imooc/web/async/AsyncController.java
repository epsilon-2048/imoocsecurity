package com.imooc.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;


@RestController
public class AsyncController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired MockQueue mockQueue;
    @Autowired DeferredResultHolder deferredResultHolder;
    @RequestMapping("/order")
    public DeferredResult<String> order() throws Exception {

        logger.info("主线程开始");

        //在实际应用中，runable异步处理并不能满足需求
        //大多会使用DeferredResult异步处理rest服务
      /*  Callable<String> result = () -> {
            logger.info("副线程开始");
            Thread.sleep(1000);
            logger.info("副线程结束");
            return "success";
        };*/

        String orderNumber = RandomStringUtils.randomNumeric(8);
        mockQueue.setPlaceOrder(orderNumber);
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber,result);
        logger.info("主线程放回");
        return result;
    }
}
