package studentcapture.feedback;


/** This model takes care of the feedback of an assignment. It is used to gather
 * all information possible.
 *
 * @author Group6
 * @version 0.1
 * @see Submission
*/
public class FeedbackModel {
    private int studentID, assignmentID, courseID;
    private String grade, feedbackText;


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
     * @return will set the grade of the Submission
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade Returns the grade of the assignment.
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
    public int getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID of the feedback.
     */
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
