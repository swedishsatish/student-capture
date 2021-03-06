package studentcapture.user;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by c12ton on 5/12/16.
 * Will act as a container for information of a user
 */
public class User {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String pswd;
    private String userID;
    private String token;  //Will be used for recovery of password
    private boolean isTeacher;

    //Needed for json string. DONT TOUCH!
    public User() {}

    public User(String userName, String fName, String lName,
                String email,String pswd, boolean isTeacher) {

        this.userName = userName;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.pswd = pswd;
        this.isTeacher = isTeacher;
    }

    /**
     * checks if all the params in the user is valid (not null)
     * @return boolean
     */
    public boolean areUserParamsValid() {

        return !(this.userName == null || this.firstName == null
                || this.lastName == null || this.email == null
                || this.pswd == null);
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public boolean isTeacher() {
		return isTeacher;
	}

	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}

	@Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof User)) return false;

        User user = (User) other;
        return user.getUserName().equals(this.getUserName())
                && user.getFirstName().equals(this.firstName)
                && user.getLastName().equals(this.lastName)
                && user.getEmail().equals(this.email)
                && user.getPswd().equals(this.pswd);
    }
    
    /**
     * Use this method to extract user id from a session
     * @param session The current session
     * @return userID
     */
    public static int getSessionUserId(HttpSession session) {
    	String userid = (String) session.getAttribute("userid");
    	return Integer.parseInt(userid);
    }
    
    /**
     * Gets the username from session
     * @param session The session
     * @return Username
     */
    public static String getSessionUserName(HttpSession session) {
    	return (String) session.getAttribute("username");
    }
    
    /**
     * Gets the UserID based on context session.
     * @return The current context session userid
     */
    public static int getContextUserId() {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpSession session = attr.getRequest().getSession();
    	String userid = (String) session.getAttribute("userid");
    	return Integer.parseInt(userid);
    }
    
    /**
     * Gets the username based on context session.
     * @return The current context session username
     */
    public static String getContextUserName() {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpSession session = attr.getRequest().getSession();
    	return (String)session.getAttribute("username");
    }
}
