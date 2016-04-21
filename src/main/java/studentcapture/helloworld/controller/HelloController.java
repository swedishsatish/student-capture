package studentcapture.helloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import studentcapture.helloworld.model.HelloModel;

@RestController
public class HelloController {
	private HelloModel model = new HelloModel();
	
	@RequestMapping(value = "hello", method = RequestMethod.GET)
	public HelloModel hello() {
		return model;
	}
	
	@RequestMapping(value = "hello", method = RequestMethod.POST)
	public void hello(@RequestParam(value="greeting", required=false) String greeting) {
		if (greeting != null) {
			model.setGreeting(greeting);
		}
	}
}
