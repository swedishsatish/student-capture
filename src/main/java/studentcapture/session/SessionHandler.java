package studentcapture.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SessionHandler allows the Spring server to set up user unique sessions. 
 * Only one session from each user is allowed. Sessions will timeout after 
 * 60 seconds (can be altered). Getting session data refreshes the session.
 * 
 * Add a session with the addSession method. Get session data with the 
 * getSessionData method. 
 * 
 * One Object is allowed to be stored for each session. The object will be 
 * lost of the session runs out.
 * 
 * I'll add further documentation later on.
 * 
 * @author tfy12hsm
 *
 */
@Repository
public class SessionHandler {
	private static final long TIMEOUT_LENGTH = 60000; // In milliseconds
	private static final String SESSION_KEY_ATTRIBUTE_STRING = "SessionKey";
	private static KeyUserTable keyUserTable = new KeyUserTable();
	private static Map<Integer, Session> sessions = new HashMap<>();
	private static volatile int nextSessionKey = 1;
	
	private Integer getNewSessionKey() {
		return nextSessionKey++;
	}
	
	private HttpSession getCurrentHttpSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	return attr.getRequest().getSession(true);
	}
	
	public synchronized boolean addSession(Integer userId, Object data) {
		return addSession(getCurrentHttpSession(), userId, data);
	}
	
	public synchronized boolean addSession(HttpSession httpSession, Integer userId, Object data) {
		if(getUserSession(userId).isPresent())
			return false;
		if(getSession(getSessionKey(httpSession)).isPresent())
			return false;
		
		try {
			Session session = new Session();
			session.data = data;
			updateSessionDuration(session);
			Integer sessionKey = getNewSessionKey();
			addSessionKey(httpSession, sessionKey);
			keyUserTable.add(sessionKey, userId);
			sessions.put(sessionKey, session);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public synchronized Optional<Object> getSessionData() {
		return getSessionData(getCurrentHttpSession());
	}
	
	public synchronized Optional<Object> getSessionData(HttpSession httpSession) {
		return getSessionData((Integer) httpSession.getAttribute(SESSION_KEY_ATTRIBUTE_STRING));
	}
	
	private Optional<Object> getSessionData(Integer sessionKey) {
		Optional<Session> session = getSession(sessionKey);
		
		if(session.isPresent())
			return Optional.of(session.get().data);
		return Optional.empty();
	}
	
	private Optional<Session> getSession(Integer sessionKey) {
		Session session = null;
		try {
			session = sessions.get(sessionKey);
			if(session == null)
				throw new NullPointerException();
			if(checkForTimeOut(session, sessionKey))
				throw new NullPointerException();
		} catch (NullPointerException e) {
			return Optional.empty();
		}
		
		return Optional.of(session);
	}
	
	private Optional<Session> getUserSession(Integer userId) {
		try {
			Integer sessionKey = keyUserTable.getKey(userId);
			if(sessionKey==null)
				throw new NullPointerException();
			
			return getSession(sessionKey);
		} catch (NullPointerException e) {
			return Optional.empty();
		}
	}
	
	public synchronized void removeSession(HttpSession httpSession) {
		removeSession(getSessionKey(httpSession));
		removeSessionKey(httpSession);
	}
	
	private void removeSession(Integer sessionKey) {
		sessions.remove(sessionKey);
		keyUserTable.removeWithKey(sessionKey);
	}
	
	public synchronized boolean removeUserSession(Integer userId) {
		try {
			Integer sessionKey = keyUserTable.getKey(userId);
			if(sessionKey==null)
				throw new NullPointerException();
			
			removeSession(sessionKey);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
		
	}
	
	private void updateSessionDuration(Session session) {
		session.lastUsed = System.currentTimeMillis();
	}
	
	private boolean checkForTimeOut(Session session, Integer sessionKey) {
		if(isTimedOut(session.lastUsed)) {
			removeSession(sessionKey);
			return true;
		} else {
			updateSessionDuration(session);
			return false;
		}
	}
	
	private boolean isTimedOut(long lastUsed) {
		return ((lastUsed+TIMEOUT_LENGTH) < System.currentTimeMillis());
	}
	
	private void addSessionKey(HttpSession httpSession, Integer sessionKey) {
		httpSession.setAttribute(SESSION_KEY_ATTRIBUTE_STRING, sessionKey);
	}
	
	private Integer getSessionKey(HttpSession httpSession) {
		return (Integer) httpSession.getAttribute(SESSION_KEY_ATTRIBUTE_STRING);
	}
	
	private void removeSessionKey(HttpSession httpSession) {
		httpSession.removeAttribute(SESSION_KEY_ATTRIBUTE_STRING);
	}
	
	private class Session {
		public long lastUsed;
		public Object data;
	}
}
