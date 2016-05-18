package studentcapture.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import studentcapture.datalayer.database.SettingsDAO;
import studentcapture.datalayer.database.UserDAO;
import studentcapture.model.Settings;

import java.util.HashMap;

/**
 * Created by Andreas Savva, Squad 8
 */
@RestController
@RequestMapping("/users")
public class UserSettingsResource {

    @Autowired
    private RestTemplate requestSender;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SettingsDAO settingsDAO;

    /**
     * Takes user settings from client and sends them to the database.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
     */
    @CrossOrigin()
    @RequestMapping(value = "/{userID}/settings", method = RequestMethod.POST)
    public ResponseEntity<String> saveSettings(
            @RequestParam("userID") String userID,
            @RequestParam("language") String language,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("textSize") String textSize,
            @RequestParam("receiveEmails") boolean receiveEmails) {

        //TODO: Implement communication with DAO.

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Returns saved user settings for the config page
     *
     * @param userID Identifier of the user to get settings for
     * @return A Settings object containing all the information
     * @author c13elt, sanna
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.GET
    )
    public Settings getUserConfig(@PathVariable(value = "userID") int userID) {
        Settings c = settingsDAO.getUserConfig(userID);
        c.setEmail(userDAO.getEmail(userID));
        return c;
    }

    /**
     * Changes a user's settings
     *
     * @param userID Identifier of the user to modify
     * @param c      An object containing all settings to be changed
     * @return True if everything went well, otherwise false
     * @author c13elt, sanna
     */
    @CrossOrigin()
    @RequestMapping(value = "/{userID}/settings",
            method = RequestMethod.PUT)
    public boolean setUserConfig(@PathVariable(value = "userID") int userID,
                                 @RequestBody Settings c) {
        boolean emailSaved = userDAO.setEmail(userID, c.getEmail());
        return emailSaved && settingsDAO.setUserConfig(userID, c);
    }
}
