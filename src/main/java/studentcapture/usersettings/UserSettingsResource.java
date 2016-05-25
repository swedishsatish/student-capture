package studentcapture.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studentcapture.user.User;
import studentcapture.user.UserDAO;

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
     * Get the active user settings using DAOs.
     * @param userID    Identifier for a specific user.
     * @return          200 Ok and the Settings as JSON.
     *                  422 Unprocessable Entity.
     *                  500 Internal Server Error.
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSettings(
            @PathVariable(value = "userID") int userID) {

        /* Unsupported id. */
        if (userID <= 0) {
            return new ResponseEntity<>(
                    "Unable to process the contained instruction further.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        /* Get a specific user. */
        User user = userDAO.getUser(Integer.toString(userID), 1);
        if (user == null) {
            return new ResponseEntity<>(
                    "Could not get user from DAO.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /* Get the settings and add the email address. */
        Settings settings = settingsDAO.getUserConfig(userID);
        settings.setEmail(user.getEmail());

        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

    /**
     * Changes a user's settings
     *
     * @param userID    Identifier for a specific user.
     * @param settings An object containing all settings to be changed
     * @return True if everything went well, otherwise false
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setUserSettings(
            @PathVariable(value = "userID") int userID,
            @RequestBody Settings settings) {

        /* Unsupported id. */
        if (userID <= 0) {
            return new ResponseEntity<>(
                    "Unable to process the contained instruction further.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        /* Get a specific user. */
        User user = userDAO.getUser(Integer.toString(userID), 1);
        if (user == null) {
            return new ResponseEntity<>(
                    "Could not get user from DAO.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        user.setEmail(settings.getEmail());
        boolean emailSaved = userDAO.updateUser(user);
        boolean result = emailSaved && settingsDAO.setUserConfig(userID, settings);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setDefaultSettings(
            @PathVariable(value = "userID") int userID) {

        /* Unsupported id. */
        if (userID <= 0) {
            return new ResponseEntity<>(
                    "Unable to process the contained instruction further.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // TODO: Set default settings.

        return new ResponseEntity<>(
                "User settings set.",
                HttpStatus.OK);
    }

}