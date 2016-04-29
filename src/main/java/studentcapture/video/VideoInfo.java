package studentcapture.video;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Beaverulf on 26/04/16.
 */
public class VideoInfo {
    private String courseID;
    private String assignmentID;
    private String studentID;
    private String title;
    private String startDate;
    private String endDate;
    private String minTime;
    private String maxTime;
    private String published;
    private MultipartFile videoFile;

    public VideoInfo(String title, String startDate, String endDate, String minTime, String maxTime, String published, String courseID, MultipartFile video) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.published = published;
        this.courseID = courseID;
        this.videoFile = video;
    }

    public VideoInfo(String courseID, String assignmentID, String studentID, MultipartFile videoFile) {
        this.courseID = courseID;
        this.assignmentID = assignmentID;
        this.studentID = studentID;
        this.videoFile = videoFile;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMinTime() {
        return minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public String getPublished() {
        return published;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }
}