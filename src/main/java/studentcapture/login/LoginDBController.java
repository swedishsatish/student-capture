package studentcapture.login;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import studentcapture.datalayer.*;
import studentcapture.datalayer.database.User;


/**
 * Controller for handling HTTP requests related to the login page.
 * @author Oskar Suikki, c13hbd
 *
 */
@RestController
public class LoginDBController {
	
	private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;
    
    /**
     * Registers a new user.
     * @param firstName
     * @param lastName
     * @param email
     * @param username
     * @param password
     */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(
							 @RequestParam(value="firstname", required = true) String firstName,
							 @RequestParam(value="lastname", required = true)  String lastName,
							 @RequestParam(value="email", required = true)     String email,
							 @RequestParam(value="username", required = true)  String username,
							 @RequestParam(value="password", required = true)  String password,
							 @RequestParam(value="confirmpassword", required = true) String confirmpassword) {
		
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
	    
	    if(!checkPasswordMatches(password, confirmpassword)) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
	    		.path("DB/addUser")
	    		.queryParam("jsonStringUser", jsonInString)
	    		.build()
	    		.toUri();
	    boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	    
	    if(response) {
	    	//registration successful
	    	System.out.println("Registration Success");
	    	mav.setViewName("redirect:login");
	    	return mav;
	    } else {
	    	//registration failed
	    	System.out.println("Registration failed");
	    }
	    return mav;
	}
	
	protected boolean checkPasswordMatches(String password, String confirmpassword) {
		if(password.equals(confirmpassword)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks password format
	 * @param password
	 * @return Returns true if correct format
	 */
	protected boolean checkPasswordFormat(String password) {
		Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$");
		Matcher m = p.matcher(password);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	protected boolean checkEmailFormat(String email) {
		Pattern p = Pattern.compile("^\\S+@(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})$");
		Matcher m = p.matcher(email);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	protected boolean checkUserExists(String username) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	    if(response) {
	    	return false;
	    }
	    return true;
	}
	
	protected boolean checkEmailExists(String email) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/usrEmailExist")
                .queryParam("email", email)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	    if(response) {
	    	return false;
	    }
	    return true;
	}
	
	protected boolean checkUsernameLength(String username) {
		if(username.length() > 5) {
			return true;
		}
		return false;
	}  
	
	protected String encryptPassword(String password) {
		String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
		return generatedPassword;
	}
}
