package studentcapture.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import studentcapture.user.User;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class RegistrationController {

	@Autowired
	private LoginDAO loginDao;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST) 
	public ResponseEntity<Object> register(
			@RequestParam(value="firstname", required = true)           String firstName,
			@RequestParam(value="lastname", required = true)            String lastName,
			@RequestParam(value="email", required = true)               String email,
			@RequestParam(value="username", required = true)            String username,
			@RequestParam(value="password", required = true)            String password,
			@RequestParam(value="confirmpassword", required = true)     String confirmpassword) throws URISyntaxException {
		
		URI uri;
        HttpHeaders httpHeaders = new HttpHeaders();
        
        //If user fails confirming password
		if (!password.equals(confirmpassword)) {
			uri = new URI("/login?error=passwordmatch");
			httpHeaders.setLocation(uri);
			return new ResponseEntity<Object>(httpHeaders, HttpStatus.FOUND);
		}
		
		User user = new User(username, firstName, lastName, email, encryptPassword(password));
        
        ErrorFlags status = loginDao.addUser(user);

        //Get the correct status message
        if(status == ErrorFlags.NOERROR){
            uri = new URI("/login?" + status.toString());
        }else{
            uri = new URI("/login?error=" + status.toString());
        }
        httpHeaders.setLocation(uri);
        return new ResponseEntity<Object>(httpHeaders, HttpStatus.FOUND);
	}
	
    /**
     * Encrypts password
     * @param password The input password
     * @return Encrypted password
     */
    protected String encryptPassword(String password) {
        String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
        return generatedPassword;
    }
}
