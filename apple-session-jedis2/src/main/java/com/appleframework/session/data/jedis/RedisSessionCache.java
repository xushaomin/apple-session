package com.appleframework.session.data.jedis;

import com.appleframework.session.data.SessionCache;

/**
 * redis储存
 * @author cruise.xu
 *
 */
public class RedisSessionCache implements SessionCache {

	private RedisPool cachePool;

	public void setCachePool(RedisPool cachePool) {
		this.cachePool = cachePool;
	}

	@Override
	public void close() {
		cachePool.close();
	}

	@Override
	public void put(String key, Object value, int expiredTime) {
		if (expiredTime <= 0) {
			cachePool.set(key, value);
		} else {
			cachePool.setex(key, expiredTime, value);
		}
	}

	@Override
	public <T> T get(String key) {
		return cachePool.get(key);
	}

	@Override
	public void remove(String key) {
		cachePool.remove(key);
	}

	@Override
	public void expire(String key, int expiredTime) {
		cachePool.expire(key, expiredTime);
	}

}
