package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        String insertQueryString = "INSERT INTO Assignment (CourseID, Title, StartDate, EndDate, MinTime, MaxTime, Published) VALUES (?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertQueryString, new String[] {"AssignmentID"});
                    ps.setString(1, courseID);
                    ps.setString(2, assignmentTitle);
                    ps.setString(3, startDate);
                    ps.setString(4, endDate);
                    ps.setString(5, minTime);
                    ps.setString(6, maxTime);
                    ps.setBoolean(7, published);
                    return ps;
                },
                keyHolder);

        return keyHolder.getKey().intValue();
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
