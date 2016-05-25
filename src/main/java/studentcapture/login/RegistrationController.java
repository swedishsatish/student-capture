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
import studentcapture.user.UserDAO;

import java.net.URI;
import java.net.URISyntaxException;
/**
 * RegistrationController.java
 *      Managing the registration of users, and adds them to the DB
 *      
 *      Changes : 
 *          (Removed) RequestParam, parameter required = true is default. 
 *          
 *  @author c13hbd     
 */
@RestController
public class RegistrationController {

	@Autowired
	private UserDAO userDao;
	
	/**
	 * Adds a user to the User DB .
	 * <p>
	 * @param firstName,       Firstname of the user 
	 * @param lastName,        Lastname of the user
	 * @param email,           The users email
	 * @param username,        The login name of the user
	 * @param password,        The password for the user.
	 * @param confirmpassword, Second check if the password matches.
	 * @return Response,       Returns response with a redirect URL and 
	 *                         HTTP status
	 * @throws URISyntaxException, Should never cast a Exception of this kind 
	 *                         because of static links
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST) 
	public ResponseEntity<Object> register(
	    	@RequestParam(value="firstname")           String firstName,
			@RequestParam(value="lastname")            String lastName,
			@RequestParam(value="email")               String email,
			@RequestParam(value="username")            String username,
			@RequestParam(value="password")            String password,
			@RequestParam(value="confirmpassword")     String confirmpassword) 
			        throws URISyntaxException {
	    //Redirect URI
		URI uri;
        HttpHeaders httpHeaders = new HttpHeaders();
        
        //Return if user fails to confirm the password
		if (!password.equals(confirmpassword)) {
			uri = new URI("/login?error=passwordmatch");
			httpHeaders.setLocation(uri);
			return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
		}
		
		User user = new User(username, firstName, lastName, email,
		        encryptPassword(password));
        
        ErrorFlags status = userDao.addUser(user);

        //Put correct status message in URL.
        if(status == ErrorFlags.NOERROR){
            uri = new URI("/login?" + status.toString());
        }else{
            //if an error occurred
            uri = new URI("/login?error=" + status.toString());
        }
        
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
	}
	
    /**
     * Encrypts password with BCrypt
     * <p>
     * @see org.springframework.security.crypto.bcrypt.BCrypt
     * 
     * @param password, The input password
     * @return Encrypted password
     */
    private String encryptPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(11));
    }
}