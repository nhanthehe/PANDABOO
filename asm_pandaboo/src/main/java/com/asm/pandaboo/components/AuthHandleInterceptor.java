package com.asm.pandaboo.components;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.asm.pandaboo.utils.Contants;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthHandleInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		if(request.getCookies() != null) {
			boolean check = false;
			for (Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals(Contants.COOKIE_UERNAME)) {
					check = true;
					break;
				}
			}

			if(check) {
				return true;
			}
		}
		response.sendRedirect(String.format("/login?path=%s%s"
				,request.getRequestURI()
				,request.getQueryString() != null ? request.getQueryString() : ""));
		return false;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("Post Handle: %s"+request.getRequestURI());
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("After Handle: %s"+request.getRequestURI());
	}
	
}
