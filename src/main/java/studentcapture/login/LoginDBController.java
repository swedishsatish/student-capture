package studentcapture.login;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.datalayer.*;


/**
 * Controller for handling HTTP requests related to the login page.
 * @author Oskar Suikki
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
	public void registerUser(
							 @RequestParam(value="firstname", required = true) String firstName,
							 @RequestParam(value="lastname", required = true)  String lastName,
							 @RequestParam(value="email", required = true)     String email,
							 @RequestParam(value="username", required = true)  String username,
							 @RequestParam(value="password", required = true)  String password
							 ) {
	    LoginAuthentication loginAuth = new LoginAuthentication();
	    if(loginAuth.checkUser(username, password)) {
	    	//Send error that user already exists
	    	return;
	    }
	    
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
	    		.path("DB/register")
	    		.queryParam("username", username)
	    		.queryParam("fName", firstName)
	    		.queryParam("lName", lastName)
	    		.queryParam("pNr", "123")
	    		.queryParam("pwd", password)
	    		.build()
	    		.toUri();
	   
	    Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	   
	    if(response) {
	    	//registration successful
	    	System.out.println("Registration Success");
	    } else {
	    	//registration failed
	    	System.out.println("Registration failed");
	    }
	}
}
