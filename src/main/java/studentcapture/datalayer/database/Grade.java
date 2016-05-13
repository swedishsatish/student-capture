package studentcapture.datalayer.database;

import java.util.Date;

/**
 * Created by c13arm on 2016-05-11.
 */
public class Grade {
    private String grade;
    private int teacherID;
    private Date date;
    private boolean publishStudentSubmission = false;
    private boolean publishFeedback = false;

    public Grade(String grade, int teacherID) {
        this.grade = grade;
        this.teacherID = teacherID;
        this.date = new Date();
    }

    public boolean getPublishFeedback() { return publishFeedback; }

    public void setPublishFeedback(boolean publishFeedback) { this.publishFeedback = publishFeedback; }

    public boolean getPublishStudentSubmission() {
        return publishStudentSubmission;
    }

    public void setPublishStudentSubmission(boolean publishStudentSubmission) {
        this.publishStudentSubmission = publishStudentSubmission;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        this.date = new Date();
    }
}
