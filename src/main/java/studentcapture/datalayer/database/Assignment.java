package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
=======
>>>>>>> 17312bcfbb7b7c56550b01f1de7657ec69c2e440

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c12ton on 4/26/16.
 */
@Repository
public class Assignment {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;


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
    public int createAssignment(String courseID, String assignmentTitle, String startDate, String endDate,
                                String minTime, String maxTime, boolean published){
        //TODO
        return 0;
    }

    public ArrayList<String> getAssignmentInfo(int assignmentID){
        ArrayList info = new ArrayList<String>();
        for (int i = 0; i < 6; i++){
            info.add("a");
        }

        // Construct query

        // Execute query

        // Format results

        return info;
    }

    public boolean updateAssignment(String assignmentID, String assignmentTitle,
                                    String startDate, String endDate, int minTime, int maxTime,
                                    boolean published){
        //TODO
        return true;
    }

    public boolean removeAssignment(String assignmentID){
        //TODO
        return true;
    }

}
