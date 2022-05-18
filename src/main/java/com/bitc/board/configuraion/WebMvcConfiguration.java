package com.bitc.board.configuraion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSizePerFile(5*1024*1024);
//        기본적으로 byte 단위로 계산이 이루어지므로 최대 크기를 5메가로 설정 하려면 5,000,000byte 이어야 하므로 5*1024*1024로 계산함
//        컴퓨터는 진수로 계산을 진행하므로 1000이 아니라 2의 10승인 1024로 계산해야함
        return commonsMultipartResolver;
    }
}
