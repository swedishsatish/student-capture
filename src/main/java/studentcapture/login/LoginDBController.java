package studentcapture.login;

import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import studentcapture.model.User;
import studentcapture.mail.MailClient;

/**
 * Controller for handling HTTP requests related to the login page.
 * @author dv11osi, c13hbd
 */
@RestController
public class LoginDBController {
	
	private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;
    
    @RequestMapping(value = "/testResetPassword", method = RequestMethod.POST)
    public String resetPassword(
            HttpServletRequest request, @RequestParam("email") String userEmail) {
        
        String token = UUID.randomUUID().toString();
        System.out.println("Email: " + userEmail + ", Token: " + token);
        
        String url = 
                "http://" + request.getServerName() + 
                ":" + request.getServerPort() + 
                request.getContextPath();   
        
        System.out.println("Url: " + url);
        
        return token;
    }
    
    @RequestMapping(value = "/lostPassword", method = RequestMethod.POST)
    public ModelAndView lostPassword(
            @RequestParam(value="email", required = true)    String email,
            @RequestParam(value="username", required = true) String username,
            HttpServletRequest request
            ){
        
        String token = UUID.randomUUID().toString();
        System.out.println("Email: " + email + ", Token: " + token);
        
        String url = 
                "http://" + request.getServerName() + 
                ":" + request.getServerPort() + 
                request.getContextPath();
        
        
        System.out.println("Url: " + url);
        
        String tokenUrl = url + "/lostPassword?token=" + token;
        
        ModelAndView mav = new ModelAndView(); 
        mav.setViewName("redirect:login");

        
        //Validate credentials
        //Check if email and username match
        if(checkEmailExistsWithUserName(email,username))
            System.out.println("success!");
        else{
            System.out.println("fail...");
            mav.setViewName("redirect:login?error=invaliduser");

        }
        
        //Generate link
        //Spring generate token http://www.baeldung.com/spring-security-registration-i-forgot-my-password
        
        //Store token in db ?
        
        
        //Email link
        //Spring or custom mail?
        
        
        MailClient mailClient = new MailClient();
        //mailClient.send(String to, String from, String subject, String msg)
        mailClient.send("receiver", "sender", "Reset Password", tokenUrl);
        
        
        //Compare header token with db token <-- another method?
        
        return mav;
    }
    
    //How should this even work?
    @RequestMapping(value = "/lostPassword", method = RequestMethod.GET)
    public ModelAndView resetPassword(
            //@RequestParam(value="email", required = true) String email,
            @RequestParam(value="token", required = true) String token
            ){
        
        //System.out.println("Received email: " + email + ", token: " + token);
        System.out.println("Received token: " + token);
        ModelAndView mav = new ModelAndView(); 
        mav.setViewName("redirect:login?error=passwordRecoveryNotImplemented");
        
        return mav;
        
    }
    
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
			@RequestParam(value="firstname", required = true)			String firstName,
			@RequestParam(value="lastname", required = true)  			String lastName,
			@RequestParam(value="email", required = true)     			String email,
			@RequestParam(value="username", required = true)  			String username,
			@RequestParam(value="password", required = true)  			String password,
			@RequestParam(value="confirmpassword", required = true) 	String confirmpassword
			//RedirectAttributes redirectAttributes //Not working at the moment
			) {
		
	    ModelAndView mav = new ModelAndView(); 
	    mav.setViewName("redirect:login?error=default");

	    if(!checkUsernameLength(username)) {
	    	mav.setViewName("redirect:login?error=usernamelength");
	    	//redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect username length");
	    	return mav;
	    }
	    
	    if(!checkPasswordFormat(password)) {
	    	mav.setViewName("redirect:login?error=passwordformat");
	        //redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect password format");
	    	return mav;
	    }
	    
	    if(!password.equals(confirmpassword)) {
	    	mav.setViewName("redirect:login?error=passwordmatch");
	        //redirectAttributes.addFlashAttribute("message", "ERROR: Passwords does not match");

	    	return mav;
	    }
	    
	    if(!checkEmailFormat(email)) {
	    	mav.setViewName("redirect:login?error=emailformat");
	        //redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect email format");

	    	return mav;
	    }
	    
	    if(!checkEmailExists(email)) {
	    	mav.setViewName("redirect:login?error=emailexists");
	        //redirectAttributes.addFlashAttribute("message", "ERROR: Email does already exist");

	    	return mav;
	    }
	    
	    if(!checkUserExists(username)) {
	    	mav.setViewName("redirect:login?error=userexists");
	        //redirectAttributes.addFlashAttribute("message", "ERROR: Username does already exist");

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

	/**
	 * Checks if password follows the required format
	 * The format consists of atleast one small letter, at least one big letter and at least one number.
	 * @param password The password to validate
	 * @return Returns true if correct format
	 */
	protected boolean checkPasswordFormat(String password) {
		Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$");
		Matcher m = p.matcher(password);
		return m.find();
	}
	
	/**
	 * Checks if email is in correct format
	 * @param email The email
	 * @return True if in correct format
	 */
	protected boolean checkEmailFormat(String email) {
		Pattern p = Pattern.compile("^\\S+@(([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6})$");
		Matcher m = p.matcher(email);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if user exists in database
	 * @param username The user name
	 * @return True if user is in database
	 */
	protected boolean checkUserExists(String username) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    return !requestSender.getForObject(targetUrl, Boolean.class);
	}
	
	/**
	 * Checks if email exists in database
	 * @param email The email
	 * @return True if email exists in database
	 */
	protected boolean checkEmailExists(String email) {
	    URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userEmailExist")
                .queryParam("email", email)
                .build()
                .toUri();
	    //Send request to DB and get the boolean answer
	    return !requestSender.getForObject(targetUrl, Boolean.class);
	}

    /**
     * Checks if email and user exist in the same user.
     * @param email Email address to check
     * @param userName Username to check
     * @return True if Email and Username belong to the same user.
     */
	protected boolean checkEmailExistsWithUserName(String email, String userName) {
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userEmailExistWithUserName")
                .queryParam("email", email)
                .queryParam("username", userName)
                .build()
                .toUri();
        
        System.out.println("Target: " + targetUrl);
        
        Boolean answer = requestSender.getForObject(targetUrl, Boolean.class);
        System.out.println(answer);
        //Send request to DB and get the boolean answer
        return requestSender.getForObject(targetUrl, Boolean.class);
	}

	/**
	 * Checks if the user name is required length 
	 * @param username The username
	 * @return True if requirements are met
	 */
	protected boolean checkUsernameLength(String username) {
		if(username.length() > 5) {
			return true;
		}
		return false;
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
