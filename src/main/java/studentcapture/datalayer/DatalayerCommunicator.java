package studentcapture.datalayer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import studentcapture.datalayer.database.Assignment;
import studentcapture.datalayer.database.Submission;

/**
 * Created by c12osn on 2016-04-22.
 * Edited by c13arm, ens13ahr
 */
@RestController
@RequestMapping(value = "DB")
public class DatalayerCommunicator {



    @Autowired
    private Submission dbc;
    @Autowired
    private Assignment ass;

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
    public int createAssignment(@RequestParam(value = "courseID") String courseID,
                                @RequestParam(value = "assignmentTitle") String assignmentTitle,
                                @RequestParam(value = "startDate") String startDate,
                                @RequestParam(value = "endDate") String endDate,
                                @RequestParam(value = "minTime") String minTime,
                                @RequestParam(value = "maxTime") String maxTime,
                                @RequestParam(value = "published") boolean published){

        //int returnResult = ass.createAssignment(courseID, assignmentTitle, startDate, endDate, minTime, maxTime, published);

        return 1234;//returnResult;
    }
    /**
     * Save grade for a submission
     * @param assID
     * @param teacherID
     * @param studentID
     * @param grade
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "assID") String assID,
            				@RequestParam(value = "teacherID") String teacherID,
            				@RequestParam(value = "studentID") String studentID,
            				@RequestParam(value = "grade") String grade) {
    	
    	return false;
    }
    
    /**
     * Give feedback for a submission
     * @param assID
     * @param teacherID
     * @param studentID
     * @param feedbackVideo	Can be null
     * @param feedbackText	Can be null
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/giveFeedback", method = RequestMethod.POST)
    public boolean giveFeedback(@RequestParam(value = "assID") String assID,
            				@RequestParam(value = "teacherID") String teacherID,
            				@RequestParam(value = "studentID") String studentID/*,
            				@RequestParam(value = "feedbackVideo") video feedbackVideo,
            				@RequestParam(value = "feedbackText") text feedbackText*/){
    	
    	return false;
    }
}
