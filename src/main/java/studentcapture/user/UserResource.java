package studentcapture.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * @param userName for the user
     * @return user object with a httpStatus.OK if successful
     *         else HttpStatus.NOT_FOUND
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> getUser(String userName) {

        User user = userDAO.getUser(userName,0);

        if(user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    /**
     *
     * @param user
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addUser(User user) {

        boolean success = userDAO.addUser(user);

        if(!success) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param user
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateUser(User user) {
        boolean success = userDAO.updateUser(user);

        if(!success) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
