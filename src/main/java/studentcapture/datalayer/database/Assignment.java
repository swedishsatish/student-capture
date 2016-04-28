package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by E&S on 4/26/16.
 */
@Repository
public class Assignment {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public int createAssignment(String courseID, String assignmentTitle,
                                String startDate, String endDate, int minTime, int maxTime,
                                boolean published){
        //TODO
        return 0;
    }

    /**
     * Fetches info about an assignment from the database.
     * @param assignmentID Unique identifier for an assignment.
     * @return A list containing information about the assignment.
     *      The list is on the form [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time]
     */
    public ArrayList<String> getAssignmentInfo(int assignmentID){
        ArrayList<String> returnValues = new ArrayList<>();

        // Construct query
        String columns[] = {"courseid", "title", "startdate", "enddate", "mintime", "maxtime"};
        String tempVal;
        String query;

        // Execute query
        try {
            for (String c : columns) {
                query = "SELECT " + c + " FROM assignment WHERE (assignmentid = ?)";
                tempVal = jdbcTemplate.queryForObject(query, new Object[]{assignmentID}, String.class);

                if (tempVal == null) {
                    tempVal = "Missing value";
                } else {
                    tempVal = tempVal.trim();
                }
                returnValues.add(tempVal);
            }
        } catch (IncorrectResultSizeDataAccessException up) {
            throw up;
        } catch (DataAccessException down) {
            throw down;
        }

        // Format results

        return returnValues;
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
