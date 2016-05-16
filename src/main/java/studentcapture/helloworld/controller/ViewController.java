package studentcapture.helloworld.controller;

import groovyjarjarasm.asm.commons.Method;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * This class sends the html page when first entering the website.
 */
@Controller
public class ViewController {

    /**
     * This function gets called when the url root is paged. Returns the name of the starting page html file,
     * spring then sends this file to the browser. It's located in src/main/resources/templates.
     * @return Name of starting page html file.
     */
    @RequestMapping(value = "/")
    public String defaultIndexView() {
        return "index";
    }
    
    @RequestMapping(value = "/index")
    public String indexView() {
        return "index";
    }
    
    @RequestMapping(value = "/login")
    public String loginView() {
        return "login";
    }
    
    @RequestMapping(value = "/loggedin")
    public String loggedinView() {
        return "loggedin";
    }

}
