package com.imooc.wiremock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

//伪造数据提供前端使用
public class MockServer {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        configureFor(8062);
        //清空之前的配置，每次开服务使用最新的配置
        removeAllMappings();

        mock("/order/1","01");
        mock("/order/2","02");
    }
    //重构为一个私有方法,重构不是一步就成的，而是按需重构，一次一小步
    private static void mock(String url, String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+ file +".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray());
        stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));
    }
}
