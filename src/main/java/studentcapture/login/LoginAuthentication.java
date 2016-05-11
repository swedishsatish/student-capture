package studentcapture.login;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    
    private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;

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
		/*if(username.equals("user") && password.equals("user")) {
			Authentication a = new UsernamePasswordAuthenticationToken(username, password);
			return a;
		} else {
			return null;
		}*/
		if(checkUser(username, password)){
		    Collection<? extends GrantedAuthority> authorities = 
		            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		    Authentication a = new UsernamePasswordAuthenticationToken(username, password, authorities);
		    //a.setAuthenticated(arg0); //Should not be necessary
            return a;
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> auth) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth));
	}

	public boolean checkUser(String username, String password) {
		//Send request to DB and check if the username and password exists.
	    
	    System.out.println("Checking user data in DB");
	    
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/login")
                .queryParam("username", username)
                .queryParam("pswd", password)
                .build()
                .toUri();
	    
	    Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	    
	    System.out.println("Boolean response received: Checkuser = " + response.toString());
	    
		return response;
	}
}
