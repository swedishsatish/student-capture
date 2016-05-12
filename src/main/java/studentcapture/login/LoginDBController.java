package studentcapture.login;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	public void registerUser(
							 @RequestParam(value="firstname", required = true) String firstName,
							 @RequestParam(value="lastname", required = true)  String lastName,
							 @RequestParam(value="email", required = true)     String email,
							 @RequestParam(value="username", required = true)  String username,
							 @RequestParam(value="password", required = true)  String password
							 ) {
	    
	    //LoginAuthentication loginAuth = new LoginAuthentication();
	    if(checkUser(username, password)) {
	    	//Send error that user already exists
	        System.out.println("User already exists");
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
	   
	    //Send/don't send new user to the database
	    //Boolean response = false;
	    Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
	   
	    if(response) {
	    	//registration successful
	    	System.out.println("Registration Success");
	    } else {
	    	//registration failed
	    	System.out.println("Registration failed");
	    }  
	}
	
	/**
	 * TODO: Check username and email instead. 
     *         Maybe two different methods, one for username, one for email 
     *         
	 * @return true if the user exists in the database, else false
	 */
	public boolean checkUser(String username, String password) {
        
	    System.out.println("checkUser() in DBController");
        System.out.println("Checking user data in DB with user: " + username + " and password: " + password);
        
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/login")
                .queryParam("username", username)
                .queryParam("pswd", password)
                .build()
                .toUri();
        
        System.out.println(targetUrl.toString());
        
        //Send request to DB and get the boolean answer
        Boolean response = requestSender.getForObject(targetUrl, Boolean.class);
        
        System.out.println("Boolean response received: Checkuser = " + response.toString());
        
        return response;
    }
}
