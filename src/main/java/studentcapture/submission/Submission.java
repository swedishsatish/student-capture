package studentcapture.submission;

import studentcapture.model.Grade;

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
    private String gradeSign;
    private Integer teacherID;
    private Boolean publishStudentSubmission = false;
    private String courseID;
    private String courseCode;
    private String feedback;
    private Status subStatus;
    private String firstName;
    private String lastName;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //A submission must have one of these statuses
    public enum Status {
        ANSWER("Answer"),
        NOANSWER("NoAnswer"),
        BLANK("Blank");

        Status(String status) {

        }
    }

    public Submission(int studentID, int assignmentID) {
        this.studentID = studentID;
        this.assignmentID = assignmentID;
    }

    public Submission() {
    }

    public String getGradeSign() {
        return gradeSign;
    }

    public void setGradeSign(String gradeSign) {
        this.gradeSign = gradeSign;
    }

    /**
     * Constructor that parses map of database elements.
     *
     * @param map map retrieved from database
     * @author tfy12hsm
     */
    public Submission(Map<String, Object> map) {
        // These three variables (assignmentID, studentID, submissionDate) cannot be null.
        assignmentID = (Integer) map.get("AssignmentId");
        studentID = (Integer) map.get("StudentId");
        submissionDate = (Timestamp) map.get("SubmissionDate");

        try {
            firstName = (String) map.get("FirstName"); // Upper/lower-case doesn't matter
        } catch (NullPointerException e) {
            firstName = null;
        }

        try {
            lastName = (String) map.get("LastName");
        } catch (NullPointerException e) {
            lastName = null;
        }

        try {
            studentPublishConsent = (Boolean) map.get("StudentPublishConsent");
        } catch (NullPointerException e) {
            studentPublishConsent = null;
        }

        try {
            gradeSign = (String) map.get("Grade");
//        grade.setGrade((String) map.get("Grade"));
        } catch (NullPointerException e) {
            gradeSign = null;
        }

        try {
            status = (String) map.get("Status");
        } catch (NullPointerException e) {
            status = null;
        }
        try {
            teacherID = (Integer) map.get("TeacherId");
        } catch (NullPointerException e) {
            teacherID = null;
        }
        try {
            publishStudentSubmission = (Boolean) map.get("PublishStudentSubmission");
        } catch (NullPointerException e) {
            publishStudentSubmission = null;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getStudentID() {
        return studentID;
    }

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

    public Status getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Status subStatus) {
        this.subStatus = subStatus;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "assignmentID=" + assignmentID +
                ", studentID=" + studentID +
                ", studentPublishConsent=" + studentPublishConsent +
                ", submissionDate=" + submissionDate +
                ", grade=" + grade +
                ", teacherID=" + teacherID +
                ", publishStudentSubmission=" + publishStudentSubmission +
                ", courseID='" + courseID + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", feedback='" + feedback + '\'' +
                ", subStatus=" + subStatus +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
