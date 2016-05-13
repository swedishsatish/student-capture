package studentcapture.login;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import studentcapture.datalayer.database.User;


/**
 * Controller for handling HTTP requests related to the login page.
 * 
 * 2016-05-13
 * @author dv11osi, c13hbd
 *
 */
@RestController
public class LoginDBController {
	
	private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;
    
    /**
     * Registers a new user.
     * @param firstName 		First name of the user
     * @param lastName 			Last name of the user 
     * @param email 			Email for the user
     * @param username 			User name for the user
     * @param password 			Password the user uses to log in.
     * @param confirmpassword 	The repeated password the user has to input to register
     */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(
							 @RequestParam(value="firstname", required = true) 			String firstName,
							 @RequestParam(value="lastname", required = true)  			String lastName,
							 @RequestParam(value="email", required = true)     			String email,
							 @RequestParam(value="username", required = true)  			String username,
							 @RequestParam(value="password", required = true)  			String password,
							 @RequestParam(value="confirmpassword", required = true)	String confirmpassword) {
		
	    ModelAndView mav = new ModelAndView(); 
	    mav.setViewName("redirect:login?error=default");

	    if(!checkUsernameLength(username)) {
	    	mav.setViewName("redirect:login?error=usernamelength");
	    	return mav;
	    }
	    
	    if(!checkPasswordFormat(password)) {
	    	mav.setViewName("redirect:login?error=passwordformat");
	    	return mav;
	    }
	    
	    if(!password.equals(confirmpassword)) {
	    	mav.setViewName("redirect:login?error=passwordmatch");
	    	return mav;
	    }
	    
	    if(!checkEmailFormat(email)) {
	    	mav.setViewName("redirect:login?error=emailformat");
	    	return mav;
	    }
	    
	    if(!checkEmailExists(email)) {
	    	mav.setViewName("redirect:login?error=emailexists");
	    	return mav;
	    }
	    
	    if(!checkUserExists(username)) {
	    	mav.setViewName("redirect:login?error=userexists");
	    	return mav;
	    }
	    
	    User user = new User(username, firstName, lastName, email, encryptPassword(password));
	    ObjectMapper mapper = new ObjectMapper();
	    String jsonInString = "";
	    try {
			jsonInString = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
	    		.path("DB/addUser")
	    		.queryParam("jsonStringUser", jsonInString)
	    		.build()
	    		.toUri();
	    boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	    
	    if(response) {
	    	mav.setViewName("redirect:login");
	    	return mav;
	    }
	    mav.setViewName("redirect:login?registrationfail");
	    return mav;
	}

	/**
	 * Checks if password follows the required format
	 * The format consists of at least one small character, at least one big character and at least one number.
	 * The password has to be at least 6 characters long
	 * @param password The password
	 * @return Returns true if correct format
	 */
	protected boolean checkPasswordFormat(String password) {
		Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$");
		Matcher m = p.matcher(password);
		return m.find();
	}
	
	/**
	 * Checks if the email follows the required format
	 * @param email The email
	 * @return True if correct format
	 */
	protected boolean checkEmailFormat(String email) {
		Pattern p = Pattern.compile("^\\S+@(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})$");
		Matcher m = p.matcher(email);
		return m.find();
	}
	
	/**
	 * Checks if a user name exists in the database
	 * @param username The user name
	 * @return True if user name exists in the database
	 */
	protected boolean checkUserExists(String username) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    return requestSender.getForObject(targetUrl, Boolean.class);
	}
	
	/**
	 * Checks if the email exists in the database
	 * @param email The email
	 * @return True if the email exists in the database
	 */
	protected boolean checkEmailExists(String email) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/usrEmailExist")
                .queryParam("email", email)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    return requestSender.getForObject(targetUrl, Boolean.class);
	}
	
	/**
	 * Checks if the user name passes the required length.
	 * @param username The user name
	 * @return True if it passes
	 */
	protected boolean checkUsernameLength(String username) {
		if(username.length() > 5) {
			return true;
		}
		return false;
	}  
	
	/**
	 * Encrypts a password
	 * @param password The password to be encrypted
	 * @return Encrypted password
	 */
	protected String encryptPassword(String password) {
		String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
		return generatedPassword;
	}
}
