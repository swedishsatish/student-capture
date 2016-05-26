package studentcapture.assignment;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Optional;

/**
 * Created by E&S on 4/26/16.
 */
@Repository
public class AssignmentDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate databaseConnection;

    /**
     * Inserts an assignment into the database.
     *
     * @param assignmentModel - The data a assignment consists of.
     * @return the generated AssignmentID
     * @throws IllegalArgumentException fails if startDate or endDate is not
     *                        in the right format
     */
    public int createAssignment(AssignmentModel assignmentModel)
            throws IllegalArgumentException, IOException {

        Integer assignmentID;

        // Construct query, depends on if assignment has publishdate or not.
        String insertQueryString = getInsertQueryString(assignmentModel.getAssignmentIntervall().getPublishedDate());

        // Execute query and fetch generated AssignmentID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        databaseConnection.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertQueryString,
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, assignmentModel.getCourseID());
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
                    assignmentModel.getDescription(), FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
            FilesystemInterface.storeAssignmentText(assignmentModel.getCourseID(), assignmentID.toString(),
                    assignmentModel.getRecap(), FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
        } catch (IOException e) {
            try {
                removeAssignment(assignmentModel.getCourseID(), assignmentModel.getAssignmentID());
            } catch (IOException e2) {
                throw new IOException("Could not store assignment and " +
                        "could not delete semi-stored assignment successfully. ");
            }
            throw new IOException("Could not store assignment successfully.");
        }

        return assignmentID;
    }

    private String getInsertQueryString(String published){
        String insertQueryString;

        if(published == null) {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?);";
        } else {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'),?,?," +
                    "to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'),?);";
        }
        return insertQueryString;
    }

    public void addAssignmentVideo(MultipartFile video, Integer courseID, String assignmentID) {
        FilesystemInterface.storeAssignmentVideo(courseID, assignmentID, video);
    }

    /**
     * Update an assignment, both in the database and in the file system.
     *
     * @param assignmentModel The assignment to update to.
     * @throws NotFoundException If the corresponding assignment is the database does not exist.
     * @throws IOException If files could not be stored successfully.
     */
    public void updateAssignment(AssignmentModel assignmentModel) throws NotFoundException, IOException {

        String updateQuery = "UPDATE Assignment SET " +
                "CourseID=?, " +
                "Title=?, " +
                "StartDate=to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                "EndDate=to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                "MinTime=?, " +
                "MaxTime=?, " +
                "Published=to_timestamp(?, 'YYYY-MM-DD HH24:MI:SS'), " +
                "GradeScale=? " +
                "WHERE AssignmentID=?;";

        int rowAffected = databaseConnection.update(updateQuery,
                assignmentModel.getCourseID(),
                assignmentModel.getTitle(),
                assignmentModel.getAssignmentIntervall().getStartDate(),
                assignmentModel.getAssignmentIntervall().getEndDate(),
                assignmentModel.getVideoIntervall().getMinTimeSeconds(),
                assignmentModel.getVideoIntervall().getMaxTimeSeconds(),
                assignmentModel.getAssignmentIntervall().getPublishedDate(),
                assignmentModel.getScale(),
                assignmentModel.getAssignmentID());

        if (rowAffected == 0) {
            throw new NotFoundException("Assignment not found.");
        }

        // Overwrite description and recap
        try {
            FilesystemInterface.storeAssignmentText(
                    assignmentModel.getCourseID(),
                    assignmentModel.getAssignmentID().toString(),
                    assignmentModel.getDescription(),
                    FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
            FilesystemInterface.storeAssignmentText(
                    assignmentModel.getCourseID(),
                    assignmentModel.getAssignmentID().toString(),
                    assignmentModel.getRecap(),
                    FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);
        } catch (IOException e) {
            throw new IOException("Could not store some data in the edited " +
                    "assignment correctly. Please try again.");
        }
    }

    /**
     * Gets the video question corresponding to the specified assignment.
     * @param assignmentID  Unique assignment identifier.
     * @return              The video and Http status OK or Http status NOT_FOUND.
     */
    public ResponseEntity<InputStreamResource> getAssignmentVideo(int assignmentID) {
        Optional<AssignmentModel> assignment = getAssignment(assignmentID);

        if (assignment.isPresent()) {
            String path = FilesystemInterface.generatePath(assignment.get());
            path = path.concat(FilesystemConstants.ASSIGNMENT_VIDEO_FILENAME);
            return FilesystemInterface.getVideo(path);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     *
     * !!!!
     * THIS METHOD IS NOT IN USE
     *
     * WILL BE USED AFTER DEMO
     * !!!!
     *
     * Method for checking that the user is enrolled on the course where the video is received.
     *
     * @param assignmentModel the assignment model
     * @return true or false
     */
    public boolean hasAccess(AssignmentModel assignmentModel){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();

        String userID = session.getAttribute("userid").toString();
        String accessQuery = "SELECT userid FROM participants WHERE userid = ? AND courseid = ? LIMIT 1;";


        //Needs more testing probably...
        KeyHolder keyHolder = new GeneratedKeyHolder();
        databaseConnection.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(accessQuery,
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, Integer.getInteger(userID));
                    ps.setInt(2, assignmentModel.getCourseID());
                    return ps;
                },
                keyHolder);

        return (Integer.getInteger(userID) == keyHolder.getKeys().get("userid"));
    }

//    /**
//     * Fetches info about an assignment from the database.
//     * @param assignmentID Unique identifier for an assignment.
//     * @return A list containing information about the assignment.
//     *      The list is on the form [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time]
//     */
//    public AssignmentModel getAssignmentInfo(int assignmentID){
//        ArrayList<String> returnValues = new ArrayList<>();
//
//        // Construct query
//        String columns[] = {"courseid", "title", "startdate", "enddate", "mintime", "maxtime"};
//        String tempVal;
//        String query;
//
//        // Execute query
//        try {
//            for (String c : columns) {
//                query = "SELECT " + c + " FROM assignment WHERE (assignmentid = ?);";
//                tempVal = databaseConnection.queryForObject(query, new Object[]{assignmentID}, String.class);
//
//                if (tempVal == null) {
//                    tempVal = "Missing value";
//                } else {
//                    tempVal = tempVal.trim();
//                }
//                returnValues.add(tempVal);
//            }
//        } catch (IncorrectResultSizeDataAccessException up) {
//            throw up;
//        } catch (DataAccessException down) {
//            throw down;
//        }
//        // Format results
//        AssignmentModel assignment = new AssignmentModel();
//        assignment.setCourseID(returnValues.get(0));
//        assignment.setTitle(returnValues.get(1));
//        assignment.setStartDate(new Timestamp(System.currentTimeMillis()));
//        assignment.setEndDate(new Timestamp(System.currentTimeMillis()));
//        assignment.setMinTime(Integer.parseInt(returnValues.get(4)));
//        assignment.setMaxTime(Integer.parseInt(returnValues.get(5)));
//
//        return assignment;
//    }

	public String getAssignmentID(String courseID,String assignmentTitle){
    	String sql = "SELECT assignmentID from Assignment WHERE courseID = ? AND Title = ?";
    	return databaseConnection.queryForObject(sql, new Object[]{courseID,assignmentTitle},String.class);
	}

    public String getCourseIDForAssignment(int assignmentID) {
        String sql = "SELECT courseID from Assignment WHERE assignmentID = ?";
        return databaseConnection.queryForObject(sql, new Object[]{assignmentID},String.class);
    }

    /**
     * Returns a sought assignment from the database.
     * 
     * @param assignmentID		assignments identifier
     * @return					sought assignment
     */
	public Optional<AssignmentModel> getAssignment(int assignmentID) {
		try {
            String getAssignmentStatement = "SELECT * FROM "
                    + "Assignment WHERE AssignmentId=?";

			Map<String, Object> map = databaseConnection.queryForMap(
	    			getAssignmentStatement, assignmentID);
			AssignmentModel result = new AssignmentModel(map);
	    	
	    	return Optional.of(result);
		} catch (IncorrectResultSizeDataAccessException e){
		    return Optional.empty();
		} catch (DataAccessException e1){
			return Optional.empty();
		}
	}

    /**
     * Returns a sought, published assignment from the database.
     *
     * @param assignmentID		assignments identifier
     * @return					sought assignment
     */
    public Optional<AssignmentModel> getPublishedAssignment(int assignmentID) {
        try {
            String getPublishedAssignmentStatement = "SELECT * FROM "
                    + "Assignment WHERE AssignmentId=? AND "
                    + "published < current_timestamp";

            Map<String, Object> map = databaseConnection.queryForMap(
                    getPublishedAssignmentStatement, assignmentID);
            AssignmentModel result = new AssignmentModel(map);

            return Optional.of(result);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
    }

    /**
     * Returns an AssignmentModel from the database.
     *
     * @param assignmentID          Assignment identifier
     * @return The AssignmentModel
     * @throws NotFoundException    If the assignment was not found.
     */
    public Optional<AssignmentModel> getAssignmentModel(int assignmentID) throws NotFoundException, IOException {

        String getAssignmentStatement = "SELECT * FROM "
                + "Assignment WHERE AssignmentId=?;";
        Object[] parameters = new Object[]{new Integer(assignmentID)};
        SqlRowSet srs = databaseConnection.queryForRowSet(getAssignmentStatement, parameters);

        if (!srs.next()){
            throw new NotFoundException("Assignment not found");
        }

        AssignmentVideoIntervall videoIntervall = new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls = new AssignmentDateIntervalls();

        videoIntervall.setMinTimeSeconds(srs.getInt("MinTime"));
        videoIntervall.setMaxTimeSeconds(srs.getInt("MaxTime"));
        assignmentIntervalls.setStartDate(srs.getString("StartDate").replaceAll("\\.\\d+", ""));
        assignmentIntervalls.setEndDate(srs.getString("EndDate").replaceAll("\\.\\d+", ""));

        if (srs.getString("Published") != null) {
            assignmentIntervalls.setPublishedDate(srs.getString("Published").replaceAll("\\.\\d+", ""));
        }

        int courseId = srs.getInt("courseId");

        AssignmentModel assignment = new AssignmentModel();
        assignment.setAssignmentID(assignmentID);
        assignment.setCourseID(courseId);
        assignment.setTitle(srs.getString("Title"));
        assignment.setVideoIntervall(videoIntervall);
        assignment.setAssignmentIntervall(assignmentIntervalls);
        assignment.setScale(srs.getString("GradeScale"));

        String description = FilesystemInterface.getAssignmentDescription(assignment);
        assignment.setDescription(description);

        String recap = FilesystemInterface.getAssignmentRecap(assignment);
        assignment.setRecap(recap);

        return Optional.of(assignment);
    }

    /**
     * Removes an assignment from the database.
     *
     * @param assignmentID  Assignment identifier
     * @return true if the assignment were removed, else false.
     */
    public boolean removeAssignment(int courseId, int assignmentID) throws IOException {
        int rowAffected = databaseConnection.update("DELETE FROM Assignment WHERE AssignmentId = ?", assignmentID);
        FilesystemInterface.deleteAssignmentFiles(courseId, assignmentID);
        return rowAffected > 0;
    }
}
