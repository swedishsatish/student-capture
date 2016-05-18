package studentcapture.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestParam(value = "String")String value,
                                        @RequestParam(value = "int") int flag) {

        User user = userDAO.getUser(value,flag);

        if(user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    /**
     * Add a new user with given user object details
     * @param user
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addUser(
            @RequestParam(value="firstname", required = true)           String firstName,
            @RequestParam(value="lastname", required = true)            String lastName,
            @RequestParam(value="email", required = true)               String email,
            @RequestParam(value="username", required = true)            String username,
            @RequestParam(value="password", required = true)            String password) {
       
        User user = new User(username, firstName, lastName, email, password);
        
        boolean success = userDAO.addUser(user);

        if(!success) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
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
}
