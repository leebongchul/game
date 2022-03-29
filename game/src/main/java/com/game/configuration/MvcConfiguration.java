package com.game.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.game.interceptor.BlockInterceptor;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

	@Autowired
	BlockInterceptor blockInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(blockInterceptor).addPathPatterns("/member/login", "/member/kakaologin")
				.excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");

	}

}
