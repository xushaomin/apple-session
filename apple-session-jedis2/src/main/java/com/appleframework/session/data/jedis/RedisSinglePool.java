package com.appleframework.session.data.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.appleframework.session.Serialize;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

/**
 */
public class RedisSinglePool implements InitializingBean, DisposableBean, RedisPool {

	private JedisPool jedisPool;
	
	private String host;
	private int port = 6379;
	private String password;
	private int database = 0;

	private int maxIdle = 5;
	private int maxTotal = 20;
	private int maxWaitMillis = 10000;
	private boolean testOnBorrow = true;

	private int timeout = 2000;

	private String clientName;

	public String set(String key, Object value) {
		Jedis jedis = getResource();
		try {
			if (value == null) {
				jedis.del(keyToBytes(key));
				return null;
			} else {
				return jedis.set(keyToBytes(key), valueToBytes(value));
			}
		} finally {
			close(jedis);
		}
	}

	public String setex(String key, int seconds, Object value) {
		Jedis jedis = getResource();
		try {
			if (value == null) {
				jedis.del(keyToBytes(key));
				return null;
			}
			return jedis.setex(keyToBytes(key), seconds, valueToBytes(value));
		} finally {
			close(jedis);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		Jedis jedis = getResource();
		try {
			return (T) valueFromBytes(jedis.get(keyToBytes(key)));
		} finally {
			close(jedis);
		}
	}

	private byte[] keyToBytes(String key) {
		return SafeEncoder.encode(key);
	}

	private byte[] valueToBytes(Object object) {
		return Serialize.serialize(object);
	}

	private <T> T valueFromBytes(byte[] bytes) {
		if (bytes != null)
			return Serialize.deserialize(bytes);
		return null;
	}

	public Jedis getResource() {
		return jedisPool.getResource();
	}

	private void close(Jedis jedis) {
		jedis.close();
	}

	public void remove(String key) {
		Jedis jedis = getResource();
		try {
			jedis.del(keyToBytes(key));
		} finally {
			close(jedis);
		}
	}

	public void expire(String key, int expiredTime) {
		byte[] bkey = keyToBytes(key);
		Jedis jedis = getResource();
		try {
			jedis.expire(bkey, expiredTime);
		} finally {
			close(jedis);
		}
	}

	public void close() {
		jedisPool.close();
	}
	
	@Override
	public void destroy() throws Exception {
		jedisPool.close();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxWaitMillis(maxWaitMillis);
		poolConfig.setTestOnBorrow(testOnBorrow);
		jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database, clientName);
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}
