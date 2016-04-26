package login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    
    //private LoginModel model;
    
    //@RequestMapping("/login")
    /*
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public LoginModel login() {
        return model;
    }
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void login(@RequestParam(value="text", required=false) String text){
        if (text != null) {
            model.setText(text);
        }
    }
    */
    
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView loginPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Login page");           
        model.addObject("message", "This is a login page");
        model.setViewName("login");
        return model;
    }
    
    @RequestMapping(value = {"/loggedin"}, method = RequestMethod.GET)
    public ModelAndView loggedinPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Loggedin page");
        model.addObject("message", "You are logged in!");
        model.setViewName("loggedin");
        return model;
    }
    
    
   // @RequestMapping(value = "loggedin", method = RequestMethod.GET)
}
