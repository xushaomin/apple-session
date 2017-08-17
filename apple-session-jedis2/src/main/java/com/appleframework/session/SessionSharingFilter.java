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
		/*String cookieDomain = p.getProperty("cookie.domain", "");
		String cookiePath = p.getProperty("cookie.path", "/");
		String cookieName = p.getProperty("cookie.name", SessionConfig.DEFAULT_COOKIENNAME);
		String cookiemaxAgeStr = p.getProperty("cookie.maxAge");
		int cookieMaxAge = SessionConfig.DEFAULT_COOKIEMAXAGE;
		if (cookiemaxAgeStr != null && !"".equals(cookiemaxAgeStr.trim())) {
			try {
				cookieMaxAge = Integer.valueOf(cookiemaxAgeStr);
			} catch (Exception e) {
			}
		}
		String sessionTimeoutStr = p.getProperty("session.timeout");
		int sessionTimeout = SessionConfig.DEFAULT_SESSION_TIMEOUT;
		if (sessionTimeoutStr != null && !"".equals(sessionTimeoutStr.trim())) {
			try {
				sessionTimeout = Integer.valueOf(sessionTimeoutStr);
			} catch (Exception e) {
			}
		}*/
		//SessionConfig config = SessionConfig.INSTANCE;
		SessionConfig.setContext(context);
				
		/*config.setCookieDomain(cookieDomain);
		config.setCookieMaxAge(cookieMaxAge);
		config.setCookieName(cookieName);
		config.setCookiePath(cookiePath);
		config.setSessionTimeout(sessionTimeout);*/
	}

}
