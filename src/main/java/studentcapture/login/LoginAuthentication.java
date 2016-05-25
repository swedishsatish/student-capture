package studentcapture.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import studentcapture.user.User;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * Custom login authentication for Spring Security
 * Currently uses database for user comparisons.
 * This class also sets sessions when a login is successful.
 * 
 * @author dv11osi, c13hbd
 */
@Component
public class LoginAuthentication implements AuthenticationProvider {

	//Session attribute constants
	private static final String SESSION_USERNAME_TAG = "username";
    private static final String SESSION_USERID_TAG = "userid";
    //Session timeout, this is in minutes.
    private static final int SESSION_TIMEOUT = 60*60;
    
    @Autowired
    private LoginDAO loginDao;
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * Fired when login occurs via Spring Security.
     * Checks password
     * Updates session contents
     * Redirects to initial target link if needed
     */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName().trim();
		String password = auth.getCredentials().toString();

		if(checkUser(username, password)){
		    //Set role
		    Collection<? extends GrantedAuthority> authorities = 
		            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		    
		    Authentication a = new UsernamePasswordAuthenticationToken(username, password, authorities);
		    updateSession(username);	   
		    redirection();
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
	private boolean checkUser(String username, String password) {
        User user = loginDao.getUser(username, 0);
        if (user == null) {
        	return false;
        }
        return BCrypt.checkpw(password, user.getPswd());
           
    }
	
	/**
	 * Updates session object with attribute and timeout settings.
	 * @param username The user that is logging in.
	 */
	private void updateSession(String username) {
		User user = loginDao.getUser(username, 0);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		attr.getRequest().getSession().setAttribute(SESSION_USERNAME_TAG, user.getUserName());
		attr.getRequest().getSession().setAttribute(SESSION_USERID_TAG, user.getUserID());
		//The method for timeouts uses seconds, multiply by 60 for minutes.
		attr.getRequest().getSession().setMaxInactiveInterval(SESSION_TIMEOUT*60);
	}
	
	/**
	 * Redirects to a index url if it includes ?param=
	 * Does not work with /login
	 */
	private void redirection() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(attr.getRequest(), attr.getResponse());
	    if(savedRequest != null) {
	    	String redirURL = savedRequest.getRedirectUrl();
	    	if(redirURL.contains("?param=")) {
	    		try {
					redirectStrategy.sendRedirect(attr.getRequest(), attr.getResponse(), redirURL);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
}
