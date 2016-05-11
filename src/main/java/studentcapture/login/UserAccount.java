package studentcapture.login;

/**
 * User account used for logging in.
 * @author Oskar Suikki
 *
 */
public class UserAccount {
	private String firstName;
	private String lastName;
	private String email;
	private String userName;
	private String password;

	
	public UserAccount() {
		firstName = "";
		lastName = "";
		email = "";
		userName = "";
		password = "";

	}
	
	public UserAccount(String firstName, String lastName, String email, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userName = userName;
		this.password = password;
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
	
	public String getUsername() {
		return userName;
	}
	
	public void setUsername(String username) {
		this.userName = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
