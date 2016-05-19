package studentcapture.login;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.user.User;

/**
 * Custom login authentication for Spring Security
 * Currently accepts "user" as username and password
 * 
 * 2016-05-11
 * Connected to DB
 * 
 * @author Oskar Suikki, c13hbd
 */
@Component
public class LoginAuthentication implements AuthenticationProvider {

    
    //Using the same method for connecting to DB as in FeedbackController.java
    
    private static final String dbURI = "https://localhost:8443";
    private static final String SESSION_USERNAME_TAG = "username";
    private static final String SESSION_USERID_TAG = "userid";
    
    @Autowired
    private RestTemplate requestSender;
    
    //private UserDBController userDBController;

    /*
    Login only works with users in the database. 
    If the username and password matches with the database, 
        the user will be given the role "ROLE_USER". 
    The role prevents authenticate() from being called more than once.
    */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName().trim();
		String password = auth.getCredentials().toString();
		
		//userDBController = new UserDBController();

		if(checkUser(username, password)){
		    //Set role
		    Collection<? extends GrantedAuthority> authorities = 
		            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		    
		    Authentication a = new UsernamePasswordAuthenticationToken(username, password, authorities);
		    updateSession(username);
		    return a;
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> auth) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth));
	}

	/**
	 * Checks if user is registered in the database.
	 * @param username User name
	 * @param password User input password
	 * @return true if user name and password match in the database, else false
	 */
	
	public boolean checkUser(String username, String password) {
                
        //Get user from DB
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("/users")
                .queryParam("String", username)
                .queryParam("int", 0)
                .build()
                .toUri();
                
        //Get response from database
        ResponseEntity<?> response = requestSender.getForEntity(targetUrl, User.class);
        //System.out.println("Received status code: " + response.getStatusCode());

        //Check if NOT_FOUND was received
        if(response.getStatusCode().compareTo(HttpStatus.FOUND) == 0){
            //Return error?
            return false;
        }
        
        //Get the user object and check that the password is valid
        User user = (User) response.getBody();

        return comparePassword(password, user.getPswd());
                
    }

	public boolean comparePassword(String password, String hashed) {
		//String hashed = "";
		return BCrypt.checkpw(password, hashed);
	}
	
	private void updateSession(String username) {
		URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("/users")
                .queryParam("String", username)
                .queryParam("int", 0)
                .build()
                .toUri();
		ResponseEntity<?> response = requestSender.getForEntity(targetUrl, User.class);
		User user = (User) response.getBody();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		attr.getRequest().getSession().setAttribute(SESSION_USERNAME_TAG, user.getUserName());
		attr.getRequest().getSession().setAttribute(SESSION_USERID_TAG, user.getUserID());
		attr.getRequest().getSession().setMaxInactiveInterval(60*60);
	}
}
