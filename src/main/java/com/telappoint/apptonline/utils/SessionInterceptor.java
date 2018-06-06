package com.telappoint.apptonline.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Murali
 * 
 */

public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler)  throws Exception {		
		HandlerMethod method = (HandlerMethod) handler;	
		if(!method.getMethod().getName().equals("getLandingPage") && !method.getMethod().getName().equals("sessionExpired")
				&& !method.getMethod().getName().equals("clearCache")){
			HttpSession session = request.getSession(false);
			if(session == null) {
				response.sendRedirect(request.getContextPath()+"/session-expired.html");
				//request.getRequestDispatcher("/sessionExpired.html").forward(request, response);
				return false;
			}
		}		
		return true;
	}
}