package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.Assignment.AssignmentWrapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by E&S on 4/26/16.
 */
@Repository
public class Assignment {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;


    /**
     * Inserts an assignment into the database.
     *
     * @param courseID        the courseID existing in the course-table
     * @param assignmentTitle the title to the assignment
     * @param startDate       the startdate of the assignment, should be on
     *                        format "YYYY-MM-DD HH:MI:SS"
     * @param endDate         the enddate of the assignment, should be on
     *                        format "YYYY-MM-DD HH:MI:SS"
     * @param minTime         minimum time for the assignment, in seconds
     * @param maxTime         maximum time for the assignment, in seconds
     * @param published       true if the assignment should be published
     * @return the generated AssignmentID
     * @throws ParseException fails if startDate or endDate is not in the
     *                        right format
     */
    public int createAssignment(String courseID, String assignmentTitle,
                                String startDate, String endDate,
                                int minTime, int maxTime, boolean published)
    throws IllegalArgumentException {

        // Check dates
        try {
            if (!isValidDateFormat("yyyy-MM-dd HH:mm:ss", startDate)) {
                throw new IllegalArgumentException("startDate is not in " +
                        "format YYYY-MM-DD HH:MI:SS");
            }
            if (!isValidDateFormat("yyyy-MM-dd HH:mm:ss", endDate)) {
                throw new IllegalArgumentException("endDate is not in " +
                        "format YYYY-MM-DD HH:MI:SS");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date formatting failed");
        }

        // Check time
        if (minTime >= maxTime) {
            throw new IllegalArgumentException("minTime must be less than " +
                    "maxTime");
        }
        if (minTime < 0) {
            throw new IllegalArgumentException("minTime must be greater or " +
                    "equal to 0");
        }
        if (maxTime <= 0) {
            throw new IllegalArgumentException("maxTime must be greater " +
                    "than 0");
        }

        // Construct query
        String insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                "Published) VALUES (DEFAULT ,?,?, " +
                "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'), " +
                "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?,?,?);";

        // Execute query and fetch generated AssignmentID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertQueryString,
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, courseID);
                    ps.setString(2, assignmentTitle);
                    ps.setString(3, startDate);
                    ps.setString(4, endDate);
                    ps.setInt(5, minTime);
                    ps.setInt(6, maxTime);
                    ps.setBoolean(7, published);
                    return ps;
                },
                keyHolder);

        // Return generated AssignmentID
        return (int) keyHolder.getKeys().get("assignmentid");
        //return 1;
    }

    /**
     * Used to verify if a given date is in the right format.
     *
     * @param format The format to check against.
     * @param value The date to check.
     * @return True if the date follows the format, false if not.
     */
    private static boolean isValidDateFormat(String format, String value)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(value);

        return value.equals(sdf.format(date));
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
                query = "SELECT " + c + " FROM assignment WHERE (assignmentid = ?);";
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

	public String getAssignmentID(String courseID,String assignmentTitle){
    	String sql = "SELECT assignmentID from Assignment WHERE courseID = ? AND Title = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{courseID,assignmentTitle},String.class);
	}

    public String getCourseIDForAssignment(String assignmentID) {
        String sql = "SELECT courseID from Assignment WHERE assignmentID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{assignmentID},String.class);
    }

    private static final String getAssignmentStatement = "SELECT * FROM "
    		+ "Assignment WHERE AssignmentId=?";
	public Optional<AssignmentWrapper> getAssignmentWithWrapper(
			int assignmentId) {
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(
	    			getAssignmentStatement, assignmentId);
			AssignmentWrapper result = new AssignmentWrapper();
	    	result.assignmentId = (int) map.get("AssignmentId");
	    	result.courseId = (String) map.get("CourseId");
	    	
	    	result.title = (String) map.get("Title");
	    	result.StartDate = map.get("StartDate").toString();
	    	result.EndDate = map.get("EndDate").toString();
	    	result.minTime = (int) map.get("MinTime");
	    	result.maxTime = (int) map.get("MaxTime");
	    	result.published = (boolean) map.get("Published");
	    	
	    	return Optional.of(result);
		} catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}
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

    public static class AssignmentWrapper {
    	public int assignmentId;
    	public String courseId;
    	public String title;
    	public String StartDate;
    	public String EndDate;
    	public int minTime;
    	public int maxTime;
    	public boolean published;
    }
}
