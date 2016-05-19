package studentcapture.login;

import java.net.URI;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.mail.MailClient;
import studentcapture.user.User;

/**
 * Controller for handling HTTP requests related to the login page.
 * @author dv11osi, c13hbd
 */
@RestController
public class ResetPasswordController {
	
	private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;
    
    /*
    @RequestMapping(value = "/testResetPassword", method = RequestMethod.POST)
    public String resetPassword(
            HttpServletRequest request, @RequestParam("email") String userEmail) {
        
        String token = UUID.randomUUID().toString();
        System.out.println("Email: " + userEmail + ", Token: " + token);
        
        String url = 
                "https://" + request.getServerName() + 
                ":" + request.getServerPort() + 
                request.getContextPath();   
        
        System.out.println("Url: " + url);
        
        return token;
    }
    */
    
    @RequestMapping(value = "/lostPassword", method = RequestMethod.POST)
    public ModelAndView lostPassword(
            @RequestParam(value="email", required = true)    String email,
            @RequestParam(value="username", required = true) String username,
            HttpServletRequest request
            ){
        
        User user = getUserFromDB(username);
        
        String token = UUID.randomUUID().toString();
        System.out.println("Email: " + email + ", Token: " + token);
        
        String url = 
                "http://" + request.getServerName() + 
                ":" + request.getServerPort() + 
                request.getContextPath();
        
        
        System.out.println("Url: " + url);
        
        //Url syntax: /changePassword?username=123&token=456
        String tokenUrl = url + "/changePassword?username=" + user.getUserName() + "&token=" + token;
        System.out.println("Generated token url: " + tokenUrl);
        
        ModelAndView mav = new ModelAndView(); 
        mav.setViewName("redirect:login");

        
        //Validate credentials
        //Check if email and username match
        /*
        if(checkEmailExistsWithUserName(email,username)){
            System.out.println("success!");
        */
        
        //Generate link
        //Spring generate token http://www.baeldung.com/spring-security-registration-i-forgot-my-password
        
        //Store token in db ?
        System.out.println("Storing token: " + token + " for username: " + username);
       
        user.setToken(token);
        
        String targetUrl = "https://localhost:8443/users";
        HttpEntity<User> userEntity = new HttpEntity<User>(user);
        
        //Update user
        requestSender.put(targetUrl, userEntity, "User");
        
        System.out.println("Token stored!");

        
        //Email link
        //Spring or custom mail?
        
        /*
        MailClient mailClient = new MailClient();
        //mailClient.send(String to, String from, String subject, String msg)
        mailClient.send("receiver", "sender", "Reset Password", tokenUrl);
        */
        
        //Compare header token with db token <-- another method?
        
        /*
        }else{
            System.out.println("fail...");
            mav.setViewName("redirect:login?error=invaliduser");

        }*/

        
        return mav;
    }
    
    //How should this even work? //old password $2a$11$mG8HMjEx2J8pYPEEAScEM.jzSbMAQlNJChwh895Hr5D8J/sOE5YnW
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(
            @RequestParam(value="username", required = true) String username,
            @RequestParam(value="token", required = true) String token,
            @RequestParam(value="password", required = true) String password
            ){
        
        //System.out.println("Received email: " + email + ", token: " + token);
        System.out.println("Received token: " + token);
        
        User user = getUserFromDB(username);
        
        ModelAndView mav = new ModelAndView(); 
        //If the token does not exist, return
        if(user.getToken().compareTo("") == 0){
            mav.setViewName("redirect:login?error=badToken");
            
        }else if(user.getToken().compareTo(token) == 0){
            //Tokens match
            
            //Encrypt the password, set password and token
            user.setPswd(encryptPassword(password));
            user.setToken("");
            
            mav.setViewName("redirect:login?success");
            
            String targetUrl = "https://localhost:8443/users";
            HttpEntity<User> userEntity = new HttpEntity<User>(user);
            
            //Update user
            requestSender.put(targetUrl, userEntity, "User");

            System.out.println("Password updated for user: " + user.getUserName());
            
        }else{
            mav.setViewName("redirect:login?error=badToken");
        }
        
        /*
        ModelAndView mav = new ModelAndView(); 
        mav.setViewName("redirect:login?error=passwordRecoveryNotImplemented");
        */
        return mav;
        
    }
    
    @RequestMapping(value = "/sessiontest", method = RequestMethod.POST)
    public void sessionPost(@RequestParam(value="coolString", required = true) String cool, HttpSession session) {
    	session.setAttribute("123", cool);
    }
    
    @RequestMapping(value = "/sessiontest", method = RequestMethod.GET)
    public void sessionGet(HttpSession session) {
    	System.out.println("USERNAME: " + session.getAttribute("username") + " ID: " + session.getAttribute("userid"));
    	System.out.println("Put this cool string: " + session.getAttribute("123"));
    	
    	System.out.println("NOW WITH OTHER METHOD -----------------------------------");
    	
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpSession session2 = attr.getRequest().getSession();
    	System.out.println("USERNAME: " + session2.getAttribute("username") + " ID: " + session2.getAttribute("userid"));
    	System.out.println("Put this cool string: " + session2.getAttribute("123"));
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
    
    private User getUserFromDB(String username){
        
        int flag = 0;
        //Get user from DB
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("/users")
                .queryParam("String", username)
                .queryParam("int", flag)
                .build()
                .toUri();
        
        System.out.println("Target url: " + targetUrl);
                
        //Get response from database
        ResponseEntity<?> response = requestSender.getForEntity(targetUrl, User.class);
        System.out.println("Received status code: " + response.getStatusCode());

        //Check if NOT_FOUND was received
        if(response.getStatusCode().compareTo(HttpStatus.FOUND) == 0){
            //Return error?
            return null;
        }
        
        //Get the user object    
        return (User) response.getBody();
    }
    
    protected String encryptPassword(String password) {
        String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
        return generatedPassword;
    }
    
    
}
