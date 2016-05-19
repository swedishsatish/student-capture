package studentcapture.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.assignment.AssignmentDAO;
import studentcapture.course.HierarchyModel.AssignmentPackage;
import studentcapture.course.HierarchyModel.CoursePackage;
import studentcapture.user.UserDAO;
import studentcapture.submission.Submission;
import studentcapture.submission.SubmissionDAO;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository

/**
 * Now with slightly less code smell.
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
     * @param userID users identifier
     * @return hierarchy of course, assignment and submission data
     * @author tfy12hsm
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
            e.printStackTrace();
            return Optional.empty();
        } catch (DataAccessException e1) {
            e1.printStackTrace();
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
     * @author tfy12hsm
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
     * @author tfy12hsm
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
     * @author tfy12hsm
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
                + "par.userId=? AND par.function='student' AND ass.published "
                + "< current_timestamp";

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
            AssignmentPackage currentAssignment = addAssignmentToHierarchy(currentCourse,
                    assignmentId);

            Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
            Integer studentId = (Integer) row.get("StudentId");
            if (submissionDate != null) {
                addSubmissionToHierarchy(assignmentId, currentAssignment,
                        studentId);
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
            AssignmentPackage currentAssignment = addAssignmentToHierarchy(currentCourse,
                    assignmentId);

            Timestamp submissionDate = (Timestamp) row.get("SubmissionDate");
            Integer studentId = (Integer) row.get("StudentId");
            if (submissionDate != null) {
                addSubmissionToHierarchy(assignmentId, currentAssignment,
                        studentId);
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

    private AssignmentPackage addAssignmentToHierarchy(
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
            currentAssignment.setAssignment(assignmentDAO
                    .getAssignment(assignmentId).get());
            currentAssignment.setSubmissions(new HashMap<>());
            currentCourse.getAssignments()
                    .put(assignmentId, currentAssignment);
        }
        return currentAssignment;
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
