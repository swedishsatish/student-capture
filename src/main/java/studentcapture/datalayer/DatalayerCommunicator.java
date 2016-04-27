package studentcapture.datalayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import studentcapture.datalayer.database.Submission;

/**
 * Created by c12osn on 2016-04-22.
 */
@RestController
@RequestMapping(value = "DB")
public class DatalayerCommunicator {



    @Autowired
    private Submission dbc;
    // Not that into what this stuff do, but
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.POST)
    public MultiValueMap getGrade(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "course", required = false) String course,
                                  @RequestParam(value = "exam", required = false) String exam) {

        // Creates the object that should be returned
        LinkedMultiValueMap<String, String> returnData = new LinkedMultiValueMap<String, String>();


        // Do your DB and filesystem calls
        //String grade = dbc.returnGrade(1, 2);

        // Add what you want to return to the map here
        // EX: returndata.add("nyckel", variabel);
        // EX: returndata.add("grade", grade);


        // What is returned to the calling address
        return returnData;
    }

}
