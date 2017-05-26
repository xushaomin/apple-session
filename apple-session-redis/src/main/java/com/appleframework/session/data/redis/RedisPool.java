package com.appleframework.session.data.redis;

import redis.clients.jedis.Jedis;

public interface RedisPool {

	public Jedis getResource();
}
