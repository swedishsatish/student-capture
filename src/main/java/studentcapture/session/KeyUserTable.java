package studentcapture.session;

import java.util.HashMap;
import java.util.Map;


/**
 * KeyUserTable connects two hashmaps in order to make both keys and values 
 * unique. It is used in {@link SessionHandler} to keep track of which user
 * account is bound to which session (sessionkey).
 * 
 *
 */
public class KeyUserTable {
	private final Map<Integer, Integer> keyUserRelations = new HashMap<>();
	private final Map<Integer, Integer> userKeyRelations = new HashMap<>();
	
	/**
	 * Bind a key to a user.
	 * 
	 * @param key		key identifier
	 * @param user		user identifier
	 * @return			true if successful, else false
	 */
	public boolean add(Integer key, Integer user) {
		if(keyUserRelations.containsKey(key))
			return false;
		if(userKeyRelations.containsKey(user))
			return false;
		keyUserRelations.put(key, user);
		userKeyRelations.put(user, key);
		return true;
	}
	
	/**
	 * Removes bind between user and key using key.
	 * 
	 * @param key		key identifier
	 */
	public void removeWithKey(Integer key) {
		userKeyRelations.remove(keyUserRelations.get(key));
		keyUserRelations.remove(key);
	}
	
	/**
	 * Removes bind between user and key using user.
	 * 
	 * @param user		user identifier
	 */
	public void removeWithUser(Integer user) {
		keyUserRelations.remove(userKeyRelations.get(user));
		userKeyRelations.remove(user);
	}
	
	/**
	 * Returns user using key.
	 * 
	 * @param key		key identifier
	 * @return			user identifier
	 */
	public Integer getUser(Integer key) {
		return keyUserRelations.get(key);
	}
	
	/**
	 * Returns key using user.
	 * 
	 * @param user		user identifier
	 * @return			key identifier
	 */
	public Integer getKey(Integer user) {
		return userKeyRelations.get(user);
	}
}
