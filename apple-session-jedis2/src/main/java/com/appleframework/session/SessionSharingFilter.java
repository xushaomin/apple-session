package com.appleframework.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appleframework.session.data.SessionCache;
import com.appleframework.session.data.SessionCacheManager;

public class SessionSharingFilter implements Filter {

	private ServletContext context;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();
		initConfig();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(new SessionHttpServletRequestWrapper((HttpServletRequest) request, (HttpServletResponse) response),
				response);
	}

	@Override
	public void destroy() {
		SessionCache sessionCache = SessionCacheManager.getSessionCache();
		if (sessionCache != null) {
			sessionCache.close();
		}
	}

	private void initConfig() {
		SessionConfig.setContext(context);
	}

}
