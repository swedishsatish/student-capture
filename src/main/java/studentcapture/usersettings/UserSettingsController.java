package studentcapture.usersettings;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by andreassavva on 2016-05-12.
 */
@RestController
public class UserSettingsController {

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
            @RequestParam("newUser") boolean newUser) {

        //TODO: Implement communication with DatalayerCommunicator.

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
