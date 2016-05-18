package studentcapture.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.datalayer.filesystem.FilesystemInterface;

/**
 * Created by victor on 2016-04-28.
 * RestController for assignments.
 */
@RestController
@RequestMapping("/assignments")
public class AssignmentResource {

    @Autowired
    private AssignmentDAO assignmentDAO;

    @RequestMapping(method = RequestMethod.POST)
    public int createAssignment(@RequestBody AssignmentModel assignment) {
        return assignmentDAO.createAssignment(assignment);
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public void addAssignmentVideo(@RequestParam("video") MultipartFile video,
                                   @RequestParam("courseID") String courseID,
                                   @RequestParam("assignmentID") String assignmentID) {
        assignmentDAO.addAssignmentVideo(video, courseID, assignmentID);
    }

    /**
     * Gets the video question corresponding to the specified assignment.
     * @param assignmentID  Unique assignment identifier.
     * @return              The video and Http status OK
     *                      or Http status NOT_FOUND.
     */
    @RequestMapping(value = "/{assignmentID}/video",
                    method = RequestMethod.GET,
                    produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideo(
            @PathVariable("assignmentID") int assignmentID) {
        return assignmentDAO.getAssignmentVideo(assignmentID);
    }
}
