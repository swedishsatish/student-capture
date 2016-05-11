package studentcapture.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import studentcapture.datalayer.*;

@RestController
public class LoginDBController {
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void registerUser(
							 @RequestParam(value="firstname", required = true) String firstName,
							 @RequestParam(value="lastname", required = true)  String lastName,
							 @RequestParam(value="email", required = true)     String email,
							 @RequestParam(value="username", required = true)  String username,
							 @RequestParam(value="password", required = true)  String password
							 ) {
		UserAccount user = new UserAccount(firstName, lastName, email, username, password);
	}
}
