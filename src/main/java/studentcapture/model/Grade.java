package studentcapture.model;

import java.util.Date;

/**
 * Created by c13arm on 2016-05-11.
 */
public class Grade {
    private String grade;
    private Integer teacherID;
    private Date date;
    private boolean publishStudentSubmission = false;
    private boolean publishFeedback = false;

    public Grade(String grade, Integer teacherID) {
        this.grade = grade;
        this.teacherID = teacherID;
        this.date = new Date();
    }

    public Grade() {}

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

    public Integer getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(Integer teacherID) {
        this.teacherID = teacherID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        this.date = new Date();
    }
}
