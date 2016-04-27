package studentcapture.feedback;

import com.fasterxml.jackson.annotation.JacksonInject;

import java.net.URI;

import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public String handleFeedbackRequestFromStudent(@RequestParam(value = "userID", required = false) String userID,
                                                   @RequestParam(value = "assID", required = false) String assID) {
        //TODO Unsafe data needs to be cleaned

        URI targetUrl = UriComponentsBuilder.fromUriString("https://localhost:8443")
                .path("DB/getGrade")
                .queryParam("userID", userID)
                .queryParam("assID", assID)
                .build()
                .toUri();
        String response = null;
        try {
            response = requestSender.getForObject(targetUrl, String.class);
        } catch (RestClientException e) {
            //TODO Maybe not good to send exceptions to browser?
            response = "{error:" + e.getMessage() + "}";
        }
        return response;
    }


}
