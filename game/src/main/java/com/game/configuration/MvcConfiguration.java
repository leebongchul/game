package com.game.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.game.interceptor.BlockInterceptor;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer  {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        LoginIntercepter loginIntercepter = ;
        registry.addInterceptor(new BlockInterceptor())
                .addPathPatterns("/member/login")
//                .excludePathPatterns();
        .excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");

    }

    
}
