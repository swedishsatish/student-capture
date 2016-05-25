package studentcapture.course.participant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/courses/{courseID}/participants")
public class ParticipantResource {


    @Autowired
    private ParticipantDAO participantDAO;


    private boolean validRole(String role) {
        return (role.compareTo("assistant") == 0 || role.compareTo("teacher") == 0 || role.compareTo("student") == 0);
    }
    /**
     * Method gets a list with participants,it is possible to narrow down the list
     * by using the param "userRole".
     * @param courseID courseID
     * @param userRole student || teacher || assistant :defaultvalue is "all roles"
     * @return List of participants,HttpStatus
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    private ResponseEntity<List<Participant>> getParticipants(
            @PathVariable(value = "courseID") int courseID,
            @RequestParam(value = "userRole", required = false, defaultValue = "all roles") String userRole) {

        userRole = userRole.toLowerCase();
        if (!validRole(userRole) && (userRole.compareTo("all roles") != 0)) {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants(courseID, userRole);
        if (participants.isPresent()) {
            return new ResponseEntity<>(participants.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Method gets a participant.
     * @param courseID courseID
     * @param userID student || teacher || assistant
     * @return ResponseEntity with a participant,HttpStatus
     */
    @CrossOrigin
    @RequestMapping(value = "/{userID}", method = RequestMethod.GET)
    private ResponseEntity<Participant> getParticipant(
            @PathVariable(value = "courseID") int courseID,
            @PathVariable(value = "userID") int userID) {

        System.out.println("userID = "+userID);
        Optional<Participant> participant = participantDAO.getCourseParticipant(courseID, userID);
        if (participant.isPresent()) {
            return new ResponseEntity<>(participant.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Participant(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Method adds a participant to a course
     * @param courseID courseID
     * @param userID userID
     * @param userRole student || teacher || assistant
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    private ResponseEntity<String> addParticipant(
            @PathVariable(value = "courseID") int courseID,
            @RequestParam(value = "userID", required = true) int userID,
            @RequestParam(value = "userRole", required = true) String userRole) {
        if (userRole == null) {
            return new ResponseEntity<>("Participant role is not set", HttpStatus.BAD_REQUEST);
        }
        userRole = userRole.toLowerCase();
        if (!validRole(userRole)) {
            return new ResponseEntity<>("Participant is neither teacher,assistant or student", HttpStatus.BAD_REQUEST);
        }
        Participant participant = new Participant(userID, courseID, userRole);
        if (participantDAO.addParticipant(participant)) {
            return new ResponseEntity<>("Participant successfully added to course", HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR adding participant",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Method
     * @param courseID
     * @param userID
     * @param userRole
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    private ResponseEntity<String> editParticipant(
            @PathVariable(value = "courseID") String courseID,
            @RequestParam(value = "userID", required = true) String userID,
            @RequestParam(value = "userRole", required = true) String userRole) {

        ResponseEntity<String> errorEntity =
                new ResponseEntity<>("Participant role could not be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        if (userRole == null || userID == null) {
            return errorEntity;
        }
        userRole = userRole.toLowerCase();
        if (!validRole(userRole)) {
            return new ResponseEntity<>("Participant is neither teacher,assistant or student", HttpStatus.BAD_REQUEST);
        }
        Participant participant = new Participant();
        participant.setCourseId(Integer.parseInt(courseID));
        participant.setUserId(Integer.parseInt(userID));
        participant.setFunction(userRole);

        if (participantDAO.setParticipantFunction(participant)) {
            return new ResponseEntity<>("Participant role successfully updated", HttpStatus.OK);
        }
        return errorEntity;
    }

}