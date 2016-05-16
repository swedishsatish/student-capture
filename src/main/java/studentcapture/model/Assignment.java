package studentcapture.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Map;

import studentcapture.assignment.GradeScale;

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
    private GradeScale scale;
    private String recap;
    
    public Assignment() {
	}
    
    /**
     * Constructor that parses map of database elements.
     * 
     * @param map	map of database elements
     * 
     * @author tfy12hsm
     */
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

    public String getStartDate() {
        return startDate.toString().replace('T', ' ') + ":00";
    }
    
    public void setStartDate(String startDate) {
        startDate = startDate.replace(' ', 'T');
        startDate = startDate.substring(0, 16);
        this.startDate = Timestamp.valueOf(LocalDateTime.parse(startDate));
        //validateStartEndTime(this.startDate, this.endDate);
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

    public void setMinTime(int minTimeSeconds) throws InputMismatchException {
        this.minTime = minTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTime);
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTimeSeconds(int maxTimeSeconds) throws InputMismatchException {
        this.maxTime = maxTimeSeconds;
        validateMinMaxTimeSeconds(minTime, maxTimeSeconds);
    }

    public String getPublished() {
        return published.toString().replace('T', ' ') + ":00";
    }

    public void setPublished(String publishDate) {
        publishDate = publishDate.replace(' ', 'T');
        publishDate = publishDate.substring(0, 16);
        this.published = Timestamp.valueOf(LocalDateTime.parse(publishDate));
        validatePublishAndStartTime(this.startDate, this.published);
    }

    public String getGradeScale() {
        return gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }

	/**
	 * @return the scale
	 */
    public String getScale() {
        return scale.name();
    }

    public void setScale(String scale) {
        this.scale = GradeScale.valueOf(scale);
    }

	/**
	 * @return the recap
	 */
	public String getRecap() {
		return recap;
	}

	/**
	 * @param recap the recap to set
	 */
	public void setRecap(String recap) {
		this.recap = recap;
	}
	
	private void validateStartEndTime(Timestamp startDate,
            Timestamp endDate)
            		throws InputMismatchException {
		if (startDate.after(endDate)) {
				throw new InputMismatchException("Start Time is after end time, Start time was " + startDate +
						" and end time " + endDate);
		}
	}

	private void validatePublishAndStartTime(Timestamp startDate,
                   Timestamp publishDate)
                		   throws InputMismatchException {
		if (publishDate.after(startDate)) {
				throw new InputMismatchException("Publish time is after Start time, Start time was " + startDate +
						" and publish time " + publishDate);
		}
	}

	private void validateMinMaxTimeSeconds(int minTimeSeconds, int maxTimeSeconds) {
		if ((minTimeSeconds > maxTimeSeconds) && (maxTimeSeconds != 0)) {
				throw new InputMismatchException(
			"Minimum time can't be larger than max time." +
				" Input was, min: " + minTimeSeconds +
				" max: " + maxTimeSeconds);
		}
	}
}