package studentcapture.datalayer;

//import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import studentcapture.datalayer.database.Assignment;
import studentcapture.datalayer.database.Course;
import studentcapture.datalayer.database.Submission;
import studentcapture.datalayer.database.User;
import studentcapture.datalayer.filesystem.FilesystemInterface;
//import studentcapture.video.VideoInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by c12osn on 2016-04-22.
 * Edited by c13arm, ens13ahr
 */
@RestController
@RequestMapping(value = "DB")
public class DatalayerCommunicator {



    @Autowired
    private Submission submission;
    @Autowired
    private Assignment assignment;
    @Autowired
    private Course course;
    @Autowired
    private User user;

    //@Autowired
    FilesystemInterface fsi;
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.POST)
    public MultiValueMap getGrade(@RequestParam(value = "studentID", required = false) String studentID,
                                  @RequestParam(value = "courseCode", required = false) String courseCode,
                                  @RequestParam(value = "courseID", required = false) String courseID,
                                  @RequestParam(value = "assignmentID", required = false) String assignmentID) {

        LinkedMultiValueMap<String, Object> returnData = new LinkedMultiValueMap<>();

        returnData.add("grade", submission.getGrade(studentID, assignmentID).get("grade"));
        returnData.add("time", submission.getGrade(studentID, assignmentID).get("time"));
        returnData.add("teacher",  submission.getGrade(studentID, assignmentID).get("teacher"));


        return returnData;
    }


    /**
     *
     * @param courseID
     * @param assignmentTitle
     * @param startDate
     * @param endDate
     * @param minTime
     * @param maxTime
     * @param published
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/createAssignment", method = RequestMethod.POST)
    public int createAssignment(//@RequestBody AssigmentModel assignment){ //will be used after merge
                                @RequestParam(value = "courseID") String courseID,
                                @RequestParam(value = "assignmentTitle") String assignmentTitle,
                                @RequestParam(value = "startDate") String startDate,
                                @RequestParam(value = "endDate") String endDate,
                                @RequestParam(value = "minTime") int minTime,
                                @RequestParam(value = "maxTime") int maxTime,
                                @RequestParam(value = "published") boolean published){
        int returnResult;

        try{
            returnResult = assignment.createAssignment(courseID, assignmentTitle,
                    startDate, endDate, minTime, maxTime, published);
        } catch (IllegalArgumentException e) {
            //TODO return smarter error msg
            return -1;
        }

        return returnResult;
    }

    /**
     * Save grade for a submission
     * @param assID Assignment identification
     * @param teacherID Teacher identification
     * @param studentID Student identification
     * @param grade Grade
     * @return  True if tables were updated, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "assID") String assID,
                            @RequestParam(value = "teacherID") String teacherID,
                            @RequestParam(value = "studentID") String studentID,
                            @RequestParam(value = "grade") String grade) {

        return submission.setGrade(Integer.parseInt(assID), teacherID, studentID, grade);
    }

    /**
     * Set feedback for a submission, video and text cannot both be null
     * @param assID Assignment identification
     * @param teacherID Teacher identification
     * @param studentID Student identification
     * @param feedbackVideo Video feedback
     * @param feedbackText Text feedback
     * @return True if feedback was saved, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
    public boolean setFeedback(@RequestParam(value = "assID") String assID,
                               @RequestParam(value = "teacherID") String teacherID,
                               @RequestParam(value = "studentID") String studentID,
                               @RequestParam(value = "feedbackVideo") String feedbackVideo,
                               @RequestParam(value = "feedbackText") String feedbackText) {
        int feedback = 0;
    	if(feedbackVideo != null) {
            // Call to filesystem API save feedback video
            feedback++;
        }
        if(feedbackText != null) {
            // Call to filesystem API save feedback text
            feedback++;
        }
        if(feedback == 0)
            return false;
        else
            return true;
    }

    /**
     * Sends the assignment video file.
     * @param courseCode    Courses 6 character identifier.
     * @param courseId      Courses unique database id.
     * @param assignmentId  Assignments unique database id.
     * @return              The video file vie http.
     */
    @CrossOrigin
    @RequestMapping(value = "/getAssignmentVideo", method = RequestMethod.POST, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideo(@RequestParam("courseCode") String courseCode,
                                                                  @RequestParam("courseId") String courseId,
                                                                  @RequestParam("assignmentId") int assignmentId) {

        ResponseEntity<InputStreamResource> responseEntity;

        try {
            FileInputStream videoInputStream = fsi.getAssignmentVideo(courseCode,courseId,assignmentId);

            byte []out = new byte[fsi.getAssignmentVideoFileSize (courseCode, courseId, assignmentId)];
            videoInputStream.read(out);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "inline; filename=AssignmentVideo" + assignmentId);

            responseEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            //TODO change HttpStatus to something bad?
            responseEntity = new ResponseEntity("File not found.", HttpStatus.OK);
        } catch (IOException e) {
            //TODO change HttpStatus to something bad?
            responseEntity = new ResponseEntity("Error getting file.", HttpStatus.OK);
        }

        return responseEntity;


    }

    /**
     * Fetches information about an assignment.
     * Description is mocked at the moment due to filesystem issues.
     * @param assID Unique identifier for the assignment
     * @return Array containing [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time, description]
     */
    @CrossOrigin
    @RequestMapping(value = "/getAssignmentInfo", method = RequestMethod.POST)
    public ArrayList<String> getAssignmentInfo(@RequestParam(value = "assID") int assID){

        ArrayList<String> results = assignment.getAssignmentInfo(assID);

        //Need the courseCode for the path
        //code for the filesystem
        /*String courseCode = course.getCourseCodeFromId(results.get(0));
        FileInputStream descriptionStream = fsi.getAssignmentDescription(courseCode, results.get(0), assID);
        Scanner scanner = new Scanner(descriptionStream);
        String description = "";

        //Construct description string
        while (scanner.hasNext()){
            description += scanner.nextLine() + "\n";
        }*/

        String description = "beskrivning";

        results.add(description);
        return results;
    }


    /**
     * Check if given user name and password exist in database.
     * @param username a unique user name.
     * @param pswd password for the unique username
     * @return true  if correct user password and username is given otherwise false
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public boolean login(@RequestParam(value = "username") String username,
                         @RequestParam(value = "pswd") String pswd) {
        return   user.userExist(username,pswd);
    }
}
