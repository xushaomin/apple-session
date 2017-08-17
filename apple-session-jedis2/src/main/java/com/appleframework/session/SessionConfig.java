package com.appleframework.session;

import javax.servlet.ServletContext;

public class SessionConfig {

	public static final String DEFAULT_COOKIE_NAME = "APPLE_SESSIONID";
	
	public static final String DEFAULT_COOKIE_PATH = "/";
	
	public static final String DEFAULT_COOKIE_DOMAIN = "";

	public static final int DEFAULT_COOKIE_MAX_AGE = -1;

	public static final int DEFAULT_SESSION_TIMEOUT = 1800;

	private static String cookieDomain = DEFAULT_COOKIE_DOMAIN;

	private static String cookieName = DEFAULT_COOKIE_NAME;

	private static int cookieMaxAge = DEFAULT_COOKIE_MAX_AGE;

	private static String cookiePath = DEFAULT_COOKIE_PATH;

	private static int sessionTimeout = DEFAULT_SESSION_TIMEOUT;

	private static ServletContext context;

	public static String getCookieDomain() {
		return cookieDomain;
	}

	public static void setCookieDomain(String cookieDomain) {
		SessionConfig.cookieDomain = cookieDomain;
	}

	public static String getCookieName() {
		return cookieName;
	}

	public static void setCookieName(String cookieName) {
		SessionConfig.cookieName = cookieName;
	}

	public static int getCookieMaxAge() {
		return cookieMaxAge;
	}

	public static void setCookieMaxAge(int cookieMaxAge) {
		SessionConfig.cookieMaxAge = cookieMaxAge;
	}

	public static String getCookiePath() {
		return cookiePath;
	}

	public static void setCookiePath(String cookiePath) {
		SessionConfig.cookiePath = cookiePath;
	}

	public static int getSessionTimeout() {
		return sessionTimeout;
	}

	public static void setSessionTimeout(int sessionTimeout) {
		SessionConfig.sessionTimeout = sessionTimeout;
	}

	public static ServletContext getContext() {
		return context;
	}

	public static void setContext(ServletContext context) {
		SessionConfig.context = context;
	}

}
