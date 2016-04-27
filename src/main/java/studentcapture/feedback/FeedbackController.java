package studentcapture.feedback;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by c12osn on 2016-04-26.
 */

@RestController
@RequestMapping(value = "feedback")
public class FeedbackController {



    @Autowired
    private RestTemplate requestSender;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String handleFeedbackRequestFromStudent(@RequestParam(value = "courseID", required = false) String course,
                                                   @RequestParam(value = "studentID", required = false) String student,
                                                   @RequestParam(value = "examID", required = false) String exam) {
        //TODO Unsafe data needs to be cleaned
        HashMap<String, String> params = new HashMap<>();
        params.put("courseID", course);
        params.put("studentID", student);
        params.put("examID", exam);
        return requestSender.getForObject("http://localhost:8080/DB/getGrade", String.class, params);
    }


}
