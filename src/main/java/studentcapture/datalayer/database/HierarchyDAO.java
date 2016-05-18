package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.assignment.AssignmentDAO;
import studentcapture.course.CourseModel;
import studentcapture.course.CourseDAO;
import studentcapture.model.Hierarchy;
import studentcapture.submission.Submission;
import studentcapture.model.Hierarchy.AssignmentPackage;
import studentcapture.model.Hierarchy.CoursePackage;
import studentcapture.submission.SubmissionDAO;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository

/**
 * Aware of code smell.
 * 
 * @author tfy12hsm
 *
 */
public class HierarchyDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private AssignmentDAO assignmentDAO;
    @Autowired
    private SubmissionDAO submissionDAO;
    @Autowired
    private UserDAO userDAO;


    /**
     * Returns an hierarchy of data, retrieved from the database, related to
     * courses, assignments and submissions a user is participating in.
     *
     * @param userID	users identifier
     * @return			hierarchy of course, assignment and submission data
     * 
     * @author tfy12hsm
     */
    public Optional<Hierarchy> getCourseAssignmentHierarchy(
    		String userID) {
    	Hierarchy hierarchy = new Hierarchy();
    	int userId = Integer.parseInt(userID);
    	try {
    		addStudentHierarchy(hierarchy, userId);
    		addTeacherHierarchy(hierarchy, userId);
    		addUserToHierarchy(hierarchy, userId);
    		//hierarchy.moveMapsToLists();
	    } catch (IncorrectResultSizeDataAccessException e){
	    	e.printStackTrace();
		    return Optional.empty();
		} catch (DataAccessException e1){
			e1.printStackTrace();
			return Optional.empty();
		}
    	
    	return Optional.of(hierarchy);
    }

    /**
	 * Adds user data, related to a given user,
	 * from the database to a {@link CourseAssignmentHierarchy}
	 *
	 * @param hierarchy		hierarchy added to
	 * @param userId		student identifier
	 * 
     * @author tfy12hsm
	 */
    private void addUserToHierarchy(Hierarchy hierarchy,
    		int userId) {
    	String getUserStatement = "SELECT * FROM Users WHERE "
        		+ "UserId=?";

    	Map<String, Object> map = jdbcTemplate.queryForMap(
    			getUserStatement, userId);
    	hierarchy.setUserId((int) map.get("UserId"));
    	hierarchy.setFirstName((String) map.get("FirstName"));
    	hierarchy.setLastName((String) map.get("LastName"));
	}

    /**
	 * Adds course, assignment and submission data, related to a given teacher,
	 * from the database to a {@link CourseAssignmentHierarchy}
	 *
	 * @param hierarchy		hierarchy added to
	 * @param userId		teacher identifier
     * 
     * @author tfy12hsm
	 */
	private void addTeacherHierarchy(Hierarchy hierarchy, int
			userId) {

		String getTeacherHierarchyStatement = "SELECT par.courseId AS "
				+ "CourseId,ass.assignmentId AS AssignmentId,"
				+ "sub.submissionDate AS SubmissionDate,sub.studentId"
				+ " AS StudentId FROM Participant AS par"
				+ " LEFT JOIN Course AS cou ON par.courseId="
	    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
	    		+ "ass.courseId LEFT JOIN Submission AS sub ON "
	    		+ "ass.assignmentId=sub.assignmentId WHERE par.userId=? AND "
	    		+ "par.function='Teacher'";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
    			getTeacherHierarchyStatement, userId);
    	for (Map<String, Object> row : rows) {
    		CoursePackage currentCourse;
    		String courseId = (String) row.get("CourseId");
    		try {
    			currentCourse = hierarchy.getTeacherCourses().get(courseId);
    			if(currentCourse==null)
    				throw new NullPointerException();
    		} catch (NullPointerException e) {
    			currentCourse = new CoursePackage();
    			CourseModel course = new CourseModel();
    			course.setCourseId(courseId);
    			currentCourse.setCourse(courseDAO.getCourse(course));
    			hierarchy.getTeacherCourses().put(courseId, currentCourse);
    		}

    		AssignmentPackage currentAssignment;

    		try {
        		int assignmentId = (int) row.get("AssignmentId");

    			try {
        			currentAssignment = currentCourse.getAssignments()
        					.get(assignmentId);

					if (currentAssignment == null) {
						throw new NullPointerException();
					}
        		} catch (NullPointerException e) {
        			currentAssignment = new AssignmentPackage();
        			currentAssignment.setAssignment(assignmentDAO
        					.getAssignment(assignmentId).get());
        			currentAssignment.setSubmissions(new HashMap<>());
        			currentCourse.getAssignments()
        					.put(assignmentId, currentAssignment);
        		}
    			
    			Submission currentSubmission;
    			Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
    			Integer studentId = (Integer) row.get("StudentId");
				if (submissionDate != null) {
    				try {
    					currentSubmission = currentAssignment.getSubmissions().get(studentId);
    					if(currentSubmission==null)
    						throw new NullPointerException();
					} catch (NullPointerException e) {
    					currentSubmission = submissionDAO.getSubmission(
    							assignmentId,studentId).get();
    					currentAssignment.getSubmissions().put(studentId,
    							currentSubmission);
    				}
    			}
    		} catch (NullPointerException e) {
    			continue;
    		}
    	}
	}

	/**
	 * Adds course, assignment and submission data, related to a given student,
	 * from the database to a {@link CourseAssignmentHierarchy}
	 *
	 * @param hierarchy		hierarchy added to
	 * @param userId		student identifier
     * 
     * @author tfy12hsm
	 */
	private void addStudentHierarchy(Hierarchy hierarchy,
			int userId) {
		String getStudentHierarchyStatement = "SELECT par.courseId AS CourseId,"
				+ "ass.assignmentId AS AssignmentId,sub.submissionDate AS "
				+ "SubmissionDate,sub.studentId AS StudentId "
	    		+ "FROM Participant AS par LEFT JOIN Course AS cou ON par.courseId="
	    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
	    		+ "ass.courseId LEFT JOIN Submission AS sub ON par.userId="
	    		+ "sub.studentId AND ass.assignmentId=sub.assignmentId WHERE "
	    		+ "par.userId=? AND par.function='Student'";

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
    			getStudentHierarchyStatement, userId);
    	for (Map<String, Object> row : rows) {
    		CoursePackage currentCourse;
    		String courseId = (String) row.get("CourseId");
    		try {
    			currentCourse = hierarchy.getStudentCourses().get(courseId);
    			if(currentCourse==null)
    				throw new NullPointerException();
    		} catch (NullPointerException e) {
    			currentCourse = new CoursePackage();
    			CourseModel course = new CourseModel();
    			course.setCourseId(courseId);
    			currentCourse.setCourse(courseDAO.getCourse(course));
    			hierarchy.getStudentCourses().put(courseId, currentCourse);
    		}

    		AssignmentPackage currentAssignment;
    		try {
        		int assignmentId = (int) row.get("AssignmentId");

    			try {
        			currentAssignment = currentCourse.getAssignments().get(
        					assignmentId);
        			if(currentAssignment==null)
        				throw new NullPointerException();
        		} catch (NullPointerException e) {
        			currentAssignment = new AssignmentPackage();
        			currentAssignment.setAssignment(assignmentDAO
        					.getAssignment(assignmentId).get());
        			currentAssignment.setSubmissions(new HashMap<>());
        			currentCourse.getAssignments().put(assignmentId,
        					currentAssignment);
        		}

    			Submission currentSubmission = null;
    			Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
    			Integer studentId = (Integer) row.get("StudentId");
    			if (submissionDate != null) {
    				try {
    					currentSubmission = currentAssignment.getSubmissions().get(studentId);
    					if(currentSubmission==null)
    						throw new NullPointerException();
					} catch (NullPointerException e) {
    					currentSubmission = submissionDAO.getSubmission(
    							assignmentId,userId).get();
    					currentAssignment.getSubmissions().put(studentId,
    							currentSubmission);
    				}
    			}
    		} catch (NullPointerException e) {
    			continue;
    		}

    	}
	}
}
