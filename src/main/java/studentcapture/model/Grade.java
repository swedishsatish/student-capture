package studentcapture.model;

import java.util.Date;

/**
 * Created by c13arm on 2016-05-11.
 */
public class Grade {
    private String grade;
    private Integer teacherID;
    private Date date;
    private Boolean feedbackIsVisible;

    public Grade(String grade, Integer teacherID) {
        this.grade = grade;
        this.teacherID = teacherID;
        this.date = new Date();
    }

    public Grade() {}

    public void setFeedbackIsVisible(boolean feedbackIsVisible){
        this.feedbackIsVisible = feedbackIsVisible;
    }

    public Boolean getFeedbackIsVisible() { return feedbackIsVisible; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;

        Grade grade1 = (Grade) o;

        if (feedbackIsVisible != grade1.feedbackIsVisible) return false;
        if (grade != null ? !grade.equals(grade1.grade) : grade1.grade != null)
            return false;
        if (teacherID != null ? !teacherID.equals(grade1.teacherID) : grade1.teacherID != null)
            return false;
        return date != null ? date.equals(grade1.date) : grade1.date == null;

    }

    @Override
    public int hashCode() {
        int result = grade != null ? grade.hashCode() : 0;
        result = 31 * result + (teacherID != null ? teacherID.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (feedbackIsVisible ? 1 : 0);
        return result;
    }
}
