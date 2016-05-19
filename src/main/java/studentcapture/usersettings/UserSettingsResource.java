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
     * @author c13elt, sanna, tfy12ajn.
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
     * Initialize the user settings.
     * @param userID    Identifier for a specific user.
     * @return          200 Ok and .
     *                  422 Unprocessable Entity.
     *                  500 Internal Server Error.
     * @author tfy12ajn
     */
    @CrossOrigin
    @RequestMapping(
            value = "/{userID}/settings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveSettings(
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

        /* Set the default settings. */
        Settings settings = defaultSettings(user);

        /* Insert the settings into the back-end. */
        boolean succeeded = settingsDAO.setUserConfig(userID, settings);
        if (!succeeded) {
            return new ResponseEntity<>(
                    "Could not set user settings.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                "User settings set.",
                HttpStatus.OK);
    }

    /**
     * Changes a user's settings
     *
     * @param userID    Identifier for a specific user.
     * @param settings An object containing all settings to be changed
     * @return True if everything went well, otherwise false
     * @author c13elt, sanna
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

    /**
     * The default user settings.
     * Should be used when initializing user settings (POST),
     * and when deleting user settings (DELETE).
     * @param user A specific user.
     * @return The default settings object.
     */
    private Settings defaultSettings(User user) {
        Settings settings = new Settings();
        settings.setLanguage("English");
        settings.setEmail(user.getEmail()); // email already in
        settings.setMailUpdate(true);
        settings.setTextSize(12);
        return settings;
    }

}