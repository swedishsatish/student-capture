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
 * These sessions are only stored locally so they're not appropriate for 
 * larger systems. Only one session from each user is allowed. Sessions 
 * will timeout after 60 seconds (can be altered). Getting session data 
 * refreshes the session.
 * <br>
 * Add a session with the addSession method. Get session data with the 
 * getSessionData method. Methods without {@link HttpSession} as a parameter
 * uses the current threads httpsession.
 * <br>
 * One Object is allowed to be stored for each session. The object will be 
 * lost of the session runs out.
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
	
	/**
	 * Returns a unique session key.
	 * 
	 * @return		session key
	 */
	private Integer getNewSessionKey() {
		return nextSessionKey++;
	}
	
	/**
	 * Returns current threads {@link HttpSession}.
	 * 
	 * @return		current threads session
	 */
	private HttpSession getCurrentHttpSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	return attr.getRequest().getSession(true);
	}
	
	/**
	 * Adds current session with stored session data.
	 * 
	 * @param userId	user identifier
	 * @param data		session data
	 * @return			true if successful, false if user already has a session 
	 */
	public synchronized boolean addSession(Integer userId, Object data) {
		return addSession(getCurrentHttpSession(), userId, data);
	}
	
	/**
	 * Adds a session with stored session data.
	 * 
	 * @param httpSession	session to store session data on
	 * @param userId		user identifier
	 * @param data			session data
	 * @return				true if successful, false if user already has a 
	 * 						session
	 */
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
	
	/**
	 * Updates session data for current session.
	 * 
	 * @param data		new session data
	 * @return			true if successful, else false
	 */
	public synchronized boolean setSessionData(Object data) {
		return setSessionData(getCurrentHttpSession(), data);
	}
	
	/**
	 * Updates session data for given session.
	 * 
	 * @param httpSession		given session
	 * @param data				new session data
	 * @return					true if successful, else false
	 */
	public synchronized boolean setSessionData(HttpSession httpSession, Object data) {
		Integer sessionKey = getSessionKey(httpSession);
		return setSessionData(sessionKey, data);
	}
	
	/**
	 * Updates session data for given session.
	 * 
	 * @param sessionKey		given sessions identifier
	 * @param data				new session data
	 * @return					true if successful, else false
	 */
	private boolean setSessionData (Integer sessionKey, Object data) {
		Optional<Session> session = getSession(sessionKey);
		if(session.isPresent()) {
			session.get().data = data;
			return true;
		} else return false;
	}
	
	/**
	 * Returns session data from current session.
	 * 
	 * @return		optional session data
	 */
	public synchronized Optional<Object> getSessionData() {
		return getSessionData(getCurrentHttpSession());
	}
	
	/**
	 * Returns session data from given session.
	 * 
	 * @param httpSession		given session
	 * @return					optional session data
	 */
	public synchronized Optional<Object> getSessionData(HttpSession httpSession) {
		return getSessionData((Integer) httpSession.getAttribute(SESSION_KEY_ATTRIBUTE_STRING));
	}
	
	/**
	 * Returns session data from given session.
	 * 
	 * @param sessionKey		session identifier
	 * @return					optional session data
	 */
	private Optional<Object> getSessionData(Integer sessionKey) {
		Optional<Session> session = getSession(sessionKey);
		
		if(session.isPresent())
			return Optional.of(session.get().data);
		return Optional.empty();
	}
	
	/**
	 * Returns session struct for given session.
	 * 
	 * @param sessionKey		session identifier
	 * @return					optional session struct
	 * 
	 * @see Session
	 */
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
	
	/**
	 * Returns session struct related to given user.
	 * 
	 * @param userId	user identifier
	 * @return			optional session struct
	 */
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
	
	/**
	 * Removes current sessions session data.
	 */
	public synchronized void removeSession() {
		HttpSession httpSession = getCurrentHttpSession();
		removeSession(httpSession);
	}
	
	/**
	 * Remove given sessions session data.
	 * 
	 * @param httpSession		given session
	 */
	public synchronized void removeSession(HttpSession httpSession) {
		removeSession(getSessionKey(httpSession));
		removeSessionKey(httpSession);
	}
	
	/**
	 * Remove given sessions session data.
	 * 
	 * @param sessionKey		given sessions identifier
	 */
	private void removeSession(Integer sessionKey) {
		sessions.remove(sessionKey);
		keyUserTable.removeWithKey(sessionKey);
	}
	
	/**
	 * Remove given users session data.
	 * 
	 * @param userId		user identifier
	 * @return				true if successful, false if user does not 
	 * 						possess a session.
	 */
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
	
	/**
	 * Updates a session structs last time used variable.
	 * 
	 * @param session		session struct
	 */
	private void updateSessionDuration(Session session) {
		session.lastUsed = System.currentTimeMillis();
	}
	
	/**
	 * Checks if given session struct has timed out. Refreshes timer if it 
	 * has not timed out.
	 * 
	 * @param session			session struct
	 * @param sessionKey		sessions identifier
	 * @return					true if timed out, else false
	 */
	private boolean checkForTimeOut(Session session, Integer sessionKey) {
		if(isTimedOut(session.lastUsed)) {
			removeSession(sessionKey);
			return true;
		} else {
			updateSessionDuration(session);
			return false;
		}
	}
	
	/**
	 * Checks if a given time has timed out.
	 * 
	 * @param lastUsed		given time in milliseconds
	 * @return				true if timed out, else false
	 */
	private boolean isTimedOut(long lastUsed) {
		return ((lastUsed+TIMEOUT_LENGTH) < System.currentTimeMillis());
	}
	
	/**
	 * Adds a session identifier to an {@link HttpSession}.
	 * 
	 * @param httpSession		httpsession
	 * @param sessionKey		session identifier
	 */
	private void addSessionKey(HttpSession httpSession, Integer sessionKey) {
		httpSession.setAttribute(SESSION_KEY_ATTRIBUTE_STRING, sessionKey);
	}
	
	/**
	 * Returns session identifier from given {@link HttpSession}.
	 * 
	 * @param httpSession		httpsession
	 * @return					session identifier
	 */
	private Integer getSessionKey(HttpSession httpSession) {
		return (Integer) httpSession.getAttribute(SESSION_KEY_ATTRIBUTE_STRING);
	}
	
	/**
	 * Removes session identifier from given {@link HttpSession}. 
	 * 
	 * @param httpSession		httpsession
	 */
	private void removeSessionKey(HttpSession httpSession) {
		httpSession.removeAttribute(SESSION_KEY_ATTRIBUTE_STRING);
	}
	
	/**
	 * A small struct that keeps track of information about a session.
	 * 
	 * @author tfy12hsm
	 *
	 */
	private class Session {
		public long lastUsed;
		public Object data;
	}
}
