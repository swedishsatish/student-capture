package studentcapture.video;

/**
 * Created by Beaverulf on 26/04/16.
 */
public class VideoInfo {

    private UserData ud;

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
    }
}
