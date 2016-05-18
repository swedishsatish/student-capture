package studentcapture.login;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    
    @Autowired
    private RestTemplate requestSender;
    
    private UserDBController userDBController;

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
	    
	    //System.out.println("checkUser() in Authenticator");
	    //System.out.println("Checking user data in DB with user: " + username);
	    
	    //Check username in DB
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                //.queryParam("pswd", password)
                .build()
                .toUri();
        
        //TODO: Change to User
        Boolean userExist = requestSender.getForObject(targetUrl, Boolean.class);
        
        if(!userExist){
            System.out.println("ERROR: Incorrect username");
            return false;
        }
        System.out.println("User \"" + username + "\" found!");
	    
	    //Get the password from DB
	    targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/getHpswd")
                .queryParam("username", username)
                //.queryParam("pswd", password)
                .build()
                .toUri();
	    
	    //System.out.println(targetUrl.toString());
	    
	    //Send request to DB and get the string answer
	    String hashedPswd = requestSender.getForObject(targetUrl, String.class);
	    
	    System.out.println("Boolean response received: Checkuser = " + hashedPswd.toString());
	    
	    if(comparePassword(password, hashedPswd)){
	        System.out.println("Passwords match!");
	        return true;
	    }
	    System.out.println("ERROR: Incorrect password");
	    
		return false;
	}
	
	/* //Uncomment this method when the DB is updated to REST 
    public boolean checkUser(String username, String password) {
        
        
        //System.out.println("checkUser() in Authenticator");
        System.out.println("Checking user data in DB with user: " + username);
        
        //Get user from DB
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                .build()
                .toUri();
        
        System.out.println("Target: " + targetUrl);
        
        //DB must be correct
        User user = requestSender.getForObject(targetUrl, User.class);
        
        if(user == null){
            System.out.println("ERROR: Incorrect username");
            return false;
        }
        //System.out.println("Verifying user: " + user.getUserName());

        return comparePassword(password, user.getPswd());
                
    }*/
	
	public boolean comparePassword(String password, String hashed) {
		//String hashed = "";
		return BCrypt.checkpw(password, hashed);
	}
}
