package studentcapture.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import studentcapture.model.Settings;

/**
 * Created by Andreas Savva, Squad 8
 */
@RestController
@RequestMapping("/users")
public class UserSettingsResource {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SettingsDAO settingsDAO;

    /**
     * Get the active user settings.
     *
     * @param userID Identifier for a specific user.
     * @return
     * @author c13elt, sanna
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Settings> getUserSettings(@PathVariable(value = "userID") int userID) {
        if (userID <= 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        /* Get the settings object. */
        Settings settings = settingsDAO.getUserConfig(userID);
        //s.setEmail(userDAO.getEmail(userID));

        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

    /**
     * Takes user settings from client and sends them to the database.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveSettings(
            @RequestParam("userID") String userID,
            @RequestParam("language") String language,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("textSize") String textSize,
            @RequestParam("receiveEmails") boolean receiveEmails) {

        if (userID <= 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        //TODO: Implement communication with DAO.

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Changes a user's settings
     *
     * @param userID Identifier of the user to modify
     * @param c      An object containing all settings to be changed
     * @return True if everything went well, otherwise false
     * @author c13elt, sanna
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.PUT)
    public boolean setUserConfig(
            @PathVariable(value = "userID") int userID,
            @RequestBody Settings c) {

        /*
        boolean emailSaved = userDAO.setEmail(userID, c.getEmail());
        return emailSaved && settingsDAO.setUserConfig(userID, c);*/
        return true;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.DELETE)
    public void setDefaultSettings() {

    }

}
