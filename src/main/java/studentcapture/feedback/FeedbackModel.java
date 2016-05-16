package studentcapture.feedback;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This model takes care of the feedback of an assignment. It is used to gather
 * all information possible.
 *
 * @author Group6
 * @version 0.1
 * @see studentcapture.model.Submission
 */
public class FeedbackModel {
    @NotNull @Min(1)
    private int studentID;

    @NotNull @Min(1)
    private int assignmentID;

    @Size(min=1, max=10)
    private String courseID;

    @Size(min=1, max=6)
    private String courseCode;

    private String grade, feedbackText;

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    private String teacherID;


    /**
     * @return the StudentID of the feedback.
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * @param studentID set the studentID of feedback.
     */
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    /**
     * @return the AssigmentID of the feedback
     */
    public int getAssignmentID() {
        return assignmentID;
    }

    /**
     * @param assignmentID sets the assignmentID
     */
    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    /**
     * @return Returns the grade of the assignment.
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade will set the grade of the Submission
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return the given feedback text appointed towards the submission.
     */
    public String getFeedbackText() {
        return feedbackText;
    }

    /**
     * @param feedbackText The feedback in text form by the user.
     */
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    /**
     * @return courseID of the feedback.
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID of the feedback.
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    /**
     * @return teacherID of the current feedback.
     */
    public String getTeacherID() {
        return teacherID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
