package test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldTest {
    @RequestMapping("/helloworld")
    public String hello(@RequestParam(value="name", defaultValue="World") String name) {
        return "Hello" + name;
    }
}