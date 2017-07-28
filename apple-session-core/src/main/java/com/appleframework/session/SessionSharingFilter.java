package com.appleframework.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.appleframework.session.data.SessionCacheManager;

public class SessionSharingFilter implements Filter {
	
	private ServletContext servletContext;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse HttpServletResponse = (HttpServletResponse) response;
		SessionHttpServletRequestWrapper sessionHttpServletRequestWrapper = new SessionHttpServletRequestWrapper(httpServletRequest, servletContext);
		try {
			chain.doFilter(sessionHttpServletRequestWrapper, HttpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sessionHttpServletRequestWrapper.isRequestedSessionIdValid()) {
				Cookie cookie = new Cookie("JSESSIONID", null);
				cookie.setMaxAge(0);
				HttpServletResponse.addCookie(cookie);
				HttpSession httpSession = sessionHttpServletRequestWrapper.getSession(false);
				if (httpSession != null) {
					SessionCacheManager.getSessionCache().setMaxInactiveInterval(httpSession.getId(), 0);
				}
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
