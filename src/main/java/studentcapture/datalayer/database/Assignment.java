package studentcapture.datalayer.database;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by c13elt on 2016-05-12.
 */
public class Assignment {

    private Integer assignmentID;
    private String courseID;
    private String title;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer minTime;
    private Integer maxTime;
    private Timestamp published;
    private String gradeScale;
    private String description;
    
    public Assignment() {
	}
    
    public Assignment(Map<String, Object> map) {
		assignmentID = (Integer) map.get("AssignmentId");
		courseID = (String) map.get("CourseId");
		title = (String) map.get("Title");
		startDate = (Timestamp) map.get("StartDate");
		endDate = (Timestamp) map.get("EndDate");
		minTime = (Integer) map.get("MinTime");
		maxTime = (Integer) map.get("MaxTime");
		try {
			published = (Timestamp) map.get("Published");
		} catch (NullPointerException e) {
			published = null;
		}
		gradeScale = (String) map.get("GradeScale");
		description = (String) map.get("Description");
	}

	public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

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

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
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

    public void setPublished(Timestamp published) {
        this.published = published;
    }

    public String getGradeScale() {
        return gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }
}