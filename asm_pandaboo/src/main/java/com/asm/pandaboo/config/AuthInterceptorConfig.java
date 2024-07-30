package com.asm.pandaboo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.asm.pandaboo.components.AuthHandleInterceptor;

@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

	@Autowired
	AuthHandleInterceptor authHandleInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {		
		registry.addInterceptor(authHandleInterceptor)
		//.addPathPatterns("")
		.excludePathPatterns("/images/́́́́́́́**","/error/**","/login/**","/register","/logout"
				,"/images/css/**","/images/js/**","/images/fonts/**","/images/img/**","/images/scss/**");
	}
}
