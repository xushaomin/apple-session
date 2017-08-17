package com.appleframework.session;

import java.util.Properties;

import com.appleframework.session.data.SessionCache;

public interface Config {

	public int setSessionTimeout();

	public String setCookieName();

	public int setCookieMaxAge();

	public String setCookieDomain();

	public String setCookiePath();

	public Properties setStorageProperties();

	public SessionCache setStorage();

}