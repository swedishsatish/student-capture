package studentcapture.datalayer.database;

import java.util.Date;

/**
 * Created by c13elt on 2016-05-12.
 */
public class Assignment {

    private int assignmentID;

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    private String courseID;
    private String title;
    private Date startDate;
    private Date endDate;
    private int minTime;
    private int maxTime;
    private Date published;
    private String gradeScale;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getGradeScale() {
        return gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }
}