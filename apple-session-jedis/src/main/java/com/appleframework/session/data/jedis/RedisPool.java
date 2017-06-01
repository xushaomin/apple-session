package com.appleframework.session.data.jedis;

import redis.clients.jedis.Jedis;

public interface RedisPool {

	public Jedis getResource();
}
