package studentcapture.video;

/**
 * Created by Beaverulf on 26/04/16.
 */
public class UserData {

    private String userID;
    private Long courseID;
    private String examID;


    public UserData addExamID(String examID) {
        this.examID = examID;
        return this;
    }

    public UserData addCourseID(Long courseID) {
        this.courseID = courseID;
        return this;
    }

    public UserData addUserID(String userID){
        this.userID = userID;
        return this;
    }

    public String getUserID() {
        return userID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public String getExamID() {
        return examID;
    }
}
