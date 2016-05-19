package studentcapture.assignment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by David Björkstrand on 4/25/16.
 * This class is the model for a assignment. I.e. it contains all data needed for an assignment.
 * Add more fields if needed.
 */
public class AssignmentModel {
	private Integer assignmentID;
    private String courseID;
    private String title;
    private String description;
    private AssignmentVideoIntervall videoIntervall;
    private AssignmentDateIntervalls assignmentIntervall;
    private GradeScale scale;
    private String recap;
    private Timestamp published;
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public AssignmentModel(String title,
                           String description,
                           AssignmentVideoIntervall videoIntervall,
                           AssignmentDateIntervalls assignmentIntervall,
                           String scale,
                           String recap) throws InputMismatchException {
        this.courseID = "1200"; //should be changed.
        this.title = title;
        this.description = description;
        this.videoIntervall = videoIntervall;
        this.assignmentIntervall = assignmentIntervall;
        this.scale = GradeScale.valueOf(scale);
        this.recap = recap;
    }

    public AssignmentModel() {


    }

    public AssignmentModel(Map<String, Object> map) {
    	assignmentID = (Integer) map.get("AssignmentId");
		courseID = (String) map.get("CourseId");
		title = (String) map.get("Title");
		assignmentIntervall = new AssignmentDateIntervalls();
		assignmentIntervall.setStartDate(FORMATTER.format((Timestamp) map.get("StartDate")));
		assignmentIntervall.setEndDate(FORMATTER.format((Timestamp) map.get("EndDate")));
		videoIntervall = new AssignmentVideoIntervall();
		videoIntervall.setMinTimeSeconds((Integer) map.get("MinTime"));
		videoIntervall.setMaxTimeSeconds((Integer) map.get("MaxTime"));
		try {
			published = (Timestamp) map.get("Published");
		} catch (NullPointerException e) {
			published = null;
		}
		description = (String) map.get("Description");
		try {
			scale = (GradeScale) map.get("Scale");
			if(scale == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			scale = GradeScale.NUMBER_SCALE;
		}
	}

	public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setVideoIntervall(AssignmentVideoIntervall videoIntervall) {
        this.videoIntervall = videoIntervall;
    }

    public AssignmentVideoIntervall getVideoIntervall() {
        return videoIntervall;
    }

    public void setAssignmentIntervall(AssignmentDateIntervalls assignmentIntervall) {
        this.assignmentIntervall = assignmentIntervall;
    }

    public AssignmentDateIntervalls getAssignmentIntervall() {
        return assignmentIntervall;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScale() {
        return scale.name();
    }

    public void setScale(String scale) {
        this.scale = GradeScale.valueOf(scale);
    }

    public String getRecap() {
        return recap;
    }

    public void setRecap(String recap) {
        this.recap = recap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentModel that = (AssignmentModel) o;

        if (assignmentID != null ? !assignmentID.equals(that.assignmentID) : that.assignmentID != null)
            return false;
        if (courseID != null ? !courseID.equals(that.courseID) : that.courseID != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (videoIntervall != null ? !videoIntervall.equals(that.videoIntervall) : that.videoIntervall != null)
            return false;
        if (assignmentIntervall != null ? !assignmentIntervall.equals(that.assignmentIntervall) : that.assignmentIntervall != null)
            return false;
        if (scale != that.scale) return false;
        return recap != null ? recap.equals(that.recap) : that.recap == null;

    }

    @Override
    public int hashCode() {
        int result = assignmentID != null ? assignmentID.hashCode() : 0;
        result = 31 * result + (courseID != null ? courseID.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (videoIntervall != null ? videoIntervall.hashCode() : 0);
        result = 31 * result + (assignmentIntervall != null ? assignmentIntervall.hashCode() : 0);
        result = 31 * result + (scale != null ? scale.hashCode() : 0);
        result = 31 * result + (recap != null ? recap.hashCode() : 0);
        return result;
    }
}
