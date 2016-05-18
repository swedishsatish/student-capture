package studentcapture.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by Andreas Savva, Squad 8
 */
@RestController
public class UserSettingsResource {

    @Autowired
    private RestTemplate requestSender;

    /**
     * Takes user settings from client and sends them to the database.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
     */
    @CrossOrigin()
    @RequestMapping(value = "/settings",
            method = RequestMethod.POST)
    public ResponseEntity<String> saveSettings(
            @RequestParam("userID") String userID,
            @RequestParam("language") String language,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("textSize") String textSize,
            @RequestParam("receiveEmails") boolean receiveEmails){

        //TODO: Implement communication with DatalayerCommunicator.

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Returns user settings from the database.
     *
     * @param userID The user ID of requested user
     * @return A JSON object containing user setting information
     */
    @CrossOrigin()
    @RequestMapping(value = "/settings",
            method = RequestMethod.GET)
    public HashMap<String, String> receiveSettings(
            @RequestParam("userID") String userID) {

        HashMap<String, String> hashMap = new HashMap<>();

        //TODO: Implement communication with DatalayerCommunicator.

        // Hard coded values for testing.
        hashMap.put("userID", "1");
        hashMap.put("emailAddress", "benjamin@calleinc.se");
        hashMap.put("textSize", "16");
        hashMap.put("language","Swedish");
        hashMap.put("receiveEmails","true");

        // hashMap as JSON
        return hashMap;
    }
}
