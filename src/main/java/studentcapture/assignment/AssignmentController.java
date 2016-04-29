package studentcapture.assignment;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by victor on 2016-04-28.
 */
@RestController
public class AssignmentController {

    @RequestMapping(value = "assignment", method = RequestMethod.POST)
    public AssignmentModel assignment(@RequestBody AssignmentModel assignment){
        return assignment;
    }
}
