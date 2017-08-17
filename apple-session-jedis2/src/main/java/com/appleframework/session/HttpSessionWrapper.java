package com.appleframework.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.appleframework.session.data.SessionCache;
import com.appleframework.session.data.SessionCacheManager;

/**
 * HttpSession实现
 * @author cruise.xu
 */
@SuppressWarnings("deprecation")
public class HttpSessionWrapper implements HttpSession {
	
	private final SessionCache sessionCache = SessionCacheManager.getSessionCache();
	private final String cookieName = SessionConfig.getCookieName();
	private int maxAlive = SessionConfig.getSessionTimeout();

	private final String id;
	private final String session_key;
	private Session session;
	private boolean invalid = false;

	public HttpSessionWrapper(String id, int maxAlive) {
		this.id = id;
		this.maxAlive = maxAlive;
		this.session_key = cookieName + ":" + id;
		session = sessionCache.get(session_key);
		if (session == null) {
			initSession();
			session.setLastAccessTime(System.currentTimeMillis());
			sessionCache.put(session_key, session, maxAlive);
		} else {
			if (session.isNew()) {
				session.setNew(false);
			}
			sessionCache.expire(session_key, maxAlive);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getCreationTime() {
		return session.getCreateTime();
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessTime();
	}

	@Override
	public ServletContext getServletContext() {
		return SessionConfig.getContext();
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		// ignore
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxAlive;
	}

	@Override
	public Object getAttribute(String attributeName) {
		checkSessionInvalild();
		Serializable attribute = session.getAttribute(attributeName);
		return attribute;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		checkSessionInvalild();
		Set<String> attributeNameSet = session.getAttributeNames();
		return new Enumerator(attributeNameSet);
	}

	@Override
	public void setAttribute(String attributeName, Object attributeValue) {
		checkSessionInvalild();
		checkSerializable(attributeValue);
		session.putAttribute(attributeName, (Serializable) attributeValue);
		sessionCache.put(session_key, session, maxAlive);
	}

	@Override
	public void removeAttribute(String attributeName) {
		checkSessionInvalild();
		session.removeAttribute(attributeName);
		if (session.isEmpaty()) {
			sessionCache.remove(session_key);
		} else {
			sessionCache.put(session_key, session, maxAlive);
		}
	}

	@Override
	public void invalidate() {
		invalid = true;
		sessionCache.remove(session_key);
	}

	@Override
	public boolean isNew() {
		checkSessionInvalild();
		return session.isNew();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HttpSessionWrapper other = (HttpSessionWrapper) obj;
		if ((id == null) ? (other.getId() != null) : !id.equals(other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Session id is ");
		sb.append(this.id);
		sb.append(" ,detail is ");
		sb.append(session);
		return sb.toString();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException("Not supported .");
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public String[] getValueNames() {
		Enumeration<String> attributeNames = getAttributeNames();
		List<String> attributeNameList = new ArrayList<String>();
		while (attributeNames.hasMoreElements()) {
			attributeNameList.add((String) attributeNames.nextElement());
		}
		return attributeNameList.toArray(new String[0]);
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	/**
	 * 初始化一个新的Session基本信息。
	 */
	private void initSession() {
		session = new Session();
		session.setNew(true);
	}

	private void checkSerializable(Object value) {
		if (value == null) {
			throw new NullPointerException();
		}
		if (!Serializable.class.isInstance(value)) {
			throw new RuntimeException(value.getClass().getName() + " NotSerializable  ");
		}
	}

	/**
	 * 判断当前Session是否已经失效.
	 * 
	 * @throws IllegalStateException
	 *             Session已经失效的异常.
	 */
	private void checkSessionInvalild() {
		if (invalid) {
			throw new IllegalStateException("Session is invalid.");
		}
	}

	private static class Enumerator implements Enumeration<String> {
		
		private Iterator<String> iter;

		public Enumerator(Set<String> attributeNames) {
			this.iter = attributeNames.iterator();
		}

		@Override
		public boolean hasMoreElements() {
			return iter.hasNext();
		}

		@Override
		public String nextElement() {
			return iter.next();
		}
	}

}