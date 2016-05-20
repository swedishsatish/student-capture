package studentcapture.model;

import java.util.Date;

/**
 * Created by c13arm on 2016-05-11.
 */
public class Grade {
    private String grade;
    private Integer teacherID;
    private Date date;
    private boolean feedbackIsVisible = false;

    public Grade(String grade, Integer teacherID) {
        this.grade = grade;
        this.teacherID = teacherID;
        this.date = new Date();
    }

    public Grade() {}

    public void setFeedbackIsVisible(boolean feedbackIsVisible){
        this.feedbackIsVisible = feedbackIsVisible;
    }

    public boolean getFeedbackIsVisible() { return feedbackIsVisible; }

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

    @java.lang.Override
    public java.lang.String toString() {
        return "Grade{" +
                "grade='" + grade + '\'' +
                ", teacherID=" + teacherID +
                ", date=" + date +
                ", feedbackIsVisible=" + feedbackIsVisible +
                '}';
    }
}
