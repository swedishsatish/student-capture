package studentcapture.datalayer.database;

/**
 * Created by c13arm on 2016-05-11.
 */
public class Submission {
    private int studentID;
    private int assignmentID;
    private boolean studentPublishConsent = false;
    private Grade grade;

    public Submission (int studentID, int assignmentID) {
        this.studentID = studentID;
        this.assignmentID = assignmentID;
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
}