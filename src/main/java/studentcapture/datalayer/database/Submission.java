package studentcapture.datalayer.database;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Map;
/**
 * Created by c13arm on 2016-05-11.
 */

public class Submission {
    @NotNull
	private Integer assignmentID;
    @NotNull
    private Integer studentID;
    private Boolean studentPublishConsent = false;
    private Timestamp submissionDate;
    private Grade grade;
    private Integer teacherID;
    private Boolean publishStudentSubmission = false;
    private String courseID;
    private String courseCode;
    private String feedback;

    public Submission() {
    }
    
    public Submission (int studentID, int assignmentID) {
        this.studentID = studentID;
        this.assignmentID = assignmentID;
    }

    /**
     * Constructor that parses map of database elements.
     * 
     * @param map		map retrieved from database
     * 
     * @author tfy12hsm
     */
    public Submission(Map<String, Object> map) {
    	assignmentID = (Integer) map.get("AssignmentId");
		studentID = (Integer) map.get("StudentId");
		try {
			studentPublishConsent = (Boolean) map.get("StudentPublishConsent");
		} catch (NullPointerException e) {
		}
		submissionDate = (Timestamp) map.get("SubmissionDate");
		//grade = ;
		try {
			teacherID = (Integer) map.get("TeacherId");
		} catch (NullPointerException e) {
			teacherID = null;
		}
		try {
			publishStudentSubmission = (Boolean) map.get("PublishStudentSubmission");
		} catch (NullPointerException e) {
		}
	}

    public String getCourseID() {
		return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    public void studentAllowsPublication(boolean studentPublishConsent) {
        this.studentPublishConsent = studentPublishConsent;
    }

    public int getStudentID() { return studentID; }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public boolean getStudentPublishConsent() {
        return studentPublishConsent;
    }

    public void setStudentPublishConsent(boolean studentPublishConsent) {
        this.studentPublishConsent = studentPublishConsent;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
