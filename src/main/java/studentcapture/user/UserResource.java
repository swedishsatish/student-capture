package studentcapture.user;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import studentcapture.login.ErrorFlags;

/**
 * Created by c12ton on 5/17/16.
 */
@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserDAO userDAO;

    /**
     * Get user object containing all related information of a user,
     * except user ID.
     * @param value for the user
     * @param flag 0 for getting user details by username
     *             1 for getting user details by userid.
     * @return user object with a httpStatus.OK if successful
     *         else HttpStatus.NOT_FOUND
     * @throws URISyntaxException 
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam(value = "String")String value,
                                        @RequestParam(value = "int") int flag) throws URISyntaxException {

        User user = userDAO.getUser(value,flag);

        if(user == null) {
            URI uri = new URI("/login?failed");            
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity(httpHeaders, HttpStatus.FOUND);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    /**
     * Add a new user with given user details
     * @param user
     * @return
     * @throws URISyntaxException 
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addUser(
            @RequestParam(value="firstname", required = true)           String firstName,
            @RequestParam(value="lastname", required = true)            String lastName,
            @RequestParam(value="email", required = true)               String email,
            @RequestParam(value="username", required = true)            String username,
            @RequestParam(value="password", required = true)            String password) throws URISyntaxException {
       
        User user = new User(username, firstName, lastName, email, encryptPassword(password));
        
        ErrorFlags status = userDAO.addUser(user);

        //Redirect to /login after the registration process is complete
        URI uri;
        HttpHeaders httpHeaders = new HttpHeaders();
        
        //Get the correct status message
        uri = new URI("/login?" + status.toString());
        httpHeaders.setLocation(uri);
        
        return new ResponseEntity(httpHeaders, HttpStatus.FOUND);
    }

    /**
     * Update a existing user by given user object details.
     * @param user an instance of User, which will replace old user by username
     * @return HttpStatus.Ok if succesfull else HttpStatus.NOT_FOUND
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestParam(value = "User")User user) {
        boolean success = userDAO.updateUser(user);

        if(!success) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
    
    /**
     * Encrypts password
     * @param password The input password
     * @return Encrypted password
     */
    protected String encryptPassword(String password) {
        String generatedPassword = BCrypt.hashpw(password, BCrypt.gensalt(11));
        return generatedPassword;
    }
}
