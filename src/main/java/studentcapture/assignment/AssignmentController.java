package studentcapture.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import studentcapture.datalayer.database.CourseDAO;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.model.Course;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by victor on 2016-04-28.
 * Controller for assignments. At the moment only contains the method that newly created assignments should be posted
 * to.
 */
@RestController
public class AssignmentController {

    @Autowired
    private CourseDAO courseDAO;

    @RequestMapping(value = "/assignment", method = RequestMethod.POST)
    public String postAssignment(@RequestBody AssignmentModel assignment) throws MalformedURLException {
        RestTemplate rt = new RestTemplate();
        AssignmentErrorHandler assignmentErrorHandler = new AssignmentErrorHandler();
        String res;

        rt.setErrorHandler(assignmentErrorHandler);

        URL url = new URL("https://localhost:8443/DB/createAssignment");
        res = rt.postForObject(url.toString(), assignment, String.class);

        return res;
    }

    @RequestMapping(value = "/assignmentVideo", method = RequestMethod.POST)
    public void postAssignmentVideo(@RequestParam("video") MultipartFile video,
                               @RequestParam("courseID") String courseID,
                               @RequestParam("assignmentID") String assignmentID) {
        System.out.println("Success! " + courseID + " " + assignmentID);
        Course course = new Course();
		course.setCourseId(courseID);
        String courseCode = courseDAO.getCourseCodeFromId(course);

        FilesystemInterface.storeAssignmentVideo(courseCode, courseID, assignmentID, video);

    }
}
