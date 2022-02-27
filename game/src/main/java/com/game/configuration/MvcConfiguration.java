package com.game.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.game.interceptor.BlockInterceptor;

import groovy.util.logging.Slf4j;
//신규코드
//@Slf4j
@Configuration
//신규코드
//@EnableWebMvc  
public class MvcConfiguration implements WebMvcConfigurer  {

    //신규코드
//  @Bean
//  public BlockInterceptor blockInterceptor() {
//      return new BlockInterceptor();
// }
//    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        LoginIntercepter loginIntercepter = ;
//      기존코드
        registry.addInterceptor(new BlockInterceptor()) 
      //신규코드
//        registry.addInterceptor(blockInterceptor())
                .addPathPatterns("/member/login")
//                .excludePathPatterns();
        .excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");
    
      
    
 }
  //신규코드
//    @Bean
//    public BlockInterceptor blockInterceptor() {
//        return new BlockInterceptor();
//    }

}
