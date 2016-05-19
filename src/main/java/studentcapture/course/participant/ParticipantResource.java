package studentcapture.course.participant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by bio11lrm on 2016-05-17.
 */

@RestController
@RequestMapping(value = "/courses/{courseID}/participants")
public class ParticipantResource {


    @Autowired
    private ParticipantDAO participantDAO;


    /**
     *
     * @param courseID
     * @param userRole student || teacher || assistant :defaultvalue is "all roles"
     * @param userID   if userID is set the method fetches specific participant on course
     *                 if userID is not set the method fetches all participants on course
     * @return List of participants
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    private ResponseEntity<List<Participant>> getParticipants(
            @PathVariable(value = "courseID") String courseID,
            @RequestParam(value = "userID",required = false, defaultValue = "many users") String userID,
            @RequestParam(value = "userRole",required = false,defaultValue = "all roles") String userRole) {

        if (userID == null || userRole == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userRole = userRole.toLowerCase();
        if (!validRole(userRole) && userRole.compareTo("all roles") != 0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants(courseID,userID,userRole);
        if (participants.isPresent()) {
            return new ResponseEntity<List<Participant>>(participants.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    private ResponseEntity<String> addParticipant(
            @PathVariable(value = "courseID") String courseID,
            @RequestParam(value = "userID",required = true) String userID,
            @RequestParam(value = "userRole",required = true) String userRole) {

        ResponseEntity<String> errorEntity =
                new ResponseEntity("Participant could not be added to course",HttpStatus.INTERNAL_SERVER_ERROR);
        if(userRole == null || userID == null){
            return errorEntity;
        }
        userRole = userRole.toLowerCase();
        if(!validRole(userRole)){
            return new ResponseEntity<String>("Participant is neither teacher,assistant or student",HttpStatus.BAD_REQUEST);
        }
        if(participantDAO.addParticipant(userID,courseID,userRole)){
            return new ResponseEntity("Participant successfully added to course",HttpStatus.OK);
        }
        return errorEntity;
    }

    private boolean validRole(String role){
        return (role.compareTo("assistant") == 0 || role.compareTo("teacher") == 0 || role.compareTo("student") == 0);
    }
}