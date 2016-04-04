package com.appleframework.session.data.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class RedisSentinelPool implements InitializingBean, DisposableBean {

	private JedisSentinelPool jedisSentinelPool;

	private Set<String> hostSet = new HashSet<String>();;
	private String auth;
	private int maxIdle = 5;
	private int maxTotal = 20;
	private int maxWaitMillis = 10000;
	private boolean testOnBorrow = true;
	private String name = "redis_master";

	public void setHosts(String hosts) {
		String[] hostss = hosts.split(",");
		for (String host : hostss) {
			hostSet.add(host);
		}
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public void setName(String name) {
		if (StringUtils.isNotEmpty(name)) {
			this.name = name;
		}
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

	public Jedis getResource() {
		if (jedisSentinelPool != null)
			return jedisSentinelPool.getResource();
		return null;
	}

	@SuppressWarnings("deprecation")
	public void returnResource(final Jedis jedis) {
		if (jedis != null) {
			if (jedisSentinelPool != null)
				jedisSentinelPool.returnResource(jedis);
		}
	}

	public String getCurrentHostMaster() {
		if (jedisSentinelPool != null) {
			return jedisSentinelPool.getCurrentHostMaster().toString();
		}
		return "";
	}

	@Override
	public void destroy() throws Exception {
		if (jedisSentinelPool != null) {
			jedisSentinelPool.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxWaitMillis(maxWaitMillis);
		poolConfig.setTestOnBorrow(testOnBorrow);
		jedisSentinelPool = new JedisSentinelPool(name, hostSet, poolConfig, auth);
	}

}
