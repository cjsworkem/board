package com.bitc.board.configuraion;

import com.bitc.board.intercepter.BoardLoginCheck;
import com.bitc.board.intercepter.LoginCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    @Value("${user.resource.location}")
    private String userLoc;

    @Value("${user.resource.path}")
    private String userPath;
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        addResourceHandler() 스프링프레임워크 내에서 사용할 폴더명
//        addResourceLocations() 실제 디스크의 파일 폴더 위치
//        리눅스 및 유닉스에서는 file:/폴더명/
//        윈도우에서는 file:///C:/폴더명/
//        registry 에 여러개의 외부 폴더를 추가 할 수있음
        registry.addResourceHandler("/img/**").addResourceLocations("file:///C:/images/").setCachePeriod(20);
        registry.addResourceHandler(userLoc+"**").addResourceLocations("file:"+userPath).setCachePeriod(20);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        addPathPatterns() : 인터셉터가 동작될 컨트롤러 지정
//        excludePathPatterns() : 인터셉터를 제외할 컨트롤러 지정
        registry.addInterceptor(new LoginCheck())
                .addPathPatterns("/login/*")
                .excludePathPatterns("/login/login")
                .excludePathPatterns("/login/loginFail")
                .excludePathPatterns("/login/loginCheck");

        registry.addInterceptor(new BoardLoginCheck())
                .addPathPatterns("/bsBoard/*")
                .excludePathPatterns("/bsBoard/login")
                .excludePathPatterns("/bsBoard/loginCheck")
                .excludePathPatterns("/bsBoard/loginFail")
                .excludePathPatterns("/bsBoard/bsBoardList.do")
                .excludePathPatterns("/bsBoard/isLoginFalse");



    }
}
