/**
 * Sprint       1
 * Group        8
 * Date:        27/4-2016
 *
 * User stories covered in this class:
 * [Student](9): As a student I want to be able to leave a blank answer to a question.
 * [Student](10.1): As a student I want to view the accepted starting time for each upcoming exam.
 * https://github.com/student-capture/student-capture/issues
 *
 * TODO: Not fully implemented.
 */
package studentcapture.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

/**
 * RESTController that provide the service requested by the user
 * stories mentioned above.
 * TODO: Merge with other assignment controllers (?).
 * TODO: RequestMapping() args to be changed (sprint 2).
 */
@RestController
@RequestMapping("assignment2")
public class AController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * DatabaseLayer using a mock for required method, not sure how
     * using a mock on a mock really works.
     * TODO: Look for exact interface to talk to DB.
     * TODO: Check @return from db.
     * @return At minimum, the start and stop date for an exam as JSON.
     */
    @RequestMapping(value = "startTime", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public HashMap<String, String> getStartTime(@RequestParam(value = "courseID") String courseID,
                                                @RequestParam(value = "assignmentID") String assignmentID) {
        // Get the db address.
        URI targetUrl = getStartTimeAddress(courseID, assignmentID);
        HashMap<String, String> response;
        try {
            // Send a POST request at the specified url (answer or null)
            // TODO: Not working.
            response = restTemplate.postForObject(targetUrl, null, HashMap.class);

        } catch (Exception e) {
            response = new HashMap<>();
            response.put("exception", e.getMessage());
        }
        return response;
    }

    /**
     * Get the database address to the controller containing the information.
     * address: https://localhost:8443/DB/getAssignmentInfo/courseID/assignmentID
     *
     * TODO: Database using a mock, and the wiki interface is changed.
     * git.wiki interface: List getAssignmentInfo(courseID, assignmentID)
     *
     * @param courseID The course id.
     * @param assignmentID The assignment id.
     * @return Assignment information corresponding to the id's.
     */
    private URI getStartTimeAddress(String courseID, String assignmentID) {
        //TODO Change hard coded address away from "localhost"
        String rootPath = "https://localhost:8443";
        String dbAssignmentPath = "DB/getAssignmentInfo";
        String param1 = "courseID";
        String param2 = "assignmentID";

        return UriComponentsBuilder
                .fromUriString(rootPath).path(dbAssignmentPath)
                //.queryParam(param1, courseID)
                .queryParam(param2, assignmentID)
                .build().toUri();
    }


    /**
     * TODO: Conceptual controller tested (with view), need DB communication.
     */
    @RequestMapping(
            value = "/emptyAnswer",
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8")
    public void setAnswer() {
        //TODO: Talk to  DB.
    }

}