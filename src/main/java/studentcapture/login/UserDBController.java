package studentcapture.login;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.model.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
public class UserDBController {
    
    private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;  
    
    public UserDBController(){
        
    }
    
    /**
     * Registers a new user.
     * @param firstName
     * @param lastName
     * @param email
     * @param username
     * @param password
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView registerUser(
            @RequestParam(value="firstname", required = true)           String firstName,
            @RequestParam(value="lastname", required = true)            String lastName,
            @RequestParam(value="email", required = true)               String email,
            @RequestParam(value="username", required = true)            String username,
            @RequestParam(value="password", required = true)            String password,
            @RequestParam(value="confirmpassword", required = true)     String confirmpassword
            //RedirectAttributes redirectAttributes //Not working at the moment
            ) {
        
        ModelAndView mav = new ModelAndView(); 
        mav.setViewName("redirect:/login?error=default");

        if(!checkUsernameLength(username)) {
            mav.setViewName("redirect:/login?error=usernamelength");
            //redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect username length");
            return mav;
        }
        
        if(!checkPasswordFormat(password)) {
            mav.setViewName("redirect:/login?error=passwordformat");
            //redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect password format");
            return mav;
        }
        
        if(!password.equals(confirmpassword)) {
            mav.setViewName("redirect:/login?error=passwordmatch");
            //redirectAttributes.addFlashAttribute("message", "ERROR: Passwords does not match");

            return mav;
        }
        
        if(!checkEmailFormat(email)) {
            mav.setViewName("redirect:/login?error=emailformat");
            //redirectAttributes.addFlashAttribute("message", "ERROR: Incorrect email format");

            return mav;
        }
        
        if(!checkEmailExists(email)) {
            mav.setViewName("redirect:/login?error=emailexists");
            //redirectAttributes.addFlashAttribute("message", "ERROR: Email does already exist");

            return mav;
        }
        
        if(!checkUserExists(username)) {
            mav.setViewName("redirect:/login?error=userexists");
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
            mav.setViewName("redirect:/login");
            return mav;
        } else {
            //registration failed
            System.out.println("Registration failed");
        }
        return mav;
    }
    
    /**
     * Checks if user exists in database
     * @param username The user name
     * @return True if user is in database
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    protected boolean checkUserExists(
            @RequestParam(value="username", required = true) String username) {
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/userNameExist")
                .queryParam("userName", username)
                .build()
                .toUri();
        //Send request to DB and get the boolean answer
        return requestSender.getForObject(targetUrl, Boolean.class);
    }

    
    /**
     * Get User object based on user id
     * @param id
     * @return
     */
    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("user_id") int id){
        
        System.out.println("Checking user id: " + id);
        
        //Get user from DB based on user_id
        URI targetUrl = UriComponentsBuilder.fromUriString(dbURI)
                .path("DB/getUser") //DB must implement /getUser
                .queryParam("user_id", id)
                .build()
                .toUri();
        //User user = requestSender.getForObject(targetUrl, User.class);
        ResponseEntity<?> response = requestSender.getForEntity(targetUrl, User.class);
        System.out.println("Received status code: " + response.getStatusCode());
        
        //Check if NOT_FOUND was received
        if(response.getStatusCode().compareTo(HttpStatus.NOT_FOUND) == 0){
            //Return error?
            return null;
        }
        
        User user = (User) response.getBody();
        
        return user;
    }
    
    
    public boolean comparePassword(String password, String hashed) {
        //String hashed = "";
        return BCrypt.checkpw(password, hashed);
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