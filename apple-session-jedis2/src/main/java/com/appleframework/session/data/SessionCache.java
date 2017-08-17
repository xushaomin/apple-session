package com.appleframework.session.data;

public interface SessionCache {
	
	public void close();

	public void put(String key, Object value, int expiredTime);

	public <T> T get(String key);

	public void remove(String key);

	public void expire(String session_key, int expiredTime);

}