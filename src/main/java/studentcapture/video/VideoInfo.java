package studentcapture.video;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Beaverulf on 26/04/16.
 */
public class VideoInfo {

    private MultipartFile videoFile;
    private String userID;
    private String videoName;

    public VideoInfo(MultipartFile videoFile, String userID, String videoName) {
        this.videoFile = videoFile;
        this.userID = userID;
        this.videoName = videoName;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public String getUserID() {
        return userID;
    }

    public String getVideoName() {
        return videoName;
    }

    /*private UserData ud;
    private Long videoSize;
    private VideoTypeEnum vidType;

    public UserData getUd() {
        return ud;
    }

    public void setUd(UserData ud) {
        this.ud = ud;
    }

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    public VideoTypeEnum getVidType() {
        return vidType;
    }

    public void setVidType(VideoTypeEnum vidType) {
        this.vidType = vidType;
    }*/
}
