package com.appleframework.session.data.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

	public void setPort(int port) {
		this.port = port;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabase(Integer database) {
		this.database = database;
	}

	public Jedis getResource() {
		if (jedisPool != null)
			return jedisPool.getResource();
		return null;
	}

	@SuppressWarnings("deprecation")
	public void returnResource(final Jedis jedis) {
		if (jedis != null) {
			if (jedisPool != null)
				jedisPool.returnResource(jedis);
		}
	}

	@Override
	public void destroy() throws Exception {
		if (jedisPool != null) {
			jedisPool.close();
		}
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

}
