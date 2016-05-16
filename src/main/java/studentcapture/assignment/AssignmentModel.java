package studentcapture.assignment;

import java.time.LocalDateTime;
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
    private int minTimeSeconds;
    private int maxTimeSeconds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime publishDate;
    private GradeScale scale;
    private String recap;

    public AssignmentModel(String title,
                           String info,
                           int minTimeSeconds,
                           int maxTimeSeconds,
                           String startDate,
                           String endDate,
                           String publishDate,
                           String scale,
                           String recap) throws InputMismatchException {
        this.courseID = "1000"; //should be changed.
        this.title = title;
        this.info = info;
        this.minTimeSeconds = minTimeSeconds;
        this.maxTimeSeconds = maxTimeSeconds;
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setPublished(publishDate);
        this.scale = GradeScale.valueOf(scale);
        this.recap = recap;

        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
        validateStartEndTime(this.startDate, this.endDate);
    }

    public AssignmentModel() {
        this.courseID = "1000"; //should be changed.
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getPublished() {
        return publishDate.toString().replace('T', ' ') + ":00";
    }

    public void setPublished(String publishDate) {
        publishDate = publishDate.replace(' ', 'T');
        publishDate = publishDate.substring(0, 16);
        this.publishDate = LocalDateTime.parse(publishDate);
        validatePublishAndStartTime(this.startDate, this.publishDate);
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

    public int getMinTimeSeconds() {
        return minTimeSeconds;
    }

    public void setMinTimeSeconds(int minTimeSeconds) throws InputMismatchException {
        this.minTimeSeconds = minTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public int getMaxTimeSeconds() {
        return maxTimeSeconds;
    }

    public void setMaxTimeSeconds(int maxTimeSeconds) throws InputMismatchException {
        this.maxTimeSeconds = maxTimeSeconds;
        validateMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    public String getEndDate() {
        return endDate.toString().replace('T', ' ') + ":00";
    }

    public void setEndDate(String endDate) {
        endDate = endDate.replace(' ', 'T');
        endDate = endDate.substring(0, 16);
        this.endDate = LocalDateTime.parse(endDate);
        validateStartEndTime(this.startDate, this.endDate);
    }

    public String getStartDate() {
        return startDate.toString().replace('T', ' ') + ":00";
    }

    public void setStartDate(String startDate) {
        startDate = startDate.replace(' ', 'T');
        startDate = startDate.substring(0, 16);
        this.startDate = LocalDateTime.parse(startDate);
        //validateStartEndTime(this.startDate, this.endDate);
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

    private void validateStartEndTime(LocalDateTime startDate,
                                      LocalDateTime endDate)
            throws InputMismatchException {
        if (startDate.isAfter(endDate)) {
            throw new InputMismatchException("Start Time is after end time, Start time was " + startDate +
                    " and end time " + endDate);
        }
    }

    private void validatePublishAndStartTime(LocalDateTime startDate,
                                             LocalDateTime publishDate)
            throws InputMismatchException {
        if (publishDate.isAfter(startDate)) {
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
