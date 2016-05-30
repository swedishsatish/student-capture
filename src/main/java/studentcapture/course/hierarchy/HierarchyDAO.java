package studentcapture.course.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.assignment.AssignmentModel;
import studentcapture.course.CourseDAO;
import studentcapture.course.CourseModel;
import studentcapture.course.hierarchy.HierarchyModel.AssignmentPackage;
import studentcapture.course.hierarchy.HierarchyModel.CoursePackage;
import studentcapture.submission.Submission;
import studentcapture.submission.SubmissionDAO;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository

/**
 * HierarchyDAO is used to 
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


    /**
     * Returns an hierarchy of data, retrieved from the database, related to
     * courses, assignments and submissions a user is participating in.
     *
     * @param userId users identifier
     * @return hierarchy of course, assignment and submission data
     */
    public Optional<HierarchyModel> getCourseAssignmentHierarchy(
            Integer userId) {
        HierarchyModel hierarchy = new HierarchyModel();
        try {
            addStudentHierarchy(hierarchy, userId);
            addTeacherHierarchy(hierarchy, userId);
            addUserToHierarchy(hierarchy, userId);
            //hierarchy.moveMapsToLists();
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e1) {
            return Optional.empty();
        }

        return Optional.of(hierarchy);
    }

    /**
     * Adds user data, related to a given user,
     * from the database to a {@link CourseAssignmentHierarchy}
     *
     * @param hierarchy hierarchy added to
     * @param userId    student identifier
     */
    private void addUserToHierarchy(HierarchyModel hierarchy,
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
     * @param hierarchy hierarchy added to
     * @param userId    teacher identifier
     */
    private void addTeacherHierarchy(HierarchyModel hierarchy, int
            userId) {

        String getTeacherHierarchyStatement = "SELECT par.courseId AS "
                + "CourseId,ass.assignmentId AS AssignmentId,"
                + "sub.submissionDate AS SubmissionDate,sub.studentId"
                + " AS StudentId FROM Participant AS par"
                + " LEFT JOIN Course AS cou ON par.courseId="
                + "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
                + "ass.courseId LEFT JOIN Submission AS sub ON "
                + "ass.assignmentId=sub.assignmentId WHERE par.userId=? AND "
                + "par.function='teacher'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                getTeacherHierarchyStatement, userId);
        for (Map<String, Object> row : rows) {
            addTeacherMapToHierarchy(hierarchy, row);
        }
    }

    /**
     * Adds course, assignment and submission data, related to a given student,
     * from the database to a {@link CourseAssignmentHierarchy}
     *
     * @param hierarchy hierarchy added to
     * @param userId    student identifier
     */
    private void addStudentHierarchy(HierarchyModel hierarchy,
                                     int userId) {
        String getStudentHierarchyStatement = "SELECT par.courseId AS CourseId,"
                + "ass.assignmentId AS AssignmentId,sub.submissionDate AS "
                + "SubmissionDate,sub.studentId AS StudentId "
                + "FROM Participant AS par LEFT JOIN Course AS"
                + " cou ON par.courseId="
                + "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
                + "ass.courseId LEFT JOIN Submission AS sub ON par.userId="
                + "sub.studentId AND ass.assignmentId=sub.assignmentId WHERE "
                + "par.userId=? AND par.function='student'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                getStudentHierarchyStatement, userId);
        for (Map<String, Object> row : rows) {
            addStudentMapToHierarchy(hierarchy, row);
        }
    }

    private void addTeacherMapToHierarchy(HierarchyModel hierarchy,
                                          Map<String, Object> row) {
        Integer courseId = (Integer) row.get("CourseId");
        CoursePackage currentCourse = addCourseToHierarchy(hierarchy.getTeacherCourses(), courseId);

        try {
        	int assignmentId = (int) row.get("AssignmentId");
	        Optional<AssignmentPackage> currentAssignment = addAssignmentToTeacherHierarchy(currentCourse,
	                assignmentId);
	        if(currentAssignment.isPresent()) {
	
	            Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
	            Integer studentId = (Integer) row.get("StudentId");
	            if (submissionDate != null) {
	                addSubmissionToHierarchy(assignmentId, currentAssignment.get(),
	                        studentId);
	            }
	        }
        } catch (NullPointerException e) {
	    	return;
	    }
    }

    private void addStudentMapToHierarchy(HierarchyModel hierarchy,
                                          Map<String, Object> row) {
        Integer courseId = (Integer) row.get("CourseId");
        CoursePackage currentCourse = addCourseToHierarchy(hierarchy.getStudentCourses(), courseId);
        
        try {
        	int assignmentId = (int) row.get("AssignmentId");
        	Optional<AssignmentPackage> currentAssignment = addAssignmentToStudentHierarchy(currentCourse,
                    assignmentId);
            if(currentAssignment.isPresent()) {
                Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
                Integer studentId = (Integer) row.get("StudentId");
                if (submissionDate != null) {
                    addSubmissionToHierarchy(assignmentId, currentAssignment.get(),
                            studentId);
                }
            }
        } catch (NullPointerException e) {
        	return;
        }
        
    }

    private void addSubmissionToHierarchy(int assignmentId,
                                          AssignmentPackage currentAssignment, Integer studentId) {
        Submission currentSubmission;
        try {
            currentSubmission = currentAssignment.getSubmissions().get(studentId);
            if (currentSubmission == null)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            currentSubmission = submissionDAO.getSubmission(
                    assignmentId, studentId).get();
            currentAssignment.getSubmissions().put(studentId,
                    currentSubmission);
        }
    }

    private Optional<AssignmentPackage> addAssignmentToTeacherHierarchy(
            CoursePackage currentCourse, int assignmentId) {
        AssignmentPackage currentAssignment;
        try {
            currentAssignment = currentCourse.getAssignments()
                    .get(assignmentId);

            if (currentAssignment == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            currentAssignment = new AssignmentPackage();
            Optional<AssignmentModel> assignment = assignmentDAO
                    .getAssignment(assignmentId);
            if(assignment.isPresent()) {
            	currentAssignment.setAssignment(assignment.get());
	            currentAssignment.setSubmissions(new HashMap<>());
	            currentCourse.getAssignments()
	                    .put(assignmentId, currentAssignment);
            } else {
            	return Optional.empty();
            }
	        
        }
        return Optional.of(currentAssignment);
    }
    
    private Optional<AssignmentPackage> addAssignmentToStudentHierarchy(
            CoursePackage currentCourse, int assignmentId) {
        AssignmentPackage currentAssignment;
        try {
            currentAssignment = currentCourse.getAssignments()
                    .get(assignmentId);

            if (currentAssignment == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            currentAssignment = new AssignmentPackage();
            Optional<AssignmentModel> assignment = assignmentDAO
                    .getPublishedAssignment(assignmentId);
            if(assignment.isPresent()) {
            	currentAssignment.setAssignment(assignment.get());
	            currentAssignment.setSubmissions(new HashMap<>());
	            currentCourse.getAssignments()
	                    .put(assignmentId, currentAssignment);
            } else {
            	return Optional.empty();
            }
	        
        }
        return Optional.of(currentAssignment);
    }

    private CoursePackage addCourseToHierarchy(
            Map<Integer, CoursePackage> courses, Integer courseId) {
        CoursePackage currentCourse;
        try {
            currentCourse = courses.get(courseId);
            if (currentCourse == null)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            currentCourse = new CoursePackage();
            CourseModel course = new CourseModel();
            course.setCourseId(courseId);
            currentCourse.setCourse(courseDAO.getCourse(course));
            courses.put(courseId, currentCourse);
        }
        return currentCourse;
    }
}
