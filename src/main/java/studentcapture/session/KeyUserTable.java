package studentcapture.session;

import java.util.HashMap;
import java.util.Map;

public class KeyUserTable {
	private Map<Integer, Integer> keyUserRelations = new HashMap<>();
	private Map<Integer, Integer> userKeyRelations = new HashMap<>();
	
	public boolean add(Integer key, Integer user) {
		if(keyUserRelations.containsKey(key))
			return false;
		if(userKeyRelations.containsKey(user))
			return false;
		keyUserRelations.put(key, user);
		userKeyRelations.put(user, key);
		return true;
	}
	
	public void removeWithKey(Integer key) {
		userKeyRelations.remove(keyUserRelations.get(key));
		keyUserRelations.remove(key);
	}
	
	public void removeWithUser(Integer user) {
		keyUserRelations.remove(userKeyRelations.get(user));
		userKeyRelations.remove(user);
	}
	
	public Integer getUser(Integer key) {
		return keyUserRelations.get(key);
	}
	
	public Integer getKey(Integer user) {
		return userKeyRelations.get(user);
	}
}
