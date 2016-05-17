package studentcapture.assignment;

import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.datalayer.database.AssignmentDAO;
import studentcapture.datalayer.database.CourseDAO;
import studentcapture.datalayer.filesystem.FilesystemInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by victor on 2016-04-28.
 * Controller for assignments. At the moment only contains the method that newly created assignments should be posted
 * to.
 */
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

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
}
