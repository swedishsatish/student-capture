package studentcapture.login;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Custom login authentication for Spring Security
 * Currently accepts "user" as username and password
 * @author Oskar Suikki
 */
@Component
public class LoginAuthentication implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName().trim();
		String password = auth.getCredentials().toString();
		if(username.equals("user") && password.equals("user")) {
			Authentication a = new UsernamePasswordAuthenticationToken(username, password);
			return a;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> auth) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(auth));
	}

	public boolean checkUser(String username, String password) {
		//Send request to DB and check if the username and password exists.
		return true;
	}
}
