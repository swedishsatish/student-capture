package studentcapture.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentModel;
import studentcapture.course.CourseModel;
import studentcapture.course.CourseDAO;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.model.Assignment;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by E&S on 4/26/16.
 */
@Repository
public class AssignmentDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Inserts an assignment into the database.
     *
     * @param assignmentModel - The data a assignment consists of.
     * @return the generated AssignmentID
     * @throws IllegalArgumentException fails if startDate or endDate is not
     *                        in the right format
     */
    public int createAssignment(AssignmentModel assignmentModel)
    throws IllegalArgumentException {

        Integer assignmentID;
        String courseCode;

        // Construct query, depends on if assignment has publishdate or not.
        String insertQueryString = getQueryString(assignmentModel.getAssignmentIntervall().getPublishedDate());

        // Execute query and fetch generated AssignmentID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertQueryString,
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, assignmentModel.getCourseID());
                    ps.setString(2, assignmentModel.getTitle());
                    ps.setString(3, assignmentModel.getAssignmentIntervall().getStartDate());
                    ps.setString(4, assignmentModel.getAssignmentIntervall().getEndDate());
                    ps.setInt(5, assignmentModel.getVideoIntervall().getMinTimeSeconds());
                    ps.setInt(6, assignmentModel.getVideoIntervall().getMaxTimeSeconds());
                    ps.setString(7, assignmentModel.getAssignmentIntervall().getPublishedDate());
                    ps.setString(8, assignmentModel.getScale());
                    return ps;
                },
                keyHolder);

        // Return generated AssignmentID
        //This is a work around, keyHolder has several keys which it shouldn't
        if (keyHolder.getKeys().size() > 1) {
            assignmentID = (int) keyHolder.getKeys().get("assignmentid");
        } else {
            //If only one key assumes it is assignmentid.
            assignmentID = keyHolder.getKey().intValue();
        }


        try {
            FilesystemInterface.storeAssignmentText(assignmentModel.getCourseID(), assignmentID.toString(),
                    assignmentModel.getInfo(), FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
            FilesystemInterface.storeAssignmentText(assignmentModel.getCourseID(), assignmentID.toString(),
                    assignmentModel.getRecap(), FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
        } catch (IOException e) {
            //TODO: HANDLE THIS
        }

        return assignmentID;
    }


    private String getQueryString(String published){
        String insertQueryString;

        if(published == null) {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?,?,?,?);";
        } else {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?,?," +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?);";
        }
        return insertQueryString;
    }

    public void addAssignmentVideo(MultipartFile video, String courseID, String assignmentID) {
        FilesystemInterface.storeAssignmentVideo(courseID, assignmentID, video);
    }

    /**
     * Used to verify if a given date is in the right format.
     *
     * @param format The format to check against.
     * @param value The date to check.
     * @return True if the date follows the format, false if not.
     */
    /*private static boolean isValidDateFormat(String format, String value)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(value);

        return value.equals(sdf.format(date));
    }
*/
    /**
     * Fetches info about an assignment from the database.
     * @param assignmentID Unique identifier for an assignment.
     * @return A list containing information about the assignment.
     *      The list is on the form [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time]
     */
    public Assignment getAssignmentInfo(int assignmentID){
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
        Assignment assignment = new Assignment();
        assignment.setCourseID(returnValues.get(0));
        assignment.setTitle(returnValues.get(1));
        assignment.setStartDate(new Timestamp(System.currentTimeMillis()));
        assignment.setEndDate(new Timestamp(System.currentTimeMillis()));
        assignment.setMinTime(Integer.parseInt(returnValues.get(4)));
        assignment.setMaxTime(Integer.parseInt(returnValues.get(5)));

        return assignment;
    }

	public String getAssignmentID(String courseID,String assignmentTitle){
    	String sql = "SELECT assignmentID from Assignment WHERE courseID = ? AND Title = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{courseID,assignmentTitle},String.class);
	}

    public String getCourseIDForAssignment(String assignmentID) {
        String sql = "SELECT courseID from Assignment WHERE assignmentID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{assignmentID},String.class);
    }

    /**
     * Returns a sought assignment from the database.
     * 
     * @param assignmentId		assignments identifier
     * @return					sought assignment
     * 
     * @author tfy12hsm
     */
	public Optional<AssignmentModel> getAssignment(int assignmentId) {
		try {
            String getAssignmentStatement = "SELECT * FROM "
                    + "Assignment WHERE AssignmentId=?";

			Map<String, Object> map = jdbcTemplate.queryForMap(
	    			getAssignmentStatement, assignmentId);
			AssignmentModel result = new AssignmentModel(map);
	    	
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
        throw new UnsupportedOperationException();
    }

    public boolean removeAssignment(String assignmentID){
        throw new UnsupportedOperationException();
    }
}
