package cn.com.dhcc.creditquery.person.queryweb.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.RedissonUtil;


public class RedisSessionDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	/** 
     * shiro-redis的session对象前缀 
     */  
//    private RedisManager redisManager;  
    private static RedissonUtil redisson ;
    private static RedissonClient redissonClient = redisson.getLocalRedisson(); 
    /** 
     * The Redis key prefix for the sessions  
     */  
    private String keyPrefix = "shiro_redis_session:";  
    private static final String REDIS_EXPIRE = "redis.expire";
      
    private static String SESSION_KEY = "shiro_redis_session";

	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}

	/**
	 * save session
	 * 
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void saveSession(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			logger.error("session or session id is null");
			return;
		}
		String strKey = getStrKey(session.getId());
		Serializable sessionId = session.getId();
		 byte[] value = Serialize.serialize(session);
		Properties prop = RedissonUtil.getProperties();
		String expire = prop.getProperty(REDIS_EXPIRE);
		// session.setTimeout(property);
		// session.setTimeout(redisManager.getExpire()*1000);
		// this.redisManager.set(key, value, redisManager.getExpire()); 
		RBucket<Box> bucket = redissonClient.getBucket(keyPrefix + sessionId);
//		RMap<String,box> sessionMap = redissonClient.getMap(SESSION_KEY);
		Box box = new Box();
		box.setSession(value);
		bucket.set(box);
		bucket.expire(Long.parseLong(expire), TimeUnit.SECONDS);
//		sessionMap.put(strKey,box);
//		sessionMap.expire(Long.parseLong(expire), TimeUnit.SECONDS);
		// this.redissonClient.getBucket(key).expire(20, TimeUnit.MINUTES);
	}

	@Override
	public void delete(Session session) {
		Serializable sessionId = session.getId();
		if (session == null || sessionId == null) {
			logger.error("session or session id is null");
			return;
		}
		redissonClient.getBucket(keyPrefix+sessionId).delete();
		// redisManager.del(this.getByteKey(session.getId()));

	}

	// 用来统计当前活动的session
	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		Collection<String> findKeysByPattern = redissonClient.getKeys().findKeysByPattern(this.keyPrefix + "*");
		Iterator<String> iterator = findKeysByPattern.iterator();
		if(iterator.hasNext()){
			String str = iterator.next();
			RBucket<Box> bucket = redissonClient.getBucket(str);
			Box box = bucket.get();
			byte[] sessionByte = box.getSession();
			Session session =(Session)Serialize.deserialize(sessionByte);
			 sessions.add(session);
		}
		/*Set<Session> sessions = new HashSet<Session>();  
        
        Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");  
        if(keys != null && keys.size()>0){  
            for(byte[] key:keys){  
                Session s = (Session)SerializeUtils.deserialize(redisManager.get(key));
                Session s = (Session)Serialize.deserialize(redisManager.get(key));  
                sessions.add(s);  
            }  
        }  */
		
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		}
		RBucket<Box> bucket = redissonClient.getBucket(keyPrefix + sessionId);
		Box box = bucket.get();
//		String strKey = getStrKey(sessionId);
//		RMap<String,box> map = redissonClient.getMap("preKey"+sessionId);
//		box box = map.get(strKey);
		if(box == null){
			return null;
		}
		byte[] s = box.getSession();
		
		Session session = (Session)Serialize.deserialize(s);
//		Session s = (Session) object;
		// Session s = (Session)Serialize.deserialize2(b);
		// Session s =
		// (Session)Serialize.deserialize(redisManager.get(this.getByteKey(sessionId)));
		/*
		 * Session s =
		 * (Session)SerializeUtils.deserialize(redisManager.get(this.getByteKey(
		 * sessionId)));
		 */
		return session;
	}

	/**
	 * 获得byte[]型的key
	 * 
	 * @param sessionId
	 * @return
	 */
	private String getStrKey(Serializable sessionId) {
		String preKey = this.keyPrefix + sessionId;
		return preKey;
	}

	// public RedisManager getRedisManager() {
	// return redisManager;
	// }
	//
	// public void setRedisManager(RedisManager redisManager) {
	// this.redisManager = redisManager;

	/**
	 * 初始化redisManager
	 */
	// this.redisManager.init();
	// }

	public static RedissonUtil getRedisson() {
		return redisson;
	}

	public static void setRedisson(RedissonUtil redisson) {
		RedisSessionDAO.redisson = redisson;
	}

	/**
	 * Returns the Redis session keys prefix.
	 * 
	 * @return The prefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * Sets the Redis sessions key prefix.
	 * 
	 * @param keyPrefix
	 *            The prefix
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

}

class Box {

	private byte[] session;

	public byte[] getSession() {
		return session;
	}

	public void setSession(byte[] session) {
		this.session = session;
	}
}
