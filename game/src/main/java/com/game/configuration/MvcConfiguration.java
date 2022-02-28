package com.game.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.game.interceptor.BlockInterceptor;

@Configuration
//신규코드
//@EnableWebMvc
//@ComponentScan
//@Slf4j
public class MvcConfiguration implements WebMvcConfigurer {

	@Autowired
	BlockInterceptor blockInterceptor;

	// 신규코드
	/*
	 * @Bean public BlockInterceptor blockInterceptor() { return new
	 * BlockInterceptor(); }
	 */
//    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//        LoginIntercepter loginIntercepter = ;
//      기존코드
//        registry.addInterceptor(new BlockInterceptor()) 
		// 신규코드
		registry.addInterceptor(blockInterceptor).addPathPatterns("/member/login", "/member/kakaologin")
//                .excludePathPatterns();
				.excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");

	}

}
