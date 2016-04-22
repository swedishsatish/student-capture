package studentcapture.helloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import studentcapture.helloworld.model.HelloModel;
import studentcapture.helloworld.model.TestModel;

@RestController
public class HelloController {
	private HelloModel model = new HelloModel();
	private TestModel testModel = new TestModel();

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

	@RequestMapping(value = "message", method = RequestMethod.GET)
	public TestModel message() { return testModel; }

	@RequestMapping(value = "message", method = RequestMethod.POST)
	public void message(@RequestParam(value="message", required = false) String message){
		if(message != null){
			model.setMessage(message);
		}
	}
}
