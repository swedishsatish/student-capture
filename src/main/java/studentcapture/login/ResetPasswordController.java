package studentcapture.login;

import java.net.URI;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.mail.MailClient;

/**
 * Controller for handling HTTP requests related to the login page.
 * @author dv11osi, c13hbd
 */
@RestController
public class ResetPasswordController {
	
	private static final String dbURI = "https://localhost:8443";
    
    @Autowired
    private RestTemplate requestSender;
    
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
        if(checkEmailExistsWithUserName(email,username)){
            System.out.println("success!");
        
        
        //Generate link
        //Spring generate token http://www.baeldung.com/spring-security-registration-i-forgot-my-password
        
        //Store token in db ?
        
        
        //Email link
        //Spring or custom mail?
        
        
        MailClient mailClient = new MailClient();
        //mailClient.send(String to, String from, String subject, String msg)
        mailClient.send("receiver", "sender", "Reset Password", tokenUrl);
        
        
        //Compare header token with db token <-- another method?
        
        }else{
            System.out.println("fail...");
            mav.setViewName("redirect:login?error=invaliduser");

        }

        
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
    
    
}
