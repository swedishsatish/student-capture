package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.Assignment.AssignmentWrapper;
import studentcapture.datalayer.database.Course.CourseWrapper;
import studentcapture.datalayer.database.Submission.SubmissionWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public class User {


    private final String SQL_ADD_USR = "INSERT INTO users"
                                       + " (username, firstname, lastname, persnr, pswd)"
                                        + " VALUES (?, ?, ?, ?, ?)";

    private final String SQL_GET_USR_BY_ID = "SELECT  * FROM users WHERE userid = ?";
    private final String SQL_USR_EXIST     = "SELECT EXISTS (SELECT 1 FROM users "
                                           + "WHERE  username = ? AND pswd = ?)";

    private final String SQL_GET_PSWDS = "";
    private final String SQL_RM_USR   = "";

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Course course;
    @Autowired
    private Assignment assignment;
    @Autowired
    private Submission submission;



    /**
     * Add a new user to the User-table in the database.
     *
     * @param userName  unique identifier for a person
     * @param fName     First name of a user
     * @param lName     Last name of a user
     * @param pNr       Person-Number
     * @param pwd       Password
     * @return          true if success, else false.
     */
    public boolean addUser(String userName, String fName, String lName, String pNr, String pwd) {
        // TODO:
        //Check that user doesn't exist

        // Generate salt
        // Create idSalt by combining salt with user name
        // register user info with idSalt.

        Object[] args = new Object[] {userName, fName,lName,pNr,pwd};
        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                                      Types.CHAR,Types.VARCHAR};

        try {
            jdbcTemplate.update(SQL_ADD_USR, args,types);

        } catch (DataIntegrityViolationException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


    /**
     * Remove a user from the User-table in the database.
     *
     * @param username     unique identifier for a person
     * @return          true if the remove succeed, else false.
     */
    public String getUserID(String username){
    	String sql = "SELECT userID from users WHERE username = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{username},String.class);
    }

    public boolean removeUser(String casID) {
		throw new UnsupportedOperationException();
    }


    /**
     * Updates a user in the User-table in the database. Use Null-value for
     * those parameters that shouldn't update.
     *
     * @param fName     First name of a user
     * @param lName     Last name of a user
     * @param pNr       Person-Number
     * @param pWord     Password
     * @param casID     unique identifier for a person
     * @return          true if the update succeed, else false.
     */
    public boolean updateUser(String fName, String lName, String pNr,
                              String pWord, String casID) {
		throw new UnsupportedOperationException();
    }


    /**
     * Returns a list with info of a user.
     *
     * @param userID     unique identifier for a person
     * @return          The list with info of a person.
     */
    public HashMap<String,String> getUserByID(String userID) {

        String sql = "SELECT * FROM users WHERE userid = ?";

        Object[] arg = new Object[]{Integer.parseInt(userID)};
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcTemplate.queryForObject(SQL_GET_USR_BY_ID,
                        arg, new UserWrapper());

        return info;
    }


    /**
     * Check if a user exist by the name and password.
     * @return true if user exist, otherwise false.
     */
    public boolean userExist(String userName,String pswd) {

        boolean exist = jdbcTemplate.queryForObject(SQL_USR_EXIST,
                                                    new Object[] {userName,pswd},
                                                     Boolean.class);
        return exist;
    }

    public Optional<CourseAssignmentHierarchy> getCourseAssignmentHierarchy(
    		String userID) {
    	CourseAssignmentHierarchy hierarchy = new CourseAssignmentHierarchy();
    	int userId = Integer.parseInt(userID);
    	try {
    		addStudentHierarchy(hierarchy, userId);
    		addTeacherHierarchy(hierarchy, userId);
    		addUserToHierarchy(hierarchy, userId);	
	    } catch (IncorrectResultSizeDataAccessException e){
			System.out.println("HEJ");
		    return Optional.empty();
		} catch (DataAccessException e1){
			e1.printStackTrace();
			return Optional.empty();
		}
    	
    	return Optional.of(hierarchy);
    }

    private static final String getUserStatement = "SELECT * FROM Users WHERE "
    		+ "UserId=?";
    private void addUserToHierarchy(CourseAssignmentHierarchy hierarchy, int userId) {
    	Map<String, Object> map = jdbcTemplate.queryForMap(
    			getUserStatement, userId);
    	hierarchy.userId = (int) map.get("UserId");
    	hierarchy.firstName = (String) map.get("FirstName");
    	hierarchy.lastName = (String) map.get("LastName");
	}

    private static final String getTeacherHierarchyStatement = "SELECT * FROM "
    		+ "Participant AS par LEFT JOIN Course AS cou ON par.courseId="
    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
    		+ "ass.courseId LEFT JOIN Submission AS sub ON ass.assignmentId="
    		+ "sub.assignmentId WHERE par.userId=? AND par.function='Teacher'";
	private void addTeacherHierarchy(CourseAssignmentHierarchy hierarchy, int userId) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
    			getTeacherHierarchyStatement, userId);
    	for (Map<String, Object> row : rows) {
    		CoursePackage currentCourse;
    		String courseId = (String) row.get("CourseId");
    		try {
    			currentCourse = hierarchy.teacherCourses.get(courseId);
    			if(currentCourse==null)
    				throw new NullPointerException();
    		} catch (NullPointerException e) {
    			currentCourse = new CoursePackage();
    			currentCourse.course = course.getCourseWithWrapper(courseId);
    			hierarchy.teacherCourses.put(courseId, currentCourse);
    		}
    		
    		try {
    			AssignmentPackage currentAssignment;
        		int assignmentId = (int) row.get("AssignmentId");

    			try {
        			currentAssignment = currentCourse.assignments.get(assignmentId);

					if (currentAssignment == null) {
						throw new NullPointerException();
					}
        		} catch (NullPointerException e) {
        			currentAssignment = new AssignmentPackage();
        			currentAssignment.assignment = assignment
        					.getAssignmentWithWrapper(assignmentId).get();
        			currentCourse.assignments.put(assignmentId, currentAssignment);
        		}

				// TODO: unused variable
    			SubmissionWrapper currentSubmission;
    			Integer submissionId = (Integer) row.get("SubmissionId");

				if (submissionId != null) {
    				try {
    					currentSubmission = currentAssignment
    							.submissions.get(submissionId);
					} catch (Exception e) {
    					currentSubmission = submission.getSubmissionWithWrapper(
    							assignmentId,userId).get();
    				}
    			}
    		} catch (Exception e) {
    			//TODO
    		}
    	}
	}

	private static final String getStudentHierarchyStatement = "SELECT * FROM "
    		+ "Participant AS par LEFT JOIN Course AS cou ON par.courseId="
    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
    		+ "ass.courseId LEFT JOIN Submission AS sub ON par.userId="
    		+ "sub.studentId AND ass.assignmentId=sub.assignmentId WHERE "
    		+ "par.userId=? AND par.function='Student'";
	private void addStudentHierarchy(CourseAssignmentHierarchy hierarchy, int userId) {
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
    			getStudentHierarchyStatement, userId);
    	for (Map<String, Object> row : rows) {
    		CoursePackage currentCourse;
    		String courseId = (String) row.get("CourseId");
    		try {
    			currentCourse = hierarchy.studentCourses.get(courseId);
    			if(currentCourse==null)
    				throw new NullPointerException();
    		} catch (NullPointerException e) {
    			currentCourse = new CoursePackage();
    			currentCourse.course = course.getCourseWithWrapper(courseId);
    			hierarchy.studentCourses.put(courseId, currentCourse);
    		}
    		
    		try {
    			AssignmentPackage currentAssignment;
        		int assignmentId = (int) row.get("AssignmentId");

    			try {
        			currentAssignment = currentCourse.assignments.get(assignmentId);
        			if(currentAssignment==null)
        				throw new NullPointerException();
        		} catch (NullPointerException e) {
        			currentAssignment = new AssignmentPackage();
        			currentAssignment.assignment = assignment
        					.getAssignmentWithWrapper(assignmentId).get();
        			currentCourse.assignments.put(assignmentId, currentAssignment);
        		}
    			
    			SubmissionWrapper currentSubmission = null;
    			Integer submissionId = (Integer) row.get("SubmissionId");
    			if (submissionId != null) {
    				try {
    					currentSubmission = currentAssignment
    							.submissions.get(submissionId);
					} catch (Exception e) {
    					currentSubmission = submission.getSubmissionWithWrapper(
    							assignmentId,userId).get();
    					currentAssignment.submissions.put(submissionId, currentSubmission);
    				}
    			}
    		} catch (Exception e) {
    			//TODO
				throw new RuntimeException("Unimplemented catch-block triggered");
    		}
    		
    	}
	}

	/**
     *  Used to collect user information, and return a hashmap.
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap();
            info.put("userid",rs.getString("userid"));
            info.put("username",rs.getString("username"));
            info.put("lastname",rs.getString("lastname"));
            info.put("persnr",rs.getString("persnr"));
            info.put("pswd",rs.getString("pswd"));
            return info;
        }
    }

    public static class CourseAssignmentHierarchy {
    	public int userId;
    	public String firstName;
    	public String lastName;
    	public Map<String, CoursePackage> teacherCourses;
    	public Map<String, CoursePackage> studentCourses;
    	
    	public CourseAssignmentHierarchy() {
    		teacherCourses = new HashMap<>();
    		studentCourses = new HashMap<>();
    	}
    }

    public class CoursePackage {
    	public CourseWrapper course;
    	public Map<Integer, AssignmentPackage> assignments;
    	
    	public CoursePackage() {
    		assignments = new HashMap<>();
    	}
    }

    public class AssignmentPackage {
    	public AssignmentWrapper assignment = null;
    	public Map<Integer, SubmissionWrapper> submissions = null;
    }
}

