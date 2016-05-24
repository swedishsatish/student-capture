package studentcapture.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import studentcapture.mail.MailClient;
import studentcapture.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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
     * @param email Email of the user trying to reset their password.
     * @param username Username of the user trying to reset their password
     * @param request Origin of the HTTP request
     * @return A ModelAndView Object defining the redirect to the login page.
     */
    @RequestMapping(value = "/lostPassword", method = RequestMethod.POST)
    public ModelAndView lostPassword(
            @RequestParam(value="email")    String email,
            @RequestParam(value="username") String username,
            HttpServletRequest request
            ){
        
        ModelAndView mav = new ModelAndView(); 
        
        //flag = 0 for username, flag = 1 for userID
        int flag = 0;
        User user = loginDao.getUser(username, flag);

        
        //Return if user does not exist
        if(user == null){
            mav.setViewName("redirect:login?error=invalidUser");
        } else if(!user.getEmail().equals(email)) {
            mav.setViewName("redirect:login?passwordemail");
        } else {
            
            //Spring generate token
            String token = UUID.randomUUID().toString();
            
            String url = 
                    "https://" + request.getServerName() + 
                    ":" + request.getServerPort() + 
                    request.getContextPath();
                        
            //Generate link
            //Url syntax: /changePassword?username=abc&token=123
            String tokenUrl = url + "/changePassword?username=" +
                    user.getUserName() + "&token=" + token;
                
            //Set token for the user           
            user.setToken(token);
            
            //Update user
            loginDao.updateUser(user);
                        
            //Email link
            MailClient mailClient = new MailClient();
            String receiver = user.getEmail();
            String msg = "To reset your password on Student Capture, please " +
                    "follow this link:\n"+tokenUrl;
            //mailClient.send(String to, String from, String subj, String msg)
            mailClient.send(receiver, "studentcapture@cs.umu.se",
                    "Reset Password", msg);

            mav.setViewName("redirect:login?passwordemail");
            
        }

        return mav;
    }
    

    /**
     * Changes password for the user username, 
     * if token matches the token stored in the users database entry.
     * 
     * @param username User trying to change their password.
     * @param token Token used to verify the user is who they say they are.
     * @param password The new password that the user is attempting to switch
     *                 to.
     * @return ModelAndView Object redirecting to the login page with message
     * about whether the change succeeded or not.
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(
            @RequestParam(value="username") String username,
            @RequestParam(value="token") String token,
            @RequestParam(value="password") String password
            ){
                
        //flag = 0 for username, flag = 1 for userID
        int flag = 0;
        User user = loginDao.getUser(username, flag);
                
        ModelAndView mav = new ModelAndView(); 
        
        //If the token does not exist, return
        if (user == null || user.getToken() == null ||
                user.getToken().compareTo("") == 0) {
            mav.setViewName("redirect:login?error=badtoken");
        } else if (user.getToken().compareTo(token) == 0) {
            //Tokens match

            //Encrypt the password, set password and token
            user.setPswd(encryptPassword(password));
            user.setToken("");

            mav.setViewName("redirect:login?passwordchanged");

            //Update user
            loginDao.updateUser(user);

        } else {
            mav.setViewName("redirect:login?error=badtoken");
        }

        return mav;
        
    }
    
    /**
     * Encrypts a password string with BCrypt.
     * 
     * @param password Unencrypted password to encrypt
     * @return the encrypted password
     */
    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(11));
    }    
    
}
