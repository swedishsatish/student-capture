package studentcapture.feedback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by c12osn on 2016-04-26.
 */

@RestController
@RequestMapping(value = "feedback")
public class FeedbackFetchingController {

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String handleFeedbackRequestFromStudent(@RequestParam(value = "name", required = false) String name) {
        String responseToWeb;

        if (name == "Olle") {
            responseToWeb = "{error:student not found}";
        }else{
            responseToWeb = "{feedback: vg}";
        }
        return responseToWeb;
    }

}
