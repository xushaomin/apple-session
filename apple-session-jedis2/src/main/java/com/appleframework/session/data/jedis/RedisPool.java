package com.appleframework.session.data.jedis;

import redis.clients.jedis.Jedis;

public interface RedisPool {

	public Jedis getResource();
	
	public String set(String key, Object value);

	public String setex(String key, int seconds, Object value);

	public <T> T get(String key);
	
	public void remove(String key);

	public void expire(String key, int expiredTime);

	public void close();
}
