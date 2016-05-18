package studentcapture.assignment;

import java.util.InputMismatchException;

/**
 * Created by David Björkstrand on 4/25/16.
 * This class is the model for a assignment. I.e. it contains all data needed for an assignment.
 * Add more fields if needed.
 */
public class AssignmentModel {

    private String courseID;
    private String title;
    private String info;
    private AssignmentVideoIntervall videoIntervall;
    private AssignmentDateIntervalls assignmentIntervall;
    private GradeScale scale;
    private String recap;

    public AssignmentModel(String title,
                           String info,
                           AssignmentVideoIntervall videoIntervall,
                           AssignmentDateIntervalls assignmentIntervall,
                           String scale,
                           String recap) throws InputMismatchException {
        this.courseID = "1200"; //should be changed.
        this.title = title;
        this.info = info;
        this.videoIntervall = videoIntervall;
        this.assignmentIntervall = assignmentIntervall;
        this.scale = GradeScale.valueOf(scale);
        this.recap = recap;
    }

    public AssignmentModel() {
        this.courseID = "1200"; //should be changed.
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
}
