package com.appleframework.session.data.redisson;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.session.data.SessionCache;
import com.appleframework.session.data.SessionMap;

public class RedissonSessionCache implements SessionCache {

	private static final Logger LOG = LoggerFactory.getLogger(RedissonSessionCache.class);

	private RedissonClient redisson;

	@Override
	public void put(String sessionId, SessionMap sessionMap, int timeout) {
		try {
			redisson.getBucket(sessionId).set(sessionMap, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.error("Put session to redis error", e);
		}
	}

	@Override
	public SessionMap get(String sessionId) {
		SessionMap sessionMap = null;
		try {
			sessionMap = (SessionMap)redisson.getBucket(sessionId).get();
		} catch (Exception e) {
			LOG.error("Read session from redis error", e);
			return null;
		}
		return sessionMap;
	}

	@Override
	public void setMaxInactiveInterval(String sessionId, int interval) {
		try {
			redisson.getBucket(sessionId).expire(interval, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.error("Set session max inactive interval to redis error", e);
		}
	}

	@Override
	public void destroy(String sessionId) {
		try {
			redisson.getBucket(sessionId).delete();
		} catch (Exception e) {
			LOG.error("Destroy session from redis error", e);
		}
	}

	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}

}
