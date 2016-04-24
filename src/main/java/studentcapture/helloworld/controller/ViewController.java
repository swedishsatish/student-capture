package studentcapture.helloworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
    public String index() {
        return "index";
    }
}
