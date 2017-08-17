package com.appleframework.session;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author cruise.xu
 *
 */
public class SessionHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private HttpSessionWrapper session;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public SessionHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
		super(request);
		this.request = request;
		this.response = response;
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean create) {
		return doGetSession(create);
	}

	private HttpSession doGetSession(boolean create) {
		if (session == null) {
			Cookie cookie = null;
			Cookie[] cookies = super.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (c.getName().equals(SessionConfig.getCookieName())) {
						cookie = c;
						break;
					}
				}
			}
			if (cookie != null) {
				String sessionId = cookie.getValue();
				session = buildSession(sessionId, false);
			} else {
				session = buildSession();
			}
		}
		return session;
	}

	private HttpSessionWrapper buildSession(String sessionId, boolean create) {
		int maxAlive = request.getSession().getMaxInactiveInterval();
		HttpSessionWrapper session = new HttpSessionWrapper(sessionId, maxAlive);
		if (create) {
			addCookie(sessionId);
		}
		return session;
	}

	private HttpSessionWrapper buildSession() {
		String sessionId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		return buildSession(sessionId, true);
	}

	private void addCookie(String sessionId) {
		Cookie cookie = new Cookie(SessionConfig.getCookieName(), sessionId);
		cookie.setMaxAge(SessionConfig.getCookieMaxAge());
		cookie.setSecure(request.isSecure());
		cookie.setPath(SessionConfig.getCookiePath());
		String cookieDomain = SessionConfig.getCookieDomain();
		if (cookieDomain != null && !"".equals(cookieDomain.trim())) {
			cookie.setDomain(cookieDomain);
		}
		response.addCookie(cookie);
	}
}
