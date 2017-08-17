package com.appleframework.session.data;

public class SessionCacheManager {

	private static SessionCache sessionCache;

	public static SessionCache getSessionCache() {
		return sessionCache;
	}

	public void setSessionCache(SessionCache sessionCache) {
		SessionCacheManager.sessionCache = sessionCache;
	}

}
