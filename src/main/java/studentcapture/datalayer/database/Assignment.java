package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by c12ton on 4/26/16.
 */
public class Assignment {

    // Use this template to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public int createAssignment(String courseID, String assignmentTitle,
                                String startDate, String endDate, int minTime, int maxTime,
                                boolean published){
        //TODO
        return 0;
    }

    public List<Object> getAssignmentInfo(int assignmentID){
        //TODO
        return null;
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
