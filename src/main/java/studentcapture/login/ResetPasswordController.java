package studentcapture.login;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import studentcapture.mail.MailClient;
import studentcapture.user.User;

/**
 * Controller for handling HTTP requests related to the login page.
 * @author dv11osi, c13hbd
 */
@RestController
public class ResetPasswordController {
    
    @Autowired
    private LoginDAO loginDao;
    
    /**
     * Generates a link for resetting username's password, and emails the link
     * to the users email.
     * 
     * The link contains a randomly generated token, 
     *  which is also stored in the database.
     * 
     * @param email
     * @param username
     * @param request
     * @return
     */
    @RequestMapping(value = "/lostPassword", method = RequestMethod.POST)
    public ModelAndView lostPassword(
            @RequestParam(value="email", required = true)    String email,
            @RequestParam(value="username", required = true) String username,
            HttpServletRequest request
            ){
        
        ModelAndView mav = new ModelAndView(); 
        
        //flag = 0 for username, flag = 1 for userID
        int flag = 0;
        User user = loginDao.getUser(username, flag);
        
        //Return if user does not exist
        if(user.getUserName() == null){
            mav.setViewName("redirect:login?error=invalidUser");
        }else{
            
            //Spring generate token http://www.baeldung.com/spring-security-registration-i-forgot-my-password
            String token = UUID.randomUUID().toString();
            
            String url = 
                    "https://" + request.getServerName() + 
                    ":" + request.getServerPort() + 
                    request.getContextPath();
                        
            //Generate link
            //Url syntax: /changePassword?username=123&token=456
            String tokenUrl = url + "/changePassword?username=" + user.getUserName() + "&token=" + token;
                
            //Set token for the user           
            user.setToken(token);
            
            //Update user
            loginDao.updateUser(user);
                        
            //Email link
            MailClient mailClient = new MailClient();
            String receiver = user.getEmail();
            //mailClient.send(String to, String from, String subject, String msg)
            mailClient.send(receiver, "no-reply@studentcapture.com", "Reset Password", tokenUrl);

            mav.setViewName("redirect:login?passwordemail");
            
        }

        return mav;
    }
    

    /**
     * Changes password for the user username, 
     * if token matches the token stored in the users database entry.
     * 
     * @param username
     * @param token
     * @param password
     * @return
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(
            @RequestParam(value="username", required = true) String username,
            @RequestParam(value="token", required = true) String token,
            @RequestParam(value="password", required = true) String password
            ){
                
        //flag = 0 for username, flag = 1 for userID
        int flag = 0;
        User user = loginDao.getUser(username, flag);
                
        ModelAndView mav = new ModelAndView(); 
        
        //If the token does not exist, return
        if(user == null){
            mav.setViewName("redirect:login?error=badtoken");
        }
        else if(user.getToken() == null){
            mav.setViewName("redirect:login?error=badtoken");
        }
        else if(user.getToken().compareTo("") == 0){
            mav.setViewName("redirect:login?error=badtoken");
            
        }else if(user.getToken().compareTo(token) == 0){
            //Tokens match
            
            //Encrypt the password, set password and token
            user.setPswd(encryptPassword(password));
            user.setToken("");
            
            mav.setViewName("redirect:login?passwordchanged");
            
            //Update user
            loginDao.updateUser(user);
            
        }else{
            mav.setViewName("redirect:login?error=badToken");
        }

        return mav;
        
    }
    
    /**
     * Encrypts a password string with BCrypt.
     * 
     * @param password
     * @return the encrypted password
     */
    protected String encryptPassword(String password) {
        String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
        return generatedPassword;
    }    
    
}
