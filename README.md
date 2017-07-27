参考#uncode-session
http://git.oschina.net/uncode/uncode-session

非常小巧的集群session共享组件，代码千行以内，避免使用应用容器插件的多种烦恼。

# 功能概述

1. 非常小巧的集群session公享组件，类似于spring-session。
2. 总代码不超过1000行。
3. 易于使用和扩展。

------------------------------------------------------------------------

# 配置

## 1. web.xml
	
	<!-- 会话共享过滤器，注意放在其他filter之前 -->
	<filter>
	    <filter-name>SessionSharingFilter</filter-name>
	    <filter-class>com.appleframework.session.SessionSharingFilter</filter-class>
	</filter>
	<filter-mapping>
	    <filter-name>SessionSharingFilter</filter-name>
	    <url-pattern>/*</url-pattern>
	</filter-mapping>

## 2. 基于Jedis的Spring配置
	
	<!-- 配置Redis缓存池（默认基于redis实现，所以只需要配置缓存池就可以了） -->
	<bean id="redisSentinelPool" class="com.appleframework.session.data.jedis.RedisSentinelPool">
		<property name="hosts" value="127.0.0.1:6379,127.0.0.1:6379" />
    	<property name="auth" value="123456" />
		<property name="maxIdle" value="5" />
		<property name="maxTotal" value="20" />
		<property name="maxWaitMillis" value="10000" />
		<property name="testOnBorrow" value="true" />
	</bean>
	
	<bean id="sessionCache" class="com.appleframework.session.data.memcached.MemcachedSessionCache">
		<property name="cachePool" ref="redisSentinelPool" />
	</bean>
	
## 3. 基于Memcached的Spring配置
	
	<bean id="memcachedPool" class="com.appleframework.session.data.memcached.MemcachedPool">
        <property name="hosts">
            <list>
                <value>127.0.0.1:11211</value>
            </list>
        </property>
    </bean>
    
    <bean id="sessionCache" class="com.appleframework.session.data.jedis.RedisSessionCache">
    	<property name="cachePool" ref="memcachedPool" />
	</bean>
	
## 4. 配置会话缓存管理器

	<bean id="sessionCacheManager" class="com.appleframework.session.data.SessionCacheManager">
		<property name="sessionCache" ref="sessionCache" />
	</bean>

------------------------------------------------------------------------
	
# 自定义扩展


## 1. 自定义实现类

	public class CustomSessionCache implements SessionCache{
	
		@Override
		public void put(String sessionId, SessionMap sessionMap, int timeout) {
		
		}
	
		@Override
		public SessionMap get(String sessionId) {
		
		}
	
		@Override
		public void setMaxInactiveInterval(String sessionId, int interval) {
		
		}
	
		@Override
		public void destroy(String sessionId) {
		
		}
	}
	

## 2. 配置管理器

	<!-- 配置缓存 -->
	<bean id="customSessionCache" class="com.appleframework.session.*.*.CustomSessionCache" />
	
	<!-- 配置会话缓存管理器 -->
	<bean id="sessionCacheManager" class="com.appleframework.session.data.SessionCacheManager">
		<property name="sessionCache" ref="customSessionCache" />
	</bean>

------------------------------------------------------------------------